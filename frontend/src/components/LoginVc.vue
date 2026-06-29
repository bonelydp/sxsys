<template>
    <el-row class="login-page">
        <!-- 背景 -->
        <el-col :span="12" class="bg-col">
            <div class="bg"></div>
        </el-col>

        <!-- 登录模块 -->
        <el-col :span="12" class="form-col">
            <el-col :span="12" :offset="2" class="form">
                <!-- 注册表单 -->
                <el-form ref="registerFormRef" size="large" autocomplete="off" v-if="isRegister" :model="registerData"
                    :rules="rules">
                    <el-form-item class="text">
                        <h1 class="flex-center">注册</h1>
                    </el-form-item>
                    <el-form-item prop="username">
                        <el-input :prefix-icon="User" placeholder="请输入邮箱" v-model="registerData.username"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                            v-model="registerData.password"></el-input>
                    </el-form-item>
                    <el-form-item prop="rePassword">
                        <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入密码"
                            v-model="registerData.rePassword"></el-input>
                    </el-form-item>
                    <!-- 注册按钮 -->
                    <el-form-item>
                        <el-button class="button" type="primary" auto-insert-space @click="onRegister">
                            注册
                        </el-button>
                    </el-form-item>
                    <el-form-item class="flex">
                        <el-link type="info" :underline="false" @click="isRegister = false">
                            ← 返回
                        </el-link>
                    </el-form-item>
                </el-form>
                <!-- 登录表单 -->
                <el-form ref="loginFormRef" :rules="rules" :model="form" size="large" autocomplete="off" v-else>
                    <el-form-item class="text">
                        <h1 class="flex-center">登录</h1>
                    </el-form-item>
                    <el-form-item prop="username">
                        <el-input :prefix-icon="User" placeholder="请输入邮箱" v-model="form.username"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                            v-model="form.password"></el-input>
                    </el-form-item>
                    <el-form-item class="flex">
                        <div class="flex">
                            <!-- <el-checkbox>记住我</el-checkbox> -->
                            <!-- <el-link type="primary" :underline="false">忘记密码？</el-link> -->
                        </div>
                    </el-form-item>
                    <!-- 登录按钮 -->
                    <el-form-item>
                        <el-button class="button" type="primary" auto-insert-space @click="onLogin">登录</el-button>
                    </el-form-item>
                    <el-form-item class="flex">
                        <el-link type="info" :underline="false" @click="isRegister = true">
                            注册 →
                        </el-link>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-col>
    </el-row>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage, ElForm, ElFormItem } from 'element-plus';
import { useUserTokenStore } from '@/store/userToken'
import { useAdminTokenStore } from '@/store/adminToken'
import { login, register } from "@/api/users.js"
import { useRouter, useRoute } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue';
import { userInfoService, userInfoUpdateService } from "@/api/users";
import { useUserInfoStore } from '@/store/userInfo';

const userInfoStore = useUserInfoStore();
const router = useRouter()
const route = useRoute()
const userTokenStore = useUserTokenStore()
const adminTokenStore = useAdminTokenStore()
const loginFormRef = ref(null);
const registerFormRef = ref(null);

const form = ref({
    username: '',
    password: ''
});

// 判断显示登录页面还是注册页面
const isRegister = ref(false)
const registerData = ref({
    username: '',
    password: '',
    rePassword: '',
})

// 再次输入密码的校验规则
const checkRePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认密码'))
    } else if (value !== registerData.value.password) {
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}

// 定义表单校验规则
const rules = {
    username: [
        { required: true, message: '用户名不能为空', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16个非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '密码不能为空', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16个非空字符', trigger: 'blur' }
    ],
    rePassword: [
        { required: true, validator: checkRePassword, trigger: 'blur' }
    ]
};

async function onLogin() {
    loginFormRef.value.validate(async (valid) => {
        if (valid) {
            let result = await login(form.value);
            console.log("登录情况：",result);
            if (result.data.code == 0) {
                // 普通用户登录
                console.log("result.data.data11111",result.data.data);
                userTokenStore.saveUserToken(result.data.data)
                ElMessage.success("登录成功！");
                router.push("/home")
            } else if (result.data.code == 2) {
                // 管理员登录
                adminTokenStore.saveAdminToken(result.data.data)
                ElMessage.success("登录成功！");
                router.push("/admin")
            } else {
                ElMessage.error('请检查输入的邮箱和密码是否有误')
               
            }
        } else {
            ElMessage.error("请检查输入的信息");
            return false;
        }
    })
}

async function onRegister() {
    registerFormRef.value.validate(async (valid) => {
        if (valid) {
            let result = await register(registerData.value);
        
            if (result.data.code == 0) {
                ElMessage.success("注册成功！");
                isRegister.value = false;
            } else {
                ElMessage.error('注册失败')
                throw new Error("注册失败")
            }
        } else {
            ElMessage.error("请检查输入的信息");
            return false;
        }
    })
}
</script>

<style scoped>
/* 样式 */
.login-page {
    position: relative;
    height: 100vh;
}

.bg-col {
    
}

.bg {
    width: 100%; /* 使用整个列宽 */
    height: 100%; /* 使用整个列高 */
    background: url('@/assets/loginBackground.png') no-repeat center / cover;
    z-index: 1000;
}

.form-col {
    display: flex;
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    background-color: #404040;
}

.form {
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 80%; /* 根据需要调整宽度 */
    max-width: 400px; /* 设置最大宽度 */
    user-select: none;
    background: rgba(255, 255, 255, 0); /* 半透明白色背景 */
    border-radius: 10px;
    padding: 20px
}

.form h1 {
    margin-bottom: 20px;
    text-align: center;
    color: white;
}

.button {
    width: 100%;
}

.flex {
    width: 100%;
    display: flex;
    justify-content: space-between;
}

.flex-center {
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
}
</style>