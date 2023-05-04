package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.utils.Logger;

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
                            response = new Message(MessageType.CREATE);
                            view.write(response);
                            //view.updateState(GameState.INSIDE_LOBBY);
                        } else {
                            if (input.equals("1")) {
                                response = new Message(MessageType.JOIN);
                                view.write(response);
                                //view.updateState(GameState.LOBBY_CHOICE);
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
                                response.setType(MessageType.JOIN_LOBBY);
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
                }
                case INSIDE_LOBBY -> {
                    switch (input){
                        case "/start" -> {
                            Message response = new Message(MessageType.START);
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
