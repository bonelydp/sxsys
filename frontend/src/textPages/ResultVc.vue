<template>
  <el-card class="result-container" v-if="topResults.length > 0">
    <h2 class="title">SSU rRNA序列对比结果(根据18S序列库比对结果)</h2>
    <div class="info-section">
      <el-descriptions title="" :column="1" direction="vertical" border>
        <el-descriptions-item label="样本编号">{{ sampleNumber }}</el-descriptions-item>
        <el-descriptions-item label="匹配结果">
          <el-table :data="topResults" style="width: 100%" @row-click="showStandardSequence" highlight-current-row
            stripe>
            <el-table-column prop="sequenceName" label="序列名称" width="180"></el-table-column>
            <el-table-column prop="sequenceId" label="序列号" width="180"></el-table-column>
            <el-table-column prop="species" label="虫种"></el-table-column>
            <el-table-column prop="subtype" label="亚型"></el-table-column>
            <el-table-column prop="alignmentLength" label="匹配长度"></el-table-column>
            <el-table-column prop="identity" label="一致性">
              <template #default="scope">
                {{ scope.row.identity }}%
              </template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
        <el-descriptions-item label="基因序列（显示库中匹配的序列）" v-if="selectedSequence">
          <div class="alignment-results">
            <div class="alignment-result-details">
              <el-collapse-transition>
                <div class="result-details">
                  <el-descriptions :column="1" direction="vertical" border>
                    <el-descriptions-item label="序列名称"><strong>{{ selectedSequence.id }}</strong></el-descriptions-item>
                    <el-descriptions-item label="具体序列">
                      <pre>{{ selectedSequence.sequence }}</pre>
                    </el-descriptions-item>
                  </el-descriptions>
                </div>
              </el-collapse-transition>
            </div>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import { useBlastStore } from '@/store/blast'

const blastStore = useBlastStore()
const topResults = ref([]);
const selectedSequence = ref(null);
const formData = reactive(null)

onMounted(() => {
  // 从 store 中获取 topResults 和 selectedSequence
  topResults.value = blastStore.blastResults;
  console.log("topResults.value",topResults.value)
  selectedSequence.value = blastStore.selectedSequence;

});
const standardSequences = ref({});
// 新增一个获取标准序列的方法
// 更新 fetchStandardSequence 函数中的 URL
async function fetchStandardSequence(subjectId) {
  if (standardSequences.value[subjectId]) {
    return standardSequences.value[subjectId];
  }

  formData.value = blastStore.formData
  // 调用后端API获取序列
  const response = await fetch(`/api/blast/sequence/${subjectId}?selectedDatabase=${formData.selectedDatabase}`);
  if (response.ok) {
    const sequence = await response.text();
    standardSequences.value[subjectId] = { id: subjectId, sequence };
    return standardSequences.value[subjectId];
  } else {
    console.error("Failed to fetch standard sequence:", response.statusText);
    return null;
  }
}

function combineSequenceInfo(sequenceId, sequenceName) {
  return `${sequenceId}_${sequenceName}`;
}

function showStandardSequence(row, column, event) {
  const subjectId = combineSequenceInfo(row.sequenceId,row.sequenceName)
  fetchStandardSequence(subjectId).then(sequence => {
    if (sequence) {
      selectedSequence.value = sequence;
    }
  });
}

</script>

<style scoped>
.result-container {
  margin: 20px auto;
  width: 1100px;
}

.title {
  margin-bottom: 20px;
  font-size: 16px;
  /* 调整标题字体大小 */
  color: #333;
  text-align: center;
}

.info-section {
  margin-bottom: 20px;
}

.alignment-results {
  width: 100%;
}

.alignment-result-details {
  width: 100%;
  margin-top: 10px;
}

.result-details {
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
}

pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
