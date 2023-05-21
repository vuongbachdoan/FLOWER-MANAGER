/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import models.Flower;

/**
 *
 * @author user
 */
public class FlowerManager {

    private Scanner scanner = new Scanner(System.in);
    private Utilities utils = new Utilities();
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
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

    public ArrayList<Flower> getFlowers() {
        return flowers;
    }

    public Flower findFlower(String flowerId) {
        for (Flower flower : flowers) {
            if (flower.getFlowerId().equals(flowerId)) {
                return flower;
            }
        }
        return null;
    }

    public Flower findFlowerByName(String flowerName) {
        for (Flower flower : flowers) {
            if (flower.getName().equals(flowerName)) {
                return flower;
            }
        }
        return null;
    }

    public void addFlower() {
        String flowerId = "F" + String.format("%03d", this.flowers.size());
        System.out.print("Enter flower name: ");
        String flowerName = scanner.nextLine();
        System.out.print("Enter price: ");
        Float price = scanner.nextFloat();
        scanner.nextLine();
        Date date = new Date();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormater.format(date);

        this.flowers.add(new Flower(flowerId, flowerName, price, formattedDate));
    }

    public void modifyFlower() {
        System.out.print("Enter flowerId FXXX: ");
        String flowerId = scanner.nextLine();
        System.out.print("Enter name: ");
        String flowerName = scanner.nextLine();
        System.out.print("Enter price: ");
        Float flowerPrice = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Enter date: ");
        String date = scanner.nextLine();
        for (Flower flower : flowers) {
            if (flower.getFlowerId().equals(flowerId)) {
                flower.setName(flowerName);
                flower.setUnitPrice(flowerPrice);
                flower.setImportDate(date);
            }
        }
        System.out.println("Successfully modify the flower");
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

    public void removeFlower() {
        System.out.print("Enter flowerId FXXX: ");
        String flowerId = scanner.nextLine();
        for (Iterator<Flower> iterator = this.flowers.iterator(); iterator.hasNext();) {
            Flower flower = iterator.next();
            if (flower.getFlowerId().equals(flowerId)) {
                iterator.remove();
            }
        }
        System.out.println("Successfully remove the flower");
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/flowers.dat"));
            for (Flower flower : this.flowers) {
                writer.write(flower.getFlowerId() + "," + flower.getName() + "," + String.valueOf(flower.getUnitPrice()) + "," + flower.getImportDate());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(OrderManager orderManager) {
        orderManager.addFlower(orderManager);
    }
}
