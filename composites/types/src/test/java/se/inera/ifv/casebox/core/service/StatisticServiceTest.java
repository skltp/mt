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
package se.inera.ifv.casebox.core.service;

import java.util.Collection;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.ifv.casebox.core.service.impl.StatisticInfo;
import se.inera.ifv.casebox.util.JpaRepositoryTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class StatisticServiceTest extends JpaRepositoryTestBase {

    @Autowired
    private StatisticService statisticService;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Ignore
    // FIXME: Depends on current time.
    public void findForLastMonth() throws Exception {
        
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getXmlDataSet("statistic.xml"));
        Collection<StatisticInfo> col = statisticService.findStatisticLastMonth();
        Assert.assertEquals(2, col.size());
        
        Iterator<StatisticInfo> infos = col.iterator();
        
        // Should contain 2 summed items order by number of messages
        StatisticInfo info = infos.next();
        Assert.assertEquals(4, info.getNumberOfMessages());
        info = infos.next();
        Assert.assertEquals(4, info.getNumberOfMessages());
    }
}
