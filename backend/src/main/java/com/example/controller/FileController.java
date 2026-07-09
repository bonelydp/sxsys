package com.example.controller;



import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.pojo.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    /**
     * 文件上传
     */
    // 文件上传存储路径
    @Value("${app.upload-dir:files}")
    private String uploadDir;

    @Value("${app.file-url-prefix:/api/files/}")
    private String fileUrlPrefix;

    @Value("${app.excel-template-path:../frontend/public/resources/sampledata.xlsx}")
    private String excelTemplatePath;

    private String getUploadPath() {
        Path path = Paths.get(uploadDir);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(path);
        }
        return path.normalize().toString() + File.separator;
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        String flag;
        synchronized (this) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        } // 锁
        String fileName = file.getOriginalFilename();
        try {
            String filePath = getUploadPath();
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);  // ***/manager/files/1697438073596-avatar.png
            System.out.println(fileName + "--上传成功");
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
            return Result.error("文件上传失败");
        }
        return Result.success(fileUrlPrefix + flag + "-" + fileName);
    }
//    @PostMapping("/upload")
//    public Result<String> upload(MultipartFile file) throws IOException {
//        String originalFilename = file.getOriginalFilename();
//        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
//        return Result.success("url上传成功");
//    }

    @GetMapping("/{flag}")   //  1697438073596-avatar.png
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) {
                String lowerFlag = flag.toLowerCase();
                boolean isImage = lowerFlag.endsWith(".jpg") || lowerFlag.endsWith(".jpeg")
                        || lowerFlag.endsWith(".png") || lowerFlag.endsWith(".gif")
                        || lowerFlag.endsWith(".webp") || lowerFlag.endsWith(".svg");
                if (isImage) {
                    response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(flag, "UTF-8"));
                    String mimeType = lowerFlag.endsWith(".webp") ? "image/webp"
                            : lowerFlag.endsWith(".svg") ? "image/svg+xml"
                            : lowerFlag.endsWith(".png") ? "image/png"
                            : lowerFlag.endsWith(".gif") ? "image/gif"
                            : "image/jpeg";
                    response.setContentType(mimeType);
                } else {
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                }
                byte[] bytes = FileUtil.readBytes(getUploadPath() + flag);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败: " + flag + " - " + e.getMessage());
            try {
                response.reset();
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":404,\"message\":\"文件不存在\"}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * 下载特定文件
     */
    @GetMapping("/download/excel")
    public ResponseEntity<FileSystemResource> downloadExcel() throws UnsupportedEncodingException {
        // 文件路径
        File file = Paths.get(excelTemplatePath).toFile();

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 创建FileSystemResource对象
        FileSystemResource resource = new FileSystemResource(file);

        // 设置HTTP头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }


}
