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
import models.Staff;

/**
 *
 * @author user
 */
public class StaffManager extends AccountManager {

    private ArrayList<Staff> staffs = new ArrayList<>(); // to storage list staffs
    private Utilities utils = new Utilities(); // to access utilities methods
    private Scanner scanner = new Scanner(System.in); // to access input functions
    private String currentAccountId; // to storage currnet id logged in

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }

    /**
     * Constructor to get staff information from database and save to @staffs
     */
    public StaffManager(String currentAccountId) {
        this.currentAccountId = currentAccountId;
        ArrayList<String> staffFromFile = utils.readFileData("src/data/staffs.dat");
        for (String staffString : staffFromFile) {
            staffs.add(new Staff(
                    staffString.split(",")[0], // staff id
                    staffString.split(",")[1], // staff name
                    staffString.split(",")[2], // staff address
                    Long.parseLong(staffString.split(",")[3]) // staff phone
            ));
        }
    }

    /**
     * Function to find staff by id
     *
     * @param staffId id of staff
     * @return
     */
    public Staff findStaff(String staffId) {
        for (Staff staff : this.staffs) {
            if (staff.getStaffId().equals(staffId)) {
                return staff; // if found staff match id, return Staff objecy
            }
        }
        return null; // if not found, return null
    }

    /**
     * Function to save updated staffs
     */
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/staffs.dat"));
            // read staff data from @staff and save to database
            for (Staff staff : this.staffs) {
                writer.write(staff.getStaffId() + "," + staff.getName() + "," + staff.getAddress() + "," + String.valueOf(staff.getPhone()));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to modify the account information by id
     */
    public void modifyAccount() {
        String inputCheck;
        System.out.print("Enter name: ");
        String name = scanner.nextLine(); // new account name
        name = name.trim();
        System.out.print("Enter address: ");
        String address = scanner.nextLine(); // new account address
        address = address.trim();
        do {
            System.out.print("Enter Phone number: ");
            inputCheck = scanner.nextLine();
        } while (utils.validateNumber(inputCheck));
        Long phoneNumber = Long.parseLong(inputCheck); // new phone number
        for (Staff staff : this.staffs) {
            String staffId = super.searchById(currentAccountId).getCustomerId(); // search staff object to update
            // update information
            if (staff.getStaffId().equals(staffId)) {
                staff.setName(name);
                staff.setAddress(address);
                staff.setPhone(phoneNumber);
            }
        }
        System.out.println("Successfully update account"); // notify updated 
    }
}
