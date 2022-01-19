package ticket;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import ticket.Cinema.ScreenType;
import ticket.lib.UI;

/**
 * Wrapper class for data associated with a booking event
 * */
public class BookingInfo {

    enum TxStatus {
        INPROG {
            public String toString() { return "In progress"; }
        },
        CANCELLED {
            public String toString() { return "Cancelled"; }
        },
        DONE {
            public String toString() { return "Success"; }
        },
    }

    public static final String txIdFilename = "txid.txt";
    public static final String txFilename = "tx.csv";
    private static long counter = 0;

    private final String txId;
    private Showing showing;
    private User user;
    private TxStatus status;
    private String cancelReason;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    Movie bookedMovie;
    int[] numOfPeople;
    Cinema cinema;
    int[] seatType = {0, 0, 0};;

    public BookingInfo(){
        BookingInfo.readTxId();
        this.txId = String.valueOf(BookingInfo.counter);
        this.status = TxStatus.INPROG;
        this.user = null; // Default to anon
        this.startTime = LocalDateTime.now();
        BookingInfo.counter += 1;
        BookingInfo.writeTxId();
    }

    public BookingInfo(
        Movie movie,
        Showing showing,
        Cinema cinema,
        int[] numOfPeople
    ) {
        this();
        this.bookedMovie = movie;
        this.showing = showing;
        this.cinema = cinema;
        setNumOfPeople(numOfPeople);
    }


    public String getTxId() { return this.txId; }
    public TxStatus getTxStatus() { return this.status; }
    
    public boolean doneTx() {
        if (this.status == TxStatus.CANCELLED) {
            return false;
        }
        int totalPeople = 0;
        for (int type : numOfPeople) {
            totalPeople += type;
        }
        if (bookedMovie != null && totalPeople > 0 &&
        cinema != null && showing != null) {
            this.endTime = LocalDateTime.now();
            this.status = TxStatus.DONE;
            return true;
        }
        return false;
    }
    
    public void cancelTx(String reason) {
        this.endTime = LocalDateTime.now();
        this.status = TxStatus.CANCELLED;
        this.cancelReason = reason;
        this.appendCancelledTxToFile();
    }
    
    public boolean isCancelled() {
        if (this.status == TxStatus.CANCELLED) {
            return true;
        }
        return false;
    }
    
    public String getCancelReason() {
        if (!this.isCancelled()) {
            return null;
        }
        return this.cancelReason;
    }

    public boolean isComplete(){
        if (this.status == TxStatus.DONE) {
            return true;
        }
        return false;
    }
    
    public LocalDateTime getTxStartTime() {
        return this.startTime;
    }

    public LocalDateTime getTxEndTime() {
        return this.endTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public Showing getShowing() { return this.showing; }
    
    public void setShowing(Showing showing) {
        if (this.isComplete() || this.isCancelled()) { return; }
        this.showing = showing;
    }
    
    public Movie getMovie() {
        return this.bookedMovie;
    }
    
    public Movie getBookedMovie() {
        if(!isComplete())return null;return bookedMovie;
    }
    
    public void setBookedMovie(Movie bookedMovie) {
        if (this.isComplete() || this.isCancelled()) { return; }
        this.bookedMovie = bookedMovie;
    }
    
    public int[] getNumOfPeople() {
        return this.numOfPeople;
    }
    
    public boolean setNumOfPeople(int[] numOfPeople) {
        if (this.isComplete() || this.isCancelled()) { return false; }
        if (numOfPeople.length != 4) {
            return false;
        }
        this.numOfPeople = numOfPeople;
        return true;
    }
    
    public boolean setSeatType(int[] seatType) {
        if (this.isComplete() || this.isCancelled()) { return false; }
        if (seatType.length != 3) {
            return false;
        }
        this.seatType = seatType;
        return true;
    }

    public int[] getSeatType() {
        return this.seatType;
    }
    
    public Cinema getCinema() {
        return cinema;
    }
    
    public void setCinema(Cinema cinema) {
        if (this.isComplete() || this.isCancelled()) { return; }
        this.cinema = cinema;
    }

    public String getTxInfo() {
        double price = 0;
        double[] list = this.getShowing().getSize().getPriceList();
        for (int i = 0; i < this.numOfPeople.length; i++) {
            price += this.numOfPeople[i] * list[i];
        }
        String info = "Booking information\n" +
        "Transaction ID: %s\n" +
        "Movie         : %s\n" +
        "Cinema        : %s\n" +
        "Screening Time: %s\n" +
        "Screen Type   : %s\n" +
        "Seats         : Front  - %d\n" +
        "                Middle - %d\n" +
        "                Back   - %d\n" +
        "Tickets       : Child   - %d\n" +
        "                Student - %d\n" +
        "                Adult   - %d\n" +
        "                Senior  - %d\n" +
        "Total Price   : $ %.02f\n" ;
        info = String.format(info,
        this.getTxId(),
        this.getMovie().getName(),
        this.getCinema().getCinemaName(),
        this.getShowing().getTime(),
        this.getShowing().getSize(),
        this.seatType[0],
        this.seatType[1],
        this.seatType[2],
        this.numOfPeople[0],
        this.numOfPeople[1],
        this.numOfPeople[2],
        this.numOfPeople[3],
        price);
        return info;
    }

    public static void generateCancelledTxReport(String filename) {
        String name = (filename == null) ? "Cancelled_TX.csv" : filename + ".csv";
        
        try {
            FileWriter fileWrite = new FileWriter(name);
            BufferedWriter writer = new BufferedWriter(fileWrite);

            for (String line : readCancelledTxFile()) {
                if (line.split(",")[2].equals("Test")) {
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
            writer.close();
            fileWrite.close();
            UI.success(String.format("Report written to %s.", name), true);
        } catch (IOException e) {
            UI.error(String.format("Error writing to %s", name), true);
        }
    }

    private static void readTxId() {
        File file = new File(txIdFilename);
        final String path = App.resourcesDir + File.separator + txIdFilename;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            BookingInfo.counter = Long.valueOf(line);
        } catch(Exception e){
            UI.error(String.format("Error reading %s", path), true);
        }
    }

    private static void writeTxId() {
        File file = new File(App.resourcesDir + File.separator + txIdFilename);
        final String path = App.resourcesDir + File.separator + txIdFilename;

        try {
            FileWriter myWriter = new FileWriter(path);
            String newCount = Long.toString(BookingInfo.counter);
            myWriter.write(newCount);
            myWriter.close();

        } catch (IOException e) {
            UI.error(String.format("Error writing %s", path), true);
        }
    }

    private void appendCancelledTxToFile() {
        File file = new File(App.resourcesDir + File.separator + txFilename);
        final String path = App.resourcesDir + File.separator + txFilename;
        final String username = (this.user == null) ? "Anonymous" : this.user.getName();
        try {
            FileWriter fileWrite = new FileWriter(path, true);
            BufferedWriter writer = new BufferedWriter(fileWrite);
            String txStr = String.format("%s,%s,%s,%s", this.txId, username, this.cancelReason, this.endTime);
            writer.write(txStr);
            writer.newLine();
            writer.close();
            fileWrite.close();
        } catch (IOException e) {
            UI.error(String.format("Error writing %s", path), true);
        }
    }

    private static List<String> readCancelledTxFile() {
        File file = new File(App.resourcesDir + File.separator + txFilename);
        final String path = App.resourcesDir + File.separator + txFilename;
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch(Exception e){
            UI.error(String.format("Error reading %s", txFilename), true);
            e.printStackTrace();
        }
        return lines;
    }
}
