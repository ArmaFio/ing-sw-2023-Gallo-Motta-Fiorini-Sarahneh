package it.polimi.ingsw;

import it.polimi.ingsw.client.NetworkHandler;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ClientApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        boolean ok = false;
        System.out.println("Welcome to MyShelfie!\n[0]CLI\n[1]GUI");
        while (!ok){
            try {
                choice = sc.nextInt();
                if(choice == 0 || choice == 1){
                    ok = true;
                }else {
                    System.out.println("Invalid option!");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid option!");
                sc.nextLine();
            }
        }
        if(choice == 0) {
            try {
                new NetworkHandler(0);
            } catch (IOException e) {
                System.out.println("Errore creazione URL");
            }

        } else{
            try {
                new NetworkHandler(1);
            } catch (IOException e) {
                System.out.println("Unable to start network handler foc client GUI");
            }
            //Application.launch(ViewGUI.class, args);

        }
    }
}
