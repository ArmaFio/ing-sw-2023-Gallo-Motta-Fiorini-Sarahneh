package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainServerTest {
    static String PASSWORDS_PATH = "./MyShelfie/src/test/java/it/polimi/ingsw/server/AccountsTest.ser";

    private HashMap<String, String> loadPasswords(HashMap<String, String> passwords) {
        HashMap<String, String> usersPassword = new HashMap<>(0);

        try {
            usersPassword = passwords;
            if (usersPassword.size() > 0) {
                Logger.debug("Trovato file password contenente:");
                for (String key : usersPassword.keySet()) {
                    Logger.debug(key + " " + usersPassword.get(key));
                }
            } else {
                Logger.debug("Creating password file!");
            }
        } catch (RuntimeException e) {
            Logger.error("An error occurred! " + e);
        }

        return usersPassword;
    }


    @Test
    void loadPasswordsTest() throws IOException, InterruptedException {

        HashMap<String, String> usersPasswordIni = new HashMap<>();
        usersPasswordIni.put("Prova1", "prova1");
        usersPasswordIni.put("Prova2", "prova2");
        usersPasswordIni.put("Prova3", "prova3");

        HashMap<String, String> usersPasswordFin = this.loadPasswords(usersPasswordIni);

        for (String key : usersPasswordIni.keySet()) {
            assertTrue(usersPasswordIni.containsKey(key));
            assertEquals(usersPasswordIni.get(key), usersPasswordFin.get(key));
        }
    }

    @Test
    void sendAll() {
    }

    @Test
    void sendToLobby() {
    }

    @Test
    void getLobby() {
    }

    @Test
    void getUser() {
    }
}