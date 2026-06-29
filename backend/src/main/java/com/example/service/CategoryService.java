package com.example.service;


import com.example.pojo.Year;

import javax.swing.*;
import java.util.List;

public interface CategoryService {


    //列表查询
    List<Year> list();

    //新增
    void add(Year year);

    void update(Year year);

    void delete(Integer id);
}
