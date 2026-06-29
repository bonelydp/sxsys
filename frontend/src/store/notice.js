import { defineStore} from 'pinia';
import{ref} from 'vue'

export const useNoticeStore = defineStore('notice',() => {
    const Info = ref({})
    const setInfo = (newInfo)=>{
        Info.value = newInfo;
    }
    const removeInfo = ()=>{
        Info.value = ''
    }

    const homeInfomaShow = ref(false)
    const setHomeInfomaShow = (ts)=>{
        homeInfomaShow.value = ts
    }
    return {Info,setInfo,removeInfo,homeInfomaShow,setHomeInfomaShow}
})