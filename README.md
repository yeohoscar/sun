# Blackjack

## Instructions to Run
1. Run jar package.
2. Enter name when prompted
3. Place bet using numbers when prompted
4. Select option using the number label when prompted

## Justification of Implementation
Due to the distinct differences between Blackjack and Poker, despite their similarities, it was necessary to create standalone classes such as Player, DeckofCards, GameOfBlackjack, etc. Many of the code present in the Poker classes were replicated and modified to accomodate Blackjack. This is because extending the classes did not seem correct as Blackjack is not Poker. Additionally, it would require overriding many of the methods, making it difficult to debug. Thus, it was better to create them as standalone classes. 
