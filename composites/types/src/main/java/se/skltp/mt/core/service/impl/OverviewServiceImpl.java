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

package se.skltp.mt.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.skltp.mt.core.service.CareUnitInfo;
import se.skltp.mt.core.service.OverviewService;

/**
 * @author Pär Wenåker
 *
 */
@Service
@Transactional
public class OverviewServiceImpl implements OverviewService {

    @PersistenceContext
    private EntityManager entityManager;
    
    @SuppressWarnings("unchecked")
    public List<CareUnitInfo> getCareUnitInfos(int maxNumber) {
        CareUnitInfoBuilder b = new CareUnitInfoBuilder(maxNumber);
        b.addAll(entityManager.createQuery(
                "select " + 
                "new se.skltp.mt.core.service.impl.CareUnitInfoQuestionInternal(q.careUnit, q.status, count(q)) " +
        	"from Question q group by q.careUnit, q.status")
        	.getResultList());
        b.addAll(entityManager.createQuery(
                "select " + 
                "new se.skltp.mt.core.service.impl.CareUnitInfoAnswerInternal(a.careUnit, a.status, count(a)) " +
                "from Answer a group by a.careUnit, a.status")
                .getResultList());
        
        return b.build();
    }
    
    private class CareUnitInfoBuilder {

        private Map<String, CareUnitInfo> result = new HashMap<String, CareUnitInfo>();
        
        private final int maxNumber;
        /**
         * @param maxNumber
         */
        public CareUnitInfoBuilder(int maxNumber) {
            this.maxNumber = maxNumber;
        }
        /**
         * @param resultList
         */
        public void addAll(List<CareUnitInfoInternal> resultList) {
            for(CareUnitInfoInternal i : resultList) {
                CareUnitInfo info = result.get(i.getName());
                if(info == null) {
                    info = new CareUnitInfo(i.getName());
                    result.put(i.getName(), info);
                }
                i.update(info);
            }
        }
        public List<CareUnitInfo> build() {
            ArrayList<CareUnitInfo> list = new ArrayList<CareUnitInfo>(new TreeSet<CareUnitInfo>(result.values()));
            return list.subList(0, list.size() > maxNumber ? maxNumber : list.size());
        }
        
    } 
    
}
