<template>
  <div class="science-container">
    <h1 class="title">{{ t('message.cryptosporidiosis') }}</h1>
    <div class="scientific-name">[Cryptosporidium spp.]</div>

    <!-- 导航栏 -->
    <div class="nav-tabs">
      <div 
        v-for="tab in tabs" 
        :key="tab.key"
        :class="['tab', { active: activeTab === tab.key }]"
        @click="activeTab = tab.key"
      >
        {{ t(`message.${tab.key}`) }}
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <div v-if="activeTab === 'parasiteBiology'" class="tab-content">
        <h2 class="section-title">{{ t('message.causalAgents') }}</h2>
        <p>{{ t('message.parasiteBiologyCausalAgents') }}</p>
        
        <h2 class="section-title">{{ t('message.lifeCycle') }}</h2>
        <div class="life-cycle-image">
          <img :src="require('@/assets/Cryptosporidium_LifeCycle_lg.jpg')" :alt="t('message.lifeCycle')" />
        </div>
        
        <div class="biology-content">
          <p class="lifecycle-description">
            {{ t('message.parasiteBiologyLifeCycle') }}
          </p>

          <h2 class="section-title">{{ t('message.hosts') }}:</h2>
          <p>{{ t('message.parasiteBiologyHosts') }}</p>

          <h2 class="section-title">{{ t('message.geographicRange') }}:</h2>
          <p>{{ t('message.parasiteBiologyGeographicRange') }}</p>

          <h2 class="section-title">{{ t('message.clinicalPresentation') }}</h2>
          <p>{{ t('message.parasiteBiologyClinicalPresentation') }}</p>
        </div>
      </div>
      
      <div v-if="activeTab === 'imageGallery'" class="tab-content">
        <div class="image-grid">
          <!-- 图片展示区域 -->
          <div v-for="(image, index) in images" :key="index" class="image-item">
            <img :src="image.url" :alt="image.description" />
            <p>{{ t(`message.${image.descKey}`) }}</p>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'laboratoryDiagnosis'" class="tab-content">
        <h2 class="lab-title">{{ t('message.labDiagnosisTitle') }}</h2>
        <p class="lab-text">
          {{ t('message.labDiagnosisIntro') }}
        </p>

        <h3 class="lab-subtitle">{{ t('message.specimenProcessing') }}</h3>
        <p class="lab-text">
          {{ t('message.specimenProcessingContent') }}
        </p>

        <p class="lab-note">
          {{ t('message.formalinNote') }}
        </p>

        <h3 class="lab-subtitle">{{ t('message.immunoassays') }}</h3>
        <p class="lab-text">
          {{ t('message.immunoassaysContent1') }}
        </p>

        <p class="lab-text">
          {{ t('message.immunoassaysContent2') }}
        </p>

        <p class="lab-text">
          {{ t('message.immunoassaysContent3') }}
        </p>

        <h3 class="lab-subtitle">{{ t('message.molecularMethods') }}</h3>
        <h4 class="lab-subsubtitle">{{ t('message.multipathogenPanels') }}</h4>
        <p class="lab-text">
          {{ t('message.multipathogenPanelsContent') }}
        </p>

        <h4 class="lab-subsubtitle">{{ t('message.molecularTyping') }}</h4>
        <p class="lab-text">
          {{ t('message.molecularTypingContent') }}
        </p>

        <h2 class="lab-title">{{ t('message.labSafety') }}</h2>
        <p class="lab-text">
          {{ t('message.labSafetyContent1') }}
        </p>

        <p class="lab-text">
          {{ t('message.labSafetyContent2') }}
        </p>

        <p class="lab-text">
          {{ t('message.labSafetyContent3') }}
        </p>
      </div>

      <div v-if="activeTab === 'resources'" class="tab-content">
        <h2 class="resource-title">{{ t('message.suggestedReading') }}</h2>
        <div class="reference-list">
          <p class="reference-item">
            {{ t('message.reference1') }}
          </p>
          <p class="reference-item">
            {{ t('message.reference2') }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

// 当前激活的标签
const activeTab = ref('parasiteBiology')

// 导航标签配置
const tabs = [
  { key: 'parasiteBiology' },
  { key: 'imageGallery' },
  { key: 'laboratoryDiagnosis' },
  { key: 'resources' }
]

// 图片数据
const images = [
  {
    url: require('@/assets/Logo.png'),
    descKey: 'image1Description'
  },
  {
    url: require('@/assets/Logo.png'),
    descKey: 'image2Description'
  }
]
</script>

<style scoped>
.science-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.title {
  color: #003366;
  font-size: 2.5em;
  margin-bottom: 10px;
}

.scientific-name {
  font-style: italic;
  margin-bottom: 20px;
  font-size: 1.2em;
  color: #666;
}

.nav-tabs {
  display: flex;
  border-bottom: 2px solid #ddd;
  margin-bottom: 20px;
}

.tab {
  padding: 10px 20px;
  cursor: pointer;
  border: 1px solid transparent;
  border-bottom: none;
  margin-right: 5px;
  background-color: #f8f8f8;
}

.tab.active {
  background-color: #fff;
  border-color: #ddd;
  border-bottom: 2px solid #fff;
  margin-bottom: -2px;
}

.content-area {
  padding: 20px;
  background-color: #fff;
  min-height: 400px;
}

.tab-content {
  line-height: 1.6;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.image-item {
  text-align: center;
}

.image-item img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

.image-item p {
  margin-top: 10px;
  color: #666;
}

.life-cycle-image {
  margin-top: 20px;
  text-align: center;
}

.life-cycle-image img {
  max-width: 100%;
  height: auto;
}

.biology-content {
  margin-top: 30px;
}

.section-title {
  color: #333;
  font-size: 24px;
  font-weight: bold;
  margin-top: 30px;
  margin-bottom: 15px;
}

.biology-content p {
  margin-bottom: 20px;
  line-height: 1.6;
  text-align: justify;
}

.lifecycle-description {
  margin-top: 20px;
}

.lab-title {
  font-size: 24px;
  color: #333;
  margin: 30px 0 20px 0;
  font-weight: normal;
}

.lab-subtitle {
  font-size: 18px;
  color: #333;
  margin: 25px 0 15px 0;
  font-weight: bold;
}

.lab-subsubtitle {
  font-size: 16px;
  color: #333;
  margin: 20px 0 10px 0;
  font-weight: bold;
}

.lab-text {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 15px;
  text-align: justify;
}

.lab-note {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  margin: 15px 0;
  padding-left: 20px;
}

.lab-link {
  color: #0066cc;
  text-decoration: none;
}

.lab-link:hover {
  text-decoration: underline;
}

i {
  font-style: italic;
}

.resource-title {
  font-size: 24px;
  color: #333;
  margin: 30px 0 20px 0;
  font-weight: normal;
}

.reference-list {
  margin-top: 20px;
}

.reference-item {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 20px;
  text-align: justify;
  padding-left: 36px;
  text-indent: -36px;  /* 创建悬挂缩进效果 */
}

.reference-item i {
  font-style: italic;
}
</style> 