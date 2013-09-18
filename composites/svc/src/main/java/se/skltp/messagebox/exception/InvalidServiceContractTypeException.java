package se.skltp.messagebox.exception;

/**
 * Exception thrown when a service contract can't be handled by the messagebox service.
 *
 * Only contracts that can be mapped to a Request/Response (Uppdrag/Resultat) pattern can
 * be handled.
 *
 * @author mats.olsson@callistaenterprise.se
 *
 */
public class InvalidServiceContractTypeException extends Exception {


}
