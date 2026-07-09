<template>
  <div class="container">
    <div class="underline-text">
      {{t('message.teamIntroduction')}}
    </div>
    <div class="pic_text">
      <div style="width: 900px;margin-left: 100px;">
        <el-carousel :interval="4000" type="card" height="250px">
          <el-carousel-item v-for="(contour,index) in contourList" :key="index">
            <img :src="contour.img" style="width: 100%;height: 100%;">
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="introduction">{{ t('message.teamIntroText') }}</div>
    </div>
    <div class="underline-text">
      {{t('message.teamMember')}}
    </div>
    <div class="admin-members-container">
      <div class="admin-members-grid">
        <div v-for="(member, index) in adminMember" :key="index" class="admin-card">
          <img :src="member.avatarUrl" :alt="member.name" class="member-photo" @error="e => e.target.src = Avatar">
          <div class="member-info">
            <p class="member-name">{{ member.name }}</p>
          </div>
          <div class="member-description">
            {{ truncateContent(member.introduction) }}
          </div>
        </div>
      </div>
    </div>
    <div class="worker-members-container">
      <div class="worker-members-grid">
        <div v-for="(member, index) in workerMember" :key="index" class="worker-card">
          <img :src="member.avatarUrl" :alt="member.name" class="member-photo" @error="e => e.target.src = Avatar">
          <div class="member-info">
            <p class="member-name">{{ member.name }}</p>
          </div>
          <div class="member-description">
            {{ truncateContent(member.introduction) }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'

import TeamMember from '@/pages/TeamMember.vue'
import Avatar from '@/assets/avatar.jpg'

import {getTeamMemberService} from '@/api/teamMember'
import { useI18n } from 'vue-i18n'
const  { t } = useI18n()
// -----------------轮播图------------------------
const contourList = ref();
const fetchContours = async () => {
  try {
    const response = await axios.get('/api/contour/selectAll');
    console.log("response.data.code", response.data.code)
    console.log("Array.isArray(response.data.data)", Array.isArray(response.data.data))
    if (response.data.code === 0 && Array.isArray(response.data.data)) {
      contourList.value = response.data.data.map(item => ({
        id: item.id,
        img: item.img,
      }));
      console.log("contourList.value", contourList.value)
    } else {
      console.error('Unexpected response format or status code:', response.data.code);
    }
  } catch (error) {
    console.error('Failed to fetch contour data:', error);
  }
};
onMounted(()=>{
  fetchContours()
  getTeamMemberData()
})

//-------------------------------------------------------------------------------------

const adminMember = ref([
  {
    id: 1,
    avatarUrl: Avatar,
    name: '张三',
    introduction: '信息介绍文本',
    category:'admin'

  },
  {
    id: 2,
    avatarUrl: Avatar,
    name: '李四',
    introduction: '信息介绍文本',
    category:'worker'
  },
]);
const workerMember = ref([
  {
    id: 1,
    avatarUrl: Avatar,
    name: '张三',
    introduction: '信息介绍文本',
    category:'admin'

  },
  {
    id: 2,
    avatarUrl: Avatar,
    name: '李四',
    introduction: '信息介绍文本',
    category:'worker'
  },
]);

const getTeamMemberData= async() =>{
  let adminResult = await getTeamMemberService("admin")
  console.log("adminResult.data",adminResult.data);
  adminMember.value = adminResult.data.data
  let workerResult = await getTeamMemberService("worker")
  workerMember.value = workerResult.data.data
}
import DOMPurify from 'dompurify'; // 确保你安装了 dompurify
const truncateContent = (content) => {
  // 首先净化内容以确保安全性
  const sanitized = DOMPurify.sanitize(content || '');

  // 将 HTML 内容转换为纯文本
  const textContent = sanitized.replace(/<[^>]+>/g, '');
  return textContent;
};
</script>

<style scoped>
.container {
  width: 100%;
  margin-top: 40px;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.pic_text {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 50px;
}

.introduction {
  width: 430px;
  height: 300px;
  border-radius: 10px;
  border: 1px solid #ccc;
  padding: 20px;
  margin-right: 80px;
  text-align: justify;
  line-height: 1.6;
  overflow-y: auto;
  text-indent: 2em;
}

.admin-members-container, .worker-members-container {
  width: 100%;
  display: flex;
  justify-content: center;
  margin: 40px 0;
}

.admin-members-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, 300px);
  gap: 30px;
  justify-content: center;
  max-width: 630px; /* 两列的最大宽度：300px * 2 + 30px gap */
  width: 100%;
}

.worker-members-grid {
  display: grid;
  grid-template-columns: repeat(4, 300px);
  gap: 30px;
  justify-content: center;
  width: fit-content;
  margin: 0 auto;
}

.admin-card, .worker-card {
  width: 300px;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.member-photo {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.member-info {
  padding: 15px;
}

.member-name {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  margin: 0;
}

.member-description {
  padding: 15px;
  border-top: 1px solid #eee;
  font-size: 14px;
  color: #666;
}

/* 响应式布局 */
@media (max-width: 1300px) {
  .worker-members-grid {
    grid-template-columns: repeat(3, 300px);
  }
}

@media (max-width: 1000px) {
  .worker-members-grid {
    grid-template-columns: repeat(2, 300px);
  }
}

@media (max-width: 650px) {
  .worker-members-grid {
    grid-template-columns: repeat(1, 300px);
  }
}

.underline-text {
  position: relative;
  display: inline-block;
  /* 让容器仅包裹文本 */
  margin-left: 20px;
}

.underline-text::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -5px;
  /* 调整此值以改变线条与文本的距离 */
  width: 250px;
  /* 线条宽度为文本宽度的120% */
  height: 2px;
  /* 线条高度 */
  background-color: #000000;
  /* 线条颜色 */
  z-index: -1;
  /* 确保线条在文本之下 */
}
</style>
