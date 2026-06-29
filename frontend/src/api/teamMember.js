import request from "@/utils/request"

export const getTeamMemberService = (category) =>{
    return request.get("/teamintroduction/selectByCategory?category="+category)
}

export const getAllTeamMemberService = ()=>{
    return request.get("/teamintroduction/selectAll")
}

export const deleteTeamMemberService = (id)=>{
    return request.delete("/teamintroduction/delete?id="+id)
}

export const updateTeamMemberService = (teamMember)=>{
    return request.put("/teamintroduction/update",teamMember)
}

export const addTeamMemberService = (teamMember)=>{
    return request.post("/teamintroduction/add",teamMember)
}