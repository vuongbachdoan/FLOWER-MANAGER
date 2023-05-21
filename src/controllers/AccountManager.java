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
import models.Account;

/**
 *
 * @author user
 */
public class AccountManager {

    private Scanner scanner = new Scanner(System.in);
    private Utilities utils = new Utilities();
    private ArrayList<Account> accounts = new ArrayList<>();

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public AccountManager() {
        ArrayList<String> accountsFromFile = utils.readFileData("src/data/accounts.dat");
        for (String accountString : accountsFromFile) {
            accounts.add(new Account(
                    accountString.split(",")[0],
                    accountString.split(",")[1],
                    accountString.split(",")[2],
                    accountString.split(",")[3]
            ));
        }
    }

    public Account searchById(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }
    
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/accounts.dat"));
            for (Account account : this.accounts) {
                writer.write(account.getAccountId() + "," + account.getPassword() + "," + account.getRole() + "," + account.getCustomerId());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
