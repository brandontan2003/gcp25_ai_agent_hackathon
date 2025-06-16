package gcp25.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import gcp25.service.CodeReviewAgentService;
import gcp25.validator.AgentCallback;

import static gcp25.constants.AgentCommonConstant.*;

public class CodeReviewAgent {
    public static LlmAgent codeReviewAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(CODE_REVIEW_AGENT_NAME)
                .description("Reviews code for issues like style and complexity")
                .instruction("""
                        You are a senior software engineer tasked with reviewing source code.
                        Your responsibilities include identifying:
                                                
                        - Code smells,bugs and anti-patterns
                        - Readability and maintainability issues
                        - Violations of language-specific or general best practices
    
                        Only comment on files where improvements are needed.
                        Return your feedback in plain text, grouped by filename.
                                                
                        For each issue found:
                        - Describe the problem clearly
                        - Provide a recommendation or fix suggestion
                        - Keep comments concise and actionable
                                                
                        Do not summarize or repeat the code.
                        If no issues are found in a file, skip that file.
                                                
                        Format:
                        Filename: <filename>
                        Issue: <short description>
                        Suggestion: <how to improve>
                                                
                        Example:
                        Filename: UserService.java
                        Issue: Method `processUser()` is too long and does multiple unrelated tasks
                        Suggestion: Split into smaller, single-responsibility methods
                        """)
                .tools(FunctionTool.create(CodeReviewAgentService.class, "reviewCodeService"))
                .afterModelCallbackSync(AgentCallback.afterModelCallbackSync)
                .outputKey(REVIEW_COMMENTS_OUTPUT)
                .build();
    }
}
