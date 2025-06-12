package gcp25.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import gcp25.service.CodeReviewAgentService;
import gcp25.validator.AgentOutputValidator;

import static gcp25.constants.AgentCommonConstant.CODE_REVIEW_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;

public class CodeReviewAgent {
    public static LlmAgent codeReviewAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(CODE_REVIEW_AGENT_NAME)
                .description("Reviews code for issues like style and complexity")
                .instruction("""
                        You are a senior code reviewer. Review the following source code for:
                        - Code smells, bugs, and anti-patterns
                        - Readability and maintainability
                        - Adherence to best practices

                        Provide file-specific feedback using markdown. Focus only on the content shown.
                        """)
                .outputKey("review_comments")
                .tools(FunctionTool.create(CodeReviewAgentService.class, "reviewCodeService"))
                .build();
    }
}
