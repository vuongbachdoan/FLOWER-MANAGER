/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.FlowerManager;
import controllers.OrderManager;
import controllers.Utilities;
import java.util.Scanner;

/**
 *
 * @author Nguyen Thi Thuy Dung
 */
public class DevMenu extends CommonMenu {
    private String idCurrentUser;

    public DevMenu(String idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }
    
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        FlowerManager flowerManager = new FlowerManager();
        OrderManager orderManager = new OrderManager(idCurrentUser);
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
                    break;
                case 2:
                    flowerManager.display();
                    break;
                case 3:
                    orderManager.addFlower();
                    break;
                case 6:
                    orderManager.viewOrderSorted();
                    break;
            }
        } while (choice != 8);
    }
}
