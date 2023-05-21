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

    private Scanner scanner = new Scanner(System.in); // to access input handler
    private Utilities utils = new Utilities(); // to access custom utilities
    private ArrayList<Account> accounts = new ArrayList<>(); // to storage accounts information

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    /**
     * Constructor get data of accounts in database when init
     */
    public AccountManager() {
        ArrayList<String> accountsFromFile = utils.readFileData("src/data/accounts.dat");
        // get data in fole and add to accounts
        for (String accountString : accountsFromFile) {
            accounts.add(new Account(
                    accountString.split(",")[0],
                    accountString.split(",")[1],
                    accountString.split(",")[2],
                    accountString.split(",")[3]
            ));
        }
    }

    /**
     * Function to search account by id
     * @param accountId is id of account
     * @return
     */
    public Account searchById(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) { // if id match, return Account object
                return account;
            }
        }
        return null; // account not found
    }
    
    /**
     * Function to save updated information of account
     */
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/accounts.dat"));
            for (Account account : this.accounts) { // get account information to save in database
                writer.write(account.getAccountId() + "," + account.getPassword() + "," + account.getRole() + "," + account.getCustomerId());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
