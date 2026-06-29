package com.example.pojo;

import java.util.List;

public class PageResult {
    private int total;
    private int page;
    private int size;
    private List<SampleEntity> data;

    public PageResult(int total, int page, int size, List<SampleEntity> data) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<SampleEntity> getData() {
        return data;
    }

    public void setData(List<SampleEntity> data) {
        this.data = data;
    }

    // Getter and Setter
}