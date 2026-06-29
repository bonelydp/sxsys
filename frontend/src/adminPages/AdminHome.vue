<template>
  <el-card class="notification-manager">
    <template #header>
      <div class="card-header">
        <span>通知信息管理</span>
        <el-button type="primary" @click="showAddDialog = true">新增通知</el-button>
      </div>
    </template>

    <!-- 通知列表 -->
    <el-table :data="notifications" style="width: 100%">
      <el-table-column label="序号" width="100">
        <template #default="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="date" label="发布时间"></el-table-column>
      <el-table-column fixed="right" label="操作" width="180">
        <template #default="scope">
          <el-button :icon="Edit" circle plain type="primary" @click="editNotification(scope.row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteNotification(scope.row.id)"
            style="color:red;"></el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="showAddDialog" width="30%">
      <el-form :model="currentNotification" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="currentNotification.title"></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload class="avatar-uploader" 
          :auto-upload="true" 
          :show-file-list="false"
          action="/api/files/upload"
          :headers="{'Authorization':tokenStore.token}"
          :on-success="uploadSuccess"
          >
            <img :headers="{'Authorization':tokenStore.token}" v-if="currentNotification.coverImg" :src="currentNotification.coverImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="文章内容">
          <div class="editor">
            <quill-editor theme="snow" v-model:content="currentNotification.content" contentType="html">
            </quill-editor></div>
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
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Edit, Delete,Plus } from '@element-plus/icons-vue'
import { informationService, updateInformationService, addInformationService, deleteInformationService } from "@/api/information"
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { useTokenStore } from '@/store/mytoken';

const tokenStore = useTokenStore()

const uploadSuccess= (result)=>{
  console.log("result图片",result);
  currentNotification.value.coverImg = result.data
}
// 模拟的通知信息数据
const notifications = ref([
]);

const getInformationInfo = async () => {
  let result = await informationService()
  console.log("result.data.data", result.data.data);
  notifications.value = result.data.data
}
getInformationInfo()

// 当前正在编辑的通知信息
const currentNotification = ref({ id: null, title: '', content: '', date: '' ,coverImg:''});

// 对话框显示状态及标题
const showAddDialog = ref(false);
const dialogTitle = ref('新增通知');

// 编辑通知
const editNotification = (notification) => {
  Object.assign(currentNotification.value, notification);
  dialogTitle.value = '编辑通知';
  showAddDialog.value = true;

};

// 删除通知
const deleteNotification = async (id) => {
  await ElMessageBox.confirm(
    '确定要删除这条通知吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async() => {
    console.log("id", id)
    let result = await deleteInformationService(id)
    getInformationInfo()
    ElMessage.success('删除成功');
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};

// 确认新增或编辑
const handleConfirm = async () => {
  if (!currentNotification.value.title || !currentNotification.value.content) {
    ElMessage.error('标题和内容不能为空');
    return;
  }

  if (currentNotification.value.id === null) {
    Object.assign(currentNotification, { id: null, title: '', content: '', date: '' ,coverImg:'' });
    // 新增
    notifications.value.push({
      title: currentNotification.value.title,
      content: currentNotification.value.content
    });
    console.log("currentNotification.value", currentNotification.value)
    let result = await addInformationService(currentNotification.value)
    console.log("result", result)
    getInformationInfo()
    ElMessage.success('新增成功');
  } else {
    // 修改
    const index = notifications.value.findIndex(n => n.id === currentNotification.value.id);
    if (index > -1) {
      let result = await updateInformationService(currentNotification.value)
      // notifications.value[index] = { ...currentNotification };
      getInformationInfo()
      ElMessage.success('修改成功');
    }
  }
  // 清空表单并关闭对话框
  Object.assign(currentNotification.value, { id: null, title: '', content: '', date: '' ,coverImg:'' });
  showAddDialog.value = false;
};
</script>

<style scoped lang="scss">
.avatar-uploader {
  :deep() {
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      text-align: center;
    }
  }
}

.editor {
  width: 100%;

  :deep(.ql-editor) {
    min-height: 200px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
