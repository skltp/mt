package se.skltp.messagebox.ws;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.List;

import javax.jms.JMSException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageMetaType;
import se.riv.itintegration.messagebox.v1.MessageStatusType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.ws.base.BaseIntegrationTest;
import se.skltp.messagebox.intsvc.ReceiveErrorCode;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ReceiveMessagesIntegrationTest extends BaseIntegrationTest {

	
	
	/**
	 * Send a message
	 * @throws JMSException 
	 * @throws InterruptedException 
	 */
	@Test
	public void receive_OK() throws SOAPException, MalformedURLException, JMSException {
		
        SOAPMessage soapMessage = createIncomingMessage(tkName, targetOrg);
        SOAPMessage response = sendToReceive(soapMessage);
        
        // Should be empty
        assertEquals(0, response.getSOAPBody().getChildNodes().getLength());

        
        // Should be 1 log message on the infoQueue
        assertEquals(1, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
        resetNumberOfLoggedMessages();
        

        // Check for 1 message
        ListMessagesResponseType listResponse = listMessages(new ListMessagesType());

        // Response of ListMessages
        assertEquals(ResultCodeEnum.OK, listResponse.getResult().getCode());
        List<MessageMetaType> metas = listResponse.getMessageMetas();
        assertEquals(1, metas.size()); // Should be only 1 message
        
        // Message details
        MessageMetaType meta = metas.get(0);
        assertEquals(targetOrg, meta.getTargetOrganization());
        assertEquals(tkName, meta.getServiceContractType());
        assertEquals(MessageStatusType.RECEIVED, meta.getStatus());
        
        // List should not generate any log messages
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}
	
	
	@Test
	public void receive_ERR_r2_should_return_soap_fault() throws MalformedURLException, SOAPException, JMSException  {
		
        SOAPMessage soapMessage;
		soapMessage = createIncomingMessage(tkName, "");
		SOAPMessage response = sendToReceive(soapMessage);
		
		// Should get a soap fault with error code MB00001
		String faultString = response.getSOAPPart().getEnvelope().getBody().getFault().getFaultString();
		assertEquals(ReceiveErrorCode.MB0001.toString(), faultString);

		// There should not be any messages in the database
		assertEquals(0, countNumberOfMessages());
		
		// Should be no info message and two error messages
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(2, countNumberOfLogMessages(errorQueueName));
        
	}
	
	
}
