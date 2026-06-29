<template>
  <el-header>
    <!--图标-->
    <el-icon @click="toggleNavCollapse">
      <Fold v-show="!isCollapse"/>
      <Expand v-show="isCollapse"/>
    </el-icon>

    <!--面包屑-->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item v-for="(item,index) in $route.matched" :key="index" :to="{ path: item.path }">
        {{item.meta.title}}
      </el-breadcrumb-item>
    </el-breadcrumb>

    <!--下拉菜单-->
    <el-dropdown @command="handleCommand">
      <span class="el-dropdown-link">
        <el-avatar :size="30" :src="require('@/assets/avatar.jpg')" />
        <el-icon class="el-icon--right">
          <arrow-down />
        </el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>  
          <!-- <el-dropdown-item command="peoplecenter">个人资料</el-dropdown-item> -->
          <el-dropdown-item command="logout">退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-header>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Expand, Fold,ArrowDown  } from '@element-plus/icons-vue';
import {useStore} from 'vuex'
import { ElMessage, ElMessageBox } from 'element-plus';
const store = useStore();

const isCollapse = computed(() => store.state.isCollapse);
const toggleNavCollapse = () => {
  store.commit('toggleSidebar',!isCollapse.value)
}

import {useRouter} from "vue-router"
import { useAdminTokenStore } from '@/store/adminToken';

import {useUserInfoStore} from "@/store/userInfo"
const router = useRouter();

const adminTokenStore = useAdminTokenStore()
const userInfoStore = useUserInfoStore()


const handleCommand = (command)=>{
  if(command == 'logout'){
    ElMessageBox.confirm(
      '您确认要退出吗？',
      '温馨提示',
      {
        confirmButtonText:'确认',
        cancelButtonText:'取消',
        type:'warning',
      }
    )
    .then(async()=>{
      console.log("退出登录");
      //清空pinia中存储的token及个人信息
      adminTokenStore.removeAdminToken()
      userInfoStore.removeInfo()
      router.push("/login")
      ElMessage({
        type:'success',
        message:'退出登录成功',
      })
    })
    .catch(()=>{
      console.log("取消退出登录");
      ElMessage({
        type:'info',
        message:'用户取消了退出登录'
      })
    })
  }else{
    router.push('/'+command)
  }
}

</script>

<style scoped lang="scss">
.el-header{
  display:flex;
  align-items:center;
  background-color:#f0f0f0;
  .el-icon{
    margin-right:17px;
  }
} 
.el-dropdown{
  margin-left:auto;
  .el-dropdown-link{
    display:flex;
    justify-content: center;
    align-items: center;
  }
}
</style>