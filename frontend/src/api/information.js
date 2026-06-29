import request from "@/utils/request"

export const informationService=()=>{
    return request.get("/notice/selectAll")
}

export const updateInformationService = (informationData)=>{
    return request.put("/notice/update",informationData)
}

export const addInformationService = (informationData)=>{
    return request.post("/notice/add",informationData)
}

export const deleteInformationService = (id)=>{
    return request.delete("/notice/delete?id="+id)
}

//带分页
export const allInformationService= (params)=>{
    return request.get("/notice/list",{params:params})
}

export const oneInformationService = (id)=>{
    return request.get("/notice/getOne?id="+id)
}