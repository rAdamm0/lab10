package it.unibo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Controller {
    int stringNum = 0;

    public final void setLine(int n){
        this.stringNum=n;
    }

    public final String readLine(String input, int n){
        try (BufferedReader toRead = new BufferedReader(new FileReader(input))) {
            String result;
            int i = 0;
            do{
                result = toRead.readLine();
                i++;
            }while(i<n);
            return result;
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }


    public final String readLine(File input, int n){
        return readLine(input.getAbsolutePath(), n);
    }

    public final int getAfterColumn(String input){
        return Integer.parseInt(input.split(":")[1]);
    }

}
