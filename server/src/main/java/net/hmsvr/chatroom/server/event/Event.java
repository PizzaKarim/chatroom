package net.hmsvr.chatroom.server.event;

/**
 * An abstract class inherited by all Event classes.
 */
public abstract class Event {

    /**
     * Executes the event, invoking whatever is contained in the inherited method body.
     */
    public abstract void execute();
}
