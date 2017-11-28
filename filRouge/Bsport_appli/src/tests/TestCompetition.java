package fr.uv1.tests;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.utils.MyCalendar;
/**
 * @author ZHAO Lu and GUO Jiao
 *
 */
public class TestCompetition {
	
	
	private Competition co1;
	
    /**
     * test constructor of competition correct for the competition of athletes
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test
    public void testCompetitionAthletes() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition("coupeFrance", d1, competitors1);
		
		assertTrue(co1.getName().equals("coupeFrance"));
		assertTrue(co1.getClosingDate().equals(new MyCalendar(2016,8,24)));
		assertTrue(co1.getCompetitors().equals(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)))));
	
	}
	
	/**
     * test constructor of competition correct for the competition of teams
     * @throws BadParametersException
     * @throws CompetitionException
     */
    public void testCompetitionTeams() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Athlete comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	
    	MyCalendar d2 = new MyCalendar(2016,8,24);
    	Athlete comptr4 = new Athlete(4,"Hua", "Zhu",new MyCalendar(1995,8,9));
    	Athlete comptr5 = new Athlete(5,"Xiao", "Hong",new MyCalendar(1995,8,9));
    	Athlete comptr6 = new Athlete(6,"Xiao", "Feng",new MyCalendar(1995,8,9));
    	
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
    	ArrayList<Competitor> competitors2  = new ArrayList<Competitor>((Arrays.asList(comptr4, comptr5, comptr6)));
    	
    	co1 = new Competition("coupeFrance1", d1, competitors1);
    	Competition co2 = new Competition("coupeFrance2", d2, competitors2);
    	
		assertTrue(co1.getClosingDate().equals(new MyCalendar(2016,8,24)));
		assertTrue(co1.getCompetitors().equals(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)))));
		assertTrue(co2.getName().equals("coupeFrance2"));
		assertTrue(co2.getCompetitors().equals(new ArrayList<Competitor>((Arrays.asList(comptr4, comptr5, comptr6)))));
    }
	
	
    /**
     * test null name
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = BadParametersException.class)
    public void testCompetitionNullName() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition(null, d1, competitors1);
	}
    
    /**
     * test null closingDate
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = BadParametersException.class)
    public void testCompetitionNullClosingDate() throws BadParametersException, CompetitionException {
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition("dodoee", null, competitors1);
	}
    

    /**
     * test null list of competitors
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = BadParametersException.class)
    public void testCompetitionNullListCompetitors() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
		co1 = new Competition("dodoee", d1, null);
	}
    
    /**
     * check can not add the same athlete into different team in the same competition
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddAthleteTwiceInDifferentTeam() throws BadParametersException, CompetitionException{
    	MyCalendar date = new MyCalendar(2016,8,24);
    	
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	Athlete comptr4 = new Athlete("XIAO", "Wang",new MyCalendar(1995,8,9));
    	Athlete comptr5 = new Athlete("XIAO", "Hong",new MyCalendar(1995,8,9));
    	Athlete comptr6 = new Athlete("XIAO", "Hua",new MyCalendar(1995,8,9));
    	
    	Team team1 = new Team ("hongxing",new ArrayList <Athlete>(Arrays.asList(comptr4, comptr2, comptr3)));
    	Team team2 = new Team ("hongxing",new ArrayList <Athlete>(Arrays.asList(comptr4, comptr5, comptr6)));

    
		Competition co3 = new Competition("coupeFrance3", date, new ArrayList <Competitor>(Arrays.asList(team1,team2)));
    	assertTrue(comptr1.isInCompetition(co3) == false);
    }
	
    
    /**
     * testCompetitionNonValidClosingDate
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = CompetitionException.class)
    public void testCompetitionNonValidClosingDate() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2015,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition("dodo", d1, competitors1);
	}
   
    
    /**
     * test of no valid name
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = BadParametersException.class)
    public void testCompetitionNonValidName() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition("@@#", d1, competitors1);
	}
   
    
    /**
     * test of no valid list of competitors
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = CompetitionException.class)
    public void testCompetitionNonValidList() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1)));
		co1 = new Competition("dodoee", d1, competitors1);
	}
    
    
    /**
     * test of setting resultType correct
     * @param type
     * @throws BadParametersException
     * @throws CompetitionException
     */ 
    public void testSetResultType() throws BadParametersException, CompetitionException {	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.setResultType(2);
		assertTrue(co1.getResultType()==2);
	}  
   
    
    /**
     * test of setting wrong number of type 
     * @param type
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetResultTypeWrongNumber() throws BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.setResultType(4);
	}  
    
    
    /**
     * test setRankList Correct 
     * 
     * @param rankingList
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testSetRankingList() throws BadParametersException, CompetitionException, IllegalArgumentException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Athlete comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	Athlete comptr4 = new Athlete(4,"MARCUZZO", "Tom",new MyCalendar(1995,8,9));
    	
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3,comptr4)));
		Competition co0 = new Competition("coupeFrance6", d1, competitors1);
		co0.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3))));
		assertTrue(co0.getRankingList().equals(new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)))));
	}   
   
    
    /**
     * test of setting rankingList with a competitor who is not in the list of competitors
     * 
     * @param rankingList
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetRankingListNoCompetitor() throws BadParametersException, CompetitionException, IllegalArgumentException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.setResultType(2);
		co1.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr2, comptr3))));
	} 
 
    
    /**
     * test of setting rankingList with less than 3 competitors.
     * 
     * @param rankingList
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetRankingListLessThreeCompetitors() throws BadParametersException, CompetitionException, IllegalArgumentException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	Competitor comptr4 = new Athlete(3,"MARCUZZO", "Tom",new MyCalendar(1995,8,9));
    	
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3,comptr4)));
		co1 = new Competition("dodo", d1, competitors1);
		co1.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr2, comptr3))));
	} 
 
    
    /**
     * test add a Competitor 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testAddCompetitor() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.addCompetitor(comptr3);
	} 
    
    /**
     * test add a Competitor who is already in the list. 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = ExistingCompetitorException.class)
    public void testAddCompetitorExisted
    	() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.addCompetitor(comptr3);
	} 
 
    
    /**
     * test delete a Competitor . 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testDeleteCompetitor() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.deleteCompetitor(comptr3);
	} 
    
    /**
     * test delete a Competitor who is not in the list. 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = ExistingCompetitorException.class)
    public void testDeleteCompetitorNoExisting() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2)));
		co1 = new Competition("dodo", d1, competitors1);
		co1.deleteCompetitor(comptr3);
	} 
    
    /**
     * test competition is not closed . 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testIsClosed() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		assertTrue(co1.isClosed() == false);
	} 
    
    /**
     * test getRankCompetitor correct
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testGetRankCompetitor() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)));
		co1 = new Competition("dodo5", d1, competitors1);
		co1.setResultType(2);
		co1.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr2, comptr1,comptr3))));
		
		assertTrue(co1.getRankCompetitor(comptr1) == 2);
	}  
  
    
    /**
     * test getRankCompetitor with a competitor who is not in competition 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRankCompetitorNonExisting() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.setResultType(2);
		co1.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr2, comptr1))));
		co1.getRankCompetitor(comptr3);
	} 
 
    
    /**
     * test getRankCompetitor while the competition is not finished
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test(expected = IllegalArgumentException.class)
    
    public void testGetRankCompetitorNonResult() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.getRankCompetitor(comptr3);
	} 

    /**
     * test getRankCompetitor if the competition is draw 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testGetRankCompetitorCompetionDraw() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2,comptr3)));
		co1 = new Competition("dodoee", d1, competitors1);
		co1.setResultType(2);
		co1.setRankingList(new ArrayList<Competitor>((Arrays.asList(comptr2, comptr1,comptr3))));
		co1.getRankCompetitor(comptr3);
		assertTrue(co1.getRankCompetitor(comptr1) == 1);
	} 
    
   
}
