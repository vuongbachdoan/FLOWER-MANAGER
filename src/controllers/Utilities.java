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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Order;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class Utilities {

    public boolean validateNumber(String input) {
        return !input.isEmpty() && input.matches("^[1-9]\\d*$");
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    public void sortOrderByTypeInput(ArrayList<Order> orders, final String term, final String type) {

        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if ("asc".equals(type)) {
                    if ("count".equals(term)) {
                        int count1 = 0;
                        int count2 = 0;
                        for (int count : o1.getFlowers().values()) {
                            count1 += count;
                        }
                        for (int count : o2.getFlowers().values()) {
                            count2 += count;
                        }
                        return count1 - count2;
                    } else if ("date".equals(term)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            return sdf.parse(o1.getDate()).compareTo(sdf.parse(o2.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    } else if ("price".equals(term)) {
                        return Float.compare(o1.getTotalPrice(), o2.getTotalPrice());
                    } else {
                        System.out.println("Invalid type");
                        return 0;
                    }
                } else if ("desc".equals(type)) {
                    if ("count".equals(term)) {
                        int count1 = 0;
                        int count2 = 0;
                        for (int count : o1.getFlowers().values()) {
                            count1 += count;
                        }
                        for (int count : o2.getFlowers().values()) {
                            count2 += count;
                        }
                        return count2 - count1;
                    } else if ("date".equals(term)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            return sdf.parse(o2.getDate()).compareTo(sdf.parse(o1.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    } else if ("price".equals(term)) {
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

    public Integer displayOrderFilteredbyDate(Order order, String fromDate, String toDate, int index, String idCurrentUser) throws ParseException {
        CustomerManager customerManager = new CustomerManager(idCurrentUser);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            Date dateCheck = formatter.parse(order.getDate());
            Date dateStart = formatter.parse(fromDate);
            Date dateEnd = formatter.parse(toDate);
            if (dateCheck.after(dateStart) && dateCheck.before(dateEnd)) {
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
        } else if (!fromDate.isEmpty()) {
            Date dateCheck = formatter.parse(order.getDate());
            Date dateStart = formatter.parse(fromDate);
            if (dateCheck.after(dateStart)) {
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
        } else if (!toDate.isEmpty()) {
            Date dateCheck = formatter.parse(order.getDate());
            Date dateEnd = formatter.parse(toDate);
            if (dateCheck.before(dateEnd)) {
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
        } else {
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
}
