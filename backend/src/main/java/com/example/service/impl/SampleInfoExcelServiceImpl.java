package com.example.service.impl;

import com.alibaba.excel.EasyExcel;
import com.example.DTO.SampleInfoExcelDTO;
import com.example.Listener.HeaderReadListener;
import com.example.Listener.SampleInfoExcelListener;
import com.example.service.SampleInfoExcelService;
import com.example.service.SampleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SampleInfoExcelServiceImpl implements SampleInfoExcelService {

    @Autowired
    private SampleInfoService sampleInfoService;

    // 存储表头中的亚型字段名（从第28列开始）
    private Map<Integer, String> subtypeHeaders = new LinkedHashMap<>();

    @Override
    public void parseExcel(InputStream inputStream) {
        try {
            byte[] fileBytes = inputStream.readAllBytes();

            InputStream headerStream = new ByteArrayInputStream(fileBytes);
            EasyExcel.read(headerStream)
                    .headRowNumber(0)
                    .sheet()
                    .registerReadListener(new HeaderReadListener(subtypeHeaders))
                    .doReadSync();

            InputStream dataStream = new ByteArrayInputStream(fileBytes);
            EasyExcel.read(dataStream, SampleInfoExcelDTO.class, new SampleInfoExcelListener(sampleInfoService, subtypeHeaders))
                    .headRowNumber(1)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            System.out.println("解析 Excel 文件失败: {}"+e.getMessage());
        }
    }
}

