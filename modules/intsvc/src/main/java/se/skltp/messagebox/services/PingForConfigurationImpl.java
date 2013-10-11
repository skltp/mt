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
package se.skltp.messagebox.services;

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
import se.skltp.messagebox.TimeDelta;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.service.StatisticService;
import se.skltp.messagebox.core.service.TimeService;

/**
 * PingForConfiguration is used to verify that a server is up and running.
 * <p/>
 * It consists of a mandatory part where you have to fill in the version and the current time.
 * <p/>
 * Apart from that, a number of properties are returned that are supposed to reflect the status of the
 * service, and make sure it is still running.
 * <p/>
 * We summarize the information we show on the website, ie status on the current message queue and statistics for
 * how much we have delivered today, and send it back on a per-receiver basis. This means that we send back 2 property
 * variables (-queueSize and -oldestMessage) for each receiver with un-delivered messages, and two more for each
 * receiver that has taken deliveres of message today.
 * <p/>
 * We should perhaps keep track of the last time a receiving system has made a "List" request with us so we can report
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
        wsdlLocation = "schemas/interactions/PingForConfigurationInteraction/PingForConfigurationInteraction_1.0_rivtabp21.wsdl")
public class PingForConfigurationImpl extends BaseService implements PingForConfigurationResponderInterface {

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
        response.setVersion("Messagebox " + getClass().getPackage().getImplementationVersion());

        // current time in required format
        long now = timeService.now();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = format.format(new Date(now));
        response.setPingDateTime(dateTime);

        // we extract some info from the status reports and todays delivery stats
        response.getConfiguration().addAll(new StatusBuilder(messageService.getStatusReports()).result);
        response.getConfiguration().addAll(new StatsBuilder(statisticService.getStatisticsForTimeSlice(now, now)).result);

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
            String receiver = null;
            long queueSize = 0;
            Date oldestMessage = new Date(timeService.now());
            for ( StatusReport report : reports ) {
                if ( !report.getTargetSystem().equals(receiver) ) {
                    save(receiver, queueSize, oldestMessage);
                    receiver = report.getTargetSystem();
                    queueSize = 0;
                    oldestMessage = new Date(timeService.now());
                }
                queueSize += report.getMessageCount();
                if ( oldestMessage.getTime() > report.getOldestMessageDate().getTime() ) {
                    oldestMessage = report.getOldestMessageDate();
                }
            }
            save(receiver, queueSize, oldestMessage);
        }

        private void save(String receiver, long queueSize, Date oldestMessage) {
            if ( receiver != null ) {
                result.add(conf(receiver + "-queueSize", String.valueOf(queueSize)));
                result.add(conf(receiver + "-oldestMessage", new TimeDelta(timeService.now() - oldestMessage.getTime()).toString()));
            }
        }
    }

    private class StatsBuilder {

        List<ConfigurationType> result = new ArrayList<ConfigurationType>();

        public StatsBuilder(List<Statistic> reports) {
            String receiver = null;
            long deliveries = 0;
            long maxDeliveryTime = 0;
            for ( Statistic stat : reports ) {
                if ( !stat.getTargetSystem().equals(receiver) ) {
                    save(receiver, deliveries, maxDeliveryTime);
                    receiver = stat.getTargetSystem();
                    deliveries = 0;
                    maxDeliveryTime = 0;
                }
                deliveries += stat.getDeliveryCount();
                maxDeliveryTime = Math.max(maxDeliveryTime, stat.getMaxWaitTimeMs());

            }
            save(receiver, deliveries, maxDeliveryTime);

        }

        private void save(String receiver, long queueSize, long maxDeliveryTime) {
            if ( receiver != null ) {
                result.add(conf(receiver + "-deliveryCount", String.valueOf(queueSize)));
                result.add(conf(receiver + "-maxDeliveryTime", new TimeDelta(maxDeliveryTime).toString()));
            }
        }

    }

}


