import poker.Card;

public class RoundOfBlackJack {
    private DeckOfCards deck;
    private Player[] players;

    public RoundOfBlackJack(DeckOfCards deck, Player[] players) {
        this.deck = deck;
        this.players = players;
        deal();
    }

    public void play() {
        DealerPlayer dealer = (DealerPlayer) players[players.length - 1];
        Card faceUpCard = dealer.showOneCard();

        for (Player player : players) {
            if (player == null) {
                continue;
            }
            if (!(player instanceof DealerPlayer)) {

                switch (player.getClass().getSimpleName()) {
                    case "HumanPlayer" -> {
                        System.out.println("Dealer's card: (" + faceUpCard.getName() + " of " + faceUpCard.getSuit() + ")");
                        player.placeBet(-1);
                    }
                    case "ComputerPlayer" -> {
                        //TODO player.placeBet();
                        ((ComputerPlayer) player).setDealerCard(faceUpCard);
                        player.placeBet(Math.min(player.getBank(), 5));
                    }
                    default -> {
                    }
                }
                player.takeTurn(faceUpCard);
            }
        }
        dealer.takeTurn(faceUpCard);
        for (Player player : players) {
            if (player == null) {
                continue;
            }
            if (!(player instanceof DealerPlayer) ) {
                for (BlackjackHand hand : player.getHands()) {
                    if (hand.isBusted()) {
                        //player busted
                        System.out.println(player.getName() + " loss " + hand.getStake() + " bet");
                    } else if (hand.hasSurrendered()) {
                        player.winBet(hand.getStake() / 2);
                        System.out.println(player.getName() + " surrendered, return " + hand.getStake() / 2 + " bet");
                    } else if (dealer.getHand().isBusted() || hand.getValue() > dealer.getHand().getValue() || hand.getNumCardsInHand() == 5) {
                        //player win
                        player.winBet(hand.getStake() * 2);
                        System.out.println(player.getName() + " win " + hand.getStake() + " bet");
                    } else if (hand.getValue() == dealer.getHand().getValue()) {
                        //draw
                        player.winBet(hand.getStake());
                        System.out.println(player.getName() + " return " + hand.getStake() + " bet back");
                    } else {
                        //dealer win
                        System.out.println(player.getName() + " loss " + hand.getStake() + " bet");
                        hand.setStake(0);
                    }
                }
                System.out.println("> "+player.getName() + "'s current bank: " + player.getBank() + " bet");
            }

        }
    }

    /*-----------Restart game and deal all remaining players cards--------------------------*/

    public void deal() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                if (players[i].isBankrupt()) {
                    removePlayer(i);
                } else {
                    players[i].dealTo(deck);
                }
            }
        }
    }

    /*------------Modifier-------------------*/

    public void removePlayer(int num) {
        if (num >= 0 && num < players.length) {
            System.out.println("\n> " + players[num].getName() + " leaves the game.\n");

            players[num] = null;
        }
    }
}
