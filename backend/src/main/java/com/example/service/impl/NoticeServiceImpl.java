package com.example.service.impl;

import com.example.mapper.NoticeMapper;
import com.example.pojo.Notice;
import com.example.pojo.PageBean;
import com.example.service.NoticeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public PageBean<Notice> list(Integer pageNum,Integer pageSize) {
        PageBean<Notice> pg = new PageBean<>();

        PageHelper.startPage(pageNum,pageSize);
        List<Notice> ln = noticeMapper.list();
        Page<Notice> p = (Page<Notice>) ln;

        pg.setTotal(p.getTotal());
        pg.setItems(p.getResult());
        return pg;
    }

    @Override
    public void add(Notice notice) {
        notice.setDate(LocalDate.now());
        noticeMapper.add(notice);
    }

    @Override
    public List<Notice> selectAll() {
        List<Notice> ln = noticeMapper.list();
        return ln.stream()
                .sorted(Comparator.comparing(Notice::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        noticeMapper.delete(id);
    }

    @Override
    public void update(Notice notice) {
        noticeMapper.update(notice);
    }

    @Override
    public Notice getOne(Integer id) {
        Notice n = noticeMapper.getOne(id);
        return n;
    }
}
