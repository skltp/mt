/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera MessageService (http://code.google.com/p/inera-message).
 *
 * Inera MessageService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera MessageService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.skltp.mb.intsvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.riv.itintegration.monitoring.PingForConfiguration.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.ConfigurationType;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.PingForConfigurationType;
import se.skltp.mb.svc.TimeDelta;
import se.skltp.mb.types.StatusReport;
import se.skltp.mb.types.entity.Statistic;
import se.skltp.mb.svc.services.StatisticService;
import se.skltp.mb.types.services.TimeService;

/**
 * PingForConfiguration is used to verify that a server is up and running.
 * <p/>
 * It consists of a mandatory part where you have to fill in the version and the current time.
 * <p/>
 * Apart from that, a number of properties are returned that are supposed to reflect the status of the
 * service, and make sure it is still running.
 * <p/>
 * We summarize the information we show on the website, ie status on the current message queue and statistics for
 * how much we have delivered today, and send it back on a per-targetSystem basis. This means that we send back 2 property
 * variables (-currentQueueSize and -currentOldestMessage) for each targetSystem with un-delivered messages, and two more for each
 * targetSystem that has taken deliveres of message today.
 * <p/>
 * We should perhaps keep track of the last time a target system has made a "List" request with us so we can report
 * that.
 * <p/>
 * The optional service contract parameter that can be part of the ping request is ignored for now. We could use it
 * to filter the statistics and status reports, but it seems unnecessary.
 * <p/>
 * See http://rivta.googlecode.com/svn/ServiceInteractions/riv/itintegration/monitoring/trunk/docs/Tjänstekontrakt%20Itintegration%20Monitoring%20-%20beskrivning.doc
 *
 * @author mats.olsson@callistaenterprise.se
 */
@WebService(serviceName = "PingForConfigurationResponderService",
        endpointInterface = "se.riv.itintegration.monitoring.PingForConfiguration.v1.PingForConfigurationResponderInterface",
        portName = "PingForConfigurationResponderPort",
        targetNamespace = "urn:riv:itintegration:monitoring:PingForConfiguration:1:rivtabp21",
        wsdlLocation = "schemas/external/interactions/PingForConfigurationInteraction/PingForConfigurationInteraction_1.0_rivtabp21.wsdl")
public class PingForConfigurationImpl extends BaseService implements PingForConfigurationResponderInterface {
    public static final String QUEUE_SIZE_TAG = "currentQueueSize";
    public static final String OLDEST_MESSAGE_TAG = "currentOldestMessage";
    public static final String DELIVERY_COUNT_TAG = "todaysDeliveryCount";
    public static final String MAX_DELIVERY_TIME_TAG = "todaysMaxDeliveryTime";

    private static final Logger log = LoggerFactory.getLogger(PingForConfigurationImpl.class);

    private StatisticService statisticService;
    private TimeService timeService;

    @Autowired
    public void setTimeService(TimeService service) {
        this.timeService = service;
    }

    @Autowired
    public void setStatisticService(StatisticService service) {
        this.statisticService = service;
    }


    @Override
    public PingForConfigurationResponseType pingForConfiguration(@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress, @WebParam(partName = "parameters", name = "PingForConfiguration", targetNamespace = "urn:riv:itintegration:monitoring:PingForConfigurationResponder:1") PingForConfigurationType parameters) {
        PingForConfigurationResponseType response = new PingForConfigurationResponseType();

        // Extract the version from the Manifest file
        String version = "Messagebox " + getClass().getPackage().getImplementationVersion();
        response.setVersion(version);

        // current time in required format
        long now = timeService.now();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = format.format(new Date(now));
        response.setPingDateTime(dateTime);

        // we extract some info from the status reports and todays delivery stats
        StatusBuilder statusBuilder = new StatusBuilder(messageService.getStatusReports());
        StatsBuilder statsBuilder = new StatsBuilder(statisticService.getStatisticsForTimeSlice(now, now));

        response.getConfiguration().addAll(statusBuilder.result);
        response.getConfiguration().addAll(statsBuilder.result);

        log.info("PingForConfig [" + version + "] " + dateTime +
                ", " + response.getConfiguration().size() + " configuration entries reported");
        // if we get any kind of error, we will generate a SOAP Fault and tomcat will log the error, which is good
        // enough - no need to do any error handling.

        return response;
    }

    private ConfigurationType conf(String name, String value) {
        ConfigurationType conf = new ConfigurationType();
        conf.setName(name);
        conf.setValue(value);
        return conf;
    }

    private class StatusBuilder {

        List<ConfigurationType> result = new ArrayList<ConfigurationType>();


        public StatusBuilder(List<StatusReport> reports) {
            String targetSystem = null;
            long queueSize = 0;
            Date oldestMessage = new Date(timeService.now());
            for ( StatusReport report : reports ) {
                if ( !report.getTargetSystem().equals(targetSystem) ) {
                    save(targetSystem, queueSize, oldestMessage);
                    targetSystem = report.getTargetSystem();
                    queueSize = 0;
                    oldestMessage = new Date(timeService.now());
                }
                queueSize += report.getMessageCount();
                if ( oldestMessage.getTime() > report.getOldestMessageDate().getTime() ) {
                    oldestMessage = report.getOldestMessageDate();
                }
            }
            save(targetSystem, queueSize, oldestMessage);
        }

        private void save(String targetSystem, long queueSize, Date oldestMessage) {
            if ( targetSystem != null ) {
                result.add(conf(targetSystem + "-" +QUEUE_SIZE_TAG, String.valueOf(queueSize)));
                result.add(conf(targetSystem + "-" + OLDEST_MESSAGE_TAG, new TimeDelta(timeService.now() - oldestMessage.getTime()).toString()));
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for ( ConfigurationType v : result ) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(v.getName()).append("='").append(v.getValue()).append("'");
            }
            return sb.toString();
        }
    }

    private class StatsBuilder {

        List<ConfigurationType> result = new ArrayList<ConfigurationType>();


        public StatsBuilder(List<Statistic> statistics) {
            String targetSystem = null;
            long deliveries = 0;
            long maxDeliveryTime = 0;
            for ( Statistic stat : statistics ) {
                if ( !stat.getTargetSystem().equals(targetSystem) ) {
                    save(targetSystem, deliveries, maxDeliveryTime);
                    targetSystem = stat.getTargetSystem();
                    deliveries = 0;
                    maxDeliveryTime = 0;
                }
                deliveries += stat.getDeliveryCount();
                maxDeliveryTime = Math.max(maxDeliveryTime, stat.getMaxWaitTimeMs());

            }
            save(targetSystem, deliveries, maxDeliveryTime);

        }

        private void save(String targetSystem, long queueSize, long maxDeliveryTime) {
            if ( targetSystem != null ) {
                result.add(conf(targetSystem + "-" + DELIVERY_COUNT_TAG, String.valueOf(queueSize)));
                result.add(conf(targetSystem + "-" + MAX_DELIVERY_TIME_TAG, new TimeDelta(maxDeliveryTime).toString()));
            }
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for ( ConfigurationType v : result ) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(v.getName()).append("='").append(v.getValue()).append("'");
            }
            return sb.toString();
        }

    }
    
    @Override
    public Logger getLogger() {
        return log;
    }

}


