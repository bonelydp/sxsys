                <!-- 检测信息 -->
                <el-divider content-position="left">检测信息</el-divider>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="检测方法">
                            <el-input v-model="selectedRow.detectionMethod" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="selectedRow.detectionMethod === 'SSU rRNA PCR'">
                        <el-form-item label="虫种">
                            <el-input v-model="selectedRow.insectSpecies" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="selectedRow.detectionMethod === 'gp60 PCR'">
                        <el-form-item label="亚型">
                            <el-input v-model="selectedRow.subtype" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="selectedRow.detectionMethod === '其他'">
                        <el-form-item label="其他检测方法">
                            <el-input v-model="selectedRow.detectionMethodnew" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 镜检图片，仅在显微镜检测时显示 -->
                <template v-if="selectedRow.detectionMethod === '显微镜检测'">
                    <el-divider content-position="left">镜检图片</el-divider>
                    <el-row>
                        <el-col :span="24">
                            <el-image 
                                v-if="selectedRow.jingpicture"
                                :src="selectedRow.jingpicture"
                                :preview-src-list="[selectedRow.jingpicture]"
                                fit="contain"
                                style="max-width: 300px; max-height: 300px;"
                            >
                                <template #error>
                                    <div class="image-slot">
                                        <el-icon><Picture /></el-icon>
                                        <span>暂无图片</span>
                                    </div>
                                </template>
                            </el-image>
                            <div v-else class="no-image">
                                <el-icon><Picture /></el-icon>
                                <span>暂无图片</span>
                            </div>
                        </el-col>
                    </el-row>
                </template>

                <!-- 序列文件 -->
                <el-divider content-position="left">序列文件</el-divider>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="标准序列类型">
                            <el-input v-model="selectedRow.selectedStandardSequence" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="序列文件">
                            <div v-if="selectedRow.file" class="file-download">
                                <el-button 
                                    type="primary" 
                                    link 
                                    @click="handleFileDownload(selectedRow.file)"
                                >
                                    <el-icon><Download /></el-icon>
                                    下载序列文件
                                </el-button>
                            </div>
                            <div v-else class="no-file">
                                <el-icon><Document /></el-icon>
                                <span>暂无文件</span>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 修改检测信息部分 -->
                <el-divider content-position="left">检测信息</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="检测方法">
                            <el-input v-model="selectedRow.detectionMethod" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" v-if="selectedRow.detectionMethod === '显微镜检测'">
                        <el-form-item label="显微镜检查结果">
                            <el-input v-model="selectedRow.microscopyResult" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" v-if="selectedRow.detectionMethod === 'SSU rRNA PCR'">
                        <el-form-item label="虫种">
                            <el-input v-model="selectedRow.insectSpecies" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" v-if="selectedRow.detectionMethod === 'gp60 PCR'">
                        <el-form-item label="亚型">
                            <el-input v-model="selectedRow.subtype" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 镜检图片 -->
                <template v-if="selectedRow.detectionMethod === '显微镜检测'">
                    <el-divider content-position="left">镜检图片</el-divider>
                    <el-row>
                        <el-col :span="24">
                            <el-image 
                                v-if="selectedRow.jingpicture"
                                :src="selectedRow.jingpicture"
                                :preview-src-list="[selectedRow.jingpicture]"
                                fit="contain"
                                style="max-width: 300px; max-height: 300px;"
                            >
                                <template #error>
                                    <div class="image-slot">
                                        <el-icon><Picture /></el-icon>
                                        <span>暂无图片</span>
                                    </div>
                                </template>
                            </el-image>
                            <div v-else class="no-image">
                                <el-icon><Picture /></el-icon>
                                <span>暂无图片</span>
                            </div>
                        </el-col>
                    </el-row>
                </template>

                <!-- 序列文件 -->
                <el-divider content-position="left">序列文件</el-divider>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="标准序列类型">
                            <el-input v-model="selectedRow.selectedStandardSequence" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="序列文件">
                            <div v-if="selectedRow.file" class="file-download">
                                <el-button 
                                    type="primary" 
                                    link 
                                    @click="handleFileDownload(selectedRow.file)"
                                >
                                    <el-icon><Download /></el-icon>
                                    下载序列文件
                                </el-button>
                            </div>
                            <div v-else class="no-file">
                                <el-icon><Document /></el-icon>
                                <span>暂无文件</span>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

<style scoped>
.image-slot {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 300px;
    height: 200px;
    background: #f5f7fa;
    color: #909399;
}

.no-image, .no-file {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 300px;
    height: 100px;
    background: #f5f7fa;
    color: #909399;
    border-radius: 4px;
}

.no-image .el-icon, .no-file .el-icon {
    font-size: 32px;
    margin-bottom: 8px;
}

.file-download {
    display: flex;
    align-items: center;
}

.file-download .el-icon {
    margin-right: 4px;
}
</style> 

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Download, Document, Picture } from '@element-plus/icons-vue'
import { 
  getAllSamplesService, 
  approveSampleService, 
  rejectSampleService, 
  deleteSampleService,
  getSampleDetailService
} from '@/api/dataSubmit'

// 文件下载处理函数
const handleFileDownload = (filePath) => {
    if (!filePath) {
        ElMessage.warning('文件路径不存在')
        return
    }
    
    // 创建一个隐藏的a标签来下载文件
    const link = document.createElement('a')
    link.href = filePath
    link.setAttribute('download', '') // 让浏览器自动获取文件名
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}

// 获取状态文本
const getStatusText = (status) => {
    const statusMap = {
        'PENDING': '未审核',
        'APPROVED': '通过',
        'REJECTED': '拒绝'
    }
    return statusMap[status] || status
}
</script> 