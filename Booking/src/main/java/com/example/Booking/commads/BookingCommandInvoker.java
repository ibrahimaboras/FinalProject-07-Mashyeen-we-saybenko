package com.example.Booking.commads;// src/main/java/com/example/Booking/command/BookingCommandInvoker.java

import com.example.Booking.commads.BookingCommand;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Holds a queue of BookingCommands and processes them on a schedule.
 */
@Component
public class BookingCommandInvoker {
    private final Queue<BookingCommand> queue = new ConcurrentLinkedQueue<>();

    /** Enqueue a command for later execution */
    public void addCommand(BookingCommand cmd) {
        queue.offer(cmd);
    }

    /** Every second, drain and execute all pending commands */
    @Scheduled(fixedDelay = 1_000)
    public void processCommands() {
        BookingCommand cmd;
        while ((cmd = queue.poll()) != null) {
            try {
                cmd.execute();
            } catch (Exception e) {
                // TODO: log & handle failures (retry, DLQ, etc.)
                e.printStackTrace();
            }
        }
    }
}
