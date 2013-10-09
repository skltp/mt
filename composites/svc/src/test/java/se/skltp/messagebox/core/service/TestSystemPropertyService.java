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

import static org.junit.Assert.*;

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
