package com.example.service;

import com.example.pojo.Notice;
import com.example.pojo.PageBean;

import java.util.List;

public interface NoticeService {

    PageBean<Notice> list(Integer pageNum,Integer pageSize);

    void add(Notice notice);

    List<Notice> selectAll();

    void delete(Integer id);

    void update(Notice notice);

    Notice getOne(Integer id);
}
