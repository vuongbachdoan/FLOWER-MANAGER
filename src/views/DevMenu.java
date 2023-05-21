/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.AccountManager;
import controllers.FlowerManager;
import controllers.OrderManager;
import controllers.StaffManager;
import controllers.Utilities;
import java.text.ParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class DevMenu extends CommonMenu {

    private String idCurrentUser;

    public DevMenu(String idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        FlowerManager flowerManager = new FlowerManager();
        AccountManager accountManager = new AccountManager();
        OrderManager orderManager = new OrderManager(idCurrentUser, accountManager);
        StaffManager staffManager = new StaffManager(idCurrentUser);
        Utilities utils = new Utilities();
        String inputChoice;
        int choice;

        do {
            System.out.println("DEV MENU:");
            System.out.println("1-Update Profile");
            System.out.println("2-View Flower List");
            System.out.println("3-Add Flower");
            System.out.println("4-Modify Flower");
            System.out.println("5-Remove Flower");
            System.out.println("6-View Sorted Orders");
            System.out.println("7-Remove Order");
            System.out.println("8-Quit");

            do {
                System.out.print("Enter your choice:");
                inputChoice = scanner.nextLine();
            } while (!utils.validateNumber(inputChoice)
                    && (Integer.parseInt(inputChoice) < 1
                    || Integer.parseInt(inputChoice) > 8));
            choice = Integer.parseInt(inputChoice);

            switch (choice) {
                case 1:
                    staffManager.modifyAccount();
                    break;
                case 2:
                    flowerManager.display();
                    break;
                case 3:
                    flowerManager.addFlower();
                    break;
                case 4:
                    flowerManager.modifyFlower();
                    break;
                case 5:
                    flowerManager.removeFlower();
                    break;
                case 6: {
                    try {
                        orderManager.viewSortedOrderDev();
                    } catch (ParseException ex) {
                        Logger.getLogger(DevMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case 7: 
                    orderManager.removeOrder();
                    break;
                case 8:
                    System.out.println("Do you want to save save[1/0-Y/N-T/F]: ");
                    String isSave = scanner.nextLine();
                    if("1".equals(isSave) | "Y".equals(isSave) | "T".equals(isSave)) {
                        accountManager.save();
                        staffManager.save();
                        flowerManager.save();
                        orderManager.save();
                        System.out.println("Successfully save data");
                    }
                    System.out.println("Successfully logout");
                    break;
            }
        } while (choice != 8);
    }
}
