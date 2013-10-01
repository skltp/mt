package se.skltp.messagebox.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.service.StatisticService;

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
    public ModelAndView index(@RequestParam(required = false) String careUnit) {
        ModelAndView mav = new ModelAndView("stats");

        List<Statistic> statistics = statisticService.getStatisticsFor30Days(System.currentTimeMillis());
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
        List<StatisticView> results = new ArrayList<>();
        StatisticView recRow = null;
        StatisticView orgRow = null;
        for ( Statistic s : statistics ) {
            if ( recRow == null || !s.getReceiverId().equals(recRow.getReceiverId()) ) {
                results.add(recRow = StatisticView.createRecRow(s));
                results.add(orgRow = StatisticView.createOrgRow(s));
            }
            if (!s.getTargetOrganization().equals(orgRow.getTargetOrganization())) {
                results.add(orgRow = StatisticView.createOrgRow(s));
            }
            recRow.merge(s);
            orgRow.merge(s);
            results.add(new StatisticView(s));
        }
        return results;
    }

    /**
     * View object, mostly wrapping {@link Statistic} functionality.
     */
    public static class StatisticView {
        private String receiverId;
        private String targetOrganization;
        private ServiceContractView serviceContract;
        private int deliveryCount;
        private long maxWaitTimeMs;
        private long totalWaitTimeMs;

        StatisticView(Statistic s) {
            receiverId = s.getReceiverId();
            targetOrganization = s.getTargetOrganization();
            serviceContract = new ServiceContractView(s.getServiceContract());
            deliveryCount = s.getDeliveryCount();
            maxWaitTimeMs = s.getMaxWaitTimeMs();
            totalWaitTimeMs = s.getTotalWaitTimeMs();
        }

        @Override
        public String toString() {
            return "StatisticView{" +
                    "receiverId='" + receiverId + '\'' +
                    ", targetOrganization='" + targetOrganization + '\'' +
                    ", serviceContract='" + serviceContract + '\'' +
                    ", deliveryCount=" + deliveryCount +
                    ", maxTime=" + getMaxDeliveryTime() +
                    ", avgTime=" + getAverageDeliveryTime() +
                    '}';
        }

        public String getReceiverId() {
            return receiverId;
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
            StatisticView result = new StatisticView(s);
            result.serviceContract = new ServiceContractView("");
            result.totalWaitTimeMs = result.maxWaitTimeMs = result.deliveryCount = 0;
            return result;
        }

        public void merge(Statistic s) {
            deliveryCount += s.getDeliveryCount();
            totalWaitTimeMs += s.getTotalWaitTimeMs();
            maxWaitTimeMs = Math.max(maxWaitTimeMs, s.getMaxWaitTimeMs());
        }

    }

}
