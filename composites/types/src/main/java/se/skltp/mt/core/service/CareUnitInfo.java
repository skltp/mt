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

package se.skltp.mt.core.service;


/**
 * @author Pär Wenåker
 *
 */
public class CareUnitInfo implements Comparable<CareUnitInfo> {

    private String name;

    private long questionsInArrived;

    private long questionsInRetrieved;

    private long answersInArrived;

    private long answersInRetrieved;

    /**
     * 
     */
    public CareUnitInfo(String name) {
        this.name = name;
    }

    public void addQuestionsInArrived(long cnt) {
        questionsInArrived += cnt;
    }

    public void addQuestionsInRetreived(long cnt) {
        questionsInRetrieved += cnt;
    }

    public void addAnswersInArrived(long cnt) {
        answersInArrived += cnt;
    }

    public void addAnswersInRetreived(long cnt) {
        answersInRetrieved += cnt;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public long getQuestionsInArrived() {
        return questionsInArrived;
    }

    public long getQuestionsInRetrieved() {
        return questionsInRetrieved;
    }

    public long getAnswersInArrived() {
        return answersInArrived;
    }

    public long getAnswersInRetrieved() {
        return answersInRetrieved;
    }
    
    private Long getTotal() {
        return questionsInArrived + questionsInRetrieved + answersInArrived + answersInRetrieved;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(CareUnitInfo o) {
        int cnt = o.getTotal().compareTo(this.getTotal());
        if(cnt == 0) {
            return this.getName().compareTo(o.getName());
        } else {
            return cnt;
        }
    }

}
