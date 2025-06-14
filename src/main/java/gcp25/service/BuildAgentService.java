package gcp25.service;

import com.google.adk.tools.Annotations.Schema;
import gcp25.dto.AgentToolResponse;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static gcp25.constants.AgentCommonConstant.BUILD_RESULT_OUTPUT;
import static gcp25.constants.AgentCommonConstant.COLON_SPACE;
import static gcp25.constants.AgentServiceConstant.STATUS_ERROR;
import static gcp25.constants.AgentServiceConstant.STATUS_SUCCESS;
import static gcp25.utils.LogsUtils.tail;
import static gcp25.utils.RepoUtils.isValidRepoPath;
import static gcp25.utils.ResponseUtils.buildResponse;

public class BuildAgentService {

    @Schema(description = "Temporary repository path.")
    public static Map<String, AgentToolResponse> buildProjectService(String repoPath) {
        if (!isValidRepoPath(repoPath)) {
            return buildResponse(STATUS_ERROR, "Repository path is invalid: " + repoPath);
        }

        File repoDir = new File(repoPath);
        // Detect build system
        String[] buildCommand = detectBuildCommand(repoDir);
        if (buildCommand == null) {
            return buildResponse(STATUS_ERROR, "No supported build system detected in: " + repoPath);
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(buildCommand);
            builder.directory(repoDir);
            builder.redirectErrorStream(true);

            Process process = builder.start();
            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return buildResponse(STATUS_ERROR,
                        "Build failed with exit code " + exitCode + ". Output:\n" + tail(output));
            }
            return buildResponse(STATUS_SUCCESS, BUILD_RESULT_OUTPUT + COLON_SPACE + tail(output));

        } catch (IOException | InterruptedException ex) {
            return buildResponse(STATUS_ERROR, "Build process error: " + ex.getMessage());
        }
    }

    private static String[] detectBuildCommand(File dir) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        File gradleWrapper = new File(dir, isWindows ? "gradlew.bat" : "gradlew");
        File buildGradle = new File(dir, "build.gradle");
        File buildKts = new File(dir, "build.gradle.kts");
        File pom = new File(dir, "pom.xml");
        File pkgJson = new File(dir, "package.json");
        File makefile = new File(dir, "Makefile");

        if (gradleWrapper.exists()) {
            return isWindows
                    ? new String[]{"cmd", "/c", "gradlew.bat", "build"}
                    : new String[]{"./gradlew", "build"};
        } else if (buildGradle.exists() || buildKts.exists()) {
            return isWindows
                    ? new String[]{"cmd", "/c", "gradle", "build"}
                    : new String[]{"gradle", "build"};
        } else if (pom.exists()) {
            return isWindows
                    ? new String[]{"cmd", "/c", "mvn.cmd", "clean", "install", "-DskipTests"}
                    : new String[]{"mvn", "clean", "install", "-DskipTests"};
        } else if (pkgJson.exists()) {
            return isWindows
                    ? new String[]{"cmd", "/c", "npm.cmd", "install", "&&", "npm.cmd", "run", "build"}
                    : new String[]{"sh", "-c", "npm install && npm run build"};
        } else if (makefile.exists()) {
            return isWindows
                    ? new String[]{"cmd", "/c", "make"}
                    : new String[]{"make"};
        }

        return null;
    }

}
