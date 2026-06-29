<template>
    <el-card class="notification-manager">
        <template #header>
            <div class="card-header">
                <span>论文统计表</span>
                <el-button type="primary" @click="showAddDialog = true">新增论文统计表</el-button>
            </div>
        </template>

        <!-- 通知列表 -->
        <el-table :data="statColumn" style="width: 100%">
            <el-table-column label="序号" width="100">
                <template #default="scope">{{ scope.$index + 1 }}</template>
            </el-table-column>
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="year" label="年份"></el-table-column>
            <el-table-column prop="createTime" label="创建时间"></el-table-column>
            <el-table-column prop="updateTime" label="更新时间"></el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button :icon="Edit"  circle plain type="primary" @click="editStatColumn(scope.row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteStatColumn(scope.row.id)"
                        style="color:red;"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 新增/编辑对话框 -->
        <el-dialog :title="dialogTitle" v-model="showAddDialog" width="30%">
            <el-form :model="currentCategoryInfo" label-width="100px">
                <el-form-item label="标题">
                    <el-input v-model="currentCategoryInfo.title"></el-input>
                </el-form-item>
                <el-form-item label="年份">
                    <el-input v-model="currentCategoryInfo.year"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="showAddDialog = false">取消</el-button>
                    <el-button type="primary" @click="handleConfirm">确认</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Edit, Delete } from '@element-plus/icons-vue'
import { categoryInfoService, addCategoryInfoService,updateCategoryInfoService ,deleteCategoryInfoService} from "@/api/category"

// 模拟的通知信息数据
const statColumn = ref([]);

const getStatColumeInfp = async () => {
    let result = await categoryInfoService()
    console.log("result.data.data", result.data.data);
    statColumn.value = result.data.data
}
getStatColumeInfp()

// 当前正在编辑的通知信息
const currentCategoryInfo = ref({id:null,title:'',year:'',createTime:'',updateTime:''});


// 对话框显示状态及标题
const showAddDialog = ref(false);
const dialogTitle = ref('新增通知');

// 编辑通知
const editStatColumn = (notification) => {
    Object.assign(currentCategoryInfo.value, notification);
    dialogTitle.value = '编辑通知';
    showAddDialog.value = true;

};

// 删除通知
const deleteStatColumn = async (id) => {
    await ElMessageBox.confirm(
        '确定要删除这个统计表吗？',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(async () => {
        await deleteCategoryInfoService(id)
        statColumn.value = statColumn.value.filter(n => n.id !== id);
        ElMessage.success('删除成功');
    }).catch(() => {
        ElMessage.info('已取消删除');
    });
};

// 确认新增或编辑
const handleConfirm = async () => {
    if (!currentCategoryInfo.value.title || !currentCategoryInfo.value.year) {
        ElMessage.error('标题和内容不能为空');
        return;
    }
    if (currentCategoryInfo.value.id === null) {
        console.log("currentCategoryInfo.value", currentCategoryInfo.value);
        let result = await addCategoryInfoService(currentCategoryInfo.value)
        getStatColumeInfp()
        ElMessage.success('新增成功');
    } else {
        // 修改
        const index = statColumn.value.findIndex(n => n.id === currentCategoryInfo.value.id);
        if (index > -1) {
            await updateCategoryInfoService(currentCategoryInfo.value)
            // notifications.value[index] = { ...currentNotification };
            getStatColumeInfp()
            ElMessage.success('修改成功');
        }
    }
    // 清空表单并关闭对话框
    Object.assign(currentCategoryInfo.value, { id: null, title: '', year: '' });
    showAddDialog.value = false;
};
</script>

<style scoped>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>