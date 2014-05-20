package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

import java.io.*;

/**
 * Created by travis on 2/12/14.
 */
public class Scanner {
    private final char EOL = '\n';
    private final String[] KEYWORDS = {                    "id", "DECLARE", "BEGIN", "END", "BOOLEAN", "TRUE", "FALSE", "NULL", "NOT", "INT", "NUMBER",
            "SMALLINT", "POSITIVE", "CHAR", "IF", "THEN", "WHILE", "LOOP", "DBMS_OUTPUT.PUT", "DBMS_OUTPUT.PUT_LINE", "DBMS_OUTPUT.NEW_LINE",
            "+", "-", "*", "/", "MOD", ":", ",", ";", "<", ">", ">=", "<=", "<>", ":=", "(", ")", ".", "$",
            "ERROR", "COMMENT", "num", "&", "'", "="};

    private final char[] SYMBOLS = {'&', '+', '-', '*', '/', ';', ':', '\'',
                                    '<', '>', '=', '(', ')', '-', '$', '.'};     //These are in no particular order
    protected Token current;
    private BufferedReader br;
    private FileReader reader;
    private int col,            // Current carrot position from the start of current line
            pos,            // Current carrot position from the start of file
            line;           // Current line of file (starts at 1)
    private char ch;             // Points to next character
    private char[] charArray;          // Current String that is being processed
    private int charLength;
    private String type;           // used for token
    private int scope;
    private int link;
    private boolean EOF;
    private TerminalSymbols terminalSymbols;
    private NonTerminalSymbols nonTerminalSymbols;

    public Scanner(String fileName) {
        try {
            this.reader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            new ErrorHandler(0, fileName);
        }
        init();
    }


    private void init() {
        this.br = new BufferedReader(reader);
        this.charArray = new char[21]; // currently not sure how big this array should be
        this.line = 1;
        this.col = 0;
        this.scope = 0;
        this.link = 0;
        this.charLength = 0;
        this.EOF = false;
        getNextCharacter();     //Get the first character in file
        terminalSymbols = new TerminalSymbols();
        nonTerminalSymbols = new NonTerminalSymbols();
    }

    /**
     * Returns next character in the buffer reader
     * updates position, column, and line counters accordingly
     */
    private void getNextCharacter() {
        try {
            int i = 0;
            if ((i = reader.read()) != -1) {
                ch = (char) i;

                col++;
                pos++;
                if (ch == EOL) {
                    col = 0; // reset carrot position counter
                    line++;  // update line counter
                }
            } else
                this.EOF = true;

        } catch (IOException e) // Should only happen at EOF
        {
            e.printStackTrace();
        }
    }

    /**
     * Iterates over all whitespace until it finds a non whitespace char
     * If ch is ", iterate while storing everything in charArray until a second " appears (Unsure if the quotes get added as a separate token)
     * If ch is numeric, iterate while ch is numeric or a '.'
     * If ch is symbol (minus '"' and '_', create Token out of the single char
     * <p/>
     * If ch is alphabetic letter, iterate over text while allowing '_' and digits (digits or '_' cannot start a word token as of now)
     *
     * @return
     */
    protected Token getNextToken() {

        Token t;
        while (isWhiteSpace())
            getNextCharacter();
        if (isNumeric()) {
            if(!isNumeric())
            {
                new ErrorHandler(8, " '-' is not a number!");
            }
            while (isNumeric()) {
                charArray[charLength++] = ch;
                getNextCharacter();
            }
            t = createNumericToken();
            resetArray();
            return t;
        }

        if (isSymbol()) {
            t = createSymbolToken();
            if(t == null)
            {
                getNextToken();
                return null;
            }
            resetArray();
            if(t.tokenID == 41) t = getNextToken();
            return t;
        }

        if (isLetter()) {
            return getLettersForToken();
        }
        t = new Token();
        t.tokenValue = "ERROR";
        t.tokenID = terminalSymbols.map.get("ERROR");
        return t;

    }

    private Token getLettersForToken()
    {
        Token t;
        while (isLetter() || ch == '_' || isNumeric() || ch == '.') // Iterates through token allowing letters, numbers, and '_'. Also allows '.' for DBMS
        {
            charArray[charLength++] = ch;
            getNextCharacter();
        }
        t = createToken();
        resetArray();
        return t;
    }

    private Token createNumericToken() {
        Token t = new Token();
        String s = "";
        for (char c : charArray) {
            if (c == '\u0000')
                break;
            s += c;
        }
        t.tokenValue = s;
        t.tokenID = terminalSymbols.map.get("num");
        t.tokenType = "num";
        return t;
    }

    private Token createSymbolToken() {
        boolean twoChars = false;
        Token t = new Token();
        if (ch == '>') {
            getNextCharacter();
            switch (ch) {
                case '=':
                    t.tokenValue = ">=";
                    t.tokenID = terminalSymbols.map.get(">=");
                    getNextCharacter();
                    twoChars = true;
                    break;
                default:
                    t.tokenValue = ">";
                    t.tokenID = terminalSymbols.map.get(">");
                    break;
            }
        } else if (ch == '<') {
            getNextCharacter();
            switch (ch) {
                case '=':
                    t.tokenValue = "<=";
                    t.tokenID = terminalSymbols.map.get("<=");
                    twoChars = true;
                    getNextCharacter();
                    break;
                case '>':
                    t.tokenValue = "<>";
                    t.tokenID = terminalSymbols.map.get("<>");
                    twoChars = true;
                    getNextCharacter();
                    break;
                default:
                    t.tokenValue = "<";
                    t.tokenID = terminalSymbols.map.get("<");
                    break;

            }
        } else if (ch == ':')                          //Assuming as of now that  := will be assign
        {
            getNextCharacter();
            switch (ch) {
                case '=':
                    t.tokenValue = ":=";
                    t.tokenID = terminalSymbols.map.get(":=");
                    twoChars = true;
                    getNextCharacter();
                    break;
                default:
                    t.tokenValue = ":";
                    t.tokenID = terminalSymbols.map.get(":");
                    break;
            }
        } else if (ch == '-') {
            getNextCharacter();
            switch(ch)
            {
                case '-':
                    //Skip rest of
                    while(ch != '\n') {
                        getNextCharacter();
                    }
                    t.tokenValue = "--";
                    t.tokenID = terminalSymbols.map.get("COMMENT");
                    break;
                default:
                    t.tokenValue = "-";
                   t.tokenID = terminalSymbols.map.get("-");
                    break;
            }
        } else if(ch =='/')
        {
            getNextCharacter();
            switch (ch)
            {
                case '*':
                    //Skip everything until */ appears
                    boolean shouldSkipAhead = true;
                    while(shouldSkipAhead)
                    {
                        getNextCharacter();
                        if(ch == '*')
                        {
                            getNextCharacter();
                            switch (ch)
                            {
                                case '/':
                                    shouldSkipAhead = false;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    t.tokenValue = "COMMENT";
                    t.tokenID = terminalSymbols.map.get("COMMENT");
                    break;
                default:
                    t.tokenValue = "/";
                    t.tokenID = terminalSymbols.map.get("/");
                    break;
            }
        }
        else {

            t.tokenValue = String.valueOf(ch);
            t.tokenID = getSymbol();
        }
        if (!twoChars)
            getNextCharacter();
        return t;
    }

    private Token createToken() {
        Token t = new Token();
        String s = "";
        for (char c : charArray) {
            if (c == '\u0000')
                break;
            s += c;
        }
        t.tokenValue = s;
        if (isSymbol()) {
            t.tokenID = getSymbol();
        }
        if (isKeyword(s)) {
            t.tokenID = getKeyword(s);
        } else {
            t.tokenID = terminalSymbols.map.get("id");
            t.tokenType = "id";
        }
        return t;
    }

    // MOD gets taken char of via getKeyword since it is the only non character operator
    // Used string builder to make ch a String, otherwise ch will be casted to an integer causing errors.
    private int getSymbol() {
        return terminalSymbols.map.get(new StringBuilder().append(ch).toString());
    }


    private int getKeyword(String s) {

        if (s.equals("DBMS_OUTPUT.PUT"))
        {
            getNextCharacter();
            switch(ch)
            {
                case '_':
                {
                    resetArray();
                    getNextCharacter();
                    getNextCharacter();
                    getNextCharacter();
                    if(charArrayToString().equals("LINE"))
                        return terminalSymbols.map.get("DBMS_OUTPUT.PUT_LINE");
                    else
                        return terminalSymbols.map.get("ERROR");
                }
                default:
                    return terminalSymbols.map.get("DBMS_OUTPUT.PUT");
            }
        }
        return terminalSymbols.map.get(s);

    }

    private void resetArray() {
        charArray = new char[128];
        charLength = 0;
    }

    private boolean isLetter() {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
            return true;
        return false;
    }

    private boolean isNumeric() {
        if (ch >= '0' && ch <= '9')
            return true;
        return false;
    }

    private String charArrayToString() {
        String s = "";
        for (char c : charArray) {
            if (c == '\u0000')
                break;
            s += c;
        }
        return s;
    }

    /*
    Iterate through KEYWORDS array checking if current character is a valid symbol given by the languange grammar
*/
    private boolean isSymbol() {
        for (int i = 0; i < SYMBOLS.length; i++) {
            if (ch == SYMBOLS[i])
                return true;
        }
        return false;
    }


    /*
        Iterate through KEYWORDS array checking if 'charArray' is a language keyword or just a user identifier
    */
    private boolean isKeyword(String word) {
        for (int i = 0; i < KEYWORDS.length; i++) {
            if (word.equals(KEYWORDS[i]))
                return true;
        }
        return false;
    }

    /*
    Possible white space characters
     */
    private boolean isWhiteSpace() {
        if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r')
            return true;
        return false;
    }
}
