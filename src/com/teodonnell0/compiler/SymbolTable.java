package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

import java.util.HashMap;

public class SymbolTable{

    private HashMap<String, Token> hashMap;
    /*
        Initializes SymbolTable
     */
    public SymbolTable()
    {
        hashMap = new HashMap<String, Token>();
    }



    protected void insert(String s, Token t)
    {
        if(hashMap.containsKey(s))
        {
            new ErrorHandler(4, s);
        }
        else
        {
            hashMap.put(s, t);
        }
    }


    protected boolean contains(String s)
    {
        return hashMap.containsKey(s);
    }

    protected Token retrieve(String s)
    {
        return hashMap.get(s);
    }
}