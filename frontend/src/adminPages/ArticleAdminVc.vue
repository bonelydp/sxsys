<script setup>
import {
  Edit,
  Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'
//用户搜索时选中的分类id
const categoryId = ref('')
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(1)//总条数
const pageSize = ref(3)//每页条数

//文章列表数据显示
import { articleInfoService, addArticleService, deleteArticleService, updateArticleService } from "@/api/article"
import { useArticleInfoStore } from '@/store/articleInfo';
const articles = ref()
const articleInfoStore = useArticleInfoStore()
const getArticleInfo = async () => {
  let params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  }
  let result = await articleInfoService(params)
  articleInfoStore.setInfo(result.data.data.items)
  articles.value = articleInfoStore.info
  total.value = result.data.data.total
}
getArticleInfo()


//添加文章
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus';
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
  title: '',
  categoryId: '',
  author: '',
  volume: '',
  publishTime: '',
  jcr: '',
  issn: ''
})

const addArticle = async () => {
  //调用接口
  let result = await addArticleService(articleModel.value)
  ElMessage.success(result.msg ? result.msg : '添加成功')
  //让抽屉消失
  visibleDrawer.value = !visibleDrawer.value
  articleModel.value = ref({})
  getArticleInfo()
}

//删除文章
const deleteArticle = async (row) => {
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
      let result = await deleteArticleService(row.id)
      ElMessage({
        type: 'success',
        message: '删除成功',
      })
      getArticleInfo()
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '用户取消了删除'
      })
    })
}

//修改文章信息
const dialogVisible = ref(false)
const articleNew = ref({})
const updateArticle = async () => {
  let result = await updateArticleService(articleNew.value)
  ElMessage.success(result.msg ? result.msg : "修改成功")
  getArticleInfo()
  dialogVisible.value = !dialogVisible.value
}

const editArticle = (row) => {
  dialogVisible.value = !dialogVisible.value
  articleNew.value = row
}



//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
  pageSize.value = size
  getArticleInfo()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
  pageNum.value = num
  getArticleInfo()
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章管理</span>
        <div class="extra">
          <el-button type="primary" @click="visibleDrawer = !visibleDrawer">添加文章</el-button>
        </div>
      </div>
    </template>
    <!-- 搜索表单 -->
    <el-form inline>
      <el-form-item label="文章搜索：">
        <el-input></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary">搜索</el-button>
        <el-button>重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 文章列表 -->
    <el-table :data="articles" style="width: 100%">
      <el-table-column label="序号" width="80">
        <template #default="scope">
          {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
      <el-table-column label="第一作者" prop="author"></el-table-column>
      <el-table-column label="期刊名称" prop="jcr"></el-table-column>
      <el-table-column label="发表时间" prop="publishTime"> </el-table-column>
      <el-table-column label="分类" prop="type"></el-table-column>
      <el-table-column label="卷期号" prop="volume"></el-table-column>
      <el-table-column label="ISSN" prop="issn"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="editArticle(row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页条 -->
    <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
      layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
      @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />


    <el-drawer v-model="visibleDrawer" title="添加文章" direction="rtl" size="50%">
      <!-- 添加文章表单 -->
      <el-form :model="articleModel" label-width="100px" v-show="visibleDrawer">
        <el-form-item label="文章标题">
          <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="文章第一作者">
          <el-input v-model="articleModel.author" placeholder="请输入文章第一作者"></el-input>
        </el-form-item>
        <el-form-item label="文章类型">
          <el-input v-model="articleModel.type" placeholder="请输入文章类型"></el-input>
        </el-form-item>
        <el-form-item label="期刊名称">
          <el-input v-model="articleModel.jcr" placeholder="请输入期刊名称"></el-input>
        </el-form-item>
        <el-form-item label="发表时间">
          <el-input v-model="articleModel.publishTime" placeholder="请输入发表时间"></el-input>
        </el-form-item>
        <el-form-item label="卷期号">
          <el-input v-model="articleModel.volume" placeholder="请输入卷期号"></el-input>
        </el-form-item>
        <el-form-item label="ISSN">
          <el-input v-model="articleModel.issn" placeholder="请输入ISSN"></el-input>
        </el-form-item>
        <el-form-item class="addButton">
          <el-button type="primary" @click="addArticle">发布</el-button>
          <el-button type="primary" @click="visibleDrawer = !visibleDrawer">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>

    <!-- 修改文章弹窗 -->
    <el-dialog v-model="dialogVisible" title="文章信息修改" width="30%">
      <el-form :model="articleNew" label-width="100px" style="padding-right: 30px">
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="articleNew.title"></el-input>
        </el-form-item>
        <el-form-item label="文章类型" prop="type">
          <el-input v-model="articleNew.type"></el-input>
        </el-form-item>
        <el-form-item label="文章第一作者" prop="author">
          <el-input v-model="articleNew.author"></el-input>
        </el-form-item>
        <el-form-item label="卷期号" prop="volume">
          <el-input v-model="articleNew.volume"></el-input>
        </el-form-item>
        <el-form-item label="发表时间" prop="publishTime">
          <el-input v-model="articleNew.publishTime"></el-input>
        </el-form-item>
        <el-form-item label="期刊名称" prop="jcr">
          <el-input v-model="articleNew.jcr"></el-input>
        </el-form-item>
        <el-form-item label="ISSN" prop="issn">
          <el-input v-model="articleNew.issn"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = !dialogVisible">取消</el-button>
          <el-button type="primary" @click="updateArticle"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>

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

.addButton {
  margin-top: 30px;
  margin-left: 30px;
}

.editor {
  width: 100%;

  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>