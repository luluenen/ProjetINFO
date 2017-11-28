package fr.uv1.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.MyCalendar;


public class TestTeam {
	Team t1;

    /**
     * test constructor of Team is correct
     * @throws BadParametersException
     * @throws CompetitionException 
     */
	@Test
    public void testTeam() throws BadParametersException, SubscriberException, CompetitionException {
		
		Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
		Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
    	
		Team t1 = new Team("TOTO", athletes);
		assertTrue(t1.getTeamName().equals("TOTO"));
		assertTrue(t1.getAthletes().equals(new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)))));
    }
    
	
    /**
     * test constructor of team when teamName is null
     * @throws BadParametersException
     * @throws SubscriberException
     * @throws CompetitionException 
     */
	@Test(expected =BadParametersException.class)
    public void testTeamNullName() throws BadParametersException, SubscriberException, CompetitionException {
		Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
		Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
		t1 = new Team(null, athletes);
    }
	
	
    /**
     * test constructor of team when the list of athletes is null
     * @throws BadParametersException
     * @throws SubscriberException
     * @throws CompetitionException 
     */
	@Test(expected =BadParametersException.class)
    public void testTeamNullList() throws BadParametersException, SubscriberException, CompetitionException {
		t1 = new Team("toto", null);
    }
   
	
    /**
     * test team with less than 2 athletes 
     * @throws BadParametersException
     * @throws SubscriberException
     * @throws CompetitionException
     */
	@Test (expected =CompetitionException.class)
    public void testTeamNoValidList() throws BadParametersException, SubscriberException, CompetitionException {
		Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1)));
		t1 = new Team("toto", athletes);
    }
    
	
    /**
     * test team with no valid name
     * include the character illegal
     * @throws BadParametersException
     * @throws SubscriberException
     * @throws CompetitionException
     */
	@Test (expected =BadParametersException.class)
    public void testTeamNoValidName() throws BadParametersException, SubscriberException, CompetitionException {
		Athlete comptr1 = new Athlete("Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete("GUO", "Jiao",new MyCalendar(1995,8,9));
		Athlete comptr3 = new Athlete("CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1,comptr2, comptr3)));
		t1 = new Team("2##", athletes);
    }
	
	

    /**
     * test add athlete to the team correct
     * @throws BadParametersException
     * @throws SubscriberException
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test 
    public void testAddAthletes() throws BadParametersException, SubscriberException, ExistingCompetitorException, CompetitionException {
    	
		Athlete comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2)));
		Team myTeam = new Team("TOTO", athletes);
	
		Athlete comptr = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
				
		myTeam.addMember(comptr);
		assertTrue(myTeam.getAthletes().get(2).equals(comptr));

    }
    

    
    /**
     * test add a Competitor who has already been in the list. 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test (expected =ExistingCompetitorException.class)
    public void testAddAthletesHasCompetitor() throws ExistingCompetitorException, BadParametersException, CompetitionException {
		Athlete comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
		Athlete comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
		t1 = new Team("TOTO", athletes);
		t1.addMember(comptr3);
	} 
	
 
    /**
     * test delete a Competitor . 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    @Test
    public void testDeleteMember() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		Athlete comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
		Athlete comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
		t1 = new Team("TOTO", athletes);
		t1.deleteMember(comptr3);
	} 
 
    
    
    /**
     * test delete a athlete who is not in the list. 
     * @param competitor
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws CompetitionException
     */
    
    @Test (expected =ExistingCompetitorException.class)
    public void testDeleteMemberNoExisting() throws ExistingCompetitorException, BadParametersException, CompetitionException {
    	
		Athlete comptr1 = new Athlete(1,"Zhao", "Lu",new MyCalendar(1995,8,9));
		Athlete comptr2 = new Athlete(2,"GUO", "Jiao",new MyCalendar(1995,8,9));
		Athlete comptr3 = new Athlete(3,"CHEN", "William",new MyCalendar(1995,8,9));
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2)));
		t1 = new Team("TOTO", athletes);
		t1.deleteMember(comptr3);
	} 
    
    
		

}
