package gcp25.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import gcp25.service.SecurityScanAgentService;
import gcp25.validator.AgentOutputValidator;

import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;
import static gcp25.constants.AgentCommonConstant.SECURITY_SCAN_AGENT_NAME;

public class SecurityScanAgent {
    public static LlmAgent securityScanAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(SECURITY_SCAN_AGENT_NAME)
                .description("Scans code for security issues")
                .instruction("""
                        You are a security auditor reviewing code.

                        Perform a static security analysis on the provided source files.
                        Look for the following types of issues:
                        - Hardcoded credentials (e.g., passwords, API keys, tokens)
                        - SQL injection risks (e.g., unparameterized queries)
                        - Insecure use of system resources (e.g., `Runtime.exec`)
                        - Usage of outdated or vulnerable dependencies
                        - Open HTTP connections or unsafe deserialization

                        Be specific — provide:
                        - Filename and line number (if mentioned)
                        - Detected vulnerability or smell
                        - A short recommendation to fix or mitigate it

                        If no issues are found, respond: "No security issues detected in the scanned code."
                        """)
                .tools(FunctionTool.create(SecurityScanAgentService.class, "securityScanService"))
                .afterModelCallbackSync(AgentOutputValidator.afterModelCallbackSync)
                .outputKey("security_scan_result")
                .build();
    }
}
