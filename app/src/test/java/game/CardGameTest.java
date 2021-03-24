/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package game;



import org.junit.Test;


public class CardGameTest {
    
    
    @Test
    public void testSend()   {
    
        // Test Case 1  : input the win Point 40.  
        //The null parameter for cardList will result in auto generate cardList
        CardGame game = new CardGame(40);
        game.setGameName(" 抓的快 ");
        test(game); 
        
        // Test Case 2 :  input the invalid win Point that no one can get this point
        // the current validate logic is open for extension, is only validate again auto generate card List. 
        game = new CardGame( 500);
        test(game); // this is not valid winPoint
        
        
        
        // Test Case 3  : input the win Point 100.  
        //The null parameter for cardList will result in auto generate cardList
        game = new CardGame( 100);
        test(game); 
        
        
    }
    
    private void test(CardGame game) {
        game.send();
        
        
        while(game.getStatus() == Status.RUNNING) {
            try {
                Thread.currentThread();
                Thread.sleep(100); 
                // Junit not support multiple thread testing. and i have not add multiple thread testing lib dependency here 
                // this is just a work around, and not recommend in production code testing. 
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
