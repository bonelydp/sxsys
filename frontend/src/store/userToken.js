import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserTokenStore = defineStore('userToken', () => {
  const userToken = ref("")

  // 保存用户token
  const saveUserToken = (token) => {
    userToken.value = token
    localStorage.setItem('userToken', token)
  }

  // 获取用户token
  const getUserToken = () => {
    if (!userToken.value) {
      userToken.value = localStorage.getItem('userToken')
    }
    return userToken.value
  }

  // 删除用户token
  const removeUserToken = () => {
    userToken.value = ""
    localStorage.removeItem('userToken')
  }

  return {
    userToken,
    saveUserToken,
    getUserToken,
    removeUserToken
  }
}) 