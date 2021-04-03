This is gradle project. We can open it in eclipse or idea.
please make sure you have JDK installed, in order to run it. 


CardGame class has a constructor that accept an int value as win point. 
It will send card to three players in order, and exit when one of them get the total point greater than win point. 

example : 

CardGame game = new CardGame(40);
game.setGameName(" Card Game "); // we can give name to the game instance
game.send(); // this method will initialize a card list and begin send card. 


we can run the test case in CardGameTest class.
the test result will show the cards in each player's hand, and the person who win the game. 
Here is sample output.
Begin Game ：  Card Deck Game 
[5, 4, 8, A, Q, 3, 8, 9, 3, A, J, 7, J, 9, 6, 6, 5, 8, K, 10, 8, 4, 10, 3, 6, 9, 10, 7, 2, K, 9, Q, 4, 3, K, J, J, 7, 10, K, 2, 5, 6, 2, 2, A, 5, 4, A, JOKE, Q, 7, Q, JOKE]
spiderMan[5] =5
ironMan[4] =4
AmericanCaptain[8] =8
spiderMan[5, A] =6
ironMan[4, Q] =16
AmericanCaptain[8, 3] =11
spiderMan[5, A, 8] =14
ironMan[4, Q, 9] =25
AmericanCaptain[8, 3, 3] =14
spiderMan[5, A, 8, A] =15
ironMan[4, Q, 9, J] =36
AmericanCaptain[8, 3, 3, 7] =21
spiderMan[5, A, 8, A, J] =26
Game Over: ironMan wins 45 that exceed win point :40 [4, Q, 9, J, 9]

