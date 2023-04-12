# Texas Hold'Em
## Created by sun_emoji
Oscar Yeoh 20403662  
Haocheng Sun 19203637  
Yanhao Sun 19205434

## Instructions to run
1. Run jar package
2. Enter name when prompted
3. Enter number of players, with 2 being minimum and 10 being the maximum
4. Follow prompts to play game

## Justification of Implementation
Since permission to edit the poker package was given, we tried to make minimal changes to the package in order to reuse the code. The implementation for Texas Hold'Em was done as abstract as possible to enable smooth development of Texas
Scramble in the future.

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
- Updated hand type getValue methods to handle issues where hands evaluate to same value despite being different due to different kicker cards
