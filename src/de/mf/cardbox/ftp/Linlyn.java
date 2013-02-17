package de.mf.cardbox.ftp; 

 import java.io.*;
 import java.net.*;
 import java.util.*;	

public class Linlyn 
{

    // FOR DEBUGGING: set the variable to "true"
    private boolean DEBUG = false;

    // constructor needs servername, username and passwd
    public Linlyn(String server, String user, String pass) {
        try { 
            ftpConnect(server);
            ftpLogin(user, pass);
        } catch(IOException ioe) {ioe.printStackTrace();}
    }

    public String download(String dir, String file)
        throws IOException { return download(dir, file, true); }

    public String download(String dir, String file, boolean asc)
        throws IOException {
        ftpSetDir(dir);
        ftpSetTransferType(asc);
        dsock = ftpGetDataSock();
        InputStream is = dsock.getInputStream();
        ftpSendCmd("RETR "+file);
        
        String contents = getAsString(is);
        ftpLogout();
        return contents;
    }

    public void append(String dir, String file, String what, boolean asc)
        throws IOException {
        ftpSetDir(dir);
        ftpSetTransferType(asc);
        dsock = ftpGetDataSock();
        OutputStream os = dsock.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        ftpSendCmd("APPE "+file); 
        dos.writeBytes(what);
        dos.flush();
        dos.close();
        ftpLogout();
    }


    public void upload(String dir, String file, String what)
        throws IOException { upload(dir, file, what, true); }

    public void upload(String dir, String file, String what, boolean asc)
        throws IOException {
        ftpSetDir(dir);
        ftpSetTransferType(asc);
        dsock = ftpGetDataSock();
        OutputStream os = dsock.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        ftpSendCmd("STOR "+file);
        dos.writeBytes(what);    
        dos.flush();
        dos.close();
        ftpLogout();
    }

    ///////////////// private fields ////////////////////
    private boolean pauser = false;  // it's a hack. We're going to 
          // stall (refuse further requests) till we get a reply back 
          // from server for the current request.

    private String getAsString(InputStream is) {
        int c=0;
        char lineBuffer[]=new char[128], buf[]=lineBuffer;
        int room= buf.length, offset=0;
        try {
          loop: while (true) {
              // read chars into a buffer which grows as needed
                  switch (c = is.read() ) {
                      case -1: break loop;

                      default: if (--room < 0) {
                                   buf = new char[offset + 128];
                                   room = buf.length - offset - 1;
                                   System.arraycopy(lineBuffer, 0, 
                                            buf, 0, offset);
                                   lineBuffer = buf;
                               }
                               buf[offset++] = (char) c;
                               break;
                  }
          }
        } catch(IOException ioe) {ioe.printStackTrace();}
        if ((c == -1) && (offset == 0)) {
            return null;
        }
        return String.copyValueOf(buf, 0, offset);
    }

    private void ftpConnect(String server)
        throws IOException {
        // Set up socket, control streams, connect to ftp server
        // Open socket to server control port 21
        csock = new Socket(server, CNTRL_PORT);
        // Open control streams
        InputStream cis = csock.getInputStream();
        dcis =  new BufferedReader(new InputStreamReader(cis));
        OutputStream cos = csock.getOutputStream();
        pos = new PrintWriter(cos, true); // set auto flush true.
        // See if server is alive or dead... 
           String numerals = responseHandler(null); 
        if(numerals.substring(0,3).equals("220")) // ftp server alive
            ; // System.out.println("Connected to ftp server");
        else System.err.println("Error connecting to ftp server.");
    }

    private void ftpLogin(String user, String pass)
        throws IOException {
        ftpSendCmd("USER "+user);
        ftpSendCmd("PASS "+pass);
    }

    private void ftpSetDir(String dir)
        throws IOException { 
        // cwd to dir
        ftpSendCmd("CWD "+dir);
    }

    private void ftpSetTransferType(boolean asc)
    throws IOException {
    // set file transfer type
        String ftype = (asc? "A" : "I");
        ftpSendCmd("TYPE "+ftype);
    }    

    private Socket ftpGetDataSock()
        throws IOException {
        // Go to PASV mode, capture server reply, parse for socket setup
        // V2.1: generalized port parsing, allows more server variations
        String reply = ftpSendCmd("PASV");

        // New technique: just find numbers before and after ","!
        StringTokenizer st = new StringTokenizer(reply, ",");
        String[] parts = new String[6]; // parts, incl. some garbage
        int i = 0; // put tokens into String array
        while(st.hasMoreElements()) {
            // stick pieces of host, port in String array
            try {
                parts[i] = st.nextToken();
                i++;
            } catch(NoSuchElementException nope){nope.printStackTrace();}
        } // end getting parts of host, port

        // Get rid of everything before first "," except digits
        String[] possNum = new String[3];
        for(int j = 0; j < 3; j++) {
            // Get 3 characters, inverse order, check if digit/character
            possNum[j] = parts[0].substring(parts[0].length() - (j + 1),
                parts[0].length() - j); // next: digit or character?
            if(!Character.isDigit(possNum[j].charAt(0)))
                possNum[j] = "";
        }
        parts[0] = possNum[2] + possNum[1] + possNum[0];
        // Get only the digits after the last ","
        String[] porties = new String[3];
        for(int k = 0; k < 3; k++) {
            // Get 3 characters, in order, check if digit/character
            // May be less than 3 characters
            if((k + 1) <= parts[5].length())
                porties[k] = parts[5].substring(k, k + 1);
            else porties[k] = "FOOBAR"; // definitely not a digit!
            // next: digit or character?
            if(!Character.isDigit(porties[k].charAt(0)))
                    porties[k] = "";
        } // Have to do this one in order, not inverse order
        parts[5] = porties[0] + porties[1] + porties[2];
        // Get dotted quad IP number first
        String ip = parts[0]+"."+parts[1]+"."+parts[2]+"."+parts[3];

        // Determine port
        int port = -1;
        try { // Get first part of port, shift by 8 bits.
            int big = Integer.parseInt(parts[4]) << 8;
            int small = Integer.parseInt(parts[5]);
            port = big + small; // port number
        } catch(NumberFormatException nfe) {nfe.printStackTrace();}
        if((ip != null) && (port != -1))

            dsock = new Socket(ip, port);
        else throw new IOException();
        return dsock;
    }

    private String ftpSendCmd(String cmd)
        throws IOException
    { // This sends a dialog string to the server, returns reply
      // V2.0 Updated to parse multi-string responses a la RFC 959
      // Prints out only last response string of the lot.
        if (pauser) // i.e. we already issued a request, and are
                  // waiting for server to reply to it.  
            {
                if (dcis != null)
                {
                    String discard = dcis.readLine(); // will block here
                    // preventing this further client request until server
                    // responds to the already outstanding one.
                    if (DEBUG) {
                        System.out.println("keeping handler in sync"+
                            " by discarding next response: ");
                        System.out.println(discard);
                    }
                    pauser = false;
                }
            }
        pos.print(cmd + "\r\n" );
        pos.flush(); 
        String response = responseHandler(cmd);
        return response;
    }

     // new method to read multi-line responses
     // responseHandler: takes a String command or null and returns
     // just the last line of a possibly multi-line response
     private String responseHandler(String cmd) 
         throws IOException
     { // handle more than one line returned
        String reply = this.responseParser(dcis.readLine());
        String numerals = reply.substring(0, 3);
        String hyph_test = reply.substring(3, 4);
        String next = null;
        if(hyph_test.equals("-")) {
            // Create "tester", marks end of multi-line output
            String tester = numerals + " ";
            boolean done = false;
            while(!done) { // read lines til finds last line
                next = dcis.readLine();
                // Read "over" blank line responses
                while (next.equals("") || next.equals("  ")) {
                    next = dcis.readLine();
                }

                // If next starts with "tester", we're done
               if(next.substring(0,4).equals(tester))
                   done = true;
            }

            if(DEBUG)
                if(cmd != null)
                    System.out.println("Response to: "+cmd+" was: "+next);
                else
                    System.out.println("Response was: "+next);
            return next;

        } else // "if (hyph_test.equals("-")) not true"
            if(DEBUG)
                if(cmd != null)
                    System.out.println("Response to: "+cmd+" was: "+reply);
                else
                    System.out.println("Response was: "+reply);
            return reply;
    }

    // responseParser: check first digit of first line of response
    // and take action based on it; set up to read an extra line
    // if the response starts with "1"
    private String responseParser(String resp)
        throws IOException
    { // Check first digit of resp, take appropriate action.
        String digit1 = resp.substring(0, 1);
        if(digit1.equals("1")) {
            // server to act, then give response
            if(DEBUG) System.out.println("in 1 handler");
            // set pauser
            pauser = true;
            return resp;
        }
        else if(digit1.equals("2")) { // do usual handling
            if(DEBUG) System.out.println("in 2 handler");
            // reset pauser
            pauser = false;
            return resp;
        }
        else if(digit1.equals("3") || digit1.equals("4")
            || digit1.equals("5")) { // do usual handling
            if(DEBUG) System.out.println("in 3-4-5 handler");
            return resp;
        }
        else { // not covered, so return null
            return null;
        }
    }


    private void ftpLogout() {// logout, close streams
        try { 
            if(DEBUG) System.out.println("sending BYE");
            pos.print("BYE" + "\r\n" );
            pos.flush();
            pos.close();
            dcis.close();
            csock.close();
            dsock.close();
        } catch(IOException ioe) {ioe.printStackTrace();}
    }


    private static final int CNTRL_PORT = 21;
    private Socket csock = null;
    private Socket dsock = null;
    private BufferedReader dcis;
    private PrintWriter pos;
}

//////////////////////////////////////////////////////////////////////
// 
//              The "Artistic License"   (from a KDE module)
// 
//                 Preamble
// 
// The intent of this document is to state the conditions under which this
// software may be copied, such that the Copyright Holder maintains some
// semblance of artistic control over the development of the software,
// while giving the users of the software the right to use and distribute
// the software in a more-or-less customary fashion, plus the right to make
// reasonable modifications.
// 
// Definitions:
// 
//     "Package" refers to the collection of files distributed by the
//     Copyright Holder, and derivatives of that collection of files
//     created through textual modification.
// 
//     "Standard Version" refers to such a Package if it has not been
//     modified, or has been modified in accordance with the wishes
//     of the Copyright Holder as specified below.
// 
//     "Copyright Holder" is whoever is named in the copyright or
//     copyrights for the package.
// 
//     "You" is you, if you're thinking about copying or distributing
//     this Package.
// 
//     "Reasonable copying fee" is whatever you can justify on the
//     basis of media cost, duplication charges, time of people involved,
//     and so on.  (You will not be required to justify it to the
//     Copyright Holder, but only to the computing community at large
//     as a market that must bear the fee.)
// 
//     "Freely Available" means that no fee is charged for the item
//     itself, though there may be fees involved in handling the item.
//     It also means that recipients of the item may redistribute it
//     under the same conditions they received it.
// 
// 1. You may make and give away verbatim copies of the source form of the
// Standard Version of this Package without restriction, provided that you
// duplicate all of the original copyright notices and associated disclaimers.
// 
// 2. You may apply bug fixes, portability fixes and other modifications
// derived from the Public Domain or from the Copyright Holder.  A Package
// modified in such a way shall still be considered the Standard Version.
// 
// 3. You may otherwise modify your copy of this Package in any way, provided
// that you insert a prominent notice in each changed file stating how and
// when you changed that file, and provided that you do at least ONE of the
// following:
// 
//     a) place your modifications in the Public Domain or otherwise make them
//     Freely Available, such as by posting said modifications to Usenet or
//     an equivalent medium, or placing the modifications on a major archive
//     site such as uunet.uu.net, or by allowing the Copyright Holder to include
//     your modifications in the Standard Version of the Package.
// 
//     b) use the modified Package only within your corporation or organization.
// 
//     c) rename any non-standard executables so the names do not conflict
//     with standard executables, which must also be provided, and provide
//     a separate manual page for each non-standard executable that clearly
//     documents how it differs from the Standard Version.
// 
//     d) make other distribution arrangements with the Copyright Holder.
// 
// 4. You may distribute the programs of this Package in object code or
// executable form, provided that you do at least ONE of the following:
// 
//     a) distribute a Standard Version of the executables and library files,
//     together with instructions (in the manual page or equivalent) on where
//     to get the Standard Version.
// 
//     b) accompany the distribution with the machine-readable source of
//     the Package with your modifications.
// 
//     c) give non-standard executables non-standard names, and clearly
//     document the differences in manual pages (or equivalent), together
//     with instructions on where to get the Standard Version.
// 
//     d) make other distribution arrangements with the Copyright Holder.
// 
// 5. You may charge a reasonable copying fee for any distribution of this
// Package.  You may charge any fee you choose for support of this
// Package.  You may not charge a fee for this Package itself.  However,
// you may distribute this Package in aggregate with other (possibly
// commercial) programs as part of a larger (possibly commercial) software
// distribution provided that you do not advertise this Package as a
// product of your own.  You may embed this Package's interpreter within
// an executable of yours (by linking); this shall be construed as a mere
// form of aggregation, provided that the complete Standard Version of the
// interpreter is so embedded.
// 
// 6. The scripts and library files supplied as input to or produced as
// output from the programs of this Package do not automatically fall
// under the copyright of this Package, but belong to whomever generated
// them, and may be sold commercially, and may be aggregated with this
// Package.  If such scripts or library files are aggregated with this
// Package via the so-called "undump" or "unexec" methods of producing a
// binary executable image, then distribution of such an image shall
// neither be construed as a distribution of this Package nor shall it
// fall under the restrictions of Paragraphs 3 and 4, provided that you do
// not represent such an executable image as a Standard Version of this
// Package.
// 
// 7. C subroutines (or comparably compiled subroutines in other
// languages) supplied by you and linked into this Package in order to
// emulate subroutines and variables of the language defined by this
// Package shall not be considered part of this Package, but are the
// equivalent of input as in Paragraph 6, provided these subroutines do
// not change the language in any way that would cause it to fail the
// regression tests for the language.
// 
// 8. Aggregation of this Package with a commercial distribution is always
// permitted provided that the use of this Package is embedded; that is,
// when no overt attempt is made to make this Package's interfaces visible
// to the end user of the commercial distribution.  Such use shall not be
// construed as a distribution of this Package.
// 
// 9. The name of the Copyright Holder may not be used to endorse or 
// promote products derived from this software without specific prior 
// written permission.
// 
// 10. THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
// 
// 11.  You must not be Microsoft, or currently be employed by any
// company that is partly or wholly owned by Microsoft, if there are
// any companies left in the software industry for which this is true.
//
//                 The End
