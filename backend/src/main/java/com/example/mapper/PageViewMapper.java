package com.example.mapper;

import com.example.pojo.PageView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PageViewMapper {

    void increment(@Param("viewType") String viewType);

    List<PageView> selectAll();
}
