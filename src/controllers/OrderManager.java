/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import models.Flower;
import models.Order;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class OrderManager{
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

            String[] flowersString = utils.extractOrderDetailsFromFile(orderString);
            for (String flowerString : flowersString) {
                String flowerId = flowerString.split(":")[0];
                int flowerQuantities = Integer.parseInt(flowerString.split(":")[1]);
                flowerOrder.put(
                        flowerId,
                        flowerQuantities
                );
            }
            orders.add(
                    new Order(this.orderId, this.date, this.buyerId, this.flowerOrder, this.totalPrice)
            );
        }

        this.orderId = "O" + String.format("%03d", orders.size());
        this.buyerId = this.idCurrnetUser;
        this.date = utils.convertDate(new Date());
    }

    public void addFlower() {
        scanner.nextLine();
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
        } else {
            System.out.println("Can't not find flowerID");
        }
    }

    public void viewOrderSorted() {
        System.out.println("#######################################################");
        System.out.printf("# %-15s %15s %15s #\n", "Order ID", "Date", "Buyer ID");

        System.out.println("#######################################################");
        System.out.format("# %-15s # %-15s # %-15s #\n",
                orderId, date, buyerId);

        System.out.println("#######################################################");
        System.out.format("# %-15s # %-15s # %-15s #\n",
                "    Flower Name", "       Quantity", "          Price");

        Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return flowerOrder.get(o1).compareTo(flowerOrder.get(o2));
            }
        });
        
        if(sortedMap.size() != 0) {
            System.out.println("#######################################################");
        }
        for (Map.Entry<String, Integer> flower : sortedMap.entrySet()) {
            System.out.format("# %-15s # %-15s # %-15s #\n",
                    flower.getKey(),
                    flower.getValue(),
                    flower.getValue() * flowerManger.findFlowerByName(flower.getKey()).getUnitPrice()
            );
            totalPrice += flower.getValue() * flowerManger.findFlowerByName(flower.getKey()).getUnitPrice();
        }
        System.out.println("#######################################################");
        System.out.format(
                "# %-51s #\n",
                "Total: $" + totalPrice.toString()
        );
        System.out.println("#######################################################");
    }
}
