package gcp25.utils;

import com.gcp.agent25.common.core.service.TicketWebClientService;
import org.springframework.stereotype.Component;

@Component
public class TicketServiceUtil {

    private static TicketWebClientService ticketService;

    public static void setService(TicketWebClientService service) {
        ticketService = service;
    }

    public static TicketWebClientService getService() {
        return ticketService;
    }

}
