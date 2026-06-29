package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sampledata") // 修改表名为SampleData
public class SampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 唯一标识符，自增主键
    private Integer id; // 修改类型为Integer

    // 样本编号
    @Column(name = "sample_number") // 添加列名映射
    private String sampleNumber;

    // 国家
    @Column(name = "country")
    private String country;

    // 省份
    @Column(name = "province")
    private String province;

    // 城市
    @Column(name = "city")
    private String city;

    // 区县
    @Column(name = "district")
    private String district;

    // 详细地址
    @Column(name = "address_details") // 修改列名映射
    private String addressDetails;

    // 样本类型
    public enum SampleType {
        粪便,
        其他
    }
    // 样本类型
    @Column(name = "sample_type")
    @Enumerated(EnumType.STRING)
    private SampleType sampleType;

    // 其他样本类型
  /*  @Column(name = "other_sample_type")
    private String otherSampleType;*/

    // 采集时间
    @Column(name = "collection_time")
    private LocalDate collectionTime; // 修改类型为LocalDate

    // 样本储存条件
    @Column(name = "storage_conditions")
    private String storageConditions;

    /*// 样本送检人/单位
    @Column(name = "sender")
    private String sender;*/
    //是否暴发
    public enum OutbreakStatus {
        是,
        否,
        不确定
    }
    // 暴发情况的开始时间
    @Column(name = "start_time")
    private LocalDate startTime; // 修改类型为LocalDate

    // 暴发情况的结束时间
    @Column(name = "end_time")
    private LocalDate endTime; // 修改类型为LocalDate

    // 报告时间
    @Column(name = "report_time")
    private LocalDate reportTime; // 修改类型为LocalDate

    // 病畜数
    @Column(name = "disease_count")
    private Integer diseaseCount;

    // 死亡数
    @Column(name = "death_count")
    private Integer deathCount;

    // 宿主来源
    @Column(name = "host_source")
    private String hostSource;

    // 宿主性别
    @Column(name = "gender")
    private String gender;

    // 是否腹泻
    @Column(name = "diarrhea")
    private String diarrhea;

    // 腹泻类型
    @Column(name = "diarrhea_type")
    private String diarrheaType;

    // 是否死亡
    @Column(name = "death")
    private String death;

    // 宿主具体品种
    @Column(name = "host_species")
    private String hostSpecies;

    // 月龄/年龄
    @Column(name = "age")
    private Integer age;

    // 其他症状
    @Column(name = "other_symptoms")
    private String otherSymptoms;

    // 免疫力
    @Column(name = "immunity")
    private String immunity;

   /* // 显微镜检查结果
    @Column(name = "microscopy_result")
    private String microscopyResult;

    // SSU rRNA PCR 检测结果
    @Column(name = "ssu_rna_pcr")
    private String ssuRnaPcr;

    // GP60 PCR 检测结果
    @Column(name = "gp60_pcr")
    private String gp60Pcr;

    // qPCR Ct值
    @Column(name = "qpcr_ct_value")
    private BigDecimal qpcrCtValue;
*/

    // 定义检测方法枚举
    public enum DetectionMethod {
        SSU_rRNA_PCR, gp60_PCR, 显微镜检测, 其他
    }

    // 新增字段：检测方法
    @Column(name = "detection_method")
    @Enumerated(EnumType.STRING)
    private DetectionMethod detectionMethod;

    // getters and setters for detection_method

    public DetectionMethod getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(DetectionMethod detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    //输入检测方法
    @Column(name = "detection_methodnew")
    private String detectionMethodnew; // 修改字段名为insectSpecies

    // 虫种
    @Column(name = "insect_species")
    private String insectSpecies; // 修改字段名为insectSpecies

    // 亚型
    @Column(name = "subtype")
    private String subtype;

    //上传镜检图片
    // 新增字段：镜检图片URL
    @Column(name = "jingpicture")  // 如果允许NULL，请移除nullable = false
    private String jingpicture;

    //状态
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    public SampleEntity() {
        this.status = Status.PENDING; // 默认状态为 PENDING
    }

    public enum Status {
        PENDING("待审核"),
        APPROVED("已通过"),
        REJECTED("已拒绝");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // Getter and Setter for other fields...

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // getters and setters for jingpicture

    public String getJingpicture() {
        return jingpicture;
    }

    public void setJingpicture(String jingpicture) {
        this.jingpicture = jingpicture;
    }



   /* // 上传的文件名
    @Column(name = "file")
    private String file;*/


    // 上传的文件本身
    @Column(name = "file")
    private String file; // ]


    // 选择的标准序列
    @Column(name = "selected_standard_sequence")
    private String selectedStandardSequence;

    // getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    // 暴发情况的是否暴发
    @Column(name = "outbreak")
    @Enumerated(EnumType.STRING)
    private OutbreakStatus outbreak;

// getters and setters

    public OutbreakStatus getOutbreak() {
        return outbreak;
    }

    public void setOutbreak(OutbreakStatus outbreak) {
        this.outbreak = outbreak;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

 /*   public String getOtherSampleType() {
        return otherSampleType;
    }

    public void setOtherSampleType(String otherSampleType) {
        this.otherSampleType = otherSampleType;
    }*/

    public LocalDate getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(LocalDate collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

   /* public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }*/

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public LocalDate getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDate reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getDiseaseCount() {
        return diseaseCount;
    }

    public void setDiseaseCount(Integer diseaseCount) {
        this.diseaseCount = diseaseCount;
    }

    public Integer getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(Integer deathCount) {
        this.deathCount = deathCount;
    }

    public String getHostSource() {
        return hostSource;
    }

    public void setHostSource(String hostSource) {
        this.hostSource = hostSource;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(String diarrhea) {
        this.diarrhea = diarrhea;
    }

    public String getDiarrheaType() {
        return diarrheaType;
    }

    public void setDiarrheaType(String diarrheaType) {
        this.diarrheaType = diarrheaType;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getHostSpecies() {
        return hostSpecies;
    }

    public void setHostSpecies(String hostSpecies) {
        this.hostSpecies = hostSpecies;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public String getImmunity() {
        return immunity;
    }

    public void setImmunity(String immunity) {
        this.immunity = immunity;
    }

   /* public String getMicroscopyResult() {
        return microscopyResult;
    }

    public void setMicroscopyResult(String microscopyResult) {
        this.microscopyResult = microscopyResult;
    }

    public String getSsuRnaPcr() {
        return ssuRnaPcr;
    }

    public void setSsuRnaPcr(String ssuRnaPcr) {
        this.ssuRnaPcr = ssuRnaPcr;
    }

    public String getGp60Pcr() {
        return gp60Pcr;
    }

    public void setGp60Pcr(String gp60Pcr) {
        this.gp60Pcr = gp60Pcr;
    }

    public BigDecimal getQpcrCtValue() {
        return qpcrCtValue;
    }

    public void setQpcrCtValue(BigDecimal qpcrCtValue) {
        this.qpcrCtValue = qpcrCtValue;
    }*/

    public String getDetectionMethodnew() {
        return detectionMethodnew;
    }

    public void setDetectionMethodnew(String detectionMethodnew) {
        this.detectionMethodnew = detectionMethodnew;
    }


    public String getInsectSpecies() {
        return insectSpecies;
    }

    public void setInsectSpecies(String insectSpecies) {
        this.insectSpecies = insectSpecies;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


   /* public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }*/


    @Column(name = "user_id")
    @JsonIgnore  // 这个注解可以防止该字段被转成 JSON 输出
    private Integer userId;

    @Transient // 标记此字段不会被持久化到数据库
    private String username;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSelectedStandardSequence() {
        return selectedStandardSequence;
    }

    public void setSelectedStandardSequence(String selectedStandardSequence) {
        this.selectedStandardSequence = selectedStandardSequence;
    }
}