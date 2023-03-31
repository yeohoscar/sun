package Texas_Hold_Em;

import poker.DeckOfCards;
import poker.PotOfMoney;

import java.util.ArrayList;

public class PrintGame {
    private ArrayList<TexasPlayer> texasPlayers;
    private DeckOfCards deck;
    private PotOfMoney pot;

    public PrintGame(ArrayList<TexasPlayer> texasPlayers, DeckOfCards deck, PotOfMoney pot){
        this.texasPlayers = texasPlayers;
        this.deck = deck;
        this.pot = pot;
    }
    private String printFaceDownCard(TexasPlayer texasPlayer){
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
    private String printFaceUpCard(){
        String[] suits = {"\u001B[31m♥\u001B[0m", "\u001B[34m♠\u001B[0m", "\u001B[32m♦\u001B[0m", "\u001B[33m♣\u001B[0m"};
        //String[] suits = {"♠", "♥", "♦", "♣"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int suitIndex = 0;
        int rankIndex = 0;
        String storeCode;
        for (int i = 0; i < suits.length; i++) {
            if (suit.equals(suits[i])) {
                suitIndex = i;
                break;
            }
        }
//        switch (suit){
//            case "♠" ->{
//                storeCode = "│  \u001B[34m";
//            }
//        }
        for (int i = 0; i < ranks.length; i++) {
            if (rank.equals(ranks[i])) {
                rankIndex = i;
                break;
            }
        }
        if(ranks[rankIndex]=="10"){
            return "╭───╮\n" +
                    "│" + ranks[rankIndex]  + " │\n" +
                    "│  " + suits[suitIndex] +  "│\n" +
                    "╰───╯";
        }else {
            return "╭───╮\n" +
                    "│" + ranks[rankIndex] + "  │\n" +
                    "│  " + suits[suitIndex] + "│\n" +
                    "╰───╯";
        }
    }
    private void printTable(boolean showPublicCards){
        int count=0;
        for(TexasPlayer player: texasPlayers){
            if(count==2){
                System.out.println(printFaceDownCard(player)+"\n");
                if(showPublicCards){
                    //TODO: print faceUpCard
                }
            }
            else {
                System.out.print(printFaceDownCard(player));
            }
            count++;
        }
    }
    public void table(String command){
        switch (command){
            case "deal" ->{
                printTable(false);
                break;
            }
            case "pre-flop" ->{
                printTable(true);
                break;
            }
            case "flop" ->{
                printTable(true);
                break;
            }
            case "turn" ->{
                printTable(true);
                break;
            }
            case "river" ->{
                printTable(true);
                break;
            }
            //after river round, if there are more than two players left, they need to showDown
            default -> {
                printTable(true);
                break;
            }
        }
    }
}