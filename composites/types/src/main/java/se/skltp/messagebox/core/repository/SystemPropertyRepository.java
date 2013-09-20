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
