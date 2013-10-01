/*
 * Copyright 2010 Inera
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *
 *   Boston, MA 02111-1307  USA
 */
package se.skltp.messagebox.services;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.ConfigurationType;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.PingForConfigurationResponder.v1.PingForConfigurationType;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.service.StatisticService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestPingForConfigurationImpl extends BaseTestImpl {

    @Mock
    private StatisticService statisticService;
    private static final String BASE_URL = "http://localhost";

    /**
     * Verify that we map the parameters and return types correctly when translating
     * between webservice calls and the underlying entity model.
     *
     * @throws Exception
     */
    @Test
    public void testResponse() throws Exception {

        PingForConfigurationImpl impl = new PingForConfigurationImpl();
        impl.setStatisticService(statisticService);
        impl.setMessageService(messageService);

        // TODO: add a system-service to allow us to control the time during testing... there is probably a
        // random chance that this test will fail now and then...

        List<Statistic> statistics = new ArrayList<>();
        statistics.add(new Statistic("rec1", "org1", "sc1", 0));
        statistics.get(0).addDelivery(121000);   // 02:01
        List<StatusReport> reports = new ArrayList<>();
        reports.add(new StatusReport("rec1", "org1", "sc1", 1, new Date(System.currentTimeMillis() - 15000))); // 00:15

        when(statisticService.getStatisticsForTimeSlice(anyLong(), anyLong())).thenReturn(statistics);
        when(messageService.getStatusReports()).thenReturn(reports);

        PingForConfigurationType params = new PingForConfigurationType();
        PingForConfigurationResponseType responseType = impl.pingForConfiguration("mbox-address", params);
        assertEquals("MessageBox.v1.0", responseType.getVersion());
        Date time = new SimpleDateFormat("yyyyMMddHHmmss").parse(responseType.getPingDateTime());
        long diff = time.getTime() - System.currentTimeMillis();
        assertTrue(Math.abs(diff)  < 2000);
        List<ConfigurationType> confList = responseType.getConfiguration();
        assertEquals(4, confList.size());
        Map<String,String> props = new HashMap<>();
        for ( ConfigurationType c : confList ) {
            props.put(c.getName(), c.getValue());
        }
        assertEquals("1", props.get("rec1-queueSize"));
        assertEquals("00:15", props.get("rec1-oldestMessage"));
        assertEquals("1", props.get("rec1-deliveryCount"));
        assertEquals("02:01", props.get("rec1-maxDeliveryTime"));

    }

}
