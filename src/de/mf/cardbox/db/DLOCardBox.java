package de.mf.cardbox.db;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class DLOCardBox {
  private DAOCardBox daoCardBox = null;

  /**
   * Constructor for DLOCardBox.
   */
  public DLOCardBox() {
    this.daoCardBox = new DAOCardBox();
  }


  /**
   * Method getEntriesForLetter.
   *
   * @param letter
   * @param database
   * @return String[]
   */
  public String[] getEntriesForLetter(String letter, String database) {
    return daoCardBox.getEntriesForLetter(letter, database);
  }


  /**
   * Method getEntry.
   *
   * @param letter
   * @param entry
   * @param database
   * @return String
   */
  public String getEntry(String letter, String entry, String database) {
    return daoCardBox.getEntry(letter, entry, database);
  }


  /**
   * Method getDatabases.
   *
   * @return ArrayList
   */
  public List<String> getDatabases() {
    return daoCardBox.getDatabases();
  }


  /**
   * Method deleteEntry.
   *
   * @param letter
   * @param database
   * @param entry
   */
  public void deleteEntry(String letter, String database, String entry) {
    daoCardBox.deleteEntry(letter, database, entry);
  }


  /**
   * Method insertEntry.
   *
   * @param letter
   * @param title
   * @param text
   * @param database
   */
  public void insertEntry(String letter, String title, String text, String database, String group) throws SQLException {
    daoCardBox.insertEntry(letter, title, text, database, group);
  }


  /**
   * Method updateEntry.
   *
   * @param letter
   * @param title
   * @param text
   * @param database
   */
  public void updateEntry(String letter, String title, String text, String database) {
    daoCardBox.updateEntry(letter, title, text, database);
  }

  /**
   * Method findEntries.
   *
   * @param string
   * @return String
   */
  public Hashtable findEntries(String string, String database, boolean searchTitle, boolean searchText) {
    return daoCardBox.findEntries(string, database, searchTitle, searchText);
  }


  /**
   * Method getAllEntries.
   *
   * @param database
   * @return Hashtable
   */
  public Hashtable getAllEntries(String database) {
    return daoCardBox.getAllEntries(database);
  }
}
