package com.example.mapper;

import com.example.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {
    @Select("select * from notice order by date DESC")
    List<Notice> list();

    @Insert("insert into notice(title,content,date,cover_img) values (#{title},#{content},#{date},#{coverImg})")
    void add(Notice notice);

    @Delete("delete from notice where id=#{id}")
    void delete(Integer id);

    @Update("update notice set title=#{title},content=#{content},date=#{date},cover_img=#{coverImg} where id=#{id}")
    void update(Notice notice);

    @Select("select * from notice where id=#{id}")
    Notice getOne(Integer id);
}
