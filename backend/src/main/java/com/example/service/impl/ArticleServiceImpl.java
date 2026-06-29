package com.example.service.impl;

import com.example.mapper.ArticleMapper;
import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize,Integer year) {
        //
        PageBean<Article> pb = new PageBean<>();
        if(pageNum != null || pageSize != null){
            //开启分页查询
            PageHelper.startPage(pageNum,pageSize);
            List<Article> as = articleMapper.list(year);
            //Page中提供了方法，可以获取PageHelper分页查询后得到的《总记录条数》和《当前页数据》
            Page<Article> p = (Page<Article>) as;
            pb.setTotal(p.getTotal());
            pb.setItems(p.getResult());
        }else{
            List<Article> as = articleMapper.list(year);
            pb.setTotal((long)as.size());
            pb.setItems(as);
        }
        return pb;
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }


}
