package de.mf.cardbox.db;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class DAOCardBox {
  private Connection con = null;

  /**
   * Constructor for DAOCardBox.
   */
  public DAOCardBox() {
    super();
  }

  /**
   * Method getConnection.
   *
   * @return Connection
   */
  private synchronized Connection getConnection() {

    if(con == null) {
      try {

        Class.forName("com.mysql.jdbc.Driver");
        String ipAddress = "";
        String port = "3306";
        String database = "";
        String username = "";
        String password = "";
        String url = "jdbc:mysql://" + ipAddress + ":" + port + "/" + database + "?characterEncoding=UTF-8";
        System.out.println(url);
        con = DriverManager.getConnection(url, username, password);
        return con;
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
      }
      return null;
    }
    else
      return con;

  }


  /**
   * Method getEntriesForLetter.
   *
   * @return String[]
   */
  public String[] getEntriesForLetter(String letter, String db) {
    try {
      Statement stmt = getConnection().createStatement();
      ResultSet set = stmt.executeQuery("select title from KARTEIKASTEN where letter = '" + letter + "' and db = '" + db + "'");
      ArrayList result = new ArrayList();

      while(set.next()) {
        result.add(set.getString("title"));
      }

      return (String[]) result.toArray(new String[0]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * Method getEntry.
   *
   * @param letter
   * @return String
   */
  public String getEntry(String letter, String title, String db) {
    try {
      Statement stmt = getConnection().createStatement();
      String sql = "select text from KARTEIKASTEN where letter = '" + letter + "' and db = '" + db + "' and title ='" + title + "'";
      ResultSet set = stmt.executeQuery(sql);

      while(set.next()) {
        return set.getString("text");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "keine Beschreibung";
  }


  /**
   * Method getdbs.
   *
   * @return ArrayList
   */
  public ArrayList getDatabases() {
    ArrayList result = new ArrayList();
    try {
      Statement stmt = getConnection().createStatement();
      ResultSet set = stmt.executeQuery("select db from KARTEIKASTEN");

      while(set.next()) {
        String database = set.getString("db");
        if(!result.contains(database))
          result.add(database);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }


  /**
   * Method deleteEntry.
   *
   * @param letter
   * @param db
   * @param title
   */
  public void deleteEntry(String letter, String db, String title) {
    try {
      String sql = "delete from KARTEIKASTEN where letter = '" + letter + "' and db = '" + db + "' and title = '" + title + "'";
      getConnection().createStatement().execute(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Method insertEntry.
   *
   * @param letter
   * @param title
   * @param text
   * @param db
   */
  public void insertEntry(String letter, String title, String text, String db, String group) throws SQLException {
    String sql = "insert into KARTEIKASTEN values ('" + letter + "', '" + title.trim() + "', '" + text.trim() + " ', '" + db + "', 0, '" + group + "')";
    getConnection().createStatement().execute(sql);
  }


  /**
   * Method updateEntry.
   *
   * @param letter
   * @param title
   * @param text
   * @param db
   */
  public void updateEntry(String letter, String title, String text, String db) {
    try {
      String sql = "update KARTEIKASTEN set text ='" + text.trim() + "' where title = '" + title.trim() + "'";
      getConnection().createStatement().execute(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method findEntries.
   *
   * @param string
   * @return String
   */
  public Hashtable findEntries(String string, String db, boolean searchTitle, boolean searchText) {
    Hashtable result = new Hashtable();
    try {
      String sql = "select * from KARTEIKASTEN where db = '" + db + "'";
      Statement stmt = getConnection().createStatement();
      ResultSet set = stmt.executeQuery(sql);

      while(set.next()) {
        String title = set.getString("title");
        String explanation = set.getString("text");

        if(searchTitle && title.indexOf(string) != -1) {
          result.put(title, explanation);
        }
        else if(searchText && explanation.indexOf(string) != -1) {
          result.put(title, explanation);
        }
        else
          continue;
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * Method getAllEntries.
   *
   * @param db
   * @return Hashtable
   */
  public Hashtable getAllEntries(String db) {
    Hashtable result = new Hashtable();
    try {
      String sql = "select * from KARTEIKASTEN where db = '" + db + "'";
      Statement stmt = getConnection().createStatement();
      ResultSet set = stmt.executeQuery(sql);

      while(set.next()) {
        String title = set.getString("title");
        String explanation = set.getString("text");

        result.put(title, explanation);
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
