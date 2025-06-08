package gcp25.agents;

import com.google.adk.agents.LlmAgent;

import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;
import static gcp25.constants.AgentCommonConstant.SECURITY_SCAN_AGENT_NAME;

public class SecurityScanAgent {
    public static LlmAgent securityScanAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(SECURITY_SCAN_AGENT_NAME)
                .description("Scans code for security issues")
                .instruction("""
                        Perform a static security scan of the Java code.
                        Check for hardcoded credentials, SQL injections, or unsafe system calls.
                        Respond with found issues or confirm that it's clean.
                        """)
                .outputKey("security_scan_result")
                .build();
    }
}
