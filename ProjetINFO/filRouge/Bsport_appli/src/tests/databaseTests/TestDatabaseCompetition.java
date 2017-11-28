package fr.uv1.tests.databaseTests;

import static org.junit.Assert.assertTrue;

import java.awt.List;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.database.AthleteManager;
import fr.uv1.database.CompetitionManager;
import fr.uv1.database.SubscribersManager;
import fr.uv1.utils.MyCalendar;

public class TestDatabaseCompetition {
	
	Competition competition = null;
	Competition competitionOnDB = null;
	
	@Test
	public void testPersistCompetition() throws BadParametersException, SQLException, CompetitionException, ExistingCompetitorException {
		
		MyCalendar dateBirth = new MyCalendar(1995,8,24);
//    	// create some athletes
    	Athlete comptr1 = new Athlete("zhao", "Lu", dateBirth);
    	Athlete comptr2 = new Athlete("Guo", "Jiao", dateBirth);
    	Athlete comptr3 = new Athlete("CHEN", "William", dateBirth);
//    	// insert these athletes in database
    	AthleteManager.persist(comptr1);
    	AthleteManager.persist(comptr2);
    	AthleteManager.persist(comptr3);

    	
    	// create lists of competitors for competition
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
 
    	// create competitions
	    MyCalendar date = new MyCalendar(2016,9,24);
    	competition = new Competition("ppppp", date, competitors1);
    	
    	CompetitionManager.persist(competition);
        competitionOnDB = CompetitionManager.findByCompName("ppppp");
        
        assertTrue(competitionOnDB.getName().equals(competition.getName()));
        assertTrue(competitionOnDB.getClosingDate().equals(competition.getClosingDate()));
        assertTrue(CompetitionManager.findCompetitors(competitionOnDB.getName()).equals(competition.getCompetitors()));
        
	}

	
	
	@Test
	public void testFindByCompName() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException   {
		
        competitionOnDB = CompetitionManager.findByCompName("uoooyu");
        assertTrue(competitionOnDB == null);
	}
	
	
	@Test
	public void testUpdateCompetition() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException  {
		
        competitionOnDB = CompetitionManager.findByCompName("ppppp");
        System.out.println(competitionOnDB.toString());
        competitionOnDB.setIsSold();
        System.out.println(competitionOnDB.getIsSold());
        CompetitionManager.update(competitionOnDB);
        System.out.println(CompetitionManager.findByCompName("ppppp").getIsSold());
        assertTrue(CompetitionManager.findByCompName("ppppp").getIsSold() == 1);
        
	}
	
	
	/**
	 * test updateRanking for not draw result
	 *  
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException
	 * @throws ExistingCompetitorException
	 */
	@Test
	public void testSetRanking() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException   {
		
		MyCalendar dateBirth = new MyCalendar(1995,8,24);
//    	// create some athletes
    	Athlete comptr1 = new Athlete("zhao", "Lu", dateBirth);
    	Athlete comptr2 = new Athlete("Guo", "Jiao", dateBirth);
    	Athlete comptr3 = new Athlete("CHEN", "William", dateBirth);
//    	// insert these athletes in database
    	AthleteManager.persist(comptr1);
    	AthleteManager.persist(comptr2);
    	AthleteManager.persist(comptr3);

    	
    	// create lists of competitors for competition
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
 
    	// create competitions
	    MyCalendar date = new MyCalendar(2016,9,24);
    	competition = new Competition("hhhhh", date, competitors1);
    	
    	CompetitionManager.persist(competition);
		
        competitionOnDB = CompetitionManager.findByCompName("hhhhh");
        ArrayList<Competitor> competitors = CompetitionManager.findCompetitors(competitionOnDB.getName());

        ArrayList <Competitor> rankingList = new ArrayList<Competitor>((Arrays.asList(comptr2, comptr3, comptr1)));
        competitionOnDB.setRankingList(rankingList);
        competitionOnDB.setResultType(2);
        System.out.println(competitionOnDB.getResultType());
        
        System.out.println(competitionOnDB.getRankCompetitor(comptr1));
        System.out.println(competitionOnDB.getRankCompetitor(comptr2));
        System.out.println(competitionOnDB.getRankCompetitor(comptr3));
        
        CompetitionManager.setRank(competitionOnDB);
        assertTrue(CompetitionManager.getRankingCompetitor(comptr1.getId(), "hhhhh") == 3);
        assertTrue(CompetitionManager.getRankingCompetitor(comptr2.getId(), "hhhhh") == 1);
        assertTrue(CompetitionManager.getRankingCompetitor(comptr3.getId(), "hhhhh") == 2);
        
	}
	
	@Test
	public void testDeleteCompetition() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException, SubscriberException {
		competitionOnDB = CompetitionManager.findByCompName("ppppp");
		CompetitionManager.delete(competitionOnDB);
	}
			
}		
			
			
			
		
		
	    	
	    	
	  



