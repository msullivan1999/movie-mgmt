package ticket;

import java.util.*;

import ticket.lib.UI;

public class Timeout extends TimerTask {
    
    private BookingInfo booking;

    public Timeout(BookingInfo booking) {
        this.booking = booking;
    }

    public void run() {
        booking.cancelTx("Timeout");
        UI.error("Booking cancelled. Reason: Timeout", true);
        System.out.println("Enter any text and press enter to continue.");
    }
}
