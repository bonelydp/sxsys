<template>
  <div class="data-visualization-manage">
    <h2>数据可视化管理</h2>
    
    <!-- 搜索和操作栏 -->
    <div class="operation-bar">
      <div class="search-bar">
        <span>数据搜索：</span>
        <el-input
          v-model="searchQuery"
          placeholder="请输入数据编号"
          style="width: 200px"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      <div class="action-buttons">
        <el-button type="success" @click="downloadTemplate">
          <el-icon><Download /></el-icon>下载模板
        </el-button>
        <el-button type="primary" @click="showBatchUpload">
          <el-icon><Upload /></el-icon>批量导入
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table 
      :data="tableData" 
      
      v-loading="loading"
      @row-click="showDetails"
      class="custom-table"
    >
      <el-table-column 
      
        label="序号" 
        width="140" 
        fixed="left"
        
      >
      <template #default="scope">
          {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
      </template>
      </el-table-column>
      <el-table-column prop="sampleId" label="样本编号" width="200" fixed="left" />
      <el-table-column prop="province" label="省份" width="200" />
      <el-table-column prop="city" label="城市" width="200" />
      <el-table-column prop="sampleType" label="样本类型" width="200" />
      <el-table-column prop="collectionYear" label="采集年份" width="200" />
      <el-table-column prop="hostSource" label="宿主来源" width="200" />
      

      
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="scope">
          <el-button
            type="danger"
            circle
            size="small"
            
            @click.stop="handleDelete(scope.row)"
          >
            <el-icon><Delete /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        background
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 数据详情对话框 -->
    <el-dialog
      v-model="detailsVisible"
      title="数据详情"
      width="70%"
    >
      <div class="data-details" v-if="currentData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="样本编号">{{ currentData.sampleId }}</el-descriptions-item>
          <el-descriptions-item label="省份">{{ currentData.province }}</el-descriptions-item>
          <el-descriptions-item label="城市">{{ currentData.city }}</el-descriptions-item>
          <el-descriptions-item label="样本类型">{{ currentData.sampleType }}</el-descriptions-item>
          <el-descriptions-item label="采集年份">{{ currentData.collectionYear }}</el-descriptions-item>
          <el-descriptions-item label="宿主来源">{{ currentData.hostSource }}</el-descriptions-item>
          <el-descriptions-item label="是否暴发">{{ currentData.outbreak ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="是否腹泻">{{ currentData.diarrhea ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="检测方法">{{ currentData.detectionMethod }}</el-descriptions-item>
          <el-descriptions-item label="年龄范围">{{ currentData.ageRangeStart }}-{{ currentData.ageRangeEnd }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">感染情况统计</el-divider>
        
        <el-descriptions :column="3" border>
          <el-descriptions-item label="总样本数">{{ currentData.cTotalSamples }}</el-descriptions-item>
          <el-descriptions-item label="阳性病例">{{ currentData.cPositiveCases }}</el-descriptions-item>
          <el-descriptions-item label="感染率">{{ currentData.cInfectionRate.toFixed(2) }}%</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">各型感染详情</el-divider>

        <el-table :data="getSpeciesData()" border style="width: 100%">
          <el-table-column prop="name" label="隐孢子虫类型" width="180" />
          <el-table-column prop="cases" label="阳性病例" width="120" />
          <el-table-column prop="rate" label="感染率">
            <template #default="scope">
              {{ scope.row.rate ? scope.row.rate.toFixed(2) + '%' : '-' }}
            </template>
          </el-table-column>
        </el-table>

        <el-divider content-position="left">其他信息</el-divider>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="混合感染数">{{ currentData.mixedInfectionCases || 0 }}</el-descriptions-item>
          <el-descriptions-item label="其他类型阳性病例">{{ currentData.otherCPositiveCases || 0 }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailsVisible = false">关闭</el-button>
          <el-button type="primary" @click="handleEdit(currentData)">编辑</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 批量上传对话框 -->
    <el-dialog
      v-model="batchUploadVisible"
      title="批量导入数据"
      width="500px"
    >
      <div class="upload-content">
        <el-alert
          title="请先下载模板，按照模板格式填写数据后再进行上传"
          type="info"
          :closable="false"
          style="margin-bottom: 20px"
        />
        
        <el-upload
          class="upload-demo"
          drag
          :action="null"
          :auto-upload="false"
          :multiple="true"
          :on-change="handleFileChange"
          :before-upload="beforeUpload"
          accept=".xlsx"
          :limit="5"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只支持 xlsx 格式的Excel文件，可同时上传多个文件
            </div>
          </template>
        </el-upload>

        <!-- 上传按钮 -->
        <div class="upload-actions" v-if="selectedFiles.length > 0">
          <el-button 
            type="primary" 
            @click="submitUpload"
            :loading="uploading"
          >
            开始上传
          </el-button>
        </div>

        <!-- 上传进度 -->
        <div v-if="uploadProgress > 0" class="upload-progress">
          <el-progress :percentage="uploadProgress" />
        </div>

        <!-- 上传结果 -->
        <div v-if="uploadResults.length > 0" class="upload-results">
          <h4>导入结果：</h4>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="成功数量">
              {{ uploadResults.filter(r => r.status === 'success').length }}
            </el-descriptions-item>
            <el-descriptions-item label="失败数量">
              {{ uploadResults.filter(r => r.status === 'error').length }}
            </el-descriptions-item>
          </el-descriptions>

          <!-- 错误详情 -->
          <div v-if="uploadResults.some(r => r.status === 'error')" class="error-details">
            <h5>错误详情：</h5>
            <el-table :data="uploadResults.filter(r => r.status === 'error')" size="small">
              <el-table-column prop="row" label="行号" width="80" />
              <el-table-column prop="message" label="错误原因" />
            </el-table>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="batchUploadVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑数据"
      width="80%"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="formRules"
        label-width="140px"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="样本编号" prop="sampleId">
              <el-input v-model="editForm.sampleId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="省份" prop="province">
              <el-input v-model="editForm.province" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="城市" prop="city">
              <el-input v-model="editForm.city" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="样本类型" prop="sampleType">
              <el-input v-model="editForm.sampleType" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="采集年份" prop="collectionYear">
              <el-input-number 
                v-model="editForm.collectionYear" 
                :min="1900" 
                :max="2100" 
                controls-position="right"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="宿主来源" prop="hostSource">
              <el-input v-model="editForm.hostSource" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 状态信息 -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="是否暴发" prop="outbreak">
              <el-switch
                v-model="editForm.outbreak"
                active-text="是"
                inactive-text="否"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否腹泻" prop="diarrhea">
              <el-switch
                v-model="editForm.diarrhea"
                active-text="是"
                inactive-text="否"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="检测方法" prop="detectionMethod">
              <el-input v-model="editForm.detectionMethod" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 年龄范围 -->
        <el-divider content-position="left">年龄范围</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="起始年龄" prop="ageRangeStart">
              <el-input-number 
                v-model="editForm.ageRangeStart" 
                :min="0" 
                controls-position="right"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束年龄" prop="ageRangeEnd">
              <el-input-number 
                v-model="editForm.ageRangeEnd" 
                :min="0" 
                controls-position="right"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 总体感染情况 -->
        <el-divider content-position="left">总体感染情况</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="总样本数" prop="cTotalSamples">
              <el-input-number 
                v-model="editForm.cTotalSamples" 
                :min="0" 
                controls-position="right"
                @change="calculateTotalRate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="阳性病例" prop="cPositiveCases">
              <el-input-number 
                v-model="editForm.cPositiveCases" 
                :min="0" 
                :max="editForm.cTotalSamples"
                controls-position="right"
                @change="calculateTotalRate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="感染率(%)" prop="cInfectionRate">
              <el-input-number 
                v-model="editForm.cInfectionRate" 
                :min="0" 
                :max="100" 
                :precision="2"
                controls-position="right"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 各型隐孢子虫数据 -->
        <el-divider content-position="left">各型隐孢子虫数据</el-divider>
        <el-row :gutter="20">
          <template v-for="item in cryptoTypes" :key="item.label">
            <el-col :span="12">
              <el-form-item :label="item.label">
                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-input-number 
                      v-model="editForm[item.casesField]" 
                      :min="0"
                      :max="editForm.cTotalSamples"
                      controls-position="right"
                      placeholder="阳性病例"
                      @change="() => calculateSpecificRate(item)"
                    />
                  </el-col>
                  <el-col :span="12">
                    <el-input-number 
                      v-model="editForm[item.rateField]" 
                      :min="0"
                      :max="100"
                      :precision="2"
                      controls-position="right"
                      placeholder="感染率(%)"
                      disabled
                    />
                  </el-col>
                </el-row>
              </el-form-item>
            </el-col>
          </template>
        </el-row>

        <!-- 其他感染情况 -->
        <el-divider content-position="left">其他感染情况</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="混合感染数" prop="mixedInfectionCases">
              <el-input-number 
                v-model="editForm.mixedInfectionCases" 
                :min="0"
                :max="editForm.cPositiveCases"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他隐孢子虫" prop="otherCPositiveCases">
              <el-input-number 
                v-model="editForm.otherCPositiveCases" 
                :min="0"
                :max="editForm.cTotalSamples"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, View, Download, Upload, UploadFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { downloadTemplateService, batchImportService, getAllSampleInfoService, deleteSampleInfoService, updateSampleInfoService, addSampleInfoService } from '@/api/visualization'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchQuery = ref('')
const detailsVisible = ref(false)
const currentData = ref(null)

// 批量上传相关
const batchUploadVisible = ref(false)
const selectedFiles = ref([])
const uploading = ref(false)
const uploadProgress = ref(0)
const uploadResults = ref([])

// 编辑对话框相关
const editDialogVisible = ref(false)
const editForm = ref({})
const formRules = {
  province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  sampleType: [{ required: true, message: '请输入样本类型', trigger: 'blur' }],
  collectionYear: [{ required: true, message: '请输入采集年份', trigger: 'blur' }],
  hostSource: [{ required: true, message: '请输入宿主来源', trigger: 'blur' }],
  cTotalSamples: [{ required: true, message: '请输入总样本数', trigger: 'blur' }],
  cPositiveCases: [{ required: true, message: '请输入阳性病例数', trigger: 'blur' }]
}

const cryptoTypes = [
  { label: '微小隐孢子虫', casesField: 'cParvumPositiveCases', rateField: 'cParvumInfectionRate' },
  { label: '牛隐孢子虫', casesField: 'cBovisPositiveCases', rateField: 'cBovisInfectionRate' },
  { label: '芮氏隐孢子虫', casesField: 'cHominisPositiveCases', rateField: 'cHominisInfectionRate' },
  { label: '安氏隐孢子虫', casesField: 'cAnPositiveCases', rateField: 'cAnInfectionRate' },
  { label: '肖氏隐孢子虫', casesField: 'cXiaoPositiveCases', rateField: 'cXiaoInfectionRate' },
  { label: '泛在隐孢子虫', casesField: 'cUbiquitumPositiveCases', rateField: 'cUbiquitumInfectionRate' }
]

// 获取数据列表
const fetchData = async () => {
  loading.value = true
  try {
    let params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
  }
    const response = await getAllSampleInfoService(params)
    console.log("response",response);
    if (response.data.code === 0) {
      await nextTick(() => {
        tableData.value = response.data.data.content
        total.value = response.data.data.totalElements // 或者从后端获取总数
      })
    } else {
      ElMessage.error(response.data.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败：' + (error.response?.data?.message || '服务器错误'))
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  searchQuery.value = ''
  currentPage.value = 1
  fetchData()
}

// 显示数据详情
const showDetails = (row) => {
  currentData.value = row
  detailsVisible.value = true
}

// 删除数据
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除这条数据吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const response = await deleteSampleInfoService(row.sampleId)
      if (response.data.code === 0) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(response.data.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败：' + (error.response?.data?.message || '服务器错误'))
    }
  })
}

// 编辑数据
const handleEdit = (data) => {
  editForm.value = { ...data }
  editDialogVisible.value = true
}

// 处理每页显示数量变化
const handleSizeChange = async (val) => {
  pageSize.value = val
  
  await nextTick()
  fetchData()
}

// 处理页码变化
const handleCurrentChange = async (val) => {
  currentPage.value = val
  await nextTick()
  fetchData()
}

// 初始化
onMounted(async () => {
  await nextTick()
  fetchData()
})

// 下载模板

const downloadTemplate = () => {
  // 文件路径是相对于 public 的
  const filePath = '/resources/sampledata.xlsx'
  
  // 创建一个隐藏的 a 标签来触发下载
  const link = document.createElement('a')
  link.href = filePath
  link.download = '数据导入模板.xlsx'  // 指定下载后的文件名
  link.click()
}



// 文件选择处理
const handleFileChange = (file, fileList) => {
  selectedFiles.value = fileList
}

// 上传前验证
const beforeUpload = (file) => {
  const isXlsx = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  const isLt30M = file.size / 1024 / 1024 < 30

  if (!isXlsx) {
    ElMessage.error('只能上传 xlsx 格式的Excel文件!')
    return false
  }
  if (!isLt30M) {
    ElMessage.error('文件大小不能超过30MB!')
    return false
  }

  return true
}

// 提交上传
const submitUpload = async () => {
  if (selectedFiles.value.length === 0) {
    ElMessage.warning('请先选择文件')
    return
  }

  uploading.value = true
  uploadProgress.value = 0

  try {
    // 获取所有文件的原始文件对象
    const files = selectedFiles.value.map(file => file.raw)
    const response = await batchImportService(files)
    console.log("response",response);
    
      ElMessage.success('数据导入成功')
      // 清空文件列表
      selectedFiles.value = []
      // 关闭对话框并刷新数据
      setTimeout(() => {
        batchUploadVisible.value = false
        fetchData()
      }, 1500)
    
      
    
  } catch (error) {
    ElMessage.error('上传失败：' + (error.response?.data?.message || '未知错误'))
  } finally {
    uploading.value = false
    uploadProgress.value = 100
  }
}

// 显示批量上传对话框
const showBatchUpload = () => {
  batchUploadVisible.value = true
  selectedFiles.value = []
  uploadProgress.value = 0
  uploading.value = false
}

// 获取各型隐孢子虫数据
const getSpeciesData = () => {
  if (!currentData.value) return []
  
  return [
    {
      name: '微小隐孢子虫',
      cases: currentData.value.cParvumPositiveCases,
      rate: currentData.value.cParvumInfectionRate
    },
    {
      name: '牛隐孢子虫',
      cases: currentData.value.cBovisPositiveCases,
      rate: currentData.value.cBovisInfectionRate
    },
    {
      name: '芮氏隐孢子虫',
      cases: currentData.value.cHominisPositiveCases,
      rate: currentData.value.cHominisInfectionRate
    },
    {
      name: '安氏隐孢子虫',
      cases: currentData.value.cAnPositiveCases,
      rate: currentData.value.cAnInfectionRate
    },
    {
      name: '肖氏隐孢子虫',
      cases: currentData.value.cXiaoPositiveCases,
      rate: currentData.value.cXiaoInfectionRate
    },
    {
      name: '泛在隐孢子虫',
      cases: currentData.value.cUbiquitumPositiveCases,
      rate: currentData.value.cUbiquitumInfectionRate
    }
  ]
}

// 计算序号的方法（考虑分页）
const calculateIndex = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

// 计算总感染率
const calculateTotalRate = () => {
  if (editForm.value.cTotalSamples && editForm.value.cPositiveCases) {
    editForm.value.cInfectionRate = (editForm.value.cPositiveCases / editForm.value.cTotalSamples * 100).toFixed(2)
  }
}

// 计算特定类型的感染率
const calculateSpecificRate = (item) => {
  if (editForm.value.cTotalSamples && editForm.value[item.casesField]) {
    editForm.value[item.rateField] = (editForm.value[item.casesField] / editForm.value.cTotalSamples * 100).toFixed(2)
  }
}

// 提交编辑
const submitEdit = async () => {
  if (!editFormRef.value) return
  
  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 构建更新DTO对象
        const updateDTO = {
          sampleId: editForm.value.sampleId,
          province: editForm.value.province,
          city: editForm.value.city,
          sampleType: editForm.value.sampleType,
          collectionYear: editForm.value.collectionYear,
          hostSource: editForm.value.hostSource,
          outbreak: editForm.value.outbreak,
          diarrhea: editForm.value.diarrhea,
          detectionMethod: editForm.value.detectionMethod,
          ageRangeStart: editForm.value.ageRangeStart,
          ageRangeEnd: editForm.value.ageRangeEnd,
          cTotalSamples: editForm.value.cTotalSamples,
          cPositiveCases: editForm.value.cPositiveCases,
          cInfectionRate: editForm.value.cInfectionRate,
          // 各型隐孢子虫数据
          cParvumPositiveCases: editForm.value.cParvumPositiveCases,
          cParvumInfectionRate: editForm.value.cParvumInfectionRate,
          cBovisPositiveCases: editForm.value.cBovisPositiveCases,
          cBovisInfectionRate: editForm.value.cBovisInfectionRate,
          cHominisPositiveCases: editForm.value.cHominisPositiveCases,
          cHominisInfectionRate: editForm.value.cHominisInfectionRate,
          cAnPositiveCases: editForm.value.cAnPositiveCases,
          cAnInfectionRate: editForm.value.cAnInfectionRate,
          cXiaoPositiveCases: editForm.value.cXiaoPositiveCases,
          cXiaoInfectionRate: editForm.value.cXiaoInfectionRate,
          cUbiquitumPositiveCases: editForm.value.cUbiquitumPositiveCases,
          cUbiquitumInfectionRate: editForm.value.cUbiquitumInfectionRate,
          // 其他感染情况
          mixedInfectionCases: editForm.value.mixedInfectionCases,
          otherCPositiveCases: editForm.value.otherCPositiveCases
        }

        const response = await updateSampleInfoService(updateDTO)
        if (response.data.code === 0) {
          ElMessage.success('数据编辑成功')
          editDialogVisible.value = false
          fetchData() // 刷新数据列表
        } else {
          ElMessage.error(response.data.message || '编辑失败')
        }
      } catch (error) {
        console.error('编辑失败:', error)
        ElMessage.error('编辑失败：' + (error.response?.data?.message || '服务器错误'))
      }
    } else {
      ElMessage.warning('请填写必要的字段')
      return false
    }
  })
}

// 可以添加表单验证
const editFormRef = ref(null)
</script>

<style scoped>
.data-visualization-manage {
  padding: 20px;
  box-sizing: border-box;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  padding: 10px;
}

.data-details {
  padding: 20px;
}

.data-preview {
  margin-top: 10px;
  min-height: 200px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
}

.el-table {
  margin-top: 20px;
}

:deep(.el-descriptions__label) {
  width: 120px;
}

.upload-content {
  padding: 20px;
}

.upload-progress {
  margin-top: 20px;
}

.upload-results {
  margin-top: 20px;
}

.error-details {
  margin-top: 20px;
}

.error-details h5 {
  margin-bottom: 10px;
  color: #f56c6c;
}

:deep(.el-upload-dragger) {
  width: 100%;
}

:deep(.el-upload) {
  width: 100%;
}

.upload-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-descriptions) {
  margin-bottom: 20px;
}

:deep(.el-divider__text) {
  font-weight: bold;
  color: #409EFF;
}

/* 确保表格可以横向滚动 */
.custom-table {
  margin-left: 20px;
  margin-top: 30px;
  overflow-x: auto;
  
}

:deep(.el-table) {
  width: auto !important;
  min-width: 100%;
}

.el-dialog {
  max-height: 90vh;
  overflow-y: auto;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-input-number) {
  width: 100%;
}

.el-divider {
  margin: 20px 0;
}

:deep(.el-pagination) {
  justify-content: flex-end;
  margin: 20px 0;
}

/* 确保分页组件有足够的空间 */
:deep(.el-pagination .el-select .el-input) {
  margin: 0;
  width: 100px;
}
</style> 