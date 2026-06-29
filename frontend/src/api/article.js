import request from "@/utils/request"


//文章列表查询
export const articleInfoService = (params)=>{
    console.log("params",params);
    return request.get('/article/list',{params:params})
}

//文章添加
export const addArticleService = (articleInfo)=>{
    return request.post('/article/add',articleInfo)
}

//文章删除
export const deleteArticleService = (id)=>{
    return request.delete('/article/delete?id='+id)
}

//修改文章
export const updateArticleService = (updateArticleData)=>{
    return request.put('/article/update',updateArticleData)
}