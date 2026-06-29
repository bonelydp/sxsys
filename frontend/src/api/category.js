import request from "@/utils/request"

export const categoryInfoService = ()=>{
    return request.get("/category/list")
}

export const addCategoryInfoService = (categoryInfo)=>{
    return request.post("/category/add",categoryInfo)
}

export const updateCategoryInfoService = (categoryInfo)=>{
    return request.put("/category/update",categoryInfo)
}

export const deleteCategoryInfoService = (id)=>{
    return request.delete("/category/delete?id="+id)
}