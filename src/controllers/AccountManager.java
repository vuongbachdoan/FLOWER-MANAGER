/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import models.Account;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class AccountManager {

    private Utilities utils = new Utilities();
    private ArrayList<Account> accounts = new ArrayList<>();

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
}
