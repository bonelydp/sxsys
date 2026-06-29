package com.example.DTO;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleInfoExcelDTO {

    @ExcelProperty(index = 0)
    private String sampleId;
    @ExcelProperty(index = 1)
    private String province;
    @ExcelProperty(index = 2)
    private String city;
    @ExcelProperty(index = 3)
    private String sampleType;
    @ExcelProperty(index = 4)
    private int CollectionYear;
    @ExcelProperty(index = 5)
    private String hostSource;
    @ExcelProperty(index = 6)
    private String outbreak;
    @ExcelProperty(index = 7)
    private String diarrhea;
    @ExcelProperty(index = 8)
    private String detectionMethod;
    @ExcelProperty(index = 9)
    private int ageRangeStart;
    @ExcelProperty(index = 10)
    private int ageRangeEnd;
    @ExcelProperty(index = 11)
    private int cTotalSamples;
    @ExcelProperty(index = 12)
    private int cPositiveCases;
    @ExcelProperty(index = 13)
    private double cInfectionRate;
    @ExcelProperty(index = 14)
    private Integer cParvumPositiveCases;
    @ExcelProperty(index = 15)
    private Double cParvumInfectionRate;
    @ExcelProperty(index = 16)
    private Integer cBovisPositiveCases;
    @ExcelProperty(index = 17)
    private Double cBovisInfectionRate;
    @ExcelProperty(index = 18)
    private Integer cHominisPositiveCases;
    @ExcelProperty(index = 19)
    private Double cHominisInfectionRate;
    @ExcelProperty(index = 20)
    private Integer cAnPositiveCases;
    @ExcelProperty(index = 21)
    private Double cAnInfectionRate;
    @ExcelProperty(index = 22)
    private Integer cXiaoPositiveCases;
    @ExcelProperty(index = 23)
    private Double cXiaoInfectionRate;
    @ExcelProperty(index = 24)
    private Integer cUbiquitumPositiveCases;
    @ExcelProperty(index = 25)
    private Double cUbiquitumInfectionRate;
    @ExcelProperty(index = 26)
    private Integer mixedInfectionCases;
    @ExcelProperty(index = 27)
    private Integer otherCPositiveCases;

    // 第29列开始亚型信息（28）
    // 动态字段：用于存储亚型字段
    private String dynamicFields ;
}