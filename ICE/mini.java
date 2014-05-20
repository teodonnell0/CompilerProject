/*
  Intermediate Code mini-Assembler.

  Allows user to translate a file of mini-assembler instructions 
  into a file of raw integer quadruples. MICE instruction format is:

  OPCODE (string) whitespace OPERAND1 comma OPERAND2 comma OPERAND3

  OPCODE is a 2-3 character string.
  OPERANDS are digit strings.

  version 2.0 note: symbol table added. User identifiers may be operands.

  A '#' prefix for operands indicates immediate data (where valid).
  Will serve as test file generator for MICE (mini-ICE) virtual machine,
  and as compiler back-end for K++.

  22 Apr 2002. Truman Parks Boyer.
  04 June 2003. Version 2.0. Symbolic names allowed for operands.
*/

import java.io.*;
import java.math.*;
import java.util.*;
import java.lang.Object;

class TextReader{
  BufferedReader br;

  public TextReader(String filename) throws java.io.IOException,
                                            java.io.FileNotFoundException{
    File aFile = new File(filename);
    FileInputStream fis = new FileInputStream(aFile);
    InputStreamReader isr = new InputStreamReader(fis);
    br = new BufferedReader (isr);
    }

  String readLine() throws java.io.IOException,
                           java.io.FileNotFoundException{
    return br.readLine();
  }
}

public class mini{
  
  public static TextReader infile;
  public static DataOutputStream outfile;

  public static int LineCount;               // Line Counter
  public static final int TABLE_SIZE = 1024; // size of symbol table
  public static String symbol[];             // Symbol Table
  public static int location[];
  public static int last_address = -1;       // address of last entry
  public static int last_index = -1;         // index of last entry

  public static void main(String args[]) throws IOException {

    symbol = new String[TABLE_SIZE]; // allocate symbol table
    location = new int[TABLE_SIZE];
    init_symbol_table();             // set addresses to -1

    System.out.println("     Intermediate Code mini-Assembler");
    System.out.println("Copyright (c) Truman Parks Boyer 02 Apr 2002\n");

    System.out.print("Input Code File ?: ");
    String fname = getString();

    // open infile
    try{ infile = new TextReader(fname); }
    catch(IOException e){ file_err(e.toString()+": could not open.\n"); };
    
    // open outfile
    try{ outfile=new DataOutputStream(new FileOutputStream(fname+".out")); }
    catch(IOException e){ file_err(e.toString()+": could not open.\n"); };
    
    String SourceLine = "primed";
    LineCount = 0;
    
    do{             // TRANSLATION LOOP                            
      try{
        SourceLine = infile.readLine();
        if(SourceLine==null){
          System.out.println("-----EOF (or blank line encountered)-----");
          System.exit(1);
        }   
      }catch(EOFException EOF){
        System.out.println("-----EOF-----");
        System.exit(1);
      }

      LineCount ++;  

      if(SourceLine == null)  break;

      System.out.println(SourceLine);

      SourceLine = SourceLine.trim();        // trim whitespace from ends
      check(SourceLine);

      int mark = SourceLine.indexOf(' ');    // 1st space marks end of line#
                      
      String linenum = SourceLine.substring(0,maxInt(0,mark)); // parse line#
             check(linenum);
             linenum = linenum.trim();      // trim it
             check(linenum);
      SourceLine = SourceLine.substring(mark+1);  // use rest of string
      SourceLine = SourceLine.trim();        // trim whitespace from ends
      check(SourceLine);
     // echo("linenum="+linenum+"...");
     // echo("sourcelineafterop1="+SourceLine+"|||");

      mark = SourceLine.indexOf(' ');    // next space marks end of opcode
                      
      String opcode = SourceLine.substring(0,maxInt(0,mark));
                                             // parse out opcode
             check(opcode);
             opcode = opcode.toUpperCase();            // make uppercase
             opcode = opcode.trim();                   // trim it
             check(opcode);
      SourceLine = SourceLine.trim();       
      SourceLine = SourceLine.substring(mark+1);  // use rest of string
      check(SourceLine);
     // echo("opcode="+opcode+"...");
     // echo("sourcelineafterop1="+SourceLine+"|||");
      mark = 0;             // remove all spaces
      while(mark != -1){
        mark = SourceLine.indexOf(' ');
        if(mark < 0) break;
        SourceLine = SourceLine.substring(0,maxInt(0,mark)) +
                     SourceLine.substring(mark+1);
      }
      check(SourceLine);
      mark = SourceLine.indexOf(',');             // parse out operand #1
      String op1 = SourceLine.substring(0,maxInt(0,mark));
      if(op1.length()==0) op1 = "-1";
      SourceLine = SourceLine.substring(mark+1);
      check(SourceLine);
      mark = SourceLine.indexOf(',');             // parse out operand #2
      String op2 = SourceLine.substring(0,maxInt(0,mark));
      if(op2.length()==0) op2 = "-1";
      String op3 = SourceLine.substring(mark+1);  // operand #3 is remainder
      if(op3.length()==0) op3 = "-1";

      // now have strings for all members

      int operand1 = -1;
      int operand2 = -1;
      int operand3 = -1;
      byte opbyte  = -1;
      byte op1flag = -1;
      byte op2flag = -1;
      byte op3flag = -1;
      int linenumber = -1;

      linenumber = Integer.parseInt(linenum);

      opbyte = opcode_value(opcode);   // get opcode value

      op1flag = setflag(op1);          // set the address mode flags
      op2flag = setflag(op2);      
      op3flag = setflag(op3);

      op1 = raw(op1);    // strip off any addressing mode flags
      op2 = raw(op2);
      op3 = raw(op3);
      
      operand1 = resolve(op1);           // get op1
      operand2 = resolve(op2);           // get op2
      operand3 = resolve(op3);           // write op3

      if(opbyte < 0){                    // assembler directive
        execute_directive(opbyte,op1,op2,op3);
        continue;                        // no output generated
      }

      validate(opbyte,op1flag,op2flag,op3flag,operand1,operand2,operand3);

      outfile.writeInt(linenumber);      // write line number

      outfile.writeByte(opbyte);         // write opcode
      outfile.writeByte(op1flag);
      outfile.writeByte(op2flag);
      outfile.writeByte(op3flag);

      outfile.writeInt(operand1);        // write operands
      outfile.writeInt(operand2);
      outfile.writeInt(operand3);

      echo("just wrote line# opcode op1,op2,op3 = "
                  +linenumber+" "+opbyte+" "+mode(op1flag)+operand1+","
                                 +mode(op2flag)+operand2+","
                                 +mode(op3flag)+operand3);

    }while(true);   // END TRANSLATION LOOP

  } // end of main()

  private static void execute_directive
    (byte opbyte,String op1,String op2,String op3) throws IOException {
    if (opbyte == -1){
      char k = op1.charAt(0);
      if ( ! (Character.isDigit(k)) ){
        echo("***Operand 1 must be numeric: Line: "+LineCount);
        System.exit(1);
      }
      if (op2 != "-1"){
        echo("***Operand 2 not allowed "+LineCount);
        System.exit(1);
      }

      int count = Integer.parseInt(op1);

      if (op3 == "-1"){
        last_address += count;
      }
      else{
        op3 = raw(op3);
        int number = resolve(op3); // make sure symbol goes in table
        last_address = last_address + count - 1;
      }
    }     
  }

  private static int resolve(String s) throws IOException{
    int temp;
    char k = s.charAt(0);
    if ( (Character.isDigit(k)) || (k == '-') || (k == '+') ){
      temp = Integer.parseInt(s);
    }
    else{
      temp = address(s);
    }
    return temp;
  }

  private static void init_symbol_table(){
    for (int i=0; i<TABLE_SIZE; i++){
      symbol[i] = "123...";
      location[i]= -1;
    }
    last_address = -1;        // so the first symbol has address zero
    last_index = -1;          // stored in table in entry zero 
  }

  private static int address(String s) throws IOException{
    int temp = -1;
    if (s == "") return temp;
    for (int i=0; i<=last_index; i++){   // see if already in list
      if (s.equals(symbol[i])){   //echo("comparing."+s+".to."+symbol[i]+".");
        temp = location[i];
        break;                    //echo("match!");
      }                        
    }
    if (temp == -1){   // not in table, yet
      last_index++;
      if(last_index > TABLE_SIZE){      // overflow
        echo("***Symbol Table Overflow at Line: "+LineCount
            +"at symbol: "+s);
        System.exit(1);
      }
      //echo("installing new symbol."+symbol[index]+".");
      symbol[last_index] = s;                 // enter it
      last_address++;
      location[last_index] = last_address;
      temp = last_address;
    }
    return temp;  //echo("returning address: "+index); pressEnter();
  }

  private static void check(String s){
    if (s.length() == 0){
       echo("***Unexpected end of line. Line: "+LineCount);
       System.exit(1);
    }
  }

  private static void validate(byte opcode,
                               byte flag1, byte flag2, byte flag3,
                               int field1, int field2, int field3){
    if ( (opcode < 0) || (opcode > 22) ){
       echo("***Bad opcode ("+opcode+") generated: Line: "+LineCount);
       System.exit(1);
    }
    if ( (flag1 != 1) && (flag1 != 0) ){
       echo("***Bad address mode ("+mode(flag1)+") operand 1: Line: "
               +LineCount);
       System.exit(1);
    }
    if ( (flag2 != 1) && (flag2 != 0) ){
       echo("***Bad address mode ("+mode(flag2)+") operand 2: Line: "
                +LineCount);
       System.exit(1);
    }
    if ( (flag3 != 1) && (flag3 != 0) ){
       echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
                +LineCount);
       System.exit(1);
    }

    // if (opcode == 0) "no checking needed"

    if ( (opcode < 6) && (flag3 == 0) ){
       echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
                +LineCount);
       System.exit(1);
    }
    if ( (opcode == 6) || (opcode == 7) ){
       if (flag1 != 0){
         echo("***Bad address mode ("+mode(flag1)+") operand 1: Line: "
                  +LineCount);
         System.exit(1);
       }
       if (flag3 != 1){
         echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
                  +LineCount);
         System.exit(1);
       }
    }
    if (opcode == 8){
       if (flag3 == 0){
         echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
                  +LineCount);
         System.exit(1);
       }
    }
    if (opcode == 9){
       if (flag3 == 1){
         echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
                  +LineCount);
         System.exit(1);
       }
    }
    if ( (opcode > 9) && (opcode < 16) ){
       if (flag3 == 1){
         echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
         +LineCount);
         System.exit(1);
       }
    }
    if (opcode == 16) {
       if (flag1 == 1){
         echo("***Bad address mode ("+mode(flag1)+") operand 1: Line: "
                  +LineCount);
         System.exit(1);
       }
    }
    if ( (opcode > 16) && (opcode < 22) ){
       if (flag3 == 0){
         echo("***Bad address mode ("+mode(flag3)+") operand 3: Line: "
                  +LineCount);
         System.exit(1);
       }
    }
  }  // end of validate()

  private static byte opcode_value(String s){
    String opcodes=
"NOPADDSUBMULDIVMODINCDECSTOJMPJEQJNEJLTJLEJGTJGESYSANDOR XORNOTNEGHLT";
    while (s.length() < 3) s = s + " ";
    byte temp = (byte) opcodes.indexOf(s);
    if (temp < 0)
      return temp;
    else 
      return (byte)(temp / 3);
  }

  private static byte setflag(String s){
    byte temp=1;                    // addressing mode 1 is direct (default)
    if (s.charAt(0) == '#') temp = 0;   // addressing mode zero is immediate
    return temp;
  }

  private static String raw(String s){
    String temp=s;
    if (temp.charAt(0) == '#') temp = s.substring(1);
    return temp;
  }

  private static String mode(byte flag){
    String temp = "direct ";
    if(flag == 0) temp = "immediate ";
    return temp;
  }

  private static int maxInt(int x1, int x2){
    int max;
    max = x1;
    if (x2 > x1) max=x2;
    return max;
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
  private static void echo(String s){ System.out.println(s); }
  private static void newline(){ System.out.println(" "); }
}
