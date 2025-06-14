package gcp25.service;


import com.google.adk.tools.Annotations.Schema;
import gcp25.dto.AgentToolResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gcp25.constants.AgentCommonConstant.COLON_SPACE;
import static gcp25.constants.AgentCommonConstant.REVIEW_COMMENTS_OUTPUT;
import static gcp25.constants.AgentServiceConstant.STATUS_ERROR;
import static gcp25.constants.AgentServiceConstant.STATUS_SUCCESS;
import static gcp25.utils.RepoUtils.isValidRepoPath;
import static gcp25.utils.ResponseUtils.buildResponse;

public class CodeReviewAgentService {
    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of("java", "py", "js", "ts", "go", "cs", "cpp");

    @Schema(description = "Path to the local cloned GitHub repository")
    public static Map<String, AgentToolResponse> reviewCodeService(String repoPath) {
        if (repoPath == null || repoPath.isBlank() || !isValidRepoPath(repoPath)) {
            return buildResponse(STATUS_ERROR, "Repository path is missing or invalid: " + repoPath);
        }

        StringBuilder reviewInput = new StringBuilder();

        try (Stream<Path> paths = Files.walk(Path.of(repoPath))) {
            List<Path> codeFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> isSupportedExtension(path.getFileName().toString()))
                    .collect(Collectors.toList());

            if (codeFiles.isEmpty()) {
                return buildResponse(STATUS_ERROR, "No supported code files found in the repository.");
            }

            for (Path file : codeFiles) {
                reviewInput.append("### File: `").append(file.getFileName()).append("`\n");
                reviewInput.append("Path: ").append(file.toAbsolutePath()).append("\n\n");
                reviewInput.append("```").append(getLanguageTag(file)).append("\n");
                reviewInput.append(Files.readString(file)).append("\n```\n\n");
            }
            return buildResponse(STATUS_SUCCESS, REVIEW_COMMENTS_OUTPUT + COLON_SPACE + reviewInput);

        } catch (IOException ex) {
            return buildResponse(STATUS_ERROR, "Failed to read source files: " + ex.getMessage());
        }
    }

    private static boolean isSupportedExtension(String filename) {
        int dotIdx = filename.lastIndexOf('.');
        if (dotIdx == -1 || dotIdx == filename.length() - 1) return false;
        String ext = filename.substring(dotIdx + 1).toLowerCase();
        return SUPPORTED_EXTENSIONS.contains(ext);
    }

    private static String getLanguageTag(Path file) {
        String ext = Optional.of(file.getFileName().toString())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.') + 1))
                .orElse("")
                .toLowerCase();

        return switch (ext) {
            case "java" -> "java";
            case "py" -> "python";
            case "js" -> "javascript";
            case "ts" -> "typescript";
            case "go" -> "go";
            case "cs" -> "csharp";
            case "cpp" -> "cpp";
            default -> "";
        };
    }
}
