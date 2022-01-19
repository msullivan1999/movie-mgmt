package ticket;

import java.util.ArrayList;
import java.util.HashMap;

public class Cinema {
    private String cinemaName;
    private int cinemaID;
    private ArrayList<Showing> movies;
    private HashMap<Integer, ScreenType> rooms;

    public Cinema(String cinemaName, int cinemaID, int numberOfBronze, int numberOfSilver, int numberOfGold){
        this.cinemaID = cinemaID;
        this.cinemaName = cinemaName;
        this.rooms = initialiseRooms(numberOfBronze, numberOfSilver,  numberOfGold);
        this.movies = initialiseMovies();

    }

    public int getCinemaID(){ return this.cinemaID; }

    public String getCinemaName(){ return this.cinemaName; }

    public HashMap<Integer, ScreenType> getRooms(){
        return this.rooms;
    }

    /**
     * Set up the rooms in the cinema
     * @param b number of bronze rooms
     * @param s number of silver rooms
     * @param g number of gold rooms
     * @return a hashmap of rooms (ID to ScreenType)
     */
    private HashMap<Integer, ScreenType> initialiseRooms(int b, int s, int g){
        HashMap<Integer, ScreenType> returnMap = new HashMap<>();
        int roomID = 1;
        for (;b > 0; b--){
            returnMap.put(roomID, ScreenType.BRONZE);
            roomID++;
        }
        for (;s > 0; s--){
            returnMap.put(roomID, ScreenType.SILVER);
            roomID++;
        }
        for (;g > 0; g--){
            returnMap.put(roomID, ScreenType.GOLD);
            roomID++;
        }

        return returnMap;
    }

    public static ScreenType getScreenTypeOfRoomNumber(Cinema c, int roomNumber){
        return c.getRooms().get(roomNumber);
    }

    public static Cinema getCinemaWithID(ArrayList<Cinema> cinemaList, int id){
        for (Cinema c : cinemaList){
            if (c.getCinemaID() == id){
                return c;
            }
        }
        return null;
    }

    /**
     * Read the file movieList, add all the movies at this cinema
     * @return
     */
    private ArrayList<Showing> initialiseMovies(){
        return null;
    }

    enum ScreenType{
        GOLD {
            public String toString() { return "Gold"; }
        },
        SILVER {
            public String toString() { return "Silver"; }
        },
        BRONZE {
            public String toString() { return "Bronze"; }
        };

        public double[] getPriceList() {
            double[] price;
            if (this == ScreenType.GOLD) {
                final double[] priceG = {15, 25, 30, 20};
                price = priceG;
            } else if (this == ScreenType.SILVER) {
                final double[] priceS = {13, 23, 28, 18};
                price = priceS;
            } else {
                final double[] priceB = {10, 20, 25, 15};
                price = priceB;
            }
            return price;
        }
    }

}
