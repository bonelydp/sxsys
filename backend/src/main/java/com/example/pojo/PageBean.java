package com.example.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T> {
    private Long total;
    private List<T> items;
}
