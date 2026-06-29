package com.example.controller;


import com.example.common.Result;
import com.example.pojo.PageResult;
import com.example.pojo.SampleEntity;
import com.example.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/samples")
@CrossOrigin(originPatterns = "*")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    // 查所有
    @GetMapping("/selectall")
    public ResponseEntity<List<SampleEntity>> getAllSamples() {
        return new ResponseEntity<>(sampleService.getAllSamples(), HttpStatus.OK);
    }

    // 查个人
    @GetMapping("/get/{id}")
    public ResponseEntity<SampleEntity> getSampleById(@PathVariable Integer id) {
        SampleEntity sample = sampleService.getSampleById(id);
        if (sample != null) {
            return new ResponseEntity<>(sample, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    // 增(没有文件)
//    @PostMapping
//    public ResponseEntity<SampleEntity> saveSample(@RequestBody SampleEntity sample) {
//        SampleEntity savedSample = sampleService.saveSample(sample);
//        return new ResponseEntity<>(savedSample, HttpStatus.CREATED);
//    }

    // 删
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSample(@PathVariable Long id) {
        sampleService.delete(Math.toIntExact(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 增
  /*  @PostMapping("/upload")
    public ResponseEntity<SampleEntity> uploadSample(
            @RequestPart("sampleEntity") SampleEntity sampleEntity,
            @RequestPart("file") MultipartFile file) throws IOException {
        sampleService.saveSample(sampleEntity, file);
        return new ResponseEntity<>(sampleEntity, HttpStatus.CREATED);
    }*/

    @PostMapping("/upload")
    public Result uploadSample(@RequestBody SampleEntity sampleEntity) {
        sampleService.saveSample(sampleEntity);
        return Result.success();
    }

    // 审批样本
    @PostMapping("/{id}/approve")
    public ResponseEntity<SampleEntity> approveSample(@PathVariable Long id) { // 修改为Long类型以匹配其他方法
        SampleEntity sample = sampleService.updateStatus(Math.toIntExact(id), SampleEntity.Status.APPROVED);
        if (sample != null) {
            return new ResponseEntity<>(sample, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 拒绝样本
    @PostMapping("/{id}/reject")
    public ResponseEntity<SampleEntity> rejectSample(@PathVariable Long id) { // 修改为Long类型以匹配其他方法
        SampleEntity sample = sampleService.updateStatus(Math.toIntExact(id), SampleEntity.Status.REJECTED);
        if (sample != null) {
            return new ResponseEntity<>(sample, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/samplespage")
    public ResponseEntity<PageResult> getSamplesByPageAndStatus(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult result = sampleService.getSamplesByPageAndStatus(status, page, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}