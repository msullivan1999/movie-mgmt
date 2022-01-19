package ticket;

/**
 * A class that has a movie and times it runs
 */
public class Showing {
    private String time;
    private int movieID;
    private int cinemaID;
    private int cinemaRoomNumber;
    private int bseats;
    private int mseats;
    private int fseats;
    private Cinema.ScreenType screenSize;

    public Showing(String time, int movieID, int cinemaID, int cinemaRoomNumber, Cinema.ScreenType ScreenSize, int fseats, int mseats, int bseats){
        this.time = time;
        this.movieID = movieID;
        this.cinemaID = cinemaID;
        this.cinemaRoomNumber = cinemaRoomNumber;
        this.screenSize = ScreenSize;
        this.bseats = bseats;
        this.mseats = mseats;
        this.fseats = fseats;
    }

    public int getBSeats() {
        return bseats;
    }

    public int getFSeats() {
        return fseats;
    }

    public int getMSeats() {
        return mseats;
    }
   
    public int getCinemaRoomNumber() {
        return this.cinemaRoomNumber;
    }

    public int getID() { return this.movieID; }

    public String getTime() { return this.time; }

    public int getCinemaID() { return this.cinemaID; }

    public Cinema.ScreenType getSize() { return this.screenSize; }

    public int takeSeats(int[] seats){
        if (((this.fseats - seats[0]) < 0) && ((this.mseats - seats[1]) < 0) && ((this.bseats - seats[2]) < 0)){
            return 0;
        }
        int[] originalSeats = {this.fseats, this.mseats, this.bseats};
        if ((this.fseats - seats[0]) >= 0){
            this.fseats -= seats[0];
        }
        else{
            this.fseats = originalSeats[0];
            this.mseats = originalSeats[1];
            this.bseats = originalSeats[2];
            return -1;
        }
        if ((this.mseats - seats[1]) >= 0){
            this.mseats -= seats[1];
        }
        else{
            this.fseats = originalSeats[0];
            this.mseats = originalSeats[1];
            this.bseats = originalSeats[2];
            return -2;
        }
        if ((this.bseats - seats[2]) >= 0){
            this.bseats -= seats[2];
        }
        else{
            this.fseats = originalSeats[0];
            this.mseats = originalSeats[1];
            this.bseats = originalSeats[2];
            return -3;
        }
        return 1;
    }

}
