<template>
    <div>
        <el-container>
            <AdminSidebarVc/>
            <el-container class="headermain">
                <el-hader> </el-hader>
                <HeaderVc/>
                <el-main>        
                        <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import AdminSidebarVc from "@/adminPages/AdminSidebarVc.vue"
import HeaderVc from "@/components/HeaderVc.vue"

</script>

<style scoped>
.headermain{
  flex-direction:column;
  height:100vh;
}

.el-main{
    padding-right:0;
}

</style>