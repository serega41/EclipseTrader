/*
 * Copyright (c) 2004-2011 Marco Maccaferri and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco Maccaferri - initial API and implementation
 */

package org.eclipsetrader.core.internal.charts.repository;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.eclipsetrader.core.charts.repository.IElementSection;

public class ElementSectionTest extends TestCase {

    private String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    public void testMarshalId() throws Exception {
        IElementSection object = new ElementSection("id1", null);
        assertEquals(prefix + "<element id=\"id1\"/>", marshal(object));
    }

    public void testUnmarshalId() throws Exception {
        IElementSection object = unmarshal(prefix + "<element id=\"id1\"/>");
        assertEquals("id1", object.getId());
    }

    public void testMarshalPluginId() throws Exception {
        IElementSection object = new ElementSection(null, "plug1");
        assertEquals(prefix + "<element plugin-id=\"plug1\"/>", marshal(object));
    }

    public void testUnmarshalPluginId() throws Exception {
        IElementSection object = unmarshal(prefix + "<element plugin-id=\"plug1\"/>");
        assertEquals("plug1", object.getPluginId());
    }

    private String marshal(IElementSection object) throws Exception {
        StringWriter string = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); //$NON-NLS-1$
        marshaller.marshal(object, string);
        return string.toString();
    }

    private IElementSection unmarshal(String string) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ElementSection.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (IElementSection) unmarshaller.unmarshal(new StringReader(string));
    }
}
