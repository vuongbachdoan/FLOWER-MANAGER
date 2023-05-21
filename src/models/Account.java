/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author user
 */
public class Account {
    private String accountId;
    private String password;
    private String role;
    private String customerId;

    public Account() {
    }

    public Account(String accountId, String password, String role, String customerId) {
        this.accountId = accountId;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Account{" + "accountId=" + accountId + ", password=" + password + ", role=" + role + ", customerId=" + customerId + '}';
    }
}
