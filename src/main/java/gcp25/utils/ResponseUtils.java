package gcp25.utils;

import gcp25.dto.AgentToolMessage;
import gcp25.dto.AgentToolResponse;

import java.util.Map;

import static gcp25.constants.AgentServiceConstant.RESPONSE;

public class ResponseUtils {

    public static Map<String, AgentToolResponse> buildResponse(String status, String message) {
        AgentToolMessage agentToolMessage = new AgentToolMessage();
        agentToolMessage.setReport(message);

        AgentToolResponse response = new AgentToolResponse();
        response.setStatus(status);
        response.setResult(agentToolMessage);
        return Map.of(RESPONSE, response);
    }
}
