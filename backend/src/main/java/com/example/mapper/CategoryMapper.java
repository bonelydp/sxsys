package com.example.mapper;



import com.example.pojo.Year;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    //查询所有
    @Select("select * from category")
    List<Year> list();

    @Insert("insert into category(title,year,create_time,update_time) values (#{title},#{year},#{createTime},#{updateTime})")
    void add(Year year);

    @Update("update category set title=#{title},year=#{year},update_time=#{updateTime} where id=#{id}")
    void update(Year year);

    @Delete("delete from category where id=#{id}")
    void delete(Integer id);
}
