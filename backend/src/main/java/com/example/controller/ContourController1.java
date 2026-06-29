package com.example.controller;


import com.example.common.Result;
import com.example.pojo.Contour1;
import com.example.service.ContourService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contour1")
@CrossOrigin(originPatterns = "*")
public class ContourController1 {
    @Autowired
    private ContourService1 contourService;

    @PostMapping("/add")
    public Result add(@RequestBody Contour1 contour) {
        contourService.add(contour);
        return Result.success();
    }

    /**
     * 查询所有轮廓图信息
     */
    @GetMapping("/selectAll")
    public Result<List<Contour1>> selectAll() {
        List<Contour1> contourList = contourService.selectAll();
        return Result.success(contourList);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        System.out.println("id "+id);
        contourService.delete(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Contour1 contour) {
        contourService.update(contour);
        return Result.success();
    }
}