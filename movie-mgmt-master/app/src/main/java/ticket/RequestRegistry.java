package ticket;

import java.util.HashMap;
import java.util.Map;

public class RequestRegistry {
  private Map<String,RequestHandler> handlerMap = new HashMap<String,RequestHandler>();

  public RequestRegistry(){
    handlerMap.put("Booking",new BookingRequestHandler());
  }

  public void addRegistry(String request, RequestHandler requestHandler){
    if(handlerMap.get(request)!=null)return;
    handlerMap.put(request,requestHandler);
  }

  public RequestHandler getHandler(String request){
   return handlerMap.get(request);

  }

}
