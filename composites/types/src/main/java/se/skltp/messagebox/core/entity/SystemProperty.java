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
package se.skltp.messagebox.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * A system property.
 * <p/>
 * A simple name/value tuples for system values.
 * <p/>
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Entity
public class SystemProperty extends AbstractEntity<String> {

    @Id
    private String name;
    private String value;

    public SystemProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public SystemProperty() {
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getId() {
        return name;
    }
}

