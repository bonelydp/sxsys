<template>
  <div class="resource-share-container">
    <div class="resource-card">
      <h2>{{ t('message.dataResource') }}</h2>
      <ul>
        <li v-for="(item, index) in dataResources" :key="index">
          <a href="javascript:void(0)" @click="handleDownload(item)">
            {{ item.name }}
            <el-icon class="download-icon"><Download /></el-icon>
          </a>
        </li>
      </ul>
    </div>
    <div class="resource-card">
      <h2>{{t('message.bookResource')}}</h2>
      <!-- 这里可以根据需要添加书籍资源 -->
    </div>
    <div class="resource-card">
      <h2>{{t('message.articleResource')}}</h2>
      <ul>
        <li>
          <a href="https://doi.org/10.1038/s41467-025-60088-7" target="_blank">
            SKSR1 identified as key virulence factor in Cryptosporidium by genetic crossing
          </a>
        </li>
        <!-- 这里可以根据需要添加书籍资源 -->
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'

const { t } = useI18n()

// 数据资源
const dataResources = ref([
  {
    name: 'SSU rRNA标准序列库',
    fileName: 'ssu-rna.fasta',
    path: '/resources/18S-参考基因组-2024-11-24.fasta'
  },
  {
    name: 'gp60数据库',
    fileName: 'gp60-database.fasta',
    path: '/resources/Cry-GP60-2024-11-24-核对.fasta'
  }
])

// 处理下载
const handleDownload = async (item) => {
  try {
    const response = await fetch(item.path)
    if (!response.ok) {
      throw new Error('文件下载失败')
    }
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = item.fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('文件下载成功')
  } catch (error) {
    console.error('下载错误:', error)
    ElMessage.error('文件下载失败：' + error.message)
  }
}
</script>

<style scoped>
.resource-share-container {
  margin-bottom: -100px;
  width: 100%;
  height: 700px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #747474;
  background-image: url('@/assets/resourceBackground.png');
  background-size: cover;
  /* 确保背景图片覆盖整个容器 */
}

.resource-card {
  background-color: #f9f9f9;
  border-radius: 10px;
  padding: 20px;
  margin: 20px 0;
  width: 80%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-top: 60px;
}

.resource-card h2 {
  font-size: 20px;
  margin-bottom: 10px;
  margin-left: 20px;
}

.resource-card ul {
  list-style-type: none;
  padding: 0;

}

.resource-card li {
  margin-bottom: 15px;

}

.resource-card li a:hover {
  color: #409EFF;
}
.resource-card li a {
  color: black;
  text-decoration: none;
}

.resource-card li::before {
  content: '>';
  margin-right: 10px;
  color: #666;
}

/* 添加下载图标悬停效果 */
.resource-card li a:hover .download-icon {
  color: #409EFF;
  transform: translateY(2px);
  transition: transform 0.3s;
}
</style>