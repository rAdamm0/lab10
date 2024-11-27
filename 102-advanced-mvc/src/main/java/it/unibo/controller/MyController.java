package it.unibo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyController {
    
    public final Map<String, Integer> readAllLines(String input){
        final Map<String,Integer> result = new HashMap<>();
        try (BufferedReader toRead = new BufferedReader(new FileReader(input))) {
            String temp;
            while((temp = toRead.readLine())!=null){
                result.put(temp.split(":")[0],Integer.parseInt(temp.split(": ")[1]));
            }
            return result;
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }

    public final Map<String, Integer> readAllLines(File input){
        return readAllLines(input.getAbsolutePath());
    }


}
