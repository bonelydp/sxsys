<template>
  <div class="article-list">
    <h2 class="center-title">{{year}}年 发表论文统计表</h2>
    <el-table :data="articles" style="article-table">
      <el-table-column prop="id" label="序号" width="100"></el-table-column>
      <el-table-column prop="title" label="论文题目" width="300"></el-table-column>
      <el-table-column prop="author" label="第一作者"></el-table-column>
      <el-table-column prop="jcr" label="期刊名称"></el-table-column>
      <el-table-column prop="issn" label="ISSN"></el-table-column>
      <el-table-column prop="volume" label="卷期号" width="100"></el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {onBeforeMount, ref} from "vue"
import {useCategoryInfoService} from "@/store/articleCategoryInfo"

const articles = ref({})
const year = ref({})
onBeforeMount(()=>{
  const categoryInfoService = useCategoryInfoService()
  articles.value = categoryInfoService.info
  year.value = categoryInfoService.year
  console.log("categoryInfoService.year",categoryInfoService.year);
})


</script>


<style scoped>
.article-table{
  
}
</style>
