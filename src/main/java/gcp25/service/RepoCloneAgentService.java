package gcp25.service;

import com.google.adk.tools.Annotations.Schema;
import gcp25.dto.AgentToolResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static gcp25.constants.AgentServiceConstant.STATUS_ERROR;
import static gcp25.constants.AgentServiceConstant.STATUS_SUCCESS;
import static gcp25.utils.ResponseUtils.buildResponse;

public class RepoCloneAgentService {
    private static Boolean isValidGitHubUrl(String url) {
        return url.matches("^https://github.com/[\\w.-]+/[\\w.-]+(\\.git)?$");
    }

    @Schema(description = "The Github url to be cloned.")
    public static Map<String, AgentToolResponse> cloneRepoService(String repoUrl) {
        if (repoUrl == null || !isValidGitHubUrl(repoUrl)) {
            return buildResponse(STATUS_ERROR, "Repository " + repoUrl + " is not accessible or invalid.");
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
                return buildResponse(STATUS_ERROR,"Error during git clone: " + output);
            }

            System.out.println("✅ Repo cloned to: " + destination.toAbsolutePath());
            return buildResponse(STATUS_SUCCESS, "repo_path" + destination.toAbsolutePath());

        } catch (IOException | InterruptedException e) {
            return buildResponse(STATUS_ERROR, "Error during git clone: " + e);
        }
    }
}
