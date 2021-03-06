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
package com.jsql.view.list.dnd;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

/**
 * Handler for processing cut/copy/paste/drag/drop action on a JList items.
 */
@SuppressWarnings("serial")
public class ListTransfertHandler extends TransferHandler {
    /**
     * List of cut/copy/paste/drag/drop items.
     */
    private List<ListItem> dragPaths = null;
    
    /**
     * Log4j logger sent to view.
     */
    private static final Logger LOGGER = Logger.getLogger(ListTransfertHandler.class);

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        DnDList list = (DnDList) c;
        dragPaths = list.getSelectedValuesList();

        StringBuilder buff = new StringBuilder();
        for (ListItem t: dragPaths) {
            buff.append(t + "\n");
        }

        return new StringSelection(buff.toString().trim());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == TransferHandler.MOVE) {
            JList<ListItem> list = (JList<ListItem>) c;
            DefaultListModel<ListItem> model = (DefaultListModel<ListItem>) list.getModel();
            for (ListItem t: dragPaths) {
                model.remove(model.indexOf(t));
            }
            
            dragPaths = null;
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.stringFlavor)
                || support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        DnDList list = (DnDList) support.getComponent();
        DefaultListModel<ListItem> model = (DefaultListModel<ListItem>) list.getModel();
        //This is a drop
        if (support.isDrop()) {
            if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                int childIndex = dl.getIndex();

                List<Integer> selectAfterDrop = new ArrayList<Integer>();

                // DnD from list
                if (dragPaths != null && !dragPaths.isEmpty()) {
                    for (ListItem value: dragPaths) {
                        if (!"".equals(value.toString())) {
                            //! FUUuu
                            ListItem newValue = new ListItem(value.toString().replace("\\", "/"));
                            selectAfterDrop.add(childIndex);
                            model.add(childIndex++, newValue);
                        }
                    }
                // DnD from outside
                } else {
                    try {
                        String importString = (String) (support.getTransferable().getTransferData(DataFlavor.stringFlavor));
                        for (String value: importString.split("\\n")) {
                            if (!"".equals(value)) {
                                selectAfterDrop.add(childIndex);
                                model.add(childIndex++, new ListItem(value.replace("\\", "/")));
                            }
                        }
                    } catch (UnsupportedFlavorException e) {
                        LOGGER.error(e, e);
                    } catch (IOException e) {
                        LOGGER.error(e, e);
                    }
                }

                //array is the Integer array
                int[] selectedIndices = new int[selectAfterDrop.size()];
                int i = 0;
                for (Integer integer: selectAfterDrop) {
                    selectedIndices[i] = integer.intValue();
                    i++;
                }
                list.setSelectedIndices(selectedIndices);
            } else if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                int childIndex = dl.getIndex();

                try {
                    list.dropPasteFile((List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor), childIndex);
                } catch (UnsupportedFlavorException e) {
                    LOGGER.error(e, e);
                } catch (IOException e) {
                    LOGGER.error(e, e);
                }
            }
        //This is a paste
        } else {
            Transferable transferableFromClipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferableFromClipboard != null) {
                if (transferableFromClipboard.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String clipboardText = (String) transferableFromClipboard.getTransferData(DataFlavor.stringFlavor);

                        int y = 0;
                        if (list.getSelectedIndex() > 0) {
                            y = list.getSelectedIndex();
                        }
                        list.clearSelection();

                        List<Integer> k = new ArrayList<Integer>();
                        for (String f: clipboardText.split("\\n")) {
                            if (!"".equals(f)) {
                                ListItem c = new ListItem(f.replace("\\", "/"));
                                k.add(y);
                                model.add(y++, c);
                            }
                        }
                        int[] array2 = new int[k.size()];
                        int i = 0;
                        for (Integer integer : k) {
                            array2[i] = integer.intValue();
                            i++;
                        }
                        list.setSelectedIndices(array2);
                        list.scrollRectToVisible(
                                list.getCellBounds(
                                        list.getMinSelectionIndex(),
                                        list.getMaxSelectionIndex()
                                        )
                                );
                    } catch (UnsupportedFlavorException e) {
                        LOGGER.error(e, e);
                    } catch (IOException e) {
                        LOGGER.error(e, e);
                    }
                } else if (transferableFromClipboard.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        int y = 0;
                        if (list.getSelectedIndex() > 0) {
                            y = list.getSelectedIndex();
                        }
                        list.clearSelection();

                        list.dropPasteFile((List<File>) transferableFromClipboard.getTransferData(DataFlavor.javaFileListFlavor), y);
                    } catch (UnsupportedFlavorException e) {
                        LOGGER.error(e, e);
                    } catch (IOException e) {
                        LOGGER.error(e, e);
                    }
                }
            }
        }

        return true;
    }
}
