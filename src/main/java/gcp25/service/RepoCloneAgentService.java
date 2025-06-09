package gcp25.service;

import com.google.adk.tools.Annotations.Schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import static gcp25.constants.AgentServiceConstant.*;

public class RepoCloneAgentService {
    private static Boolean isValidGitHubUrl(String url) {
        return url.matches("^https://github.com/[\\w.-]+/[\\w.-]+(\\.git)?$");
    }

    public static Map<String, String> cloneRepoService(@Schema(description = "The Github url to be cloned.") String repoUrl) {
        if (repoUrl == null || !isValidGitHubUrl(repoUrl)) {
            return Map.of(
                    STATUS, STATUS_ERROR,
                    REPORT, "Repository " + repoUrl + " is not accessible or invalid.");
        }

        // TODO Add UUID.randomUUID in the future
        Path destination = Paths.get(System.getProperty("java.io.tmpdir"), "repo_");

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

            System.out.println("✅ Repo cloned to: " + destination.toAbsolutePath());
            return Map.of(
                    STATUS, STATUS_SUCCESS,
                    "repo_path", destination.toAbsolutePath().toString());

        } catch (IOException | InterruptedException e) {
            return Map.of(
                    STATUS, STATUS_ERROR,
                    REPORT, "Error during git clone: " + e);
        }
    }
}
