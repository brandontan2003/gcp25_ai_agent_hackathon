package gcp25.service;

import com.google.adk.tools.Annotations.Schema;
import gcp25.dto.AgentToolResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import static gcp25.constants.AgentServiceConstant.STATUS_ERROR;
import static gcp25.constants.AgentServiceConstant.STATUS_SUCCESS;
import static gcp25.utils.ResponseUtils.buildErrorResponse;
import static gcp25.utils.ResponseUtils.buildRepoPathResponse;

public class RepoCloneAgentService {
    private static Boolean isValidGitHubUrl(String url) {
        return url.matches("^https://github.com/[\\w.-]+/[\\w.-]+(\\.git)?$");
    }

    @Schema(description = "The Github url to be cloned.")
    public static Map<String, AgentToolResponse> cloneRepoService(String repoUrl) {
        if (repoUrl == null || !isValidGitHubUrl(repoUrl)) {
            return buildErrorResponse(STATUS_ERROR, "Repository " + repoUrl + " is not accessible or invalid.");
        }

        Path destination = Paths.get(System.getProperty("java.io.tmpdir"), "repo/" + UUID.randomUUID());

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
                return buildErrorResponse(STATUS_ERROR,"Error during git clone: " + output);
            }

            return buildRepoPathResponse(STATUS_SUCCESS, String.valueOf(destination.toAbsolutePath()));

        } catch (IOException | InterruptedException e) {
            return buildErrorResponse(STATUS_ERROR, "Error during git clone: " + e);
        }
    }
}
