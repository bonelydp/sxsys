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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class BlastService {

    private final BioProcessRunner processRunner;
    private final BioDatabaseCatalog databaseCatalog;

    @Value("${bio.blast-path:/usr/local/bin/blastn}")
    private String blastPath;

    public BlastService(BioProcessRunner processRunner, BioDatabaseCatalog databaseCatalog) {
        this.processRunner = processRunner;
        this.databaseCatalog = databaseCatalog;
    }

    public ResponseEntity<byte[]> runBlast(
            MultipartFile queryFile, String selectedDatabase, String jobTitle)
            throws IOException, InterruptedException {
        Path jobDirectory = processRunner.createJobDirectory("blast");
        try {
            Path inputFile = jobDirectory.resolve("query.fasta");
            try (InputStream input = queryFile.getInputStream()) {
                Files.copy(input, inputFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return processBlast(jobDirectory, inputFile, selectedDatabase);
        } finally {
            processRunner.deleteJobDirectory(jobDirectory);
        }
    }

    public ResponseEntity<byte[]> runBlast(
            String querySequence, String selectedDatabase, String jobTitle)
            throws IOException, InterruptedException {
        Path jobDirectory = processRunner.createJobDirectory("blast");
        try {
            Path inputFile = jobDirectory.resolve("query.fasta");
            Files.writeString(inputFile, querySequence, StandardCharsets.UTF_8);
            return processBlast(jobDirectory, inputFile, selectedDatabase);
        } finally {
            processRunner.deleteJobDirectory(jobDirectory);
        }
    }

    private ResponseEntity<byte[]> processBlast(
            Path jobDirectory, Path inputFile, String selectedDatabase)
            throws IOException, InterruptedException {
        Path database = databaseCatalog.resolveDatabase(selectedDatabase);
        Path resultFile = jobDirectory.resolve("blast-result.txt");

        processRunner.run(List.of(
                blastPath,
                "-query", inputFile.toString(),
                "-db", database.toString(),
                "-outfmt", "6",
                "-evalue", "1e-5",
                "-out", resultFile.toString()
        ), jobDirectory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "blast_result.txt");
        return new ResponseEntity<>(Files.readAllBytes(resultFile), headers, HttpStatus.OK);
    }

    public String saveSubjectFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("blast_", ".fasta");
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        tempFile.toFile().deleteOnExit();
        return tempFile.toAbsolutePath().toString();
    }
}
