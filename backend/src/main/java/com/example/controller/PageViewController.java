package com.example.controller;

import com.example.pojo.PageView;
import com.example.pojo.Result;
import com.example.service.PageViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pageview")
public class PageViewController {

    @Autowired
    private PageViewService pageViewService;

    @GetMapping("/record")
    public Result<List<PageView>> recordGet(@RequestParam("type") String type) {
        System.out.println("PageView GET called, type=" + type);
        List<PageView> list = pageViewService.recordAndGet(type);
        System.out.println("PageView result: " + list);
        return Result.success(list);
    }

    @PostMapping("/record")
    public Result<List<PageView>> recordPost(@RequestParam("type") String type) {
        System.out.println("PageView POST called, type=" + type);
        List<PageView> list = pageViewService.recordAndGet(type);
        System.out.println("PageView result: " + list);
        return Result.success(list);
    }
}
