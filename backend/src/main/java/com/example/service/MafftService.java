package com.example.service;

import com.example.service.support.BioDatabaseCatalog;
import com.example.service.support.BioProcessRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class MafftService {

    private final BioProcessRunner processRunner;
    private final BioDatabaseCatalog databaseCatalog;

    @Value("${bio.mafft-path:/usr/bin/mafft}")
    private String mafftPath;

    public MafftService(BioProcessRunner processRunner, BioDatabaseCatalog databaseCatalog) {
        this.processRunner = processRunner;
        this.databaseCatalog = databaseCatalog;
    }

    public ResponseEntity<byte[]> runMafft(
            MultipartFile unalignedFile, String selectedDataBase, String experimentName) throws Exception {
        Path jobDirectory = processRunner.createJobDirectory("mafft");
        try {
            Path inputFile = jobDirectory.resolve("input.fasta");
            try (InputStream input = unalignedFile.getInputStream()) {
                Files.copy(input, inputFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return processMafft(jobDirectory, inputFile, selectedDataBase);
        } finally {
            processRunner.deleteJobDirectory(jobDirectory);
        }
    }

    public ResponseEntity<byte[]> runMafftFromText(
            String sequence, String selectedDataBase, String experimentName) throws Exception {
        Path jobDirectory = processRunner.createJobDirectory("mafft");
        try {
            Path inputFile = jobDirectory.resolve("input.fasta");
            try (BufferedWriter writer = Files.newBufferedWriter(inputFile, StandardCharsets.UTF_8)) {
                writer.write(">sequence");
                writer.newLine();
                writer.write(sequence);
                writer.newLine();
            }
            return processMafft(jobDirectory, inputFile, selectedDataBase);
        } finally {
            processRunner.deleteJobDirectory(jobDirectory);
        }
    }

    private ResponseEntity<byte[]> processMafft(
            Path jobDirectory, Path inputFile, String selectedDataBase) throws Exception {
        Path referenceFile = databaseCatalog.resolveReference(selectedDataBase);
        if (!Files.isRegularFile(referenceFile)) {
            throw new IOException("Reference FASTA does not exist: " + referenceFile);
        }

        Path mergedFile = jobDirectory.resolve("merged.fasta");
        try (var output = Files.newOutputStream(mergedFile)) {
            Files.copy(referenceFile, output);
            output.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
            Files.copy(inputFile, output);
        }

        Path resultFile = jobDirectory.resolve("aligned.fasta");
        processRunner.runToFile(List.of(
                mafftPath,
                "--reorder",
                "--adjustdirection",
                mergedFile.toString()
        ), jobDirectory, resultFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "mafft_result.fasta");
        return new ResponseEntity<>(Files.readAllBytes(resultFile), headers, HttpStatus.OK);
    }

    public String saveSubjectFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("mafft_", ".fasta");
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        tempFile.toFile().deleteOnExit();
        return tempFile.toAbsolutePath().toString();
    }
}
