package com.example.service;


import com.example.mapper.TeamIntroductionMapper;
import com.example.pojo.TeamIntroduction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeamIntroductionService {
    @Autowired
    private TeamIntroductionMapper teamIntroductionMapper;

    /**
     * 添加团队成员介绍信息
     * @param teamIntroduction 团队成员介绍实体
     */
    public void add(TeamIntroduction teamIntroduction) {
        teamIntroductionMapper.insert(teamIntroduction);
    }

    /**
     * 查询所有团队成员介绍信息
     * @return 团队成员介绍列表
     */
    public List<TeamIntroduction> selectAll() {
        return teamIntroductionMapper.selectAll();
    }

    /**
     * 根据ID删除团队成员介绍信息
     * @param id 团队成员介绍ID
     */
    public void delete(Integer id) {
        teamIntroductionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新团队成员介绍信息
     * @param teamIntroduction 团队成员介绍实体
     */
    public void update(TeamIntroduction teamIntroduction) {
        teamIntroductionMapper.updateByPrimaryKey(teamIntroduction);
    }

    /**
     * 根据ID查询团队成员介绍信息
     * @param id 团队成员介绍ID
     * @return 团队成员介绍实体或null（如果找不到）
     */
    public TeamIntroduction selectById(Integer id) {
        return teamIntroductionMapper.selectByPrimaryKey(id);
    }

    public List<TeamIntroduction> selectByCategory(String category) {
        return teamIntroductionMapper.selectByCategory(category);
    }

}