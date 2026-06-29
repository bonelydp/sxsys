<template>
  <div class="homeContainer">
    <div class="top">
      <div class="pic">
        <el-carousel :interval="5000" arrow="always" class="carousel-container">
          <el-carousel-item v-for="(contour, index) in contourList" :key="index">

            <img :src="contour.img" alt="轮廓图" style="width: 100%; height: 100%; object-fit: cover;">
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="inf">
        <el-card shadow="hover" class="information">
          <template #header>
            <div class="card-header">
              <span>{{ t('message.notification') }}</span>
              <router-link to="/home/information" @click="navigateToMoreNotifications"
                class="more-link">{{ t('message.more') }} >></router-link>
            </div>
          </template>
          <NotificationVc v-for="(item, index) in newsItems.slice(0, 2)" :key="index" :id="item.id" :title="item.title"
            :date="item.date"></NotificationVc>
        </el-card>
      </div>
    </div>
    <div class="underline-text1">
      {{ t('message.dataInformation') }}
    </div>
    <div class="middle">
      
      <div class="map" @click="navigateToDataVisulization">
        <div ref="mapChartRef" class="mapChart"></div>
        <div class="vertical-text description">
          <p>{{ t('message.distributionMap') }}</p>
        </div>
        <div class="vertical-text title">
          <p>{{ t('message.dataVisualization') }}</p>
        </div>
      </div>
      <div>
        
        <div class="data">

          <div class="stat-item">
            <span class="number">{{totalSample}}</span>
            <span class="description">{{ t('message.totalSample') }} </span>
          </div>
          <div class="stat-item">
            <span class="number">{{totalHost}}</span>
            <span class="description">{{ t('message.totalSpecies') }}</span>
          </div>
          <div class="stat-item">
            <span class="number">{{totalProvince}}</span>
            <span class="description">{{ t('message.originalRegion') }}</span>
          </div>
          <div class="stat-item">
            <span class="number">1</span>
            <span class="description">{{ t('message.yinzi') }}</span>
          </div>
        </div>
      </div>
    </div>


    <div class="bottom">
      <FunctionModelVc></FunctionModelVc>
    </div>

  </div>



  <!-- <el-carousel :interval="100000" type="card" height="300px">
      <el-carousel-item v-for="item in 6" :key="item">
        <h3 text="2xl" justify="center">{{ item }}</h3>
      </el-carousel-item>
    </el-carousel> -->
  <!-- <el-carousel :interval="4000" type="card" height="300px" v-if="contourList.length > 0">
      <el-carousel-item v-for="contour in contourList" :key="contour.id">
        <img :src="contour.img" alt="轮廓图" style="width: 100%; height: 100%; object-fit: cover;">
      </el-carousel-item>
    </el-carousel> -->
  <!-- <el-carousel :interval="5000" arrow="always" height="400px" v-if="contourList.length > 0">
      <el-carousel-item v-for="contour in contourList" :key="contour.id">
        <img :src="contour.img" alt="轮廓图" style="width: 100%; height: 100%; object-fit: cover;">
      </el-carousel-item>
    </el-carousel> -->







</template>

<script setup>
import { ref, onMounted } from 'vue';
import { informationService } from "@/api/information";
import FunctionModelVc from '@/components/FunctionModelVc.vue';
import NotificationVc from "@/components/NotificationVc.vue"
import axios from 'axios';
import { useRouter } from 'vue-router'
import { getAliyunMapData } from '@/api/map'

//中英文切换
import { useI18n } from 'vue-i18n';
const { t } = useI18n();

const router = useRouter();
//跳转数据可视化
const navigateToDataVisulization = () => {
  router.push('/datavisualization');
}
//跳转更多消息
const navigateToMoreNotifications = () => {
  router.push('/notifications')
}
// 示例新闻数据
const newsItems = ref([]);

// 轮廓图数据
const contourList = ref([]);

// 获取轮廓图数据
const fetchContours = async () => {
  try {
    const response = await axios.get('/api/contour1/selectAll');
    console.log("response.data.code", response.data.code)
    console.log("Array.isArray(response.data.data)", Array.isArray(response.data.data))
    if (Array.isArray(response.data.data)) {
      contourList.value = response.data.data.map(item => ({
        id: item.id,
        img: item.img,
      }));
      console.log("contourList.value", contourList.value)
    } else {
      console.error('Unexpected response format or status code:', response.data.code);
    }
  } catch (error) {
    console.error('Failed to fetch contour data:', error);
  }
};

//获取通知数据
const fetchNotices = async () => {
  try {
    let result = await informationService();
    console.log("result.data.data", result.data.data);
    newsItems.value = result.data.data;
  } catch (error) {
    console.error("Failed to fetch contours:", error);
  }
};

//-------------------------------------------------------------middle------------------------------------------------------------------------


import * as echarts from "echarts/core";
import { GeoComponent } from 'echarts/components';
import { VisualMapComponent, LegendComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { MapChart, BarChart, PieChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, GridComponent } from 'echarts/components';
echarts.use([LegendComponent, PieChart, MapChart, BarChart, CanvasRenderer, GeoComponent, VisualMapComponent, TitleComponent, GridComponent, TooltipComponent]);
import { getAllSampleInfoNoPageService, getCountOfSampleAndprovinceAndHost } from '@/api/visualization'
const mapChartRef = ref();
const currentMapName = ref("中国");
const province_InfectionSize_Data = ref([]);
//国级和省级
const currentLevel = ref("country")
const initAdCode = ref(100000);
const initChinaMap = ref();
const totalSample = ref();
const totalProvince = ref();
const totalHost = ref();
//根据当前地图的情况返回数据
const mapDataMapping = (item) => {
  if (currentLevel.value === 'country') {
    return { name: item[0], value: item[1] };
  } else {
    return { name: item.city, value: item.positiveCount };
  }
};
//获取样本数据
const getMapSample = async () => {
  try {
    let result = await getAllSampleInfoNoPageService();
    console.log("province_InfectionSize_Data.value", result.data.data);
    province_InfectionSize_Data.value = result.data.data;
    result = await getCountOfSampleAndprovinceAndHost();
    console.log("sample_province_host_Data.value", result.data.data);
    totalSample.value = result.data.data[0][0];
    totalHost.value = result.data.data[0][1];
    totalProvince.value = result.data.data[0][2];
  } catch (error) {
    console.error("Failed to fetch sample data:", error);
  }
}
// 获取地图数据
const getMapData = async (code) => {
  try {
    const mapData = await getAliyunMapData(code);
    await renderChart(currentMapName.value, mapData);
    return mapData;
  } catch (error) {
    console.error('Failed to fetch Aliyun map data:', error);
  }
};
// 渲染地图
const renderChart = async (mapName, mapData) => {
  console.log("renderChart");
  // 注册: registerMap('地图名称', 地图数据）, 地图名称须和option里面的map一致
  echarts.registerMap(mapName, mapData);
  // 地图配置项
  const option = {
    // 提示浮窗样式
    tooltip: {
      show: true,
      trigger: "item",
      formatter: function (params) {
        return `${params.name}<br/>病例数量: ${isNaN(params.value) ? 0 : params.value} (例)`;
      }
    },
    geo: {
      map: mapName,
      roam: false, // 禁用缩放和平移
      label: {
        show: false // 不显示市级名称
      },
      itemStyle: {
        areaColor: '#eee', // 地图背景色
        borderColor: '#444' // 边界线颜色
      },

      emphasis: {
        itemStyle: {
          areaColor: 'rgba(255, 165, 0, 0.8)', // 设置悬停时的地图背景色
          borderColor: 'rgba(255, 165, 0, 1)' // 设置悬停时的边界线颜色
        }
      },
      zoom: 1.2 // 调整缩放级别以适应地图居中
    },
    visualMap: {
      show: true,
      right: '85%', // 确保有足够的空间
      bottom: '5%', // 确保有足够的空间
      seriesIndex: 0,
      type: 'piecewise',
      pieces: [
        { min: 5000, color: 'rgb(112,22,29)' },
        { min: 500, max: 2000, color: 'rgb(203,42,47)' },
        { min: 100, max: 499, color: 'rgb(229,90,78)' }, // 更改阈值以匹配数据范围
        { min: 10, max: 99, color: 'rgb(245,158,131)' },
        { min: 1, max: 9, color: 'rgb(253,235,107)' }
      ],
      textStyle: {
        color: '#000000'
      }
    },
    series: [
      {
        type: "map",
        data: province_InfectionSize_Data.value.map(mapDataMapping),
        geoIndex: 0,
        label: {
          show: false, // 是否显示省份名字
        },

      },
    ],
  };
  // 渲染
  initChinaMap.value.setOption(option);
}

onMounted(async () => {

  initChinaMap.value = echarts.init(mapChartRef.value);
  getMapData(initAdCode.value);
  await getMapSample();
  getMapData(initAdCode.value);
  fetchContours();
  fetchNotices();
});

</script>

<style scoped>
.homeContainer {
  align-items: center;
  justify-content: center;

}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.top {
  display: flex;
  margin-top: 50px;
  align-items: center;
  justify-content: center;

}

.carousel-container {
  width: 800px;
  height: 300px;
  border-radius: 10px;
}

.inf {
  margin-left: 100px;
  width: 400px;
  border-radius: 10px;
}

.information {
  margin-top: 10px;

}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.middle {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
}

.map {
  display: flex;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 800px;
  align-items: center;
  justify-content: center;
  margin-left: -100px;
}

.mapChart {
  height: 300px;
  width: 600px;
  margin-left: 70px;
}

.data {
  height: auto;
  widows: auto;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 80px;
  margin-top: -40px;
  margin-left: 100px;
}

.stat-item {
  display: flex;
  height: 60px;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.description {
  font-size: 14px;
  color: #555;
}

.bottom {
  margin-top: 80px;
}

/* <-------------------------------------------------------------------------> */
.more-link {
  color: #0f0f0f;
  text-decoration: none;
}

.news-list {
  list-style-type: none;
  padding: 0;
}

.news-list li {
  margin-bottom: 1rem;
}

.news-list p,
.news-list h3 {
  color: #0f0f0f;
  font-size: 15px;
}

.date {
  color: gray;
}

/* ----------下划线------------------- */
.underline-text {
  font-size: 20px;
  font-weight: 500;
  position: relative;
  display: inline-block;
  /* 让容器仅包裹文本 */
  margin-top: 40px;
}

.underline-text::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -5px;
  /* 调整此值以改变线条与文本的距离 */
  width: 400px;
  /* 线条宽度为文本宽度的120% */
  height: 2px;
  /* 线条高度 */
  background-color: #000000;
  /* 线条颜色 */
  z-index: -1;
  /* 确保线条在文本之下 */
}

.underline-text1 {

  margin-left: 1300px;
  font-size: 20px;
  font-weight: 500;
  position: relative;
  display: inline-block;
  /* 让容器仅包裹文本 */
  margin-top: 40px;
}

.underline-text1::after {
  content: '';
  right: 0;
  height: 2px;
  width: 500px;
  bottom: -5px;
  background-color: #000000;
  z-index: -1;
  position: absolute;
}

.vertical-text {
  writing-mode: vertical-rl;
  text-orientation: mixed;
}

.vertical-text.description {
  height: 160px;
  text-align: center;
  margin-right: -20px;
}

.vertical-text.title {
  font-weight: bold;
  font-size: 25px;
  color: #8BABB4;
}


</style>
