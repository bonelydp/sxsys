package com.example.service.impl;

import com.example.mapper.CategoryMapper;

import com.example.pojo.Year;
import com.example.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.time.LocalDate;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Year> list() {
        return categoryMapper.list();
    }

    @Override
    public void add(Year year) {
        year.setCreateTime(LocalDate.now());
        year.setUpdateTime(LocalDate.now());
        categoryMapper.add(year);
    }

    @Override
    public void update(Year year) {
        year.setUpdateTime(LocalDate.now());
        categoryMapper.update(year);
    }

    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}
