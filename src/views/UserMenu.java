/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.AccountManager;
import controllers.CustomerManager;
import controllers.FlowerManager;
import controllers.OrderManager;
import controllers.Utilities;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class UserMenu extends CommonMenu {

    private String idCurrnetUser;

    public UserMenu(String idCurrnetUser) {
        this.idCurrnetUser = idCurrnetUser;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        FlowerManager flowerManager = new FlowerManager();
        AccountManager accountManager = new AccountManager();
        OrderManager orderManager = new OrderManager(idCurrnetUser, accountManager);
        CustomerManager customerManager = new CustomerManager(idCurrnetUser);
        Utilities utils = new Utilities();
        String inputChoice;
        int choice;
        do {
            System.out.println("USER MENU:");
            System.out.println("1-Update Profile");
            System.out.println("2-View Flower List");
            System.out.println("3-Add Flower to Cart");
            System.out.println("4-View Order");
            System.out.println("5-Cancel Order");
            System.out.println("6-Quit");
            do {
                System.out.print("Enter your choice:");
                inputChoice = scanner.nextLine();
            } while (!utils.validateNumber(inputChoice)
                    || (Integer.parseInt(inputChoice) < 1
                    && Integer.parseInt(inputChoice) > 6));
            choice = Integer.parseInt(inputChoice);

            switch (choice) {
                case 1:
                    customerManager.modifyAccount();
                    break;
                case 2:
                    flowerManager.display();
                    break;
                case 3:
                    orderManager.addFlower(orderManager);
                    break;
                case 4:
                    orderManager.viewOrderSorted();
                    break;
                case 5:
                    orderManager = new OrderManager(idCurrnetUser, accountManager);
                    break;
                case 6:
                    do {
                        System.out.println("Do you want to dave save[1/0-Y/N-T/F]: ");
                        inputChoice = scanner.nextLine();
                    } while (!inputChoice.matches("^[10YNTF]{1}$"));
                    String isSave = inputChoice;
                    if ("1".equals(isSave) | "Y".equals(isSave) | "T".equals(isSave)) {
                        accountManager.save();
                        customerManager.save();
                        orderManager.save();
                        System.out.println("Successfully save data");
                    }
                    System.out.println("Successfully logout");
                    break;
            }
        } while (choice != 6);
    }
}
