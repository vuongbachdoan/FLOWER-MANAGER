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

    private Scanner scanner = new Scanner(System.in); // to access input handler functions
    private Utilities utils = new Utilities(); // to access custom utilities 
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd"); // to format date input of user
    private ArrayList<Flower> flowers = new ArrayList<>(); // to storage the flowers

    /**
     * Constructor to get flowers data from the database and save in @flowers
     */
    public FlowerManager() {
        ArrayList<String> flowersFromFile = utils.readFileData("src/data/flowers.dat");
        // get data from file and save in @flowers
        for (String flowerString : flowersFromFile) {
            flowers.add(new Flower(
                    flowerString.split(",")[0], // flower id
                    flowerString.split(",")[1], // flower name
                    Float.parseFloat(flowerString.split(",")[2]), // unit price
                    flowerString.split(",")[3] // date
            ));
        }
    }

    public ArrayList<Flower> getFlowers() {
        return flowers;
    }

    /**
     * Function to find flower by id
     *
     * @param flowerId
     * @return
     */
    public Flower findFlower(String flowerId) {
        for (Flower flower : flowers) {
            if (flower.getFlowerId().equals(flowerId)) {
                return flower; // if flower match id, return Flower object
            }
        }
        return null; // if not found, return null
    }

    /**
     * Function to find flower by name
     *
     * @param flowerName
     * @return
     */
    public Flower findFlowerByName(String flowerName) {
        for (Flower flower : flowers) {
            if (flower.getName().equals(flowerName)) {
                return flower; // if flower name match, return Flower object
            }
        }
        return null; // if flower not found, return null
    }

    /**
     * Function to add flower to list @flowers
     */
    public void addFlower() {
        String inputCheck;
        String flowerId = "F" + String.format("%03d", this.flowers.size()); // auto increment id 
        System.out.print("Enter flower name: ");
        String flowerName = scanner.nextLine(); // flower name
        flowerName = flowerName.trim();
        do {
            System.out.print("Enter price: ");
            inputCheck = scanner.nextLine();
        } while (utils.validateNumber(inputCheck));
        Float price = Float.parseFloat(inputCheck); // price of the flower
        Date date = new Date();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormater.format(date); // date import flower is auto generated

        this.flowers.add(new Flower(flowerId, flowerName, price, formattedDate)); // add flower to list
        System.out.println("Successfullly add flower");
    }

    /**
     * Function to modify flower
     */
    public void modifyFlower() {
        String inputCheck;
        do {
            System.out.print("Enter flowerId FXXX: ");
            inputCheck = scanner.nextLine(); // id of flower to modify
            inputCheck.trim();
        } while (!utils.validateId("FXXX", inputCheck));
        String flowerId = inputCheck;

        System.out.print("Enter name: ");
        String flowerName = scanner.nextLine(); // new name
        flowerName = flowerName.trim();

        do {
            System.out.print("Enter price: ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateFloat(inputCheck));
        Float flowerPrice = Float.parseFloat(inputCheck); // new price

        do {
            System.out.print("Enter date: ");
            inputCheck = scanner.nextLine(); // new date
            inputCheck = inputCheck.trim();
        } while (!utils.validateDate(inputCheck));
        String date = inputCheck;

        for (Flower flower : flowers) {
            // update flower information
            if (flower.getFlowerId().equals(flowerId)) {
                flower.setName(flowerName);
                flower.setUnitPrice(flowerPrice);
                flower.setImportDate(date);
            }
        }
        System.out.println("Successfully modify the flower"); // notify updated flower information
    }

    /**
     * Function to print information of flower
     */
    public void display() {
        String[] headers = {"              ID", "             Name", "            Price", "            Date"};
        System.out.println("################################################################################");
        System.out.format("# %16s # %17s # %18s # %16s #\n",
                headers[0], headers[1], headers[2], headers[3]);
        System.out.println("################################################################################");
        for (Flower flower : flowers) {
            System.out.format("# %16s # %17s # %18s # %16s #\n",
                    flower.getFlowerId(), flower.getName(), flower.getUnitPrice(), flower.getImportDate());
        };
        System.out.println("################################################################################");
    }

    /**
     * Function to remove flower by id
     */
    public void removeFlower() {
        String inputCheck;
        do {
            System.out.print("Enter flowerId FXXX: ");
            inputCheck = scanner.nextLine();
        } while (utils.validateId("FXXX", inputCheck));
        String flowerId = inputCheck; // id of flower to remove
        for (Iterator<Flower> iterator = this.flowers.iterator(); iterator.hasNext();) {
            Flower flower = iterator.next();
            if (flower.getFlowerId().equals(flowerId)) { // if found flower match id, remove it
                iterator.remove();
            }
        }
        System.out.println("Successfully remove the flower"); // notify user delete successfully
    }

    /**
     * Save updated flower
     */
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/flowers.dat"));
            // get flower information and save to database
            for (Flower flower : this.flowers) {
                writer.write(flower.getFlowerId() + "," + flower.getName() + "," + String.valueOf(flower.getUnitPrice()) + "," + flower.getImportDate());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
