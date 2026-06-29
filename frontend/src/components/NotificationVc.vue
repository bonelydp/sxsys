<template>
  <div class="notification-item" @click="handleClick">
    <el-image v-if="props.image" :src="props.image" fit="cover" style="width: 205px; height: 115px;"></el-image>
    <div class="content">
      <h3>{{ props.title }}</h3>
      <p class="date">{{ props.date }}</p>
      <p class="description">{{ truncateContent(props.description) }}</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

// 定义 props
const props = defineProps({
  image: {
    type: String,
    required: false,
  },
  title: {
    type: String,
    required: true,
  },
  date: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    required: true,
  },
  id:{
    type:[Number,String],
    required:true,
  },
  isShow:{
    type:Boolean,
    required:true
  }
});

import {useRouter} from "vue-router"
const router = useRouter()
import { useNoticeStore } from '@/store/notice';
const noticeStore = useNoticeStore()
// 定义点击事件处理函数
const handleClick = () => {
  noticeStore.setInfo(props)
  router.push("/notifications/notificationdetail")
};

import DOMPurify from 'dompurify'; // 确保你安装了 dompurify
// 定义一个方法来净化富文本内容并截取前20个字符
const truncateContent = (content) => {
  // 首先净化内容以确保安全性
  const sanitized = DOMPurify.sanitize(content || '');

  // 将 HTML 内容转换为纯文本
  const textContent = sanitized.replace(/<[^>]+>/g, '');

  // 截取前20个字符
  const truncatedText = textContent.slice(0, 80);

  // 如果文本超过20个字符，添加省略号
  return truncatedText + (textContent.length > 80 ? '...' : '');
};
</script>

<style scoped>
.notification-item {
  display: flex;
  align-items: flex-start; /* 改为顶部对齐 */
  padding: 20px; /* 减少内边距以减小高度 */
  border-bottom: 1px dashed #ccc;
}

.content {
  margin-left: 8px; /* 减少左边距 */
  text-align: left; /* 文字左对齐 */
}

.date, .description {
  font-size: 14px; /* 减小字体大小 */
  color: #666;
}

.description {
  margin-top: 4px; /* 减少上边距 */
}

h3 {
  margin: 0; /* 移除默认的上下边距 */
  font-size: 16px; /* 调整标题字体大小 */
}
.notification-item:hover h3{
  color: #4A7DC5; /* 悬停时字体变蓝 */
  cursor: pointer; /* 鼠标指针变为手型 */
}
</style>