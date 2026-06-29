import { defineStore} from 'pinia';
import {ref} from 'vue'

export const useCategoryInfoService = defineStore("articleCategoryInfo",()=>{
    const info = ref({})
    const year = ref({})
    const setInfo = (newInfo)=>{
        info.value = newInfo
    }

    const setYear = (newInfo)=>{
        year.value = newInfo
    }
    const removeInfo = ()=>{
        info.value = {}
    }

    return {info,year,setInfo,removeInfo,setYear}
})