package com.example.service;

import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ArticleService {
    void add(Article article);

    void delete(Integer id);

    void update(Article article);


    PageBean<Article> list(Integer pageNum, Integer pageSize,Integer year);
}
