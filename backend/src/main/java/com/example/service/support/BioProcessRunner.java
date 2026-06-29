package com.example.service.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Component
public class BioProcessRunner {

    @Value("${bio.job-dir:/app/bio-jobs}")
    private String jobRoot;

    public Path createJobDirectory(String prefix) throws IOException {
        Path root = Path.of(jobRoot).toAbsolutePath().normalize();
        Files.createDirectories(root);
        return Files.createTempDirectory(root, prefix + "-");
    }

    public String run(List<String> command, Path workDirectory) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workDirectory.toFile());
        builder.redirectErrorStream(true);

        Process process = builder.start();
        String output;
        try (var stream = process.getInputStream()) {
            output = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Command failed with exit code " + exitCode + ": "
                    + String.join(" ", command) + System.lineSeparator() + output);
        }
        return output;
    }

    public void runToFile(List<String> command, Path workDirectory, Path outputFile)
            throws IOException, InterruptedException {
        Path errorFile = workDirectory.resolve("process-error.log");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workDirectory.toFile());
        builder.redirectOutput(outputFile.toFile());
        builder.redirectError(errorFile.toFile());

        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            String error = Files.exists(errorFile)
                    ? Files.readString(errorFile, StandardCharsets.UTF_8)
                    : "";
            throw new IOException("Command failed with exit code " + exitCode + ": "
                    + String.join(" ", command) + System.lineSeparator() + error);
        }
        Files.deleteIfExists(errorFile);
    }

    public void deleteJobDirectory(Path jobDirectory) throws IOException {
        if (jobDirectory == null || !Files.exists(jobDirectory)) {
            return;
        }

        Path root = Path.of(jobRoot).toAbsolutePath().normalize();
        Path target = jobDirectory.toAbsolutePath().normalize();
        if (!target.startsWith(root) || target.equals(root)) {
            throw new IOException("Refusing to delete path outside the biological job directory: " + target);
        }

        try (var paths = Files.walk(target)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }
}
