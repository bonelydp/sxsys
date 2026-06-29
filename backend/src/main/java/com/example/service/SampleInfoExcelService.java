package com.example.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

public interface SampleInfoExcelService {
    // 解析Excel
    void parseExcel(InputStream inputStream);

}
