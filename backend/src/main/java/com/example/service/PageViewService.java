package com.example.service;

import com.example.pojo.PageView;

import java.util.List;

public interface PageViewService {

    List<PageView> recordAndGet(String viewType);
}
