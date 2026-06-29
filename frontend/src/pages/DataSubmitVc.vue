<template>
  <el-form v-if="showResult" ref="formRef" :model="formData" :rules="rules" label-width="90px" class="formContainer">
    <div class="leftContainer">
      <el-form-item prop="sampleNumber" class="sampleContainer">
        <div class="titleText"><el-text>样本编号</el-text></div>
        <el-input class="sampleImput" v-model="formData.sampleNumber"></el-input>
      </el-form-item>
      <el-form class="geoContainer">
        <div class="testT">
          <el-text>地理来源</el-text>
        </div>
        <el-form-item label="国家" prop="country">
          <el-input v-model="formData.country"></el-input>
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="formData.province"></el-input>
        </el-form-item>
        <el-form-item label="市" prop="city">
          <el-input v-model="formData.city"></el-input>
        </el-form-item>
        <el-form-item label="县/镇" prop="district">
          <el-input v-model="formData.district"></el-input>
        </el-form-item>
        <el-form-item label="详细地址" prop="addressDetails">
          <el-input v-model="formData.addressDetails"></el-input>
        </el-form-item>
      </el-form>

      <el-form class="samplemessageContainer">
        <div class="testT">
          <el-text>样本信息</el-text>
        </div>
        <el-form-item label="样本类型">
          <el-select v-model="formData.sampleType" placeholder="请选择" @change="handleSampleTypeChange">
            <el-option value="粪便">粪便</el-option>
            <el-option value="其他">其他</el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="showOtherInput" label-width="auto">
          <el-input v-model="formData.otherSampleType" placeholder="请输入样本类型"></el-input>
        </el-form-item>
        <el-form-item label="采集时间">
          <el-date-picker v-model="formData.collectionTime" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="样本储存条件" label-width="auto">
          <el-select v-model="formData.storageConditions" placeholder="请选择">
            <el-option value="2.5%重铬酸钾">2.5%重铬酸钾</el-option>
            <el-option value="75%酒精">75%酒精</el-option>
            <el-option value="-80℃冷藏">-80℃冷藏</el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <el-form class="outbreakContainer">
        <div class="testT">
          <el-text>暴发情况</el-text>
        </div>
        <el-form-item label="是否暴发">
          <el-select v-model="formData.outbreak" placeholder="请选择" @change="handleIsOutbreak">
            <el-option value="是">是</el-option>
            <el-option value="否">否</el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="开始时间" v-if="isOutbreak">
          <el-date-picker v-model="formData.startTime" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" v-if="isOutbreak">
          <el-date-picker v-model="formData.endTime" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="报告时间" v-if="isOutbreak">
          <el-date-picker v-model="formData.reportTime" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-row v-if="isOutbreak">
          <el-col :span="13">
            <el-form-item label="病畜数" >
              <el-input-number v-model="formData.diseaseCount" controls-position="right"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="死亡数">
              <el-input-number v-model="formData.deathCount" controls-position="right"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <div class="rightContainer">
      <el-form class="hostsourceContainer" v-if="!showOtherInput">
        <div class="testT">
          <el-text>宿主来源</el-text>
        </div>

        <div class="hostsourceLeft">
          <el-form-item label="宿主来源" label-width="auto" prop="hostSource">
            <el-select v-model="formData.hostSource" placeholder="请选择">
              <el-option value="奶牛">奶牛</el-option>
              <el-option value="肉牛">肉牛</el-option>
              <el-option value="绵羊">绵羊</el-option>
              <el-option value="山羊">山羊</el-option>
              <el-option value="猫">猫</el-option>
              <el-option value="狗">狗</el-option>
              <el-option value="猪">猪</el-option>
              <el-option value="啮齿类动物">啮齿类动物</el-option>
              <el-option value="其他">其他</el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="性别" label-width="auto" prop="gender">
            <el-select v-model="formData.gender" placeholder="请选择">
              <el-option value="雄性">雄性</el-option>
              <el-option value="female">雌性</el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="是否腹泻" label-width="auto" prop="diarrhea">
            <el-select v-model="formData.diarrhea" placeholder="请选择" @change="handlediarrheaChange">
              <el-option value="是">是</el-option>
              <el-option value="否">否</el-option>
            </el-select>
          </el-form-item>

          <el-form-item v-if="showdiarrheaType" label-width="auto">
            <el-select v-model="formData.diarrheaType" placeholder="请选择腹泻类型">
              <el-option value="中度腹泻">中度腹泻</el-option>
              <el-option value="水样腹泻">水样腹泻</el-option>
              <el-option value="血便">血便</el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="是否死亡" label-width="auto" prop="death">
            <el-select v-model="formData.death" placeholder="请选择">
              <el-option value="是">是</el-option>
              <el-option value="否">否</el-option>
            </el-select>
          </el-form-item>
        </div>

        <div class="hostsourceRight">
          <el-form-item label="宿主具体品种" prop="hostSpecies" label-width="auto">
            <el-input v-model="formData.hostSpecies"></el-input>
          </el-form-item>
          <el-form-item label="月龄/年龄" prop="age">
            <el-input-number v-model="formData.age" controls-position="right"></el-input-number>
          </el-form-item>

          <el-form-item label="其他症状" prop="otherSymptoms">
            <el-select v-model="formData.otherSymptoms" placeholder="请选择">
              <el-option value="呕吐">呕吐</el-option>
              <el-option value="发热">发热</el-option>
              <el-option value="消瘦">消瘦</el-option>
              <el-option value="被毛粗乱">被毛粗乱</el-option>
              <el-option value="其他">其他</el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="免疫力" prop="immunity" label-width="auto">
            <el-select v-model="formData.immunity" placeholder="请选择">
              <el-option value="正常">正常</el-option>
              <el-option value="免疫力缺陷">免疫力缺陷</el-option>
            </el-select>
          </el-form-item>
        </div>
      </el-form>

      <el-form class="testresultContainer">
        <div class="testT">
          <el-text>检测结果</el-text>
        </div>
        <div class="testresultContainerLeft">
          <el-form-item label="检测方法">
            <el-select v-model="formData.detectionMethod" placeholder="请选择检测方法">
              <el-option value="显微镜检测">显微镜检测</el-option>
              <el-option value="SSU rRNA PCR">SSU rRNA PCR</el-option>
              <el-option value="gp60 PCR">gp60 PCR</el-option>
              <el-option value="其他">其他</el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="显微镜检查结果" label-width="auto" prop="microscopyResult" v-if="formData.detectionMethod === '显微镜检测'">
            <el-select v-model="formData.microscopyResult" placeholder="请选择">
              <el-option value="阳性">阳性</el-option>
              <el-option value="阴性">阴性</el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上传镜检图片" label-width="auto" prop="jingpicture" v-if="formData.detectionMethod === '显微镜检测'">
            <el-upload
              :auto-upload="false"
              :show-file-list="false"
              :before-upload="beforeUploadImage"
              :on-change="handleImageChange"
              accept="image/*">
              <el-button size="small" type="primary" :loading="imageLoading">
                {{ selectedImageName || '未选择图片' }}
              </el-button>
            </el-upload>
          </el-form-item>
        </div>
        <div class="testresultContainerRight">
          <el-form-item label="虫种" prop="insectSpecies" v-if="formData.detectionMethod === 'SSU rRNA PCR'">
            <el-input v-model="formData.insectSpecies"></el-input>
          </el-form-item>
          <el-form-item label="亚型" prop="subtype" v-if="formData.detectionMethod === 'gp60 PCR'">
            <el-input v-model="formData.subtype"></el-input>
          </el-form-item>
        </div>
      </el-form>

      <el-form class="sequenceuploadContainer">
        <div class="testT">
          <el-text>序列上传</el-text>
        </div>

        <el-form-item label="标准序列选择" label-width="auto" prop="selectedStandardSequence">
          <el-select v-model="formData.selectedStandardSequence" placeholder="请选择标准序列">
            <el-option label="SSU rRNA RNA 序列" value="SSU"></el-option>
            <el-option label="gp60 序列" value="gp60"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="请选择文件上传" prop="file">
          <el-upload 
            :auto-upload="false"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-change="handleFileChange">
            <el-button size="small" type="primary" :loading="loading">
              {{ selectedFileName || '未选择文件' }}
            </el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <div class="buttonContainer">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </div>
    </div>
  </el-form>
  <router-view></router-view>
</template>

<script setup>
import { reactive, ref, onMounted, onUpdated } from "vue";
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import {
  ElForm,
  ElFormItem,
  ElInput,
  ElSelect,
  ElOption,
  ElRadioGroup,
  ElRadio,
  ElInputNumber,
  ElSwitch,
  ElUpload,
  ElButton,
} from "element-plus";
import { submitSampleService, uploadFileService } from '@/api/dataSubmit';
const formRef = ref(null);
const formData = reactive({
  // 基本信息
  sampleNumber: "",
  
  // 地理来源
  country: "",
  province: "",
  city: "",
  district: "",
  addressDetails: "",
  
  // 样本信息
  sampleType: "",
  collectionTime: "",
  storageConditions: "",
  
  // 暴发情况
  outbreak: "",  // 改自 isOutbreak
  startTime: null,
  endTime: "",
  reportTime: "",
  diseaseCount: null,
  deathCount: null,
  
  // 宿主来源
  hostSource: "",
  gender: "",
  diarrhea: "",
  diarrheaType: "",
  death: "",
  hostSpecies: "",
  age: null,
  otherSymptoms: "",
  immunity: "",
  
  // 检测结果
  detectionMethod: "",  // 改自 detectWay
  detectionMethodnew: "", // 新增字段
  insectSpecies: null,  // 改自 insectspecies
  subtype: null,
  
  // 其他字段
  jingpicture: null,  // 新增字段
  status: null,      // 新增字段
  file: "",
  selectedStandardSequence: null,
});
const rules = {};
// 假设服务器返回了序列数据
const router = useRouter();

const showResult = ref(true)

onUpdated(() => {
  console.log(router)
  console.log(router.currentRoute._value.path)
  if (router.currentRoute._value.path == "/datasubmit/resultSSU") {
    showResult.value = false;
  } else {
    showResult.value = true;
  }
})

//文件上传
const selectedStandardSequence = ref("");
const selectedFileName = ref("未选择文件");
const fileList = ref([]);
const loading = ref(false);
const filePath = ref(""); // 存储上传后的文件路径

//文件上传前的检查
function beforeUpload(file) {
  const isFasta = file.type === "chemical/x-fasta" || file.type === "text/plain";
  if (!isFasta) {
    ElMessage.error("只允许上传 .fasta 或 .faa 格式的文件");
    return false;
  }
  return true;
}

async function handleFileChange(file) {
  selectedFileName.value = file.name;
  fileList.value = [file];
  
  // 上传文件
  try {
    loading.value = true;
    const response = await uploadFileService(file.raw);
    if (response.data) {
      console.log("response.data.data+",response.data.data);
      filePath.value = response.data.data; // 保存文件路径
      ElMessage.success('文件上传成功！');
    } else {
      ElMessage.error('文件上传失败，请重试');
    }
  } catch (error) {
    console.error('文件上传出错：', error);
    ElMessage.error(error.message || '文件上传失败，请重试');
  } finally {
    loading.value = false;
  }
}

async function submitForm() {
  // 表单验证
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    
    // 准备提交的样本数据
    const sampleData = {
      // 基本信息
      sampleNumber: formData.sampleNumber,
      
      // 地理来源
      country: formData.country,
      province: formData.province,
      city: formData.city,
      district: formData.district,
      addressDetails: formData.addressDetails,
      
      // 样本信息
      sampleType: formData.sampleType === '其他' ? formData.otherSampleType : formData.sampleType,
      collectionTime: formData.collectionTime,
      storageConditions: formData.storageConditions,
      
      // 暴发情况
      outbreak: formData.outbreak,
      startTime: formData.startTime,
      endTime: formData.endTime,
      reportTime: formData.reportTime,
      diseaseCount: formData.diseaseCount,
      deathCount: formData.deathCount,
      
      // 宿主来源
      hostSource: formData.hostSource,
      hostSpecies: formData.hostSpecies,
      gender: formData.gender,
      age: formData.age,
      diarrhea: formData.diarrhea,
      diarrheaType: formData.diarrheaType,
      death: formData.death,
      otherSymptoms: formData.otherSymptoms,
      immunity: formData.immunity,
      
      // 检测结果
      detectionMethod: formData.detectionMethod,
      detectionMethodnew: formData.detectionMethodnew,
      insectSpecies: formData.insectSpecies,
      subtype: formData.subtype,
      
      // 其他字段
      jingpicture: formData.jingpicture,
      status: "PENDING",
      selectedStandardSequence: formData.selectedStandardSequence,
      
      // 添加文件路径
      file: filePath.value

  
    };

    // 调用提交接口
    const response = await submitSampleService(sampleData);
    
    if (response.data) {
      ElMessage.success('样本提交成功！');
      
    } else {
      ElMessage.error('样本提交失败，请重试');
    }
  } catch (error) {
    console.error('提交出错：', error);
    ElMessage.error(error.message || '提交失败，请检查表单数据');
  }
}

function resetForm() {
  formRef.value.resetFields();
}

// 选择其他显示填写框
const showOtherInput = ref(false);
const handleSampleTypeChange = (value) => {
  if (value == "其他") {
    showOtherInput.value = true;
  } else {
    showOtherInput.value = false;
  }
};
//是否暴发
const isOutbreak = ref(true);
const handleIsOutbreak = (value) => {
  if(value == "是"){
    formData.outbreak = "是";
    isOutbreak.value = true;
  }else{
    formData.outbreak = "否";
    isOutbreak.value = false;
  }
}
//选择腹泻类型
const showdiarrheaType = ref(false);
const handlediarrheaChange = (value) => {
  if (value == "是") {
    showdiarrheaType.value = true;
  } else {
    showdiarrheaType.value = false;
  }
};
//
const handleHostsourceChange = (value) => {
  if (value == "粪便") {
    showdiarrheaType.value = true;
  } else {
    showdiarrheaType.value = false;
  }
}

// 在 script setup 部分添加以下变量和方法
const selectedImageName = ref("未选择图片");
const imageLoading = ref(false);
const imagePath = ref(""); // 存储上传后的图片路径

//图片上传前的检查
function beforeUploadImage(file) {
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    ElMessage.error("只允许上传图片文件");
    return false;
  }
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    ElMessage.error("图片大小不能超过 5MB!");
    return false;
  }
  return true;
}

async function handleImageChange(file) {
  selectedImageName.value = file.name;
  
  // 上传图片
  try {
    imageLoading.value = true;
    const response = await uploadFileService(file.raw);
    if (response.data) {
      imagePath.value = response.data.data; // 保存图片路径
      formData.jingpicture = response.data.data; // 将图片路径保存到表单数据中
      ElMessage.success('图片上传成功！');
    } else {
      ElMessage.error('图片上传失败，请重试');
    }
  } catch (error) {
    console.error('图片上传出错：', error);
    ElMessage.error(error.message || '图片上传失败，请重试');
  } finally {
    imageLoading.value = false;
  }
}

</script>

<style scoped>
.formContainer {
  width: 1500px;
  margin-left: px;
  display: flex;
  flex-direction: row;
  padding-top: 20px;
  z-index: 1;
  justify-content: center;
}

.sampleContainer,
.geoContainer,
.samplemessageContainer,
.outbreakContainer,
.hostsourceContainer,
.testresultContainer,
.sequenceuploadContainer {
  border-width: 1px;
  border-color: black;
  border-style: solid;
  padding: 10px;
  padding-left: 5px;
}

.hostsourceContainer,
.testresultContainer {
  display: flex;
  flex-direction: row;
}

.rightContainer .el-form {
  margin-bottom: 20px;
}

.testT {
  width: 80px;
  height: 40px;
  text-align: center;
  margin-left: 20px;
  margin-top: -20px;
  background: white;
}

.geoContainer .el-form-item,
.samplemessageContainer .el-form-item,
.outbreakContainer .el-form-item {
  margin-left: 80px;
  max-width: 400px;
}

.sequenceuploadContainer .el-form-item {
  margin-left: 100px;
  width: 300px;
}

.leftContainer {
  width: 600px;
  display: flex;
  flex-direction: column;
}

.leftContainer .el-form {
  margin-bottom: 20px;
}

.rightContainer {
  display: flex;
  flex-direction: column;
}

.titleText {
  width: 80px;
  height: 40px;
  text-align: center;
  margin-left: -70px;
  margin-top: -30px;
  background: white;
}

.geoContainer .titleText {
  margin-bottom: 25px;
  /* 可以调整标题和表单项之间的间隔 */
}

.geoContainer .el-form-item {
  margin-bottom: 15px;
  /* 设置每个表单项的底部间隔 */
}

.samplemessageContainer .titleText {
  margin-bottom: 20px;
}

.samplemessageContainer .el-form-item {
  margin-bottom: 15px;
  /* 设置每个表单项的底部间隔 */
}

.outbreakContainer .titleText {
  margin-bottom: 20px;
}

.outbreakContainer .el-form-item {
  margin-bottom: 15px;
  /* 设置每个表单项的底部间隔 */
}

.rightContainer {
  margin-left: 15px;
  width: 800px;
}

.hostsourceContainer .titleText {
  margin-bottom: 220px;
}

.testresultContainer .titleText {
  margin-bottom: 170px;
}

.hostsourceLeft .el-form-item,
.testresultContainerLeft .el-form-item {
  margin-bottom: 15px;
  width: 250px;
  /* 设置每个表单项的底部间隔 */
}

.hostsourceContainer .hostsourceRight .el-form-item,
.testresultContainerRight .el-form-item {
  margin-bottom: 15px;
  /* 设置每个表单项的底部间隔 */
}

.hostsourceRight,
.testresultContainerRight {
  margin-left: 40px;
}

.sequenceupload {
  display: flex;
  flex-direction: column;
}

.test {
  width: 80px;
  height: 40px;
  text-align: center;
  margin-left: -70px;
  margin-top: -90px;
  background: white;
}

.buttonContainer {
  margin-top: 10px;
  margin-left: 400px;
}

.buttonContainer .el-button {
  margin-right: 20px;
}
</style>