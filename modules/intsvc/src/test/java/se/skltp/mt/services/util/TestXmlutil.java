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

package se.skltp.mt.services.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.skltp.mt.services.util.XmlUtil;

/*
 * TODO This test is somewhat bound to the specific DOM implementation since there might be small differences in
 * how XML is serialized (e.g. attribute order, whitespaces). Therefore, on other platforms than the Sun JDK,
 * there might be a need to improve the test to not compare with a predefined string. 
 */
public class TestXmlutil {

    private static final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
    static {
        DBF.setNamespaceAware(true);
    }
    private static final File XML_FILE = new File("src/test/resources/TestXmlUtil.xml");
    private String expectedXml;
    private String expectedRootLocalName;

    @Before
    public void setup() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(XML_FILE), "UTF-8"));
        expectedXml = reader.readLine();
        reader.close();

        expectedRootLocalName = expectedXml.substring(3, 27);
    }
    
    @Test
    public void testToString() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder db = DBF.newDocumentBuilder();

        Document doc = db.parse(XML_FILE);
        DOMSource source = new DOMSource(doc.getDocumentElement());

        Assert.assertEquals(expectedXml, XmlUtil.toString(source));
    }

    @Test
    public void fromString() throws ParserConfigurationException, SAXException, IOException {
        DOMSource source = XmlUtil.fromString(expectedXml);
        
        Node node = source.getNode();
        Assert.assertTrue(node instanceof Element);
        Assert.assertEquals(expectedRootLocalName, node.getLocalName());
        Assert.assertEquals("http://example.com", node.getNamespaceURI());
    }

}
