<template>
 
    <div class="container">
      <el-card class="page-container">
        <template #header>
          <div class="header">
            <span>基本资料</span>
          </div>
        </template>
        <el-row>
          <el-col :span="12">
            <el-form :model="tempUserInfo" :rules="rules" ref="formRef" label-width="100px" size="large">
              <el-form-item label="用户编号" prop="id">
                <el-input v-model="tempUserInfo.id" disabled></el-input>
              </el-form-item>
              <el-form-item label="用户名" prop="username">
                <el-input v-model="tempUserInfo.username" disabled></el-input>
              </el-form-item>
              <el-form-item label="用户邮箱" prop="email">
                <el-input v-model="tempUserInfo.email"></el-input> <!-- 移除 disabled -->
              </el-form-item>
              <el-form-item label="注册时间" prop="createTime">
                <el-input :value="formattedCreateTime" disabled></el-input>
              </el-form-item>
              <el-form-item class="peopleButton">
                <el-button type="primary" @click="submitForm">提交修改</el-button>
                <el-button type="primary" @click="loginBack">退出登录</el-button>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-card>
    </div>


</template>

<script setup>
import { computed, ref, onMounted } from 'vue';
import { userInfoService, userInfoUpdateService } from "@/api/users";
import { ElMessage } from 'element-plus';
import { useUserInfoStore } from '@/store/userInfo';
import { reactive } from 'vue';

import { useUserTokenStore } from '@/store/userToken'
import { useRouter } from 'vue-router';

const userTokenStore = useUserTokenStore()


const router = useRouter()

const userInfoStore = useUserInfoStore();
const formRef = ref(null);

// 定义临时用户信息，确保它是可写的
const tempUserInfo = ref({});

// 获取用户信息并初始化 tempUserInfo
const getUserInfo = async () => {
  let result = await userInfoService();
  userInfoStore.setInfo(result.data.data);
  console.log("userInfoStore.info1", userInfoStore.info);
  tempUserInfo.value = { ...userInfoStore.info }; // 初始化临时用户信息
};

getUserInfo();

// 格式化注册时间
const formattedCreateTime = computed(() => {
  if (tempUserInfo.value.createTime) {
    const date = new Date(tempUserInfo.value.createTime);
    return date.toLocaleString(); // 或者使用任何你喜欢的日期格式化库
  }
  return '';
});

// 表单验证规则
const rules = reactive({
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ]
});

// 提交修改的方法
const submitForm = async () => {
  try {
    await formRef.value.validate(async (valid) => {
      if (valid) {
        console.log("tempUserInfo.value", tempUserInfo.value);
        let result = await userInfoUpdateService(tempUserInfo.value);
        ElMessage.success(result.msg ? result.msg : '修改成功');

        // 更新 store 中的信息
        userInfoStore.setInfo({ ...tempUserInfo.value });
        console.log("userInfoStore.info2", userInfoStore.info);
        // 刷新页面或其他操作
      } else {
        ElMessage.error('请检查输入的信息');
        return false;
      }
    });
  } catch (error) {
    ElMessage.error('修改失败');
    console.error(error);
  }
};

// 退出登录的方法
const loginBack = () => {
  // 实现退出登录逻辑
  userTokenStore.removeUserToken()
  router.push('/login')
};
</script>

<style scoped>

.container {
  height: 600px;
  width: 100%;
  margin-bottom: -100px;
  justify-items: center;
}

.page-container {
  width: 90%;
  margin-top: 80px;

}


.peopleButton {
  margin-top: 30px;
  margin-left: 70px;
}

.peopleButton .el-button {
  margin-right: 40px;
}
</style>