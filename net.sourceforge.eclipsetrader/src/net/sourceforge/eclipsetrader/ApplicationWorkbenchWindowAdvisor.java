/*
 * Copyright (c) 2004-2006 Marco Maccaferri and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco Maccaferri - initial API and implementation
 */
package net.sourceforge.eclipsetrader;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
    {
        super(configurer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer)
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
     */
    public void preWindowOpen()
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowMenuBar(true);
        configurer.setShowCoolBar(true);
        configurer.setShowPerspectiveBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);

        configurer.getWindow().addPageListener(new IPageListener() {
            public void pageActivated(IWorkbenchPage page)
            {
                updateTitle();
            }

            public void pageClosed(IWorkbenchPage page)
            {
                updateTitle();
            }

            public void pageOpened(IWorkbenchPage page)
            {
            }
        });
        configurer.getWindow().addPerspectiveListener(new PerspectiveAdapter() {
            public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective)
            {
                updateTitle();
            }
            
            public void perspectiveSavedAs(IWorkbenchPage page,IPerspectiveDescriptor oldPerspective,IPerspectiveDescriptor newPerspective)
            {
                updateTitle();
            }

            public void perspectiveDeactivated(IWorkbenchPage page, IPerspectiveDescriptor perspective)
            {
                updateTitle();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowShellClose()
     */
    public boolean preWindowShellClose()
    {
        IPreferenceStore preferenceStore = EclipseTraderPlugin.getDefault().getPreferenceStore();
        boolean promptOnExit = preferenceStore.getBoolean(EclipseTraderPlugin.PROMPT_ON_EXIT);
        if (promptOnExit)
        {
            MessageDialogWithToggle dlg = MessageDialogWithToggle.openOkCancelConfirm(getWindowConfigurer().getWindow().getShell(), "Confirm Exit", "Exit EclipseTrader ?", "Always exit without prompt", false, null, null);
            if (dlg.getReturnCode() != IDialogConstants.OK_ID)
                return false;
            EclipseTraderPlugin.getDefault().getPreferenceStore().setValue(EclipseTraderPlugin.PROMPT_ON_EXIT, !dlg.getToggleState());
            EclipseTraderPlugin.getDefault().savePluginPreferences();
        }
        return super.preWindowShellClose();
    }

    private String computeTitle() 
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        IWorkbenchPage currentPage = configurer.getWindow().getActivePage();

        String title = null;
        IProduct product = Platform.getProduct();
        if (product != null)
            title = product.getName();
        if (title == null)
            title = ""; //$NON-NLS-1$

        if (currentPage != null)
        {
            IPerspectiveDescriptor persp = currentPage.getPerspective();
            if (persp != null)
            {
                if (!persp.getLabel().equals("") && !persp.getLabel().equals(title))
                    title += " - " + persp.getLabel();
            }
        }

        return title;
    }
    
    private void updateTitle() 
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        String oldTitle = configurer.getTitle();
        String newTitle = computeTitle();
        if (!newTitle.equals(oldTitle))
            configurer.setTitle(newTitle);
    }
}
