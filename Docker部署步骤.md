# Docker 部署步骤

## 1. 部署架构

项目面向 Linux x86_64 服务器部署，服务器可使用 Rocky Linux 9.4。

- `frontend`：Vue 3 构建后由 Nginx 托管
- `backend`：Rocky Linux 9 + Java 17 + Spring Boot
- `mysql`：MySQL 8.0

后端镜像内包含：

- NCBI BLAST+ 2.15.0
- MAFFT 7.526
- IQ-TREE 3.1.2
- Python 3、ETE3、PyQt5

后端直接在容器内执行生信软件，不使用 SSH、SFTP 或外部虚拟机。

## 2. 关键目录

```text
frontend/                 Vue 前端
backend/                  Spring Boot 后端
bio_software/             生信软件 Linux x86_64 安装包
bio_data/references/      原始参考 FASTA
bio_data/blast_db/        BLAST 索引
data/bio_jobs/            计算任务临时目录
data/files/               上传文件持久化目录
scripts/                  Python 和维护脚本
db/init/                  MySQL 初始化 SQL
```

参考 FASTA：

```text
bio_data/references/cryptosporidium-18s-reference.fasta
bio_data/references/cryptosporidium-gp60-reference.fasta
```

## 3. 环境变量

复制配置模板：

```bash
cp .env.example .env
```

至少修改数据库密码：

```env
MYSQL_ROOT_PASSWORD=替换为安全的root密码
MYSQL_DATABASE=shouyi
MYSQL_USER=shouyi
MYSQL_PASSWORD=替换为安全的应用密码
```

生信工具默认配置：

```env
BIO_BLAST_PATH=/usr/local/bin/blastn
BIO_MAFFT_PATH=/usr/bin/mafft
BIO_IQTREE_PATH=/usr/local/bin/iqtree3
BIO_JOB_DIR=/app/bio-jobs
BIO_18S_DATABASE=/app/bio-data/blast_db/cryptosporidium-18s
BIO_GP60_DATABASE=/app/bio-data/blast_db/cryptosporidium-gp60
BIO_18S_REFERENCE=/app/bio-data/references/cryptosporidium-18s-reference.fasta
BIO_GP60_REFERENCE=/app/bio-data/references/cryptosporidium-gp60-reference.fasta
```

## 4. 构建后端镜像

```bash
docker compose build backend
```

构建过程会验证：

```text
Java 17
Python / ETE3 / PyQt5
blastn / makeblastdb / blastdbcmd
mafft
iqtree3
```

## 5. 生成 BLAST 索引

首次部署或更新参考 FASTA 后执行：

```bash
docker compose --profile tools run --rm bio-db-init
```

生成结果位于：

```text
bio_data/blast_db/cryptosporidium-18s.*
bio_data/blast_db/cryptosporidium-gp60.*
```

## 6. 启动服务

```bash
docker compose up -d
```

首次启动时 MySQL 会导入：

```text
db/init/shouyi.sql
```

数据库导入完成后，后端才会启动。

查看状态：

```bash
docker compose ps
```

查看日志：

```bash
docker compose logs -f backend
docker compose logs -f mysql
docker compose logs -f frontend
```

## 7. 生信 API 冒烟测试

后端正常启动后执行：

```bash
docker compose exec -T backend python3 /app/scripts/smoke_test_bio_api.py
```

测试覆盖：

```text
参考序列查询
BLAST
MAFFT
IQ-TREE
Newick 树图 PNG 渲染
```

## 8. 停止与更新

停止服务并保留数据库：

```bash
docker compose down
```

代码或镜像更新后：

```bash
docker compose build
docker compose up -d
```

不要在生产环境执行 `docker compose down -v`，该命令会删除 MySQL 数据卷。
