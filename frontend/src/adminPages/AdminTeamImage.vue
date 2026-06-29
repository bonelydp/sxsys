<template>
  <div class="container">
    <div class="underline-text">
      团队合照
    </div>
    <div class="image-manager">
      <!-- 图片上传 -->
      <el-upload action="/api/files/upload" list-type="picture-card" :on-preview="handlePictureCardPreview"
        :on-remove="handleRemove" :headers="{ 'Authorization': tokenStore.token }" :file-list="paginatedImages"
        :auto-upload="true" multiple accept="image/*" :on-success="uploadSuccess">
        <i class="el-icon-plus"></i>
      </el-upload>
      <el-dialog v-model="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="" />
      </el-dialog>

      <!-- 操作按钮 -->
      <!-- <div style="margin-top: 20px;">
      <el-button type="primary" @click="addImages">添加图片</el-button>
      <el-button type="success" @click="saveChanges">保存更改</el-button>
    </div> -->
    </div>
    <div class="underline-text">
      团队介绍
    </div>
    <div class="editor">
      <quill-editor theme="snow" contentType="html">
      </quill-editor>
    </div>
  </div>

</template>

<script setup>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { getTeamImageService, addTeamImageService, deleteTeamImageService } from "@/api/image"
import avatar from '@/assets/avatar.jpg'; // 导入本地图片
import { useTokenStore } from '@/store/mytoken';
import { clearConfig } from 'dompurify';
import { QuillEditor } from '@vueup/vue-quill'
const tokenStore = useTokenStore()
// 使用本地图片作为初始数据
const imageList = ref([
  { url: avatar, name: 'avatar.jpg' },
  { url: avatar, name: 'avatar_copy.jpg' }, // 可以根据需要添加更多项
]);
const getImage = async () => {
  let result = await getTeamImageService();
  console.log("result.data.data111", result.data.data);
  imageList.value = result.data.data
  console.log("imageList.value", imageList.value)
}
getImage()
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(8); // 每页显示的数量



// 计算属性 - 当前页面的图片列表
const paginatedImages = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return imageList.value.slice(start, end).map(image => ({ ...image, url: image.img }));
});

// 处理预览图片
const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

// 处理移除图片
const handleRemove = async (file, fileList) => {
  let result = await deleteTeamImageService(file.id)
  ElMessage({
    message: '图片删除成功',
    type: 'success',
  });
};

// 上传前检查
// const beforeUpload = (file) => {
//   const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png';
//   if (!isJPGOrPNG) {
//     ElMessage.error('仅允许上传 JPG 或 PNG 文件!');
//   }
//   const isLt2M = file.size / 1024 / 1024 < 10;
//   if (!isLt2M) {
//     ElMessage.error('图片大小不能超过 2MB!');
//   }
//   return isJPGOrPNG && isLt2M;
// };

const contour = ref(
  { name: "", category: "", img: "" }
)
const uploadSuccess = async (response, file, fileList) => {
  console.log("response", response);
  console.log("file", file);
  console.log("fileList", fileList);
  contour.value.name = file.name
  contour.value.name = file.name
  contour.value.img = response.data
  await addTeamImageService(contour.value)
  getImage()
}


</script>

<style scoped lang="scss">
.underline-text {
  font-size: 20px;
  font-weight: 500;
  position: relative;
  display: inline-block;
  /* 让容器仅包裹文本 */
  margin-top: 30px;
}

.underline-text::after {
  content: '';
  position: absolute;
  left: 0;

  bottom: -5px;
  /* 调整此值以改变线条与文本的距离 */
  width: 600px;
  /* 线条宽度为文本宽度的120% */
  height: 1.5px;
  /* 线条高度 */
  background-color: #000000;
  /* 线条颜色 */
  z-index: -1;
  /* 确保线条在文本之下 */
}

.image-manager {
  margin-top: 30px;
}

.editor {
  width: 80%;
  margin-top: 20px;

  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>