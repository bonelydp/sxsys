// src/stores/user.js
import { defineStore} from 'pinia';
import {ref } from 'vue'

export const useTokenStore = defineStore('mytoken', () => {
    //用来保存状态的
  const token = ref("")
  
  function saveToken(newToken){
    token.value = newToken
  }

  function removeToken(){
    token.value = ""
  }
  return {token,saveToken,removeToken}
});