# Shouyi Platform

隐孢子虫数据管理与生物信息分析平台。

## 项目结构

- `frontend/`：Vue 3 前端
- `backend/`：Spring Boot 后端
- `scripts/`：后端本地调用的 Python 脚本
- `bio_software/`：BLAST、MAFFT、IQ-TREE 的 Linux x86_64 安装包
- `bio_data/references/`：原始生物学参考序列
- `bio_data/blast_db/`：由参考序列生成的 BLAST 索引
- `db/init/`：MySQL Docker 初始化 SQL
- `data/files/`：Docker 部署时的上传文件持久化目录
- `data/bio_jobs/`：生信计算任务工作目录
- `docker-compose.yml`：完整服务编排

## 关键计算依赖

- BLAST、MAFFT、IQ-TREE 均在 Rocky Linux 后端容器内直接运行。
- 参考 FASTA 与 BLAST 索引通过 `bio_data/` 只读挂载到后端容器。
- 每次计算使用 `data/bio_jobs/` 下的独立临时目录，完成后自动清理。
- 系统发育树图片由 `scripts/visualize_tree.py` 在后端运行环境中生成。
