package com.example.controller;

import com.example.service.BlastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(originPatterns = "*")
public class BlastController {

    @Autowired
    private BlastService blastService;

    @PostMapping("/runBlast")
    public ResponseEntity<?> runBlast(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "querySequence", required = false) String querySequence,
            @RequestParam("selectedDatabase") String selectedDatabase,
            @RequestParam(value = "jobTitle", required = false, defaultValue = "defaultJob") String jobTitle) {
        try {
            // 如果提供了文件，则使用文件
            if (file != null && !file.isEmpty()) {
                return blastService.runBlast(file, selectedDatabase, jobTitle);
            }
            // 否则，使用序列
            else if (querySequence != null && !querySequence.trim().isEmpty()) {
                return blastService.runBlast(querySequence, selectedDatabase, jobTitle);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("请输入对比序列或选择文件上传");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("运行 BLAST 出错: " + e.getMessage());
        }
    }

    // 上传BLAST文件
    @PostMapping("/uploadBlast")
    public String uploadBlast(@RequestParam("file") MultipartFile file) throws IOException {
        return blastService.saveSubjectFile(file);
    }
}