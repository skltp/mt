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
package se.skltp.messagebox.core.repository.jpa;

import org.springframework.stereotype.Repository;
import se.skltp.messagebox.core.entity.SystemProperty;
import se.skltp.messagebox.core.repository.SystemPropertyRepository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

@Repository
public class JpaSystemPropertyRepository extends DefaultJpaRepository<SystemProperty, String> implements SystemPropertyRepository {

    @Override
    public SystemProperty getProperty(String name, String defaultValue) {
        SystemProperty result;

        result = find(name);

        if ( result == null ) {
            result = new SystemProperty(name, defaultValue);
            entityManager.persist(result);
        }
        return result;
    }
}
