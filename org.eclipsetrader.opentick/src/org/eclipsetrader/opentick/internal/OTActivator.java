/*
 * Copyright (c) 2004-2008 Marco Maccaferri and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco Maccaferri - initial API and implementation
 */

package org.eclipsetrader.opentick.internal;

import java.io.File;
import java.io.FileWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsetrader.opentick.internal.core.repository.IdentifiersList;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class OTActivator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.eclipsetrader.opentick";
	public static final String REPOSITORY_FILE = "identifiers.xml"; //$NON-NLS-1$

    public static final String PREFS_SERVER = "SERVER";
    public static final String PREFS_PORT = "PORT";
    public static final String PREFS_USERNAME = "USERNAME";
    public static final String PREFS_PASSWORD = "PASSWORD";

	// The shared instance
	private static OTActivator plugin;

	private IdentifiersList identifiersList;

	/**
	 * The constructor
	 */
	public OTActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		startupRepository(getStateLocation().append(REPOSITORY_FILE).toFile());
		new Connector();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		shutdownRepository(getStateLocation().append(REPOSITORY_FILE).toFile());

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static OTActivator getDefault() {
		return plugin;
	}

	public static void log(IStatus status) {
		if (plugin == null) {
			System.err.println(status.getMessage());
			if (status.getException() != null)
				status.getException().printStackTrace();
		}
		plugin.getLog().log(status);
	}

	public void startupRepository(File file) {
        if (file.exists() == true) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(IdentifiersList.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				unmarshaller.setEventHandler(new ValidationEventHandler() {
                    public boolean handleEvent(ValidationEvent event) {
        				Status status = new Status(Status.WARNING, PLUGIN_ID, 0, "Error validating XML: " + event.getMessage(), null); //$NON-NLS-1$
        				getLog().log(status);
	                    return true;
                    }
				});
				identifiersList = (IdentifiersList) unmarshaller.unmarshal(file);
			} catch (Exception e) {
				Status status = new Status(Status.ERROR, PLUGIN_ID, 0, "Error loading repository", e); //$NON-NLS-1$
				getLog().log(status);
			}
        }

        // Fail safe, create an empty repository
        if (identifiersList == null)
        	identifiersList = new IdentifiersList();
	}

	public void shutdownRepository(File file) {
		try {
			if (file.exists())
				file.delete();

			JAXBContext jaxbContext = JAXBContext.newInstance(IdentifiersList.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, System.getProperty("file.encoding")); //$NON-NLS-1$
			marshaller.setEventHandler(new ValidationEventHandler() {
                public boolean handleEvent(ValidationEvent event) {
    				Status status = new Status(Status.WARNING, PLUGIN_ID, 0, "Error validating XML: " + event.getMessage(), null); //$NON-NLS-1$
    				getLog().log(status);
                    return true;
                }
			});
			marshaller.marshal(identifiersList, new FileWriter(file));
		} catch (Exception e) {
			Status status = new Status(Status.ERROR, PLUGIN_ID, 0, "Error saving repository", e); //$NON-NLS-1$
			getLog().log(status);
		}
	}
}