package se.skltp.messagebox.ws;

import java.net.MalformedURLException;
import javax.jms.JMSException;
import javax.xml.soap.SOAPException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.ws.base.BaseIntegrationTest;
import se.skltp.messagebox.intsvc.DeleteMessagesImpl;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class DeleteMessagesIntegrationTest extends BaseIntegrationTest {

	@Test
	@Transactional
	public void delete_OK() throws MalformedURLException, SOAPException, JMSException {
		sendOneMessageAndWait();
		resetNumberOfMessages();
		
		long messageId = getMessageId();
		
        DeleteMessagesType deleteParams = new DeleteMessagesType();
        deleteParams.getMessageIds().add(messageId);
        DeleteMessagesResponseType deleteResponse = deleteMessages(deleteParams);

        assertEquals(ResultCodeEnum.OK, deleteResponse.getResult().getCode());
        assertEquals(1, deleteResponse.getDeletedIds().size());
        assertEquals(messageId, (long) deleteResponse.getDeletedIds().get(0));
		
        // Count number of messages in db - should be zero messages left
        assertEquals(0, countNumberOfMessages());
        
        // Should only result in one message in the info log
        assertEquals(1, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
	}
	
	@Test
	@Transactional
	public void delete_ERR_delete_invalid_msg_id() throws MalformedURLException, SOAPException, JMSException {
		sendOneMessageAndWait();
		resetNumberOfMessages();
		
        DeleteMessagesType deleteParams = new DeleteMessagesType();
        deleteParams.getMessageIds().add((long) Integer.MAX_VALUE);
        DeleteMessagesResponseType deleteResponse = deleteMessages(deleteParams);
		
        
        assertEquals(ResultCodeEnum.INFO, deleteResponse.getResult().getCode());
        assertEquals(DeleteMessagesImpl.INCOMPLETE_ERROR_MESSAGE, deleteResponse.getResult().getErrorMessage());
        
        // There should be any deleted ids
        assertEquals(0, deleteResponse.getDeletedIds().size());
        
        // Count number of messages in db - should still be 1 message left
        assertEquals(1, countNumberOfMessages());
        
        // Should only result in one message in the info log
        assertEquals(0, countNumberOfLogMessages(infoQueueName));
        assertEquals(1, countNumberOfLogMessages(errorQueueName));
	}
	
}
