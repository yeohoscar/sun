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
            if (!player.isBankrupt() && player.getClass().getSimpleName() != "DealerPlayer") {
                System.out.println("Dealer's card: (" + faceUpCard.getName() + " of " + faceUpCard.getSuit() + ")");
                switch (player.getClass().getSimpleName()) {
                    case "HumanPlayer":
                        player.placeBet(-1);
                        break;
                    case "ComputerPlayer":
                        //TODO player.placeBet();
                        ((ComputerPlayer) player).setDealerCard(faceUpCard);
                        player.placeBet(5);
                        break;
                    default:
                        break;
                }
                player.takeTurn(faceUpCard);
            }
        }
        dealer.takeTurn(faceUpCard);
        for (Player player : players) {
            if (player.getClass().getSimpleName() != "DealerPlayer") {
                for (BlackjackHand hand : player.getHands()) {
                    if (hand.getValue() > 21) {
                        //player busted
                        dealer.winBet(hand.getStake());
                        System.out.println(player.getName() + " loss " + hand.getStake() + " bet");
                    } else if (dealer.hasBusted() || hand.getValue() > dealer.getHands().get(0).getValue()) {
                        //player win
                        player.winBet(hand.getStake() * 2);
                        dealer.lossBet(hand.getStake());
                        System.out.println(player.getName() + " win " + hand.getStake() + " bet");
                    } else if (hand.getValue() == dealer.getHands().get(0).getValue()) {
                        //draw
                        player.winBet(hand.getStake());
                        System.out.println(player.getName() + " return " + hand.getStake() + " bet back");
                    } else {
                        //dealer win
                        System.out.println(player.getName() + " loss " + hand.getStake() + " bet");
                        dealer.winBet(hand.getStake());
                    }
                }
                System.out.println(player.getName() + "'s current bank: " + player.getBank() + " bet");
            } else {
                System.out.println("Dealer's current bank: " + player.getBank() + " bet");
            }

        }
    }

    public void deal() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                if (players[i].isBankrupt()) {
                    removePlayer(i);
                } else {
                    players[i].reset();
                    players[i].dealTo(deck);
                }
            }
        }
    }

    public void removePlayer(int num) {
        if (num >= 0 && num < players.length) {
            System.out.println("\n> " + players[num].getName() + " leaves the game.\n");

            players[num] = null;
        }
    }
}
