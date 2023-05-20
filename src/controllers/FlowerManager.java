/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import models.Flower;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class FlowerManager {

    private Scanner scanner = new Scanner(System.in);
    private Utilities utils = new Utilities();
    private ArrayList<Flower> flowers = new ArrayList<>();

    public FlowerManager() {
        ArrayList<String> flowersFromFile = utils.readFileData("src/data/flowers.dat");
        for (String flowerString : flowersFromFile) {
            flowers.add(new Flower(
                    flowerString.split(",")[0],
                    flowerString.split(",")[1],
                    Float.parseFloat(flowerString.split(",")[2]),
                    flowerString.split(",")[3]
            ));
        }
    }
    
    public Flower findFlower(String flowerId) {
        for(Flower flower: flowers) {
            if(flower.getFlowerId().equals(flowerId)) {
                return flower;
            }
        }
        return null;
    }
    
    public Flower findFlowerByName(String flowerName) {
        for(Flower flower: flowers) {
            if(flower.getName().equals(flowerName)) {
                return flower;
            }
        }
        return null;
    }

    public void display() {
        String[] headers = {"ID", "Name", "Price", "Date"};
        System.out.println("|----------------|----------------|----------------|----------------|");
        System.out.format("| %-14s | %-14s | %-14s | %-14s |\n",
                headers[0], headers[1], headers[2], headers[3]);
        System.out.println("|----------------|----------------|----------------|----------------|");
        for (Flower flower : flowers) {
            System.out.format("| %-14s | %-14s | %-14f | %-14s |\n",
                    flower.getFlowerId(), flower.getName(), flower.getUnitPrice(), flower.getImportDate());
        };
        System.out.println("|----------------|----------------|----------------|----------------|");
    }
}
