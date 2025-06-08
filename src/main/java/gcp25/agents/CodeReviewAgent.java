package gcp25.agents;

import com.google.adk.agents.LlmAgent;

import static gcp25.constants.AgentCommonConstant.CODE_REVIEW_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;

public class CodeReviewAgent {
    public static LlmAgent codeReviewAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(CODE_REVIEW_AGENT_NAME)
                .description("Reviews code for issues like style and complexity")
                .instruction("You will receive code snippets and must respond with a short review including complexity and formatting feedback.")
                .outputKey("review_comments")
                .build();
    }
}
