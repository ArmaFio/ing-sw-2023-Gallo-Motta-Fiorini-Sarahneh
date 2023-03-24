package it.polimi.ingsw.model.utils;

import java.io.*;

public class LoadSave {
    /**
     * Writes the serializable object to the specified path
     *
     * @param filePath the path where the file will be saved
     * @param obj      the {@code Object} to save
     * @author Gallo Matteo
     */
    public static void write(String filePath, Object obj) {
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(obj);

            out.close();
            file.close();

            System.out.println("Object has been serialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Reads the serializable object from the specified path
     *
     * @param filePath the path where to find the file to read
     * @return the {@code Object} red
     * @author Gallo Matteo
     */
    public static Object read(String filePath) {
        Object obj;

        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            obj = in.readObject();

            System.out.println("Object has been deserialized ");

            in.close();
            file.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }
}
