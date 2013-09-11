package se.skltp.messagebox.deleteanswers.v1.rivtabp20;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.3.0
 * Wed Sep 11 11:15:58 CEST 2013
 * Generated source version: 2.3.0
 * 
 */
 
@WebService(targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswers:1:rivtabp20", name = "DeleteAnswersResponderInterface")
@XmlSeeAlso({org.w3.wsaddressing10.ObjectFactory.class, se.skltp.messagebox.deleteanswersresponder.v1.ObjectFactory.class, iso.v21090.dt.v1.ObjectFactory.class, se.skltp.messagebox.v2.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface DeleteAnswersResponderInterface {

    @WebResult(name = "DeleteAnswersResponse", targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1", partName = "parameters")
    @WebMethod(operationName = "DeleteAnswers", action = "urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1")
    public se.skltp.messagebox.deleteanswersresponder.v1.DeleteAnswersResponseType deleteAnswers(
        @WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true)
        org.w3.wsaddressing10.AttributedURIType logicalAddress,
        @WebParam(partName = "parameters", name = "DeleteAnswers", targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1")
        se.skltp.messagebox.deleteanswersresponder.v1.DeleteAnswersType parameters
    );
}
