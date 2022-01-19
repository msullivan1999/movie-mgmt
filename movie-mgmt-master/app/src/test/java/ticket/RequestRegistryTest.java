package ticket;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RequestRegistryTest {
  RequestRegistry requestRegistry = new RequestRegistry();

  @Test
  void testInitiation() {
    assertNotNull(requestRegistry.getHandler("Booking"));
    assertNull(requestRegistry.getHandler("someRandomCarp"));
  }

  @Test
  void testAddReg() {
    BookingRequestHandler bookingRequestHandler = new BookingRequestHandler();
   requestRegistry.addRegistry("String",bookingRequestHandler);
   assertNotNull(requestRegistry.getHandler("String"));
   assertEquals(bookingRequestHandler,requestRegistry.getHandler("String"));

  }


}
