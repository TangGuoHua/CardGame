This is gradle project. We can open it in eclipse or idea.
please make sure you have JDK installed, in order to run it. 


CardGame class has a constructor that accept an int value as win point. 
It will send card to three players in order, and exit when one of them get the total point greater than win point. 

example : 

CardGame game = new CardGame(40);
game.setGameName(" 抓的快 "); // we can give name to the game instance
game.send(); // this method will initialize a card list and begin send card. 

开始牌局 ： main
[10, 8, 3, 9, Q, 5, 3, 7, A, 2, 5, K, 9, J, Q, 4, 2, A, 10, A, 3, 7, 5, 6, J, 8, J, 10, 6, 8, K, 4, 4, J, 9, 5, 6, 8, 7, 2, JOKE, Q, 9, 3, 7, 4, Q, 10, K, K, 6, 2, A, JOKE]
发给 张三 一张 10
发给 李四 一张 8
发给 王五 一张 3
发给 张三 一张 9
发给 李四 一张 Q
发给 王五 一张 5
发给 张三 一张 3
发给 李四 一张 7
发给 王五 一张 A
发给 张三 一张 2
发给 李四 一张 5
发给 王五 一张 K
发给 张三 一张 9
发给 李四 一张 J
游戏结束 李四赢得比赛main, 得分:[8, Q, 7, 5, J] = 43

