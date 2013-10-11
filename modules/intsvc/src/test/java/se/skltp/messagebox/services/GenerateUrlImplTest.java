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
package se.skltp.messagebox.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.riv.itintegration.messagebox.GenerateUrlResponder.v1.GenerateUrlResponseType;
import se.riv.itintegration.messagebox.GenerateUrlResponder.v1.GenerateUrlType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.core.entity.SystemProperty;
import se.skltp.messagebox.core.service.SystemPropertyService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenerateUrlImplTest extends BaseTestImpl {

    @Mock
    private SystemPropertyService propertyService;
    private static final String BASE_URL = "http://localhost";

    /**
     * Verify that we map the parameters and return types correctly when translating
     * between webservice calls and the underlying entity model.
     *
     * @throws Exception
     */
    @Test
    public void testResponse() throws Exception {

        GenerateUrlImpl impl = new GenerateUrlImpl();
        impl.setSystemPropertyService(propertyService);

        when(propertyService.getReceiveMessageUrl()).thenReturn(new SystemProperty("RECEIVE_MESSAGE_URL", BASE_URL));

        GenerateUrlType params = new GenerateUrlType();
        String nonEncodedHsaId = "a-test-with-an-url-compatible-hsa-id";
        params.setConsumerHsaId(nonEncodedHsaId);
        GenerateUrlResponseType responseType = impl.generateUrl("mbox-address", params);
        assertEquals(ResultCodeEnum.OK, responseType.getResult().getCode());
        assertEquals(BASE_URL + "/" + nonEncodedHsaId, responseType.getMessageBoxUrl());

        String needsEncodingHsaID = "a hsa-id that needs some encoding: äåö!&&<>#";
        String encoded = "a%20hsa-id%20that%20needs%20some%20encoding:%20%C3%A4%C3%A5%C3%B6!&&%3C%3E%23";
        params.setConsumerHsaId(needsEncodingHsaID);
        responseType = impl.generateUrl("mbox-address", params);
        assertEquals(ResultCodeEnum.OK, responseType.getResult().getCode());
        assertEquals(BASE_URL + "/" + encoded, responseType.getMessageBoxUrl());

    }

}