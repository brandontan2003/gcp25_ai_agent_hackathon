package gcp25.configurations;

import com.gcp.agent25.common.core.service.TicketWebClientService;
import gcp25.utils.TicketServiceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class SpringContextHolder {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    static {
        try {
            context.getEnvironment().getPropertySources().addLast(
                    new ResourcePropertySource("classpath:application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.register(ApplicationConfiguration.class);
        context.refresh();

        TicketServiceUtil.setService(context.getBean(TicketWebClientService.class));
    }

    public static ApplicationContext getContext() {
        return context;
    }

}
