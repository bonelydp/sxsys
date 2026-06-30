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
    public Result<List<PageView>> record(@RequestParam("type") String type) {
        System.out.println("PageView record called, type=" + type);
        List<PageView> list = pageViewService.recordAndGet(type);
        System.out.println("PageView result: " + list);
        return Result.success(list);
    }
}
