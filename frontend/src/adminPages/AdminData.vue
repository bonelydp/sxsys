<template>
    <el-card class="page-container">

        <template #header>
            <div class="header">
                <span>用户管理</span>
            </div>
        </template>
        <el-form inline>
            <el-form-item label="用户搜索：">
                <el-input></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary">搜索</el-button>
                <el-button>重置</el-button>
            </el-form-item>
        </el-form>
        <el-main>
            <el-table :data="tableData" style="width: 100%" @row-click="handleRowClick">
                <el-table-column label="序号" width="120" align="center">
                    <template #default="scope">
                        {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="username" label="用户名" width="290"></el-table-column>
                <el-table-column prop="collectionTime" label="提交时间"></el-table-column>
                <el-table-column prop="status" label="状态" width="180">
                    <template #default="scope">
                        <el-tag :type="getStatusType(scope.row.status)">
                            {{ getStatusText(scope.row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="useremail" label="用户邮箱" width="180"></el-table-column> -->
                <el-table-column prop="collectionTime" label="收集时间"></el-table-column>
                <el-table-column fixed="right" label="操作" width="250">
                    <template #default="scope">
                        <div class="operation-buttons">
                            <el-button size="small" @click.stop="handleReview(scope.row)">审核</el-button>
                            <el-button size="small" type="danger" @click.stop="handleDelete(scope.row)">删除</el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[10, 20, 30, 40]"
                layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
                @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
        </el-main>


        <!-- 审核对话框 -->
        <el-dialog title="审核数据" v-model="dialogVisible" width="500px" :close-on-click-modal="false">
            <el-form :model="reviewForm" label-width="100px">
                <el-form-item label="审核结果">
                    <el-radio-group v-model="reviewForm.status">
                        
                        <el-radio label="APPROVED">通过</el-radio>
                        <el-radio label="REJECTED">不通过</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="审核意见" v-if="reviewForm.status === 'REJECTED'"
                    :rules="[{ required: true, message: '请填写审核意见', trigger: 'blur' }]">
                    <el-input v-model="reviewForm.comment" type="textarea" :rows="3"
                        placeholder="请输入审核不通过的原因"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmReview">确认</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 查看详情对话框 -->
        <el-dialog :title="`数据详情 - 样本编号: ${selectedRow.sampleNumber}`" v-model="detailsVisible" width="80%">
            <el-form :model="selectedRow" label-width="140px">
                <!-- 基本信息 -->
                <el-divider content-position="left">基本信息</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="样本编号">
                            <el-input v-model="selectedRow.sampleNumber" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="状态">
                            <el-tag :type="getStatusType(selectedRow.status)">{{ selectedRow.status }}</el-tag>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 地理位置信息 -->
                <el-divider content-position="left">地理位置信息</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="国家">
                            <el-input v-model="selectedRow.country" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="省份">
                            <el-input v-model="selectedRow.province" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="城市">
                            <el-input v-model="selectedRow.city" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="区县">
                            <el-input v-model="selectedRow.district" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="16">
                        <el-form-item label="详细地址">
                            <el-input v-model="selectedRow.addressDetails" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 样本信息 -->
                <el-divider content-position="left">样本信息</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="样本类型">
                            <el-input v-model="selectedRow.sampleType" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="采集时间">
                            <el-input v-model="selectedRow.collectionTime" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="储存条件">
                            <el-input v-model="selectedRow.storageConditions" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 暴发情况 -->
                <el-divider content-position="left">暴发情况</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="开始时间">
                            <el-input v-model="selectedRow.startTime" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="结束时间">
                            <el-input v-model="selectedRow.endTime" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="报告时间">
                            <el-input v-model="selectedRow.reportTime" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="病畜数">
                            <el-input-number v-model="selectedRow.diseaseCount" disabled></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="死亡数">
                            <el-input-number v-model="selectedRow.deathCount" disabled></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 宿主信息 -->
                <el-divider content-position="left">宿主信息</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="宿主来源">
                            <el-input v-model="selectedRow.hostSource" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="宿主具体品种">
                            <el-input v-model="selectedRow.hostSpecies" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="性别">
                            <el-input v-model="selectedRow.gender" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 健康状况 -->
                <el-divider content-position="left">健康状况</el-divider>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="是否腹泻">
                            <el-input v-model="selectedRow.diarrhea" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="腹泻类型">
                            <el-input v-model="selectedRow.diarrheaType" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="是否死亡">
                            <el-input v-model="selectedRow.death" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-form-item label="月龄/年龄">
                            <el-input v-model="selectedRow.age" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="其他症状">
                            <el-input v-model="selectedRow.otherSymptoms" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="免疫力">
                            <el-input v-model="selectedRow.immunity" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

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

                <!-- 镜检图片 -->
                <el-divider content-position="left" v-if="selectedRow.detectionMethod === '显微镜检测'">镜检图片</el-divider>
                <el-row>
                    <el-col :span="24">
                        <el-image v-if="selectedRow.jingpicture" :src="selectedRow.jingpicture"
                            :preview-src-list="[selectedRow.jingpicture]" fit="contain" style="max-width: 300px;">
                            <template #error>
                                <div class="image-slot">暂无图片</div>
                            </template>
                        </el-image>
                        <div v-else>暂无图片</div>
                    </el-col>
                </el-row>

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

            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="detailsVisible = false">关闭</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
    getAllSamplesService,
    approveSampleService,
    rejectSampleService,
    deleteSampleService,
    getSampleDetailService
} from '@/api/dataSubmit'

const loading = ref(false)
// 状态变量
const searchQuery = ref('');
const tableData = ref([
    {
        id: 1,
        sampleNumber: "1",
        username: 'user1',
        time: '2015年1月15日',
        uploadTime: '2023-01-01 10:00:00',
        status: '待审核',
        country: "中国",
        province: "广东省", city: "广州市", district: "天河区", addressDetails: "华南农业大学", sampleType: "粪便",
        otherSampleType: "", collectionTime: "2025年1月9日", storageConditions: "2.5%重铬酸钾", sender: "雄性",
        startTime: "2025年1月1日", endTime: "2025年1月9日", reportTime: "2025年1月9日", diseaseCount: "15", deathCount: "15", hostSource: "奶牛",
        diarrhea: "是", diarrheaType: "中度腹泻", death: "是", hostSpecies: "奶牛", age: "8个月", otherSymptoms: "无", immunity: "正常",
        microscopyResult: "阳性", ssuRnaPcr: "阳性", gp60Pcr: "阳性", qpcrCtValue: "18", insectspecies: "C.parvum",
        subtype: "亚型", gender: "雄性"
    },
    {
        id: 2,
        sampleNumber: "2",
        username: 'user2',
        time: '2015年1月15日',
        uploadTime: '2023-01-01 10:00:00',
        status: '待审核',
        country: "中国",
        province: "广东省", city: "广州市", district: "天河区", addressDetails: "华南农业大学", sampleType: "粪便",
        otherSampleType: "", collectionTime: "2025年1月9日", storageConditions: "2.5%重铬酸钾", sender: "雄性",
        startTime: "2025年1月1日", endTime: "2025年1月9日", reportTime: "2025年1月9日", diseaseCount: "15", deathCount: "15", hostSource: "奶牛",
        diarrhea: "是", diarrheaType: "中度腹泻", death: "是", hostSpecies: "奶牛", age: "8个月", otherSymptoms: "无", immunity: "正常",
        microscopyResult: "阳性", ssuRnaPcr: "阳性", gp60Pcr: "阳性", qpcrCtValue: "18", insectspecies: "C.parvum",
        subtype: "亚型", gender: "雄性"
    }
]);
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(1)//总条数
const pageSize = ref(10);
const currentPage = ref(1);

const dialogVisible = ref(false);
const detailsVisible = ref(false);
let currentRow = null;
let selectedRow = ref({});


// 审核表单数据
const reviewForm = ref({
    status: 'APPROVED',
    comment: ''
})

// 获取数据函数（这里简化为静态数据）
const fetchData = async () => {
    loading.value = true
    try {
        let params = {
            status : null,
            page : pageNum.value,
            size : pageSize.value
        }
        const response = await getAllSamplesService(params)
        console.log("获取数据情况：", response);
        console.log(response.data.total);
        if (response.data.total > 0) {
            tableData.value = response.data.data
            total.value = response.data.total
        } else {
            throw new Error(response.data.message || '获取数据失败')
        }
    } catch (error) {
        console.error('获取数据失败:', error)
        ElMessage.error('获取数据失败：' + (error.response?.data?.message || error.message))
    } finally {
        loading.value = false
    }
}

// 行点击事件处理
const handleRowClick = (row) => {
    selectedRow.value = row;
    detailsVisible.value = true;
};

// 获取状态标签类型
const getStatusType = (status) => {
    const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
    }
    return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
    const statusMap = {
        'PENDING': '未审核',
        'APPROVED': '已通过',
        'REJECTED': '已拒绝'
    }
    return statusMap[status] || status
}

// 审核处理函数
const handleReview = (row) => {
    currentRow = row
    reviewForm.value.status = 'APPROVED'
    reviewForm.value.comment = ''
    dialogVisible.value = true
}

// 确认审核
const confirmReview = async () => {
    if (reviewForm.value.status === 'REJECTED' && !reviewForm.value.comment) {
        ElMessage.warning('请填写审核不通过的原因')
        return
    }

    try {
        let response
        if (reviewForm.value.status === 'APPROVED') {
            response = await approveSampleService(currentRow.id)
        } else {
            response = await rejectSampleService(currentRow.id)
        }
        console.log("response审核", response);
        if (response.status === 200) {
            ElMessage.success('审核操作成功')
            dialogVisible.value = false
            // 刷新数据列表
            fetchData()
        } else {
            throw new Error(response.data.message || '审核失败')
        }
    } catch (error) {
        console.error('审核失败:', error)
        ElMessage.error('审核失败：' + (error.response?.data?.message || error.message))
    }
}

// 删除处理函数
const handleDelete = (row) => {
    ElMessageBox.confirm(
        '确定要删除这条数据吗？此操作不可恢复',
        '警告',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(async () => {
        try {
            const response = await deleteSampleService(row.id)
            if (response.status === 200) {
                ElMessage.success('删除成功')
                fetchData() // 刷新数据列表
            } else {
                throw new Error(response.data.message || '删除失败')
            }
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败：' + (error.response?.data?.message || error.message))
        }
    }).catch(() => {
        ElMessage.info('已取消删除')
    })
}

// 分页变化处理函数
const handlePageChange = (newPage) => {
    currentPage.value = newPage;
    fetchData();
};

// 组件挂载时获取初始数据
onMounted(() => {
    fetchData();
});

// 分页大小改变
const onSizeChange = (val) => {
    pageSize.value = val
    fetchData()
}

// 当前页改变
const onCurrentChange = (val) => {
    pageNum.value = val
    fetchData()
}


import { Download, Document } from '@element-plus/icons-vue'
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
</script>

<style scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;
}

.el-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}


/* 添加悬停效果到每一行 */
.el-table__row:hover .operation-buttons {
    visibility: visible !important;
}

.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
}

:deep(.el-divider__text) {
    font-weight: bold;
    color: #409EFF;
}

.el-tag {
    font-weight: bold;
}

.review-loading {
    margin-right: 5px;
}

/* 添加审核按钮样式 */
.operation-buttons .el-button {
    margin-right: 10px;
}

/* 状态标签样式 */
.el-tag.el-tag--warning {
    background-color: #fdf6ec;
    border-color: #faecd8;
    color: #e6a23c;
}

.el-tag.el-tag--success {
    background-color: #f0f9eb;
    border-color: #e1f3d8;
    color: #67c23a;
}

.el-tag.el-tag--danger {
    background-color: #fef0f0;
    border-color: #fde2e2;
    color: #f56c6c;
}


.no-file {
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

.no-file .el-icon {
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