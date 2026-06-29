<!-- <template>
  <div class="container" v-if="!phylogeneticTreeImage">
    <h2 class="title">Mafft序列比对及系统发育树构建</h2>
    <div class="blast-container">
      <form @submit.prevent="submitForm">
        <div class="job-title">
          <label for="jobTitle">实验命名:</label>
          <input id="jobTitle" v-model="jobTitle" type="text" />
        </div>
        <div class="database-selection">
          <label for="databaseSelection">DataBase:</label>
          <select id="databaseSelection" v-model="selectedDataBase">
            <option value="18S">SSU rRNA RNA 序列</option>
            <option value="GP60">gp60 序列</option>
          </select>
        </div>
        <div class="query-sequence">
          <label for="querySequence">请输入对比序列:</label>
          <textarea
            id="querySequence"
            v-model="querySequence"
            cols="30"
            rows="10"
          ></textarea>
        </div>
        <div class="file-upload">
          <label for="fileUpload">选择文件上传:</label>
          <input
            type="file"
            id="fileUpload"
            accept=".fasta,.fas"
            @change="handleFileUpload"
            style="display: none"
          />
          <label for="fileUpload" class="file-label">{{ selectedFileName }}</label>
        </div>
      </form>
    </div>
    <div class="buttons">
      <button type="submit" class="blast-button" @click="submitMafft">提交并生成系统发育树</button>
      <button type="button" class="reset-button" @click="resetForm">Reset</button>
    </div>

   
    
  </div>
  <div v-if="phylogeneticTreeImage" class="image-display">
      <h3>发育树图像</h3>
      <img :src="phylogeneticTreeImage" alt="发育树图像" />
    </div>
</template>

<script setup>
import { ref } from "vue";

const jobTitle = ref("");
const querySequence = ref("");
const selectedFileName = ref("未选择任何文件");
const selectedDataBase = ref("18S");
let uploadedFile = null;
let mafftResultBlob = null;
let phylogeneticTreeImage = ref(null); // 新增：用于保存发育树图像的URL

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file) {
    selectedFileName.value = file.name;
    uploadedFile = file;
  }
}

async function submitMafft() {
  if (!validateForm()) {
    return;
  }

  const formDataToSend = new FormData();
  formDataToSend.append("jobTitle", jobTitle.value);
  formDataToSend.append("querySequence", querySequence.value);
  formDataToSend.append("selectedDataBase", selectedDataBase.value);
  formDataToSend.append("experimentName", jobTitle.value); // 使用实验命名作为 experimentName

  if (uploadedFile) {
    formDataToSend.append("file", uploadedFile);
  }

  try {
    const response = await fetch("/api/runMafft", {
      method: "POST",
      body: formDataToSend,
    });

    if (response.ok) {
      mafftResultBlob = await response.blob();
      console.log("MAFFT 成功，准备运行IQ-TREE...");
      runIqTree(); // 运行IQ-TREE分析
    } else {
      console.error("Mafft 失败:", response.statusText);
      alert("系统发育树生成失败，请重试！");
    }
  } catch (error) {
    console.error("请求出错:", error);
    alert("请求出错，请重试！");
  }
}

async function runIqTree() {
  if (!mafftResultBlob) {
    console.error("No MAFFT result available.");
    return;
  }

  const formData = new FormData();
  formData.append('alignedFile', new File([mafftResultBlob], "mafft_result.fasta", { type: 'text/plain' }));
  formData.append('experimentName', jobTitle.value);

  try {
    const response = await fetch("/api/iqtree", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      const contreeBlob = await response.blob();

      // 使用contreeBlob调用新的API生成发育树图像
      await generatePhylogeneticTreeImage(contreeBlob);
      alert("发育树图像生成成功！");
    } else {
      console.error("IQ-TREE 失败:", response.statusText);
      alert("IQ-TREE 分析失败，请重试！");
    }
  } catch (error) {
    console.error("IQ-TREE 请求出错:", error);
    alert("IQ-TREE 请求出错，请重试！");
  }
}

function handleImageDownload(blob) {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.style.display = 'none';
  a.href = url;
  a.download = "phylogenetic_tree.png"; // 设定下载的文件名为 phylogenetic_tree.png
  document.body.appendChild(a);
  a.click();
  window.URL.revokeObjectURL(url);
  document.body.removeChild(a);
}


function validateForm() {
  if (!jobTitle.value.trim()) {
    alert("请填写实验命名");
    return false;
  }
  if (!querySequence.value.trim() && !uploadedFile) {
    alert("请输入对比序列或选择文件上传");
    return false;
  }
  if (!selectedDataBase.value) {
    alert("请选择数据库");
    return false;
  }
  return true;
}

function resetForm() {
  jobTitle.value = "";
  querySequence.value = "";
  selectedFileName.value = "未选择任何文件";
  selectedDataBase.value = "18S";
  uploadedFile = null;
  mafftResultBlob = null;
}

async function generatePhylogeneticTreeImage(contreeBlob) {
  const formData = new FormData();
  formData.append('file', new File([contreeBlob], "output-newick.contree", { type: 'text/plain' }));

  try {
    const response = await fetch("/api/treeImage", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      const imageBlob = await response.blob();
      /* handleImageDownload(imageBlob); // 下载发育树图像 */
      const imageUrl = URL.createObjectURL(imageBlob);
      phylogeneticTreeImage.value = imageUrl; // 更新图像URL
    } else {
      console.error("发育树图像生成失败:", response.statusText);
      alert("发育树图像生成失败，请重试！");
    }
  } catch (error) {
    console.error("发育树图像请求出错:", error);
    alert("发育树图像请求出错，请重试！");
  }
}
</script> -->


<template>
  <div class="container" v-if="!phylogeneticTreeImage">
    <h2 class="title">系统发育树构建</h2>
    <div class="blast-container">
      <form @submit.prevent="submitForm">
        <div class="job-title">
          <label for="jobTitle">实验命名:</label>
          <input id="jobTitle" v-model="jobTitle" type="text" />
        </div>
        <div class="database-selection">
          <label for="databaseSelection">DataBase:</label>
          <select id="databaseSelection" v-model="selectedDataBase">
            <option value="18S">SSU rRNA RNA 序列</option>
            <option value="GP60">gp60 序列</option>
          </select>
        </div>
        <div class="query-sequence">
          <label for="querySequence">请输入对比序列:</label>
          <textarea
            id="querySequence"
            v-model="querySequence"
            cols="30"
            rows="10"
          ></textarea>
        </div>
        <div class="file-upload">
          <label for="fileUpload">选择文件上传:</label>
          <input
            type="file"
            id="fileUpload"
            accept=".fasta,.fas"
            @change="handleFileUpload"
            style="display: none"
          />
          <label for="fileUpload" class="file-label">{{ selectedFileName }}</label>
        </div>
      </form>
    </div>
    <div class="buttons">
      <button type="submit" class="blast-button" @click="submitMafft">提交并生成系统发育树</button>
      <button type="button" class="reset-button" @click="resetForm">Reset</button>
    </div>

    <!-- 新增：条件渲染的图像显示区域 -->
    
  </div>
  <div v-if="phylogeneticTreeImage" class="image-display">
      <h3>发育树图像</h3>
      <img :src="phylogeneticTreeImage" alt="发育树图像" />
    </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from 'element-plus'
const jobTitle = ref("");
const querySequence = ref("");
const selectedFileName = ref("未选择任何文件");
const selectedDataBase = ref("18S");
let uploadedFile = null;
let mafftResultBlob = null;
let processedFasta = null;
let phylogeneticTreeImage = ref(null); // 新增：用于保存发育树图像的URL
let userSeqID = ref(""); // 提取的序列号

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file) {
    selectedFileName.value = file.name;
    uploadedFile = file;
    // 如果是文件上传，读取文件内容并提取序列号
    const reader = new FileReader();
    reader.onload = function(e) {
      const content = e.target.result;
      extractSeqID(content);
    };
    reader.readAsText(file);
  }
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

async function submitMafft() {
  ElMessage.success("请稍等...")
  if (!validateForm()) {
    return;
  }

  const formDataToSend = new FormData();
  formDataToSend.append("jobTitle", jobTitle.value);
  formDataToSend.append("querySequence", querySequence.value);
  formDataToSend.append("selectedDataBase", selectedDataBase.value);
  formDataToSend.append("experimentName", jobTitle.value); // 使用实验命名作为 experimentName

  if (uploadedFile) {
    formDataToSend.append("file", uploadedFile);
  } else {
    // 如果没有上传文件，则从querySequence中提取序列号
    extractSeqID(querySequence.value);
  }

  if (uploadedFile) {
    formDataToSend.append("file", uploadedFile);
  }

  try {
    const response = await fetch("/api/runMafft", {
      method: "POST",
      body: formDataToSend,
    });

    if (response.ok) {
      mafftResultBlob = await response.blob();
      console.log("MAFFT 成功，准备运行IQ-TREE...");
      console.log(`Current userSeqID: ${userSeqID.value}`);
      // 调用处理函数  需要对返回的mafft结果进行处理再运行发育树分析
      processedFasta = await processAndTrimMafftResult(mafftResultBlob, userSeqID.value, selectedDataBase.value);
      
      console.log("Processed Fasta Type:", typeof processedFasta);
      console.log("Processed Fasta is Blob:", processedFasta instanceof Blob);

      if (typeof processedFasta === 'string'&&processedFasta.startsWith("错误：")) {
        alert(processedFasta); // 显示错误信息
        return; // 停止后续执行
      }
  

      // 更新 mafftResultBlob 为裁剪后的结果
      // mafftResultBlob = processedFasta;
    
      // const url =window.URL.createObjectURL(mafftResultBlob );
      // const a = document.createElement('a');
      // a.href = url;
      // a.download = 'mafft_result.fasta';
      // document.body.appendChild(a);
      // a.click();
      // document.body.removeChild(a);
      // window.URL.revokeObjectURL(url);

      // waiting
      
      runIqTree(); // 运行IQ-TREE分析
    } else {
      console.error("Mafft 失败:", response.statusText);
      alert("系统发育树生成失败，请重试！");
    }
  } catch (error) {
    console.error("请求出错:", error);
    alert("请求出错，请重试！");
  }
}

// 序列裁剪

// 处理并裁剪mafft结果
async function processAndTrimMafftResult(mafftResultBlob, userSeqID, sequenceType) {
  const text = await mafftResultBlob.text();
  const seqDict = parseFasta(text);
  
  // 检查用户提交的序列是否存在
  // if (!seqDict.hasOwnProperty(userSeqID)) {
  //   return "错误：序列未找到\n";
  // }

  const [maxStart, minEnd] = processSequences(seqDict);
  const processedFasta = writeFasta(seqDict, userSeqID, maxStart, minEnd, sequenceType);

  // if (typeof processedFasta === 'string') {
  //   // 如果是错误信息，则直接返回
  //   return processedFasta;
  // }
  if (typeof processedFasta === 'string' && processedFasta.startsWith("错误：")) {
    // 如果是错误信息，则直接返回
    return processedFasta;
  }

  // 将裁剪后的fasta内容转换为Blob对象
  const processedFastaBlob = new Blob([processedFasta], { type: 'text/plain' });
  return processedFastaBlob;
}


// 解析fasta文件
function parseFasta(inputData) {
  // const seqDict = {};
  const seqDict = new Map();
  let seqID = null; // Initialize seqID to None
  let seqData = []; // Store the data of the current sequence

  inputData.split('\n').forEach(line => {
    line = line.trim();
    if (line.startsWith('>')) {
      // If a sequence ID already exists, add its data to the dictionary
      if (seqID !== null) {
        //seqDict[seqID] = seqData.join(''); // Convert the list to a string and keep original case
        seqDict.set(seqID,seqData.join(''));
      }

      // Extract new sequence ID
      seqID = line.substring(1); // Remove the '>' symbol
      seqData = []; // Clear the sequence data list
    } else if (seqID !== null) {
      seqData.push(line);
    }
  });

  // Process the last sequence (if no new '>' symbol is encountered)
  if (seqID !== null) {
    //seqDict[seqID] = seqData.join(''); // Convert the list to a string and keep original case
    seqDict.set(seqID, seqData.join(''));
  }
   console.log([...seqDict]);

   return seqDict;
 
}

// 查找序列的起始和结束位置
function findStartEnd(seq) {
  let start = null;
  let end = null;

  // Find the first non-gap position (start)
  for (let i = 0; i < seq.length; i++) {
    if (seq[i] !== '-') {
      start = i;
      break;
    }
  }

  // Find the last non-gap position (end)
  for (let i = seq.length - 1; i >= 0; i--) {
    if (seq[i] !== '-') {
      end = i + 1; // +1 because slice index is exclusive
      break;
    }
  }

  return [start, end];
}

// 处理所有序列，找到最大起始和最小结束位置
function processSequences(seqDict) {
  const starts = [];
  const ends = [];

  for (const [seqID, seq] of seqDict) {
    const [start, end] = findStartEnd(seq);
    if (start !== null && end !== null) {
      starts.push(start);
      ends.push(end);
    } else {
      console.warn(`Warning: Sequence ${seqID} has no non-gap characters.`);
    }
  }

  if (starts.length === 0 || ends.length === 0) {
    throw new Error("No valid sequences found.");
  }

  const maxStart = Math.max(...starts);
  const minEnd = Math.min(...ends);

  return [maxStart, minEnd];
}

// 检查序列模式
function checkSequencePattern(seq) {
  const pattern = /(TC[AG]){3,20}/i;
  return pattern.test(seq);
}

// 写入fasta文件
function writeFasta(seqDict, userSeqID, maxStart, minEnd, sequenceType) {
  // const userSeq = seqDict[userSeqID];
  const userSeq = seqDict.get(userSeqID);
  const userSeqNoGaps = userSeq.replace(/-/g, '');

  if (userSeqNoGaps.length < 500) {
    return "错误：用户提交序列长度过短（<500bp），无法继续序列比对\n";
  }

  if (sequenceType === "GP60" && !checkSequencePattern(userSeqNoGaps)) {
    return "错误：用户提交序列不包含TCA重复区\n";
  }

  // Now we can trim all sequences using maxStart and minEnd
  let output = '';
  // 

  for (const [seqID, seq] of seqDict) {
    const trimmedSeq = seq.substring(maxStart, minEnd);
    output += `>${seqID}\n`;


    // 每行60字符
    for (let i = 0; i < trimmedSeq.length; i += 60) {
      output += trimmedSeq.substring(i, i + 60) + '\n';
    }
  }

  console.log(`Successfully wrote output for sequence ${userSeqID}`);
  // console.log(output);
  return output;
}



// 运行发育树
async function runIqTree() {
  if (!mafftResultBlob) {
    console.error("No MAFFT result available.");
    return;
  }

  const formData = new FormData();
  formData.append('alignedFile', new File([mafftResultBlob], "mafft_result.fasta", { type: 'text/plain' }));
  formData.append('experimentName', jobTitle.value);

  try {
    const response = await fetch("/api/iqtree", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      const contreeBlob = await response.blob();

      // 使用contreeBlob调用新的API生成发育树图像
      await generatePhylogeneticTreeImage(contreeBlob);
      alert("发育树图像生成成功！");
    } else {
      console.error("IQ-TREE 失败:", response.statusText);
      alert("IQ-TREE 分析失败，请重试！");
    }
  } catch (error) {
    console.error("IQ-TREE 请求出错:", error);
    alert("IQ-TREE 请求出错，请重试！");
  }
}


// 下载发育树图像
function handleImageDownload(blob) {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.style.display = 'none';
  a.href = url;
  a.download = "phylogenetic_tree.png"; // 设定下载的文件名为 phylogenetic_tree.png
  document.body.appendChild(a);
  a.click();
  window.URL.revokeObjectURL(url);
  document.body.removeChild(a);
}


function validateForm() {
  if (!jobTitle.value.trim()) {
    alert("请填写实验命名");
    return false;
  }
  if (!querySequence.value.trim() && !uploadedFile) {
    alert("请输入对比序列或选择文件上传");
    return false;
  }
  if (!selectedDataBase.value) {
    alert("请选择数据库");
    return false;
  }
  return true;
}


function resetForm() {
  jobTitle.value = "";
  querySequence.value = "";
  selectedFileName.value = "未选择任何文件";
  selectedDataBase.value = "18S";
  uploadedFile = null;
  mafftResultBlob = null;
}

async function generatePhylogeneticTreeImage(contreeBlob) {
  const formData = new FormData();
  formData.append('file', new File([contreeBlob], "output-newick.contree", { type: 'text/plain' }));

  try {
    const response = await fetch("/api/treeImage", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      const imageBlob = await response.blob();
      /* handleImageDownload(imageBlob); // 下载发育树图像 */
      const imageUrl = URL.createObjectURL(imageBlob);
      phylogeneticTreeImage.value = imageUrl; // 更新图像URL
    } else {
      console.error("发育树图像生成失败:", response.statusText);
      alert("发育树图像生成失败，请重试！");
    }
  } catch (error) {
    console.error("发育树图像请求出错:", error);
    alert("发育树图像请求出错，请重试！");
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
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 使子元素在垂直方向居中对齐 */
}

.job-title label {
  font-size: 14px;
  margin-right: 10px; /* 设置 label 和 input 之间的间距 */
  white-space: nowrap; /* 防止文字换行 */
}

.job-title input {
  padding: 5px; /* 给输入框增加内边距 */
  width: 200px; /* 设置输入框的宽度 */
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
  margin-right:40px;
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

.image-display {
  margin-top: 20px;
  text-align: center;
}

.image-display img {
  max-width: 100%;
  height: auto;
}
</style>





