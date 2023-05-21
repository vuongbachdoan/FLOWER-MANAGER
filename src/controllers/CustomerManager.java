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
public class CustomerManager extends AccountManager{

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Customer> customers = new ArrayList<>();
    private Utilities utils = new Utilities();
    private String currentAccountId;

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public CustomerManager(String currentAccountId) {
        this.currentAccountId = currentAccountId;
        ArrayList<String> customerFromFile = utils.readFileData("src/data/customers.dat");
        for (String customerString : customerFromFile) {
            customers.add(new Customer(
                    customerString.split(",")[0],
                    customerString.split(",")[1],
                    customerString.split(",")[2],
                    Long.parseLong(customerString.split(",")[3])
            ));
        }
    }

    public Customer findCustomer(String customerId) {
        for (Customer customer : this.customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/customers.dat"));
            for (Customer customer : this.customers) {
                writer.write(customer.getCustomerId() + "," + customer.getName() + "," + customer.getAddress() + "," + String.valueOf(customer.getPhone()));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void modifyAccount() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Phone number: ");
        Long phoneNumber = scanner.nextLong();
        scanner.nextLine();
        for (Customer customer : this.customers) {
            String customerId = super.searchById(currentAccountId).getCustomerId();
            if(customer.getCustomerId().equals(customerId)) {
                customer.setName(name);
                customer.setAddress(address);
                customer.setPhone(phoneNumber);
            }
        }
        System.out.println("Successfully update account");
    }
}
