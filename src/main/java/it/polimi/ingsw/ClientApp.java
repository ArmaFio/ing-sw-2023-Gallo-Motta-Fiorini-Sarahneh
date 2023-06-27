package it.polimi.ingsw;

import it.polimi.ingsw.client.RMI_NetworkHandler;
import it.polimi.ingsw.client.SocketNetworkHandler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientApp {
    public static void main(String[] args) throws UnknownHostException {
        Scanner sc = new Scanner(System.in);
        int choice1 = 0;
        String IPV4_REGEX =
                "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        int choice2 = 0;
        boolean ok = false;
        Pattern ipv4 = Pattern.compile(IPV4_REGEX);
        Matcher matcher;
        String ip;
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
        sc.nextLine();
        ok = false;
        do {
            System.out.println("Digit the server ip (press Enter for localhost)");
            ip = sc.nextLine();
            if (ip.equals("")) {
                ip = "127.0.0.1";
            }
            matcher = ipv4.matcher(ip);
            if (!matcher.matches())
                System.out.println("Not a valid ip address, try again");
        } while (!matcher.matches());
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
                new SocketNetworkHandler(choice2, ip);
            } catch (IOException e) {
                System.out.println("Unable to start socket NetworkHandler");
            }

        } else {
            try {
                new RMI_NetworkHandler(choice2, ip);
            } catch (IOException | NotBoundException e) {
                System.out.println("Unable to start RMI network handler" + e);
                e.printStackTrace();
            }
            //Application.launch(ViewGUI.class, args);

        }
    }
}
