package com.example.service.impl;

import com.example.DTO.*;
import com.example.Repository.SampleInfoRepository;
import com.example.Repository.SubtypeDetailRepository;
import com.example.pojo.SampleInfo;
import com.example.pojo.SubtypeDetail;
import com.example.service.SampleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SampleInfoServiceImpl implements SampleInfoService {

    @Autowired
    private SampleInfoRepository sampleInfoRepository;

    @Autowired
    private SubtypeDetailRepository subtypeDetailRepository;

    public void saveSampleInfo(SampleInfo sampleInfo) {
        for (SubtypeDetail subtypeDetail : sampleInfo.getSubtypeDetails()) {
            subtypeDetail.setSampleInfo(sampleInfo); // 设置关联关系
        }

        sampleInfoRepository.save(sampleInfo); // 保存主记录和所有子记录
    }
    // 获取全部年份
    @Override
    public List<Integer> getYear(){
        return sampleInfoRepository.getYear();
    }
    // 获取全部宿主
    @Override
    public List<String> getHost(){
        return sampleInfoRepository.getHost();
    }

    //查询全部数据，不分页
    @Override
    public List<Object[]> findAllInfectionCount() {
        return sampleInfoRepository.findAllInfectionCount();
    }

    //获取全部样本数、寄主数、省份数
    @Override
    public List<Object[]> findCountOfSampleAndHost() {
        return sampleInfoRepository.findCountOfSampleAndHost();
    }

    // 1.根据年份，宿主获取感染率
    public List<Object[]> findByYearAndHostWithInfectionRate(int year, String host){
        return sampleInfoRepository.findByYearAndHostWithInfectionRate(year,host);
    }

    // 2.年份，宿主获取省份，样本，感染数，感染率
    public List<Object[]> findByYearAndHostWithDetails(int year,String host){
        return sampleInfoRepository.findByYearAndHostWithDetails(year,host);
    }

    // 3.根据省份、年份和宿主获取感染率
    public List<Object[]> findByProvinceAndYearAndHostWithInfectionRate(int year,String host,String province){
        return sampleInfoRepository.findByProvinceAndYearAndHostWithInfectionRate(province,year,host);
    }

    // 4.年份，宿主获取省份及其感染数
    public List<Object[]> findByYearAndHostWithProvinceAndInfectionCount(int year,String host){
        return sampleInfoRepository.findByYearAndHostWithProvinceAndInfectionCount(year,host);
    }

    // 5.获取宿主，样本，感染数，感染率
    public List<Object[]> findByProvinceAndYearWithDetails(int year,String province){
        return sampleInfoRepository.findByProvinceAndYearWithDetails(province,year);
    }

    // 6.获取某一省份特定年份某宿主所有市级单位的感染数
    public List<Object[]> findByProvinceAndYearAndHostWithCityAndInfectionCount(int year,String province,String host){
        return sampleInfoRepository.findByProvinceAndYearAndHostWithCityAndInfectionCount(province, year, host);
    }


    // CURD
    // 实体转换
    @Override
    public SampleInfo convertToEntity(SampleInfoRequestDTO dto){
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setSampleId(dto.getSampleId());
        sampleInfo.setProvince(dto.getProvince());
        sampleInfo.setCity(dto.getCity());
        sampleInfo.setSampleType(dto.getSampleType());
        sampleInfo.setCollectionYear(dto.getCollectionYear());
        sampleInfo.setHostSource(dto.getHostSource());
        sampleInfo.setOutbreak(dto.isOutbreak());
        sampleInfo.setDiarrhea(dto.isDiarrhea());
        sampleInfo.setDetectionMethod(dto.getDetectionMethod());
        sampleInfo.setAgeRangeStart(dto.getAgeRangeStart());
        sampleInfo.setAgeRangeEnd(dto.getAgeRangeEnd());
        sampleInfo.setcTotalSamples(dto.getcTotalSamples());
        sampleInfo.setcPositiveCases(dto.getcPositiveCases());
        sampleInfo.setcInfectionRate(dto.getcInfectionRate());
        sampleInfo.setcParvumPositiveCases(dto.getcParvumPositiveCases());
        sampleInfo.setcParvumInfectionRate(dto.getcParvumInfectionRate());
        sampleInfo.setcBovisPositiveCases(dto.getcBovisPositiveCases());
        sampleInfo.setcBovisInfectionRate(dto.getcBovisInfectionRate());
        sampleInfo.setcHominisPositiveCases(dto.getcHominisPositiveCases());
        sampleInfo.setcHominisInfectionRate(dto.getcHominisInfectionRate());
        sampleInfo.setcAnPositiveCases(dto.getcAnPositiveCases());
        sampleInfo.setcAnInfectionRate(dto.getcAnInfectionRate());
        sampleInfo.setcXiaoPositiveCases(dto.getcXiaoPositiveCases());
        sampleInfo.setcXiaoInfectionRate(dto.getcXiaoInfectionRate());
        sampleInfo.setcUbiquitumPositiveCases(dto.getcUbiquitumPositiveCases());
        sampleInfo.setcUbiquitumInfectionRate(dto.getcUbiquitumInfectionRate());
        sampleInfo.setMixedInfectionCases(dto.getMixedInfectionCases());
        sampleInfo.setOtherCPositiveCases(dto.getOtherCPositiveCases());

        // 转换 SubtypeDetail DTO 到实体对象
        if (dto.getSubtypeDetails() != null && !dto.getSubtypeDetails().isEmpty()) {
            for (SubtypeDetailDTO subtypeDetailDTO : dto.getSubtypeDetails()) {
                SubtypeDetail subtypeDetail = new SubtypeDetail();

                subtypeDetail.setSubTypeName(subtypeDetailDTO.getSubTypeName());
                subtypeDetail.setSubtypePositiveCases(subtypeDetailDTO.getSubtypePositiveCases());
                sampleInfo.getSubtypeDetails().add(subtypeDetail);
            }
        }

        return sampleInfo;
    }

    // 新增信息
    @Override
    public void addSampleInfoWithSubtypes(SampleInfo sampleInfo) {
        // 遍历 subtypeDetails，设置它们的 sampleInfo 关联
        if (sampleInfo.getSubtypeDetails() != null && !sampleInfo.getSubtypeDetails().isEmpty()) {
            for (SubtypeDetail subtypeDetail : sampleInfo.getSubtypeDetails()) {
                subtypeDetail.setSampleInfo(sampleInfo); // 设置双向关联
            }
        }

        try {
            sampleInfoRepository.save(sampleInfo);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("SampleId 已存在，请勿重复添加");
        }
    }



    // 批量增加
    @Override
    @Transactional
    public void batchAddSampleInfos(List<SampleInfo> sampleInfos) {
        for (SampleInfo sampleInfo : sampleInfos) {
            // 保存主表
            //sampleInfoRepository.save(sampleInfo);

            // 保存亚型表  仅当 subtypeDetails 不为空时才处理
            if (sampleInfo.getSubtypeDetails() != null && !sampleInfo.getSubtypeDetails().isEmpty()) {
                for (SubtypeDetail subtypeDetail : sampleInfo.getSubtypeDetails()) {
                    subtypeDetail.setSampleInfo(sampleInfo); // 设置外键关联
//                    subtypeDetailRepository.save(subtypeDetail);
                }
            }
            try {
                sampleInfoRepository.save(sampleInfo);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("SampleId 已存在，请勿重复添加");
            }

        }
    }



    // 查询信息
    @Override
    public Page<SampleInfoResponseDTO> getAllSampleInfoWithSubtypes(Integer currentPage, Integer pageSize) {
        // 注意：Spring 的 Pageable 是从第 0 页开始的，前端一般是第 1 页，所以要减一
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        // 调用 Repository 查询所有 SampleInfo 及其关联的 SubtypeDetail
        Page<SampleInfo> sampleInfos = sampleInfoRepository.findAllWithSubtypeDetails(pageable);

        // 将实体对象转换为 DTO
//        return sampleInfos.stream()
//                .sorted(Comparator.comparing(SampleInfo::getCollectionYear).reversed()) // 降序排列
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
        return sampleInfos.map(this::convertToDTO);

    }

    private SampleInfoResponseDTO convertToDTO(SampleInfo sampleInfo) {
        SampleInfoResponseDTO dto = new SampleInfoResponseDTO();
        dto.setSampleId(sampleInfo.getSampleId());
        dto.setProvince(sampleInfo.getProvince());
        dto.setCity(sampleInfo.getCity());
        dto.setSampleType(sampleInfo.getSampleType());
        dto.setCollectionYear(sampleInfo.getCollectionYear());
        dto.setHostSource(sampleInfo.getHostSource());
        dto.setOutbreak(sampleInfo.isOutbreak());
        dto.setDiarrhea(sampleInfo.isDiarrhea());
        dto.setDetectionMethod(sampleInfo.getDetectionMethod());
        dto.setAgeRangeStart(sampleInfo.getAgeRangeStart());
        dto.setAgeRangeEnd(sampleInfo.getAgeRangeEnd());
        dto.setcTotalSamples(sampleInfo.getcTotalSamples());
        dto.setcPositiveCases(sampleInfo.getcPositiveCases());
        dto.setcInfectionRate(sampleInfo.getcInfectionRate());
        dto.setcParvumPositiveCases(sampleInfo.getcParvumPositiveCases());
        dto.setcParvumInfectionRate(sampleInfo.getcParvumInfectionRate());
        dto.setcBovisPositiveCases(sampleInfo.getcBovisPositiveCases());
        dto.setcBovisInfectionRate(sampleInfo.getcBovisInfectionRate());
        dto.setcHominisPositiveCases(sampleInfo.getcHominisPositiveCases());
        dto.setcHominisInfectionRate(sampleInfo.getcHominisInfectionRate());
        dto.setcAnPositiveCases(sampleInfo.getcAnPositiveCases());
        dto.setcAnInfectionRate(sampleInfo.getcAnInfectionRate());
        dto.setcXiaoPositiveCases(sampleInfo.getcXiaoPositiveCases());
        dto.setcXiaoInfectionRate(sampleInfo.getcXiaoInfectionRate());
        dto.setcUbiquitumPositiveCases(sampleInfo.getcUbiquitumPositiveCases());
        dto.setcUbiquitumInfectionRate(sampleInfo.getcUbiquitumInfectionRate());
        dto.setMixedInfectionCases(sampleInfo.getMixedInfectionCases());
        dto.setOtherCPositiveCases(sampleInfo.getOtherCPositiveCases());


        // 转换 SubtypeDetail 列表
        List<SubtypeDetailDTO> subtypeDetails = sampleInfo.getSubtypeDetails().stream()
                .map(this::convertSubtypeDetailToDTO)
                .collect(Collectors.toList());
        dto.setSubtypeDetails(subtypeDetails);

        return dto;
    }

    private SubtypeDetailDTO convertSubtypeDetailToDTO(SubtypeDetail subtypeDetail) {
        SubtypeDetailDTO dto = new SubtypeDetailDTO();
        dto.setSubTypeName(subtypeDetail.getSubTypeName());
        dto.setSubtypePositiveCases(subtypeDetail.getSubtypePositiveCases());
        return dto;
    }

    // 删除信息
    @Override
    @Transactional // 添加事务注解
    public void deleteSampleInfo(String sampleId) {
        // 根据 sampleId 查找记录
        SampleInfo sampleInfo = sampleInfoRepository.findBySampleId(sampleId)
                .orElseThrow(() -> new RuntimeException("Sample not found"));

        // 删除关联的亚型信息
        subtypeDetailRepository.deleteBySampleInfo(sampleInfo);
        // 删除样本信息
        sampleInfoRepository.delete(sampleInfo);
    }

    // 更新信息
    @Override
    @Transactional // 添加事务注解
    public void updateSampleInfo(SampleInfoUpdateDTO updateDTO) {
        // 根据 sampleId 查找记录
        SampleInfo sampleInfo = sampleInfoRepository.findBySampleId(updateDTO.getSampleId())
                .orElseThrow(() -> new RuntimeException("Sample not found"));

        // 更新允许修改的字段
        sampleInfo.setProvince(updateDTO.getProvince());
        sampleInfo.setCity(updateDTO.getCity());
        sampleInfo.setSampleType(updateDTO.getSampleType());
        sampleInfo.setCollectionYear(updateDTO.getCollectionYear());
        sampleInfo.setHostSource(updateDTO.getHostSource());
        sampleInfo.setOutbreak(updateDTO.isOutbreak());
        sampleInfo.setDiarrhea(updateDTO.isDiarrhea());
        sampleInfo.setDetectionMethod(updateDTO.getDetectionMethod());
        sampleInfo.setAgeRangeStart(updateDTO.getAgeRangeStart());
        sampleInfo.setAgeRangeEnd(updateDTO.getAgeRangeEnd());
        sampleInfo.setcTotalSamples(updateDTO.getcTotalSamples());
        sampleInfo.setcPositiveCases(updateDTO.getcPositiveCases());
        sampleInfo.setcInfectionRate(updateDTO.getcInfectionRate());
        sampleInfo.setcParvumPositiveCases(updateDTO.getcParvumPositiveCases());
        sampleInfo.setcParvumInfectionRate(updateDTO.getcParvumInfectionRate());
        sampleInfo.setcBovisPositiveCases(updateDTO.getcBovisPositiveCases());
        sampleInfo.setcBovisInfectionRate(updateDTO.getcBovisInfectionRate());
        sampleInfo.setcHominisPositiveCases(updateDTO.getcHominisPositiveCases());
        sampleInfo.setcHominisInfectionRate(updateDTO.getcHominisInfectionRate());
        sampleInfo.setcAnPositiveCases(updateDTO.getcAnPositiveCases());
        sampleInfo.setcAnInfectionRate(updateDTO.getcAnInfectionRate());
        sampleInfo.setcXiaoPositiveCases(updateDTO.getcXiaoPositiveCases());
        sampleInfo.setcXiaoInfectionRate(updateDTO.getcXiaoInfectionRate());
        sampleInfo.setcUbiquitumPositiveCases(updateDTO.getcUbiquitumPositiveCases());
        sampleInfo.setcUbiquitumInfectionRate(updateDTO.getcUbiquitumInfectionRate());
        sampleInfo.setMixedInfectionCases(updateDTO.getMixedInfectionCases());
        sampleInfo.setOtherCPositiveCases(updateDTO.getOtherCPositiveCases());

        // 处理亚型信息
        if (updateDTO.getSubtypeDetails() != null) {
            for (SubtypeDetailUpdateDTO subtypeDetailDTO : updateDTO.getSubtypeDetails()) {
                if (subtypeDetailDTO.getId() == null) {
                    // 新增亚型
                    SubtypeDetail subtypeDetail = new SubtypeDetail();
                    subtypeDetail.setSubTypeName(subtypeDetailDTO.getSubTypeName());
                    subtypeDetail.setSubtypePositiveCases(subtypeDetailDTO.getSubtypePositiveCases());
                    subtypeDetail.setSampleInfo(sampleInfo);
                    subtypeDetailRepository.save(subtypeDetail);
                } else {
                    // 更新或删除亚型
                    Optional<SubtypeDetail> existingSubtypeDetail = subtypeDetailRepository.findById(subtypeDetailDTO.getId());
                    if (existingSubtypeDetail.isPresent()) {
                        SubtypeDetail subtypeDetail = existingSubtypeDetail.get();
                        if (subtypeDetailDTO.getSubtypePositiveCases() != null) {
                            // 更新现有亚型
                            subtypeDetail.setSubTypeName(subtypeDetailDTO.getSubTypeName());
                            subtypeDetail.setSubtypePositiveCases(subtypeDetailDTO.getSubtypePositiveCases());
                            subtypeDetailRepository.save(subtypeDetail);
                        } else {
                            // 删除亚型
                            subtypeDetailRepository.delete(subtypeDetail);
                        }
                    }
                }
            }
        }

        // 保存更新后的记录
        sampleInfoRepository.save(sampleInfo);
    }

    @Override
    public List<SampleInfoResponseDTO> findBySampleId(String sampleId) {
        List<SampleInfo> sampleInfos = sampleInfoRepository.findOneWithSubtypeDetails(sampleId);
        // 将实体对象转换为 DTO
        return sampleInfos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }


}
