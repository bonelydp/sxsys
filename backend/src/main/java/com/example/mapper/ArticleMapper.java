package com.example.mapper;

import com.example.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //新增文章
    @Insert("insert into article(title,create_time,update_time,author,volume,publish_time,jcr,issn,type) " +
            "values (#{title},#{createTime},#{updateTime},#{author},#{volume},#{publishTime},#{jcr},#{issn},#{type})")
    void add(Article article);
    //删除文章
    @Delete("delete from article where id=#{id}")
    void delete(Integer id);

    //更新文章
    @Update("update article set title=#{title},type=#{type},update_time=#{updateTime},author=#{author}," +
            "volume=#{volume},publish_time=#{publishTime},jcr=#{jcr},issn=#{issn} where id=#{id}")
    void update(Article article);


    //返回列表数据
    List<Article> list(Integer year);
}
