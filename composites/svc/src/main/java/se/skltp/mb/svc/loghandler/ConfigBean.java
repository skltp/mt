package se.skltp.mb.svc.loghandler;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.spi.ObjectFactory;

import org.springframework.beans.factory.BeanNameAware;

/**
 * Configuration bean to allow external (jndi) setup of JMS.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class ConfigBean implements BeanNameAware, ConfigBeanA {

    private String name;
    private String brokerURL;
    private String infoQueueName;
    private String errorQueueName;
    private String componentName;

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", brokerURL='" + brokerURL + '\'' +
                ", infoQueueName='" + infoQueueName + '\'' +
                ", errorQueueName='" + errorQueueName + '\'' +
                ", componentName='" + componentName + '\'' +
                '}';
    }

    @Override
    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        System.err.println("setBrokerURL " + brokerURL);
        this.brokerURL = brokerURL;
    }

    @Override
    public String getInfoQueueName() {
        return infoQueueName;
    }

    public void setInfoQueueName(String infoQueueName) {
        System.err.println("setInfoQueueName " + infoQueueName);
        this.infoQueueName = infoQueueName;
    }

    @Override
    public String getErrorQueueName() {
        return errorQueueName;
    }

    public void setErrorQueueName(String errorQueueName) {
        System.err.println("setErrorQueueName " + errorQueueName);
        this.errorQueueName = errorQueueName;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        System.err.println("setComponentName " + componentName);
        this.componentName = componentName;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    public static class Factory implements ObjectFactory {

        public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws NamingException {
            System.err.println(getClass().getName() + ": getObjInst");
            // Acquire an instance of our specified bean class
            ConfigBean bean = new ConfigBean();

            // Customize the bean properties from our attributes
            Enumeration addrs = ((Reference) obj).getAll();
            while ( addrs.hasMoreElements() ) {
                RefAddr addr = (RefAddr) addrs.nextElement();
                String fieldName = addr.getType();
                String value = (String) addr.getContent();

                System.err.println("field " + fieldName + " = " + value);

                if ( fieldName.equals("brokerURL") ) {
                    bean.setBrokerURL(value);
                } else if ( fieldName.equals("infoQueueName") ) {
                    bean.setInfoQueueName(value);
                } else if ( fieldName.equals("errorQueueName") ) {
                    bean.setErrorQueueName(value);
                } else if ( fieldName.equals("componentName") ) {
                    bean.setComponentName(value);
                } else if ( fieldName.equals("factory") ) {
                    // ... ignore
                } else {
                    System.err.println("Unknown field " + fieldName + " = " + value);
                }
            }

            // Return the customized instance
            return bean;
        }

    }
}
