package com.example.service;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class IQtreeService {

    private final BioProcessRunner processRunner;

    @Value("${bio.iqtree-path:/usr/local/bin/iqtree3}")
    private String iqtreePath;

    public IQtreeService(BioProcessRunner processRunner) {
        this.processRunner = processRunner;
    }

    public ResponseEntity<byte[]> runIqtree(MultipartFile alignedFile, String experimentName)
            throws IOException, InterruptedException {
        Path jobDirectory = processRunner.createJobDirectory("iqtree");
        try {
            Path inputFile = jobDirectory.resolve("aligned.fasta");
            try (InputStream input = alignedFile.getInputStream()) {
                Files.copy(input, inputFile, StandardCopyOption.REPLACE_EXISTING);
            }

            Path outputPrefix = jobDirectory.resolve("result");
            processRunner.run(List.of(
                    iqtreePath,
                    "-s", inputFile.toString(),
                    "-m", "MFP",
                    "-bb", "1000",
                    "-bnni",
                    "-nt", "AUTO",
                    "-pre", outputPrefix.toString(),
                    "-redo"
            ), jobDirectory);

            Path treeFile = Path.of(outputPrefix + ".treefile");
            if (!Files.isRegularFile(treeFile)) {
                throw new IOException("IQ-TREE did not produce a tree file");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "iqtree_result.treefile");
            return new ResponseEntity<>(Files.readAllBytes(treeFile), headers, HttpStatus.OK);
        } finally {
            processRunner.deleteJobDirectory(jobDirectory);
        }
    }

    public String saveSubjectFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("tree_", "." + getExtension(file));
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        tempFile.toFile().deleteOnExit();
        return tempFile.toAbsolutePath().toString();
    }

    private String getExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            int index = filename.lastIndexOf('.');
            if (index > 0 && index < filename.length() - 1) {
                return filename.substring(index + 1);
            }
        }
        return "fasta";
    }
}
