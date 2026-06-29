package com.example.controller;


import com.example.pojo.Result;
import com.example.pojo.Year;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    public Result<List<Year>> list(){
        List<Year> cs = categoryService.list();
        System.out.println("cs->"+cs);
        return Result.success(cs);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Year year){
        categoryService.add(year);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Year year){
        categoryService.update(year);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam("id") Integer id){
        categoryService.delete(id);
        return Result.success();
    }

}
