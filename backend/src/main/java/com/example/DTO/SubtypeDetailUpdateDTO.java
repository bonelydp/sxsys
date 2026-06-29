package com.example.DTO;

public class SubtypeDetailUpdateDTO {
    private Long id; // 如果需要更新特定的亚型记录，则需要提供其ID
    private String subTypeName;
    private Integer subtypePositiveCases;

    // Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
