package se.skltp.mb.svc.loghandler;

/**
 * Configuration bean to allow external (jndi) setup of JMS.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class ConfigBean {

    private String brokerURL;
    private String infoQueueName;
    private String errorQueueName;
    private String componentName;
    private String errorThreshold;
    private String infoThreshold;
    private String active;

    @Override
    public String toString() {
        return "ConfigBean{" +
                "brokerURL='" + brokerURL + '\'' +
                ", infoQueueName='" + infoQueueName + '\'' +
                ", errorQueueName='" + errorQueueName + '\'' +
                ", componentName='" + componentName + '\'' +
                ", errorThreshold='" + errorThreshold + '\'' +
                ", infoThreshold='" + infoThreshold + '\'' +
                ", active='" + active + '\'' +
                '}';
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getInfoQueueName() {
        return infoQueueName;
    }

    public void setInfoQueueName(String infoQueueName) {
        this.infoQueueName = infoQueueName;
    }

    public String getErrorQueueName() {
        return errorQueueName;
    }

    public void setErrorQueueName(String errorQueueName) {
        this.errorQueueName = errorQueueName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }


    public String getErrorThreshold() {
        return errorThreshold;
    }

    public void setErrorThreshold(String errorThreshold) {
        this.errorThreshold = errorThreshold;
    }

    public String getInfoThreshold() {
        return infoThreshold;
    }

    public void setInfoThreshold(String infoThreshold) {
        this.infoThreshold = infoThreshold;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
