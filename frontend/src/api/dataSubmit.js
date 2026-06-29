import request from '@/utils/request'

// 获取所有样本数据
export const getAllSamplesService = (params) => {
  return request.get("/samples/samplespage",{params:params})
}

// 获取单个样本详情
export const getSampleDetailService = (id) => {
  return request.get(`/samples/get?id=${id}`)
}

// 审核通过样本
export const approveSampleService = (id) => {
  return request.post(`/samples/${id}/approve`)
}

// 审核拒绝样本
export const rejectSampleService = (id) => {
  return request.post(`/samples/${id}/reject`)
}

// 删除样本
export const deleteSampleService = (id) => {
  return request.delete(`/samples/${id}`)
}

// 上传文件
export const uploadFileService = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  return request({
    url: '/files/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

//提交样本
export const submitSampleService = (sampleEntity) => {
  return request({
    url: '/samples/upload',
    method: 'post',
    data: sampleEntity
  });
}