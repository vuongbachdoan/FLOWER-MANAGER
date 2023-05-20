/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.AccountAuthentication;
import java.util.Scanner;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class CommonMenu {
    
    public void menu () {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter id AXXX: ");
        String id = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        // Login function here
        AccountAuthentication authCredential = new AccountAuthentication(id, password);
        if(authCredential.checkLogin()) { // replace true by check login function
            System.out.println("Successfully login.");
            String role = authCredential.getAccount().getRole();
            if(role.equals("DEV")) { // replace true by check Role is USER
                DevMenu devMenu = new DevMenu(id);
                devMenu.menu();
            } else {
                UserMenu userMenu = new UserMenu(id);
                userMenu.menu();
            }
        } else {
            System.out.println("Fail to login.");
        }
    }
}
