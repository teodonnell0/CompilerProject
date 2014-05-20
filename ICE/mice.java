/*
  mini-Intermediate Code Engine. (mICE)
  Might also serve as test driver for bytecodes
  and .class file generator(s).
  22 Apr 2002. Truman Parks Boyer.
*/

import java.io.*;
import java.math.*;
import java.util.*;

public class mice{
  static final int memory_limit = 32767;  // workspace size, arbitrary
  static final int table_limit = 256;     // module numbers 0-255
  static int ram[];                 // workspace for hosted program
  static int code[];                // intermediate code
  static int codetop;               // highest used code byte
  static String filename;           // intermediate code file
  static int handle;                // file handle if needed
  static int ln;                    // line number of current instruction
  static int opcode;                // current instruction
  static int op[];
  
  // virtual "registers"

  static int cs;       // active code segment (program unit)
  static int ip;       // program counter, points at next active instruction
  static int register1, register2; // working register
  static int dummy;    // placeholder field for unused return values
  static int parm1,parm2,parm3;   // dummy parameters for system calls

  public static DataInputStream codefile, datafile;   // quads, module data

  public static void main(String argv[]) throws IOException {

    System.out.println("   mini-Intermediate Code Engine (mICE)");
    System.out.println("Copyright (c) Truman Parks Boyer 22 Apr 2002");

    System.out.print("Intermediate Code File ?: ");
    filename = getString();

    // initialize state vector

    code = new int[memory_limit];
    ram = new int[memory_limit];
    op = new int[4];

    cs = 0;  
    ip = 0;  

    loadCode();               // program loader
//    dumpCode();

    initRam();                // initialize program memory

    for(;;){                  // loop forever;  fetch-decode-execute
      fetch();                // get next instruction
      switch(operation(opcode)){
        case 0:                              // NOP
               ip++;                         // No operation
               break;
        case 1:                              // ADD i/d, i/d, d
               ram[op[3]] = value(1)
                          + value(2);
               ip++;
               break;
        case 2:                              // SUB i/d, i/d, d
               ram[op[3]] = value(1)
                          - value(2);
               ip++;
               break;
        case 3:                              // MUL i/d, i/d, d
               ram[op[3]] = value(1)
                          * value(2);
               ip++;
               break;
        case 4:                              // DIV i/d, i/d, d
               ram[op[3]] = value(1)
                          / value(2);
               ip++;
               break;
        case 5:                              // MOD i/d, i/d, d
               ram[op[3]] = value(1)
                          % value(2);
               ip++;
               break;
        case 6:                              // INC i,, d
               ram[op[3]] = ram[op[3]] + op[1];
               ip++;
               break;
        case 7:                              // DEC i,, d
               ram[op[3]] = ram[op[3]] - op[1];
               ip++;
               break;
        case 8:                              // STO i/d,, d
               ram[op[3]] = value(1);
               ip++;               
               break;
        case 9:                              // JMP ,, i
               ip = op[3];
               break;
        case 10:                             // JEQ i/d, i/d, i
               if(value(1) == value(2))
                 ip = op[3];
               else
                 ip++;
               break;
        case 11:                             // JNE i/d, i/d, i
               if(value(1) != value(2))
                 ip = op[3];
               else
                 ip++;
               break;
        case 12:                             // JLT i/d, i/d, i
               if(value(1) < value(2))
                 ip = op[3];
               else
                 ip++;
               break;
        case 13:                             // JLE i/d, i/d, i
               if(value(1) <= value(2))
                 ip = op[3];
               else
                 ip++;
               break;
        case 14:                             // JGT i/d, i/d, i
               if(value(1) > value(2))
                 ip = op[3];
               else
                 ip++;
               break;
        case 15:                             // JGE i/d, i/d, i
               if(value(1) >= value(2))
                 ip = op[3];
               else
                 ip++;
               break;
        case 16:                             // SYS i, i/d, i/d
               if ((operandtype(opcode,2) == 1) && (op[2] < 0))
                 parm2 = -1;
               else
                 parm2 = value(2);
               if ((operandtype(opcode,3) == 1) && (op[3] < 0))
                 parm3 = -1;
               else
                 parm3 = value(3);

               if ((op[1] == 5) || (op[1] == -5)){  //string i/o parms
                 parm2 = op[2];
                 parm3 = op[3];
                 dummy = ICE_Call(op[1],parm2,parm3);
               }
               else                                 //"normal" parms
               if (op[1] <= 0){
                 dummy = ICE_Call(op[1],parm2,parm3);
               }
               else{
                 ram[op[3]] = ICE_Call(op[1],parm2,parm3);
               }
               ip++;
               break;
        case 17:                             // AND i/d, i/d, d
               register1=1;
               if (value(1) == 0)
                  register1 = 0;
               if (value(2) == 0)
                  register1 = 0;
               ram[op[3]] = register1;
               ip++;
               break;
        case 18:                             // OR  i/d, i/d, d
               register1 = 0;
               if (value(1) != 0)
                  register1 = 1;
               if (value(2) != 0)
                  register1 = 1;
               ram[op[3]] = register1;
               ip++;
               break;
        case 19:                             // XOR i/d, i/d, d
               register1 = (value(1) == 0) ? 0:1;
               register2 = (value(2) == 0) ? 0:1;
               if (register1 != register2)
                  register1 = 1;
               else
                  register1 = 0;
               ram[op[3]] = register1;
               ip++;
               break;
        case 20:                             // NOT ,,d 
               register1 = 0;
               if (value(3) == 0)
                  register1 = 1;
               ram[op[3]] = register1;
               ip++;
               break;
        case 21:                             // NEG ,,d
               register1 = 0;
               ram[op[3]] = -ram[op[3]];
               ip++;
               break;

        case 22:                             // HLT ,,   
               ICE_Exception(1);             // Halt
               return;

        default:                             // Bad OpCode
               ICE_Exception(0);
               return;

      }  // end switch
    } // end of f-d-e cycle

  } // end of main()

  private static void fetch() throws IOException{
    int i = ip*5;
    ln = code[i];
    opcode = code[i+1];
    op[1]    = code[i+2];
    op[2]    = code[i+3];
    op[3]    = code[i+4];
  }

  private static int operation(int integer){
    integer >>= 24;
    return integer;
  }

  private static int operandtype(int integer, int opno){
    byte bite;
    int bits_to_shift = (-(opno-3))*8;
    bite = (byte) (integer >> (bits_to_shift));
    return (int) bite;
  }

  private static int value(int opno){
    if (operandtype(opcode,opno)==1)         // direct address
        return (ram[op[opno]]);
    return (op[opno]);                       // immediate data
  }

  private static int ICE_Call(int request, int value, int value2)
    throws IOException {
    switch(request){
      case -5:                 //write string
            if (value2 < 0){   //null-terminated string write
              char k = '1';
              int i = value;
              while((k = (char) ram[i]) != (char) 0 ) {
                System.out.print( (char) k);
                i++;
              }
            }
            else{              //count-controlled (VLI) string write
              int count = ram[value];
              for(int i=1;i<count+1;i++){
                System.out.print( (char) ram[value+i]);
              }
            }
            return(-1);
      case -4:                 //write float (32-bit)
            System.out.print("floating point output not supported...");
            pressEnter();
            return(-1);
      case -3:                 //write boolean
            if(value==0)
              System.out.print('F');
            else
              System.out.print('T');
            return(-1);
      case -2:                 //write character
            System.out.print((char)value);
            return(-1);       
      case -1:                 //write integer
            System.out.print(value);
            return(-1);       
      case 0:                  //carriage return
            System.out.println();
            return(-1);       
      case +1:                 //read integer
            return(getInt());
      case +2:                 //read character
            return((int)getChar());
      case +3:                 //read boolean
            char k = getChar();
            switch(k){
              case 't': case 'T':
                      return(1);
              case 'f': case 'F':
                      return(0);
              default:
                      ICE_Exception(0);
                      return(-1);
            }
      case +4:                 //read float (32-bit)
            System.out.print("floating point input not supported...");
            pressEnter();
            return(-1);
      case +5:                 //read string
            int i;
            String word = getString();
            int read_limit = Math.min(Math.abs(value),word.length());
            if (value < 0){   //return null-terminated string
              for(i=0;i<read_limit;i++){
                ram[value2+i] = (int) word.charAt(i);
                //echo("stored "+ram[value2+i]+" at "+i);
              }
              ram[value2+i] = (char) 0;
              //echo("stored "+ram[value2+i]+" at "+i);
            }
            else{              //count-controlled (VLI) string write
              ram[value2] = read_limit;
              for(i=1;i<read_limit+1;i++){
                ram[value2+i] = (int) word.charAt(i-1);
              }
              //echo("[0]="+(char)ram[value2]+"[1]="+(char)ram[value2+1]);
            }
            return(-1);            
    } // end switch
    return(-1);       // return error code
  } // end ICE_call()

  private static void ICE_Exception(int code) throws IOException{
    System.out.println(" ");
    System.out.print("***ICE: ");
    switch(code){
      case 0:                  
            System.out.println("General Exception");
            pressEnter();
            break;
      case 1:
            System.out.println("Normal Program Termination");
            break;
      default:
            System.out.println("Unknown Exception");
            pressEnter();
            break;             
    } // end switch
  } // end ICE_Exception()

  private static void loadCode() throws IOException{
    int i;                                         // pointer into code[]
    for(i=0;i < memory_limit;i++){ code[i]= -1; }  // init code memory 

    // open code file
    try{ codefile=new DataInputStream(new FileInputStream(filename)); }
    catch(IOException e){ file_err(e.toString()+": could not open.\n"); };

    i = 0;           // loop until eof                              
    do{                                       // load entire file into memory
      try{ code[i++] = codefile.readInt(); }  // get int, load, bump index
      catch(EOFException EOF){ break; };      // if eof exit loop endif 
    } while(true);
    codetop = --i;       // save marker of last integer loaded

    // close file
    try{ codefile.close(); }
    catch(IOException e){ file_err(e.toString()+": could not close.\n"); }
  } // end loadCode()

  private static void file_err(String s){
    System.err.println(s);
    System.exit(1);  // abort
  }

  private static void initRam(){
    for(int i=0;i<memory_limit ;i++) ram[i]=0xffffffff;
  }

  private static void dumpCode() throws IOException {
    int i;                   // dump index
    int opcode;              // opcode to dump
    System.out.println("Code Dump...");
    for(i=0; i<codetop; i++){
      if (i%5 == 0) System.out.println(" ");
      if ((i%100 == 0) && (i != 0)) { newline(); pressEnter(); }
      if (i%5 == 1){
        opcode = code[i];
        if (operation(opcode) < 10) System.out.print(" ");
        System.out.print(operation(opcode)+":");
        System.out.print(operandtype(opcode,1)+":");
        System.out.print(operandtype(opcode,2)+":");
        System.out.print(operandtype(opcode,3));
      }
      else
        System.out.print(fitTo(10,code[i]));
      System.out.print(" ");
    }
    newline();
    System.out.println("End of Code Dump..."); pressEnter();
  }

  private static String fitTo(int len, int num){
    String temp = Integer.toString(num);
    int start = temp.length();
    for (int i=start;i<=len;i++){
      temp = " " + temp;       
    }
    return temp;
  }

  private static void dumpModuleData() throws IOException  {
    int i;                   // dump index
    for(i=0; i< table_limit; i++){
      if (i%2 == 0) System.out.println(" ");
      if (i%40 == 0) { newline(); pressEnter(); }
      if (code[i] < 100) System.out.print(" ");
      if (code[i] < 10)  System.out.print(" ");
      if (code[i] < 0)   System.out.print("\b");   // backspace if negative
      if (code[i] < -10) System.out.print("\b");
      System.out.print(code[i]);
      System.out.print(" ");
    }
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
