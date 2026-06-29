package com.example.controller;



import com.example.pojo.Contour;

import com.example.pojo.Result;
import com.example.service.ContourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contour")
@CrossOrigin(originPatterns = "*")
public class ContourController {
    @Autowired
    private ContourService contourService;

    @PostMapping("/add")
    public Result add(@RequestBody Contour contour) {
        contourService.add(contour);
        return Result.success();
    }

    /**
     * 查询所有轮廓图信息
     */
    @GetMapping("/selectAll")
    public Result<List<Contour>> selectAll() {
        List<Contour> contourList = contourService.selectAll();
        return Result.success(contourList);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        System.out.println("id->"+id);
        contourService.delete(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Contour contour) {
        contourService.update(contour);
        return Result.success();
    }
}