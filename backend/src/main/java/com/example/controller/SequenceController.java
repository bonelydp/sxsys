package com.example.controller;

import com.example.service.SequenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(originPatterns = "*")
public class SequenceController {

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @GetMapping("/sequence")
    public ResponseEntity<String> getSequence(
            @RequestParam String sequenceName,
            @RequestParam String database) {
        try {
            String sequence = sequenceService.getBaseSequenceFromFastaFile(sequenceName, database);
            if (!sequence.isEmpty()) {
                return ResponseEntity.ok(sequence);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sequence not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve sequence: " + e.getMessage());
        }
    }
}
