package it.polimi.ingsw;

import java.io.File;

public class App {
    public static void main(String[] args) {
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        System.out.println("Hello World!");
    }
}
