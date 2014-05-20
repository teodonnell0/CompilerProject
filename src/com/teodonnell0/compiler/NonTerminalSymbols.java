package com.teodonnell0.compiler;
/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Non terminal values are stored into a tree map for easy lookup. Each non terminal is associated with a unique integer
 * id. The id will be used in the parsing table rather than the actual string
 */
public class NonTerminalSymbols {
    protected HashMap<String, Integer> map;

    private final String[] nonTermString =
            {
                    "block", "declarations", "declare_rest", "default", "data_type", "characters", "size", "size_option",
                    "numbers", "compound_statement", "optional_statements", "statement_list", "statement",
                    "lefthandside", "righthandside", "expression", "simple_expression", "term", "factor", "relop",
                    "addop", "mulop"
            };

    public NonTerminalSymbols()
    {
        map = new HashMap<String, Integer>();
        initTreeMap();
    }

    private void initTreeMap()
    {
        int i = 46; //start values at 200
        for(String s: nonTermString)
        {
            map.put(s, i++);
        }
    }

}
