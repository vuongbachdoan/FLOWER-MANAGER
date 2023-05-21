/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import models.Customer;

/**
 *
 * @author user
 */
public class CustomerManager extends AccountManager {

    private Scanner scanner = new Scanner(System.in); // to access input handler function
    private ArrayList<Customer> customers = new ArrayList<>(); // to storage customers information
    private Utilities utils = new Utilities(); // to access custom utilities
    private String currentAccountId; // to storage current user logged in

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * Constructor that get information in database and save to @customers when init
     * 
     * @param currentAccountId current id of account after login
     */
    public CustomerManager(String currentAccountId) {
        this.currentAccountId = currentAccountId; // get current account after login
        ArrayList<String> customerFromFile = utils.readFileData("src/data/customers.dat");
        // read information from database and save to @customers
        for (String customerString : customerFromFile) {
            customers.add(new Customer(
                    customerString.split(",")[0],
                    customerString.split(",")[1],
                    customerString.split(",")[2],
                    Long.parseLong(customerString.split(",")[3])));
        }
    }

    /**
     * Function to search customer by id
     * @param customerId id of customer to search
     * @return
     */
    public Customer findCustomer(String customerId) {
        for (Customer customer : this.customers) {
            if (customer.getCustomerId().equals(customerId)) { 
                return customer; // if found customer, return Customer object
            }
        }
        return null; // if not found, return null
    }

    /**
     * Function to save updated customer information
     */
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/customers.dat"));
            // read customer information in @customers and save to database
            for (Customer customer : this.customers) {
                writer.write(customer.getCustomerId() + "," + customer.getName() + "," + customer.getAddress() + ","
                        + String.valueOf(customer.getPhone()));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to modify the customer information
     */
    public void modifyAccount() {
        String inputCheck;
        System.out.print("Enter name: ");
        String name = scanner.nextLine(); // name of customer to modify
        name = name.trim();
        System.out.print("Enter address: ");
        String address = scanner.nextLine(); // address of customer to modify
        address = address.trim();
        do {
            System.out.print("Enter Phone number: ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateNumber(inputCheck));
        Long phoneNumber = Long.parseLong(inputCheck); // phone number of customer to modify
        for (Customer customer : this.customers) { 
            String customerId = super.searchById(currentAccountId).getCustomerId(); // find Customer object match the id
            // update customer information
            if (customer.getCustomerId().equals(customerId)) {
                customer.setName(name);
                customer.setAddress(address);
                customer.setPhone(phoneNumber);
            }
        }
        System.out.println("Successfully update account"); // notify successfull update
    }
}
