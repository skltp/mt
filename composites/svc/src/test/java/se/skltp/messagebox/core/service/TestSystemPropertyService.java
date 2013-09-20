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
package se.skltp.messagebox.core.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.entity.SystemProperty;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:services-config.xml"})
public class TestSystemPropertyService extends JpaRepositoryTestBase {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    SystemPropertyService service;

    @Test
    public void testPropertyChange() throws Exception {

        assertNotNull(service);
        SystemProperty prop = service.getReceiveMessageUrl();

        assertNotSame("test", prop.getValue());
        prop.setValue("test");
        entityManager.flush();
        entityManager.clear();

        SystemProperty prop2 = service.getReceiveMessageUrl();
        // a new property
        assertNotSame(prop, prop2);
        // with the new value
        assertEquals("test", prop2.getValue());
    }


}
