/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import models.Order;

/**
 *
 * @author user
 */
public class Utilities {

    /**
     * Function to validate if user enter number
     *
     * @param input is input string of user
     * @return true if user entered number
     */
    public boolean validateNumber(String input) {
        return !input.isEmpty() && input.matches("^[1-9]\\d*$");
    }

    /**
     * Function to read data from file
     *
     * @param filePath is where storage file
     * @return file data
     */
    public ArrayList<String> readFileData(String filePath) {
        ArrayList<String> fileData = new ArrayList<>(); // storage data in file as array of strings
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            // read file line by line
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

    /**
     * Function to extract multiple order information from String
     *
     * @param dataString is string contains the orders information
     * @return array of orders information
     */
    public String[] extractOrderDetailsFromFile(String dataString) {
        String[] arr = dataString.split(",");
        List<String> result = new ArrayList<>();
        for (String s : arr) {
            if (s.matches("F[0-9]+:[0-9]+:[0-9]+.[0-9]+")) { // regex pattern 
                result.add(s); // if pattern matches, save to arr
            }
        }
        return result.toArray(new String[0]); // return found pattern
    }

    /**
     * Function to convert date format
     *
     * @param date is date object
     * @return string as this format yyyy-MM-dd
     */
    public String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); // formatter
        return formatter.format(date); // return string formatted
    }

    /**
     * Function to get sort options and sort in order
     *
     * @param orders is list need to be sorted
     * @param term is descending order or ascending order
     * @param type is sort by count, or price, or date
     */
    public void sortOrderByTypeInput(ArrayList<Order> orders, final String term, final String type) {
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if ("asc".equals(type)) { // sort as ascending
                    if ("count".equals(term)) { // sort by count
                        int count1 = 0;
                        int count2 = 0;
                        for (int count : o1.getFlowers().values()) {
                            count1 += count;
                        }
                        for (int count : o2.getFlowers().values()) {
                            count2 += count;
                        }
                        return count1 - count2;
                    } else if ("date".equals(term)) { // sort by date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            return sdf.parse(o1.getDate()).compareTo(sdf.parse(o2.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    } else if ("price".equals(term)) { // sort by price
                        return Float.compare(o1.getTotalPrice(), o2.getTotalPrice());
                    } else {
                        System.out.println("Invalid type");
                        return 0;
                    }
                } else if ("desc".equals(type)) { // sort as descending
                    if ("count".equals(term)) { // sort by count
                        int count1 = 0;
                        int count2 = 0;
                        for (int count : o1.getFlowers().values()) {
                            count1 += count;
                        }
                        for (int count : o2.getFlowers().values()) {
                            count2 += count;
                        }
                        return count2 - count1;
                    } else if ("date".equals(term)) { // sort by date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            return sdf.parse(o2.getDate()).compareTo(sdf.parse(o1.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    } else if ("price".equals(term)) { // sort by price
                        return Float.compare(o2.getTotalPrice(), o1.getTotalPrice());
                    } else {
                        System.out.println("Invalid type");
                        return 0;
                    }
                } else {
                    System.out.println("Invalid type");
                    return 0;
                }
            }
        });
    }

    /**
     * Function to display sorted list in range of date
     *
     * @param order is list need to be displayed
     * @param fromDate is start date
     * @param toDate is end date
     * @param index is index if order in table result
     * @param idCurrentUser is id of logged in user
     * @return filtered list
     * @throws ParseException
     */
    public Integer displayOrderFilteredbyDate(Order order, String fromDate, String toDate, int index, String idCurrentUser) throws ParseException {
        CustomerManager customerManager = new CustomerManager(idCurrentUser); // access customer manager functions
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); // formatter
        if (!fromDate.isEmpty() && !toDate.isEmpty()) { // case from date and end date inputted
            Date dateCheck = formatter.parse(order.getDate());
            Date dateStart = formatter.parse(fromDate);
            Date dateEnd = formatter.parse(toDate);
            if (dateCheck.after(dateStart) && dateCheck.before(dateEnd)) { // filter
                int totalFlower = 0;
                for (int flowerCount : order.getFlowers().values()) {
                    totalFlower += flowerCount;
                }
                System.out.printf(
                        "# %15s # %15s # %15s # %18s # %15s # %15s #\n",
                        String.valueOf(index), order.getOrderId(), order.getDate(), customerManager.findCustomer(order.getBuyerId()).getName(), String.valueOf(totalFlower), String.valueOf(order.getTotalPrice())
                );
                return 1;
            }
            return 0;
        } else if (!fromDate.isEmpty()) { // case only input from date
            Date dateCheck = formatter.parse(order.getDate());
            Date dateStart = formatter.parse(fromDate);
            if (dateCheck.after(dateStart)) { // filter
                int totalFlower = 0;
                for (int flowerCount : order.getFlowers().values()) {
                    totalFlower += flowerCount;
                }
                System.out.printf(
                        "# %15s # %15s # %15s # %18s # %15s # %15s #\n",
                        String.valueOf(index), order.getOrderId(), order.getDate(), customerManager.findCustomer(order.getBuyerId()).getName(), String.valueOf(totalFlower), String.valueOf(order.getTotalPrice())
                );
                return 1;
            }
            return 0;
        } else if (!toDate.isEmpty()) { // case only input end date
            Date dateCheck = formatter.parse(order.getDate());
            Date dateEnd = formatter.parse(toDate);
            if (dateCheck.before(dateEnd)) { // filter
                int totalFlower = 0;
                for (int flowerCount : order.getFlowers().values()) {
                    totalFlower += flowerCount;
                }
                System.out.printf(
                        "# %15s # %15s # %15s # %18s # %15s # %15s #\n",
                        String.valueOf(index), order.getOrderId(), order.getDate(), customerManager.findCustomer(order.getBuyerId()).getName(), String.valueOf(totalFlower), String.valueOf(order.getTotalPrice())
                );
                return 1;
            }
            return 0;
        } else { // user dont enter anythind
            int totalFlower = 0;
            for (int flowerCount : order.getFlowers().values()) {
                totalFlower += flowerCount;
            }
            System.out.printf(
                    "# %15s # %15s # %15s # %18s # %15s # %15s #\n",
                    String.valueOf(index), order.getOrderId(), order.getDate(), customerManager.findCustomer(order.getBuyerId()).getName(), String.valueOf(totalFlower), String.valueOf(order.getTotalPrice())
            );
            return 1;
        }
    }

    public boolean validateDate(String date) {
        String regex = "\\d{4}/\\d{2}/\\d{2}";
        return date.matches(regex);
    }
    
    public boolean validateTwoDate(String date1, String date2) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); // formatter
        Date date_1 =  formatter.parse(date1);
        Date date_2 =  formatter.parse(date2);
        return date_1.before(date_2);
    }
    
    public boolean validateId(String type, String id) {
        if(id.isEmpty()) return false;
        if(type.equals("FXXX")) {
            return id.matches("^F[0-9]{3}$");
        } else if (type.equals("OXXX")) {
            return id.matches("^O[0-9]{3}$");
        } else if (type.equals("AXXX")) {
            return id.matches("^A[0-9]{3}$");
        }
        return true;
    }
    
    public boolean validateFloat(String input) {
        return input.matches("^[-+]?\\d*\\.\\d+$");
    }
}
