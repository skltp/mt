package se.skltp.messagebox;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.List;

import javax.jms.JMSException;
import javax.xml.soap.SOAPException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageMetaType;
import se.riv.itintegration.messagebox.v1.MessageStatusType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.base.BaseIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ListMessagesIntegrationTest extends BaseIntegrationTest {

	@Test
	@Transactional
	public void list_OK() throws MalformedURLException, SOAPException, JMSException {

		sendOneMessageAndWait();
		resetNumberOfMessages();
		
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
        
        
        // Nothing should be logged to the queues
        // List should not generate any log messages
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}
	
	@Test
	@Transactional
	public void list_OK_multipleMessages() throws MalformedURLException, SOAPException, JMSException {
		sendOneMessage();
		sendOneMessageAndWait();
		resetNumberOfMessages();
		
        // Check for 2 messages
        ListMessagesResponseType listResponse = listMessages(new ListMessagesType());

        // Response of ListMessages
        assertEquals(ResultCodeEnum.OK, listResponse.getResult().getCode());
        List<MessageMetaType> metas = listResponse.getMessageMetas();
        assertEquals(2, metas.size()); // Should be only 2 messages
        
        // Nothing should be logged to the queues
        // List should not generate any log messages
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}

	@Test
	@Transactional
	public void list_OK_none_messages() throws MalformedURLException, SOAPException, JMSException {
		
        // Check for messages
        ListMessagesResponseType listResponse = listMessages(new ListMessagesType());

        // Response of ListMessages - should be OK even if there is no messages to list
        assertEquals(ResultCodeEnum.OK, listResponse.getResult().getCode());
        assertEquals(0, listResponse.getMessageMetas().size()); // Should be only 2 messages
     
        // Nothing should be logged to the queues
        // List should not generate any log messages
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}   
        
	
	
	
	
}
