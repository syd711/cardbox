package de.mf.cardbox.importer;

import de.mf.cardbox.db.DAOCardBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by Matthias on 27.05.2016.
 */
public class CSVImporter {

  public static final String DB = "Türkisch";

  public static void main(String[] args) throws Exception {
    DAOCardBox dao = new DAOCardBox();

    File inputFile = new File("G:\\temp\\CSV-Export-vokker.csv");

    BufferedReader in = new BufferedReader(
        new InputStreamReader(
            new FileInputStream(inputFile), "UTF8"));

    String str;

    int index = 0;
    int success = 0;
    String group = "";
    while((str = in.readLine()) != null) {
      if(str.contains("#")) {
        group = str.split("#")[1].trim();
        continue;
      }
      index++;
      String[] split = str.split(";");
      String foreign = split[0];
      if(!isUTF8MB4(foreign)) {
        System.out.println("Not an UTF8 string: " + foreign);
        continue;
      }
      String translated = split[1];

      String letter = translated.substring(0, 1);
      try {
        dao.insertEntry(letter, translated, foreign, DB, group);
        success++;
      } catch (SQLException e) {
        if(e.getMessage().toLowerCase().contains("duplicate")) {
          String existingEntry = dao.getEntry(letter, translated, DB);
          if(!existingEntry.contains(foreign)) {
            dao.updateEntry(letter, translated, existingEntry + "\n" + foreign, DB);
            System.out.println("Updated existing entry (" + index + ") '" + translated + "' => '" + foreign + "'");
            success++;
          }
          else {
            System.out.println("Skipped duplicate entry (" + index + ") '" + translated + "' => '" + foreign + "' (existing value '" + existingEntry + "')");
          }
        }
        else {
          System.out.println("Skipped entry (" + index + ") '" + translated + "' => '" + foreign + "': " + e.getMessage());
        }
      }
    }

    System.out.println("Finished importing: " + success + " of " + index + " records.");
  }

  public static boolean isUTF8MB4(String s) {
    for(int i = 0; i < s.length(); i++) {
      final char c = s.charAt(i);
      if(!Character.isBmpCodePoint(c)) {
        return false;
      }
    }
    return true;
  }
}
