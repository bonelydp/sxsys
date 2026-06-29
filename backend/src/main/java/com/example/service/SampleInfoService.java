package com.example.service;

import com.example.DTO.SampleInfoRequestDTO;
import com.example.DTO.SampleInfoResponseDTO;
import com.example.DTO.SampleInfoUpdateDTO;
import com.example.pojo.SampleInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SampleInfoService {


    void saveSampleInfo(SampleInfo sampleInfo);

    List<Integer> getYear();

    List<Object[]> findByYearAndHostWithInfectionRate(int year,String host);

    List<Object[]> findByYearAndHostWithDetails(int year,String host);

    List<Object[]> findByProvinceAndYearAndHostWithInfectionRate(int year,String host,String province);

    List<Object[]> findByYearAndHostWithProvinceAndInfectionCount(int year,String host);

    List<Object[]> findByProvinceAndYearWithDetails(int year,String province);

    List<Object[]> findByProvinceAndYearAndHostWithCityAndInfectionCount(int year,String province,String host);

    // CURD
    // 实体转换
    SampleInfo convertToEntity(SampleInfoRequestDTO dto);
    // 新增信息 单个
    void addSampleInfoWithSubtypes(SampleInfo sampleInfo);

    // 批量增加信息 Excel
    void batchAddSampleInfos(List<SampleInfo> sampleInfos);


    // 查询信息
    Page<SampleInfoResponseDTO> getAllSampleInfoWithSubtypes(Integer currentPage, Integer pageSize);

    // 删除信息
    void deleteSampleInfo(String sampleId);

    void updateSampleInfo(SampleInfoUpdateDTO updateDTO);


    List<SampleInfoResponseDTO> findBySampleId(String sampleId);

    List<Object[]> findAllInfectionCount();

    List<Object[]> findCountOfSampleAndHost();

    List<String> getHost();
}