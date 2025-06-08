package gcp25.agents;

import com.google.adk.agents.LlmAgent;

import static gcp25.constants.AgentCommonConstant.DEPLOYMENT_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;

public class DeploymentAgent {
    public static LlmAgent deploymentAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(DEPLOYMENT_AGENT_NAME)
                .description("Deploys application if all previous stages pass")
                .instruction("""
                        All prior steps passed. Simulate deployment of the application to a production environment.
                        Include environment (e.g. AWS), and deployment result (success/failure).
                        """)
                .outputKey("deployment_status")
                .build();
    }
}
