package gcp25.agents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import gcp25.configurations.SpringContextHolder;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static gcp25.constants.AgentCommonConstant.PROJECT_MANAGER_AGENT_NAME;
import static gcp25.constants.AgentCommonConstant.USER_ID;

public class ProjectManagerAgent {

    public static final BaseAgent ROOT_AGENT;

    static {
        SpringContextHolder.getContext();
        ROOT_AGENT = initAgent();
    }

    private static BaseAgent initAgent() {
        return SequentialAgent.builder()
                .name(PROJECT_MANAGER_AGENT_NAME)
                .description("Manages and orchestrates the SDLC pipeline.")
                .subAgents(RepoCloneAgent.repoCloneAgent(), BuildAgent.buildAgent(),
                        CodeReviewAgent.codeReviewAgent(), SecurityScanAgent.securityScanAgent())
                .build();
    }

    public static void main(String[] args) {
        ProjectManagerAgent projectManagerAgent = new ProjectManagerAgent();
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }
                projectManagerAgent.runAgent(userInput);
            }
        }
    }


    public void runAgent(String prompt) {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT, PROJECT_MANAGER_AGENT_NAME);

        Session session = runner.sessionService().createSession(PROJECT_MANAGER_AGENT_NAME, USER_ID).blockingGet();

        Content userMessage = Content.fromParts(Part.fromText(prompt));
        Flowable<Event> eventStream = runner.runAsync(USER_ID, session.id(), userMessage);

        eventStream.blockingForEach(
                event -> {
                    if (event.finalResponse()) {
                        System.out.println("Final Response :::::::::: " + event.stringifyContent());
                    }
                });
    }
}
