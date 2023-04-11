# Blackjack
## Created by sun_emoji
Oscar Yeoh 20403662
Haocheng Sun 19203637
Yanhao Sun 19205434

## Instructions to run
1. Run jar package.
2. Enter name when prompted
3. Place bet using numbers when prompted
4. Select option using the number label when prompted

## Justification of Implementation
Due to the distinct differences between Blackjack and Poker, despite their similarities, it was necessary to create standalone classes such as Player, DeckofCards, GameOfBlackjack, etc. Many of the code present in the Poker classes were replicated and modified to accomodate Blackjack. This is because extending the classes did not seem correct as Blackjack is not Poker. Additionally, it would require overriding many of the methods, making it difficult to debug. Thus, it was better to create them as standalone classes. 

# Texas Hold'Em
## Instructions to run


## Poker package change log
- DeckOfCards now extends Deck interface
- PokerHand now extends Hand interface
- Player class access modifier changed to public
- Player hand variable and relevant methods changed from type PokerHand to type Hand
- Player "should" action methods' access modifiers changed to protected
- Player throwaway method's access modifier changed to public
- Added constructor chaining to pokerHand that allows for variable number of cards to be dealt
- Added overloaded method to dealHand that allows variable number of cards to be dealt
- Changed access modifiers of Player's class variables to protected
- PokerHand categorise method now uses recategorize() instead of categoriseAs