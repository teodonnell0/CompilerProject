package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

/**
 * tokenName -
 * tokenID - token identifier for parser
 * tokenValue - value of token E.g. "3", ">", "test"
 * tokenType - type of token E.g. "INT", "SMALLINT", "IF", "("
 */
public class Token {
    protected int tokenID;
    protected String tokenValue;
    protected String tokenType;
    protected String tokenPlace;
}
