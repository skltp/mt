package se.skltp.messagebox.ListMessages.v1.rivtabp21;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.3.0
 * Mon Sep 16 13:57:17 CEST 2013
 * Generated source version: 2.3.0
 * 
 */
 
@WebService(targetNamespace = "urn:riv:itintegration:messagebox:ListMessages:1:rivtabp21", name = "ListMessagesResponderInterface")
@XmlSeeAlso({se.skltp.messagebox.ListMessagesresponder.v1.ObjectFactory.class, se.skltp.riv.itintegration.messagebox.v1.ObjectFactory.class, se.skltp.riv.itintegration.registry.v1.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ListMessagesResponderInterface {

    @WebResult(name = "ListMessagesResponse", targetNamespace = "urn:riv:itintegration:messagebox:ListMessagesResponder:1", partName = "parameters")
    @WebMethod(operationName = "ListMessages", action = "urn:riv:itintegration:messagebox:ListMessagesResponder:1:ListMessages")
    public se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesResponseType listMessages(
        @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true)
        java.lang.String logicalAddress,
        @WebParam(partName = "parameters", name = "ListMessages", targetNamespace = "urn:riv:itintegration:messagebox:ListMessagesResponder:1")
        se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesType parameters
    );
}
