import request from "@/utils/request"

//提供调用登录接口的函数
export const login = (loginData)=>{
    const params = new URLSearchParams()
    for(let key in loginData){
        params.append(key,loginData[key]);
    }
    return request.post('/user/login',params)
}

//提供调用注册接口的函数
export const register = (registerData) => {
    const params = new URLSearchParams()
    for(let key in registerData){
        console.log("key,registerData[key]",key,registerData[key])
        params.append(key,registerData[key])
    }
    console.log("params",params)
    return request.post('/user/register',params)
}
//获取用户详细信息
export const userInfoService = ()=>{
    return request.get('/user/userInfo')
}

//修改个人信息
export const userInfoUpdateService = (userInfoData)=>{
    return request.put('/user/update',userInfoData)
}

//获取全部用户信息
export const usersInfoService = (params)=>{
    return request.get("/user/list",{params:params})
}

export const deleteUserInfoService = (id)=>{
    return request.delete("/user/delete?id="+id)
}