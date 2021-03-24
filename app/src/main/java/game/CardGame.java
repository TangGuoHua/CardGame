package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represent 54 card deck.
 * I has  
 * @author Guohua
 *
 */
public class CardGame {
    private  volatile Status gameStatus = Status.RUNNING; 
    private  volatile  AtomicInteger currentCardIndex = new AtomicInteger(0);
    private String gameName = "";
    private List<Card> cardList = null;
    private int winPoint = 0;
    
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public AtomicInteger getCurrentCardIndex() {
        return currentCardIndex;
    }
    public void setCurrentCardIndex(AtomicInteger currentCardIndex) {
        this.currentCardIndex = currentCardIndex;
    }
    public Status getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(Status gameStatus) {
        this.gameStatus = gameStatus;
    }
    public List<Card> getCardList() {
        return cardList;
    }

    public int getWinPoint() {
        return winPoint;
    }
    public void setWinPoint(int winPoint) {
        this.winPoint = winPoint;
    }
    
    public CardGame( int winPoint) {
        this.winPoint = winPoint;
    }
    public Status getStatus() {
        return gameStatus;
    } 
    
    
    public  void send() {
        if( validateWinPoint()) {
            System.out.println("please input a valid win point between 1 and 134");
            gameStatus = Status.STOPED;
            return; 
        }
        if(isValidCardList()) {
            cardList = shuffleCard();
        }
        if("".equals(gameName)) {
            this.gameName = Thread.currentThread().getName();
        }
        
        Lock lock = new ReentrantLock();
        Condition lockOfPlayOne = lock.newCondition(), lockOfPlayTwo = lock.newCondition(), lockOfPlayThree = lock.newCondition();
        CountDownLatch latch = new CountDownLatch(3);
        new Player(lock, lockOfPlayThree, lockOfPlayOne, "张三", latch, this).start();
        new Player(lock, lockOfPlayOne, lockOfPlayTwo, "李四", latch, this).start();
        new Player(lock, lockOfPlayTwo, lockOfPlayThree, "王五", latch, this).start();
        try {
            latch.await(); // wait all player's arrival. 
            lock.lock();
            lockOfPlayThree.signalAll(); 
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
    }
    public boolean validateWinPoint() { // this validation logic is open for extension.
        return winPoint > 134 || winPoint < 1;
    }

    public boolean isValidCardList() {  // this validation logic is open for extension 
        return cardList == null ;
    }
    
    private  ArrayList<Card> shuffleCard(){
        ArrayList<Card> cardList = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "A");
        map.put(11, "J");
        map.put(12, "Q");
        map.put(13, "K");
        map.put(20, "JOKE");
        System.out.println("开始牌局 ： "+ gameName);
        Card[] cardArray = new Card[54];
        int id = 0;
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j <= 4; j++) {
                if(map.containsKey(i)) {
                    cardArray[id++] = new Card(i,map.get(i));
                }
                else {
                    cardArray[id++] = new Card(i,String.valueOf(i));
                }
                
            }
        }
        cardArray[id++] = new Card(20,map.get(20));
        cardArray[id++] = new Card(20,map.get(20));;
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            swap(cardArray, r.nextInt(53), r.nextInt(53));
        }
        for (Card card : cardArray) {
            cardList.add(card);
        }
        System.out.println(cardList);
        return cardList;
    }

    private static void swap(Card[] cards, int i, int j) {
        Card tmp = cards[i];
        cards[i] = cards[j];
        cards[j] = tmp;
    }


}




class Card {
    private int point;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Card(int point, String name) {
        this.point = point;
        this.name = name;
    }

    public String toString() {// we can extends this method to meeting different display requirements
        return name;
    }
}

enum Status {
    RUNNING, STOPED
}


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
   
   

   private void pickCard(String playId) {
       if(Status.RUNNING == game.getStatus()) {
           Card curCard = game.getCardList().get(game.getCurrentCardIndex().getAndIncrement());
           // send card to this player
           cardsOfPlayer.get().add(curCard);
           System.out.println("发给 "+playId + " 一张 " + curCard);

           // calculate the total point of the current player
           Card total = cardsOfPlayer.get().size() > 0
                   ? cardsOfPlayer.get().stream().reduce(new Card(0,"none"), (a, b) -> new Card(a.getPoint() + b.getPoint(),"total"))
                   : new Card(0,"none");

           if(total.getPoint()> game.getWinPoint()) {
             System.out.println("游戏结束 "+ playId + "赢得比赛"+game.getGameName()+", 得分:" + cardsOfPlayer.get() + " = " + total.getPoint());

               game.setGameStatus(Status.STOPED);
           }
       }
      
   }
   
}