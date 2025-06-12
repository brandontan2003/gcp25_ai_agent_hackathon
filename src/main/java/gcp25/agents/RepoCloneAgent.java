package gcp25.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import gcp25.service.RepoCloneAgentService;
import gcp25.validator.AgentOutputValidator;

import static gcp25.constants.AgentCommonConstant.GITHUB_REPOSITORY_CLONE_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.LLM_MODEL_NAME;

public class RepoCloneAgent {
    public static LlmAgent repoCloneAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(GITHUB_REPOSITORY_CLONE_AGENT_NAME)
                .description("Clones a GitHub repo into a local temp directory")
                .instruction("""
                        You are a skilled software automation agent designed to handle GitHub repositories.

                        Your main responsibility is to:
                        - Clone public GitHub repositories into a temporary local directory.
                        - Validate that the provided URL is a valid GitHub repository.
                        - Return the full local path where the repository was cloned.

                        Your process should follow these steps:

                        1. **Understand the user’s input.** Expect a GitHub repository URL (e.g., https://github.com/org/project). If the URL is missing or invalid, return an error.
                            
                        2. **Validate the GitHub URL.** Ensure the URL is in the correct format and points to a public repo. Accept URLs with or without `.git` at the end.
                            
                        3. **Create a temporary directory.** Use a system-generated temp path to avoid collisions and ensure isolation.
                            
                        4. **Clone the repository.** Run a `git clone` command to copy the repository into the temp folder.
                            
                        5. **Check for errors.** If cloning fails (e.g., 404, no access), return a descriptive error message.
                            
                        6. **Return the local path.** If successful, output the path to the cloned repo as a string, e.g., `/tmp/repo_abc123`.

                        Always keep the output key as `repo_path`, which can be used by downstream agents for further processing like building or analyzing the codebase.
                        """)
                .tools(
                        FunctionTool.create(RepoCloneAgentService.class, "cloneRepoService")
                )
                .afterModelCallbackSync(AgentOutputValidator.afterAgentCallback)
                .afterToolCallback(AgentOutputValidator.afterToolCallback)
                .outputKey("repo_path")
                .build();
    }
}
