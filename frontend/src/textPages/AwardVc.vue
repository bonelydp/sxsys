<template>
  <el-header class="header" :class="{ show:y > 80}">
    <!-- Logo and title -->
    <div class="logo">
      <img src="@/assets/Logo.png" alt="Logo">
      <div class = "text">
        <h3>隐孢子虫智监平台</h3>
        <p>CryptoMonitor</p>
      </div>
      
    </div>

    <!-- Navigation menu -->
    <el-menu router :default-active="activeIndex" mode="horizontal" @select="handleSelect" class="navMenu">
      <el-menu-item index="/home">首页</el-menu-item>
      <el-menu-item index="2">病原科普</el-menu-item>
      <el-menu-item index="/resourceshare">资源共享</el-menu-item>
      <el-menu-item index="/teamIntroduction">团队介绍</el-menu-item>
      <el-menu-item index="5">English</el-menu-item>
    </el-menu>

    <!-- User actions -->
    <div class="user-actions">
      <el-button type="text" @click="loginDialogVisible = true">登录/注册</el-button>
      <el-dropdown @command="handleCommand">
        <i class="el-icon-user-solid"></i>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- Login Dialog -->
    <el-dialog v-model="loginDialogVisible" title="登录/注册">
      
    </el-dialog>
  </el-header>
</template>

<script setup>
import { ref } from 'vue';
import { useScroll } from '@vueuse/core';
const {y} = useScroll(window);
// 当前激活菜单项
const activeIndex = ref('1');

// 登录对话框显示状态
const loginDialogVisible = ref(false);

// 处理菜单选择
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath);
};

// 处理用户命令（如：个人中心、退出登录）
const handleCommand = (command) => {
  if (command === 'logout') {
    console.log('用户点击了退出登录');
  } else if (command === 'profile') {
    console.log('用户点击了个人中心');
  }
};
</script>

<style scoped lang="scss">
.header {
  position: fixed;
  top:0px;
  width: 100%;
  height: 80px;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #105186; /* 设置头部组件的背景颜色 */

  transform:translateY(-100%);
  opacity: 0;

  &.show{
      transition:all 0.3s linear;
      transform: none;
      opacity: 1;
  }
}

.logo{
  display:flex;
  flex-direction: row;
  align-items: center;
}
.text{
  text-align: left;
  color: white;
}

.text h3 {
  margin-bottom: 0px; /* 调整与下面元素的距离 */
}
.text p{
  margin-top: 0px;
}

.logo img {
  height: 30px;
  margin-right: 10px;
}

.navMenu {
  flex-grow: 1; /* 让菜单占据剩余空间 */
  justify-content: flex-end; /* 将菜单右对齐 */
  background-color: #105186;
  
}

/* 修改Element Plus默认样式以改变文字颜色 */
.navMenu .el-menu-item {
  color: white; /* 设置菜单项文字颜色为白色 */
  
}
.el-menu--horizontal>.el-menu-item.is-active, .el-menu--horizontal>.el-menu-item {
  border-bottom: none;
  text-decoration: none;
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