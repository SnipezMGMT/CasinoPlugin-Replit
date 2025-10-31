package com.aidean.casinoplugin;

public class Card {
    private final Suit suit;
    private final Rank rank;

    public enum Suit {
        HEARTS("♥"), DIAMONDS("♦"), CLUBS("♣"), SPADES("♠");
        
        private final String symbol;
        
        Suit(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
    }

    public enum Rank {
        ACE("A", 11), TWO("2", 2), THREE("3", 3), FOUR("4", 4), 
        FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), 
        NINE("9", 9), TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10);
        
        private final String display;
        private final int value;
        
        Rank(String display, int value) {
            this.display = display;
            this.value = value;
        }
        
        public String getDisplay() {
            return display;
        }
        
        public int getValue() {
            return value;
        }
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank.getDisplay() + suit.getSymbol();
    }
}
