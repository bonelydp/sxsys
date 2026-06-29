import axios from 'axios'

const ALIYUN_GEO_BASE_URL = 'https://geo.datav.aliyun.com/areas_v3/bound'

export const getAliyunMapData = async (adCode) => {
  const response = await axios.get(`${ALIYUN_GEO_BASE_URL}/${adCode}_full.json`)
  return response.data
}
