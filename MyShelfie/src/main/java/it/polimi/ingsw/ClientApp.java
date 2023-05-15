package it.polimi.ingsw;

import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.javafx.ViewGUI;
import javafx.application.Application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ClientApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        boolean ok = false;
        System.out.println("Welocme to MyShelfie!\n[0]CLI\n[1]GUI");
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
            Application.launch(ViewGUI.class, args);

        }
    }
}
