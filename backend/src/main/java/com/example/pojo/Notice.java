package com.example.pojo;

import lombok.Data;

import java.time.LocalDate;


@Data
public class Notice {
    private Integer id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 创建时间 */
    private LocalDate date;
    private String coverImg;
}
