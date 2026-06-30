package com.example.pojo;

import lombok.Data;

@Data
public class PageView {
    private Integer id;
    private String viewType;
    private Long count;
}
