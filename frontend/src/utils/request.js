import axios from "axios"

import { useUserTokenStore } from '@/store/userToken'
import { useAdminTokenStore } from '@/store/adminToken'
import { ElMessage } from "element-plus"
import router from '@/router'

const baseURL = '/api'
const instance = axios.create({baseURL})


//添加请求拦截器
instance.interceptors.request.use(
    (config)=>{
        console.log("请求拦截器");
        //添加token
      
        const userTokenStore = useUserTokenStore()
        const adminTokenStore = useAdminTokenStore()
        
        // if(tokenStore.token){
        //     config.headers.Authorization = tokenStore.token
        // }
        const currentPath = router.currentRoute.value.path;
       
        if (currentPath.startsWith('/admin')) {
          console.log("使用管理员token");
            // 管理员接口使用管理员token
            const adminToken = adminTokenStore.getAdminToken()
            if (adminToken) {
              config.headers.Authorization = adminToken
            }
          } else {
            console.log("使用用户token");
            // 普通用户接口使用用户token
            const userToken = userTokenStore.getUserToken()
            if (userToken) {
              config.headers.Authorization = userToken
            }
          }
        return config
    },
    (err)=>{
        Promise.reject(err)
    }  
)

// 添加响应拦截器
instance.interceptors.response.use(
    // result=>{
    //     if(result.code == 0){
    //         return result.data
    //     }
    //     ElMessage.error(result.data.msg?result.data.msg:'服务异常')
    //     return Promise.reject(result.data)
    // },
    response => {
      return response;
    },
    error => {
      const userTokenStore = useUserTokenStore()
      if (error.response && error.response.status === 401) {
        const errorMessage = error.response.data.message;
        if (errorMessage === "令牌已过期，请重新登录") {
          alert("您的登录已过期，请重新登录！");
          userTokenStore.removeUserToken();
          window.location.href = "/login"; // 跳转到登录页
        } else {
          alert("您未登录，请先进行登录！");
        }
      }
      return Promise.reject(error);
    }
    // err=>{
    //     if(err.response.status == 401 ){
    //         console.log("响应拦截器");
    //         ElMessage.error('请先登录！')
    //         router.push("/login")
    //     }else{
    //         ElMessage.error('服务异常')
    //     }
    //     return Promise.reject(err);//异步的状态转化为失败的状态
    // }
)

export default instance