import request from "@/utils/request"

//首页图片
export const getHomeImageService = ()=>{
    return request.get("/contour1/selectAll")
}

export const addHomeImageService = (newImage)=>{
    return request.post("/contour1/add",newImage)
}

export const deleteHomeImageService = (id)=>{
    return request.delete(`/contour1/delete/${id}`)
}
//团队图片
export const getTeamImageService = ()=>{
    return request.get("/contour/selectAll")
}

export const addTeamImageService = (newImage)=>{
    return request.post("/contour/add",newImage)
}

export const deleteTeamImageService = (id)=>{
    return request.delete(`/contour/delete/${id}`)
}