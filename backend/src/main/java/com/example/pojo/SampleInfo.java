package com.example.pojo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sample_info")
public class SampleInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 逻辑外键字段
    @Column(name = "sample_id", unique = true, nullable = false)
    private String sampleId;

    private String province;
    private String city;
    private String sampleType;
    private int collectionYear;
    private String hostSource;
    private boolean outbreak;
    private boolean diarrhea;
    private String detectionMethod;
    private Integer ageRangeStart;
    private Integer ageRangeEnd;

    // 隐孢子虫-总数据
    private int cTotalSamples;
    private int cPositiveCases;
    private double cInfectionRate;

    // 微小隐孢子虫
    private Integer cParvumPositiveCases;
    private Double cParvumInfectionRate;

    // 牛隐孢子虫
    private Integer cBovisPositiveCases;
    private Double cBovisInfectionRate;

    // 芮氏隐孢子虫
    private Integer cHominisPositiveCases;
    private Double cHominisInfectionRate;

    // 安氏隐孢子虫
    private Integer cAnPositiveCases;
    private Double cAnInfectionRate;

    // 肖氏隐孢子虫
    private Integer cXiaoPositiveCases;
    private Double cXiaoInfectionRate;

    // 泛在隐孢子虫
    private Integer cUbiquitumPositiveCases;
    private Double cUbiquitumInfectionRate;

    // 混合感染数
    private Integer mixedInfectionCases;

    // 其他隐孢子虫
    @Column(name = "other_c_positive_cases")
    private Integer otherCPositiveCases;


    @OneToMany(mappedBy = "sampleInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubtypeDetail> subtypeDetails = new ArrayList<>();

    // Getters and Setters

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public int getCollectionYear() {
        return collectionYear;
    }

    public void setCollectionYear(int collectionYear) {
        this.collectionYear = collectionYear;
    }

    public String getHostSource() {
        return hostSource;
    }

    public void setHostSource(String hostSource) {
        this.hostSource = hostSource;
    }

    public boolean isOutbreak() {
        return outbreak;
    }

    public void setOutbreak(boolean outbreak) {
        this.outbreak = outbreak;
    }

    public boolean isDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(boolean diarrhea) {
        this.diarrhea = diarrhea;
    }

    public String getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(String detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public Integer getAgeRangeStart() {
        return ageRangeStart;
    }

    public void setAgeRangeStart(Integer ageRangeStart) {
        this.ageRangeStart = ageRangeStart;
    }

    public Integer getAgeRangeEnd() {
        return ageRangeEnd;
    }

    public void setAgeRangeEnd(Integer ageRangeEnd) {
        this.ageRangeEnd = ageRangeEnd;
    }

    public int getcTotalSamples() {
        return cTotalSamples;
    }

    public void setcTotalSamples(int cTotalSamples) {
        this.cTotalSamples = cTotalSamples;
    }

    public int getcPositiveCases() {
        return cPositiveCases;
    }

    public void setcPositiveCases(int cPositiveCases) {
        this.cPositiveCases = cPositiveCases;
    }

    public double getcInfectionRate() {
        return cInfectionRate;
    }

    public void setcInfectionRate(double cInfectionRate) {
        this.cInfectionRate = cInfectionRate;
    }

    public Integer getcParvumPositiveCases() {
        return cParvumPositiveCases;
    }

    public void setcParvumPositiveCases(Integer cParvumPositiveCases) {
        this.cParvumPositiveCases = cParvumPositiveCases;
    }

    public Double getcParvumInfectionRate() {
        return cParvumInfectionRate;
    }

    public void setcParvumInfectionRate(Double cParvumInfectionRate) {
        this.cParvumInfectionRate = cParvumInfectionRate;
    }

    public Integer getcBovisPositiveCases() {
        return cBovisPositiveCases;
    }

    public void setcBovisPositiveCases(Integer cBovisPositiveCases) {
        this.cBovisPositiveCases = cBovisPositiveCases;
    }

    public Double getcBovisInfectionRate() {
        return cBovisInfectionRate;
    }

    public void setcBovisInfectionRate(Double cBovisInfectionRate) {
        this.cBovisInfectionRate = cBovisInfectionRate;
    }

    public Integer getcHominisPositiveCases() {
        return cHominisPositiveCases;
    }

    public void setcHominisPositiveCases(Integer cHominisPositiveCases) {
        this.cHominisPositiveCases = cHominisPositiveCases;
    }

    public Double getcHominisInfectionRate() {
        return cHominisInfectionRate;
    }

    public void setcHominisInfectionRate(Double cHominisInfectionRate) {
        this.cHominisInfectionRate = cHominisInfectionRate;
    }

    public Integer getcAnPositiveCases() {
        return cAnPositiveCases;
    }

    public void setcAnPositiveCases(Integer cAnPositiveCases) {
        this.cAnPositiveCases = cAnPositiveCases;
    }

    public Double getcAnInfectionRate() {
        return cAnInfectionRate;
    }

    public void setcAnInfectionRate(Double cAnInfectionRate) {
        this.cAnInfectionRate = cAnInfectionRate;
    }

    public Integer getcXiaoPositiveCases() {
        return cXiaoPositiveCases;
    }

    public void setcXiaoPositiveCases(Integer cXiaoPositiveCases) {
        this.cXiaoPositiveCases = cXiaoPositiveCases;
    }

    public Double getcXiaoInfectionRate() {
        return cXiaoInfectionRate;
    }

    public void setcXiaoInfectionRate(Double cXiaoInfectionRate) {
        this.cXiaoInfectionRate = cXiaoInfectionRate;
    }

    public Integer getcUbiquitumPositiveCases() {
        return cUbiquitumPositiveCases;
    }

    public void setcUbiquitumPositiveCases(Integer cUbiquitumPositiveCases) {
        this.cUbiquitumPositiveCases = cUbiquitumPositiveCases;
    }

    public Double getcUbiquitumInfectionRate() {
        return cUbiquitumInfectionRate;
    }

    public void setcUbiquitumInfectionRate(Double cUbiquitumInfectionRate) {
        this.cUbiquitumInfectionRate = cUbiquitumInfectionRate;
    }

    public Integer getMixedInfectionCases() {
        return mixedInfectionCases;
    }

    public void setMixedInfectionCases(Integer mixedInfectionCases) {
        this.mixedInfectionCases = mixedInfectionCases;
    }

    public Integer getOtherCPositiveCases() {
        return otherCPositiveCases;
    }

    public void setOtherCPositiveCases(Integer otherCPositiveCases) {
        this.otherCPositiveCases = otherCPositiveCases;
    }


    public List<SubtypeDetail> getSubtypeDetails() {
        return subtypeDetails;
    }

    public void setSubtypeDetails(List<SubtypeDetail> subtypeDetails) {
        this.subtypeDetails = subtypeDetails;
    }

    @Override
    public String toString() {
        return "SampleInfo{" +
                "id=" + id +
                ", sampleId='" + sampleId + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", sampleType='" + sampleType + '\'' +
                ", collectionYear=" + collectionYear +
                ", hostSource='" + hostSource + '\'' +
                ", outbreak=" + outbreak +
                ", diarrhea=" + diarrhea +
                ", detectionMethod='" + detectionMethod + '\'' +
                ", ageRangeStart=" + ageRangeStart +
                ", ageRangeEnd=" + ageRangeEnd +
                ", cTotalSamples=" + cTotalSamples +
                ", cPositiveCases=" + cPositiveCases +
                ", cInfectionRate=" + cInfectionRate +
                ", cParvumPositiveCases=" + cParvumPositiveCases +
                ", cParvumInfectionRate=" + cParvumInfectionRate +
                ", cBovisPositiveCases=" + cBovisPositiveCases +
                ", cBovisInfectionRate=" + cBovisInfectionRate +
                ", cHominisPositiveCases=" + cHominisPositiveCases +
                ", cHominisInfectionRate=" + cHominisInfectionRate +
                ", cAnPositiveCases=" + cAnPositiveCases +
                ", cAnInfectionRate=" + cAnInfectionRate +
                ", cXiaoPositiveCases=" + cXiaoPositiveCases +
                ", cXiaoInfectionRate=" + cXiaoInfectionRate +
                ", cUbiquitumPositiveCases=" + cUbiquitumPositiveCases +
                ", cUbiquitumInfectionRate=" + cUbiquitumInfectionRate +
                ", mixedInfectionCases=" + mixedInfectionCases +
                ", otherCPositiveCases=" + otherCPositiveCases +
                ", subtypeDetails=" + subtypeDetails +
                '}';
    }
}