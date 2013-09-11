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

package se.skltp.messagebox.util;

import iso.v21090.dt.v1.II;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3.wsaddressing10.AttributedURIType;


/**
 * @author Pär Wenåker
 *
 */
public abstract class ClientBase extends DbunitTestBase {

    public URL createEndpointUrlFromServiceAddress(String serviceAddress) {
        try {
            return new URL(serviceAddress + "?wsdl");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
        }
    }
    
    public String getServiceBaseUrl() {
        String port = System.getProperty("server.port");
        if(port == null) {
            port = "8080";
        }
        return "http://localhost:" + port +"/message-web/ws/";
    }

	protected II createCareUnitId(AttributedURIType logicalAddress) {
		II value = new II();
		value.setRoot("root");
		value.setExtension(logicalAddress.getValue());
		value.setIdentifierName(logicalAddress.getValue());
		return value;
	}
}
