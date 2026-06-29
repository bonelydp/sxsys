package com.example.service;


import com.example.mapper.ContourMapper;
import com.example.pojo.Contour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ContourService {
    @Autowired
    private ContourMapper contourMapper;

    @Value("${app.upload-dir:files}")
    private String uploadDir;

    public void add(Contour contour) {
        contourMapper.insert(contour);
    }

    public List<Contour> selectAll() {
        return contourMapper.selectAll();
    }

    public String getUrlFilePath(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        Path path = Paths.get(uploadDir);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(path);
        }
        return path.resolve(fileName).normalize().toString();
    }

    public void delete(Integer id) {
        // 获取要删除的轮廓图信息
        Contour contour = contourMapper.selectByPrimaryKey(id);
        if (contour != null && contour.getImg() != null) {
            // 删除文件
            String filePath = getUrlFilePath(contour.getImg());
            cn.hutool.core.io.FileUtil.del(filePath);
        }
        // 删除数据库记录
        contourMapper.deleteByPrimaryKey(id);
    }

    public void update(Contour contour) {
        contourMapper.updateByPrimaryKey(contour);
    }
}
