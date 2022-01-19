package ticket;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ticket.Cinema.ScreenType;
import ticket.lib.Input;
import ticket.lib.UI;
import java.util.*;

import static ticket.App.getRequiredPath;


public class BookingRequestHandler implements RequestHandler {

    public static final String resourcesDir = "src" + File.separator + "main" + File.separator + "resources";

    private final long TIMEOUT = 120000; // 2 minutes in ms
    // private final long TIMEOUT = 1000;
    private String pageTitle = "Booking";
    private boolean goBack = false;
    private int frontseats;
    private int backseats;
    private int middleseats;
    private int totalpeople = 0;
    BookingInfo bookingInfo = new BookingInfo();

    @Override
    public void handle(App app) {
        bookingInfo.setUser(app.currentUser);
        selectCinema(app);
        if (UI.exit) {
            return;
        }

        if (bookingInfo.isComplete()) {
            UI.success("Booking successful.", true);
        } else if (bookingInfo.isCancelled()) {
            UI.error("Booking cancelled.", true);
        } else {
            UI.error("Booking failed.", true);
        }
    }

    private Timer startTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new Timeout(this.bookingInfo);
        timer.schedule(timerTask, TIMEOUT);
        return timer;
    }

    private void selectCinema(App app) {
        if (bookingInfo.isCancelled()) return;

        List<String> options = new ArrayList<String>();

        app.cinemas.forEach((cinema)->options.add(cinema.getCinemaName()));
        options.add("Cancel");
        options.add("Back");

        goBack = true;
        while (goBack) {
            goBack = false;
            Timer timer = startTimer();
            int input = UI.chooseOption(pageTitle, app.currentUser, options, null);
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input == options.size() - 1) bookingInfo.cancelTx("User cancellation.");
            else if (input == options.size()) {
                List<String> yesNo = new ArrayList<>();
                yesNo.add("Yes");
                yesNo.add("No");
                System.out.println("Cancel transaction?");
                timer = startTimer();
                int i = UI.chooseOption(null, app.currentUser, yesNo, null);
                timer.cancel();
                timer.purge();
                if (UI.exit || bookingInfo.isCancelled()) return;
                if (i == 1) {
                    bookingInfo.cancelTx("User cancellation.");
                } else {
                    goBack = true;
                }
            }
            else { 
                bookingInfo.setCinema(app.cinemas.get(input - 1));
                selectMovie(app);
            }
        }
    }

    private void selectShowing(App app) {
        if (bookingInfo.isCancelled()) return;

        List<Showing> shows = new ArrayList<>();
        List<String> options = new ArrayList<String>();

        goBack = true;
        while (goBack) {
            goBack = false;

            for (Showing show : app.movieShowings) {
                if (show.getCinemaID() == bookingInfo.getCinema().getCinemaID() &&
            show.getID() == bookingInfo.getMovie().getID()) {
                shows.add(show);
                options.add(String.format("%s, %s", show.getTime(), show.getSize()));
            }
        }
            options.add("Cancel");
            options.add("Back");
            Timer timer = startTimer();
            int input = UI.chooseOption(pageTitle, app.currentUser, options, null);
            timer.cancel();
            timer.purge();
            
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input == options.size() - 1) bookingInfo.cancelTx("User cancellation.");
            else if (input == options.size()) {
                goBack = true;
                break;
            } else {
                this.bookingInfo.setShowing(shows.get(input - 1));
                setNumberOfPeople(app);
            }
        }
    }

    private void setNumberOfPeople(App app) {
        if (bookingInfo.isCancelled()) return;
        goBack = true;
        while (goBack) {
            goBack = false;
            String[] description = {"child", "student", "adult", "senior"};
            int[] people = {0, 0, 0, 0};
            for (int i = 0; i < description.length ; i++) {
                System.out.printf("Number of %s ticket, -1 to cancel, -2 to go back: ", description[i]);
                Timer timer = startTimer();
                int input = Input.getIntInput();
                timer.cancel();
                timer.purge();
                if (UI.exit) return;
                if (input == -1) {
                    bookingInfo.cancelTx("User cancellation.");
                    return;
                } else if (input < -1) {
                    System.out.println("Restarting ticket type selection..");
                    // goBack = true;
                    i = -1;
                    continue;
                } else {
                    totalpeople += input;
                    people[i] = input;
                    input = 0;
                }
            }
            bookingInfo.setNumOfPeople(people);

//            checkCreditCard(app);
            chooseSeatLocation(app);

//            getPayment(app);
        }
    }


    private void chooseSeatLocation(App app) {
        if (bookingInfo.isCancelled()) return;
        goBack = true;
        int total = 0;
        while (goBack) {
            goBack = false;
            UI.drawBox("Seat Location");
            System.out.print("How many people would like to sit in the front? -1 to cancel, -2 to go back: ");
            Timer timer = startTimer();
            int input = Input.getIntInput();
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input == -1) {
                bookingInfo.cancelTx("User cancellation.");
                return;
            } else if (input < -1) {
                goBack = true;
                break;
            } else {
                if (input <= totalpeople){
                    frontseats = input;
                    total += input;
                }
                else{
                    System.out.print("This number exceeds the total number of people you have booked for.");
                    continue;
                }
                input = 0;
            }
            System.out.print("How many people would like to sit in the middle? -1 to cancel, -2 to go back: ");
            timer = startTimer();
            input = Input.getIntInput();
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input == -1) {
                bookingInfo.cancelTx("User cancellation.");
                return;
            } else if (input < -1) {
                goBack = true;
                break;
            } else {
                if ((total + input) <= totalpeople){
                    middleseats = input;
                    total += input;
                }
                else{
                    System.out.print("This number exceeds the total number of people you have booked for.");
                    continue;
                }
                input = 0;
            }
            System.out.print("How many people would like to sit in the back? -1 to cancel, -2 to go back: ");
            timer = startTimer();
            input = Input.getIntInput();
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input == -1) {
                bookingInfo.cancelTx("User cancellation.");
                return;
            } else if (input < -1) {
                goBack = true;
                break;
            } else {
                if ((total + input) <= totalpeople){
                    backseats = input;
                    total += input;
                }
                else{
                    System.out.print("This number exceeds the total number of people you have booked for.");
                    continue;
                }
                input = 0;
            }
            if (total < totalpeople){
                System.out.print("Your overall numbers are lower than the total number of people you have booked for.");
                continue;
            }
            int[] values = {frontseats, middleseats, backseats};
            bookingInfo.setSeatType(values);
            getPayment(app);

        }
    }


    private void checkCreditCard(App app){
        if (bookingInfo.isCancelled()) return;
        goBack = true;
        while (goBack) {
            goBack = false;
            if(app.currentUser.getCard() == null){
                System.out.println("\nYou don't have a valid credit card. Please try again");
                goBack = true;
                break;
            }
            System.out.print("Enter account password, enter cancel to cancel the booking:");
            Timer timer = startTimer();
            String input = Input.getPassword();
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input.equals("cancel")) {
                bookingInfo.cancelTx("User cancellation.");
                return;
            }
            if(!app.currentUser.validateCred(app.currentUser.getName(),input)){
               System.out.println("Incorrect account password. Please try again.");
                goBack = true;
                break;
            }

            else {
                confirmSelection(app);
                System.out.println("Payment Successful!");
                // app.viewMovies(null,-1);
            }
        }
    }




    private void selectMovie(App app) {
        if (bookingInfo.isCancelled()) return;
        List<String> options = new ArrayList<String>();
        app.movies.forEach((movie)->options.add(movie.getName()));
        options.add("Cancel");
        options.add("Back");

        goBack = true;
        while (goBack) {
            goBack = false;
            
            Timer timer = startTimer();
            int input = UI.chooseOption(pageTitle, app.currentUser, options, null);
            timer.cancel();
            timer.purge();
            
            if (UI.exit || bookingInfo.isCancelled()) return;
            if (input == options.size() - 1) {
                bookingInfo.cancelTx("User cancellation.");
            } else if (input == options.size()) {
                goBack = true;
                break;
            } else {
                bookingInfo.setBookedMovie(app.movies.get(input-1));
                selectShowing(app);
            }
        }
    }

    private void confirmSelection(App app) {
        if (bookingInfo.isCancelled()) return;
        UI.drawBox("Confirmation");
        String path = App.resourcesDir + File.separator + "bookingHistory/" + "name.csv";
        System.out.println(bookingInfo.getTxInfo());
        try {

            Files.write(Paths.get(path), bookingInfo.getTxInfo().getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            UI.error(String.format("Error writing %s", path), true);
        }

        List<String> options = new ArrayList<String>();
        options.add("Confirm");
        options.add("Cancel");
        options.add("Back");

        Timer timer = startTimer();
        int input = UI.chooseOption(null, app.currentUser, options, null);
        timer.cancel();
        timer.purge();

        if (UI.exit || bookingInfo.isCancelled()) return;
        if (input == options.size() - 1) { 
            bookingInfo.cancelTx("User cancellation.");
        } else if (input == options.size()) {
            goBack = true;
            return;
        } else {

            int[] numbers = {frontseats, middleseats, backseats};
            int value = bookingInfo.getShowing().takeSeats(numbers);
            if (value == 1){

                bookingInfo.doneTx();
            }
            else if (value == -1) {
                System.out.println("There are not enough available seats in the front for this showing. Please book at another slot.");
                bookingInfo.cancelTx("Showing full.");
            }
            else if (value == -2) {
                System.out.println("There are not enough available seats in the middle for this showing. Please book at another slot.");
                bookingInfo.cancelTx("Showing full.");
            }
            else if (value == -3) {
                System.out.println("There are not enough available seats in the back for this showing. Please book at another slot.");
                bookingInfo.cancelTx("Showing full.");
            }
            else if (value == 0) {
                System.out.println("There are not enough available seats at all for this showing. Please book at another slot.");
                bookingInfo.cancelTx("Showing full.");
            }
            return;
        }

    }
    public void getPayment(App app){
        boolean getPayment = true;
        while (!UI.exit && getPayment){

            //Print who is logged in
            ArrayList<String> options = new ArrayList<String> ();
            options.add("Pay with Credit Card");
            options.add("Pay with Gift card");
            options.add("Go Back");

            Timer timer = startTimer();
            int input = UI.chooseOption("Payment Option", app.currentUser, options, null);
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) { break; }

            switch (input){
                case 1:
                    getPayment=false;
                    checkCreditCard(app);
                    break;

                case 2:
                    getPayment=false;
                    checkGiftCard(app);
                    break;

                case 3:
                    getPayment=false;
                    break;

                default:
                    break;
            }
        }

    }

    private void checkGiftCard(App app){
        if (bookingInfo.isCancelled()) return;
        goBack = true;
        while (goBack) {
            goBack = false;

            System.out.print("Card Number: ");
            Timer timer = startTimer();
            String cardNumber = Input.getStrInput();
            timer.cancel();
            timer.purge();
            if (UI.exit || bookingInfo.isCancelled()) return;

            File file = new File("giftCard.csv");
            String newPath =  file.getAbsolutePath().replace("giftCard.csv", resourcesDir + File.separator + "giftCard.csv");
            boolean matched = false;

            for (GiftCard gc : app.giftCards){
                if(gc.getNumber().equals(cardNumber) && gc.getStatus().equals("VALID")) {
                    matched = true;
                    gc.setStatus("REDEEMED");
                    confirmSelection(app);
                    System.out.println("Payment Successful!");
                    updateGiftCards(app);
                    //    app.viewMovies(null,-1);

               } else if (gc.getNumber().equals(cardNumber) && gc.getStatus().equals("REDEEMED")){
                    matched = true;
                    System.out.println("Gift Card already redeemed! It can't be used again :(");
                    getPayment(app);
                }
            }
            if (!matched) {
                System.out.println("Gift card doesn't exist, please try again!");
                getPayment(app);
            }
        }
    }

    public void updateGiftCards(App app){
        String path = App.resourcesDir + File.separator + "giftCard.csv";

        try {
            FileWriter myWriter = new FileWriter(path);
            for(GiftCard gc : app.giftCards){
                StringBuilder toAdd = new StringBuilder(gc.getNumber() + "," + gc.getStatus() + "\n");
                myWriter.write(toAdd.toString());
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
