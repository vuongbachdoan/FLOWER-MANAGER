/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class Order {
    private String orderId;
    private String date;
    private String buyerId;
    private Float totalPrice;
    private HashMap<String, Integer> flowers = new HashMap<>();

    public Order() {
    }

    public Order(String orderId, String date, String buyerId, HashMap<String, Integer> flowers, Float totalPrice) {
        this.orderId = orderId;
        this.date = date;
        this.buyerId = buyerId;
        this.flowers = flowers;
        this.totalPrice = totalPrice;
    }
}
