package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.server.event.EventDispatcher;
import net.hmsvr.chatroom.server.event.ExitEvent;
import net.hmsvr.chatroom.server.event.InputEvent;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class InputThread extends Thread {

    public InputThread(@NotNull Server server, @NotNull EventDispatcher dispatcher) {
        super(() -> {
            Scanner scanner = new Scanner(System.in);
            while (server.isRunning()) {
                try {
                    String string = scanner.nextLine();
                    if (string.equalsIgnoreCase("exit")) break;
                    dispatcher.add(new InputEvent(string));
                } catch (NoSuchElementException e) { break; }
            }
            dispatcher.add(new ExitEvent(server));
        });
    }
}
