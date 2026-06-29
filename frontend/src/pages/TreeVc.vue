<template>
  <div class="article-list">
    <h2 class="center-title">2021年 发表论文统计表</h2>
    <el-table :data="articles" style="">
      <el-table-column prop="序号" label="序号" width="100"></el-table-column>
      <el-table-column prop="论文题目" label="论文题目" width="300"></el-table-column>
      <el-table-column prop="第一作者" label="第一作者" ></el-table-column>
      <el-table-column prop="期刊名称" label="期刊名称" ></el-table-column>
      <el-table-column prop="ISSN" label="ISSN" ></el-table-column>
      <el-table-column prop="卷期号" label="卷期号" width="100"></el-table-column>
      <el-table-column prop="起始页码" label="起始页码" width="100"></el-table-column>
      <el-table-column prop="结束页码" label="结束页码" width="100"></el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const articles = ref([
  {
    序号: 1,
    论文题目: 'TEAseq-based identification of 35,696 Dissociation insertional mutations facilitates functional genomic studies in maize',
    第一作者: '吕明杰',
    期刊名称: 'Journal of Genetics and Genomics',
    ISSN: '1673-8',
    卷期号: 'vol. 421 527',
    起始页码: 115,
    结束页码: 127
  },
  {
    序号: 2,
    论文题目: 'The cysteine-rich receptor-like kinase TaCRK3 contributes to defense against Rhizoctonia cerealis in wheat',
    第一作者: 'Feilong Guo†, Tianci Wu†, Fangdi Shen†, Gangbiao Xu†',
    期刊名称: 'JOURNAL OF EXPERIMENTAL BOTANY',
    ISSN: '0022-0957',
    卷期号: '72(20)',
    起始页码: 6904,
    结束页码: 6919
  },
  {
    序号: 3,
    论文题目: 'The receptor-like kinase TaCRK-7A inhibits Fusarium pseudograminearum growth and mediates resistance to Fusarium crown rot in wheat',
    第一作者: 'Tianci Wu； Feilong Guo',
    期刊名称: 'Biology-Basel',
    ISSN: '无',
    卷期号: '10',
    起始页码: 1122,
    结束页码: 1122
  },
  {
    序号: 4,
    论文题目: '玉米SNAC转录因子的基因结构特征与逆境调控',
    第一作者: '罗平',
    期刊名称: '中国农业科技导报',
    ISSN: '1008-0',
    卷期号: '23.0(10.0)',
    起始页码: 35,
    结束页码: 44
  }
]);
</script>

<style scoped>
.article-list {
  font-family: Arial, sans-serif;
  text-align: center;
}

.center-title {
  text-align: center;
  margin-bottom: 20px;
}
</style>