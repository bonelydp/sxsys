<template>
  <div class="notice-container">
    <header class="notice-header">
      <h1>{{ noticeInfo.title }}</h1>
      <div class="meta-info">
        <span>发布时间：{{ noticeInfo.date }}</span>
      </div>
    </header>
    <main class="notice-content">
      <!-- 使用 v-html 渲染富文本内容 -->
      <div class="text-content ql-editor" v-html="sanitizedContent"></div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onUpdated } from 'vue';
import DOMPurify from 'dompurify'; // 确保已经安装了 DOMPurify

// 定义并初始化 noticeInfo
const noticeInfo = ref({
  id: "1",
  title: "我院校举行考研学生慰问活动",
  content: `<p>	<span style="color: rgb(26, 32, 41);">近日，我院崔金荣副教授带队广东省农村科技特派员团队赴清远市清新区太平镇开展系列科技帮扶活动。</span></p><p>	<span style="color: rgb(26, 32, 41);">特派员团队先后走访太平镇蒲兴、龙湾、金门等村落，针对农户提出的问题逐一解答和技术指导，提出多项科技帮扶措施。</span></p><p>	<span style="color: rgb(26, 32, 41);">在蒲兴村，针对养渔农户的鱼塘信息化问题，特派员团队提出了智能化养殖管理系统的实施方案；在龙湾村，特派员团队考察了甜杆西兰菜的种植情况，指导种植户实施先进的滴灌、管灌和水肥一体化技术，同时提供了先进的机器人采摘技术和病虫害防治方法；在稻旭农业示范基地，特派员团队针对土壤肥力问题，指导农户进行土壤改良；在金门村，针对部分玉米田发育不良的问题，特派员团队做出专业诊断及改善方法。</span></p><p>	<span style="color: rgb(26, 32, 41);">此次科技帮扶活动，特派员团队充分发挥了专业优势，为太平镇的农业发展解决了实际问题，提升了农民的科技素养，增强了农业产业的可持续发展能力。特派员团队的指导成果得到了当地政府和农民的一致好评。</span></p>`,
  date: "2024-12-25",
  coverImg: "/api/files/1735126401788-001.jpg",
  images: [] // 如果有额外的图片列表，可以添加到这里
});

import { oneInformationService } from "@/api/information"
import { useNoticeStore } from '@/store/notice';
const getNoticeInfo = async () => {
  const noticeStore = useNoticeStore()

  let result = await oneInformationService(noticeStore.Info.id)

  noticeInfo.value = result.data.data
  console.log("result.data.data", result.data.data);
}
getNoticeInfo()

// 计算属性，用于返回净化后的富文本内容
const sanitizedContent = computed(() => {
  return DOMPurify.sanitize(noticeInfo.value.content);
});

import { useRoute } from 'vue-router';

const isShow = ref(false)
onUpdated(() => {
  const route = useRoute()
  if (route.path == "/home/information") {
    isShow.value = false
  } else {
    isShow.value = true
  }
})
</script>

<style scoped>
/* 样式调整 */
.notice-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.notice-header {
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
  text-align: center;
  /* 居中对齐 */
}

.meta-info {
  font-size: 0.8em;
  color: #666;
  display: block;
  /* 确保独占一行 */
  text-align: center;
  /* 居中对齐 */
}

.text-content {
  margin-top: 20px;
  line-height: 1.6;
}

/* 首行缩进 */
.text-content p {
  text-indent: 2em;
  /* 设置首行缩进 */
}

.image-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 20px;
}

.image-gallery img,
.cover-image img {
  width: calc(50% - 10px);
  height: auto;
}
</style>
