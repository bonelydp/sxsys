<template>
  <div class="team-member">
    <div class="member-card">
      <img :src="props.avatar" alt="Avatar" class="member-photo" />
      <div class="member-info">
        <p class="member-name">{{ props.name }}</p>
      </div>

    </div>
    <p class="member-description">{{ props.description }}</p>
  </div>
</template>

<script setup>
import { ref, defineProps } from 'vue'

// 示例数据
const avatar = ref('https://placehold.co/64x64')
const name = ref('姓名')
const description = ref('信息介绍文本')

const props = defineProps({
  avatar: {
    type: String,
    default: 'https://placehold.co/64x64'
  },
  name: {
    type: String,
    default: '姓名'
  },
  description: {
    type: String,
    default: '信息介绍文本'
  }
})
</script>

<style scoped>
.member-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.member-photo {
  width: 100%;
  height: auto;
  display: block;
  border-bottom: 1px solid #ddd;
}
.member-card{
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  text-align: center;
  margin-right: 30px;
  width: 250px;
}
.member-info {
  padding: 16px;
}
.member-name{
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
}
</style>