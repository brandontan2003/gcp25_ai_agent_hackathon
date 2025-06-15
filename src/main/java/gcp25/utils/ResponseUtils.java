package gcp25.utils;

import gcp25.dto.AgentToolMessage;
import gcp25.dto.AgentToolResponse;

import java.util.Map;

import static gcp25.constants.AgentServiceConstant.RESPONSE;

public class ResponseUtils {

    public static Map<String, AgentToolResponse> buildErrorResponse(String status, String message) {
        AgentToolMessage agentToolMessage = new AgentToolMessage();
        agentToolMessage.setError(message);

        AgentToolResponse response = new AgentToolResponse();
        response.setStatus(status);
        response.setResult(agentToolMessage);
        return Map.of(RESPONSE, response);
    }

    public static Map<String, AgentToolResponse> buildRepoPathResponse(String status, String repoPath) {
        AgentToolMessage agentToolMessage = new AgentToolMessage();
        agentToolMessage.setRepoPath(repoPath);

        AgentToolResponse response = new AgentToolResponse();
        response.setStatus(status);
        response.setResult(agentToolMessage);
        return Map.of(RESPONSE, response);
    }

    public static Map<String, AgentToolResponse> buildResultResponse(String status, String buildResult) {
        AgentToolMessage agentToolMessage = new AgentToolMessage();
        agentToolMessage.setBuildResult(buildResult);

        AgentToolResponse response = new AgentToolResponse();
        response.setStatus(status);
        response.setResult(agentToolMessage);
        return Map.of(RESPONSE, response);
    }

    public static Map<String, AgentToolResponse> buildReviewCommentResponse(String status, String reviewComment) {
        AgentToolMessage agentToolMessage = new AgentToolMessage();
        agentToolMessage.setBuildResult(reviewComment);

        AgentToolResponse response = new AgentToolResponse();
        response.setStatus(status);
        response.setResult(agentToolMessage);
        return Map.of(RESPONSE, response);
    }
}
