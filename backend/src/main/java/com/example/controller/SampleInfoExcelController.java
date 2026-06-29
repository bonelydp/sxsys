package com.example.controller;


import com.example.service.SampleInfoExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 解析Excel
 */

@RestController
@RequestMapping("/sampleinfo2")
@Validated
@Slf4j
public class SampleInfoExcelController {

    @Autowired
    private SampleInfoExcelService sampleInfoExcelService;

    @PostMapping("/excel/batch")
    public ResponseEntity<String> uploadBatchExcel(@RequestParam("files") List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("未选择任何文件");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.toLowerCase().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body(fileName + " 文件扩展名必须为.xlsx");
            }

            if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContentType())) {
                return ResponseEntity.badRequest().body(fileName + " 不是有效的 Excel 文件");
            }

            log.info("接收到Excel文件上传请求: {}", fileName);
            sampleInfoExcelService.parseExcel(file.getInputStream());
        }

        return ResponseEntity.ok("所有Excel文件上传成功");
    }

}