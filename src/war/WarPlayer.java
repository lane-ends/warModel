package war;

import exceptions.EmptyDeckException;
import framework.Location;
import framework.model.GameController;
import framework.model.Player;
import simplecards.standard.Card;
import simplecards.standard.Cardset;

public  class WarPlayer extends Player {
    private Cardset hand;
    private Cardset conqueredCards;
    private Card activeCard = null;

    public Card doTurn(){
        activeCard = draw();
        return activeCard;
    }

    //public abstract Card doTurn();
    //returns the card that we play

    public WarPlayer(GameController gameController, Location location, Cardset hand) {
        super(gameController, location);
        this.hand = hand;
        conqueredCards = new Cardset();
    }

    public void addConqueredToHand(){
        hand.addAllCards(conqueredCards);
        conqueredCards.clear();
    }

    public void addToConquered(Card card){
        conqueredCards.add(card);
    }

    public void addToConquered(Cardset cards){
        conqueredCards.addAllCards(cards);
    }

    private Card draw(){
        if (!hand.isEmpty()) {
           return hand.removeCard(0);
        }
        else if (!conqueredCards.isEmpty()) {
            addConqueredToHand();
            return hand.removeCard(0);
        }
        else
            throw new EmptyDeckException();
    }

    public Card getActiveCard(){
        return this.activeCard;
    }

    @Override
    public String toString(){
        return super.getLocation().toString();
    }
    //Cardset no longer asks for type
    //StandardCard doesn't exist - is just Card now

    //shuffle, addconqueredtohand - will handle shuffling

}
