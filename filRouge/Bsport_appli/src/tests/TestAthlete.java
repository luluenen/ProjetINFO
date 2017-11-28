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
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.utils.MyCalendar;

public class TestAthlete {
	
	private Athlete a1;
	
    /**
     * test constructor of competitor is correct
     * Athlete(String familyName, String firstName)
     * @throws BadParametersException
     * @throws SubscriberException
     */
	@Test
    public void testAthelet() throws BadParametersException, SubscriberException {
    	a1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9)); 
    	assertTrue(a1.getFirstName().equals("Lu"));
    	assertTrue(a1.getFamilyName().equals("Zhao"));	
    }
   
	
	/**
     * test athlete when his familyName is null
     * @throws BadParametersException
     * @throws SubscriberException
     */
	@Test(expected = BadParametersException.class)
    public void testAtheletNullFamilyname() throws BadParametersException, SubscriberException {
    	a1 = new Athlete(null, "Lu",new MyCalendar(1995,8,9)); 
    }
   
	
	/**
     * test athlete when his fistName is null
     * @throws BadParametersException
     * @throws SubscriberException
     */
	 @Test(expected = BadParametersException.class)
    public void testAtheletNullFirstname() throws BadParametersException, SubscriberException {
    	a1 = new Athlete("ZHAO", null,new MyCalendar(1995,8,9)); 
    }
	 
	 /**
	  * test athlete when his name is valid
	  * @throws BadParametersException
	  * @throws SubscriberException
	  */	
	 @Test 
	public void testAtheletNoValidname4() throws BadParametersException, SubscriberException {
		a1= new Athlete("Z-HA O", "L -u",new MyCalendar(1995,8,9));	   
	}
		 
		
	/**
	 * test athlete when his name is not valid ,in this three situation,throw BadParametersException
	 * @throws BadParametersException
	 * @throws SubscriberException
	 */
	@Test(expected = BadParametersException.class)
	public void testAtheletNoValidname1() throws BadParametersException, SubscriberException {
		a1 = new Athlete("ZHAO_ Lu", "Lu",new MyCalendar(1995,8,9));   
	   
	}
	@Test(expected = BadParametersException.class)
	public void testAtheletNoValidname2() throws BadParametersException, SubscriberException {
		a1 = new Athlete("0ZHAO", "Lu",new MyCalendar(1995,8,9));  
	}
	@Test(expected = BadParametersException.class)
	public void testAtheletNoValidname3() throws BadParametersException, SubscriberException {
		a1= new Athlete("0ZHAO", "Lu",new MyCalendar(1995,8,9));
	   
	}

	 
    /**
     * check athlete is in the competition when he is a athlete without the team
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test
    public void testIsInCompetitionOfAthlete() throws BadParametersException, CompetitionException{
    	MyCalendar date = new MyCalendar(2016,8,24);
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		Competition co1 = new Competition("coupeFrance", date, competitors);
    	assertTrue(comptr1.isInCompetition(co1));
    }
	
    /**
     * check athlete is in the competition when he belongs to a team 
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test
    public void testIsInCompetitionOfTeam() throws BadParametersException, CompetitionException{
    	MyCalendar date = new MyCalendar(2016,8,24);
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	Athlete comptr4 = new Athlete("XIAO", "Wang",new MyCalendar(1995,8,9));
    	Athlete comptr5 = new Athlete("XIAO", "Hong",new MyCalendar(1995,8,9));
    	Athlete comptr6 = new Athlete("XIAO", "Hua",new MyCalendar(1995,8,9));
    
    	Team team1 = new Team ("hongxing",new ArrayList <Athlete>(Arrays.asList(comptr1, comptr2, comptr3)));
    	Team team2 = new Team ("hongxing",new ArrayList <Athlete>(Arrays.asList(comptr4, comptr5, comptr6)));
   	
		Competition co1 = new Competition("coupeFrance1", date, new ArrayList <Competitor>(Arrays.asList(team1,team2)));
    	assertTrue(comptr1.isInCompetition(co1));
    }
	
	  /**
     * check athlete is not  in the competition when he belongs to a team 
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test
    public void testIsNotInCompetitionOfTeam() throws BadParametersException, CompetitionException{
    	MyCalendar date = new MyCalendar(2016,8,24);
    	
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	
    	Athlete comptr4 = new Athlete("XIAO", "Wang",new MyCalendar(1995,8,9));
    	Athlete comptr5 = new Athlete("XIAO", "Hong",new MyCalendar(1995,8,9));
    	Athlete comptr6 = new Athlete("XIAO", "Hua",new MyCalendar(1995,8,9));
    	Athlete comptr7 = new Athlete("XIAO", "Huan",new MyCalendar(1995,8,9));
    	
    	Team team1 = new Team ("hongxing",new ArrayList <Athlete>(Arrays.asList(comptr7, comptr2, comptr3)));
    	Team team2 = new Team ("hongxing",new ArrayList <Athlete>(Arrays.asList(comptr4, comptr5, comptr6)));

    
		Competition co3 = new Competition("coupeFrance3", date, new ArrayList <Competitor>(Arrays.asList(team1,team2)));
    	assertTrue(comptr1.isInCompetition(co3) == false);
    }
	
	
    /**
     * check athlete is in not the competition when he is a athlete without a team.
     * function return false
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test 
    public void testIsNotInCompetition() throws BadParametersException, CompetitionException{
    	MyCalendar d1 = new MyCalendar(2016,8,24);
    	Competitor comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Competitor comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Competitor comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	Competitor comptr4 = new Athlete("BON", "Jerome",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr4, comptr2, comptr3)));
		Competition co1 = new Competition("dodo", d1, competitors1);
    	assertTrue(comptr1.isInCompetition(co1)== false);
    }
	
    
    /**
     * test add a competition by the manager after the creation of this competition
     * throw IllegalArgumentException
     * @throws BadParametersException
     * @throws CompetitionException
     */
	 @Test(expected = IllegalArgumentException.class)
    public void testAddCompetition() throws BadParametersException, CompetitionException{
    	MyCalendar d1 = new MyCalendar(2016,8,24);
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
		Competition co1 = new Competition("coupeFrance5", d1, new ArrayList<Competitor>((Arrays.asList(comptr1,comptr2, comptr3))));
    	comptr1.addCompetition(co1);
    	assertTrue(comptr1.getCompetitons().contains(co1));
    }
	
	
	
    /**
     * test delete a competition in the list of competitions 
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test
    public void testDeleteCompetition() throws BadParametersException, CompetitionException{
    	MyCalendar d1 = new MyCalendar(2016,8,24);
    	MyCalendar d2 = new MyCalendar(2016,8,26);
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		Competition co1 = new Competition("dodo1", d1, competitors1);
		Competition co2 = new Competition("dodo2", d2, competitors1);
    	comptr1.deleteCompetition(co1);
    	assertTrue(comptr1.getCompetitons().equals(new ArrayList<Competition>((Arrays.asList(co2)))));
    }
	
	
    /**
     * test delete a competition which is not in the list of competitions 
     * @throws BadParametersException
     * @throws CompetitionException
     */
	@Test(expected =IllegalArgumentException.class)
    public void testDeleteCompetitionHasNotCompetion() throws BadParametersException, CompetitionException{
    	MyCalendar d2 = new MyCalendar(2016,8,26);
    	Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
    	Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Competitor> competitors2  = new ArrayList<Competitor>((Arrays.asList(comptr2, comptr3)));
		Competition co2 = new Competition("dodo2", d2, competitors2);
    	comptr1.deleteCompetition(co2);
    }
    
    
				

}
