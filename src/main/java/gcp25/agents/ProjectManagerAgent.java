package gcp25.agents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static gcp25.constants.AgentCommonConstant.PROJECT_MANAGER_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.USER_ID;

public class ProjectManagerAgent {

    public static BaseAgent ROOT_AGENT = initAgent();

    private static BaseAgent initAgent() {
        return SequentialAgent.builder()
                .name(PROJECT_MANAGER_AGENT_NAME)
                .description("Manages and orchestrates the SDLC pipeline.")
                .subAgents(BuildAgent.buildAgent(), CodeReviewAgent.codeReviewAgent(), TestAgent.testAgent(),
                        SecurityScanAgent.securityScanAgent(), DeploymentAgent.deploymentAgent())
                .build();
    }

    public static void main(String[] args) {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);

        Session session =
                runner
                        .sessionService()
                        .createSession(PROJECT_MANAGER_AGENT_NAME, USER_ID)
                        .blockingGet();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(USER_ID, session.id(), userMsg);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> System.out.println(event.stringifyContent()));
            }
        }
    }
}
