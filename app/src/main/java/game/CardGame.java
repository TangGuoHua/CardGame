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
 * @author Guohua
 */
public class CardGame {
    // this field represent game status, stopped when one of players won the game, otherwise running.
    private  volatile Status gameStatus = Status.RUNNING; 
    // this field indicate which card currently being sent.
    private  volatile  AtomicInteger currentCardIndex = new AtomicInteger(0);
    private String gameName = ""; // we can give this game a name, default the thread name.
    // 54 cards will be initialized and store in this field.
    private List<Card> cardList = null;
    // win points, default to 50;
    private int winPoint = 50;
    
    // default name added for three player, and we can change it via set method.
    private String playerOne="SpiderMan";
    private String playerTwo="IronMan";
    private String playerThree="AmericanCaptain";
    
    public String getPlayerOne() {
        return playerOne;
    }
    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }
    public String getPlayerTwo() {
        return playerTwo;
    }
    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }
    public String getPlayerThree() {
        return playerThree;
    }
    public void setPlayerThree(String playerThree) {
        this.playerThree = playerThree;
    }

    
    
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
        if(!isCardListInitialized()) {
            cardList = shuffleCard();
        }
        if("".equals(gameName)) {
            this.gameName = Thread.currentThread().getName();
        }
        System.out.println("Begin Game ： "+ gameName);
        System.out.println(cardList);

        Lock lock = new ReentrantLock();
        Condition lockOfPlayOne = lock.newCondition(), lockOfPlayTwo = lock.newCondition(), lockOfPlayThree = lock.newCondition();
        CountDownLatch latch = new CountDownLatch(3);
        new Player(lock, lockOfPlayThree, lockOfPlayOne, playerOne, latch, this).start();
        new Player(lock, lockOfPlayOne, lockOfPlayTwo, playerTwo, latch, this).start();
        new Player(lock, lockOfPlayTwo, lockOfPlayThree, playerThree, latch, this).start();
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

    public boolean isCardListInitialized() {  // this validation logic is open for extension 
        return cardList!=null && cardList.size() == 54 ;
    }
    
    private  ArrayList<Card> shuffleCard(){
        ArrayList<Card> cardList = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "A");
        map.put(11, "J");
        map.put(12, "Q");
        map.put(13, "K");
        map.put(20, "JOKE");
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
        return cardList;
    }

    private static void swap(Card[] cards, int i, int j) {
        Card tmp = cards[i];
        cards[i] = cards[j];
        cards[j] = tmp;
    }


}

