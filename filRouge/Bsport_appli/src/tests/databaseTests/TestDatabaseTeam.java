package fr.uv1.tests.databaseTests;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.database.AthleteManager;
import fr.uv1.database.CompetitionManager;
import fr.uv1.database.TeamManager;
import fr.uv1.utils.MyCalendar;

public class TestDatabaseTeam {
	
	/**
     * test persist of a team 
     * @throws SQLException 
	 * @throws CompetitionException 
	 * @throws ExistingCompetitorException 
     */
	@Test
	public void testPersistTeam() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
		
		Athlete comptr1 = new Athlete("Zhao", "Lu", new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUO", "Jiao", new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHEN", "William", new MyCalendar(1995,8,24));

		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
		
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
    	Team team = new Team("ZHONGHUA", athletes);
    	TeamManager.persist(team);
    	
        Team teamDatabase = TeamManager.findByTeamId(team.getId()); 
        assertTrue(teamDatabase.getTeamName().equals(team.getTeamName()));
        assertTrue(teamDatabase.getId()==team.getId());
    }
	
	 /**
     * test ExistMemberUseIdCompetitor(idCompetitor)
     * @throws SQLException 
	 * @throws ExistingCompetitorException 
     */

	@Test
	public void testExistMemberUseIdCompetitor() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
		
		Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));

		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
		
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
    	Team team = new Team("ZHONGHUA", athletes );
    	TeamManager.persist(team); 
    	 //System.out.println(teamDatabase.getId());
    	Athlete athleteDatabase = AthleteManager.findByAthleteId(comptr1.getId());  
    	 //System.out.println(athleteDatabase.getId());
        assertTrue(TeamManager.existMemberUseIdAthlete( athleteDatabase.getId()));
    }
	
	 /**
     * test existMemberBetween( idCompetitor,idTeam )
     * @throws SQLException 
	 * @throws ExistingCompetitorException 
     */

	@Test
	public void testExistMemberBetween() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
		
		Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));

		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
		
    	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
    	Team team = new Team("ZHONGHUA", athletes );
    	TeamManager.persist(team);
    	Team teamDatabase = TeamManager.findByTeamId(team.getId()); 
    	Athlete athleteDatabase = AthleteManager.findByAthleteId(comptr1.getId());  
        assertTrue(TeamManager.existMemberBetween( athleteDatabase.getId(),teamDatabase.getId()));
    }
	
	

	 /**
    * test existMemberUseIdTeam( idTeam )
    * @throws SQLException 
	 * @throws ExistingCompetitorException 
    */

	@Test
	public void testExistMemberUseIdTeam() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
		
		Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));

		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
		
   	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
   	Team team = new Team("ZHONGHUA", athletes );
   	TeamManager.persist(team);
   	Team teamDatabase = TeamManager.findByTeamId(team.getId()); 
    assertTrue(TeamManager.existMemberUseIdTeam(teamDatabase.getId()));
   }
	
	  
    /**
     * test find team by id
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testFindByIdTeam() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {

		Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));

		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
		
	   	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
	   	Team team = new Team("ZHONGHUA", athletes );
	   	TeamManager.persist(team);
	   	Team teamDatabase = TeamManager.findByTeamId(team.getId()); 
	    assertTrue(team.equals(teamDatabase));
	    assertTrue(teamDatabase.getTeamName().equals(team.getTeamName()));
	    assertTrue(teamDatabase.getId()==(team.getId()));
    }  
    
    /**
     * test add a member of team in the database.
	 *
	 * @throws SQLException
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testAddTeamMember() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {

		Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));
		Athlete comptr4 = new Athlete("nnn", "mnnn",new MyCalendar(1995,8,24));
		
		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
		AthleteManager.persist(comptr4);
	   	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
	   	
	   	Team team = new Team("ZHONGHUA", athletes );
	   	TeamManager.persist(team);
	   	System.out.println(team.getId());
	   	System.out.println(comptr4.getId());
	   	
		Team teamDatabase = TeamManager.findByTeamId(team.getId()); 
		System.out.println(teamDatabase.getId());
	   	TeamManager.addTeamMember(teamDatabase.getId(),comptr4.getId()); 
    }  
    
    
    /**
	 * test Find all the teams in the database.
	 * 
	 * 			
	 * @throws SQLException
	 * @throws BadParametersException 
	 * @throws CompetitionException 
	 */
    
    
    @Test
    public void testFindAllTeam() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	 List<Team> allTeam =TeamManager.findAll();
         int count = 0;
         for (Team team :allTeam){
         	System.out.print(team);
         	System.out.print("  ");
         	count++;
         }  
         System.out.println("  ");
         System.out.println(count);
    }  
       
    

	/**
	 * test find a team by name
	 * 
	 * @throws SQLException
     * @throws BadParametersException 
	 * @throws CompetitionException 
	 * @throws ExistingCompetitorException 
	 */
    @Test
    public void testFindTeamByName() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
		Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
		Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));
		
		AthleteManager.persist(comptr1);
		AthleteManager.persist(comptr2);
		AthleteManager.persist(comptr3);
	
	   	ArrayList<Athlete> athletes  = new ArrayList<Athlete>((Arrays.asList(comptr1, comptr2, comptr3)));
	   	
	   	Team team = new Team("ZHONGHUAa", athletes );
	   	TeamManager.persist(team);
    	Team teamFound =TeamManager.findByName("ZHONGHUAa");
    	System.out.println(teamFound.getId());
    	//Athlete athlete =AthleteManager.findByAthleteByNameBirthdate("lIU", "Jiahui",new MyCalendar(1995,8,9).toDatabaseString());
    	assertTrue(teamFound.getId()==team.getId());
    	System.out.println(teamFound.getAthletes());
    	 
    }  
    /**
	 * test delete from the database a specific team for a specific competition.
	 * 
	 * @throws SQLException
	 */
	 @Test
	    public void testDeleteTeamForCompetition() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
		    MyCalendar date = new MyCalendar(2016,8,24);	
		    Athlete comptr1 = new Athlete("Zhaonnn", "Lunnn",new MyCalendar(1995,8,24));
			Athlete comptr2 = new Athlete("GUOnnn", "Jiaonnn",new MyCalendar(1995,8,24));
			Athlete comptr3 = new Athlete("CHENnnn", "Williamnnn",new MyCalendar(1995,8,24));
			
			AthleteManager.persist(comptr1);
			AthleteManager.persist(comptr2);
			AthleteManager.persist(comptr3);
			
	    	Athlete comptr4 = new Athlete("XIAO", "Wang",new MyCalendar(1995,8,9));
	    	Athlete comptr5 = new Athlete("XIAO", "Hong",new MyCalendar(1995,8,9));
	    	Athlete comptr6 = new Athlete("XIAO", "Hua",new MyCalendar(1995,8,9));
	    	AthleteManager.persist(comptr4);
			AthleteManager.persist(comptr5);
			AthleteManager.persist(comptr6);
			
			Team team1 = new Team ("hgxing",new ArrayList <Athlete>(Arrays.asList(comptr1, comptr2, comptr3)));
	    	Team team2 = new Team ("hongxin",new ArrayList <Athlete>(Arrays.asList(comptr4, comptr5, comptr6)));
	    	
	    	TeamManager.persist(team1);
	    	TeamManager.persist(team2);
	    	
	    	Competition co3 = new Competition("cueFrce4", date, new ArrayList <Competitor>(Arrays.asList(team1,team2)));
	    	CompetitionManager.persist(co3);
			TeamManager.deleteTeamForCompetition(team1, "cueFrce4");
	    	 
	    }  
}
