import { createApp } from 'vue'
import { createI18n } from 'vue-i18n';
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import store from './store'
import { createPinia } from 'pinia';
import router from "./router"
import {createPersistedState} from 'pinia-persistedstate-plugin'
import locale from 'element-plus/dist/locale/zh-cn.js'

import en from './locales/en';
import zh from './locales/zh';


// 获取用户本地语言（默认中文）
const savedLang = localStorage.getItem('lang') || 'zh';

// 创建 i18n 实例
const i18n = createI18n({
    legacy: false, // Vue 3 语法糖必须设置 legacy: false
    locale: savedLang,
    fallbackLocale: 'en',
    messages: { en, zh }
  });

const app = createApp(App)
const pinia = createPinia();
const persist = createPersistedState();
pinia.use(persist)
app.use(pinia);
app.use(ElementPlus,{locale})
app.use(router)
app.use(store)
app.use(i18n);
app.mount('#app')

