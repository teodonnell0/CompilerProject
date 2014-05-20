package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by travis on 4/1/14.
 */
public class TerminalSymbols {


    private String[] terminalStrings =
            {
                    "id", "DECLARE", "BEGIN", "END", "BOOLEAN", "TRUE", "FALSE", "NULL", "NOT", "INT", "NUMBER",
                    "SMALLINT", "POSITIVE", "CHAR", "IF", "THEN", "WHILE", "LOOP", "DBMS_OUTPUT.PUT", "DBMS_OUTPUT.PUT_LINE", "DBMS_OUTPUT.NEW_LINE",
                    "+", "-", "*", "/", "MOD", ":", ",", ";", "<", ">", ">=", "<=", "<>", ":=", "(", ")", ".", "$",
                    "ERROR", "COMMENT", "num", "&", "'", "="
            };

    protected HashMap<String, Integer> map;


    public TerminalSymbols()
    {
        map = new HashMap<String, Integer>();
        initTreeMap();
    }

    private void initTreeMap()
    {
        int i = 1;
        for(String s : terminalStrings)
        {
            map.put(s, i++);
        }

    }
}

