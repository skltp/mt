package se.skltp.mt.core.service.impl;


public class StatisticInfo {

    private long numberOfMessages;

    private String careUnit;
    
    public StatisticInfo(String careUnit, long numberOfMessages) {
        this.careUnit = careUnit;
        this.numberOfMessages = numberOfMessages;
    }

    public long getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(long numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public String getCareUnit() {
        return careUnit;
    }

    public void setCareUnit(String careUnit) {
        this.careUnit = careUnit;
    }
}
