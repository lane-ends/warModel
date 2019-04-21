package war;

import exceptions.EmptyDeckException;
import framework.Location;
import framework.model.GameController;
import framework.model.Player;
import framework.view.GUIController;
import simplecards.standard.Card;
import simplecards.standard.*;
import simplecards.standard.Deck;

public class WarGameController implements GameController {
    private WarPlayer human;
    private WarPlayer computer;
    Deck deck;

    public boolean debug = true;

    @Override
    public void playGame() {
        reset();
        Player p;
        while ((p = playRound()) == null) {

        }
        System.out.println("The Winner Is: " + p.toString());

    }

    @Override
    public void reset() {
        deck = new Deck();
        deck.shuffle();

        Cardset humanDeck = new Cardset();
        Cardset computerDeck = new Cardset();
        for (int i = 1; !deck.isEmpty(); i++) {
            if (i % 2 == 0) {
                computerDeck.add(deck.drawCard());
            } else {
                humanDeck.add(deck.drawCard());
            }
        }
        human = new WarPlayer(this, Location.SOUTH, humanDeck);
        computer = new WarPlayer(this, Location.NORTH, computerDeck);


    }

    @Override
    public GUIController getGUIController() {
        return null;
    }

    @Override
    public void setGUIController(GUIController guiController) {

    }


    public Player playRound() {
        try {
            human.doTurn();
        } catch (EmptyDeckException e) {
            return human;
        }

        try {
            computer.doTurn();
        } catch (EmptyDeckException e) {
            return computer;
        }
        try {
            compareCard();
        }catch (EmptyDeckException e){
            return playRound();
        }
        return null;
    }


    public WarPlayer compareCard() {
        //human.doTurn();
        //computer.doTurn();

        Card humanCompareCard = human.getActiveCard();
        Card computerCompareCard = computer.getActiveCard();

        System.out.println("Next Match: ");
        System.out.println();
        System.out.println("Computer card is: " + computerCompareCard.getFaceAsString());
        System.out.println();
        System.out.println("Human card is:  " + humanCompareCard.getFaceAsString());
        System.out.println();


        WarPlayer winner = null;
        System.out.println("Ran");
        if (getCardValue(humanCompareCard) > getCardValue(computerCompareCard)) {
            winner = human;
            System.out.println("Human card won with " + getCardValue(humanCompareCard) + " "+humanCompareCard.getFaceAsString());

            human.addToConquered(humanCompareCard);
            human.addToConquered(computerCompareCard);

        } else if (getCardValue(humanCompareCard) < getCardValue(computerCompareCard)) {

            winner = computer;
            System.out.println("Computer card won with " + getCardValue(computerCompareCard) + " "+computerCompareCard.getFaceAsString());
            computer.addToConquered(computerCompareCard);
            computer.addToConquered(humanCompareCard);

        } else if (getCardValue(humanCompareCard) == getCardValue(computerCompareCard)) {
            System.out.println("Got War bru");
            Cardset warGainCards = new Cardset();
            warGainCards.add(computerCompareCard);
            warGainCards.add(humanCompareCard);

            warGainCards.add(human.doTurn());
            try {
                warGainCards.add(human.doTurn());
                warGainCards.add(human.doTurn());
                human.doTurn();
            }catch(EmptyDeckException e){
                human.addToConquered(warGainCards.removeCard(0));
                human.doTurn();
            }
                warGainCards.add(computer.doTurn());


            try {
                Object o;
                warGainCards.add(computer.doTurn());
                warGainCards.add(computer.doTurn());
                computer.doTurn();
            }catch (EmptyDeckException e){
                computer.addToConquered(warGainCards.removeCard(0));
                computer.doTurn();
            }


            winner = compareCard();
            winner.addToConquered(warGainCards);

        }
        return winner;
    }

    public int getCardValue(Card card) {
        int faceValue = card.getFace().getValue();
        if (faceValue == 0) {
            faceValue = 14;
        } else {
            faceValue++;
        }
        return faceValue;
    }

    //war method to handle when theres a war
    //arraylist cardset to determine who wins - return a player

}
