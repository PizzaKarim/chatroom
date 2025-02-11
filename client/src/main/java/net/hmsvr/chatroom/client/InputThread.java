package net.hmsvr.chatroom.client;

import net.hmsvr.chatroom.client.event.EventDispatcher;
import net.hmsvr.chatroom.client.event.ExitEvent;
import net.hmsvr.chatroom.client.event.InputEvent;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class InputThread extends Thread {

    public InputThread(@NotNull Client client, @NotNull EventDispatcher dispatcher) {
        super(() -> {
            Scanner scanner = new Scanner(System.in);
            while (client.isConnected()) {
                try {
                    String string = scanner.nextLine();
                    if (string.equalsIgnoreCase("exit")) break;
                    dispatcher.add(new InputEvent(client, string));
                } catch (NoSuchElementException e) { break; }
            }
            dispatcher.add(new ExitEvent(client));
        });
    }
}
