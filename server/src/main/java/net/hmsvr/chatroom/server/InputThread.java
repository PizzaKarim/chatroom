package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.server.event.EventDispatcher;
import net.hmsvr.chatroom.server.event.ExitEvent;
import net.hmsvr.chatroom.server.event.InputEvent;
import net.hmsvr.chatroom.server.event.PacketEvent;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A thread that listens for input from the standard input stream ({@link System#in}) and
 * forwards them to the {@link EventDispatcher} as a new {@link InputEvent}.
 * When the server stops, an {@link ExitEvent} will be dispatched instead.
 */
public final class InputThread extends Thread {

    public InputThread(@NotNull Server server, @NotNull EventDispatcher dispatcher) {
        super(() -> {
            Scanner scanner = new Scanner(System.in);
            while (server.isRunning()) {
                try {
                    String string = scanner.nextLine();
                    if (string.equalsIgnoreCase("exit")) break;
                    dispatcher.add(new InputEvent(string, server));
                } catch (NoSuchElementException e) { break; }
            }
            dispatcher.add(new ExitEvent(server));
        });
    }
}
