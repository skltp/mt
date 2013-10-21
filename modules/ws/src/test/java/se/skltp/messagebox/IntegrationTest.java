package se.skltp.messagebox;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.w3c.dom.Node;
import se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface;
import se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderService;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface;
import se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderService;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.itintegration.messagebox.ListMessages.v1.ListMessagesResponderInterface;
import se.riv.itintegration.messagebox.ListMessages.v1.ListMessagesResponderService;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageMetaType;
import se.riv.itintegration.messagebox.v1.MessageStatusType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.services.XmlUtils;

import static org.junit.Assert.assertEquals;

/**
 * @author mats.olsson@callistaenterprise.se
 */
public class IntegrationTest {

    private static final String ENDPOINT_URL = "http://localhost:8081/ws";
    private static final String MT_LOGICAL_ADDRESS = "Inera";
    private static final String TK_CONTENT = "content";
    private static final String TK_NODE_NAME = "Question";

    @Test
    public void testReciveListGetDelete() throws SOAPException, MalformedURLException, TransformerException {
        String tkName = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1";
        String targetOrg = "targetOrg";

        // Insert a message

        SOAPMessage soapMessage = createIncomingMessage(tkName, targetOrg);
        SOAPMessage response = sendToReceive(soapMessage);

        assertEquals(0, response.getSOAPBody().getChildNodes().getLength());


        // List the inserted message

        ListMessagesType listParams = new ListMessagesType();
        ListMessagesResponseType listResponse = listMessages(listParams);

        assertEquals(ResultCodeEnum.OK, listResponse.getResult().getCode());
        List<MessageMetaType> metas = listResponse.getMessageMetas();
        assertEquals(1, metas.size());
        MessageMetaType meta = metas.get(0);
        assertEquals(targetOrg, meta.getTargetOrganization());
        assertEquals(tkName, meta.getServiceContractType());
        assertEquals(MessageStatusType.RECEIVED, meta.getStatus());


        // Get the message

        GetMessagesType getParams = new GetMessagesType();
        long messageId = metas.get(0).getMessageId();
        getParams.getMessageIds().add(messageId);

        GetMessagesResponseType getResponse = getMessages(getParams);

        assertEquals(ResultCodeEnum.OK, getResponse.getResult().getCode());
        assertEquals(1, getResponse.getResponses().size());
        ResponseType responseType = getResponse.getResponses().get(0);
        assertEquals(messageId, responseType.getMessageId());
        assertEquals(targetOrg, responseType.getTargetOrganization());
        assertEquals(tkName, responseType.getServiceContractType().getServiceContractNamespace());

        Node node = XmlUtils.getStringAsDom(responseType.getMessage()).getNode();
        assertEquals(1, node.getChildNodes().getLength());
        org.w3c.dom.Node contentNode = node.getFirstChild();
        assertEquals(TK_NODE_NAME, contentNode.getLocalName());
        assertEquals(tkName, contentNode.getNamespaceURI());
        assertEquals(TK_CONTENT, contentNode.getTextContent());


        // Delete the message

        DeleteMessagesType deleteParams = new DeleteMessagesType();
        deleteParams.getMessageIds().add(messageId);

        DeleteMessagesResponseType deleteResponse = deleteMessages(deleteParams);

        assertEquals(ResultCodeEnum.OK, deleteResponse.getResult().getCode());
        assertEquals(1, deleteResponse.getDeletedIds().size());
        assertEquals(messageId, (long) deleteResponse.getDeletedIds().get(0));


        // verify that there are no more messages

        listResponse = listMessages(new ListMessagesType());

        assertEquals(ResultCodeEnum.OK, listResponse.getResult().getCode());
        assertEquals(0, listResponse.getMessageMetas().size());
    }

    private ListMessagesResponseType listMessages(ListMessagesType listParams) throws MalformedURLException {
        URL url = new URL(ENDPOINT_URL + "/ListMessages/1/rivtabp21?wsdl");
        ListMessagesResponderInterface port = new ListMessagesResponderService(url).getListMessagesResponderPort();

        return port.listMessages(MT_LOGICAL_ADDRESS, listParams);
    }

    private GetMessagesResponseType getMessages(GetMessagesType params) throws MalformedURLException {
        URL url = new URL(ENDPOINT_URL + "/GetMessages/1/rivtabp21?wsdl");
        GetMessagesResponderInterface port = new GetMessagesResponderService(url).getGetMessagesResponderPort();

        return port.getMessages(MT_LOGICAL_ADDRESS, params);
    }

    private DeleteMessagesResponseType deleteMessages(DeleteMessagesType params) throws MalformedURLException {
        URL url = new URL(ENDPOINT_URL + "/DeleteMessages/1/rivtabp21?wsdl");
        DeleteMessagesResponderInterface port = new DeleteMessagesResponderService(url).getDeleteMessagesResponderPort();

        return port.deleteMessages(MT_LOGICAL_ADDRESS, params);
    }

    private SOAPMessage sendToReceive(SOAPMessage soapMessage) throws SOAPException, MalformedURLException {
        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection conn = scf.createConnection();

        URL endpoint = new URL(ENDPOINT_URL + "/ReceiveMessage");
        return conn.call(soapMessage, endpoint);
    }

    private SOAPMessage createIncomingMessage(String tkNAme, String targetOrg) throws SOAPException {
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
}
