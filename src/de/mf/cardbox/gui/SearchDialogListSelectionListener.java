package de.mf.cardbox.gui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class SearchDialogListSelectionListener implements ListSelectionListener {
  private SearchDialog searchDialog = null;

  /**
   * Constructor for SearchDialogListSelectionListener.
   */
  public SearchDialogListSelectionListener(SearchDialog searchDialog) {
    this.searchDialog = searchDialog;
  }

  /**
   * @see javax.swing.event.ListSelectionListener#valueChanged(ListSelectionEvent)
   */
  public void valueChanged(ListSelectionEvent arg0) {
    searchDialog.showSelectedEntry();
  }

}
