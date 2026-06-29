package com.example.controller;

import com.example.DTO.SampleInfoRequestDTO;
import com.example.DTO.SampleInfoResponseDTO;
import com.example.DTO.SampleInfoUpdateDTO;
import com.example.DTO.SubtypeDetailDTO;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.pojo.SampleInfo;
import com.example.pojo.SubtypeDetail;
import com.example.service.SampleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sampleinfo")
public class SampleInfoController {

    @Autowired
    private SampleInfoService sampleInfoService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitSampleInfo(@RequestBody SampleInfo sampleInfo) {
        try {
            sampleInfoService.saveSampleInfo(sampleInfo);
            return new ResponseEntity<>("Data submitted successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while submitting data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 数据可视化

    // pre 获取所有年份
    @GetMapping("/year")
    public Result<List<Integer>> getYear(){
        List<Integer> l = sampleInfoService.getYear();
        return Result.success(l);
    }

    // pre 获取所有年份
    @GetMapping("/host")
    public Result<List<String>> getHost(){
        List<String> l = sampleInfoService.getHost();
        return Result.success(l);
    }

    // 1.根据年份，宿主获取感染率
    @GetMapping("province_infectionRate")
    public Result<List<Object[]>> getByYearAndHostWithInfectionRate(
            @RequestParam(required = false) int year,
            @RequestParam(required = false) String host) {
        List<Object[]> c =  sampleInfoService.findByYearAndHostWithInfectionRate(year, host);
        return Result.success(c);
    }

    // 2.年份，宿主获取省份，样本，感染数，感染率
    @GetMapping("details")
    public Result<List<Object[]>> getByYearAndHostWithDetails(
            @RequestParam(required = false) int year,
            @RequestParam(required = false) String host) {
        List<Object[]> c =  sampleInfoService.findByYearAndHostWithDetails(year, host);
        return Result.success(c);
    }

    // 3.根据省份、年份和宿主获取感染率
    @GetMapping("/infectionRate")
    public Result<List<Object[]>> getByProvinceAndYearAndHostWithInfectionRate(
            @RequestParam(required = false) int year,
            @RequestParam(required = false) String host,
            @RequestParam(required = false) String province
    ){
        List<Object[]> c =  sampleInfoService.findByProvinceAndYearAndHostWithInfectionRate(year, host,province);
        return Result.success(c);
    }

    // 4.年份，宿主获取省份及其感染数
    @GetMapping("/province-infection-count")
    public Result<List<Object[]>> getByYearAndHostWithProvinceAndInfectionCount(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String host) {
        List<Object[]> c =  sampleInfoService.findByYearAndHostWithProvinceAndInfectionCount(year, host);
        return Result.success(c);
    }

    // 5.获取宿主，样本，感染数，感染率
    @GetMapping("/host_details")
    public Result<List<Object[]>> getByProvinceAndYearWithDetails(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) int year) {
        List<Object[]> c = sampleInfoService.findByProvinceAndYearWithDetails(year, province);
        return Result.success(c);
    }

    // 6-1.获取某一省份特定年份某宿主所有市级单位的感染数
    @GetMapping("/city-infection-count")
    public Result<List<Map<String, Object>>> getByProvinceAndYearAndHostWithCityAndInfectionCount(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) int year,
            @RequestParam(required = false) String host) {
        List<Object[]> data = sampleInfoService.findByProvinceAndYearAndHostWithCityAndInfectionCount(year, province, host);

        // 处理数据，将 null 城市设为省会城市，并合并同一城市的感染数
        Map<String, Integer> cityCounts = new HashMap<>();
        for (Object[] row : data) {
            String city = (String) row[0];
            Integer positiveCount = (Integer) row[1];

            if (city == null) {
                city = getCapitalCity(province);
            }

            cityCounts.merge(city, positiveCount, Integer::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cityCounts.entrySet()) {
            result.add(Map.of(
                    "city", entry.getKey(),
                    "positiveCount", entry.getValue()
            ));
        }

        return Result.success(result);
    }
    // 6-2.获取省份的省会城市（假设有一个静态映射）
    private String getCapitalCity(String province) {
        Map<String, String> capitals = new HashMap<>();
        capitals.put("新疆", "乌鲁木齐");
        capitals.put("浙江", "杭州");
        capitals.put("江苏", "南京");
        capitals.put("广东", "广州");
        capitals.put("江西", "南昌");
        capitals.put("福建", "福州");
        capitals.put("广西", "南宁");
        capitals.put("云南", "昆明");
        capitals.put("海南", "海口");
        capitals.put("贵州", "贵阳");
        capitals.put("湖南", "长沙");
        capitals.put("湖北", "武汉");
        capitals.put("安徽", "合肥");
        capitals.put("四川", "成都");
        capitals.put("西藏", "拉萨");
        capitals.put("河南", "郑州");
        capitals.put("陕西", "西安");
        capitals.put("甘肃", "兰州");
        capitals.put("青海", "西宁");
        capitals.put("宁夏", "银川");
        capitals.put("山西", "太原");
        capitals.put("内蒙古", "呼和浩特");
        capitals.put("河北", "石家庄");
        capitals.put("山东", "济南");
        capitals.put("辽宁", "沈阳");
        capitals.put("吉林", "长春");
        capitals.put("黑龙江", "哈尔滨");
        capitals.put("北京", "西城区");
        capitals.put("上海", "黄浦区");
        capitals.put("天津", "和平区");
        capitals.put("重庆", "渝中区");
        capitals.put("香港", "九龙城区");
        capitals.put("澳门", "大堂区");
        capitals.put("台湾", "台北");

        return capitals.getOrDefault(province, "未知");
    }


    // CURD

    // Create 新增信息
    @PostMapping("/create-single")
    public void addSampleInfoWithSubtypes(@RequestBody SampleInfoRequestDTO dto) {
        try {

            SampleInfo sampleInfo = sampleInfoService.convertToEntity(dto);
            // 调用 Service 层方法保存数据
            sampleInfoService.addSampleInfoWithSubtypes(sampleInfo);
            // System.out.println("Converted SampleInfo: " + sampleInfo);

        }catch (Exception e){
            e.printStackTrace(); // 打印详细错误信息
        }
    }

    @PostMapping("/create-batch")
    public void batchAddSampleInfos(@RequestBody List<SampleInfoRequestDTO> dtos) {
        try {
            // 将 DTO 列表转换为实体对象列表
            List<SampleInfo> sampleInfos = new ArrayList<>();
            for (SampleInfoRequestDTO dto : dtos) {
                sampleInfos.add(sampleInfoService.convertToEntity(dto));
            }

            // 调用 Service 层方法批量保存数据
            sampleInfoService.batchAddSampleInfos(sampleInfos);
        } catch (Exception e) {
            e.printStackTrace(); // 打印详细错误信息
        }
    }




    // Read 查询信息(所有全部信息)
    @GetMapping("/read-all")
    public Result<Page<SampleInfoResponseDTO>> getAllSampleInfoWithSubtypes(
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize) {
        System.out.println("currentPage pageSize"+currentPage+" "+pageSize);
        Page<SampleInfoResponseDTO> result = sampleInfoService.getAllSampleInfoWithSubtypes(currentPage,pageSize);

        return Result.success(result);
    }

    //获取全部感染数分布
    @GetMapping("/all-infection-count")
    public Result<List<Object[]>> getAllInfectionCount() {
        List<Object[]> c =  sampleInfoService.findAllInfectionCount();
        return Result.success(c);
    }

    //获取全部样本数、寄主数、省份数
    @GetMapping("/sample-host-province-count")
    public Result<List<Object[]>> findCountOfSampleAndHost() {
        List<Object[]> c =  sampleInfoService.findCountOfSampleAndHost();
        return Result.success(c);
    }

    // Delete 删除信息
    @DeleteMapping("/delete")
    public Result<Void> deleteSampleInfo(@RequestParam("sampleId") String sampleId) {
        // 调用 Service 删除数据
        sampleInfoService.deleteSampleInfo(sampleId);
        return Result.success();
    }



    // Update 更新信息,不能更新SampleId
    @PutMapping("/update")
    public Result<Void> updateSampleInfo(@RequestBody SampleInfoUpdateDTO updateDTO) {
        // 调用 Service 更新数据
        sampleInfoService.updateSampleInfo(updateDTO);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<List<SampleInfoResponseDTO>> seatchSample(@RequestParam("sampleId") String sampleId){
        List<SampleInfoResponseDTO> result = sampleInfoService.findBySampleId(sampleId);
        return Result.success(result);
    }
}