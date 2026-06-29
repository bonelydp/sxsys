package com.example.controller;


import com.example.common.Result;
import com.example.pojo.TeamIntroduction;
import com.example.service.TeamIntroductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teamintroduction")
@CrossOrigin(originPatterns = "*")
public class TeamIntroductionController {
    @Autowired
    private TeamIntroductionService teamIntroductionService;

    /**
     * 添加团队成员介绍信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody TeamIntroduction teamIntroduction) {
        teamIntroductionService.add(teamIntroduction);
        return Result.success();
    }

    /**
     * 查询所有团队成员介绍信息
     */
    @GetMapping("/selectAll")
    public Result<List<TeamIntroduction>> selectAll() {
        List<TeamIntroduction> teamIntroductionList = teamIntroductionService.selectAll();
        return Result.success(teamIntroductionList);
    }

    /**
     * 根据ID删除团队成员介绍信息
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam("id") Integer id) {
        teamIntroductionService.delete(id);
        return Result.success();
    }

    /**
     * 更新团队成员介绍信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody TeamIntroduction teamIntroduction) {
        teamIntroductionService.update(teamIntroduction);
        return Result.success();
    }

    /**
     * 根据ID查询团队成员介绍信息
     */
    @GetMapping("/selectById/{id}")
    public Result<TeamIntroduction> selectById(@PathVariable("id") Integer id) {
        TeamIntroduction teamIntroduction = teamIntroductionService.selectById(id);
        if (teamIntroduction != null) {
            return Result.success(teamIntroduction);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/selectByCategory")
    public Result<List<TeamIntroduction>> selectByCategory(@RequestParam("category") String category) {
        System.out.println("category " + category);
        List<TeamIntroduction> teamIntroductionList = teamIntroductionService.selectByCategory(category);
        return Result.success(teamIntroductionList);
    }

}