

<template>
    <div class="login">
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" size="large">
        <h2>登录</h2>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue';
  import { ElMessage, ElForm, ElFormItem } from 'element-plus';
  import{useTokenStore} from '@/store/mytoken'
  import { login } from "@/api/users"
  import {useRouter,useRoute} from 'vue-router'
  
  const router = useRouter()
  const route = useRoute()
  
  const store = useTokenStore()
  const form = reactive({
    username: '',
    password: ''
  });
  
  const rules = reactive({
    username: [
      { required: true, message: '用户名不能为空', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '密码不能为空', trigger: 'blur' }
    ]
  });
  
  const formRef = ref(null);
  
  function onSubmit(){
    formRef.value.validate(async(valid) =>{
      if(valid){
        console.log("form",form)
        let result = await login(form);
        console.log("result",result)
        console.log("result.code",result.data.code)
        if(result.data.code == 0){
          console.log("@@@~~~!!!",result)
          ElMessage.success("登录成功！");
          router.push(route.query.redirect || '/')
        }else{
          ElMessage.error('登录信息有误')
          throw new Error("登录信息有误")
      }
      }else{
        ElMessage.error("请检查输入的信息");
        return false;
      }
    })
  }
    // const onSubmit = async()=>{
    //   console.log("form",form)
    //   let result = await login(form);
    //   console.log("result",result)
    //   console.log("result.code",result.data.code)
    //   if(result.data.code == 0){
    //     console.log("@@@~~~!!!",result)
    //     ElMessage.success("登录成功！");
    //     router.push(route.query.redirect || '/')
    //   }else{
    //     ElMessage.error('登录信息有误')
    //     throw new Error("登录信息有误")
    //   }
    // }
  
    // formRef.value.validate(async (valid) => {
    //   if (valid) {
    //     console.log("form",form)
    //     const data = await login(form);
    //     if(data.code == 0){
    //       console.log("@@@~~~!!!",data)
  
    //       store.saveToken(data.content)
  
    //       ElMessage.success("登录成功！");
    //       router.push(route.query.redirect || '/')
    //     }else{
    //       ElMessage.error('登录信息有误')
    //       throw new Error("登录信息有误")
    //     }
    //   } else {
    //     ElMessage.error("请检查输入的信息");
    //     return false;
    //   }
    // });
  
  </script>
  
  <style lang="scss">
  .login {
    background-color: #ddd;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
  
    .el-form {
      width: 300px;
      padding: 30px;
      background-color: #fff;
      border-radius: 10px;
      .el-form-item {
        margin-top: 20px;
      }
      .el-button {
        width: 100%;
        margin-top: 30px;
      }
    }
  }
  </style>