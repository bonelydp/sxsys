/*
package com.example.syxm.service;

import com.example.syxm.entity.SampleEntity;
import com.example.syxm.mapper.SampleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    public List<SampleEntity> getAllSamples() {
        return sampleMapper.findAll();
    }

    public SampleEntity getSampleById(Long id) {
        return sampleMapper.findById(id).orElse(null);
    }

    public SampleEntity saveSample(SampleEntity sample) {
        return sampleMapper.save(sample);
    }

    public void deleteSample(Long id) {
        sampleMapper.deleteById(id);
    }

    //状态
    public SampleEntity updateStatus(Integer id, SampleEntity.Status newStatus) {
        SampleEntity sample = getSampleById(Long.valueOf(id));
        if (sample != null) {
            sample.setStatus(newStatus);
            return sampleMapper.save(sample);
        }
        return null;
    }
}*/


package com.example.service;


import com.example.mapper.SampleMapper;
import com.example.pojo.PageResult;
import com.example.pojo.SampleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    @Value("${app.upload-dir:files}")
    private String uploadDir;

   /* // 查询所有
    public List<SampleEntity> getAllSamples() {
        return sampleMapper.selectAll();
    }

    // 根据ID查询
    public SampleEntity getSampleById(Integer id) {
        return sampleMapper.selectById(id);
    }*/


    public List<SampleEntity> getAllSamples() {
        return sampleMapper.selectAllWithUsername(); // 使用新SQL
    }

    public SampleEntity getSampleById(Integer id) {
        return sampleMapper.selectByIdWithUsername(id); // 使用新SQL
    }

    public PageResult getSamplesByPageAndStatus(String status, int page, int size) {
        int total = sampleMapper.countByStatus(status);
        int offset = (page - 1) * size;
        List<SampleEntity> samples = sampleMapper.selectWithPaginationAndStatus(status, offset, size);

        return new PageResult(total, page, size, samples);
    }


    // 新增
    public SampleEntity saveSample(SampleEntity sample) {
        sampleMapper.save(sample);
        return sample; // 需要确保返回对象包含生成的ID或其他字段
    }

    // 删除
  /*  public void deleteSample(Long id) {
        sampleMapper.deleteById(id);
    }*/

    // 假设有一个方法可以将URL转换为文件路径
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
        SampleEntity sampleEntity = sampleMapper.selectById(id);
        if (sampleEntity != null && sampleEntity.getFile() != null) {
            // 删除文件
            String filePath = getUrlFilePath(sampleEntity.getFile());
            cn.hutool.core.io.FileUtil.del(filePath);
        }
        // 删除数据库记录
        sampleMapper.deleteById(Long.valueOf(id));
    }


    // 更新状态
    public SampleEntity updateStatus(Integer id, SampleEntity.Status newStatus) {
        SampleEntity sample = getSampleById(id);
        if (sample != null) {
            sample.setStatus(newStatus);
            sampleMapper.updateStatus(Long.valueOf(id), newStatus.toString()); // 根据实际字段调整
            return sample;
        }
        return null;
    }
}
