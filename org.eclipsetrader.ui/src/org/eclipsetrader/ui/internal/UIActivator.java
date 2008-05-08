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

package org.eclipsetrader.ui.internal;

import java.net.URI;
import java.util.UUID;

import org.eclipse.core.internal.runtime.AdapterManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsetrader.core.instruments.ISecurity;
import org.eclipsetrader.core.markets.IMarket;
import org.eclipsetrader.core.repositories.IRepository;
import org.eclipsetrader.core.repositories.IRepositoryService;
import org.eclipsetrader.core.views.IWatchList;
import org.eclipsetrader.ui.UIConstants;
import org.eclipsetrader.ui.internal.adapters.MarketAdapterFactory;
import org.eclipsetrader.ui.internal.adapters.RepositoryAdapterFactory;
import org.eclipsetrader.ui.internal.adapters.SecurityAdapterFactory;
import org.eclipsetrader.ui.internal.adapters.WatchListAdapterFactory;
import org.eclipsetrader.ui.internal.navigator.NavigatorViewItem;
import org.eclipsetrader.ui.internal.navigator.NavigatorViewItemAdapterFactory;
import org.eclipsetrader.ui.internal.repositories.RepositoryViewItem;
import org.eclipsetrader.ui.internal.repositories.RepositoryViewItemAdapterFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings("restriction")
public class UIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipsetrader.ui";

	// The shared instance
	private static UIActivator plugin;

	private IRepositoryService service;

	/**
	 * The constructor
	 */
	public UIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
    @Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		AdapterManager.getDefault().registerAdapters(new SecurityAdapterFactory(), ISecurity.class);
		AdapterManager.getDefault().registerAdapters(new WatchListAdapterFactory(), IWatchList.class);
		AdapterManager.getDefault().registerAdapters(new MarketAdapterFactory(), IMarket.class);
		AdapterManager.getDefault().registerAdapters(new RepositoryAdapterFactory(), IRepository.class);

		AdapterManager.getDefault().registerAdapters(new NavigatorViewItemAdapterFactory(), NavigatorViewItem.class);
		AdapterManager.getDefault().registerAdapters(new RepositoryViewItemAdapterFactory(), RepositoryViewItem.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UIActivator getDefault() {
		return plugin;
	}

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/* (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
     */
    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
    	reg.put(UIConstants.BLANK_OBJECT, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/blank_obj.gif")));
    	reg.put(UIConstants.FOLDER_OBJECT, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/fldr_obj.gif")));

    	reg.put(UIConstants.MARKET_OBJECT, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/market.gif")));
    	reg.put(UIConstants.MARKET_FOLDER, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/market_fldr.gif")));

    	reg.put(UIConstants.REPOSITORY, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/repository.gif")));
    	reg.put(UIConstants.REPOSITORY_FOLDER, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/repository_fldr.gif")));
    	reg.put(UIConstants.REPOSITORY_OBJECT_FOLDER, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/repository_object_fldr.gif")));
    	reg.put(UIConstants.REPOSITORY_OBJECT, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/repository_object.gif")));

    	reg.put(UIConstants.DELETE_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/elcl16/delete.gif")));
    	reg.put(UIConstants.DELETE_DISABLED_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/dlcl16/delete.gif")));

    	reg.put(UIConstants.CUT_EDIT_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/etool16/cut_edit.gif")));
    	reg.put(UIConstants.CUT_EDIT_DISABLED_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/dtool16/cut_edit.gif")));
    	reg.put(UIConstants.COPY_EDIT_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/etool16/copy_edit.gif")));
    	reg.put(UIConstants.COPY_EDIT_DISABLED_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/dtool16/copy_edit.gif")));
    	reg.put(UIConstants.PASTE_EDIT_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/etool16/paste_edit.gif")));
    	reg.put(UIConstants.PASTE_EDIT_DISABLED_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/dtool16/paste_edit.gif")));
    	reg.put(UIConstants.DELETE_EDIT_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/etool16/delete_edit.gif")));
    	reg.put(UIConstants.DELETE_EDIT_DISABLED_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/dtool16/delete_edit.gif")));

    	reg.put(UIConstants.REFRESH_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/elcl16/refresh.gif")));

    	reg.put(UIConstants.COLLAPSEALL_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/elcl16/collapseall.gif")));
    	reg.put(UIConstants.EXPANDALL_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/elcl16/expandall.gif")));

    	reg.put(UIConstants.TREND_STABLE_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/trend_stable.gif")));
    	reg.put(UIConstants.TREND_UP_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/trend_up.gif")));
    	reg.put(UIConstants.TREND_DOWN_ICON, ImageDescriptor.createFromURL(getBundle().getResource("icons/obj16/trend_down.gif")));
    }

	public IRepositoryService getRepositoryService() {
		if (service == null) {
			BundleContext context = UIActivator.getDefault().getBundle().getBundleContext();
			ServiceReference serviceReference = context.getServiceReference(IRepositoryService.class.getName());
			if (serviceReference != null)
				service = (IRepositoryService) context.getService(serviceReference);
		}
		return service;
	}

	public IDialogSettings getDialogSettingsForView(URI uri) {
		String uriString = uri.toString();

		IDialogSettings rootSettings = getDialogSettings().getSection("Views");
		if (rootSettings == null)
			rootSettings = getDialogSettings().addNewSection("Views");

		IDialogSettings[] sections = rootSettings.getSections();
		for (int i = 0; i < sections.length; i++) {
			if (uriString.equals(sections[i].get("uri")))
				return sections[i];
		}

		String uuid = UUID.randomUUID().toString();
		IDialogSettings dialogSettings = rootSettings.addNewSection(uuid);
		dialogSettings.put("uri", uriString);

		return dialogSettings;
	}
}
