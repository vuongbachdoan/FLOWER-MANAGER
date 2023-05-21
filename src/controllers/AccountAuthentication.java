/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.Account;

/**
 *
 * @author user
 */
public class AccountAuthentication {

    private String id; // id of account
    private String password; // password of account
    private Account account; // account object

    // inititial Authentication with id and password
    public AccountAuthentication(String id, String password) {
        this.id = id; 
        this.password = password;
    }

    AccountAuthentication() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return this.account;
    }

    /**
     * Function to check login, if user informantion not match
     * @return true if exist user informations
     * @return false if not exist user informations
     * @return
     */
    public boolean checkLogin() {
        AccountManager accountManager = new AccountManager(); // to access function of account manager
        this.account = accountManager.searchById(this.id); // get account save in database
        if (this.account != null && this.account.getPassword().equals(password)) { // check if account information match
            return true;
        } else { // account not found
            this.account = null;
            return false;
        }
    }
}
