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

import se.skltp.messagebox.core.entity.SystemProperty;
import se.vgregion.dao.domain.patterns.repository.Repository;

public interface SystemPropertyRepository extends Repository<SystemProperty, String> {

    /**
     * Get a property with the given name.
     *
     * If it does not exist, it will get created and given the defaultValue
     *
     * @param name name of property
     * @param defaultValue value of property if not already set
     * @return the property
     */
    SystemProperty getProperty(String name, String defaultValue);

}
