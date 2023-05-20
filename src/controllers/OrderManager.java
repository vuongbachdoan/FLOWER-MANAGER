/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import models.Flower;
import models.Order;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class OrderManager {

    private Scanner scanner = new Scanner(System.in);
    private String idCurrnetUser;
    private Utilities utils = new Utilities();
    private FlowerManager flowerManger = new FlowerManager();
    private ArrayList<Order> orders = new ArrayList<>();
    private String orderId;
    private String date;
    private Float totalPrice = 0.0f;
    private String buyerId;
    private HashMap<String, Integer> flowerOrder = new HashMap<>();

    public OrderManager(String idCurrnetUser) {
        this.idCurrnetUser = idCurrnetUser;

        // fetch orders from file
        ArrayList<String> ordersFromFile = utils.readFileData("src/data/orders.dat");
        for (String orderString : ordersFromFile) {
            this.orderId = orderString.split(",")[0];
            this.date = orderString.split(",")[1];
            this.totalPrice = Float.parseFloat(orderString.split(",")[2]);
            this.buyerId = orderString.split(",")[3];

//            String[] flowersString = utils.extractOrderDetailsFromFile(orderString);
//            for (String flowerString : flowersString) {
//                String flowerId = flowerString.split(":")[0];
//                int flowerQuantities = Integer.parseInt(flowerString.split(":")[1]);
//                flowerOrder.put(
//                        flowerId,
//                        flowerQuantities
//                );
//            }
            orders.add(
                    new Order(this.orderId, this.date, this.buyerId, this.flowerOrder, this.totalPrice)
            );
        }

        this.orderId = "O" + String.format("%03d", orders.size());
        this.buyerId = this.idCurrnetUser;
        this.date = utils.convertDate(new Date());
        this.totalPrice = 0.0f;
    }

    public void addFlower() {
        System.out.print("Enter flower ID FXXX: ");
        String flowerId = scanner.nextLine();
        System.out.print("Enter quantities: ");
        int quantities = scanner.nextInt();

        Flower flower = flowerManger.findFlower(flowerId);
        if (flower != null) {
            flowerOrder.put(
                    flower.getName(),
                    flowerOrder.containsKey(flower.getName()) // If flower exist in order list
                            ? flowerOrder.get(flower.getName()) + quantities // increase quantity
                            : quantities // else set quantity is 1
            );
            System.out.println("Add successfully");
        } else {
            System.out.println("Can't not find flowerID");
        }
    }

    public void viewOrderSorted() {
        System.out.println("#######################################################");
        System.out.printf("# %15s # %15s # %15s #\n", "       Order ID", "           Date", "       Buyer ID");

        System.out.println("#######################################################");
        System.out.format("# %15s # %15s # %15s #\n",
                orderId, date, buyerId);

        System.out.println("#######################################################");
        System.out.format("# %15s # %15s # %15s #\n",
                "    Flower Name", "       Quantity", "          Price");

        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> sortedMap = new LinkedList<Map.Entry<String, Integer>>(this.flowerOrder.entrySet());
        // Sort the list
        Collections.sort(sortedMap, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        if (!sortedMap.isEmpty()) {
            System.out.println("#######################################################");
        }
        for (Map.Entry<String, Integer> entry : sortedMap) {
            System.out.format("# %15s # %15s # %15f #\n",
                    entry.getKey(),
                    entry.getValue().toString(),
                    entry.getValue() * flowerManger.findFlowerByName(entry.getKey()).getUnitPrice()
            );
            this.totalPrice += entry.getValue() * flowerManger.findFlowerByName(entry.getKey()).getUnitPrice();
        }
        System.out.println("#######################################################");
        System.out.format(
                "# %51s #\n",
                "Total: $" + this.totalPrice.toString()
        );
        System.out.println("#######################################################");
        this.totalPrice = 0.0f; // reset total price
    }
}
