package net.hmsvr.chatroom.client;

import net.hmsvr.chatroom.client.event.EventDispatcher;
import net.hmsvr.chatroom.client.event.ExitEvent;
import net.hmsvr.chatroom.client.event.InputEvent;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A thread that listens for input from the standard input stream ({@link System#in}) and
 * forwards them to the {@link EventDispatcher} as a new {@link InputEvent}.
 * When the client disconnects, an {@link ExitEvent} will be dispatched instead.
 */
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
            scanner.close();
            dispatcher.add(new ExitEvent(client));
        });
    }
}
