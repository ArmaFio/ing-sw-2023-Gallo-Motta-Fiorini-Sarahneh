package it.polimi.ingsw;

import it.polimi.ingsw.client.RMI_NetworkHandler;
import it.polimi.ingsw.client.SocketNetworkHandler;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ClientApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice1 = 0;
        int choice2 = 0;
        boolean ok = false;
        System.out.println("Choose The Connection method you want to use\n[0]Socket\n[1]RMI");
        while (!ok) {
            try {
                choice1 = sc.nextInt();
                if (choice1 == 0 || choice1 == 1) {
                    ok = true;
                } else {
                    System.out.println("Invalid option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid option!");
                sc.nextLine();
            }
        }
        ok = false;
        System.out.println("Welcome to MyShelfie!\n[0]CLI\n[1]GUI");
        while (!ok) {
            try {
                choice2 = sc.nextInt();
                if (choice2 == 0 || choice2 == 1) {
                    ok = true;
                } else {
                    System.out.println("Invalid option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid option!");
                sc.nextLine();
            }
        }
        if (choice1 == 0) {
            try {
                new SocketNetworkHandler(choice2);
            } catch (IOException e) {
                System.out.println("Unable to start socket NetworkHandler");
            }

        } else {
            try {
                new RMI_NetworkHandler(choice2);
            } catch (IOException | NotBoundException e) {
                System.out.println("Unable to start RMI network handler" + e);
                e.printStackTrace();
            }
            //Application.launch(ViewGUI.class, args);

        }
    }
}
