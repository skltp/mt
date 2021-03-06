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
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.ConfigurationType;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.PingForConfigurationType;
import se.skltp.mb.types.StatusReport;
import se.skltp.mb.types.entity.Statistic;
import se.skltp.mb.svc.services.StatisticService;
import se.skltp.mb.types.services.TimeService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PingForConfigurationImplTest extends BaseTestImpl {

    @Mock
    private StatisticService statisticService;
    @Mock
    private TimeService timeService;

    /**
     * Verify that the response generated is correctly summarizing the status and statistics.
     */
    @Test
    public void testResponse() throws Exception {

        PingForConfigurationImpl impl = new PingForConfigurationImpl();
        impl.setStatisticService(statisticService);
        impl.setMessageService(messageService);
        impl.setTimeService(timeService);

        List<Statistic> statistics = new ArrayList<Statistic>();
        String targSys = "TargSys";
        statistics.add(new Statistic(targSys, "Org1", "Sc1", 0));
        statistics.get(0).addDelivery(1000, 121000);   // 1000 bytes, 02:01
        List<StatusReport> reports = new ArrayList<StatusReport>();
        reports.add(new StatusReport(targSys, "org1", "sc1", 1, 1, 1000, new Date(timeService.now() - 15000))); // 00:15

        when(statisticService.getStatisticsForTimeSlice(anyLong(), anyLong())).thenReturn(statistics);
        when(messageService.getStatusReports()).thenReturn(reports);

        PingForConfigurationType params = new PingForConfigurationType();
        PingForConfigurationResponseType responseType = impl.pingForConfiguration("mbox-address", params);
// Can't test this - the version is not available during the test phase (created in the packaging phase)
// Can only test during configuration
//        assertEquals("MessageBox.v1.0", responseType.getVersion());
        Date time = new SimpleDateFormat("yyyyMMddHHmmss").parse(responseType.getPingDateTime());
        long diff = time.getTime() - timeService.now();
        assertEquals(0, diff);
        List<ConfigurationType> confList = responseType.getConfiguration();
        assertEquals(4, confList.size());
        Map<String, String> props = new HashMap<String, String>();
        for ( ConfigurationType c : confList ) {
            props.put(c.getName(), c.getValue());
        }
        assertEquals("1", props.get(targSys + "-" + PingForConfigurationImpl.QUEUE_SIZE_TAG));
        assertEquals("00:00:15", props.get(targSys + "-" + PingForConfigurationImpl.OLDEST_MESSAGE_TAG));
        assertEquals("1", props.get(targSys + "-" + PingForConfigurationImpl.DELIVERY_COUNT_TAG));
        assertEquals("00:02:01", props.get(targSys + "-" + PingForConfigurationImpl.MAX_DELIVERY_TIME_TAG));

    }

}
