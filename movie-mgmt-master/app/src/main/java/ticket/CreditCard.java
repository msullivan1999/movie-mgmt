package ticket;

public class CreditCard {
    String name;
    int number;

    public CreditCard(String name, int number){
        this.name = name;
        this.number = number;
    }

    public String getName(){
        return this.name;
    }

    public int getNumber(){
        return number;
    }

    @Override
    public boolean equals(Object c){
        if (!(c instanceof CreditCard)){
            return false;
        }

        CreditCard card = (CreditCard) c;
        return this.name.equals(card.getName()) && this.number == card.getNumber();
    }

}
