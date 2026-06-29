package com.example.service.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class BioDatabaseCatalog {

    @Value("${bio.database.18s:/app/bio-data/blast_db/cryptosporidium-18s}")
    private String ssuDatabase;

    @Value("${bio.database.gp60:/app/bio-data/blast_db/cryptosporidium-gp60}")
    private String gp60Database;

    @Value("${bio.reference.18s:/app/bio-data/references/cryptosporidium-18s-reference.fasta}")
    private String ssuReference;

    @Value("${bio.reference.gp60:/app/bio-data/references/cryptosporidium-gp60-reference.fasta}")
    private String gp60Reference;

    public Path resolveDatabase(String databaseName) {
        return Path.of(isGp60(databaseName) ? gp60Database : require18s(databaseName, ssuDatabase));
    }

    public Path resolveReference(String databaseName) {
        return Path.of(isGp60(databaseName) ? gp60Reference : require18s(databaseName, ssuReference));
    }

    private boolean isGp60(String databaseName) {
        return normalize(databaseName).contains("gp60");
    }

    private String require18s(String databaseName, String configuredPath) {
        String normalized = normalize(databaseName);
        if (normalized.contains("18s") || normalized.contains("ssu")) {
            return configuredPath;
        }
        throw new IllegalArgumentException("Unsupported biological database: " + databaseName);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Biological database must be selected");
        }
        return value.trim().toLowerCase();
    }
}
