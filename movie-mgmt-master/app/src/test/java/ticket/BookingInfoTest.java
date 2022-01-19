package ticket;
import org.junit.jupiter.api.*;

import ticket.BookingInfo.TxStatus;
import ticket.Cinema.ScreenType;

import static org.junit.jupiter.api.Assertions.*;

public class BookingInfoTest {

    BookingInfo bookingInfo;

    @BeforeEach
    void init() {
        bookingInfo = new BookingInfo();
    }

    @Test
    void testInstantiation(){
        assertFalse(bookingInfo.isComplete());
        assertNull(bookingInfo.getCinema());
        assertNull(bookingInfo.getBookedMovie());
        assertNull(bookingInfo.getShowing());
        assertEquals(TxStatus.INPROG, bookingInfo.getTxStatus());
        assertNull(bookingInfo.getNumOfPeople());
    }

    @Test
    void testSetCinema(){
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        assertFalse(bookingInfo.isComplete());
        bookingInfo.setCinema(cinema);
        assertNull(bookingInfo.getShowing());
        assertNotNull(bookingInfo.cinema);
        assertNull(bookingInfo.bookedMovie);
        assertNull(bookingInfo.getNumOfPeople());
        assertNotNull(bookingInfo.getCinema());
        assertEquals(TxStatus.INPROG, bookingInfo.getTxStatus());
        assertFalse(bookingInfo.isComplete());
    }

    @Test
    void testSetMovie(){
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        assertFalse(bookingInfo.isComplete());
        bookingInfo.setBookedMovie(movie);
        assertNotNull(bookingInfo.bookedMovie);

        assertNull(bookingInfo.getNumOfPeople());
        assertNull(bookingInfo.getCinema());
        assertEquals(TxStatus.INPROG, bookingInfo.getTxStatus());
        assertFalse(bookingInfo.isComplete());
    }

    @Test
    void testSetNumPpl(){
        assertFalse(bookingInfo.isComplete());
        final int[] people = {0,1,0,0};
        bookingInfo.setNumOfPeople(people);
        assertNull(bookingInfo.getShowing());
        assertNull(bookingInfo.cinema);
        assertEquals(people,bookingInfo.numOfPeople);
        assertNull(bookingInfo.getCinema());
        assertEquals(TxStatus.INPROG, bookingInfo.getTxStatus());
        assertFalse(bookingInfo.isComplete());
    }

    @Test
    void testIsComplete(){
        assertFalse(bookingInfo.isComplete());

        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);

        bookingInfo.setCinema(cinema);
        bookingInfo.setBookedMovie(movie);
        assertEquals(movie, bookingInfo.getMovie());
        final int[] people = {0,1,0,0};
        bookingInfo.setNumOfPeople(people);
        bookingInfo.setShowing(show);
        assertTrue(bookingInfo.doneTx());
        
        assertEquals(people,bookingInfo.getNumOfPeople());
        assertEquals(cinema,bookingInfo.getCinema());
        assertEquals(movie,bookingInfo.getBookedMovie());
        assertEquals(show,bookingInfo.getShowing());
        assertEquals(TxStatus.DONE, bookingInfo.getTxStatus());
        assertTrue(bookingInfo.isComplete());
        assertNotNull(bookingInfo.getTxInfo());
    }

    @Test
    void testIsComplete2(){
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        long ctr = Long.valueOf(bookingInfo.getTxId());
        final int[] people = {0,1,0,0};
        bookingInfo = new BookingInfo(movie, show, cinema, people);
        ctr += 1;

        assertTrue(bookingInfo.doneTx());
        
        assertEquals(people,bookingInfo.getNumOfPeople());
        assertEquals(cinema,bookingInfo.getCinema());
        assertEquals(movie,bookingInfo.getBookedMovie());
        assertEquals(show,bookingInfo.getShowing());
        assertEquals(String.valueOf(ctr), bookingInfo.getTxId());
        assertEquals(TxStatus.DONE, bookingInfo.getTxStatus());
        assertFalse(!bookingInfo.isComplete());
    }

    @Test
    void testSetSeatAfterDone(){
        assertFalse(bookingInfo.isComplete());

        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);

        bookingInfo.setCinema(cinema);
        bookingInfo.setBookedMovie(movie);
        assertEquals(movie, bookingInfo.getMovie());
        final int[] people = {0,1,0,0};
        bookingInfo.setNumOfPeople(people);
        bookingInfo.setShowing(show);
        assertTrue(bookingInfo.doneTx());
        
        assertEquals(people,bookingInfo.getNumOfPeople());
        assertEquals(cinema,bookingInfo.getCinema());
        assertEquals(movie,bookingInfo.getBookedMovie());
        assertEquals(show,bookingInfo.getShowing());
        assertEquals(TxStatus.DONE, bookingInfo.getTxStatus());
        assertTrue(bookingInfo.isComplete());
        final int[] seats = {0, 0, 1};
        assertFalse(bookingInfo.setSeatType(seats));
    }

    @Test void setShowAfterDone() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show1 = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        Showing show2 = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.SILVER, 50, 50, 50);
        final int[] people = {0,1,0,0};
        bookingInfo = new BookingInfo(movie, show1, cinema, people);
        assertTrue(bookingInfo.doneTx());
        assertNull(bookingInfo.getCancelReason());

        bookingInfo.setShowing(show2);
        assertEquals(show1, bookingInfo.getShowing());
    }

    @Test void setShowAfterCancel() {
        Showing show1 = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        bookingInfo.cancelTx("Test");
        assertEquals("Test", bookingInfo.getCancelReason());

        bookingInfo.setShowing(show1);
        assertNull(bookingInfo.getShowing());
    }

    @Test void setMovieAfterDone() {
        Movie movie1 = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Movie movie2 = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        final int[] people = {0,1,0,0};
        bookingInfo = new BookingInfo(movie1, show, cinema, people);
        assertTrue(bookingInfo.doneTx());

        bookingInfo.setBookedMovie(movie2);
        assertEquals(movie1, bookingInfo.getBookedMovie());
    }

    @Test void setMovieAfterCancel() {
        Movie movie1 = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        bookingInfo.cancelTx("Test");

        bookingInfo.setBookedMovie(movie1);
        assertNull(bookingInfo.getMovie());
    }

    @Test void setPeopleNumberAfterDone() {
        Movie movie1 = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        final int[] people = {0,1,0,0};
        bookingInfo = new BookingInfo(movie1, show, cinema, people);
        assertTrue(bookingInfo.doneTx());

        final int[] notGoodPeople = {1,2,3,4};
        bookingInfo.setNumOfPeople(notGoodPeople);
        assertEquals(people, bookingInfo.getNumOfPeople());
    }

    @Test void setPeopleNumberAfterCancel() {
        Movie movie1 = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        bookingInfo.cancelTx("Test");

        final int[] notGoodPeople = {1,2,3,4};
        bookingInfo.setNumOfPeople(notGoodPeople);
        assertNull(bookingInfo.getNumOfPeople());
    }

    @Test void setCinemaAfterDone() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema1 = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Cinema cinema2 = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        final int[] people = {1,2,3,4};
        bookingInfo = new BookingInfo(movie, show, cinema1, people);
        assertTrue(bookingInfo.doneTx());

        bookingInfo.setCinema(cinema2);
        assertEquals(cinema1, bookingInfo.getCinema());
    }

    @Test void setCinemaAfterCancel() {
        Cinema cinema1 = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        bookingInfo.cancelTx("Test");

        bookingInfo.setCinema(cinema1);
        assertNull(bookingInfo.getCinema());
    }

    @Test void doneTxAfterCancel() {
        bookingInfo.cancelTx("Test");
        assertTrue(bookingInfo.isCancelled());
        
        assertFalse(bookingInfo.doneTx());
    }

    @Test void doneTxWhenMovieNull() {
        Cinema cinema1 = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        final int[] people = {1,2,3,4};
        bookingInfo = new BookingInfo(null, show, cinema1, people);
        assertFalse(bookingInfo.doneTx());
    }
    
    @Test void doneTxWhenShowNull() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema1 = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        final int[] people = {1,2,3,4};
        bookingInfo = new BookingInfo(movie, null, cinema1, people);
        assertFalse(bookingInfo.doneTx());
    }

    @Test void doneTxWhenCinemaNull() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        final int[] people = {1,2,3,4};
        bookingInfo = new BookingInfo(movie, show, null, people);
        assertFalse(bookingInfo.doneTx());
    }

    @Test void doneTxWhenNumberPeopleZero() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        final int[] people = {0,0,0,0};
        bookingInfo = new BookingInfo(movie, show, cinema, people);
        assertFalse(bookingInfo.doneTx());
    }

    @Test void bookingInfoNotLenFour() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        final int[] people = {0,0,0};
        bookingInfo = new BookingInfo(movie, show, cinema, people);
        assertFalse(bookingInfo.setNumOfPeople(people));
    }

    @Test void testSetSeat() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        final int[] people = {0,0,0,0};
        int[] seats = {0, 0, 0};
        bookingInfo = new BookingInfo(movie, show, cinema, people);
        assertArrayEquals(seats, bookingInfo.getSeatType());
        assertTrue(bookingInfo.setSeatType(seats));
    }

    @Test void testSetSeatLenNotThree() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Showing show = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        final int[] people = {0,0,0,0};
        int[] seats = {0, 0, 0};
        bookingInfo = new BookingInfo(movie, show, cinema, people);
        assertArrayEquals(seats, bookingInfo.getSeatType());
        assertFalse(bookingInfo.setSeatType(people));
    }

    @Test void testSetSeatTypeAfterCancel() {
        bookingInfo.cancelTx("Test");
        final int[] seats = {0, 0, 0};
        assertFalse(bookingInfo.setSeatType(seats));
    }

    @Test void testSetSeatTypeAfterDone() {
        Movie movie = new Movie(null,null,Integer.MIN_VALUE,Integer.MIN_VALUE,null,null,null,null);
        Cinema cinema = new Cinema(null,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
        Showing show1 = new Showing("2021-01-12 12:12", 1, 2, 12, ScreenType.GOLD, 50, 50, 50);
        final int[] people = {0,1,0,0};
        final int[] seats = {0, 0, 0};
        bookingInfo = new BookingInfo(movie, show1, cinema, people);
        assertTrue(bookingInfo.doneTx());
        assertNull(bookingInfo.getCancelReason());

        assertFalse(bookingInfo.setSeatType(seats));
    }

    @Test void testSetUser() {
        User user = new User("Bani", "Adam", null, User.UserType.CUSTOMER);
        bookingInfo.setUser(user);
        assertEquals(user, bookingInfo.getUser());
    }

    @Test void testStartAndEndTimeNotNull() {
        assertNotNull(bookingInfo.getTxStartTime());
        assertNull(bookingInfo.getTxEndTime());
        bookingInfo.cancelTx("Test");
        assertNotNull(bookingInfo.getTxEndTime());
    }
}
