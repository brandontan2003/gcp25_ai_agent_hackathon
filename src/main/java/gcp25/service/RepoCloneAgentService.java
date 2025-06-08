package gcp25.service;

import com.google.adk.tools.Annotations.Schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

public class RepoCloneAgentService {
    private static Boolean isValidGitHubUrl(String url) {
        return url.matches("^https://github.com/[\\w.-]+/[\\w.-]+(\\.git)?$");
    }

    public static Map<String, String> cloneRepoService(@Schema(description = "The Github url to be cloned.") String repoUrl) {
        if (repoUrl == null || !isValidGitHubUrl(repoUrl)) {
            return Map.of(
                    "status", "error", "report", "Repository " + repoUrl + " is not accessible or invalid.");
        }

        Path destination = Paths.get(System.getProperty("java.io.tmpdir"), "repo_" + UUID.randomUUID());

        try {
            Files.createDirectories(destination);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp directory: " + destination, e);
        }

        ProcessBuilder builder = new ProcessBuilder("git", "clone", repoUrl, destination.toString());
        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();
            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Git clone failed:\n" + output);
            }

            System.out.println("✅ Repo cloned to: " + destination);
            return Map.of(
                    "status", "success", "report", "Repository has been cloned to temp folder: " + destination);

        } catch (IOException | InterruptedException e) {
            return Map.of(
                    "status", "error", "report", "Error during git clone: " + e);

        }
    }
}
