<template>
  <div class="article-list" v-if="!showTable">
    <h2 class="title">文章发表</h2>
    <el-table :data="articlesStat" style="width: 100%">
      <el-table-column prop="title">
        <template #default="scope">
          <el-link @click="detailStatistics(scope.row.year)" type="primary">{{ scope.row.title }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" width="180"></el-table-column>
    </el-table>
  </div>
  <router-view></router-view>
</template>

<script setup>
import {  ref ,onUpdated} from 'vue';
import { articleInfoService } from "@/api/article"
import {categoryInfoService} from "@/api/category"
import {useCategoryInfoService} from "@/store/articleCategoryInfo"
import {useRouter} from "vue-router"
const router = useRouter();
const articlesStat = ref([]);

const showTable = ref(false)
const getStatColumnInfp = async()=>{

    let result = await categoryInfoService()
    console.log("result.data.data",result.data.data);
    articlesStat.value = result.data.data
  }

getStatColumnInfp()

const detailStatistics = async(year) => {
  let params = {
    year: year
  }
  const categoryService = useCategoryInfoService()
  let result = await articleInfoService(params)
  categoryService.setInfo(result.data.data.items)
  categoryService.setYear(year)
  console.log("categoryService.year",categoryService.year);
  showTable.value = true
  router.push("/text/textstatistics")
}
import { useRoute } from 'vue-router';
onUpdated(() => {
  const route = useRoute()
  if (route.path == "/text") {
    showTable.value = false
  } else {
    showTable.value = true
  }
})
</script>

<style scoped>
.article-list {
  font-family: Arial, sans-serif;
}

.title {
  text-align: center;
}


.article-list {
  font-family: Arial, sans-serif;
  text-align: center;
}

.center-title {
  text-align: center;
  margin-bottom: 20px;
}
</style>