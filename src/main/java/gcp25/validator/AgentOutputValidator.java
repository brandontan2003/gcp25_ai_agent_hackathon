package gcp25.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.adk.agents.Callbacks;
import com.google.genai.types.Content;
import gcp25.dto.AgentToolResponse;
import io.reactivex.rxjava3.core.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static gcp25.constants.AgentServiceConstant.RESPONSE;
import static gcp25.constants.AgentServiceConstant.STATUS_ERROR;

public class AgentOutputValidator {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger log = LoggerFactory.getLogger(AgentOutputValidator.class);

    public static Callbacks.AfterModelCallbackSync afterAgentCallback = (context, response) -> {
        String agentName = context.agentName();
        log.info("Validating Agent :::::::::: " + agentName);
        Optional<Content> output = context.userContent();
        log.info("User Content :::::::::::::: " + output);
        log.info("Response :::::::::::::: " + response);
        return Optional.of(response);
    };

    public static Callbacks.AfterToolCallback afterToolCallback =
            (invocationContext, baseTool, input, toolContext, toolResponse) -> {
                return validateAgentOutput(toolResponse);
            };

    private static Maybe<Map<String, Object>> validateAgentOutput(Object toolResponse) {
        if (toolResponse == null) return Maybe.empty();
        Map<String, AgentToolResponse> map = objectMapper.convertValue(toolResponse, new
                TypeReference<>() {
                });
        AgentToolResponse response = map.get(RESPONSE);
        log.info("AfterToolCallback Response :::::::::::::: {}", response);
        if (STATUS_ERROR.equalsIgnoreCase(response.getStatus())) {
            return Maybe.just(Map.of(RESPONSE, response));
        }
        return Maybe.empty();
    }

}

