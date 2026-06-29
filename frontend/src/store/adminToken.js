import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAdminTokenStore = defineStore('adminToken', () => {
  const adminToken = ref('')

  // 保存管理员token
  const saveAdminToken = (token) => {
    adminToken.value = token
    localStorage.setItem('adminToken', token)
  }

  // 获取管理员token
  const getAdminToken = () => {
    if (!adminToken.value) {
      adminToken.value = localStorage.getItem('adminToken')
    }
    return adminToken.value
  }

  // 删除管理员token
  const removeAdminToken = () => {
    adminToken.value = ''
    localStorage.removeItem('adminToken')
  }

  return {
    adminToken,
    saveAdminToken,
    getAdminToken,
    removeAdminToken
  }
}) 