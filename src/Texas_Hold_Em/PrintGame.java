package Texas_Hold_Em;

import poker.Card;
import poker.DeckOfCards;
import poker.Player;
import poker.PotOfMoney;

import java.util.ArrayList;

public class PrintGame {
    private ArrayList<? extends Player> texasPlayers;
    private DeckOfCards deck;
    private PotOfMoney pot;
    private Hand communityCards;


    public PrintGame(ArrayList<? extends Player> texasPlayers, DeckOfCards deck, PotOfMoney pot, Hand communityCards){
        this.texasPlayers = texasPlayers;
        this.deck = deck;
        this.pot = pot;
        this.communityCards = communityCards;
    }
    private String printFaceDownCard(Player texasPlayer){

        if(texasPlayer.hasFolded()){
            return texasPlayer.getName()+" has folded";
        }else {
            if(texasPlayer.isDealer()){
                return  "  Name:    "+texasPlayer.getName()+
                        "╭───╮" +     "  ╭───╮\n"+
                        "│" + "   │"+"  │"+"   │\n"+
                        "│" + "   │"+"  │"+"   │\n"+
                        "╰───╯"     +"  ╰───╯\n"+
                        "Dealer and Current Stake="+texasPlayer.getStake();
            }
            else {
                return  "  Name:    "+texasPlayer.getName()+
                        "╭───╮" +     "  ╭───╮\n"+
                        "│" + "   │"+"  │"+"   │\n"+
                        "│" + "   │"+"  │"+"   │\n"+
                        "╰───╯"     +"  ╰───╯\n"+
                        "Current Stake="+texasPlayer.getStake();
            }
        }
    }
    public int[] getIndex(Card card, String[] suits, String[] ranks){
        int[] result = new int[2];
        int suitIndex;
        int rankIndex;
        switch (card.getSuit()){
            case "hearts" -> {
                suitIndex = 0;
            }
            case "diamonds" -> {
                suitIndex = 1;
            }
            case "clubs" -> {
                suitIndex = 2;
            }
            default -> {
                suitIndex = 3;
            }
        }
        if(card.isAce()){
            rankIndex = 0;
        }else if(card.isJack()){
            rankIndex = 10;
        }else if(card.isQueen()){
            rankIndex = 11;
        }else if(card.isKing()){
            rankIndex = 12;
        }else {
            rankIndex = card.getRank()-1;
        }
        result[0] = suitIndex;
        result[1] = rankIndex;
        return result;
    }
    private void printPublicCard(Hand hand){
        int suitIndex;
        int rankIndex;
        String[] suits = {"\u001B[31m♥\u001B[0m", "\u001B[32m♦\u001B[0m", "\u001B[33m♣\u001B[0m", "\u001B[34m♠\u001B[0m"};
        //String[] suits = {"♠", "♥", "♦", "♣"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for(Card card: hand.getHand()){
            int[] index = getIndex(card, suits, ranks);
            suitIndex = index[0];
            rankIndex = index[1];
            if(ranks[rankIndex]=="10"){
                String showCard =
                        "╭───╮\n" +
                        "│" + ranks[rankIndex]  + " │\n" +
                        "│  " + suits[suitIndex] +  "│\n" +
                        "╰───╯";
                System.out.print(showCard+" ");
            }else {
                String showCard =
                        "╭───╮\n" +
                        "│" + ranks[rankIndex] + "  │\n" +
                        "│  " + suits[suitIndex] + "│\n" +
                        "╰───╯";
                System.out.print(showCard+" ");
            }

        }
    }
    private void printFaceUpCard(Player texasPlayer){
        Hand hand = texasPlayer.getHand();
        int suitIndex;
        int rankIndex;
        String[] suits = {"\u001B[31m♥\u001B[0m", "\u001B[32m♦\u001B[0m", "\u001B[33m♣\u001B[0m", "\u001B[34m♠\u001B[0m"};
        //String[] suits = {"♠", "♥", "♦", "♣"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for(Card card: hand.getHand()){
            int[] index = getIndex(card, suits, ranks);
            suitIndex = index[0];
            rankIndex = index[1];
            if(texasPlayer.isDealer()){
                if(ranks[rankIndex]=="10"){
                    String showCard =
                                    "  Name:    "+texasPlayer.getName()+
                                    "╭───╮\n" +
                                    "│" + ranks[rankIndex]  + " │\n" +
                                    "│  " + suits[suitIndex] +  "│\n" +
                                    "╰───╯\n"+
                                    "Dealer and Current Stake="+texasPlayer.getStake();
                    System.out.print(showCard+" ");
                }else {
                    String showCard =
                                    "  Name:    "+texasPlayer.getName()+
                                    "╭───╮\n" +
                                    "│" + ranks[rankIndex] + "  │\n" +
                                    "│  " + suits[suitIndex] + "│\n" +
                                    "╰───╯\n"+
                                    "Dealer and Current Stake="+texasPlayer.getStake();
                    System.out.print(showCard+" ");
                }
            }else {
                if(ranks[rankIndex]=="10"){
                    String showCard =
                            "  Name:    "+texasPlayer.getName()+
                                    "╭───╮\n" +
                                    "│" + ranks[rankIndex]  + " │\n" +
                                    "│  " + suits[suitIndex] +  "│\n" +
                                    "╰───╯\n"+
                                    "Current Stake="+texasPlayer.getStake();
                    System.out.print(showCard+" ");
                }else {
                    String showCard =
                            "  Name:    "+texasPlayer.getName()+
                                    "╭───╮\n" +
                                    "│" + ranks[rankIndex] + "  │\n" +
                                    "│  " + suits[suitIndex] + "│\n" +
                                    "╰───╯\n"+
                                    "Current Stake="+texasPlayer.getStake();
                    System.out.print(showCard+" ");
                }
            }

        }
    }

    private void printTable(boolean showPublicCards, boolean showFaceDownCards){

        int count=0;
        if(showFaceDownCards){
            for(Player player: texasPlayers){
                if(count==2){
                    if(showPublicCards){
                        System.out.println();
                        printPublicCard(communityCards);
                        System.out.println();
                    }
                    System.out.print(printFaceDownCard(player)+" ");
                }
                else {
                    System.out.print(printFaceDownCard(player)+"  ");
                }
                count++;
            }
        }
        else {
            for(Player player: texasPlayers){
                if(count==2){
                    if(showPublicCards){
                        System.out.println();
                        printPublicCard(communityCards);
                        System.out.println();
                    }
                    if(player.hasFolded()){
                        System.out.print(player.getName()+" has folded  ");
                    }
                    else {
                        printFaceUpCard(player);
                    }
                }
                else {
                    if(player.hasFolded()){
                        System.out.print(player.getName()+" has folded  ");
                    }
                    else {
                        printFaceUpCard(player);
                    }
                }
                count++;
            }
        }
        System.out.println();
    }
    public void table(Rounds currentRound){
        switch (currentRound){
            case PRE_FLOP ->{
                printTable(false, true);
                break;
            }
            case FLOP ->{
                printTable(true, true);
                break;
            }
            case TURN ->{
                printTable(true, true);
                break;
            }
            case RIVER ->{
                printTable(true, true);
                break;
            }
            //after river round, if there are more than two players left, they need to showDown
            default -> {
                printTable(true, false);
                break;
            }
        }
    }
}