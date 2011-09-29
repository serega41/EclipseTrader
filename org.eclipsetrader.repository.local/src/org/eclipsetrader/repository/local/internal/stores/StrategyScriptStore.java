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

package org.eclipsetrader.repository.local.internal.stores;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipsetrader.core.ats.IScriptStrategy;
import org.eclipsetrader.core.feed.TimeSpan;
import org.eclipsetrader.core.instruments.ISecurity;
import org.eclipsetrader.core.repositories.IPropertyConstants;
import org.eclipsetrader.core.repositories.IRepository;
import org.eclipsetrader.core.repositories.IRepositoryElementFactory;
import org.eclipsetrader.core.repositories.IStore;
import org.eclipsetrader.core.repositories.IStoreProperties;
import org.eclipsetrader.core.repositories.StoreProperties;
import org.eclipsetrader.repository.local.LocalRepository;
import org.eclipsetrader.repository.local.internal.Activator;
import org.eclipsetrader.repository.local.internal.StrategiesCollection;
import org.eclipsetrader.repository.local.internal.types.RepositoryFactoryAdapter;
import org.eclipsetrader.repository.local.internal.types.SecurityAdapter;
import org.eclipsetrader.repository.local.internal.types.TimeSpanAdapter;

@XmlRootElement(name = "strategy")
public class StrategyScriptStore implements IStore {

    @XmlAttribute(name = "id")
    private Integer id;

    @XmlAttribute(name = "factory")
    @XmlJavaTypeAdapter(RepositoryFactoryAdapter.class)
    private IRepositoryElementFactory factory;

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "language")
    private String language;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "text")
    private String text;

    @XmlElementWrapper(name = "bars")
    @XmlElement(name = "timeSpan")
    @XmlJavaTypeAdapter(TimeSpanAdapter.class)
    private List<TimeSpan> barsTimeSpan = new ArrayList<TimeSpan>();

    @XmlElementWrapper(name = "instruments")
    @XmlElement(name = "security")
    @XmlJavaTypeAdapter(SecurityAdapter.class)
    private List<ISecurity> instruments = new ArrayList<ISecurity>();

    public StrategyScriptStore() {
    }

    public StrategyScriptStore(Integer id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#fetchProperties(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IStoreProperties fetchProperties(IProgressMonitor monitor) {
        StoreProperties properties = new StoreProperties();

        if (factory != null) {
            properties.setProperty(IPropertyConstants.ELEMENT_FACTORY, factory);
        }
        properties.setProperty(IPropertyConstants.OBJECT_TYPE, type);

        properties.setProperty(IScriptStrategy.PROP_NAME, name);
        properties.setProperty(IScriptStrategy.PROP_LANGUAGE, language);
        properties.setProperty(IScriptStrategy.PROP_TEXT, text);
        properties.setProperty(IScriptStrategy.PROP_INSTRUMENTS, instruments.toArray(new ISecurity[instruments.size()]));
        properties.setProperty(IScriptStrategy.PROP_BARS_TIMESPAN, barsTimeSpan.toArray(new TimeSpan[barsTimeSpan.size()]));

        return properties;
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#putProperties(org.eclipsetrader.core.repositories.IStoreProperties, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void putProperties(IStoreProperties properties, IProgressMonitor monitor) {
        this.factory = (IRepositoryElementFactory) properties.getProperty(IPropertyConstants.ELEMENT_FACTORY);
        this.type = (String) properties.getProperty(IPropertyConstants.OBJECT_TYPE);

        this.name = (String) properties.getProperty(IScriptStrategy.PROP_NAME);
        this.language = (String) properties.getProperty(IScriptStrategy.PROP_LANGUAGE);
        this.text = (String) properties.getProperty(IScriptStrategy.PROP_TEXT);

        this.instruments = new ArrayList<ISecurity>();
        ISecurity[] e = (ISecurity[]) properties.getProperty(IScriptStrategy.PROP_INSTRUMENTS);
        if (e != null) {
            this.instruments.addAll(Arrays.asList(e));
        }

        this.barsTimeSpan = new ArrayList<TimeSpan>();
        TimeSpan[] barsTimeSpan = (TimeSpan[]) properties.getProperty(IScriptStrategy.PROP_BARS_TIMESPAN);
        if (barsTimeSpan != null) {
            this.barsTimeSpan.addAll(Arrays.asList(barsTimeSpan));
        }
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#delete(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void delete(IProgressMonitor monitor) throws CoreException {
        StrategiesCollection.getInstance().delete(this);
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#fetchChilds(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IStore[] fetchChilds(IProgressMonitor monitor) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#createChild()
     */
    @Override
    public IStore createChild() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#getRepository()
     */
    @Override
    @XmlTransient
    public IRepository getRepository() {
        return Activator.getDefault().getRepository();
    }

    /* (non-Javadoc)
     * @see org.eclipsetrader.core.repositories.IStore#toURI()
     */
    @Override
    public URI toURI() {
        try {
            return new URI(LocalRepository.URI_SCHEMA, LocalRepository.URI_SCRIPT_PART, String.valueOf(id));
        } catch (URISyntaxException e) {
        }
        return null;
    }
}
