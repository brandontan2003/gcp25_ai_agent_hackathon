package gcp25.agents;

import com.google.adk.agents.LlmAgent;

import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;
import static gcp25.constants.AgentCommonConstant.TEST_AGENT_NAME;

public class TestAgent {
    public static LlmAgent testAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(TEST_AGENT_NAME)
                .description("Simulates unit/integration tests")
                .instruction("Given the build succeeded, simulate test results. Provide number of tests passed and " +
                        "failed.")
                .outputKey("test_result")
                .build();
    }
}
