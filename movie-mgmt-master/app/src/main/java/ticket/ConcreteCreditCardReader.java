package ticket;

import com.google.common.collect.ObjectArrays;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("unchecked")
public class ConcreteCreditCardReader implements CreditCardReader {
  FileReader reader;
  List <CreditCard> cards = new ArrayList<CreditCard>();

  public ConcreteCreditCardReader(String filePath){
    try{
      this.reader = new FileReader("src/main/resources/" + filePath);
    }
    catch (FileNotFoundException e){
      e.printStackTrace();
    }
  }

  @Override
  public List<CreditCard> getCreditcards() {
    JSONObject jsonObject;
    JSONParser jsonParser = new JSONParser();
    JSONArray jArr;
    try{
      jsonObject = (JSONObject) jsonParser.parse(reader);
      jArr = (JSONArray)jsonObject.get("cards");

      jArr.forEach(n->{
        JSONObject card = (JSONObject)n;
        cards.add(new CreditCard((String)card.get("name"),(int)card.get("number") ));
      });
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return this.cards;
  }


}
