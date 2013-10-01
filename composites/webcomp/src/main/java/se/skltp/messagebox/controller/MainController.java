package se.skltp.messagebox.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.service.MessageService;

/**
 * Main controller to open the first page with some overview information and statistic about
 * the current state in the messagebox.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView start() {
        ModelAndView mav = new ModelAndView("start");

        List<StatusReport> reports = messageService.getStatusReports();
        mav.addObject("reports", createRepView(reports));

        return mav;
    }


    private List<StatusReportView> createRepView(List<StatusReport> reports) {
        StatusReportView recRep = null;
        StatusReportView orgRep = null;
        List<StatusReportView> results = new ArrayList<>();
        for ( StatusReport r : reports ) {
            if ( recRep == null || !r.getReceiver().equals(recRep.getReceiver()) ) {
                results.add(recRep = StatusReportView.createRec(r));
                results.add(orgRep = StatusReportView.createOrg(r));
            }
            if ( !r.getTargetOrganization().equals(orgRep.getTargetOrganization()) ) {
                results.add(orgRep = StatusReportView.createOrg(r));
            }
            recRep.merge(r);
            orgRep.merge(r);
            results.add(new StatusReportView(r));
        }
        return results;
    }


    public static class StatusReportView {

        private String receiver;
        private String targetOrganization;
        private ServiceContractView serviceContract;
        private long messageCount;
        private Date oldestMessageDate;

        public StatusReportView(String receiver, String targetOrganization, String serviceContract, long messageCount, Date oldestMessageDate) {
            this.receiver = receiver;
            this.targetOrganization = targetOrganization;
            this.serviceContract = new ServiceContractView(serviceContract);
            this.messageCount = messageCount;
            this.oldestMessageDate = oldestMessageDate;
        }


        public StatusReportView(StatusReport r) {
            this(r.getReceiver(), r.getTargetOrganization(), r.getServiceContract(), r.getMessageCount(), r.getOldestMessageDate());
        }

        /**
         * Merge the other report with this one.
         *
         * Sums messageCount and uses oldest message age
         *
         * @param other to merge with
         */
        public void merge(StatusReport other) {
            this.messageCount += other.getMessageCount();
            this.oldestMessageDate = new Date(Math.min(this.oldestMessageDate.getTime(), other.getOldestMessageDate().getTime()));
        }


        public String getReceiver() {
            return receiver;
        }

        public String getTargetOrganization() {
            return targetOrganization;
        }

        public ServiceContractView getServiceContract() {
            return serviceContract;
        }

        public long getMessageCount() {
            return messageCount;
        }

        public Date getOldestMessageDate() {
            return oldestMessageDate;
        }

        public TimeDelta getOldestMessageAge() {
            return new TimeDelta(System.currentTimeMillis() - oldestMessageDate.getTime());
        }

        static StatusReportView createRec(StatusReport r) {
            return new StatusReportView(r.getReceiver(), "", "", 0, r.getOldestMessageDate());
        }

        static StatusReportView createOrg(StatusReport r) {
            return new StatusReportView(r.getReceiver(), r.getTargetOrganization(), "", 0, r.getOldestMessageDate());
        }

    }

}
