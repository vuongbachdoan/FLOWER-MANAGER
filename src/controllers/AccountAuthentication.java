/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.Account;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class AccountAuthentication {
    private AccountManager accountManager = new AccountManager();
    private String id;
    private String password;
    private Account account;

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
    
    public boolean checkLogin () {
        this.account = accountManager.searchById(this.id);
        if(this.account != null && this.account.getPassword().equals(password)) {
            return true;
        } else {
            this.account = null;
            return false;
        }
    }
}
