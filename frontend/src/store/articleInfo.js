import { defineStore} from 'pinia';
import {ref} from 'vue'

export const useArticleInfoStore = defineStore("articleInfo",() => {
    const info = ref({})

    const setInfo = (newInfo)=>{
        info.value = newInfo
    }

    const removeInfo = ()=>{
        info.value = {}
    }

    return {info,setInfo,removeInfo}
    
})