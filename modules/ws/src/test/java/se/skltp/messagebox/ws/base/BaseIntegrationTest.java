package se.skltp.messagebox.ws.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Before;

import se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface;
import se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderService;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface;
import se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderService;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.itintegration.messagebox.ListMessages.v1.ListMessagesResponderInterface;
import se.riv.itintegration.messagebox.ListMessages.v1.ListMessagesResponderService;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageMetaType;

/**
 * Contains convenience methods and settings for integration tests
 * 
 */
public class BaseIntegrationTest implements MessageListener {

	private static final String ENDPOINT_URL = "http://localhost:8081/ws";
	private static final String MT_LOGICAL_ADDRESS = "Inera";
	protected static final String TK_CONTENT = "content";
	protected static final String TK_NODE_NAME = "Question";
	protected static final String CORRELATION_ID = "correlation-id";

	protected static final String tkName = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1";
	protected static final String targetOrg = "targetOrg";
	
	// ActiveMQ
	protected static final String brokerURL="tcp://localhost:61616";
	protected static final String errorQueueName = "MT_DEFAULT_ERROR";
	protected static final String infoQueueName = "MT_DEFAULT_INFO";
	protected int numberOfInfoMessages = 0;
	protected int numberOfErrorMessages = 0;
	protected Connection connection;

	@PersistenceContext
	EntityManager entityManager;
	private MessageConsumer infoConsumer;
	private MessageConsumer errorConsumer;
	

//	static Session session;

	@Before
	public void setup() throws JMSException {
		entityManager.createQuery("delete from MessageMeta").executeUpdate();
		entityManager.createQuery("delete from MessageBody").executeUpdate();
		
		resetNumberOfMessages();
	}
	
	@After
	public void tearDown() throws JMSException {
		infoConsumer.close();
		errorConsumer.close();
		connection.stop();
		
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public BaseIntegrationTest() {
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);

		try {
			connection = connectionFactory.createConnection();
			connection.start();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
	        connection = connectionFactory.createConnection();
	        connection.start();

	        // Listen on the infoQueue
			Destination infoDest = session.createQueue(infoQueueName);
			infoConsumer = session.createConsumer(infoDest);
			infoConsumer.setMessageListener(this);
			
			// Listen on the errorQueue
			Destination errorDest = session.createQueue(errorQueueName) ;
			errorConsumer = session.createConsumer(errorDest);
			errorConsumer.setMessageListener(this);
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
	
	protected void resetNumberOfMessages() {
		numberOfErrorMessages = 0;
		numberOfInfoMessages = 0;
	}

	protected ListMessagesResponseType listMessages(ListMessagesType listParams) throws MalformedURLException {
		URL url = new URL(ENDPOINT_URL + "/ListMessages/1/rivtabp21?wsdl");
		ListMessagesResponderInterface port = new ListMessagesResponderService(url).getListMessagesResponderPort();

		return port.listMessages(MT_LOGICAL_ADDRESS, listParams);
	}

	protected GetMessagesResponseType getMessages(GetMessagesType params) throws MalformedURLException {
		URL url = new URL(ENDPOINT_URL + "/GetMessages/1/rivtabp21?wsdl");
		GetMessagesResponderInterface port = new GetMessagesResponderService(url).getGetMessagesResponderPort();

		return port.getMessages(MT_LOGICAL_ADDRESS, params);
	}

	protected DeleteMessagesResponseType deleteMessages(DeleteMessagesType params) throws MalformedURLException {
		URL url = new URL(ENDPOINT_URL + "/DeleteMessages/1/rivtabp21?wsdl");
		DeleteMessagesResponderInterface port = new DeleteMessagesResponderService(url).getDeleteMessagesResponderPort();

		return port.deleteMessages(MT_LOGICAL_ADDRESS, params);
	}

	protected SOAPMessage sendToReceive(SOAPMessage soapMessage) throws SOAPException, MalformedURLException {
		SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
		SOAPConnection conn = scf.createConnection();

		URL endpoint = new URL(ENDPOINT_URL + "/ReceiveMessage");
		return conn.call(soapMessage, endpoint);
	}

	protected SOAPMessage createIncomingMessage(String tkNAme, String targetOrg) throws SOAPException {
		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage soapMessage = mf.createMessage();
		SOAPHeader header = soapMessage.getSOAPHeader();
		SOAPBody body = soapMessage.getSOAPBody();

		QName logicalAddress = new QName("urn:riv:itintegration:registry:1","LogicalAddress");
		SOAPHeaderElement addressElem = header.addHeaderElement(logicalAddress);
		addressElem.setValue(targetOrg);

		QName tkQName = new QName(tkNAme, TK_NODE_NAME);
		SOAPBodyElement e = body.addBodyElement(tkQName);
		e.setValue(TK_CONTENT);
		
		return soapMessage;
	}

	/**
	 * Counts the number of messages in the message_meta table.
	 * @return number of messages
	 */
	protected int countNumberOfMessages() {
		Long singleResult = (Long) entityManager.createQuery("SELECT count(*) from MessageMeta").getSingleResult();
		return singleResult.intValue();
	}

	/**
	 * Helper method for sending a message to the ReceiveMessage service
	 * 
	 * @return the response as a SoapMessage
	 * @throws MalformedURLException
	 * @throws SOAPException
	 */
	protected SOAPMessage sendOneMessage() throws MalformedURLException, SOAPException {
		SOAPMessage soapMessage = createIncomingMessage(tkName, targetOrg);
		return sendToReceive(soapMessage);
	}
	
	/**
	 * Helper method for sending a message to the ReceiveMessage service and waiting a small
	 * amount of time so that the queues receive the message.
	 * 
	 * @return the response as a SoapMessage
	 * @throws MalformedURLException
	 * @throws SOAPException
	 */
	protected SOAPMessage sendOneMessageAndWait() throws MalformedURLException, SOAPException {
		SOAPMessage soapMessage = sendOneMessage();
		try { Thread.sleep(200); } catch (InterruptedException e) {}
		
		return soapMessage;
	}

	/**
	 * Helper method for getting the id of the only existing message by calling
	 * ListMessage
	 * 
	 * @return the message id
	 * @throws MalformedURLException
	 */
	protected long getMessageId() throws MalformedURLException {

		// Check for 1 message
		ListMessagesResponseType listResponse = listMessages(new ListMessagesType());

		// Response of ListMessages
		List<MessageMetaType> metas = listResponse.getMessageMetas();

		// Message details
		MessageMetaType meta = metas.get(0);
		return meta.getMessageId();
	}
	
	
	protected int countNumberOfLogMessages(String queueName) throws JMSException {
		
		//Give the message some time to get to the consumer.
		try { Thread.sleep(200); } catch (InterruptedException e) {}
		
		if (queueName == infoQueueName) {
			return numberOfInfoMessages;
		} else if (queueName == errorQueueName) {
			return numberOfErrorMessages;
		} else {
			return -1;
		}
	}
	

	@Override
	public void onMessage(Message message) {
		
		try {
			String queueName = message.getJMSDestination().toString();
			if (queueName.endsWith(infoQueueName)) {
				numberOfInfoMessages++;
			} else if (queueName.endsWith(errorQueueName)) {
				numberOfErrorMessages++;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
