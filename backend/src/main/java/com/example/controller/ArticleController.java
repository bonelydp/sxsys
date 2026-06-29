package com.example.controller;

import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody Article article){
        articleService.add(article);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<PageBean<Article>> list(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer year){
        PageBean<Article> articleList = articleService.list(pageNum,pageSize,year);
        System.out.println("articleList->"+articleList);
        return Result.success(articleList);
    }

    @DeleteMapping("/delete{id}")
    public Result delete(@RequestParam("id") Integer id){
        articleService.delete(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Article article){
        articleService.update(article);
        return Result.success();
    }


}
