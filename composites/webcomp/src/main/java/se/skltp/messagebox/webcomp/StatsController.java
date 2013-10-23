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
package se.skltp.messagebox.webcomp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.skltp.messagebox.svc.TimeDelta;
import se.skltp.messagebox.types.entity.Statistic;
import se.skltp.messagebox.svc.services.StatisticService;

@Controller
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatisticService statisticService;

    private static final Logger log = LoggerFactory.getLogger(StatsController.class);

    /**
     * Shows all questions
     * @return
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("stats");

        long now = System.currentTimeMillis();
        long thirtyDays = 30 * 24 * 3600 * 1000L;

        List<Statistic> statistics = statisticService.getStatisticsForTimeSlice(now - thirtyDays, now);
        mav.addObject("statistics", createStatView(statistics));

        return mav;
    }

    /**
     * Create a view of the current set of statistics.
     *
     * Adds summary rows to the list, as well as some view-methods.
     *
     * @param statistics to create views for
     * @return views of the statistics, with extra summary rows
     */
    private List<StatisticView> createStatView(List<Statistic> statistics) {
        List<StatisticView> results = new ArrayList<StatisticView>();
        StatisticView recRow = null;
        StatisticView orgRow = null;
        StatisticView conRow = null;
        for ( Statistic s : statistics ) {
            if ( recRow == null || !s.getTargetSystem().equals(recRow.getTargetSystem()) ) {
                results.add(recRow = StatisticView.createRecRow(s));
                results.add(orgRow = StatisticView.createOrgRow(s));
                results.add(conRow = StatisticView.createConRow(s));
            }
            if (!s.getTargetOrganization().equals(orgRow.getTargetOrganization())) {
                results.add(orgRow = StatisticView.createOrgRow(s));
                results.add(conRow = StatisticView.createConRow(s));
            }
            if (!s.getServiceContract().equals(conRow.getServiceContract().getFullName())) {
                results.add(conRow = StatisticView.createConRow(s));
            }
            recRow.merge(s);
            orgRow.merge(s);
            conRow.merge(s);
        }
        return results;
    }

    /**
     * View object, mostly wrapping {@link Statistic} functionality.
     */
    public static class StatisticView {
        private String targetSystem;
        private String targetOrganization;
        private ServiceContractView serviceContract;
        private int deliveryCount;
        private long totalSize;
        private long maxWaitTimeMs;
        private long totalWaitTimeMs;

        StatisticView(Statistic s) {
            targetSystem = s.getTargetSystem();
            targetOrganization = s.getTargetOrganization();
            serviceContract = new ServiceContractView(s.getServiceContract());
            deliveryCount = s.getDeliveryCount();
            totalSize = s.getTotalSize();
            maxWaitTimeMs = s.getMaxWaitTimeMs();
            totalWaitTimeMs = s.getTotalWaitTimeMs();
        }


        @Override
        public String toString() {
            return "StatisticView{" +
                    "targetSystem='" + targetSystem + '\'' +
                    ", targetOrganization='" + targetOrganization + '\'' +
                    ", serviceContract=" + serviceContract +
                    ", deliveryCount=" + deliveryCount +
                    ", totalSize=" + totalSize +
                    ", maxWaitTimeMs=" + maxWaitTimeMs +
                    ", totalWaitTimeMs=" + totalWaitTimeMs +
                    '}';
        }

        public String getTargetSystem() {
            return targetSystem;
        }

        public String getTargetOrganization() {
            return targetOrganization;
        }

        public ServiceContractView getServiceContract() {
            return serviceContract;
        }

        public int getDeliveryCount() {
            return deliveryCount;
        }

        public ByteSizeView getTotalSize() {
            return new ByteSizeView(totalSize);
        }

        public ByteSizeView getAverageSize() {
            return new ByteSizeView(totalSize/deliveryCount);
        }

        public TimeDelta getMaxDeliveryTime() {
            return new TimeDelta(maxWaitTimeMs);
        }

        public TimeDelta getAverageDeliveryTime() {
            if ( deliveryCount == 0 ) {
                return new TimeDelta(0);
            }
            return new TimeDelta(totalWaitTimeMs / deliveryCount);
        }

        public static StatisticView createRecRow(Statistic s) {
            StatisticView result = createOrgRow(s);
            result.targetOrganization = "";
            return result;
        }

        public static StatisticView createOrgRow(Statistic s) {
            StatisticView result = createConRow(s);
            result.serviceContract = new ServiceContractView("");
            return result;
        }
        public static StatisticView createConRow(Statistic s) {
            StatisticView result = new StatisticView(s);
            result.totalWaitTimeMs = result.maxWaitTimeMs = result.deliveryCount = 0;
            return result;
        }

        public void merge(Statistic s) {
            deliveryCount += s.getDeliveryCount();
            totalSize += s.getTotalSize();
            totalWaitTimeMs += s.getTotalWaitTimeMs();
            maxWaitTimeMs = Math.max(maxWaitTimeMs, s.getMaxWaitTimeMs());
        }

    }

}
