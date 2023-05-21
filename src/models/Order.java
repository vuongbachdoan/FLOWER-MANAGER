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

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public HashMap<String, Integer> getFlowers() {
        return flowers;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFlowers(HashMap<String, Integer> flowers) {
        this.flowers = flowers;
    }
    
    public void addFlower(String flowerName, Integer quantities) {
        this.flowers.put(flowerName, quantities);
    }
}
