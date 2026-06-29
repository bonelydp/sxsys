package com.example.controller;

import com.example.pojo.Notice;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    //分页
    @GetMapping("/list")
    public Result<PageBean<Notice>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageBean<Notice> nt =  noticeService.list(pageNum,pageSize);
        return Result.success(nt);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        noticeService.add(notice);
        return Result.success();
    }

    //获取全部
    @GetMapping("/selectAll")
    public Result<List<Notice>> selectAll() {
        List<Notice> list = noticeService.selectAll();
        return Result.success(list);
    }

    //获取单条通知
    @GetMapping("/getOne")
    public Result<Notice> getOne(@RequestParam("id") Integer id){
        System.out.println("id"+id);
        Notice n = noticeService.getOne(id);
        return Result.success(n);
    }

    @DeleteMapping( "/delete")
    public Result delete(@RequestParam("id") Integer id){
        noticeService.delete(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Notice notice){
        noticeService.update(notice);
        return Result.success();
    }
}
