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