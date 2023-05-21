/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import models.Flower;
import models.Order;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class OrderManager {
    private Scanner scanner = new Scanner(System.in);
    private String idCurrnetUser;
    private AccountManager accountManager;
    private Utilities utils = new Utilities();
    private FlowerManager flowerManger = new FlowerManager();
    private ArrayList<Order> orders = new ArrayList<>();
    private String orderId;
    private String date;
    private Float totalPrice = 0.0f;
    private String buyerId;
    private HashMap<String, Integer> flowerOrder = new HashMap<>();
    private Order currentOrder = new Order();

    public OrderManager(String idCurrnetUser, AccountManager accountManager) {
        this.idCurrnetUser = idCurrnetUser;
        this.accountManager = accountManager;

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
                this.flowerOrder.put(
                        flowerId,
                        this.flowerOrder.containsKey(flowerId)
                                ? this.flowerOrder.get(flowerId) + flowerQuantities
                                : 1
                );
            }
            orders.add(
                    new Order(this.orderId, this.date, this.buyerId, this.flowerOrder, this.totalPrice)
            );
            this.flowerOrder = new HashMap<>();
        }

        currentOrder.setOrderId("O" + String.format("%03d", orders.size()));
        currentOrder.setBuyerId(this.idCurrnetUser);
        currentOrder.setDate(utils.convertDate(new Date()));
        currentOrder.setFlowers(new HashMap<String, Integer>());
        currentOrder.setTotalPrice(0.0f);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addFlower(OrderManager orderManager) {
        System.out.print("Enter flower ID FXXX: ");
        String flowerId = scanner.nextLine();
        System.out.print("Enter quantities: ");
        int quantities = scanner.nextInt();
        scanner.nextLine();

        Flower flower = flowerManger.findFlower(flowerId);
        if (flower != null) { // check if Flower id exist
            HashMap<String, Integer> prevFlowersOrder = currentOrder.getFlowers();
            prevFlowersOrder.put(flowerId,
                    prevFlowersOrder.containsKey(flowerId)
                            ? prevFlowersOrder.get(flowerId) + quantities
                            : quantities
            );
            
            currentOrder.setFlowers(prevFlowersOrder);
            for (int flowerQuantity : currentOrder.getFlowers().values()) {
                currentOrder.setTotalPrice(flowerQuantity * flowerManger.findFlower(flowerId).getUnitPrice());
            }
            System.out.println("Successfully add to cart");
        } else {
            System.out.println("Can't not find flowerID");
        }
    }

    public void viewOrderSorted() {
        System.out.println("#######################################################");
        System.out.printf("# %15s # %15s # %15s #\n", "       Order ID", "           Date", "       Buyer ID");
        System.out.println("#######################################################");
        System.out.format("# %15s # %15s # %15s #\n", orderId, date, buyerId);
        System.out.println("#######################################################");
        System.out.format("# %15s # %15s # %15s #\n", "    Flower Name", "       Quantity", "          Price");
        List<Map.Entry<String, Integer>> sortedMap = new LinkedList<Map.Entry<String, Integer>>(this.currentOrder.getFlowers().entrySet());

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
                    entry.getValue() * flowerManger.findFlower(entry.getKey()).getUnitPrice()
            );
            this.totalPrice += entry.getValue() * flowerManger.findFlower(entry.getKey()).getUnitPrice();
        }
        System.out.println("#######################################################");
        System.out.format(
                "# %51s #\n",
                "Total: $" + String.valueOf(this.currentOrder.getTotalPrice())
        );
        System.out.println("#######################################################");
        this.totalPrice = 0.0f; // reset total price
    }

    public void viewSortedOrderDev() throws ParseException {
        System.out.println("Enter sort term(count, date, or price): ");
        String sortTerm = scanner.nextLine();
        System.out.println("Enter type(asc, desc): ");
        String sortType = scanner.nextLine();
        System.out.println("Enter fromDate(You can ignore by just enter): ");
        String fromDate = scanner.nextLine();
        System.out.println("Enter toDate(You can ignore by just enter): ");
        String toDate = scanner.nextLine();

        System.out.println("################################################################################################################");
        System.out.printf("# %15s # %15s # %15s # %18s # %15s # %15s #\n",
                "            No.", "       Order ID", "     Order Date", "          Customer", "   Flower Count", "    Order Total");
        System.out.println("################################################################################################################");
        ArrayList<Order> sortedOrder = new ArrayList<>();
        sortedOrder.addAll(this.orders);

        // Sort the list
        utils.sortOrderByTypeInput(orders, sortTerm, sortType);
        int index = 0;
        for (Order order : orders) {
            index += utils.displayOrderFilteredbyDate(order, fromDate, toDate, index, this.idCurrnetUser);
        }
        System.out.println("################################################################################################################");
    }

    public void removeOrder() {
        System.out.print("Enter orderId OXXX: ");
        String orderId = scanner.nextLine();
        for (Iterator<Order> iterator = this.orders.iterator(); iterator.hasNext();) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                iterator.remove();
            }
        }
        System.out.println("Successfully remove the order");
    }

    public void save() {
        try {
            this.orders.add(currentOrder);

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/orders.dat"));
            for (Order order : this.orders) {
                HashMap<String, Integer> flowerOrder = order.getFlowers();
                int index = 0;
                String joinedString = "";
                for (Map.Entry<String, Integer> entry : flowerOrder.entrySet()) {
                    String flowerKey = entry.getKey();
                    int flowerQuantity = entry.getValue();
                    joinedString += flowerKey + ":" + flowerQuantity + ":" + String.valueOf(flowerManger.findFlower(flowerKey).getUnitPrice() * flowerQuantity);
                    if (index != flowerOrder.size() - 1) {
                        joinedString += ",";
                    }
                    index++;
                }
                writer.write(order.getOrderId() + "," + order.getDate() + "," + String.valueOf(order.getTotalPrice()) + "," + this.accountManager.searchById(idCurrnetUser).getCustomerId() + "," + joinedString);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
