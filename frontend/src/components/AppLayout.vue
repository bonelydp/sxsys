<template>
    <div class="container"> 
      <HeaderFixVc/>
      <HeaderTest/>
      <div class="content">
          <router-view/>
      </div>
            
      <FooterVc/>
    </div>
</template>

<script setup>
import HeaderVc from "@/components/HeaderVc.vue"
import FooterVc from "@/components/FooterVc.vue"
import HeaderFixVc from "@/components/HeaderFixVc.vue";
import HeaderTest from "@/components/HeaderTest.vue"
</script>

<style scoped>

.container{
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.content{
  flex: 1;
  justify-items: center;
}
</style>