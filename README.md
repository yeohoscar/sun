# Software Engineering Project 3
## Developed by sun_emoji
Oscar Yeoh 20403662  
Haocheng Sun 19203637  
Yanhao Sun 19205434

# Poker
Created by lecturer.  
Is available to play.

# Blackjack
## Instructions to Run
1. Run jar package.
2. Enter name when prompted
3. Place bet using numbers when prompted
4. Select option using the number label when prompted

## Justification of Implementation
Due to the distinct differences between Blackjack and Poker, despite their similarities, it was necessary to create standalone classes such as Player, DeckofCards, GameOfBlackjack, etc. Many of the code present in the Poker classes were replicated and modified to accomodate Blackjack. This is because extending the classes did not seem correct as Blackjack is not Poker. Additionally, it would require overriding many of the methods, making it difficult to debug. Thus, it was better to create them as standalone classes.

# Texas Hold'Em
## Instructions to run
1. Run jar package
2. Enter name when prompted
3. Enter number of players, with 2 being minimum and 10 being the maximum
4. Follow prompts to play game

## Justification of Implementation
Since permission to edit the poker package was given, we tried to make minimal changes to the package in order to reuse the code. The implementation for Texas Hold'Em was done as abstract as possible to enable smooth development of Texas Scramble in the future.

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

# Texas Scramble
## Instructions to run
1. Run jar package
2. Select option 4
3. Enter name when prompted
4. Enter number of players to play with, with 1 being minimum and 9 being the maximum
5. Follow prompts to play game

## Note
Blank tile is represented by **^**.

## Special Features
- Able to select what game to play
- Defined computer player personalities
- Different skill levels for computer players i.e. vocabulary size
- Computer players learn new words as games are played
- Submit multiple words for increased score
- If you used all the letters for multiple words you get 50 bonus points
- If you make a 7-length word you get 100 bonus points
- Graphical representation in the text UI
  - Only current player's cards will 