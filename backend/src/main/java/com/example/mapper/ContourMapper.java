package com.example.mapper;


import com.example.pojo.Contour;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContourMapper {

    @Insert("INSERT INTO contour (name, category, img) VALUES (#{name}, #{category}, #{img})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Contour contour);

    @Select("SELECT * FROM contour")
    List<Contour> selectAll();

    @Delete("DELETE FROM contour WHERE id = #{id}")
    void deleteByPrimaryKey(Integer id);

    @Update("UPDATE contour SET name = #{name}, category = #{category}, img = #{img} WHERE id = #{id}")
    void updateByPrimaryKey(Contour contour);

    @Select("SELECT * FROM contour WHERE id = #{id}")
    Contour selectByPrimaryKey(Integer id);
}