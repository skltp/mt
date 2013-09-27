package se.skltp.messagebox.controller;

/**
 * Human-readable view of a service contract namespace.
 * <p/>
 * Use fullName in tooltips.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class ServiceContractView {
    String serviceContract;
    String shortName;

    ServiceContractView(String serviceContract) {
        this.serviceContract = shortName = serviceContract;
        String[] parts = serviceContract.split(":");
        if ( parts.length > 2 ) {
            shortName = parts[parts.length - 2];
        }
    }

    @Override
    public String toString() {
        return shortName;
    }

    public String getFullName() {
        return serviceContract;
    }

    public String getShortName() {
        return shortName;
    }
}
