<template>
  <div class="container-fluid" :class="{ show:y> 80}">

    <div class="logo">
      <img src="@/assets/Logo.png" alt="Logo">
      <div class="text">
        <h3>隐孢子虫病原监测与分析平台</h3>
        <p>CryptoMonitor</p>
      </div>
    </div>

    <div class="navMenu">
      <ul class="app-header-nav">
        <li>
          <RouterLink active-class="active" to="/home">{{t('message.home')}}</RouterLink>
        </li>
        <li v-for="(item,index) in categoryList" :key=index>
          <RouterLink active-class="active" :to="`/${item.path}`">
            {{ t(item.name) }}
          </RouterLink>
        </li>
        <li style="cursor: pointer;" @click="changeLanguage">{{ t('message.English') }}</li>
       
        <li v-show="isLogin"><RouterLink to="/peoplecenter">
          <img src="@/assets/headpic.png"  style="height: 25px; width: 25px;margin-bottom: -10px;border: 2px solid;border-radius: 20px;padding: 1px;">
        </RouterLink></li>
        <li v-show="!isLogin"><RouterLink to="/login">{{t('message.RegisterLogin')}}</RouterLink></li>
      </ul>
    </div>
    
  </div>
</template>

<script setup>

import {onMounted, ref} from 'vue'
import { useUserTokenStore } from '@/store/userToken';
import { useScroll } from '@vueuse/core';
//中英文切换
import { useI18n } from 'vue-i18n';
const { t,locale} = useI18n();
const userTokenStore = useUserTokenStore()
//下滑距离
const {y} = useScroll(window);





const categoryList = ref([

  {id:1,name:'message.science',path:'science'},
  {id:2,name:'message.resourceShare',path:'resourceShare'},
  {id:3,name:'message.teamIntroduction',path:'teamIntroduction'},
  

])
const isLogin = ref(false)
const judgeLogin = ()=>{
  console.log("userTokenStore.value1111111",userTokenStore.userToken);
  if(userTokenStore.userToken== null || userTokenStore.userToken== ""){
    isLogin.value = false
  }else{
    isLogin.value = true
  }
}
onMounted(async() => {
  judgeLogin()
})

const changeLanguage = ()=>{
  locale.value = locale.value === 'zh' ? 'en' : 'zh'; // 切换语言
  localStorage.setItem('lang', locale.value); // 存储用户选择的语言
}
</script>

<style scoped lang="scss">
.container-fluid{
  width:100%;
  background: #105186;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
  z-index: 1000;

  position: fixed;
  top: 0px;
  transform: translateY(-100%);
  opacity:0;

  &.show{
    transition: all 0.3s linear;
    transform: none;
    opacity: 1;
  }
}

.navbar-brand img {
  height: 30px;
  margin-right: 10px;
}

.logo {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-left: 20px;
}

.text {
  text-align: left;
  color: white;
}

.text h3 {
  margin-bottom: 0px;
  /* 调整与下面元素的距离 */
}

.text p {
  margin-top: 0px;
}

.logo img {
  height: 30px;
  margin-right: 10px;
}

.navMenu{
  margin-right: 30px;
}
.app-header-nav {
  display: flex;
  list-style-type: none; /* 移除默认的列表样式 */
  padding: 0; /* 如果需要的话移除默认填充 */
  
}

.app-header-nav li {
  color: white; /* 文字颜色设置为白色 */
  margin-left: 30px; /* 根据需求调整每个导航项之间的间距 */
  
}
.app-header-nav li a{
  color: white; /* 文字颜色设置为白色 */
  text-decoration: none;
}

/* 当前选中的链接样式 */
.active {
  color: lightblue !important; /* 选中时的颜色 */
}

.logo img {
  height: 30px;
  margin-right: 10px;
}

.text {
  text-align: left;
  color: white;
}

.text h3, .text p {
  margin: 0; /* 调整与下面元素的距离 */
}

.user-actions {
  display: flex;
  align-items: center;
  color: white; /* 设置按钮和图标颜色为白色 */
}

.el-icon-user-solid {
  color: white; /* 确保用户图标也是白色的 */
}

/* 确保按钮文字也是白色的 */
.user-actions .el-button {
  color: white;
}
</style>