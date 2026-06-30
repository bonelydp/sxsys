package com.example.service.impl;

import com.example.mapper.PageViewMapper;
import com.example.pojo.PageView;
import com.example.service.PageViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PageViewServiceImpl implements PageViewService {
    @Autowired
    private PageViewMapper pageViewMapper;

    @Override
    @Transactional
    public List<PageView> recordAndGet(String viewType) {
        pageViewMapper.increment(viewType);
        return pageViewMapper.selectAll();
    }
}
