package gcp25.agents;

import com.google.adk.agents.LlmAgent;

import static gcp25.constants.AgentCommonConstant.BUILD_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;

public class BuildAgent {
    public static LlmAgent buildAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(BUILD_AGENT_NAME)
                .description("Simulates the build process")
                .instruction("Based on previous review, simulate build success or failure.")
                .outputKey("build_result")
                .build();
    }
}
