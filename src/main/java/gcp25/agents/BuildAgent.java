package gcp25.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import gcp25.service.BuildAgentService;
import gcp25.validator.AgentCallback;

import static gcp25.constants.AgentCommonConstant.*;

public class BuildAgent {
    public static LlmAgent buildAgent() {
        return LlmAgent.builder()
                .model(LLM_MODEL_NAME)
                .name(BUILD_AGENT_NAME)
                .description("Build the project")
                .instruction(
                        """
                                You are a build automation agent. Your role is to build a software project that has been cloned from a GitHub repository.
                                                    
                                You should follow this process:
                                                    
                                1. **Expect a local path input.** You will be given the `repo_path` where the GitHub repository has been cloned. This directory contains the root of the source code.
                                                    
                                2. **Determine the build system.** Look for known build files inside the directory:
                                   - `pom.xml` → Maven project
                                   - `build.gradle` or `build.gradle.kts` → Gradle project
                                   - `package.json` → Node.js project
                                   - `Makefile` → Make project
                                   - If no known build file is found, return an error.
                                                    
                                3. **Run the build command.** Based on the detected build system, execute the appropriate build command:
                                   - Maven: `mvn clean install -DskipTests`
                                   - Gradle: `./gradlew clean build -x test` (or `gradle clean build -x test`)
                                   - Node.js: `npm install && npm run build`
                                   - Make: `make`
                                                        
                                4. **Capture the build output.** Store logs and any build success or failure messages.
                                                    
                                5. **Return a result.**
                                   - On success: return `"status": "success"` and `"build_log"` (last few lines of the output).
                                   - On failure: return `"status": "error"` and a `"report"` explaining the failure.
                                """
                )
                .tools(FunctionTool.create(BuildAgentService.class, "buildProjectService"))
                .afterToolCallback(AgentCallback.afterToolCallback)
                .outputKey(BUILD_RESULT_OUTPUT)
                .build();
    }
}
