package fr.uv1.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.uv1.bettingServices.bets.DrawBet;
import fr.uv1.bettingServices.bets.PodiumBet;
import fr.uv1.bettingServices.bets.WinnerBet;
import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.MyCalendar;

/**
 * @author tmarcuzz and sjerbi
 *
 */

public class BetsTest {

	private Subscriber subs;
    private Competition comp;
    private Competition comp2;
    private Competitor comptr1;
    private Competitor comptr2;
    private Competitor comptr3;
    
    @Before
    public void setUp() throws Exception{
    	try {
	    	subs = new Subscriber("Marcuzzi","Tom-Tom","totomat",new MyCalendar(1995,8,9));;
	    	comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
	    	comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
	    	comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
	    	subs.addRemoveCoins(10000);
	    	ArrayList<Competitor> comptrs = new ArrayList<Competitor>(Arrays.asList(comptr1, comptr2, comptr3));
	    	
			comp = new Competition("dodo5", new MyCalendar(2016,8,24), comptrs);
			comp2 = new Competition("dodo6", new MyCalendar(2016,8,24), comptrs);
		} catch (BadParametersException | CompetitionException | SubscriberException e) {
			e.printStackTrace();
		}
     }
    
    /**
     * Check null competition
     */
    @Test(expected = BadParametersException.class)
    public void testWinnerBetNullCompetition() throws BadParametersException {
        new WinnerBet(null, subs, new MyCalendar(2014,3,5), 9192, comptr2);
    }
    
    /**
     * Check null subscriber
     */
    @Test(expected = BadParametersException.class)
    public void testWinnerBetNullSubscriber() throws BadParametersException {
        new WinnerBet(comp, null, new MyCalendar(2014,3,5), 9192, comptr2);
    }
    
    /**
     * Check null date
     */
    @Test(expected = BadParametersException.class)
    public void testWinnerBetNullDate() throws BadParametersException {
        new WinnerBet(comp, subs, null, 9192, comptr2);
    }
    
    /**
     * Check negative coins
     */
    @Test(expected = BadParametersException.class)
    public void testWinnerBetNegativeCoins() throws BadParametersException {
        new WinnerBet(comp, subs, new MyCalendar(2014,3,5), -9192, comptr2);
    }
    
    /**
     * Check null competitor
     */
    @Test(expected = BadParametersException.class)
    public void testWinnerBetNullCompetitor() throws BadParametersException {
        new WinnerBet(comp, subs, new MyCalendar(2014,3,5), 9192, null);
    }
    
    /**
     * Check null competition
     */
    @Test(expected = BadParametersException.class)
    public void testDrawBetNullCompetition() throws BadParametersException {
        new DrawBet(null, subs, new MyCalendar(2014,3,5), 9192, comptr2, comptr3);
    }
    
    /**
     * Check null subscriber
     */
    @Test(expected = BadParametersException.class)
    public void testDrawBetNullSubscriber() throws BadParametersException {
        new DrawBet(comp, null, new MyCalendar(2014,3,5), 9192, comptr2, comptr3);
    }
    
    /**
     * Check null date
     */
    @Test(expected = BadParametersException.class)
    public void testDrawBetNullDate() throws BadParametersException {
        new DrawBet(comp, subs, null, 9192, comptr2, comptr3);
    }
    
    /**
     * Check negative coins
     */
    @Test(expected = BadParametersException.class)
    public void testDrawBetNegativeCoins() throws BadParametersException {
        new DrawBet(comp, subs, new MyCalendar(2014,3,5), -9192, comptr2, comptr3);
    }
    
    /**
     * Check null competitor
     */
    @Test(expected = BadParametersException.class)
    public void testDrawBetNullCompetitor() throws BadParametersException {
        new DrawBet(comp, subs, new MyCalendar(2014,3,5), 9192, null, comptr3);
        new DrawBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr2, null);
    }
    
    /**
     * Check null competition
     */
    @Test(expected = BadParametersException.class)
    public void testPodiumBetNullCompetition() throws BadParametersException {
        new PodiumBet(null, subs, new MyCalendar(2014,3,5), 9192, comptr1, comptr2, comptr3);
    }
    
    /**
     * Check null subscriber
     */
    @Test(expected = BadParametersException.class)
    public void testPodiumBetNullSubscriber() throws BadParametersException {
        new PodiumBet(comp, null, new MyCalendar(2014,3,5), 9192, comptr1, comptr2, comptr3);
    }
    
    /**
     * Check null date
     */
    @Test(expected = BadParametersException.class)
    public void testPodiumBetNullDate() throws BadParametersException {
        new PodiumBet(comp, subs, null, 9192, comptr1, comptr2, comptr3);
    }
    
    /**
     * Check negative coins
     */
    @Test(expected = BadParametersException.class)
    public void testPodiumBetNegativeCoins() throws BadParametersException {
        new PodiumBet(comp, subs, new MyCalendar(2014,3,5), -9192, comptr1, comptr2, comptr3);
    }
    
    /**
     * Check null competitor
     */
    @Test(expected = BadParametersException.class)
    public void testPodiumBetNullCompetitor() throws BadParametersException {
        new PodiumBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr1, null, comptr3);
        new PodiumBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr1, comptr2, null);
        new PodiumBet(comp, subs, new MyCalendar(2014,3,5), 9192, null, comptr2, comptr3);
    }
    
    /**
     * Test isWon method.
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testWinnerBetisWon() throws BadParametersException, ExistingCompetitorException, CompetitionException {
    	comp.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
    	comp.setResultType(2);
    	comp2.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
    	comp2.setResultType(1);
    	WinnerBet bet1 = new WinnerBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr1);
    	WinnerBet bet2 = new WinnerBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr2);
    	WinnerBet bet3 = new WinnerBet(comp2, subs, new MyCalendar(2014,3,5), 9192, comptr1);
    	try {
			assertTrue(bet1.isWon());
			assertFalse(bet2.isWon());
			assertFalse(bet3.isWon());
		} catch (ExistingCompetitorException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Test isWon method.
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testDrawBetisWon() throws BadParametersException, ExistingCompetitorException, CompetitionException {
    	comp.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
    	comp.setResultType(1);
    	comp2.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
    	comp2.setResultType(2);
    	DrawBet bet1 = new DrawBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr1, comptr2);
    	DrawBet bet2 = new DrawBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr2, comptr3);
    	DrawBet bet3 = new DrawBet(comp2, subs, new MyCalendar(2014,3,5), 9192, comptr1, comptr2);
    	try {
			assertTrue(bet1.isWon());
			assertFalse(bet2.isWon());
			assertFalse(bet3.isWon());
		} catch (ExistingCompetitorException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Test isWon method.
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testPodiumBetisWon() throws BadParametersException, ExistingCompetitorException, CompetitionException {
    	comp.setResultType(2);
    	comp.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
    	System.out.print(comp.getRankCompetitor(comptr3));
    	comp2.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
    	comp2.setResultType(1);
    	PodiumBet bet1 = new PodiumBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr1, comptr2, comptr3);
    	PodiumBet bet2 = new PodiumBet(comp, subs, new MyCalendar(2014,3,5), 9192, comptr2, comptr3, comptr1);
    	PodiumBet bet3 = new PodiumBet(comp2, subs, new MyCalendar(2014,3,5), 9192, comptr1, comptr2, comptr3);
    	try {
			assertTrue(bet1.isWon());
			assertFalse(bet2.isWon());
			assertFalse(bet3.isWon());
		} catch (ExistingCompetitorException e) {
			e.printStackTrace();
		}
    }
}