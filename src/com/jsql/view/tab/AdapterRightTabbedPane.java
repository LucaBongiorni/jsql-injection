/*******************************************************************************
 * Copyhacked (H) 2012-2014.
 * This program and the accompanying materials
 * are made available under no term at all, use it like
 * you want, but share and discuss about it
 * every time possible with every body.
 *
 * Contributors:
 *      ron190 at ymail dot com - initial implementation
 *******************************************************************************/
package com.jsql.view.tab;

import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;

import com.jsql.view.action.ActionHandler;
import com.jsql.view.tab.dnd.DnDTabbedPane;
import com.jsql.view.tab.dnd.TabTransferHandler;

/**
 * TabbedPane containing result injection panels.
 */
@SuppressWarnings("serial")
public class AdapterRightTabbedPane extends DnDTabbedPane {
    
    /**
     * Create the panel containing injection results.
     */
    public AdapterRightTabbedPane() {
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        TransferHandler handler = new TabTransferHandler();
        this.setTransferHandler(handler);

        // Add hotkeys to rootpane ctrl-tab, ctrl-shift-tab, ctrl-w
        ActionHandler.addShortcut(this);
    }
}
