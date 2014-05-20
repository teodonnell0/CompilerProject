package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

/**
 * stack[0] = 0;
 input = getNextToken()
 Repeat  // while not accept and not erre
 Action = PTable[stacklook[Top], input]
 case Action
 +  push(input), push(action), input = getNextToken()
 -  for i in To Grammar[Action][0] //the number of items in row
 {
 pop();
 if pop()<>Grammar[Action][i] // not equal
 { Error(#); Exit}
 }
 push(Grammar[action][Grammar[Action][0]+1])
 //last element (LHS)
 push(PTable[stack[top-1][stack[top])
 99  Exit;
 0  Exit;
 */
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Created by travis on 4/3/14.
 */
public class Parser {
    private Stack parseStack, valueStack, modeStack, ifStack, whileStack, whileStack2;
    private QuadArray quadArray;
    private ParseTable parseTable;
    private Token input;
    private Scanner scanner; //The actual class within package, not to be confused with Java.util.Scanner
    private GrammarTable grammarTable;
    private SymbolTable symbolTable;
    private Token temp;
    private int quadCount, placeCount, action, state;
    private StringBuilder stringBuilder;



    public Parser(Scanner scan)
    {
        initStacks();
        initStructures();
        initCounters();

        stringBuilder = new StringBuilder();
        push(0, true);
        scanner = scan;
        input = scanner.getNextToken();
        int state = (Integer)parseStack.peek();
        action = parseTable.getAction(state,input.tokenID);

        boolean ifExp = false;
        boolean whileExp = false;

        while(!parseStack.empty() && action != 0 && action != 999)
        {
            if(input.tokenID == 15) // if
                ifExp = true;
            if(input.tokenID == 17) //while
            {
                whileExp = true;
                whileStack2.push(quadCount);
            }

            if(action > 0 && input.tokenID != 41) //push and shift
            {
                push(input, false);
                push(action, true);
                if(input.tokenID != 39)
                    input = scanner.getNextToken();
            }

            else

            if(action < 0) // reduce
            {
                action *= -1;
                int j = grammarTable.grammarTable[action][0];
                Token t = new Token();
                Token s = new Token();

                switch(action)
                {
                    case 4:
                    {
                        Token[] tokens = new Token[5];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = nextPlace();
                        String name = tokens[4].tokenValue;

                        if(tokens[3].tokenType.equals("char"))
                        {
                            s.tokenType = tokens[3].tokenType;
                            symbolTable.insert(name, s);
                            quadArray.gen(""+nextQuad(), "sto", "#32", "", s.tokenValue); //Space by default
                        }else
                        if(tokens[2].tokenType.equals("num") && tokens[3].tokenType.equals("integer") || tokens[2].tokenType.equals(tokens[3].tokenType))
                        {
                            s.tokenType = tokens[3].tokenType;
                            symbolTable.insert(name, s);
                            quadArray.gen(""+nextQuad(), "sto", tokens[2].tokenValue, "", s.tokenValue);
                        }
                        else
                        {
                            new ErrorHandler(5, tokens[3].tokenType + " and " + tokens[2].tokenType);
                        }
                        break;
                    }
                    case 6:
                    {
                        Token[] tokens = new Token[2];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = tokens[0].tokenType;
                        s.tokenValue = tokens[0].tokenValue;
                        break;
                    }
                    case 10:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = "boolean";
                        break;
                    }
                    case 11:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = "char";
                        break;
                    }
                    case 13:
                    {
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = null;
                        s.tokenType = "empty_num";
                        break;
                    }
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    {
                        Token[] tokens = new Token[4];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = "integer";
                        s.tokenValue = null;
                        break;
                    }
                    case 27:
                    {
                        Token[] tokens = new Token[4];
                        for(int i = 1; i < j ; i++)
                        {
                            popState();
                            t = popToken();
                            if(t.tokenID != grammarTable.grammarTable[action][i])
                                new ErrorHandler(2, "Found " + t.tokenID + ", expected " +grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        if((tokens[1].tokenType).equals("id") && symbolTable.contains(tokens[1].tokenValue))
                        {
                            Token temp = symbolTable.retrieve(tokens[1].tokenValue);
                            quadArray.gen(""+nextQuad(), "sys", "#-1", temp.tokenValue,"");
                            quadArray.gen(""+nextQuad(), "sys", "#0", "", "");
                        }
                        else
                            new ErrorHandler(3, nextQuad() + ", " + tokens[2].tokenType + " " + tokens[1].tokenType);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = null;
                        s.tokenType = null;
                        break;
                    }
                    case 28:
                    {
                        Token[] tokens = new Token[4];
                        for(int i = 1; i < j ; i++)
                        {
                            popState();
                            t = popToken();
                            if(t.tokenID != grammarTable.grammarTable[action][i])
                                new ErrorHandler(2, "Found " + t.tokenID + ", expected " +grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }

                        if((tokens[1].tokenType).equals("id")) {
                            if(symbolTable.contains(tokens[1].tokenValue)) {
                                String name = symbolTable.retrieve(tokens[1].tokenValue).tokenValue;
                                quadArray.gen("" + nextQuad(), "sys", "#-1", name, "");
                            }
                        }
                        else
                            new ErrorHandler(3, nextQuad() + ", " + tokens[2].tokenType + " " + tokens[1].tokenType);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = null;
                        s.tokenType = null;
                        break;
                    }
                    case 29:
                    {
                        popState();
                        t = popToken();
                        if(t.tokenID != grammarTable.grammarTable[action][1])
                            new ErrorHandler(2, "Found " + t.tokenID + ", expected " +grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = "new_line";
                        quadArray.gen(""+nextQuad(), "sys", "#0", "", "");
                        break;
                    }
                    case 30:
                    {
                        Token[] tokens = new Token[j-1];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        if(tokens[0].tokenType.equals("id") && symbolTable.contains(tokens[0].tokenValue))
                        {
                            Token temp = symbolTable.retrieve(tokens[0].tokenValue);
                            quadArray.gen(""+nextQuad(), "sys", "#1", "",temp.tokenValue );
                            s.tokenValue = temp.tokenValue;
                            s.tokenType = temp.tokenType;
                            s.tokenID = grammarTable.grammarTable[action][j];
                        }
                        else
                            new ErrorHandler(6, "Cannot find symbol: " + t.tokenValue);
                        break;
                    }
                    case 31:
                    {
                        Token[] tokens = new Token[j-1];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        quadArray.backpatch((Integer)ifStack.pop(), quadCount);
                        ifExp = false;
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = t.tokenType;
                        s.tokenValue = null;
                        break;

                    }

                    case 32:
                    {
                        Token[] tokens = new Token[j-1];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t= popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        quadArray.backpatch((Integer)whileStack.pop(), quadCount+1);
                        whileExp = false;
                        quadArray.gen("" + nextQuad(), "jmp", "", "", "#" + whileStack2.pop());
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = t.tokenType;
                        s.tokenValue = null;
                        break;
                    }

                    case 33:
                    {
                        Token[] tokens = new Token[3];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }

                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = t.tokenType;
                        s.tokenValue = null;

                        if(symbolTable.contains(tokens[2].tokenValue))
                        {

                            quadArray.gen("" + nextQuad(), "sto", tokens[0].tokenValue, "", symbolTable.retrieve(tokens[2].tokenValue).tokenValue);
                        }
                        else
                            new ErrorHandler(6, tokens[2].tokenValue);
                        break;

                    }
                    case 36:
                    {
                        popState();
                        t = popToken();
                        if(t.tokenID != grammarTable.grammarTable[action][1])
                            new ErrorHandler(2, "Found " + t.tokenID + ", expected " +grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = t.tokenType;
                        break;
                    }
                    case 37:
                    {
                        Token[] tokens = new Token[3];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = "boolean";
                        s.tokenValue = nextPlace();
                        if(tokens[2].tokenType.equals("num") && tokens[0].tokenType.equals("num")) {

                            quadArray.gen("" + nextQuad(), tokens[1].tokenValue, tokens[2].tokenValue, tokens[0].tokenValue, "#" + (quadCount + 2));
                            quadArray.gen("" + nextQuad(), "sto", "#0", "", s.tokenValue);
                            quadArray.gen("" + nextQuad(), "jmp", "", "", "#" + (quadCount + 1));
                            quadArray.gen("" + nextQuad(), "sto", "#1", "", s.tokenValue);

                        }

                        if(tokens[2].tokenType.equals("num") && tokens[0].tokenType.equals("id")) {
                            if(symbolTable.contains(tokens[0].tokenValue))
                            {
                                Token token = symbolTable.retrieve(tokens[0].tokenValue);
                                quadArray.gen("" + nextQuad(), tokens[1].tokenValue, tokens[2].tokenValue, token.tokenValue, "#" + (quadCount + 2));
                                quadArray.gen("" + nextQuad(), "sto", "#0", "", s.tokenValue);
                                quadArray.gen("" + nextQuad(), "jmp", "", "", "#" + (quadCount + 1));
                                quadArray.gen("" + nextQuad(), "sto", "#1", "", s.tokenValue);
                            }
                            else
                                new ErrorHandler(6, ""+quadCount + ": " + tokens[0].tokenValue);
                        }

                        if(tokens[2].tokenType.equals("id") && tokens[0].tokenType.equals("num")) {

                            if(symbolTable.contains(tokens[2].tokenValue))
                            {
                                Token token = symbolTable.retrieve(tokens[2].tokenValue);
                                quadArray.gen("" + nextQuad(), tokens[1].tokenValue, token.tokenValue, tokens[0].tokenValue, "#" + (quadCount + 2));
                                quadArray.gen("" + nextQuad(), "sto", "#0", "", s.tokenValue);
                                quadArray.gen("" + nextQuad(), "jmp", "", "", "#" + (quadCount + 1));
                                quadArray.gen("" + nextQuad(), "sto", "#1", "", s.tokenValue);
                            }
                            else
                                new ErrorHandler(6, ""+quadCount + ": " + tokens[0].tokenValue);

                        }

                        if(tokens[2].tokenType.equals("id") && tokens[0].tokenType.equals("id")) {
                            if(symbolTable.contains(tokens[2].tokenValue) && symbolTable.contains(tokens[0].tokenValue))
                            {
                                Token token = symbolTable.retrieve(tokens[2].tokenValue);
                                Token token1 = symbolTable.retrieve(tokens[0].tokenValue);
                                quadArray.gen("" + nextQuad(), tokens[1].tokenValue, token.tokenValue, token1.tokenValue, "#" + (quadCount + 2));
                                quadArray.gen("" + nextQuad(), "sto", "#0", "", s.tokenValue);
                                quadArray.gen("" + nextQuad(), "jmp", "", "", "#" + (quadCount + 1));
                                quadArray.gen("" + nextQuad(), "sto", "#1", "", s.tokenValue);
                            }
                            else
                                new ErrorHandler(6, ""+quadCount + ": " + tokens[0].tokenValue);


                        }
                        if(ifExp)
                        {
                            ifStack.push(quadCount);
                            quadArray.gen("" + nextQuad(), "jne", s.tokenValue, "#1", "#");
                            ifExp = false;
                        }
                        if(whileExp)
                        {
                            whileStack.push(quadCount);
                            quadArray.gen(""+nextQuad(), "jne", s.tokenValue, "#1", "#");
                            whileExp = false;
                        }
                        break;
                    }
                    case 39: /* expression simple_expression addop term */
                    case 41: /* term term mulop factor */
                    {
                        Token[] tokens = new Token[3];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = "num";
                        s.tokenValue = nextPlace();
                        if(tokens[0].tokenType.equals("num") && tokens[2].tokenType.equals("num"))
                            quadArray.gen(""+nextQuad(), tokens[1].tokenValue, tokens[2].tokenValue, tokens[0].tokenValue, s.tokenValue);
                        else
                        if(tokens[2].tokenType.equals("id") && tokens[0].tokenType.equals("num"))
                        {
                            if(symbolTable.contains(tokens[2].tokenValue)) {
                                Token temp = symbolTable.retrieve(tokens[2].tokenValue);
                                quadArray.gen("" + nextQuad(), tokens[1].tokenValue, temp.tokenValue, tokens[0].tokenValue, s.tokenValue);
                            } else new ErrorHandler(6, nextQuad() + ", " + tokens[2].tokenValue);
                        }
                        else
                        if(tokens[0].tokenType.equals("id") && tokens[2].tokenType.equals("num"))
                        {
                            if(symbolTable.contains(tokens[0].tokenValue)) {
                                Token temp = symbolTable.retrieve(tokens[0].tokenValue);
                                quadArray.gen("" + nextQuad(), tokens[1].tokenValue, temp.tokenValue, tokens[2].tokenValue, s.tokenValue);
                            } else new ErrorHandler(6, nextQuad() + ", " + tokens[0].tokenValue);
                        } else
                        if(tokens[2].tokenType.equals("id") && tokens[0].tokenType.equals("id") && symbolTable.contains(tokens[2].tokenValue))
                        {
                            Token temp = symbolTable.retrieve(tokens[2].tokenValue);
                            Token temp1 = symbolTable.retrieve(tokens[0].tokenValue);
                            quadArray.gen(""+nextQuad(), tokens[1].tokenValue, temp.tokenValue, temp1.tokenValue, s.tokenValue);
                        }else
                            new ErrorHandler(3, nextQuad() + ", " + tokens[2].tokenType + " " + tokens[0].tokenType);
                        break;
                    }
                    case 34:
                    case 38:
                    case 40:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = t.tokenType;
                        break;
                    }
                    case 42:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        if(symbolTable.contains(t.tokenValue))
                        {
                            s.tokenID = grammarTable.grammarTable[action][j];
                            s.tokenValue = t.tokenValue;
                            s.tokenType = t.tokenType;
                        }
                        else
                        {
                            new ErrorHandler(6, "Cannot find symbol: " + t.tokenValue);
                        }
                        break;
                    }
                    case 43:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "#"+t.tokenValue;
                        s.tokenType = t.tokenType;
                        break;
                    }
                    case 44:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "#"+1;
                        s.tokenType = "boolean";
                        break;
                    }
                    case 45:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "#"+0;
                        s.tokenType = "boolean";
                        break;
                    }
                    case 46:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = "boolean";
                        break;
                    }
                    case 47: /* factor not factor */
                    {
                        Token[] tokens = new Token[2];
                        for(int i = 1; i < j; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                            tokens[i-1] = t;
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenType = "boolean";
                        s.tokenValue = nextPlace();
                        if(tokens[0].tokenType.equals("boolean"))
                            quadArray.gen(""+nextQuad(), "not", tokens[0].tokenValue, "", s.tokenValue);
                        else
                            new ErrorHandler(3, nextQuad() + ", " + tokens[0].tokenValue+ " " + s.tokenValue);
                        break;
                    }
                    case 48:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "JGT";
                        s.tokenType = "relop";
                        break;
                    }
                    case 49:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "JGE";
                        s.tokenType = "relop";
                        break;
                    }
                    case 50:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "JEQ";
                        s.tokenType = "relop";
                        break;
                    }
                    case 51:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "JLE";
                        s.tokenType = "relop";
                        break;
                    }
                    case 52:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "JLT";
                        s.tokenType = "relop";
                        break;
                    }
                    case 53:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "JNE";
                        s.tokenType = "relop";
                        break;
                    }
                    case 54:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "add";
                        s.tokenType = "addop";
                        break;
                    }
                    case 55:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "sub";
                        s.tokenType = "addop";
                        break;
                    }
                    case 56:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "mul";
                        s.tokenType = "mulop";
                        break;
                    }
                    case 57:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "div";
                        s.tokenType = "mulop";
                        break;
                    }
                    case 58:
                    {
                        popState();
                        t = popToken();
                        checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][1]);
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = "mod";
                        s.tokenType = "mulop";
                        break;
                    }

                    default:
                        for(int i = 1; i < j ; i++)
                        {
                            popState();
                            t = popToken();
                            checkEqualTokens(t.tokenID, grammarTable.grammarTable[action][i]);
                        }
                        s.tokenID = grammarTable.grammarTable[action][j];
                        s.tokenValue = t.tokenValue;
                        s.tokenType = t.tokenType;
                        break;

                }
                push(s, false);
                push(parseTable.getAction((Integer)parseStack.get(parseStack.size()-2), (Integer) parseStack.peek()), true);
                action = (Integer)parseStack.peek();

            }

            if(action == 999) new ErrorHandler(7, "Syntax error");
            if(input.tokenID != 41)
                action = (Integer)parseTable.getAction(action, input.tokenID);
            else
                input = scanner.getNextToken();
            if(action == 999) new ErrorHandler(7, "Syntax error");
        }

        if(action == 0)
            System.out.println("Compiled with success! Run the output.t file into mini and mice for execution");

        quadArray.writeOutput();
    }

    /**
     * Initializes all stacks to be used
     */
    private void initStacks()
    {
        parseStack = new Stack();
        valueStack = new Stack();
        modeStack = new Stack();
        ifStack = new Stack();
        whileStack = new Stack();
        whileStack2 = new Stack();
    }

    private void initCounters()
    {
        action = -1;
        quadCount = 0;
        placeCount = 0;
    }

    /**
     * Sets up self made data structures
     */
    private void initStructures()
    {
        quadArray = new QuadArray();
        parseTable = new ParseTable();
        grammarTable = new GrammarTable();
        symbolTable = new SymbolTable();
    }

    /**
     * For use while reducing, checks if tokens match what they are supposed to
     * @param i
     * @param j
     */
    private void checkEqualTokens(int i, int j)
    {
        if(i == j)
            return;
        new ErrorHandler(2, "Found token: " + i + "\nExpected token: " + j);
    }

    /**
     * Pushes state/token on stacks.
     * @param item
     * @param state
     */
    private void push(Object item, boolean state)
    {
        if(state)
        {
            parseStack.push(item);
            valueStack.push(null);
            modeStack.push(null);
        }
        else
        {
            if((item instanceof Token))
            {
                parseStack.push(((Token)item).tokenID);
                valueStack.push(((Token)item).tokenValue);
                modeStack.push(((Token)item).tokenType);
            } else { throw new Error("Compiler Error"); }
        }
    }


    /**
     * Only should be used to pop off state from stack
     */
    private void popState()
    {
        parseStack.pop();
        valueStack.pop();
        modeStack.pop();
    }

    private Token popToken()
    {
        Token t = new Token();
        t.tokenID = (Integer)parseStack.pop();
        t.tokenValue = (String)valueStack.pop();
        t.tokenType = (String)modeStack.pop();
        return t;
    }

    private boolean lookup(String name)
    {
        if(symbolTable.contains(name))
            return true;
        return false;
    }

    private int nextQuad()
    {
        return quadCount++;
    }



    private String nextPlace()
    {
        return ""+placeCount++;
    }



    public static void main(String[] args) {
        if(args.length != 1)
        {
            new ErrorHandler(9, "You need to specify an input file!\nUsage java -jar compiler.jar inputFile");
        }
        File input = new File(args[0]);
        if(!input.exists())
        {
            new ErrorHandler(0, "Please make sure you are inputting the correct file\nAttempted input: "+args[0]);
        }
        Scanner scan = new Scanner(args[0]);
        Parser parser = new Parser(scan);
    }




}
