import request from '@/utils/request'

// 下载模板
export const downloadTemplateService = () => {
  return request.get('/files/download/excel', {
    responseType: 'blob'  // 指定响应类型为blob
  })
}

// 批量导入数据
export const batchImportService = (files) => {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  return request.post('/sampleinfo2/excel/batch', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

//根据年份和宿主获取数据
export const sampleService = (params)=>{
  return request.get("/sampleinfo/list",{params:params})
}

//获取所有年份
export const getYearSampleService = ()=>{
  return request.get("/sampleinfo/year")
}

//获取所有年份
export const getHostService = ()=>{
  return request.get("/sampleinfo/host")
}
export const getProvinceAndInfectionSizeSampleService = (params)=>{
  return request.get("/sampleinfo/province-infection-count",{params:params})
}
//获取城市和感染数
export const getCityAndInfectionSizeSampleService = (params)=>{
  return request.get("/sampleinfo/city-infection-count",{params:params})
}

// 根据年份，宿主获取感染率
export const getProvinceInfectionRateSampleService = (params)=>{
  return request.get("/sampleinfo/province_infectionRate",{params:params})
}

//根据省份、年份和宿主获取感染率
export const getInfectionRateSampleService = (params)=>{
  return request.get("/sampleinfo/infectionRate",{params:params})
}

//根据省份、年份和宿主获取感染率
export const getProvinceSampleSizeInfectionCountInfectionRateService = (params)=>{
  return request.get("/sampleinfo/details",{params:params})
}

//根据宿主获取感染数
export const getHostSampleSizeInfectionCountInfectionRateService = (params)=>{
  return request.get("/sampleinfo/host_details",{params:params})
}

//读取所有数据
export const getAllSampleInfoService = (params)=>{
  return request.get("/sampleinfo/read-all",{params:params})
}

//读取所有数据
export const getAllSampleInfoNoPageService = ()=>{
  return request.get("/sampleinfo/all-infection-count")
}

//获取全部样本数、省份数、寄主数
export const getCountOfSampleAndprovinceAndHost = ()=>{
  return request.get("/sampleinfo/sample-host-province-count")
}

export const deleteSampleInfoService = (sampleId)=>{
  return request.delete(`/sampleinfo/delete?sampleId=${sampleId}`)
}

export const updateSampleInfoService = (updateDTO) => {
  return request.put("/sampleinfo/update", updateDTO)
}

export const addSampleInfoService = ()=>{
  return request.post("/sampleinfo/create-single")
}

