package se.skltp.mb.ws.base;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderService;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.infrastructure.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface;
import se.riv.infrastructure.itintegration.messagebox.GetMessages.v1.GetMessagesResponderService;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.infrastructure.itintegration.messagebox.ListMessages.v1.ListMessagesResponderInterface;
import se.riv.infrastructure.itintegration.messagebox.ListMessages.v1.ListMessagesResponderService;
import se.riv.infrastructure.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.infrastructure.itintegration.messagebox.v1.MessageMetaType;
import se.skltp.mb.intsvc.XmlUtils;

/**
 * Contains convenience methods and settings for integration tests
 */
public abstract class BaseIntegrationTests extends AbstractTransactionalJUnit4SpringContextTests implements MessageListener {

    private static final Logger LOG = Logger.getLogger(BaseIntegrationTests.class.getName());
    private static final String ENDPOINT_URL = "http://localhost:8081";
    private static final int ENDPOINT_PORT = 8081;
    private static final String MT_LOGICAL_ADDRESS = "Inera";
    protected static final String TK_CONTENT = "content";
    protected static final String TK_NODE_NAME = "Question";
    protected static final String CORRELATION_ID = "correlation-id";

    protected static final String tkName = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1";
    protected static final String targetOrg = "targetOrg";

    // ActiveMQ
    protected static final String brokerURL = "tcp://localhost:62626";
    protected static final String errorQueueName = "MT_DEFAULT_ERROR";
    protected static final String infoQueueName = "MT_DEFAULT_INFO";

    protected int numberOfInfoMessages = 0;
    protected int numberOfErrorMessages = 0;
    protected Connection connection;

    protected MessageConsumer infoConsumer;
    protected MessageConsumer errorConsumer;

    protected static BrokerService broker;
    protected static Server server;
    private java.sql.Connection dbConnection;
    public boolean showMessage = false;

    @BeforeClass
    public static void startJetty() throws Exception {

        server = new Server(ENDPOINT_PORT);

        // Working direcory when runnning tests needs to be applications/ws
        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/test/embedded-webapp");
        context.setContextPath("/");
        server.setHandler(context);

        server.start();
    }

    @AfterClass
    public static void stopJetty() throws Exception {
        if ( server != null ) {
            server.stop();
            server = null;
        }
    }


    @BeforeClass
    public static void startAMQ() throws Exception {
        broker = new BrokerService();
        broker.setDataDirectory("/tmp/mt-activemq/");
        broker.addConnector(brokerURL);
        broker.start();
    }

    @AfterClass
    public static void stopAMQ() throws Exception {
        if ( broker != null ) {
            broker.stop();
            broker = null;
        }
    }


    @BeforeTransaction
    public void truncateTables() throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.execute("DELETE FROM message_body");
        statement.execute("DELETE FROM message_meta");
        statement.close();
    }


    @Before
    public void setup() throws JMSException, IOException {
        broker.deleteAllMessages();
        resetNumberOfLoggedMessages();
    }


    @After
    public void tearDown() throws JMSException, SQLException, IOException {

        broker.deleteAllMessages();

        infoConsumer.close();
        errorConsumer.close();
        connection.stop();

        // close down the db connection
        dbConnection.close();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }
    }


    public BaseIntegrationTests() {
        System.err.println("Creating " + getClass().getName());

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Listen on the infoQueue
            Destination infoDest = session.createQueue(infoQueueName);
            infoConsumer = session.createConsumer(infoDest);
            infoConsumer.setMessageListener(this);

            // Listen on the errorQueue
            Destination errorDest = session.createQueue(errorQueueName);
            errorConsumer = session.createConsumer(errorDest);
            errorConsumer.setMessageListener(this);

            setupJndiEnvironment();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        // Set up DB-connection
        try {
            dbConnection = DriverManager.getConnection("jdbc:hsqldb:mem:.", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupJndiEnvironment() throws NamingException {
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        Object config = applicationContext.getBean("jmsConfig");
        builder.bind("java:comp/env/bean/MessageboxJmsConfig", config);
        builder.activate();
    }

    protected void resetNumberOfLoggedMessages() {
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

        QName logicalAddress = new QName("urn:riv:itintegration:registry:1", "LogicalAddress");
        SOAPHeaderElement addressElem = header.addHeaderElement(logicalAddress);
        addressElem.setValue(targetOrg);

        QName tkQName = new QName(tkNAme, TK_NODE_NAME);
        SOAPBodyElement e = body.addBodyElement(tkQName);
        e.setValue(TK_CONTENT);

        return soapMessage;
    }

    /**
     * Counts the number of messages in the message_meta table.
     *
     * @return number of messages
     * @throws SQLException
     */
    protected int countNumberOfMessages() throws SQLException {

        Statement statement = dbConnection.createStatement();
        ResultSet executeQuery = statement.executeQuery("SELECT count(*) FROM message_meta");
        executeQuery.next();

        return executeQuery.getInt(1);
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
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }

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
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        if ( infoQueueName.equals(queueName) ) {
            return numberOfInfoMessages;
        } else if ( errorQueueName.equals(queueName) ) {
            return numberOfErrorMessages;
        } else {
            return -1;
        }
    }


    @Override
    public void onMessage(Message message) {
        if ( message instanceof TextMessage && showMessage ) {
            TextMessage txtMsg = (TextMessage) message;
            try {
                String text = XmlUtils.prettyFormatXmlText(txtMsg.getText());

                LOG.info("--- Message in " + Thread.currentThread().getName() + " ----:\n" + text + "\n----------");
            } catch (JMSException e) {
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String queueName = message.getJMSDestination().toString();
            if ( queueName.endsWith(infoQueueName) ) {
                numberOfInfoMessages++;
            } else if ( queueName.endsWith(errorQueueName) ) {
                numberOfErrorMessages++;
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
