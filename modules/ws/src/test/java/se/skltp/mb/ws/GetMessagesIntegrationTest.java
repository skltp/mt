package se.skltp.mb.ws;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import javax.jms.JMSException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageStatusType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.mb.ws.base.BaseIntegrationTest;
import se.skltp.mb.intsvc.GetMessagesImpl;
import se.skltp.mb.intsvc.XmlUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class GetMessagesIntegrationTest extends BaseIntegrationTest {

	@Test
	public void get_message_OK() throws MalformedURLException, SOAPException, TransformerException, JMSException {
		
		sendOneMessageAndWait();
		resetNumberOfLoggedMessages();
		
		long messageIdToFind = getMessageId();

		GetMessagesType getParams = new GetMessagesType();
		getParams.getMessageIds().add(messageIdToFind);

		GetMessagesResponseType getResponse = getMessages(getParams);
		ResponseType responseType = getResponse.getResponses().get(0);

		// Test that we got a OK response with only one message
		assertEquals(ResultCodeEnum.OK, getResponse.getResult().getCode());
		assertEquals(1, getResponse.getResponses().size());

		assertEquals(messageIdToFind, responseType.getMessageId());
		assertEquals(targetOrg, responseType.getTargetOrganization());
		assertEquals(tkName, responseType.getServiceContractType().getServiceContractNamespace());

		// Verify that the sent message is the same as the one we got
		Node node = XmlUtils.getStringAsDom(responseType.getMessage()).getNode();
		org.w3c.dom.Node contentNode = node.getFirstChild();
		
		assertEquals(TK_NODE_NAME, contentNode.getLocalName());
		assertEquals(tkName, contentNode.getNamespaceURI());
		assertEquals(TK_CONTENT, contentNode.getTextContent());
		
        // Get should only generate one info log message per message
        assertEquals(1, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}
	

	@Test
	public void get_message_OK_should_update_message_status_after_read() throws MalformedURLException, SOAPException, JMSException {
		sendOneMessageAndWait();
		resetNumberOfLoggedMessages();
		
		long messageIdToFind = getMessageId();
		
		// get 
		GetMessagesType params = new GetMessagesType();
		params.getMessageIds().add(messageIdToFind);
		getMessages(params);
		
		// list
		ListMessagesResponseType listMessages = listMessages(new ListMessagesType());
		assertEquals(MessageStatusType.RETRIEVED, listMessages.getMessageMetas().get(0).getStatus());
		assertEquals(messageIdToFind, listMessages.getMessageMetas().get(0).getMessageId());
		
        //  Get should only generate one info log message per message
        assertEquals(1, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}
	
	@Test
	public void get_message_ERR_get_invalid_message_id() throws MalformedURLException, SOAPException, JMSException {
		sendOneMessageAndWait();
		resetNumberOfLoggedMessages();
		
		// get 
		GetMessagesType params = new GetMessagesType();
		params.getMessageIds().add((long) Integer.MAX_VALUE);
		GetMessagesResponseType messages = getMessages(params);
		
		// Should return result code  "INFO" 
		assertEquals(ResultCodeEnum.INFO, messages.getResult().getCode());
		assertEquals(GetMessagesImpl.INCOMPLETE_ERROR_MESSAGE, messages.getResult().getErrorMessage());
		
        // This operation should only result in one error message
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(1, countNumberOfLogMessages(errorQueueName));
	}
	
	
}
