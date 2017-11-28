package fr.uv1.tests;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

import fr.uv1.bettingServices.bets.DrawBet;
import fr.uv1.bettingServices.bets.PodiumBet;
import fr.uv1.bettingServices.bets.WinnerBet;
import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.exceptions.AuthentificationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.MyCalendar;

/**
 * 
 * @author sjerbi + tmarcuzz <br>
 * <br>
 *         This class declares all the tests for Subscriber. <br>
 * 
 */

public class SubscriberTest {

    private Subscriber subs;
    private static int PWD_LENGTH = 8;
       
    @Test
    public void testSubscriber() throws BadParametersException,

            SubscriberException {
        subs = new Subscriber("Marcuzzi","Tom-Tom","totomat",new MyCalendar(1995,8,9));
        assertTrue(subs.getFirstName().equals("Tom-Tom"));
        assertTrue(subs.getFamilyName().equals("Marcuzzi"));
        assertTrue(subs.getUsername().equals("totomat"));
        assertTrue(subs.getPassword().length() == PWD_LENGTH);
        assertTrue(subs.correctPasswordFormat(subs.getPassword()));

    }
    
    /**
     * Check null firstname
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberNullFirstName() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi",null,"totomat",new MyCalendar(1995,8,9));
    }
    
    /**
     * Check null lastname
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberNullLastName() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber(null,"Tom","totomat",new MyCalendar(1995,8,9));
    }
    
    /**
     * Check null username
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberNullUsername() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi","Tom",null,new MyCalendar(1995,8,9));
    }
    
    /**
     * Check null birthDate
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberNullBirthdate() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi","Tom","totomat",null);
    }
    
    /**
     * Check invalid format for firstname
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberInvalidFirstName() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi","T","totomat",new MyCalendar(1995,8,9));
    }
    
    /**
     * Check invalid format for lastname
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberInvalidLastName() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi7","Tom","totomat",new MyCalendar(1995,8,9));
    }
    
    /**
     * Check invalid format for username
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberInvalidUsername() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi","Tom","&és",new MyCalendar(1995,8,9));
    }
    
    /**
     * Check username too short
     */
    @Test(expected = BadParametersException.class)
    public void testSubscriberUsernameTooShort() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi","Tom","tot",new MyCalendar(1995,8,9));
    }
   
    /**
    * Check the change of the password
    */
   @Test
   public void testChangePwdSubs() throws BadParametersException,
           SubscriberException, AuthentificationException {
       subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
       subs.changeSubscriberPassword("ABcDEd45", subs.getPassword());
       assertTrue((subs.getPassword().equals("ABcDEd45")));
   }
   
   /**
    * Check a new password too short
    */
   @Test(expected = BadParametersException.class)
   public void testChangePwdSubsTooShort() throws BadParametersException,
           SubscriberException, AuthentificationException {
       subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
       subs.changeSubscriberPassword("ABcD", subs.getPassword());
   }
   
   /**
    * Check a new password too long
    */
   @Test(expected = BadParametersException.class)
   public void testChangePwdSubsTooLong() throws BadParametersException,
           SubscriberException, AuthentificationException {
       subs = new Subscriber("Marcuzzi","Tom","&és",new MyCalendar(1995,8,9));
       subs.changeSubscriberPassword("ABcDaGe789", subs.getPassword());
   }
   
   /**
    * Check a new password invalid
    */
   @Test(expected = BadParametersException.class)
   public void testChangePwdSubsInvalid() throws BadParametersException,
           SubscriberException, AuthentificationException {
       subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
       subs.changeSubscriberPassword("AB++//89", subs.getPassword());
   }
   
   /**
    * Check with a wrong login
    */
   @Test(expected = AuthentificationException.class)
   public void testChangePwdSubsWrong() throws BadParametersException,
           SubscriberException, AuthentificationException {
       subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
       subs.changeSubscriberPassword("AB++//89", "ABasce89");
   }
     
    /**
     * Check the age limit 
     */
    @Test(expected = SubscriberException.class)
    public void testUnderAgeSubscriber() throws BadParametersException,
            SubscriberException {
        subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(MyCalendar.getDate().get(Calendar.YEAR)-16,
        		MyCalendar.getDate().get(Calendar.MONTH)+1,
        		MyCalendar.getDate().get(Calendar.DAY_OF_MONTH)));
    }
    
    /**
     *  Check if the balance is correct after debit/credit
     */
    @Test
    public void testAddRemoveCoins() throws BadParametersException, SubscriberException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	subs.addRemoveCoins(10);
    	assertTrue((subs.getBalance()==10));
    	subs.addRemoveCoins(-10);
    	assertTrue((subs.getBalance()==0));    
    }
    
    /**
     *  Check exception if the balance of the subscriber is negative
     */
    @Test(expected = SubscriberException.class)
    public void testNegativeBalance() throws BadParametersException, SubscriberException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	subs.addRemoveCoins(-10);   
    }
    
    /**
     *  Check equal subscribers
     */
    @Test
    public void testEqualSubscribers() throws BadParametersException, SubscriberException{
    	Subscriber subs1 = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	Subscriber subs2 = new Subscriber("Marc","Tom","totomat",new MyCalendar(1995,8,9));
    	assertTrue((subs1.equals(subs2))); 
    	assertTrue(((subs1.hashCode()==subs2.hashCode())));
    }
    
    /**
     *  Check add WinnerBet
     * @throws SQLException 
     *
     */
    @Test
    public void testWinnerBet() throws BadParametersException, SubscriberException, CompetitionException, AuthentificationException, SQLException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	subs.addRemoveCoins(20);
    	
    	ArrayList<Competitor> comptrs = new ArrayList<Competitor>(Arrays.asList(comptr1, comptr2, comptr3));
    			
    	Competition comp = new Competition("Coupedefrance", new MyCalendar(2016, 
    			MyCalendar.getDate().get(Calendar.MONTH)+1, MyCalendar.getDate().get(Calendar.DAY_OF_MONTH)+1), comptrs);
    	
    	ArrayList<Competitor> winners = new ArrayList<Competitor>(Arrays.asList(comptr2));    	
    	
    	subs.bet(MyCalendar.getDate(), comp, winners, subs.getPassword(), 10);
    	
    	assertTrue(subs.listBets().size()==1);
    	assertTrue(subs.getBalance()==10);
    	assertTrue(subs.listBets().get(0).getClass()==WinnerBet.class);
    	
    }
    
    /**
     *  Check add PodiumBet
     * @throws SQLException 
     *
     */
    @Test
    public void testPodiumBet() throws BadParametersException, SubscriberException, CompetitionException, AuthentificationException, SQLException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	subs.addRemoveCoins(20);
    	
    	ArrayList<Competitor> comptrs = new ArrayList<Competitor>(Arrays.asList(comptr1, comptr2, comptr3));
    			
    	Competition comp = new Competition("Coupedefrance", new MyCalendar(2016, 
    			MyCalendar.getDate().get(Calendar.MONTH)+1, MyCalendar.getDate().get(Calendar.DAY_OF_MONTH)+1), comptrs);
    	
    	ArrayList<Competitor> winners = new ArrayList<Competitor>(Arrays.asList(comptr2,comptr1,comptr3));    	
    	
    	subs.bet(MyCalendar.getDate(), comp, winners, subs.getPassword(), 10);
    	
    	assertTrue(subs.listBets().size()==1);
    	assertTrue(subs.getBalance()==10);	
    	assertTrue(subs.listBets().get(0).getClass()==PodiumBet.class);

    	
    }
    
    /**
     *  Check add DrawBet
     * @throws SQLException 
     *
     */
    @Test
    public void testDrawBet() throws BadParametersException, SubscriberException, CompetitionException, AuthentificationException, SQLException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	subs.addRemoveCoins(20);
    	
    	ArrayList<Competitor> comptrs = new ArrayList<Competitor>(Arrays.asList(comptr1, comptr2, comptr3));
    			
    	Competition comp = new Competition("Coupe_de_france", new MyCalendar(2016, MyCalendar.getDate().get(Calendar.MONTH)+1,
    			MyCalendar.getDate().get(Calendar.DAY_OF_MONTH)+1), comptrs);
    	
    	ArrayList<Competitor> winners = new ArrayList<Competitor>(Arrays.asList(comptr2,comptr1));    	
    	
    	subs.bet(MyCalendar.getDate(), comp, winners, subs.getPassword(), 10);
    	
    	assertTrue(subs.listBets().size()==1);
    	assertTrue(subs.getBalance()==10);
    	assertTrue(subs.listBets().get(0).getClass()==DrawBet.class);
    	
    }
    
    /**
     *  Check for a number of bet
     * @throws SQLException 
     *
     */
    @Test
    public void testBet() throws BadParametersException, SubscriberException, CompetitionException, AuthentificationException, SQLException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	subs.addRemoveCoins(30);
    	
    	ArrayList<Competitor> comptrs = new ArrayList<Competitor>(Arrays.asList(comptr1, comptr2, comptr3));
    			
    	Competition comp = new Competition("Coupe_de_france", new MyCalendar(2016, MyCalendar.getDate().get(Calendar.MONTH)+1,
    			MyCalendar.getDate().get(Calendar.DAY_OF_MONTH)+1), comptrs);
    	
    	ArrayList<Competitor> winners = new ArrayList<Competitor>(Arrays.asList(comptr2,comptr1));  	
    	subs.bet(MyCalendar.getDate(), comp, winners, subs.getPassword(), 10);
    	
    	ArrayList<Competitor> winners1 = new ArrayList<Competitor>(Arrays.asList(comptr2,comptr1,comptr3));  	
    	subs.bet(MyCalendar.getDate(), comp, winners1, subs.getPassword(), 10);
    	
    	ArrayList<Competitor> winners2 = new ArrayList<Competitor>(Arrays.asList(comptr2));  	
    	subs.bet(MyCalendar.getDate(), comp, winners2, subs.getPassword(), 10);
    	
    	assertTrue(subs.listBets().size()==3);
    	assertTrue(subs.getBalance()==0);
    	assertTrue(subs.listBets().get(0).getClass()==DrawBet.class);
    	assertTrue(subs.listBets().get(1).getClass()==PodiumBet.class);
    	assertTrue(subs.listBets().get(2).getClass()==WinnerBet.class);
    	
    }
    
    /**
     *  Check for SubscriberException if there is not enough coins for all the bets
     * @throws SQLException 
     *
     */
    @Test(expected = SubscriberException.class)
    public void testBetNotEnoughCoins() throws BadParametersException, SubscriberException, CompetitionException, AuthentificationException, SQLException{
    	subs = new Subscriber("Marcuzzi","Tom","totomat",new MyCalendar(1995,8,9));
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	subs.addRemoveCoins(20);
    	
    	ArrayList<Competitor> comptrs = new ArrayList<Competitor>(Arrays.asList(comptr1, comptr2, comptr3));
    			
    	Competition comp = new Competition("Coupe_de_france", new MyCalendar(2016, MyCalendar.getDate().get(Calendar.MONTH)+1,
    			MyCalendar.getDate().get(Calendar.DAY_OF_MONTH)+1), comptrs);
    	
    	ArrayList<Competitor> winners = new ArrayList<Competitor>(Arrays.asList(comptr2,comptr1)); 
    	
    	subs.bet(MyCalendar.getDate(), comp, winners, subs.getPassword(), 10);
    	
    	ArrayList<Competitor> winners1 = new ArrayList<Competitor>(Arrays.asList(comptr2,comptr1,comptr3));  	
    	subs.bet(MyCalendar.getDate(), comp, winners1, subs.getPassword(), 10);
    	
    	ArrayList<Competitor> winners2 = new ArrayList<Competitor>(Arrays.asList(comptr2));  	
    	subs.bet(MyCalendar.getDate(), comp, winners2, subs.getPassword(), 10);
    	    	
    }
    
    

    
    


}