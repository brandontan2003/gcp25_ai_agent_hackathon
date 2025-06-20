package gcp25.agents;

import com.gcp.agent25.common.core.properties.TicketEndpointProperties;
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
        verifyTicketEndpointLoaded();
        ROOT_AGENT = initAgent();
    }

    private static void verifyTicketEndpointLoaded() {
        var context = SpringContextHolder.getContext();
        if (context.containsBean("ticketEndpointProperties")) {
            var props = context.getBean("ticketEndpointProperties", TicketEndpointProperties.class);
            System.out.println("createTicket endpoint: " + props.getCreateTicket().toUrl());
        } else {
            System.err.println("TicketEndpointProperties bean NOT found!");
        }
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
