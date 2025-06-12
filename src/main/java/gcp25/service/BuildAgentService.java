package gcp25.service;

import com.google.adk.tools.Annotations.Schema;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static gcp25.constants.AgentServiceConstant.*;
import static gcp25.utils.LogsUtils.tail;
import static gcp25.utils.RepoUtils.isValidRepoPath;

public class BuildAgentService {

    @Schema(description = "Temporary repository path.")
    public static Map<String, String> buildProjectService(String repoPath) {
        if (!isValidRepoPath(repoPath)) {
            return Map.of(
                    STATUS, STATUS_ERROR,
                    REPORT, "Repository path is invalid: " + repoPath
            );
        }

        File repoDir = new File(repoPath);
        // Detect build system
        String[] buildCommand = detectBuildCommand(repoDir);
        if (buildCommand == null) {
            return Map.of(
                    STATUS, STATUS_ERROR,
                    REPORT, "No supported build system detected in: " + repoPath
            );
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(buildCommand);
            builder.directory(repoDir);
            builder.redirectErrorStream(true);

            Process process = builder.start();
            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return Map.of(
                        STATUS, STATUS_ERROR,
                        REPORT, "Build failed with exit code " + exitCode + ". Output:\n" + tail(output)
                );
            }

            return Map.of(
                    STATUS, STATUS_SUCCESS,
                    "build_result", tail(output)
            );

        } catch (IOException | InterruptedException ex) {
            return Map.of(
                    STATUS, STATUS_ERROR,
                    REPORT, "Build process error: " + ex.getMessage()
            );
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
