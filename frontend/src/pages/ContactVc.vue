<template>
  <div class="image-manager">
    <!-- 图片上传 -->
    <el-upload
      action="#"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :before-upload="beforeUpload"
      :file-list="paginatedImages"
      :auto-upload="false"
      multiple
      accept="image/*"
    >
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog v-model="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>


    <!-- 操作按钮 -->
    <div style="margin-top: 20px;">
      <el-button type="primary" @click="addImages">添加图片</el-button>
      <el-button type="success" @click="saveChanges">保存更改</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import avatar from '@/assets/avatar.jpg'; // 导入本地图片

// 使用本地图片作为初始数据
const imageList = ref([
  { url: avatar, name: 'avatar.jpg' },
  { url: avatar, name: 'avatar_copy.jpg' }, // 可以根据需要添加更多项
]);

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(8); // 每页显示的数量



// 计算属性 - 当前页面的图片列表
const paginatedImages = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return imageList.value.slice(start, end).map(image => ({ ...image, url: image.url }));
});

// 处理预览图片
const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

// 处理移除图片
const handleRemove = (file, fileList) => {
  const index = imageList.value.findIndex(img => img.name === file.name);
  if (index > -1) {
    imageList.value.splice(index, 1);
  }
  ElMessage({
    message: '图片删除成功',
    type: 'success',
  });
};

// 上传前检查
const beforeUpload = (file) => {
  const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJPGOrPNG) {
    ElMessage.error('仅允许上传 JPG 或 PNG 文件!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
  }
  return isJPGOrPNG && isLt2M;
};

// 添加图片
const addImages = () => {
  const newImage = { url: avatar, name: `avatar_${Math.floor(Math.random() * 100)}.jpg` };
  imageList.value.push(newImage);
  ElMessage({
    message: '图片添加成功',
    type: 'success',
  });
};

// 保存更改
const saveChanges = () => {
  ElMessage({
    message: '更改已保存',
    type: 'success',
  });
};

// 分页事件处理
const handleSizeChange = (newPageSize) => {
  pageSize.value = newPageSize;
};

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage;
};
</script>

<style scoped>
.image-manager {
  margin: 20px;
}
</style>