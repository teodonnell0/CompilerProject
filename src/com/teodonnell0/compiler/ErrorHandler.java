package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

/**
 * Created by travis on 3/13/14.
 */
public class ErrorHandler {

    private String[] errorTypes =
            {
                    "Could not read file",
                    "Unexpected end of file",
                    "Unexpected token",
                    "Type error",
                    "Token name already found in symbol table",
                    "Mismatch data types",
                    "Unknown token found",
                    "Internal Compiler Error",
                    "Invalid Number",
                    "Invalid Usage"

            };


    public ErrorHandler(int errorNo, String arg)
    {
        System.out.println(errorTypes[errorNo] + ": " + arg);
        System.exit(-1);
    }
}
