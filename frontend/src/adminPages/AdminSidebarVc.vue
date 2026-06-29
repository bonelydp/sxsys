<template>
  <el-aside>
    <el-menu router :collapse="isCollapse" unique-opened>
      <a class="logo">
        <img src="@/assets/Logo.png" alt="" />
        <h2>管理员</h2>
      </a>
      <el-sub-menu index="1">
        <template #title>
          <span>首页管理</span>
        </template>
        <el-menu-item index="/admin/imagemanager">图片管理</el-menu-item>
        <el-menu-item index="/admin/homeadmin">通知管理</el-menu-item>
      </el-sub-menu>
      
      <el-menu-item index="/admin/useradmin">
        <span>用户管理</span>
      </el-menu-item>

      <el-sub-menu index="2">
        <template #title>
          <span>团队信息管理</span>
        </template>
        <el-menu-item index="/admin/teamImageadmin">团队照片</el-menu-item>
        <el-menu-item index="/admin/teamMemberadmin">团队成员</el-menu-item>
      </el-sub-menu>

      <!-- <el-sub-menu index="3">
        <template #title>
          <span>文章管理</span>
        </template>
        <el-menu-item index="/admin/articlestat">统计表</el-menu-item>
        <el-menu-item index="/admin/article">文章</el-menu-item>
      </el-sub-menu> -->

      <el-menu-item index="/admin/dataadmin">
        <span>数据管理</span>
      </el-menu-item>

      <el-menu-item index="/admin/dataVisualizationManage">
        <span>数据可视化管理</span>
      </el-menu-item>

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
  width: 180px;
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
  margin-bottom: 15px;

  img {
    margin-right: 5px;
    width: 30px;
    height: 30px;
  }
}
</style>