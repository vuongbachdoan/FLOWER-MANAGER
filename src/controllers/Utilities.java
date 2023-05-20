/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class Utilities {

    public boolean validateNumber(String input) {
        return input.matches("^[1-9]\\d*$");
    }

    public ArrayList<String> readFileData(String filePath) {
        ArrayList<String> fileData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                fileData.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    public String[] extractOrderDetailsFromFile(String dataString) {
        String[] arr = dataString.split(",");
        List<String> result = new ArrayList<>();
        for (String s : arr) {
            if (s.matches("F[0-9]+:[0-9]+:[0-9]+.[0-9]+")) {
                result.add(s);
            }
        }
        return result.toArray(new String[0]);
    }
    
    public String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
