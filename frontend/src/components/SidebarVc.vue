<template>
  <el-aside>
    <el-menu router :collapse="isCollapse" unique-opened>
      <a class="logo">
        <img src="@/assets/Logo.png" alt="" />
        <h1>系统</h1>
      </a>

      <el-menu-item index="/home">
        <span>首页</span>
      </el-menu-item>

      <el-menu-item index="/peopleCenter">

        <span>个人中心</span>
      </el-menu-item>

      <el-sub-menu index="3">
        <template #title>

          <span>团队介绍</span>
        </template>
        <el-menu-item index="/teamintroduction">团队信息</el-menu-item>
        <el-menu-item index="/text">文章发表</el-menu-item>
      </el-sub-menu>

      <!-- <el-sub-menu index="4">
        <template #title>
          <span>数据可视化</span>
        </template>
        <el-menu-item index="4-1" :route="{ path: '/datavisualization' }">数据可视</el-menu-item>
        <el-menu-item index="4-2" :route="{ path: '/datasubmit' }">数据提交</el-menu-item>
      </el-sub-menu> -->

      <el-menu-item index="/datavisualization">
        <span>数据可视</span>
      </el-menu-item>
      <el-menu-item index="/datasubmit">
        <span>数据提交</span>
      </el-menu-item>

      <el-sub-menu index="5">
        <template #title>

          <span>ANALYSIS</span>
        </template>
        <el-menu-item index="/blast">Blast</el-menu-item>
        <el-menu-item index="/mafft">系统发育树构建</el-menu-item>
      </el-sub-menu>

      
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex'

const store = useStore();
const isCollapse = computed(() => store.state.isCollapse);


</script>

<style lang="scss" scoped>
.el-aside {
  background-color: #f0f0f0;
  height: 100vh;
  width: auto;
}

.el-menu {
  background-color: #f0f0f0;
  width: 200px;
  height: 100vh;
  border-right: none;

  &.el-menu--collapse {
    width: 60px;

    & h1 {
      display: none;
    }
  }
}

.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  text-decoration: none;
  color: black;

  img {
    width: 30px;
    height: 30px;
  }
}
</style>