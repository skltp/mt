package se.skltp.messagebox.DeleteMessages.v1.rivtabp21;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.3.0
 * Thu Sep 12 16:56:08 CEST 2013
 * Generated source version: 2.3.0
 * 
 */
 
@WebService(targetNamespace = "urn:riv:itintegration:messagebox:DeleteMessages:1:rivtabp21", name = "DeleteMessagesResponderInterface")
@XmlSeeAlso({se.skltp.riv.itintegration.messagebox.v1.ObjectFactory.class, se.skltp.riv.itintegration.registry.v1.ObjectFactory.class, se.skltp.messagebox.DeleteMessagesresponder.v1.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface DeleteMessagesResponderInterface {

    @WebResult(name = "DeleteMessagesResponse", targetNamespace = "urn:riv:itintegration:messagebox:DeleteMessagesResponder:1", partName = "parameters")
    @WebMethod(operationName = "DeleteMessages", action = "urn:riv:itintegration:messagebox:DeleteMessagesResponder:1:DeleteMessages")
    public se.skltp.messagebox.DeleteMessagesresponder.v1.DeleteMessagesResponseType deleteMessages(
        @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true)
        java.lang.String logicalAddress,
        @WebParam(partName = "parameters", name = "DeleteMessages", targetNamespace = "urn:riv:itintegration:messagebox:DeleteMessagesResponder:1")
        se.skltp.messagebox.DeleteMessagesresponder.v1.DeleteMessagesType parameters
    );
}
