package gcp25.utils;

import java.io.File;

public class RepoUtils {

    /**
     * Checks if the provided repository path is valid:
     * - Not null or blank
     * - Points to an existing directory
     *
     * @param repoPath path to check
     * @return true if the path is valid, false otherwise
     */
    public static boolean isValidRepoPath(String repoPath) {
        if (repoPath == null || repoPath.isBlank()) {
            return false;
        }
        File repoDir = new File(repoPath);
        return repoDir.exists() && repoDir.isDirectory();
    }
}
