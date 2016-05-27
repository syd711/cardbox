package de.mf.cardbox.ftp;

/**
 * @author Syd
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.io.IOException;

public class FTPConnector {
  public FTPConnector() {
    init();
  }


  private void init() {
    try {
      // upload to file
      // You must replace the strings with actual values to use.
      //  e.g.       Linlyn("microsoft.com","billg",    "allmy$$$");
      Linlyn u = new Linlyn("matthias-faust.de", "mfaust", "lacuna!2");
      u.upload("pub", "../images/text.html", "'Just Java 2' is a great Java book! available quick and cheap from http://www.amazon.com/exec/obidos/ASIN/0130105341/afuinc ");

      // add some more onto end of existing file
      //Linlyn u2 = new Linlyn("YOUR_SERVER", "USERNAME", "PASSWD");
      //u2.append("pub", "score.txt", "append this info at ONCE", true);

      // download from file
      // You must replace the strings with actual values to use.
      //Linlyn d = new Linlyn("YOUR_SERVER", "USERNAME", "PASSWD");
      //String s = d.download("pub", "score.txt");

      // display file contents
      System.out.println("done");


    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static void main(String args[]) {
    new FTPConnector();
  }
}
