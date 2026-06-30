package com.example.mapper;

import com.example.pojo.PageView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PageViewMapper {

    @Update("UPDATE page_view SET count = count + 1 WHERE view_type = #{viewType}")
    void increment(@Param("viewType") String viewType);

    @Select("SELECT * FROM page_view")
    List<PageView> selectAll();
}
