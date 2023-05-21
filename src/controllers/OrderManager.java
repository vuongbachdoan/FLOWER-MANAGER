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
 * @author user
 */
public class OrderManager {

    private Scanner scanner = new Scanner(System.in); // to access input function
    private String idCurrnetUser; // to save current user logged in
    private AccountManager accountManager; // to access account manager functions
    private Utilities utils = new Utilities(); // to access utility functions
    private FlowerManager flowerManger = new FlowerManager(); // to access flower manager functions
    private ArrayList<Order> orders = new ArrayList<>(); // to storage ordered list
    private String orderId; // to storage current order id
    private String date; // to storage current order date
    private Float totalPrice = 0.0f; // to storage current order price
    private String buyerId; // to storage current buyer id
    private HashMap<String, Integer> flowerOrder = new HashMap<>(); // to storage current ordered flowers
    private Order currentOrder = new Order(); // to storage current order information

    /**
     * Constructor to get order information from the database and store it in
     * the @orders list
     *
     * @param idCurrnetUser // id of logged user
     * @param accountManager // to access account manager functions
     */
    public OrderManager(String idCurrnetUser, AccountManager accountManager) {
        this.idCurrnetUser = idCurrnetUser; // id of current user logged in
        this.accountManager = accountManager; // to access account manager functions

        // fetch orders from file
        ArrayList<String> ordersFromFile = utils.readFileData("src/data/orders.dat");
        for (String orderString : ordersFromFile) {
            this.orderId = orderString.split(",")[0]; // order id
            this.date = orderString.split(",")[1]; // date 
            this.totalPrice = Float.parseFloat(orderString.split(",")[2]); // total price
            this.buyerId = orderString.split(",")[3]; // buyer id

            // convert flower information from String to Flower object
            String[] flowersString = utils.extractOrderDetailsFromFile(orderString);
            for (String flowerString : flowersString) {
                String flowerId = flowerString.split(":")[0];
                int flowerQuantities = Integer.parseInt(flowerString.split(":")[1]);
                this.flowerOrder.put(
                        flowerId,
                        this.flowerOrder.containsKey(flowerId)
                                ? this.flowerOrder.get(flowerId) + flowerQuantities // if key exist, then + quantity
                                : flowerQuantities // otherwise, add quantity 
                );
            }
            orders.add(
                    new Order(this.orderId, this.date, this.buyerId, this.flowerOrder, this.totalPrice) // add order object to list of orders
            );
            this.flowerOrder = new HashMap<>(); // reset previous information for next Order object
        }

        // reset to save current order information
        currentOrder.setOrderId("O" + String.format("%03d", orders.size()));
        currentOrder.setBuyerId(this.idCurrnetUser);
        currentOrder.setDate(utils.convertDate(new Date()));
        currentOrder.setFlowers(new HashMap<String, Integer>());
        currentOrder.setTotalPrice(0.0f);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * Function to add Flower to cart
     *
     * @param orderManager access order manager instance
     */
    public void addFlower(OrderManager orderManager) {
        String inputCheck;
        do {
            System.out.print("Enter flower ID FXXX: ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateId("FXXX", inputCheck));
        String flowerId = inputCheck; // flower id

        do {
            System.out.print("Enter quantities: ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateNumber(inputCheck));
        int quantities = Integer.parseInt(inputCheck); // quantity to add

        // store the flower to HashMap
        Flower flower = flowerManger.findFlower(flowerId);
        if (flower != null) { // check if Flower id exist
            HashMap<String, Integer> prevFlowersOrder = currentOrder.getFlowers();
            prevFlowersOrder.put(flowerId,
                    prevFlowersOrder.containsKey(flowerId) // check flower id exist
                            ? prevFlowersOrder.get(flowerId) + quantities // if true, add with previous quantities
                            : quantities // if false, set is quantities
            );

            currentOrder.setFlowers(prevFlowersOrder); // update current
            // count total price of flower
            for (int flowerQuantity : currentOrder.getFlowers().values()) {
                currentOrder.setTotalPrice(flowerQuantity * flowerManger.findFlower(flowerId).getUnitPrice());
            }
            System.out.println("Successfully add to cart"); // notify add success
        } else {
            System.out.println("Can't not find flowerID"); // notify add fail
        }
    }

    /**
     * Function to view sorted order of flower
     */
    public void viewOrderSorted() {
        System.out.println("#######################################################");
        System.out.printf("# %15s # %15s # %15s #\n", "       Order ID", "           Date", "       Buyer ID");
        System.out.println("#######################################################");
        System.out.format("# %15s # %15s # %15s #\n", orderId, date, buyerId);
        System.out.println("#######################################################");
        System.out.format("# %15s # %15s # %15s #\n", "    Flower Name", "       Quantity", "          Price");
        List<Map.Entry<String, Integer>> sortedMap = new LinkedList<Map.Entry<String, Integer>>(this.currentOrder.getFlowers().entrySet()); // to storage sorted flowers

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

    /**
     * Function to view sorted order in dev role, with filter
     *
     * @throws ParseException
     */
    public void viewSortedOrderDev() throws ParseException {
        String inputCheck;
        // get sort options
        do {
            System.out.println("Enter sort term(count, date, or price): ");
            inputCheck = scanner.nextLine();
            inputCheck = inputCheck.trim();
        } while (!inputCheck.equals("count") && !inputCheck.equals("date") && !inputCheck.equals("price"));
        String sortTerm = inputCheck;

        do {
            System.out.println("Enter type(asc, desc): ");
            inputCheck = scanner.nextLine();
            inputCheck = inputCheck.trim();
        } while (!inputCheck.equals("asc") && !inputCheck.equals("desc"));
        String sortType = inputCheck;

        do {
            System.out.println("Enter fromDate(You can ignore by just enter): ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateDate(inputCheck));
        String fromDate = inputCheck;

        do {
            System.out.println("Enter toDate(You can ignore by just enter): ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateDate(inputCheck) && !utils.validateTwoDate(fromDate, inputCheck));
        String toDate = inputCheck;

        // Print out sorted order
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

    /**
     * Remove order by id
     */
    public void removeOrder() {
        String inputCheck;
        do {
            System.out.print("Enter orderId OXXX: ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateId("OXXX", inputCheck));
        String orderId = inputCheck; // order id to remove
        for (Iterator<Order> iterator = this.orders.iterator(); iterator.hasNext();) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) { // of order have if matches, remove it
                iterator.remove();
            }
        }
        System.out.println("Successfully remove the order"); // notify the user remove successfully
    }

    /**
     * Function to save updated orders
     */
    public void save() {
        try {
            if (this.currentOrder.getTotalPrice() != 0.0f) { // check if user ordered
                this.orders.add(currentOrder); // add current order to previous orders
            }

            // read data from @orders and save to database
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
