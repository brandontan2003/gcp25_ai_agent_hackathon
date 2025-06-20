package gcp25.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gcp.agent25.cases.management.service.dto.CreateTicketRequest;
import com.gcp.agent25.cases.management.service.enums.TicketStatusEnum;
import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.models.LlmResponse;
import com.google.adk.tools.ToolContext;
import com.google.genai.types.Content;
import gcp25.dto.AgentToolResponse;
import gcp25.utils.TicketServiceUtil;
import io.reactivex.rxjava3.core.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Optional;

import static gcp25.constants.AgentCommonConstant.SECURITY_SCAN_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.SECURITY_SCAN_OUTPUT;
import static gcp25.constants.AgentServiceConstant.RESPONSE;
import static gcp25.constants.AgentServiceConstant.STATUS_ERROR;

public class AgentCallback {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger log = LoggerFactory.getLogger(AgentCallback.class);
    public static final String SECURITY_SCAN_CLEAN_MESSAGE = "No security issues";

    public static Callbacks.AfterModelCallbackSync afterModelCallbackSync = AgentCallback::validateModelOutput;

    private static Optional<LlmResponse> validateModelOutput(CallbackContext context, LlmResponse llmResponse) {
        if (llmResponse == null) return Optional.empty();
        if (llmResponse.content().isPresent()) {
            Content content = llmResponse.content().get();
            String text = content.text();
            log.info("AfterModelCallback LLM Text Response :::::::::::::: {}", text);
            if (SECURITY_SCAN_AGENT_NAME.equalsIgnoreCase(context.agentName())) { //TODO Fix on the Security Scan Agent
                return Optional.of(llmResponse);
            } else {
                TicketServiceUtil.getService().createTicket(
                        buildCreateTicketRequest("Error occurred while executing: " + context.agentName(), text));
            }
            return Optional.of(llmResponse);
        }
        return Optional.of(llmResponse);
    }

    public static Callbacks.AfterToolCallback afterToolCallback =
            (invocationContext, baseTool, input, toolContext, toolResponse) -> validateAgentOutput(toolContext,
                    toolResponse);

    private static Maybe<Map<String, Object>> validateAgentOutput(ToolContext toolContext, Object toolResponse) {
        if (toolResponse == null) return Maybe.empty();
        Map<String, AgentToolResponse> map = objectMapper.convertValue(toolResponse, new
                TypeReference<>() {
                });
        AgentToolResponse response = map.get(RESPONSE);
        log.info("AfterToolCallback Response :::::::::::::: {}", response);
        if (STATUS_ERROR.equalsIgnoreCase(response.getStatus())) {
            String errorMsg = response.getResult().getError();
            log.info("ToolFunction Error Response :::::::::::::: {}", errorMsg);

            TicketServiceUtil.getService().createTicket(
                    buildCreateTicketRequest("Error occurred while executing: " + toolContext.agentName(), errorMsg));
            return Maybe.just(Map.of(RESPONSE, response));
        }
        return Maybe.just(Map.of(RESPONSE, response));
    }

    private static CreateTicketRequest buildCreateTicketRequest(String title, String description) {
        return CreateTicketRequest.builder().title(title).description(description).status(TicketStatusEnum.OPEN).build();
    }

}

