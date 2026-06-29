<template>
  <div class="functional-modules-container">
    <!-- 数据提交部分 -->
    <div class="section1">
      <div class="section-header">
        <h2>{{t('message.dataSubmit')}}</h2>
        <div class="underline"></div>
      </div>
      <div class="modules-row">
        <div class="module" @click="handleClick(translatedModules[0])">
          <img :src="translatedModules[0].icon" alt="" />
          <h3>{{ translatedModules[0].title }}</h3>
          <p>{{ translatedModules[0].description }}</p>
        </div>
      </div>
    </div>

    <!-- 数据分析部分 -->
    <div class="section">
      <div class="section-header">
        <h2>{{t('message.dataAnalysis')}}</h2>
        <div class="underline"></div>
      </div>
      <div class="modules-grid">
        <div v-for="(module, index) in translatedModules.slice(1)" 
             :key="index" 
             class="module"
             @click="handleClick(module)">
          <img :src="module.icon" alt="" />
          <h3>{{ module.title }}</h3>
          <p>{{ module.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.functional-modules-container {
  width: 1400px;
  margin: 0 auto;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
}

.section {
  
}

.section1 {
  width: 300px;  /* 固定数据提交部分的宽度 */
  flex-shrink: 0;  /* 防止压缩 */
  align-content: center;
  margin-right: 50px;
  min-width: 200px;
  display: flex;
  flex-direction: column;
  position: relative;
  
}

.section-title {
 
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e0e0e0;
 
}

.modules-row {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 30px;
  margin-top: 150px;
  justify-content: center;
}

.modules-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.module {
  width: 250px;  /* 固定每个模块的宽度 */
  min-height: 180px;  
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  background-color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
  box-sizing: border-box; 
}

.module:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
 
  word-wrap: break-word;  /* 允许长单词换行 */
  overflow-wrap: break-word;  
}

.module img {
  width: 50px;
  height: 50px;
  margin-bottom: 15px;
}

.module h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
}

.module p {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
  width: 100%;  /* 控制文字宽度 */
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.section-header {
  margin-bottom: 30px;
  position: relative;
  width: 100%;
}

.section-header h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 20px 0;
  padding: 0;
  line-height: 1.5;
  height: 30px;
  display: flex;
  align-items: center;
}

.underline {
  margin-top: -15px;
  height: 2px;
  background-color: #000000;
}


.section1 .underline {
  width: calc(100% + 50px);
  position: absolute;
  left: 0;
}

.section {
  align-content: center;
  margin-left: 140px;
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  width: 900px; 
}

.section .underline {
  width: 100%;
  position: absolute;
  right: 0;
}
</style>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';

const router = useRouter();
const { t } = useI18n();

// 原始的模块配置（仅包含图标，标题和描述用翻译动态获取）
const modules = [
  { icon: require('@/assets/datasubmit.png'), key: 'dataSubmit', descKey: 'dateSubmitDes' },
  { icon: require('@/assets/SSU.png'), key: 'ssu', descKey: 'ssuDes' },
  { icon: require('@/assets/SSU.png'), key: 'gp60', descKey: 'gp60Des' },
  { icon: require('@/assets/gene.png'),key:'gene',descKey:'geneDes'},
  { icon: require('@/assets/tree.png'), key: 'PhylogeneticAnalysis', descKey: 'PhylogeneticAnalysisDes' },
  { icon: require('@/assets/gene.png'), key: 'GenomicVariationAnalysis', descKey: 'GenomicVariationAnalysisDes' },
  { icon: require('@/assets/analysis.png'), key: 'PrincipalComponentAnalysis', descKey: 'PrincipalComponentAnalysisDes' }
];

// **使用 computed 计算国际化的标题**
const translatedModules = computed(() => {
  return modules.map(module => ({
    icon: module.icon,
    title: t(`message.${module.key}`),       // 翻译标题
    description: t(`message.${module.descKey}`) // 翻译描述
  }));
});

// **处理点击事件**
function handleClick(module) {
  switch (module.title) {
    case t('message.dataSubmit'):
      router.push('/datasubmit');
      break;
    case t('message.ssu'):
      router.push('/SSUrRNA');
      break;
    case t('message.gp60'):
      router.push('/gp60');
      break;
    case t('message.PhylogeneticAnalysis'):
      router.push('/mafft');
      break;
    case t('message.GenomicVariationAnalysis'):
      alert(t('message.tip'));
      break;
    case t('message.PrincipalComponentAnalysis'):
      alert(t('message.tip'));
      break;
    default:
      console.log('Unknown module:', module);
  }
}
</script>
