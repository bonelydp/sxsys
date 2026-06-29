package com.example.controller;


import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@CrossOrigin(originPatterns = "*")
public class PhylogeneticTreeController {

    @Value("${app.python.executable:python}")
    private String pythonExecutable;

    @Value("${app.python.tree-script-path:}")
    private String treeScriptPath;

    @Value("${app.python.work-dir:}")
    private String pythonWorkDir;

    @PostMapping("/treeImage")
    public ResponseEntity<byte[]> generateTreeImage(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 创建临时文件保存上传的 .contree 文件
        File tempInputFile = File.createTempFile("newick_input", ".tree");
        try (FileOutputStream fos = new FileOutputStream(tempInputFile)) {
            fos.write(file.getBytes());
        }

        // 定义输出图像的临时文件路径
        File tempOutputFile = File.createTempFile("phylogenetic_tree", ".png");

        // 调用Python脚本生成图像
        callPythonScript(tempInputFile.getAbsolutePath(), tempOutputFile.getAbsolutePath());

        // 读取生成的图像文件内容
        byte[] imageBytes = Files.readAllBytes(tempOutputFile.toPath());

        // 删除临时文件
        tempInputFile.delete();
        tempOutputFile.delete();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "phylogenetic_tree.png");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    private void callPythonScript(String inputFilePath, String outputFilePath) throws IOException, InterruptedException {
        if (treeScriptPath == null || treeScriptPath.isBlank()) {
            throw new IllegalStateException("app.python.tree-script-path is not configured");
        }
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExecutable, treeScriptPath, inputFilePath, outputFilePath
        );
        if (pythonWorkDir != null && !pythonWorkDir.isBlank()) {
            processBuilder.directory(new java.io.File(pythonWorkDir));
        }

        // 启动Python进程
        Process process = processBuilder.start();

        // 等待Python脚本完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script failed with exit code " + exitCode);
        }
    }
}
