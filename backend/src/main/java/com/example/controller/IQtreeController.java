package com.example.controller;

import com.example.service.IQtreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(originPatterns = "*")
public class IQtreeController {

    @Autowired
    private IQtreeService iqtreeService;

    @PostMapping("/iqtree")
    public ResponseEntity<byte[]> runIqtree(
            @RequestParam("alignedFile") MultipartFile alignedFile,
            @RequestParam("experimentName") String experimentName) {
        try {
            return iqtreeService.runIqtree(alignedFile, experimentName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes());
        }
    }

    // 新增的上传主题文件的方法
    @PostMapping("/uploadtree")
    public String uploadSubject(@RequestParam("file") MultipartFile file) throws IOException {
        return iqtreeService.saveSubjectFile(file);
    }
}
