package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.LobbyList;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.ResponseType;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utils.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputHandler extends Thread{
    private final ClientView view;
    private ArrayList<Character> buffer;

    public InputHandler(ClientView view){
        this.view = view;
        start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while(true) {
            input = scanner.nextLine();
            switch (view.getGameState()) {
                case CREATE_JOIN -> {
                    Message response;
                    try {
                        if (input.equals("0")) {
                            response = new Message(ResponseType.CREATE);
                            view.write(response);
                        }else{
                            if (input.equals("1")) {
                                response = new Message(ResponseType.JOIN);
                                view.write(response);
                            } else {
                                Logger.error("Not an option");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case LOBBY_CHOICE -> {
                    switch (input){
                        case "/back" -> view.updateState(GameState.CREATE_JOIN);
                        case "/update" -> Logger.info("Qualcosa");
                        default -> {
                            if(view.lobbiesData.length > Integer.parseInt(input)) {
                                Message response = new Message(view.lobbiesData[Integer.parseInt(input)].id);
                                response.setType(ResponseType.JOIN_LOBBY);
                                try {
                                    view.write(response); //TODO cambia
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Logger.info("Invalid lobby choice");
                                view.updateState(GameState.LOBBY_CHOICE);
                            }
                        }
                    }
                    if (input.equals("/back")) {
                        view.updateState(GameState.CREATE_JOIN);
                    } else {

                    }
                }
                case INSIDE_LOBBY -> {
                    switch (input){
                        case "/start" -> {
                            Message response = new Message(ResponseType.START);
                            try {
                                view.write(response);
                            }catch (IOException e){
                                throw new RuntimeException(e);
                            }
                        }
                        case "/chat" -> {

                        }
                    }
                }
            }
        }
    }

    private String getBufferAsString() {
        return buffer.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
