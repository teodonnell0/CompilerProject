/*
  Intermediate Code dis-Assembler.

  Reads a file of ICE machine code and prints the instructions as
  a table of numbered integer quadruples. MICE instruction format is:

  seq OPCODE tab OPERAND1 tab OPERAND2 tab OPERAND3

  Output file has same name as input, with .tab extension

  14 June 2003. Truman Parks Boyer.
*/

import java.io.*;
import java.math.*;
import java.util.*;
import java.lang.Object;

public class deice{
  
  public static DataInputStream codefile;
  public static DataOutputStream tabfile;

  public static void main(String args[]) throws IOException {

    System.out.println("     Intermediate Code dis-Assembler");
    System.out.println("Copyright (c) Truman Parks Boyer 14 Jun 2003\n");

    System.out.print("Intermediate Code File ?: ");
    String fname = getString();

    String SourceLine = "primed";  // Java doesn't like null init's
    
   // open code file
    try{ codefile=new DataInputStream(new FileInputStream(fname)); }
    catch(IOException e){ file_err(e.toString()+": could not open.\n"); };

   // open table file
    try{ tabfile=new DataOutputStream(new FileOutputStream(fname+".tab")); }
    catch(IOException e){ file_err(e.toString()+": could not open.\n"); };
    
    int i = 0;                              
    byte b = 0;
    char k = 'x';
    int count = 0;

    do{                      // translation loop
      if (count == 1){
        try{ 
          b = codefile.readByte();    // get opcode string
          tabfile.writeBytes("\t"+opcode_string(b));
          echo(" "+opcode_string(b));
          for (int j = 0;j<3;j++){
            b = codefile.readByte();  // get operand types
            k = operandtype(b);
            tabfile.writeChar(k);
            echo(" "+k);
          }
        } 
        catch(IOException e){ break; };  // if eof exit loop endif 
      } 
      else{
        try{
          i = codefile.readInt();
          tabfile.writeBytes("\t"+Integer.toString(i));
        }
        catch(IOException e){ break; };
        echo("\t"+i);
      }
      count++;
      if(count > 4){
        count=0;
        newline();
        tabfile.writeChars("\n");
      }
    } while(true);   // end translation loop
   
    // close files
    try{ codefile.close(); }
    catch(IOException e){ file_err(e.toString()+": could not close.\n"); }

    try{ tabfile.close(); }
    catch(IOException e){ file_err(e.toString()+": could not close.\n"); }

  } // end of main()

  private static String opcode_string(int opcode){
    String opcodes=
  "NOPADDSUBMULDIVMODINCDECSTOJMPJEQJNEJLTJLEJGTJGESYSANDOR XORNOTNEGHLT";
    int index = opcode*3;
    String temp = opcodes.substring(index,index+3);
    return temp;
  }

  private static char operandtype(byte flag){
    char temp = 'd';
    if(flag == 0) temp = 'i';
    return temp;
  }
  private static void file_err(String s){
    System.err.println(s);
    System.exit(1);  // abort
  }
  private static void pressEnter() throws IOException {
    String dummy;
    newline();
    System.out.print("press Enter to continue...");
    dummy = getString();
  }
  public static int getInt() throws IOException {
    String s = getString();
    return Integer.parseInt(s);
  }
  public static char getChar() throws IOException {
    String s = getString();
    return s.charAt(0);
  }
  public static String getString() throws IOException {
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);
    String s = br.readLine();
    return s;
  }
  private static void echo(String s){ System.out.print(s); }
  private static void newline(){ System.out.println(" "); }
}
