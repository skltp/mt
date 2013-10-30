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
package se.skltp.mb.webcomp;

/**
 * Human-readable view of a service contract namespace.
 * <p/>
 * Use fullName in tooltips.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class ServiceContractView {
    String serviceContract;
    String shortName;

    ServiceContractView(String serviceContract) {
        this.serviceContract = shortName = serviceContract;
        String[] parts = serviceContract.split(":");
        if ( parts.length > 2 ) {
            shortName = parts[parts.length - 2];
        }
    }

    @Override
    public String toString() {
        return shortName;
    }

    public String getFullName() {
        return serviceContract;
    }

    public String getShortName() {
        return shortName;
    }
}
