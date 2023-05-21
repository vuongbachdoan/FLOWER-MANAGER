/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class Flower {
    private String flowerId;
    private String name;
    private Float unitPrice;
    private String importDate;

    public Flower() {
    }

    public Flower(String flowerId, String name, Float unitPrice, String importDate) {
        this.flowerId = flowerId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.importDate = importDate;
    }

    public String getFlowerId() {
        return flowerId;
    }

    public String getName() {
        return name;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setFlowerId(String flowerId) {
        this.flowerId = flowerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }
    
    
}
