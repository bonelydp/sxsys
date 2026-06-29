<template>
    <div class="information" v-if="!isShow">
        <NotificationVc v-for="(item, index) in notifications" :key="index" :image="item.coverImg" :title="item.title"
            :date="item.date" :description="item.content" :id="item.id" :isShow="isShow"/>
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[20, 30, 50]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </div>
    <router-view></router-view>
</template>

<script setup>
import NotificationVc from '@/components/NotificationVc.vue';
import Avatar from '@/assets/avatar.jpg';
import { onBeforeUnmount, onUpdated, ref } from 'vue';

//分页管理
const total = ref()
//页面条数
const pageSize = ref(20)
//页码
const pageNum = ref(1)
//页面条数发生变化
const onSizeChange = (size) => {
    pageSize.value = size

}
//页码发生变化
const onCurrentChange = (num) => {
    pageNum.value = num
}
const notifications = ref([
    {
        id: 1,
        image: Avatar,
        title: '我院举行考研学生慰问活动',
        date: '2024-12-06',
        content:
            '为落实立德树人根本任务，进一步做好“有温度”的教育，缓解考研压力，增强备考信心，我院于12月6日上午在华山学生社区举行考研慰问活动。学院院长黄琼、学院党委副书记邓平宇、全体辅导员老师参加慰问活动，为考研学生送上诚挚的祝',
    },
]);

//获取全部通知
import { allInformationService } from "@/api/information"
const getNoticeInfo = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value
    }
    let result = await allInformationService(params)

    notifications.value = result.data.data.items

    total.value = result.data.data.total
}
getNoticeInfo()

import { useRoute } from 'vue-router';

const isShow = ref(false)
onUpdated(()=>{
  const route = useRoute()
  if(route.path == "/notifications"){
    isShow.value = false
  }else{
    isShow.value = true
  }
})

</script>

<style scoped>
.information {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 0px;
}
</style>