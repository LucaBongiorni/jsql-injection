/*******************************************************************************
 * Copyhacked (H) 2012-2014.
 * This program and the accompanying materials
 * are made available under no term at all, use it like
 * you want, but share and discuss about it
 * every time possible with every body.
 * 
 * Contributors:
 *      ron190 at ymail dot com - initial implementation
 ******************************************************************************/
package com.jsql.view.interaction;

import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.jsql.model.access.object.RessourceAccessObject;
import com.jsql.view.MediatorGUI;
import com.jsql.view.tab.TabHeader;
import com.jsql.view.terminal.TerminalWebshell;

/**
 * Create a new tab for the terminal.
 */
public class CreateShellTab implements IInteractionCommand {
    /**
     * Full path of the shell file on remote host.
     */
    private String path;

    /**
     * Url of the shell webpage on remote host.
     */
    private String url;

    /**
     * @param interactionParams The local path and url for the shell
     */
    public CreateShellTab(Object[] interactionParams) {
        path = (String) interactionParams[0];
        url = (String) interactionParams[1];
    }

    @Override
    public void execute() {
        UUID terminalID = UUID.randomUUID();
        TerminalWebshell terminal = new TerminalWebshell(terminalID, url);
        MediatorGUI.gui().getConsoles().put(terminalID, terminal);

        JScrollPane scroller = new JScrollPane(terminal);
        Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        scroller.setViewportBorder(border);
        scroller.setBorder(border);

        MediatorGUI.right().addTab("Web shell ", scroller);

        // Focus on the new tab
        MediatorGUI.right().setSelectedComponent(scroller);

        // Create a custom tab header with close button
        TabHeader header = new TabHeader(new ImageIcon(getClass().getResource("/com/jsql/view/images/shell.png")));

        MediatorGUI.right().setToolTipTextAt(
                MediatorGUI.right().indexOfComponent(scroller),
                "<html><b>URL</b><br>" + url + RessourceAccessObject.WEBSHELL_FILENAME
                + "<br><b>Path</b><br>" + path + RessourceAccessObject.WEBSHELL_FILENAME + "</html>");

        // Apply the custom header to the tab
        MediatorGUI.right().setTabComponentAt(MediatorGUI.right().indexOfComponent(scroller), header);

        terminal.requestFocusInWindow();
    }
}
