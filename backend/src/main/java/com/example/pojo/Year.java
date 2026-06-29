package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Year {
    private Integer id;
    private String title;
    private Integer year;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updateTime;
}
