package com.example.mapper;


import com.example.pojo.TeamIntroduction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamIntroductionMapper {

    /**
     * 插入团队成员介绍信息，包括category字段
     */
    @Insert("INSERT INTO teamintroduction (name, avatarurl, introduction, category) VALUES (#{name}, #{avatarUrl}, #{introduction}, #{category})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TeamIntroduction teamIntroduction);

    /**
     * 查询所有团队成员介绍信息，包括category字段
     */
    @Select("SELECT * FROM teamintroduction")
    List<TeamIntroduction> selectAll();

    /**
     * 根据ID删除团队成员介绍信息
     */
    @Delete("DELETE FROM teamintroduction WHERE id = #{id}")
    void deleteByPrimaryKey(Integer id);

    /**
     * 更新团队成员介绍信息，包括category字段
     */
    @Update("UPDATE teamintroduction SET name = #{name}, avatarurl = #{avatarUrl}, introduction = #{introduction}, category = #{category} WHERE id = #{id}")
    void updateByPrimaryKey(TeamIntroduction teamIntroduction);

    /**
     * 根据ID查询团队成员介绍信息，包括category字段
     */
    @Select("SELECT * FROM teamintroduction WHERE id = #{id}")
    TeamIntroduction selectByPrimaryKey(Integer id);

    @Select("SELECT * FROM teamintroduction WHERE category = #{category}")
    List<TeamIntroduction> selectByCategory(String category);
}