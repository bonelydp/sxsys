package com.example.controller;

import com.example.service.MafftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(originPatterns = "*")
public class MafftController {

    private final MafftService mafftService;

    public MafftController(MafftService mafftService) {
        this.mafftService = mafftService;
    }

    @PostMapping("/runMafft")
    public ResponseEntity<?> runMafft(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("selectedDataBase") String selectedDataBase,
            @RequestParam(value = "querySequence", required = false) String querySequence,
            @RequestParam(value = "experimentName", required = false,
                    defaultValue = "defaultExperiment") String experimentName) {
        try {
            if (file != null && !file.isEmpty()) {
                return mafftService.runMafft(file, selectedDataBase, experimentName);
            }
            if (querySequence != null && !querySequence.trim().isEmpty()) {
                return mafftService.runMafftFromText(
                        querySequence, selectedDataBase, experimentName);
            }
            return ResponseEntity.badRequest().body("A file or sequence text is required");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to run MAFFT: " + e.getMessage());
        }
    }

    @PostMapping("/uploadMafft")
    public String uploadMafft(@RequestParam("file") MultipartFile file) throws Exception {
        return mafftService.saveSubjectFile(file);
    }
}
