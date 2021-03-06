package com.jsql.view.panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.jsql.view.MediatorGUI;

/**
 * MouseAdapter to show/hide bottom panel.
 */
public class ActionHideShowConsole implements ActionListener {
    /**
     * Save the divider location when bottom panel is not visible.
     */
    private int loc = 0;

    /**
     * Ersatz panel to display in place of tabbedpane.
     */
    private JPanel panel;

    /**
     * Create the hide/show bottom panel action.
     */
    public ActionHideShowConsole(JPanel panel) {
        super();
        this.panel = panel;
    }

    /**
     * Hide bottom panel if both main and bottom are visible, also
     * displays an ersatz bar replacing tabbedpane.  
     * Or else if only main panel is visible then displays bottom panel
     * and hide ersatz panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (MediatorGUI.gui().outputPanel.getTopComponent().isVisible() && MediatorGUI.gui().outputPanel.getBottomComponent().isVisible()) {
            MediatorGUI.gui().outputPanel.getBottomComponent().setVisible(false);
            this.loc = MediatorGUI.gui().outputPanel.getDividerLocation();
            this.panel.setVisible(true);
            MediatorGUI.gui().outputPanel.disableDragSize();
        } else {
            MediatorGUI.gui().outputPanel.getBottomComponent().setVisible(true);
            MediatorGUI.gui().outputPanel.setDividerLocation(this.loc);
            this.panel.setVisible(false);
            MediatorGUI.gui().outputPanel.enableDragSize();
        }
    }
}