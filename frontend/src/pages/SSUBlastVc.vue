<template>
  <div class="container" v-if="resultsTableData.length <= 0">
    <h2 class="title">Blast序列比对</h2>
    <div class="blast-container">
      <form @submit.prevent="submitForm">
        <div class="job-title">
          <label for="jobTitle">实验命名:</label>
          <input id="jobTitle" v-model="formData.jobTitle" type="text" />
        </div>
        <div class="query-sequence">
          <label for="querySequence">请输入对比序列:</label>
          <textarea id="querySequence" v-model="formData.querySequence" cols="30" rows="10"></textarea>
        </div>
        <div class="file-upload">
          <label for="fileUpload">选择文件上传:</label>
          <input type="file" id="fileUpload" accept=".fasta,.faa" @change="handleFileUpload" style="display: none" />
          <label for="fileUpload" class="file-label">{{ selectedFileName }}</label>
        </div>

        <div class="database-selection">
          <label>比对数据库:</label>
          <span>18s 序列</span>

        </div>
      </form>
    </div>
    <div class="buttons">
      <button type="button" class="blast-button" @click="submitBlast">Blast</button>
      <button type="button" class="reset-button" @click="resetForm">
        重置
      </button>
    </div>
  </div>

  <div class="Result" v-if="resultsTableData.length > 0">
    <div class="resultTitle">
      <div class="info-group">
        <span class="label">样本编号:</span>
        <span class="value">{{ userSeqID }}</span>
      </div>
      <div class="info-group">
        <span class="label">虫种:</span>
        <span class="value">{{ userSeqID }}</span>
      </div>
    </div>
    <div class="tipsText">
      <p style="color: #C00000;">*如一致性小于98%则显示：根据比对结果怀疑您的样本中可能存在新虫种，建议提交样本信息</p>
       <p v-if="highestSimilarity !== null && highestSimilarity < 98" style="color: #C00000;">根据比对结果怀疑您的样本中可能存在新虫种，建议提交样本信息</p>
      <p class="submit-link" @click="dataSubmit">数据提交</p>
    </div>
    
    <el-card class="result-date-container" >
      <h2 class="title">{{ formData.selectedDatabase }}对比结果</h2>
      <div class="info-section">
        <el-descriptions title="" :column="1" direction="vertical" border>
          <!-- <el-descriptions-item label="用户提交的序列名称">
        {{ userSeqID }}
        </el-descriptions-item> -->

          <el-descriptions-item label="匹配结果">
            <el-table :data="resultsTableData" style="width: 100%" @row-click="showSequence" highlight-current-row
              stripe>
              <el-table-column prop="subjectId" label="序列名称" width="180"></el-table-column>
              <el-table-column prop="accession" label="序列号" width="180"></el-table-column>
              <el-table-column prop="species" label="虫种"></el-table-column>
              <el-table-column v-if="formData.selectedDatabase === 'Cry-GP60-2024-11-24-核对'" prop="subtype"
                label="亚型"></el-table-column>
              <el-table-column prop="alignmentLength" label="匹配长度"></el-table-column>
              <el-table-column prop="similarity" label="一致性">
                <template #default="scope">
                  {{ scope.row.similarity }}%
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
                      <el-descriptions-item label="序列名称"><strong>{{
                        selectedSequence.name }}</strong></el-descriptions-item>
                      <el-descriptions-item label="具体序列">
                        <pre class="sequence-pre">{{ selectedSequence.baseSequence }}</pre>
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
  </div>

</template>

<script setup>
import { ref, reactive } from "vue";
import axios from "axios";
let userSeqID = ref(""); // 提取的序列号
let highestSimilarity = ref(null); // 用于保存最高相似性


const formData = reactive({
  jobTitle: "",
  querySequence: "",
  file: null,
  selectedDatabase: "18S-参考基因组-2024-11-24", 
});

const selectedFileName = ref("未选择任何文件");
const resultsTableData = ref([]);
const selectedSequence = ref(null); // 新增：用于存储当前选中的序列信息
const baseSequence = ref(""); // 用于存储和显示碱基序列

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file) {
    selectedFileName.value = file.name;
    formData.file = file;
    // 如果是文件上传，读取文件内容并提取序列号
    const reader = new FileReader();
    reader.onload = function(e) {
      const content = e.target.result;
      extractSeqID(content);
    };
    reader.readAsText(file);

  }

}
import { useRouter } from "vue-router";
const router = useRouter()
function dataSubmit(){
  router.push("/datasubmit")
}

import { useTokenStore } from "@/store/mytoken"
import { clearConfig } from "dompurify";
import { ElMessage } from "element-plus";
const tokenStore = useTokenStore()
async function submitBlast() {
  ElMessage.success("请稍等...")
  if (!validateForm()) {
    return;
  }

  const formDataToSend = new FormData();
  formDataToSend.append("jobTitle", formData.jobTitle);
  formDataToSend.append("selectedDatabase", formData.selectedDatabase);

  let querySequences = {}; // 存储查询序列及其长度

  if (formData.file) {
    // 用户上传了文件
    formDataToSend.append("file", formData.file);
    const fileReader = new FileReader();
    fileReader.onload = async (e) => {
      const fastaText = e.target.result;
      querySequences = parseFasta(fastaText);
      if (Object.keys(querySequences).length > 0) {
        try {
          console.log("tokenStore.token", tokenStore.token)
          const response = await fetch("/api/runBlast", {
            method: "POST",
            body: formDataToSend,
            headers: {
              'Authorization': `${tokenStore.token}`,
              'Accept': 'application/json'
            },
          });

          if (response.ok) {
            const text = await response.text();
            parseAndFillResults(text, querySequences);
            showResultsTable();
            alert("Blast 成功！");
          } else {
            console.error("Blast1 失败:", response.statusText);
            alert("Blast 失败，请重试！");
          }
        } catch (error) {
          console.error("请求出错:", error);
          alert("请求出错，请重试！");
        }
      } else {
        alert("无法从上传的文件中找到有效的序列，请检查文件格式。");
      }
    };
    fileReader.readAsText(formData.file);
  } else if (formData.querySequence.trim()) {
    // 用户直接输入了序列
    formDataToSend.append("querySequence", formData.querySequence);
    const querySequence = formData.querySequence.replace(/[\r\n]+/g, ''); // 去除换行符
    const queryLength = querySequence.length;
    extractSeqID(querySequence.value);


    try {
      const response = await fetch("/api/runBlast", {
        method: "POST",
        body: formDataToSend,
        headers: {
          'Authorization': `${tokenStore.token}`,
          'Accept': 'application/json'
        },
      });

      if (response.ok) {
        const text = await response.text();
        parseAndFillResults(text, { "query_sequence": queryLength });
        showResultsTable();
        alert("Blast 成功！");
      } else {
        console.error("Blast2 失败:", response.statusText);
        alert("Blast 失败，请重试！");
      }
    } catch (error) {
      console.error("请求出错:", error);
      alert("请求出错，请重试！");
    }
  } else {
    alert("请输入对比序列或选择文件上传。");
  }
}

function validateForm() {
  if (!formData.jobTitle.trim()) {
    alert("请填写实验命名");
    return false;
  }
  if (!formData.querySequence.trim() && !formData.file) {
    alert("请输入对比序列或选择文件上传");
    return false;
  }
  if (!formData.selectedDatabase) {
    formData.selectedDatabase="18S-参考基因组-2024-11-24";
    return false;
  }
  return true;
}

function resetForm() {
  formData.jobTitle = "";
  formData.querySequence = "";
  formData.file = null;
  selectedFileName.value = "未选择任何文件";
  formData.selectedDatabase = "18S-参考基因组-2024-11-24"; // 重置为默认值
  resultsTableData.value = []; // 重置表格数据
  selectedSequence.value = null; // 清空选中的序列信息
  baseSequence.value = ""; // 清空碱基序列
}

function parseAndFillResults(text, querySequences) {
  const lines = text.trim().split('\n');

  // 解析BLAST结果
  const allResults = [];

  lines.forEach(line => {
    const parts = line.split(/\s+/);
    if (parts.length >= 12) {
      const queryId = parts[0];
      const subjectId = parts[1];
      const similarity = parseFloat(parts[2]);
      const alignmentLength = parseInt(parts[3]);

      if (similarity > 80) {
        allResults.push({ queryId, subjectId, similarity, alignmentLength });
      }
    }
  });

  // 过滤和排序所有结果
  const filteredResults = allResults
    .filter(result => {
      const queryLength = querySequences[result.queryId];
      return queryLength && result.alignmentLength > 0.6 * queryLength;
    })
    .sort((a, b) => b.similarity - a.similarity || b.alignmentLength - a.alignmentLength)
    .slice(0, 5); // 只保留前5个结果


  // 获取最高相似性
  highestSimilarity.value = filteredResults.length > 0 ? parseFloat(filteredResults[0].similarity) : null;


  // 将结果填充到表格数据中
  filteredResults.forEach(result => {
    const row = {};
    row.queryId = result.queryId; // 添加查询序列标识符
    row.subjectId = result.subjectId;
    row.similarity = result.similarity.toFixed(3);
    row.alignmentLength = result.alignmentLength;

    if (formData.selectedDatabase === "Cry-GP60-2024-11-24-核对") {
      const [accession, species, subtype] = splitId(row.subjectId);
      row.accession = accession;
      row.species = species;
      row.subtype = subtype;
    } else {
      const [accession, species] = splitS18Id(row.subjectId);
      row.accession = accession;
      row.species = species;
    }

    resultsTableData.value.push(row);
    console.log("resultsTableData.value", resultsTableData.value)
  });
}

function splitId(id) {
  const parts = id.split('_');
  const accession = parts[0];
  const species = parts.slice(1, parts.length - 1).join('_').replace(/_/g, ' ');
  const subtype = parts[parts.length - 1];
  return [accession, species, subtype];
}

function splitS18Id(id) {
  const parts = id.split('_');
  const accession = parts[0];
  const species = parts.slice(1).join('_').replace(/_/g, ' ');
  return [accession, species];
}



function showResultsTable() {
  // 这里可以添加一些额外的逻辑，例如滚动到表格位置等
}

async function showSequence(row) {
  // 设置选中的序列信息
  selectedSequence.value = {
    name: row.subjectId,
    baseSequence: '' // 初始为空
  };

  // 发起请求获取碱基序列
  try {
    const response = await axios.get('/api/sequence', {
      headers: {
        'Authorization': `${tokenStore.token}`,
      },
      params: {
        sequenceName: row.subjectId,
        database: formData.selectedDatabase
      }
    });

    if (response.status === 200) {
      selectedSequence.value.baseSequence = response.data;
      baseSequence.value = response.data; // 更新碱基序列显示
      selectedSequence.value.baseSequence = selectedSequence.value.baseSequence.replace(/\n/g, '');
    } else {
      console.error("Failed to retrieve sequence:", response.statusText);
      alert("Failed to retrieve sequence, please try again.");
    }
  } catch (error) {
    console.error("Error retrieving sequence:", error);
    alert("Error retrieving sequence, please try again.");
  }
}


function parseFasta(fastaText) {
  const sequences = {};
  let currentId = null;
  let currentSequence = '';

  fastaText.split('\n').forEach(line => {
    if (line.startsWith('>')) {
      if (currentId !== null) {
        sequences[currentId] = currentSequence.length;
      }
      currentId = line.substring(1).trim();
      currentSequence = '';
    } else {
      currentSequence += line.trim();
    }
  });

  if (currentId !== null) {
    sequences[currentId] = currentSequence.length;
  }

  return sequences;
}

// 提取序列号
function extractSeqID(content) {
  const lines = content.split('\n');
  const firstLine = lines[0].trim();
  if (firstLine.startsWith('>')) {
    userSeqID.value = firstLine.substring(1).split(' ')[0]; // 取第一个空格前的部分作为序列号
  } else {
    console.error("Invalid FASTA format: No sequence ID found.");
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;

}

.title {
  margin-bottom: 20px;
  font-size: 24px;
}

.blast-container {
  width: 800px;
  padding: 20px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: Arial, sans-serif;
}

form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.query-sequence {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.query-sequence label {
  font-size: 14px;
}

.query-sequence textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: vertical;
}

.file-upload {
  display: flex;
  align-items: center;
}

.file-label {
  width: 200px;
  cursor: pointer;
  padding: 5px;
  margin-left: 10px;
  background-color: #ffffff;
  color: rgb(5, 5, 5);
  border-radius: 5px;
  text-align: center;
  transition: background-color 0.3s ease;
  border: 1px solid #ccc;
}

.file-label:hover {
  background-color: #d7d7d7;
}

.job-title {
  display: flex;
  /* 使用 Flexbox 布局 */
  align-items: center;
  /* 使子元素在垂直方向居中对齐 */
}

.job-title label {
  font-size: 14px;
  margin-right: 10px;
  /* 设置 label 和 input 之间的间距 */
  white-space: nowrap;
  /* 防止文字换行 */
}

.job-title input {
  padding: 5px;
  /* 给输入框增加内边距 */
  width: 200px;
  /* 设置输入框的宽度 */
  border: 1px solid #ccc;
  border-radius: 5px;
}

.database-selection {
  display: flex;
  align-items: center;
}

.database-selection label {
  font-size: 14px;
}

.database-selection select {
  width: 500px;
  padding: 10px;
  margin-left: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;

}

.blast-button,
.reset-button {
  padding: 10px 30px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  margin-left: 40px;
}

.blast-button {
  background-color: #4ea4cb;
  color: white;
}

.blast-button:hover {
  background-color: #0977aa;
}

.reset-button {
  background-color: #6c757d;
  color: white;
}

.reset-button:hover {
  background-color: #5a6268;
}


.results-container {
  margin-top: 80px;
  /* 根据需要调整这个值 */
}

.results-table {
  width: 100%;
  /* 表格宽度占满父容器 */
  max-width: 1200px;
  /* 最大宽度限制，防止表格过大 */
  border-collapse: collapse;
  margin-top: 20px;
  /* 表格与上方内容的额外间距 */
  font-size: 16px;
  /* 调整字体大小 */
}

.results-table th,
.results-table td {
  border: 1px solid #ddd;
  padding: 12px;
  /* 增加内边距，使单元格更宽敞 */
  text-align: left;
}

.results-table th {
  background-color: #f2f2f2;
  font-weight: bold;
  font-size: 18px;
  /* 表头字体稍大一点 */
}

/* 如果需要，可以为按钮也调整字体大小 */
button {
  font-size: 16px;
  /* 按钮字体大小 */
}

/* 新增的框的样式 */
.sequence-box {
  margin-top: 50px;
  /* 与表格之间有一定的间距 */
  border: 1px solid #ddd;
  /* 边框 */
  border-radius: 4px;
  /* 圆角 */
  background-color: #fff;
  /* 背景色 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 阴影效果 */
  padding: 20px;
  /* 内边距 */
  width: 100%;
  /* 宽度占满父容器 */
  max-width: 1200px;
  /* 最大宽度限制 */
}

.sequence-header {
  font-size: 18px;
  /* 标题字体大小 */
  font-weight: bold;
  /* 加粗 */
  margin-bottom: 10px;
  /* 底部间距 */
  color: #333;
  /* 文字颜色 */
}

.sequence-name,
.sequence-base {
  font-size: 16px;
  /* 字体大小 */
  color: #555;
  /* 文字颜色 */
  margin-bottom: 10px;
  /* 底部间距 */
}

.sequence-base {
  white-space: pre-wrap;
  /* 保留空白符序列格式 */
  word-break: break-all;
  /* 避免长单词或序列导致的溢出 */
}
.Result {
  justify-items: center;

}

.resultTitle {
  width: auto;
  border: 2px solid #9f9fa1;
  border-radius: 5px;
  display: flex;
  padding: 10px;
}
.info-group{
  display: flex;
  margin-left: 40px;
  margin-right: 40px;
}

.tipsText{
  display: flex;
}

.submit-link{
  color: #6AA4CA;
  font-size: 10px;
  margin-top: 23px;
  margin-left: 5px;
  
  cursor: pointer;
}

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

.sequence-pre {
  margin: 0;
  white-space: pre-wrap;
  /* 保留空白符序列，但正常地进行换行 */
  word-break: break-all;
  /* 在单词内换行 */
  overflow-wrap: break-word;
  /* 在单词内换行 */
  max-width: 100%;
  /* 确保内容不超过容器宽度 */
}
</style>
