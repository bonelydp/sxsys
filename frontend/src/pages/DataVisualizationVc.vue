<template>
  <div class="data-visualization-vc">
    <div class="top">
      <div class="select">
        <!-- 新增省份选择框 -->
        <el-button type="primary" @click="goBack">{{ t('message.backToParent') }}</el-button>
        <!-- 新增宿主选择框 -->
        <el-select v-model="host" placeholder="选择宿主" @change="updateCharts">
          <el-option v-for="host in availableHosts" :key = "host" :value="host" :label="host"></el-option>
        </el-select>
      </div>
      <div class="map">
        <div ref="mapChartRef" style="height: 600px; width: 1000px"></div>
        <div style="margin-top: 10px; display: flex; align-items: center;">
          <el-select v-model="selectedYear" placeholder="选择年份" @change="updateCharts"
            style="margin-top: 10px;width:100px">
            <el-option v-for="year in availableYears" :key="year" :label="year" :value="year"></el-option>
          </el-select>
        </div>
      </div>
    </div>
    <div class="bottom">
      <div class="speciesSelect">
        <el-select v-model="species" placeholder="虫种" @change="updateCharts">
          <el-option value="C.parvum" label="C.parvum"></el-option>
        </el-select>
      </div>
      <div class="barChart_table">
        <div class="chart-container">
          <div ref="barChart" class="barChart"></div>
          <div v-if="currentLevel === 'province'" ref="pieChartRef" class="pieChart"></div>
        </div>
        <div class="table">
          <h3>{{ selectedYear }}年{{ currentMapName }}微小隐孢子虫流行病学数据</h3>
          <el-table :data="tableData" class="rounded-table" :empty-text="'没有数据'">
            <el-table-column prop="[][0]" :label="dynamicLabel" width="200"></el-table-column>
            <el-table-column prop="[][1]" label="样本数" width="100"></el-table-column>
            <el-table-column prop="[][2]" label="阳性数" width="100"></el-table-column>
            <el-table-column prop="[][3]" label="感染率" width="100"></el-table-column>
          </el-table>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUpdated, watch } from "vue";
import { getAliyunMapData } from '@/api/map'
import * as echarts from "echarts/core";
import { GeoComponent } from 'echarts/components';
import { VisualMapComponent, LegendComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { MapChart, BarChart, PieChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, GridComponent } from 'echarts/components';
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
const { t } = useI18n()
echarts.use([LegendComponent, PieChart, MapChart, BarChart, CanvasRenderer, GeoComponent, VisualMapComponent, TitleComponent, GridComponent, TooltipComponent]);

const mapChartRef = ref();
const barChart = ref(null);
const pieChartRef = ref(null);
let initChinaMap = ref(null);
let barChartInstance = null;
let pieChartInstance = null;

const currentLevel = ref("country");
const currentMapName = ref("中国");
const initAdCode = ref(100000);
const mapList = ref([]);
const historyInfo = ref([]);
const province = ref("");

const province_InfectionSize_Data = ref([]);
const province_InfectionRate_Data = ref([]);
const tableData = ref([]);
const dynamicLabel = ref("省份");
const availableYears = ref([]);
const availableHosts = ref([])

const species = ref("C.parvum");
const selectedYear = ref(2020);
const host = ref("无宿主");

import {
  getYearSampleService, getProvinceAndInfectionSizeSampleService, getCityAndInfectionSizeSampleService
  , getProvinceInfectionRateSampleService, getProvinceSampleSizeInfectionCountInfectionRateService, getHostSampleSizeInfectionCountInfectionRateService,
  getInfectionRateSampleService,
  getHostService
} from '@/api/visualization';
const initCharts = () => {
  if (mapChartRef.value && !initChinaMap.value) {
    initChinaMap.value = echarts.init(mapChartRef.value);
  }
  if (barChart.value && !barChartInstance) {
    barChartInstance = echarts.init(barChart.value);
  }
};

const cleanupCharts = () => {
  if (pieChartInstance) {
    pieChartInstance.dispose();
    pieChartInstance = null;
  }
};

const getMapSample = async () => {
  try {
    const yearResponse = await getYearSampleService();
    if (yearResponse.data?.data) {
      console.log("yearResponse.data.data",yearResponse.data);
      availableYears.value = yearResponse.data.data;
      if (availableYears.value.length > 0 && !selectedYear.value) {
        selectedYear.value = availableYears.value[0];
      }
    }
    const hostResponse = await getHostService();
    availableHosts.value = hostResponse.data.data;
    if(availableHosts.value.length > 0)
    {
      host.value = availableHosts.value[0];
    }

    if (currentLevel.value === "country") {
      const params = {
        year: selectedYear.value,
        host: host.value,
      };
      
      const [sizeResponse, rateResponse, tableResponse] = await Promise.all([
        getProvinceAndInfectionSizeSampleService(params),
        getProvinceInfectionRateSampleService(params),
        getProvinceSampleSizeInfectionCountInfectionRateService(params)
      ]);

      province_InfectionSize_Data.value = sizeResponse.data?.data || [];
      province_InfectionRate_Data.value = rateResponse.data?.data || [];
      console.log("province_InfectionRate_Data.value",province_InfectionRate_Data.value);
      tableData.value = tableResponse.data?.data || [];
      dynamicLabel.value = "省份";
    } else {
      const params = {
        province: province.value,
        year: selectedYear.value,
        host: host.value,
      };

      const [cityResponse, rateResponse, hostResponse] = await Promise.all([
        getCityAndInfectionSizeSampleService(params),
        getInfectionRateSampleService(params),
        getHostSampleSizeInfectionCountInfectionRateService({
          province: province.value,
          year: selectedYear.value,
        })
      ]);

      province_InfectionSize_Data.value = cityResponse.data?.data || [];
      province_InfectionRate_Data.value = rateResponse.data?.data || [];
      tableData.value = hostResponse.data?.data || [];
      dynamicLabel.value = "物种";
    }
  } catch (error) {
    console.error("获取数据失败:", error);
    ElMessage.error('获取数据失败，请稍后重试');
  }
};

const getMapData = async (code) => {
  try {
    const mapData = await getAliyunMapData(code);
    mapList.value = mapData.features.map((item) => item.properties);
    await renderChart(currentMapName.value, mapData);
  } catch (error) {
    console.error("获取地图数据失败:", error);
    ElMessage.error('获取地图数据失败，请稍后重试');
  }
};

const renderChart = async (mapName, mapData) => {
  if (!initChinaMap.value) return;

  echarts.registerMap(mapName, mapData);
  
  const option = {
    tooltip: {
      show: true,
      trigger: "item",
      formatter: function (params) {
        return `${params.name}<br/>病例数量: ${isNaN(params.value) ? 0 : params.value} (例)`;
      }
    },
    geo: {
      map: mapName,
      roam: false,
      label: {
        show: false
      },
      itemStyle: {
        areaColor: '#eee',
        borderColor: '#444'
      },
      emphasis: {
        itemStyle: {
          areaColor: 'rgba(255, 165, 0, 0.8)',
          borderColor: 'rgba(255, 165, 0, 1)'
        }
      },
      zoom: 1.2
    },
    visualMap: {
      show: true,
      right: '85%',
      bottom: '5%',
      seriesIndex: 0,
      type: 'piecewise',
      pieces: [
        { min: 5000, color: 'rgb(112,22,29)' },
        { min: 500, max: 2000, color: 'rgb(203,42,47)' },
        { min: 100, max: 499, color: 'rgb(229,90,78)' },
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
          show: false,
        },
      },
    ],
  };

  initChinaMap.value.setOption(option, true);
  initChinaMap.value.off("click");
  
  initChinaMap.value.on("click", async (params) => {
    const activeItem = mapList.value.find((item) => item.name === params.name);
    if (activeItem?.adcode && activeItem?.childrenNum && currentLevel.value === "country") {
      currentLevel.value = activeItem.level;
      province.value = activeItem.name;
      historyInfo.value.push(activeItem);
      currentMapName.value = params.name;
      initAdCode.value = activeItem.adcode;
      
      await getMapSample();
      await getMapData(activeItem.adcode);
      await updateCharts();
    }
  });
};

const updateCharts = async () => {
  if (!barChartInstance) return;

  const barOption = {
    title: {
      text: `${selectedYear.value}年${host.value}微小隐孢子虫感染率`,
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: province_InfectionRate_Data.value.length ? province_InfectionRate_Data.value.map(item => item[0]) : ['无数据'],
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '感染率(%)',
      min: 0,
      max: 100,
      interval: 10,
    },
    grid: {
      left: '5%',
      right: '5%',
      bottom: '10%',
      containLabel: true
    },
    series: [
      {
        name: '感染率',
        type: 'bar',
        data: province_InfectionRate_Data.value.length ? province_InfectionRate_Data.value.map(item => item[1]) : [0],
        label: {
          show: true,
          position: 'top',
          formatter: function (params) {
            const data = province_InfectionRate_Data.value[params.dataIndex];
            if (data && data[2] !== undefined) {
              return `${data[2]}`;
            }
            return '0';
          }
        }
      }
    ]
  };

  barChartInstance.setOption(barOption, true);

  if (currentLevel.value === 'province' && pieChartRef.value) {
    if (!pieChartInstance) {
      pieChartInstance = echarts.init(pieChartRef.value);
    }
    updatePieChart();
  }
};

const updatePieChart = () => {
  console.log("饼状图province_InfectionRate_Data.value", province_InfectionRate_Data.value);
  const total = province_InfectionRate_Data.value.reduce((sum, item) => sum + item[1], 0);
  const dataWithOther = province_InfectionRate_Data.value.map(item => ({
    name: species.value,
    value: item[1]
  }))

  const customColors = ['#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed', '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'];

  const targetTotal = 100;
  if (total < targetTotal) {
    dataWithOther.push({
      name: "other",
      value: targetTotal - total,
      itemStyle: { color: '#FBE3D6' }
    })
  }

  const pieOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      right: 'right',
    },
    color:customColors,
    series: [
      {
        name: '感染率',
        type: 'pie',
        radius: '50%',
        data: dataWithOther,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  pieChartInstance.setOption(pieOption);
};

const goBack = async () => {
  const lastItem = historyInfo.value.pop();
  if (lastItem?.parent?.adcode) {
    try {
      province.value = "中国";
      currentLevel.value = "country";
      initAdCode.value = lastItem.parent.adcode;
      currentMapName.value = "中国";
      
      await getMapSample();
      await getMapData(lastItem.parent.adcode);
      await updateCharts();
      
      cleanupCharts();
    } catch (error) {
      console.error("返回上级失败:", error);
      ElMessage.error('操作失败，请刷新页面重试');
    }
  }
};

onMounted(async () => {
  try {
    initCharts();
    await getMapSample();
    await getMapData(initAdCode.value);
    await updateCharts();
  } catch (error) {
    console.error("初始化失败:", error);
    ElMessage.error('初始化失败，请刷新页面重试');
  }
});

onUpdated(() => {
  if (currentLevel.value === 'province' && pieChartRef.value) {
    if (!pieChartInstance) {
      pieChartInstance = echarts.init(pieChartRef.value);
    }
    updatePieChart();
  } else {
    pieChartInstance = null;
  }
});

watch(currentLevel, (newValue) => {
  dynamicLabel.value = newValue === 'country' ? t('message.province') : t('message.species');
});

watch([selectedYear, host], async () => {
  try {
    await getMapSample();
    await getMapData(initAdCode.value);
    await updateCharts();
  } catch (error) {
    console.error("数据更新失败:", error);
    ElMessage.error('数据更新失败，请稍后重试');
  }
}, { deep: true });

const mapDataMapping = (item) => {
  if (currentLevel.value === 'country') {
    return { name: item[0], value: item[1] };
  } else {
    return { name: item.city, value: item.positiveCount };
  }
};
</script>

<style scoped>
.data-visualization-vc{
  margin-top: 20px;
  width: 1300px;
  margin-left: 140px;
}
.select {
  display: flex;
  margin-left: 15px;
  margin-top: 10px;
}

.select .el-select {
  width: 100px;
  margin-left: 5px;
}

.top {
  border-radius: 10px;
  border: 2px solid #dcdcdc;
  padding-top: 10px;
  padding-bottom: 50px;
  width: 95%;
}

.map {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bottom {
  width: 95%;
  border-radius: 10px;
  border: 2px solid #dcdcdc;
  margin-top: 10px;
  padding-bottom: 20px;
}

.speciesSelect {
  width: 150px;
  display: flex;
  margin-left: 15px;
  margin-top: 10px;
}

.barChart_table {
  margin-top: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chart-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  gap: 20px;
  flex-wrap: nowrap;
  border-bottom: 1px dashed #ccc;
}

.barChart,
.pieChart {
  width: 800px;
  height: 400px;
}

.barChart:only-child {
  flex-grow: 1;
  margin: 0 auto;
}

.table {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 500px
}

.table h3 {
  text-align: center;
}

.rounded-table {
  padding: 12px;
  border-radius: 10px;
  border: 1px solid #dcdcdc;
  overflow: hidden;
  margin-top: 10px;
  height: 300px;
  display: flex;
  justify-content: center;
}
</style>
