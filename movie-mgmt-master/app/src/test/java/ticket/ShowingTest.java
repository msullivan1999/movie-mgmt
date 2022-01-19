package ticket;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShowingTest {
    
    Showing show;
    int expectedBSeat = 50;
    int expectedMSeat = 51;
    int expectedFSeat = 52;
    int expectedMovieId = 1;
    int expectedCinemaId = 2;
    int expectedCinemaRoomNumber = 3;

    @BeforeEach
    void testInitiation() {
        show = new Showing("12:22", expectedMovieId, expectedCinemaId, expectedCinemaRoomNumber, Cinema.ScreenType.GOLD, expectedFSeat, expectedMSeat, expectedBSeat);
    }

    @Test void testGetBackSeats() {
        assertEquals(expectedBSeat, show.getBSeats());
    }

    @Test void testGetFrontSeats() {
        assertEquals(expectedFSeat, show.getFSeats());
    }

    @Test void testGetMiddleSeats() {
        assertEquals(expectedMSeat, show.getMSeats());
    }

    @Test void testGetCinemaRoomNumber() {
        assertEquals(expectedCinemaRoomNumber, show.getCinemaRoomNumber());
    }

    @Test void testGetMovieId() {
        assertEquals(expectedMovieId, show.getID());
    }

    @Test void testGetCinemaId() {
        assertEquals(expectedCinemaId, show.getCinemaID());
    }

    @Test void testTakeAllSeats() {
        int[] take = {expectedFSeat, expectedMSeat, expectedBSeat};
        assertEquals(1, show.takeSeats(take));
        assertEquals(0, show.getFSeats());
        assertEquals(0, show.getMSeats());
        assertEquals(0, show.getBSeats());
    }

    @Test void testTakeTooMuchFrontSeats() {
        int[] take = {expectedFSeat + 1, expectedMSeat, expectedBSeat};
        assertEquals(-1, show.takeSeats(take));
        assertEquals(expectedFSeat, show.getFSeats());
        assertEquals(expectedMSeat, show.getMSeats());
        assertEquals(expectedBSeat, show.getBSeats());
    }

    @Test void testTakeTooMuchMiddleSeats() {
        assertEquals(expectedFSeat, show.getFSeats());

        int[] take = {expectedFSeat, expectedMSeat + 1, expectedBSeat};
        assertEquals(-2, show.takeSeats(take));
        assertEquals(expectedFSeat, show.getFSeats());
        assertEquals(expectedMSeat, show.getMSeats());
        assertEquals(expectedBSeat, show.getBSeats());
    }

    @Test void testTakeTooMuchBackSeats() {
        assertEquals(expectedFSeat, show.getFSeats());

        int[] take = {expectedFSeat, expectedMSeat, expectedBSeat + 1};
        assertEquals(-3, show.takeSeats(take));
        assertEquals(expectedFSeat, show.getFSeats());
        assertEquals(expectedMSeat, show.getMSeats());
        assertEquals(expectedBSeat, show.getBSeats());
    }
}
