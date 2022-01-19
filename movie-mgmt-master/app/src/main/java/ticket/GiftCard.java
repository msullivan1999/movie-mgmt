package ticket;

public class GiftCard {
    String number;
    String status;

    public GiftCard(String number, String status){
        this.number = number;
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getNumber(){
        return this.number;
    }


    enum giftCardStatus{
        VALID,
        REDEEMED
    }
}
