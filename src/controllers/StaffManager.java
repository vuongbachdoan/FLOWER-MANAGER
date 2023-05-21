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

    private ArrayList<Staff> staffs = new ArrayList<>();
    private Utilities utils = new Utilities();
    private Scanner scanner = new Scanner(System.in);
    private String currentAccountId;

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }

    public StaffManager(String currentAccountId) {
        this.currentAccountId = currentAccountId;
        ArrayList<String> staffFromFile = utils.readFileData("src/data/staffs.dat");
        for (String staffString : staffFromFile) {
            staffs.add(new Staff(
                    staffString.split(",")[0],
                    staffString.split(",")[1],
                    staffString.split(",")[2],
                    Long.parseLong(staffString.split(",")[3])
            ));
        }
    }

    public Staff findStaff(String staffId) {
        for (Staff staff : this.staffs) {
            if (staff.getStaffId().equals(staffId)) {
                return staff;
            }
        }
        return null;
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/staffs.dat"));
            for (Staff staff : this.staffs) {
                writer.write(staff.getStaffId() + "," + staff.getName() + "," + staff.getAddress() + "," + String.valueOf(staff.getPhone()));
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
        for (Staff staff : this.staffs) {
            String staffId = super.searchById(currentAccountId).getCustomerId();
            if (staff.getStaffId().equals(staffId)) {
                staff.setName(name);
                staff.setAddress(address);
                staff.setPhone(phoneNumber);
            }
        }
        System.out.println("Successfully update account");
    }
}
