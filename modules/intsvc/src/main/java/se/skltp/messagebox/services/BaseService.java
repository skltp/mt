package se.skltp.messagebox.services;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import se.skltp.messagebox.core.service.MessageService;

/**
 * Base class for services
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class BaseService {
    // the name of the Http-Header for the callings system authentification id
    public static String HSA_ID_HEADER_NAME = "x-rivta-original-serviceconsumer-hsaid";
    protected MessageService messageService;
    protected WebServiceContext wsContext;

    @Resource
    public void setWsContext(WebServiceContext wsContext) {
        this.wsContext = wsContext;
    }

    @Resource
    public void setMessageService(MessageService MessageService) {
        this.messageService = MessageService;
    }

    /**
     * Extract the hsa-id for the caller from the http-header.
     *
     * The hsa-id for caller is placed by the VP in the http-header of the call.
     *
     * @return the hsa-id from the caller
     */
    protected String extractCallerIdFromRequest() {
        MessageContext msgCtxt = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) msgCtxt.get(MessageContext.SERVLET_REQUEST);
        return req.getHeader(HSA_ID_HEADER_NAME);
    }
}
