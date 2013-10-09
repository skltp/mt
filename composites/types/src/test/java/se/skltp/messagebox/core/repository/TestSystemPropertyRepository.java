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
package se.skltp.messagebox.core.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.entity.SystemProperty;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestSystemPropertyRepository extends JpaRepositoryTestBase {

    @Autowired
    private SystemPropertyRepository propertyRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testPersist() throws Exception {
        SystemProperty prop = propertyRepository.getProperty("test", "default");

        assertEquals("test", prop.getName());
        assertEquals("default", prop.getValue());

        entityManager.flush();

        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SYSTEMPROPERTY", Long.class));
    }

}
