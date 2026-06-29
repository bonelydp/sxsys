package com.example.service;

import com.example.service.support.BioDatabaseCatalog;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SequenceService {

    private final BioDatabaseCatalog databaseCatalog;

    public SequenceService(BioDatabaseCatalog databaseCatalog) {
        this.databaseCatalog = databaseCatalog;
    }

    public String getBaseSequenceFromFastaFile(String sequenceName, String selectedDatabase)
            throws IOException {
        Path referenceFile = databaseCatalog.resolveReference(selectedDatabase);
        if (!Files.isRegularFile(referenceFile)) {
            throw new IOException("Reference FASTA does not exist: " + referenceFile);
        }

        try (BufferedReader reader = Files.newBufferedReader(referenceFile, StandardCharsets.UTF_8)) {
            String line;
            boolean matched = false;
            StringBuilder sequence = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (matched) {
                        break;
                    }
                    String identifier = line.substring(1).trim().split("\\s+", 2)[0];
                    matched = identifier.equals(sequenceName) || line.substring(1).startsWith(sequenceName);
                } else if (matched) {
                    sequence.append(line.trim());
                }
            }
            return sequence.toString();
        }
    }
}
