package com.example.pojo;
import jakarta.persistence.*;

@Entity
@Table(name = "subtype_detail")
public class SubtypeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String subTypeName;
    private Integer subtypePositiveCases;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id",referencedColumnName = "sample_id")
    private SampleInfo sampleInfo;


    // Getters and Setters

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public Integer getSubtypePositiveCases() {
        return subtypePositiveCases;
    }

    public void setSubtypePositiveCases(Integer subtypePositiveCases) {
        this.subtypePositiveCases = subtypePositiveCases;
    }

    public SampleInfo getSampleInfo() {
        return sampleInfo;
    }

    public void setSampleInfo(SampleInfo sampleInfo) {
        this.sampleInfo = sampleInfo;
    }

    @Override
    public String toString() {
        return "SubtypeDetail{" +
                "id=" + id +
                ", subTypeName='" + subTypeName + '\'' +
                ", subtypePositiveCases=" + subtypePositiveCases +
                ", sampleInfo=" + sampleInfo +
                '}';
    }
}