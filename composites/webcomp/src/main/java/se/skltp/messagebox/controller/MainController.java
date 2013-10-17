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
package se.skltp.messagebox.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.skltp.messagebox.TimeDelta;
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
        List<StatusReportView> results = new ArrayList<StatusReportView>();
        for ( StatusReport r : reports ) {
            if ( recRep == null || !r.getTargetSystem().equals(recRep.getTargetSystem()) ) {
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

        private String targetSystem;
        private String targetOrganization;
        private ServiceContractView serviceContract;
        private long messageCount;
        private long totalSize;
        private Date oldestMessageDate;

        public StatusReportView(String targetSystem, String targetOrganization, String serviceContract, long messageCount, long totalSize, Date oldestMessageDate) {
            this.targetSystem = targetSystem;
            this.targetOrganization = targetOrganization;
            this.serviceContract = new ServiceContractView(serviceContract);
            this.messageCount = messageCount;
            this.totalSize = totalSize;
            this.oldestMessageDate = oldestMessageDate;
        }


        public StatusReportView(StatusReport r) {
            this(r.getTargetSystem(),
                    r.getTargetOrganization(),
                    r.getServiceContract(),
                    r.getMessageCount(),
                    r.getTotalSize(),
                    r.getOldestMessageDate());
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
            this.totalSize += other.getTotalSize();
            this.oldestMessageDate = new Date(Math.min(this.oldestMessageDate.getTime(), other.getOldestMessageDate().getTime()));
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

        public long getMessageCount() {
            return messageCount;
        }

        public Date getOldestMessageDate() {
            return oldestMessageDate;
        }

        public ByteSizeView getTotalSize() {
            return new ByteSizeView(totalSize);
        }

        public ByteSizeView getAverageSize() {
            return new ByteSizeView(totalSize/messageCount);
        }

        public TimeDelta getOldestMessageAge() {
            return new TimeDelta(System.currentTimeMillis() - oldestMessageDate.getTime());
        }

        static StatusReportView createRec(StatusReport r) {
            return new StatusReportView(r.getTargetSystem(), "", "", 0, 0, r.getOldestMessageDate());
        }

        static StatusReportView createOrg(StatusReport r) {
            return new StatusReportView(r.getTargetSystem(), r.getTargetOrganization(), "", 0, 0, r.getOldestMessageDate());
        }

    }

}
