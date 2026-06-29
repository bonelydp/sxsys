package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private Integer id;//主键ID
    @NotEmpty
    private String title;//文章标题
    @NotNull
    private String type;//文章分类id
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
    private String author;
    private String volume;
    private Integer publishTime;
    private String jcr;
    private String issn;
}
