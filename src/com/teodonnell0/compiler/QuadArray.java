package com.teodonnell0.compiler;

/******************************************************************************
 * Created by Travis O'Donnell                                                *
 * Copyright (c) 2014.                                                        *
 * Frostburg State University                                                 *
 ******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by travis on 5/1/14.
 */
public class QuadArray {
    private String[][] quadArray;
    private int count;

    public QuadArray()
    {
        quadArray = new String[10][5];
        count = 0;
    }

    public void gen(String lineNo, String op, String param1, String param2, String param3)
    {
        quadArray[count++] = new String[]{lineNo, op, param1, param2, param3};
        if(count == quadArray.length)
            expandArray();
    }

    /**
     * Doubles array size
     */
    private void expandArray()
    {
        String[][] temp = quadArray;
        quadArray = new String[temp.length*2][5];
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[0].length; j++)
                quadArray[i][j] = temp[i][j];
    }

    public String getLine(int i)
    {
        if(i < quadArray.length)
            return new String(quadArray[i][0] + " " + quadArray[i][1] + " " + quadArray[i][2] + ", " + quadArray[i][3] + ", " + quadArray[i][4]);
        else
        {
            new ErrorHandler(7, "Tried accessing quad array line " + i);
            return null;
        }
    }

    public void writeOutput()
    {
        gen(""+count, "hlt", "", "", "");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < count; i++)
        {
            stringBuilder.append(getLine(i) + "\n");
        }
        try {
            PrintWriter writer = new PrintWriter("output.t");
            for(int i = 0; i < count; i++)
                writer.println(getLine(i));     //writes data to output.t
            writer.close();
        } catch (FileNotFoundException e)
        {
            new ErrorHandler(7, "Compiler should have already created output.t");
        }




    }

    public void backpatch(int i, int j)
    {
        quadArray[i][4] += j;
    }
}

