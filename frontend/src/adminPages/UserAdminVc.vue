<script setup>
import {
  Edit,
  Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(1)//总条数
const pageSize = ref(10)//每页条数

//文章列表数据显示
import { usersInfoService,deleteUserInfoService} from '@/api/users';
import { ElMessage,ElMessageBox } from 'element-plus'

const users = ref([])

const getUserInfo = async () => {
  let params = {
    pageNum:pageNum.value,
    pageSize:pageSize.value,
  }
  let result = await usersInfoService(params)
  users.value = result.data.data.items
  total.value = result.data.data.total

}
getUserInfo()


// //添加文章
// import { Plus } from '@element-plus/icons-vue'
// import { ElMessage, ElMessageBox } from 'element-plus';
// //控制抽屉是否显示
// const visibleDrawer = ref(false)
// //添加表单数据模型
// const articleModel = ref({
//   title: '',
//   categoryId: '',
//   author: '',
//   volume: '',
//   publishTime: '',
//   jcr: '',
//   issn: ''
// })

// const addArticle = async () => {
//   //调用接口
//   let result = await addArticleService(articleModel.value)
//   ElMessage.success(result.msg ? result.msg : '添加成功')
//   //让抽屉消失
//   visibleDrawer.value = !visibleDrawer.value
//   articleModel.value = ref({})
//   getArticleInfo()
// }

//删除用户
const deleteUser = async (row) => {
  ElMessageBox.confirm(
    '您确认删除吗？',
    '温馨提示',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      let result = await deleteUserInfoService(row.id)
      ElMessage({
        type: 'success',
        message: '删除成功',
      })
      getUserInfo()
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除'
      })
    })
    
}

// //修改文章信息
// const dialogVisible = ref(false)
// const articleNew = ref({})
// const updateArticle = async () => {
//   let result = await updateArticleService(articleNew.value)
//   ElMessage.success(result.msg ? result.msg : "修改成功")
//   getArticleInfo()
//   dialogVisible.value = !dialogVisible.value
// }

// const editArticle = (row) => { 
//   dialogVisible.value = !dialogVisible.value
//   articleNew.value = row
// }



//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
  pageSize.value = size
  getUserInfo()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
  pageNum.value = num
  getUserInfo()
}
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>用户管理</span>
      </div>
    </template>
    <!-- 搜索表单 -->
    <el-form inline>
      <el-form-item label="用户搜索：">
        <el-input></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary">搜索</el-button>
        <el-button>重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 文章列表 -->
    <el-table :data="users" style="width: 100%">
      <el-table-column label="序号" width="80">
        <template #default="scope">
          {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="用户名" prop="username"></el-table-column>
      <el-table-column label="邮箱" prop="email"></el-table-column>
      <el-table-column label="用户创建时间" prop="createTime"> </el-table-column>
      <el-table-column label="用户信息更新时间" prop="updateTime"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Delete" circle plain type="danger" @click="deleteUser(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页条 -->
    <el-pagination 
    v-model:current-page="pageNum" 
    v-model:page-size="pageSize" 
    :page-sizes="[10, 20, 30, 40]"
    layout="jumper, total, sizes, prev, pager, next" 
    background 
    :total="total" 
    @size-change="onSizeChange"
    @current-change="onCurrentChange" 
    style="margin-top: 20px; justify-content: flex-end" />

  </el-card>
</template>
<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;
}

.page-container .header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* 抽屉样式 */
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

.addButton{
  margin-top:30px;
  margin-left:30px;
}

.editor {
  width: 100%;

  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>