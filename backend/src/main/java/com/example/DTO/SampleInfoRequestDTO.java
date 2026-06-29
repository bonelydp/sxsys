package com.example.DTO;

import java.util.ArrayList;
import java.util.List;

// 新增响应
public class SampleInfoRequestDTO {

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
    private int cTotalSamples = 0;
    private int cPositiveCases = 0;
    private double cInfectionRate = 0.0;
    private Integer cParvumPositiveCases = 0;
    private Double cParvumInfectionRate = 0.0;
    private Integer cBovisPositiveCases = 0;
    private Double cBovisInfectionRate = 0.0;
    private Integer cHominisPositiveCases = 0;
    private Double cHominisInfectionRate = 0.0;
    private Integer cAnPositiveCases = 0;
    private Double cAnInfectionRate = 0.0;
    private Integer cXiaoPositiveCases = 0;
    private Double cXiaoInfectionRate = 0.0;
    private Integer cUbiquitumPositiveCases = 0;
    private Double cUbiquitumInfectionRate = 0.0;
    private Integer mixedInfectionCases = 0;
    private Integer otherCPositiveCases = 0;


    private List<SubtypeDetailDTO> subtypeDetails = new ArrayList<>();;

    // Getters and Setters...


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

    public List<SubtypeDetailDTO> getSubtypeDetails() {
        return subtypeDetails;
    }

    public void setSubtypeDetails(List<SubtypeDetailDTO> subtypeDetails) {
        this.subtypeDetails = subtypeDetails;
    }

    @Override
    public String toString() {
        return "SampleInfoRequestDTO{" +
                "sampleId='" + sampleId + '\'' +
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