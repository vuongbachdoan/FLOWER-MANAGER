/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.AccountAuthentication;
import controllers.Utilities;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class CommonMenu {

    private Utilities utils = new Utilities();

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        String inputCheck;
        do {
            System.out.print("Enter id AXXX: ");
            inputCheck = scanner.nextLine();
        } while (!utils.validateId("AXXX", inputCheck));
        String id = inputCheck;
        do {
            System.out.print("Enter password: ");
            inputCheck = scanner.nextLine();
        } while (inputCheck.isEmpty() || inputCheck.contains(" "));
        String password = inputCheck;

        // Login function here
        AccountAuthentication authCredential = new AccountAuthentication(id, password);
        if (authCredential.checkLogin()) { // replace true by check login function
            System.out.println("Successfully login.");
            String role = authCredential.getAccount().getRole();
            if (role.equals("DEV")) { // replace true by check Role is USER
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
