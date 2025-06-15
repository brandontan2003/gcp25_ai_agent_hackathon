package gcp25.dto;

import java.io.Serializable;
import java.util.Objects;

public class AgentToolMessage implements Serializable {

    private String error;
    private String repoPath;
    private String buildResult;
    private String reviewComments;
    private String securityScan;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public String getBuildResult() {
        return buildResult;
    }

    public void setBuildResult(String buildResult) {
        this.buildResult = buildResult;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public String getSecurityScan() {
        return securityScan;
    }

    public void setSecurityScan(String securityScan) {
        this.securityScan = securityScan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentToolMessage that = (AgentToolMessage) o;
        return Objects.equals(error, that.error) && Objects.equals(repoPath, that.repoPath) && Objects.equals(buildResult, that.buildResult) && Objects.equals(reviewComments, that.reviewComments) && Objects.equals(securityScan, that.securityScan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, repoPath, buildResult, reviewComments, securityScan);
    }

    @Override
    public String toString() {
        return "AgentToolMessage{" +
                "error='" + error + '\'' +
                ", repoPath='" + repoPath + '\'' +
                ", buildResult='" + buildResult + '\'' +
                ", reviewComments='" + reviewComments + '\'' +
                ", securityScan='" + securityScan + '\'' +
                '}';
    }
}
