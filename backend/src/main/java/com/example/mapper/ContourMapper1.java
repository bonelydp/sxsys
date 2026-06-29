package com.example.mapper;


import com.example.pojo.Contour;
import com.example.pojo.Contour1;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContourMapper1 {

    @Insert("INSERT INTO contour1 (name, img) VALUES (#{name}, #{img})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Contour1 contour);

    @Select("SELECT * FROM contour1")
    List<Contour1> selectAll();

    @Delete("DELETE FROM contour1 WHERE id = #{id}")
    void deleteByPrimaryKey(Integer id);

    @Update("UPDATE contour1 SET name = #{name}, img = #{img} WHERE id = #{id}")
    void updateByPrimaryKey(Contour1 contour);

    @Select("SELECT * FROM contour WHERE id = #{id}")
    Contour selectByPrimaryKey(Integer id);
}