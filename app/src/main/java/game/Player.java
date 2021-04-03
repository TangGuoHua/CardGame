package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Player extends Thread {
    
   private Lock lock;
   private CountDownLatch latch;
   private String playerName;
   private Condition pre, post;
   private CardGame game; 

   Player(Lock lock, Condition pre, Condition post, String playerName, CountDownLatch latch, CardGame game) {
       this.lock = lock;
       this.pre = pre;
       this.post = post;
       this.latch = latch;
       this.playerName = playerName;
       this.game = game;
    }

   private static ThreadLocal<List<Card>> cardsOfPlayer = new ThreadLocal<List<Card>>() {
       @Override
       protected List<Card> initialValue() {
           return new ArrayList<Card>();
       };
   };

   @Override
   public void run() {
       while (game.getStatus() == Status.RUNNING) {
           lock.lock();
           // 线程每次先进入等待状态
           try {
               latch.countDown();   // latch的作用是为了保证能成功唤醒第一个线程
               pre.await();
                pickCard(playerName);
               post.signalAll();
               
           } catch (InterruptedException e) {
               System.out.println("Interrupted");
           }
           finally {
               lock.unlock();
           }
           
       }
   }
   
   

   private void pickCard(String playerName) {
       if(Status.RUNNING == game.getStatus()) {
           Card curCard = game.getCardList().get(game.getCurrentCardIndex().getAndIncrement());
           // send card to this player
           cardsOfPlayer.get().add(curCard);
//           System.out.println("Send "+playId + " a card " + curCard);

           // calculate the total point of the current player
           Card total = cardsOfPlayer.get().size() > 0
                   ? cardsOfPlayer.get().stream().reduce(new Card(0,"none"), (a, b) -> new Card(a.getPoint() + b.getPoint(),"total"))
                   : new Card(0,"none");

           if(total.getPoint()> game.getWinPoint()) {
             System.out.println("Game Over: "+ playerName + " wins "+ total.getPoint()+" that exceed win point :"+game.getWinPoint()+" " + cardsOfPlayer.get() );

               game.setGameStatus(Status.STOPED);
           }
           else {
               System.out.println(playerName + cardsOfPlayer.get()+" ="+total.getPoint());
           }
           
       }
      
   }
   
}