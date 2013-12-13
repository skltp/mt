package se.skltp.mb.ws;

import java.net.MalformedURLException;
import java.util.List;
import javax.jms.JMSException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.infrastructure.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.infrastructure.itintegration.messagebox.v1.MessageMetaType;
import se.riv.infrastructure.itintegration.messagebox.v1.MessageStatusType;
import se.riv.infrastructure.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.mb.intsvc.XmlUtils;
import se.skltp.mb.ws.base.BaseIntegrationTests;

import static org.junit.Assert.assertEquals;

/**
 * @author mats.olsson@callistaenterprise.se
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class EndToEndIntegrationTest extends BaseIntegrationTests {

	/**
	 * Complete end-to-end
	 * 
	 * @throws SOAPException
	 * @throws MalformedURLException
	 * @throws TransformerException
	 */
    @Test
    public void testReceiveListGetDelete() throws SOAPException, MalformedURLException, TransformerException, JMSException {

        resetNumberOfLoggedMessages();

        String tkName = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1";
        String targetOrg = "targetOrg";
        // Insert a message

        SOAPMessage soapMessage = createIncomingMessage(tkName, targetOrg);
        SOAPMessage response = sendToReceive(soapMessage);

        NodeList nodes = response.getSOAPBody().getChildNodes();
        assertEquals("Non-empty response: " + XmlUtils.getDocumentAsString(response.getSOAPBody().getOwnerDocument()), 0, nodes.getLength());


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

        assertEquals(3, countNumberOfLogMessages(infoQueueName));
        assertEquals(0, countNumberOfLogMessages(errorQueueName));
    }
}
