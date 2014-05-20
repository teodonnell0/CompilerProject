package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

/**
 * Created by travis on 4/3/14.
 */
public class ParseTable {
    private int[][] parseTable;
    private final int states = 109;
    private final int
            id=1,
            DECLARE=2,
            BEGIN=3,
            END=4,
            BOOLEAN=5,
            TRUE=6,
            FALSE=7,
            NULL=8,
            NOT=9,
            INT=10,
            NUMBER=11,
            SMALLINT=12,
            POSITIVE=13,
            CHAR=14,
            IF=15,
            THEN=16,
            WHILE=17,
            LOOP=18,
            PUT=19,
            PUT_LINE=20,
            NEW_LINE=21,
            ADD=22,
            SUB=23,
            MUL=24,
            DIV=25,
            MOD=26,
            COL=27,
            COM=28,
            SEM=29,
            LT=30,
            GT=31,
            GE=32,
            LE=33,
            NE=34,
            ASS=35,
            LP=36,
            RP=37,
            DOT=38,
            EOF=39,
            ERROR=40,
            COMMENT=41,
            num=42,
            AMP=43,
            APO=44,
            EQ=45,
            block=46,
            declarations=47,
            declare_rest=48,
            def=49,
            data_type=50,
            characters=51,
            size=52,
            size_option=53,
            numbers=54,
            compound_statement=55,
            optional_statements=56,
            statement_list=57,
            statement=58,
            lefthandside=59,
            righthandside=60,
            expression=61,
            simple_expression=62,
            term=63,
            factor=64,
            relop=65,
            addop=66,
            mulop=67,
            d=68,
            e=69,
            accept=0;

    public ParseTable()
    {
        parseTable = new int[states][70];
        initializeArray();
        addEntries();
    }

    private void initializeArray()
    {
        for(int i = 0; i < parseTable.length; i++)
            for(int j = 0; j < parseTable[0].length; j++)
                parseTable[i][j] = 999;
    }

    private void setDefaults(int state, int value)
    {
        for(int i = 0; i < 46; i++)
            parseTable[state][i] = value;
    }

    private void addEntries()
    {
        int state = 0;
        setDefaults(state, -3);
        parseTable[state][DECLARE] = 1;
        parseTable[state][block] = 2;
        parseTable[state][declarations] = 3;


        state = 1;
        setDefaults(state, -5);
        parseTable[state][id] = 4;
        parseTable[state][declare_rest] = 5;

        state = 2;
        parseTable[state][EOF] = 6;

        state = 3;
        parseTable[state][BEGIN] = 7;
        parseTable[state][compound_statement] = 8;

        state = 4;
        parseTable[state][BOOLEAN] = 9;
        parseTable[state][INT] =    10;
        parseTable[state][NUMBER] = 11;
        parseTable[state][SMALLINT] = 12;
        parseTable[state][POSITIVE] = 13;
        parseTable[state][CHAR] = 14;
        parseTable[state][data_type] = 15;
        parseTable[state][characters] = 16;
        parseTable[state][numbers] = 17;

        state = 5;
        setDefaults(state, -2);

        state = 6;
        setDefaults(state, accept);

        state = 7;
        parseTable[state][id] = 18;
        parseTable[state][BEGIN] = 7;
        parseTable[state][NULL] = 19;
        parseTable[state][IF] = 20;
        parseTable[state][WHILE] = 21;
        parseTable[state][PUT] = 22;
        parseTable[state][PUT_LINE] = 23;
        parseTable[state][NEW_LINE] = 24;
        parseTable[state][AMP] = 25;
        parseTable[state][compound_statement] = 26;
        parseTable[state][optional_statements] = 27;
        parseTable[state][statement_list] = 28;
        parseTable[state][statement] = 29;
        parseTable[state][lefthandside] = 30;

        state = 8;
        parseTable[state][EOF] = 31;

        state = 9;
        setDefaults(state, -10);

        state = 10;
        parseTable[state][LP] = 32;

        state = 11;
        parseTable[state][LP] = 33;

        state = 12;
        parseTable[state][LP] = 34;

        state = 13;
        parseTable[state][LP] = 35;

        state = 14;
        setDefaults(state, -11);

        state = 15;
        setDefaults(state, -7);
        parseTable[state][ASS] = 36;
        parseTable[state][def] = 37;

        state=16;
        setDefaults(state, -8);

        state = 17;
        setDefaults(state, -9);

        state = 18;
        parseTable[state][ASS] = 38;

        state = 19;
        parseTable[state][SEM] = 39;

        state = 20;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] = 44;
        parseTable[state][num] = 45;
        parseTable[state][expression] = 46;
        parseTable[state][simple_expression] = 47;
        parseTable[state][term] = 48;
        parseTable[state][factor] = 49;

        state = 21;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] = 44;
        parseTable[state][num] = 45;
        parseTable[state][expression] = 50;
        parseTable[state][simple_expression] = 47;
        parseTable[state][term] = 48;
        parseTable[state][factor] = 49;

        state = 22;
        parseTable[state][LP] = 51;

        state = 23;
        parseTable[state][LP] = 52;

        state = 24;
        setDefaults(state, -29);

        state = 25;
        parseTable[state][id] = 53;

        state = 26;
        setDefaults(state, -26);

        state = 27;
        parseTable[state][END] = 54;

        state = 28;
        setDefaults(state, -22);
        parseTable[state][SEM] = 55;

        state = 29;
        setDefaults(state, -23);

        state = 30;
        setDefaults(state, -25);

        state = 31;
        setDefaults(state, -1);

        state = 32;
        setDefaults(state, -13);
        parseTable[state][num] =  56;
        parseTable[state][size] = 57;

        state = 33;
        setDefaults(state, -13);
        parseTable[state][num] = 56;
        parseTable[state][size] = 58;

        state = 34;
        setDefaults(state, -13);
        parseTable[state][num] = 56;
        parseTable[state][size] = 59;

        state = 35;
        setDefaults(state, -13);
        parseTable[state][num] = 56;
        parseTable[state][size] = 60;

        state = 36;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] =44;
        parseTable[state][num] = 45;
        parseTable[state][APO] = 61;
        parseTable[state][righthandside] = 62;
        parseTable[state][expression] = 63;
        parseTable[state][simple_expression] = 47;
        parseTable[state][term] = 48;
        parseTable[state][factor] = 49;

        state = 37;
        parseTable[state][SEM] = 64;

        state = 38;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] =44;
        parseTable[state][num] = 45;
        parseTable[state][APO] = 61;
        parseTable[state][righthandside] = 65;
        parseTable[state][expression] = 63;
        parseTable[state][simple_expression] = 47;
        parseTable[state][term] = 48;
        parseTable[state][factor] = 49;

        state = 39;
        setDefaults(state, -21);

        state = 40;
        setDefaults(state, -42);

        state = 41;
        setDefaults(state, -44);

        state = 42;
        setDefaults(state, -45);

        state = 43;
        setDefaults(state, -46);

        state = 44;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] = 44;
        parseTable[state][num] = 45;
        parseTable[state][factor] = 66;

        state = 45;
        setDefaults(state, -43);

        state = 46;
        parseTable[state][THEN] = 67;

        state = 47;
        setDefaults(state, -36);
        parseTable[state][GT] = 68;
        parseTable[state][GE] = 69;
        parseTable[state][ASS] = 70;
        parseTable[state][LE] = 71;
        parseTable[state][LT] = 72;
        parseTable[state][NE] = 73;
        parseTable[state][ADD] = 74;
        parseTable[state][SUB] = 75;
        parseTable[state][relop] = 76;
        parseTable[state][addop] = 77;

        state = 48;
        setDefaults(state, -38);
        parseTable[state][MOD] = 78;
        parseTable[state][MUL] = 79;
        parseTable[state][DIV] = 80;
        parseTable[state][mulop] = 81;

        state = 49;
        setDefaults(state, -40);

        state = 50;
        parseTable[state][LOOP] = 82;

        state = 51;
        parseTable[state][id] = 83;

        state = 52;
        parseTable[state][id] = 84;

        state = 53;
        setDefaults(state, -30);

        state = 54;
        parseTable[state][SEM] = 85;

        state = 55;
        parseTable[state][id] = 18;
        parseTable[state][BEGIN] = 7;
        parseTable[state][IF] = 20;
        parseTable[state][WHILE] = 21;
        parseTable[state][PUT] = 22;
        parseTable[state][PUT_LINE] = 23;
        parseTable[state][NEW_LINE] = 24;
        parseTable[state][AMP] = 25;
        parseTable[state][compound_statement] = 26;
        parseTable[state][statement] = 86;
        parseTable[state][lefthandside] = 30;


        state = 56;
        setDefaults(state, -15);
        parseTable[state][COM] = 87;
        parseTable[state][size_option] = 88;

        state = 57;
        parseTable[state][RP] = 89;

        state = 58;
        parseTable[state][RP] = 90;

        state = 59;
        parseTable[state][RP] = 91;

        state = 60;
        parseTable[state][RP] = 92;

        state = 61;
        setDefaults(state, -35);

        state = 62;
        setDefaults(state, -6);

        state = 63;
        setDefaults(state, -34);

        state = 64;
        setDefaults(state, -5);
        parseTable[state][id] = 4;
        parseTable[state][declare_rest] = 93;

        state = 65;
        setDefaults(state, -33);

        state = 66;
        setDefaults(state, -47);

        state = 67;
        parseTable[state][id] = 18;
        parseTable[state][BEGIN] = 7;
        parseTable[state][IF] = 20;
        parseTable[state][WHILE] = 21;
        parseTable[state][PUT] = 22;
        parseTable[state][PUT_LINE] = 23;
        parseTable[state][NEW_LINE] = 24;
        parseTable[state][AMP] = 25;
        parseTable[state][compound_statement] = 26;
        parseTable[state][statement] = 94;
        parseTable[state][lefthandside] = 30;

        state = 68;
        setDefaults(state, -48);

        state = 69;
        setDefaults(state, -49);

        state = 70;
        setDefaults(state, -50);

        state = 71;
        setDefaults(state, -51);

        state = 72;
        setDefaults(state, -52);

        state = 73;
        setDefaults(state, -53);

        state = 74;
        setDefaults(state, -54);

        state = 75;
        setDefaults(state, -55);

        state = 76;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] = 44;
        parseTable[state][num] = 45;
        parseTable[state][simple_expression] = 95;
        parseTable[state][term] = 48;
        parseTable[state][factor] = 49;

        state = 77;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] = 44;
        parseTable[state][num] = 45;
        parseTable[state][term] = 96;
        parseTable[state][factor] = 49;

        state = 78;
        setDefaults(state, -58);

        state = 79;
        setDefaults(state, -56);

        state = 80;
        setDefaults(state, -57);

        state = 81;
        parseTable[state][id] = 40;
        parseTable[state][TRUE] = 41;
        parseTable[state][FALSE] = 42;
        parseTable[state][NULL] = 43;
        parseTable[state][NOT] = 44;
        parseTable[state][num] = 45;
        parseTable[state][factor] = 97;

        state = 82;
        parseTable[state][id] = 18;
        parseTable[state][BEGIN] = 7;
        parseTable[state][IF] = 20;
        parseTable[state][WHILE] = 21;
        parseTable[state][PUT] = 22;
        parseTable[state][PUT_LINE] = 23;
        parseTable[state][NEW_LINE] = 24;
        parseTable[state][AMP] = 25;
        parseTable[state][compound_statement] = 26;
        parseTable[state][statement] = 98;
        parseTable[state][lefthandside] = 30;

        state = 83;
        parseTable[state][RP] = 99;

        state = 84;
        parseTable[state][RP] = 100;

        state = 85;
        setDefaults(state, -20);

        state = 86;
        setDefaults(state, -24);

        state = 87;
        parseTable[state][num] = 101;

        state = 88;
        setDefaults(state, -12);

        state = 89;
        setDefaults(state, -17);

        state = 90;
        setDefaults(state, -16);

        state = 91;
        setDefaults(state, -18);

        state = 92;
        setDefaults(state, -19);

        state = 93;
        setDefaults(state, -4);

        state = 94;
        parseTable[state][END] = 102;

        state = 95;
        setDefaults(state, -37);
        parseTable[state][ADD] = 74;
        parseTable[state][SUB] = 75;
        parseTable[state][addop] = 77;

        state = 96;
        setDefaults(state, -39);
        parseTable[state][MOD] = 78;
        parseTable[state][MUL] = 79;
        parseTable[state][DIV] = 80;
        parseTable[state][mulop] = 81;

        state = 97;
        setDefaults(state, -41);

        state = 98;
        parseTable[state][END] = 103;

        state = 99;
        setDefaults(state, -28);

        state = 100;
        setDefaults(state, -27);

        state = 101;
        setDefaults(state, -14);

        state = 102;
        parseTable[state][IF] = 104;

        state = 103;
        parseTable[state][LOOP] = 105;

        state = 104;
        setDefaults(state, -31);

        state = 105;
        setDefaults(state, -32);
    }

    protected int getAction(int x, int y)
    {
        return parseTable[x][y];
    }


}
