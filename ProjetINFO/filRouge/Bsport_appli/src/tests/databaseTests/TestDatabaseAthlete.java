package fr.uv1.tests.databaseTests;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.database.AthleteManager;
import fr.uv1.database.CompetitionManager;
import fr.uv1.database.DatabaseConnection;
import fr.uv1.utils.MyCalendar;


 public class TestDatabaseAthlete {
	

	private static Connection connection;


    /**
     * test connection with database
     * @throws SQLException 
     */
	public static void openConnection() throws SQLException {
	    connection = DatabaseConnection.getConnection();
    }

	
    /**
     * test the close of the connection
     * @throws SQLException 
     */
    public static void closeConnection() throws SQLException {
        connection.close();
    }
 
    
    /**
     * test persist of an athlete
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
	@Test
	public void testPersistAthlete() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	Athlete a1 = new Athlete("lIU", "Jiahui",new MyCalendar(1995,8,9)); 
    	AthleteManager.persist(a1);
    	System.out.println(a1.getId());
        Athlete athleteDatabase = AthleteManager.findByAthleteId(a1.getId());      
        assertTrue(athleteDatabase.getFirstName().equals(a1.getFirstName()));
        assertTrue(athleteDatabase.getFamilyName().equals(a1.getFamilyName()));
        assertTrue(athleteDatabase.getFamilyName().equals(a1.getFamilyName()));
        assertTrue(athleteDatabase.getBirthDate().equals(a1.getBirthDate()));
    }
		
			
    /**
     * test delete an athlete and at the same time, we delete the 
     * corresponding competitor in the table of competitor.
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
	@Test
    public void testDeleteAthlete() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	Athlete a1 = new Athlete("WANG", "Jiahui",new MyCalendar(1995,8,9)); 
    	AthleteManager.persist(a1);
    	System.out.println(a1.getId());
    	AthleteManager.delete(a1);
    	Athlete athleteDatabase = AthleteManager.findByAthleteId(a1.getId()); 
    	assertTrue(athleteDatabase == null);
    }
    
    
    		
    /**
     * test deleteAthleteForCompetition(Athlete athlete,String nameCompetition)
     * @throws SQLException 
     * @throws CompetitionException 
     * @throws ExistingCompetitorException 
     */
	@Test
    public void testDeleteAthleteForCompetition() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	    MyCalendar dateBirth = new MyCalendar(1995,8,24);

	    	Athlete comptr1 = new Athlete("zozo", "Lu", dateBirth);
	    	Athlete comptr2 = new Athlete("Guu", "Jiooo", dateBirth);
	    	Athlete comptr3 = new Athlete("CHENlll", "Wilppliam", dateBirth);

	    	AthleteManager.persist(comptr1);
	    	AthleteManager.persist(comptr2);
	    	AthleteManager.persist(comptr3);

	    	Athlete comptr4 = new Athlete("XIAOXIAO", "Wang", dateBirth);
	    	AthleteManager.persist(comptr4);


	    	ArrayList<Competitor> competitors1  = new ArrayList<Competitor>((Arrays.asList(comptr1, comptr2, comptr3)));
		    MyCalendar date = new MyCalendar(2016,8,24);
	    	Competition co1 = new Competition("uighueeeee", date, competitors1);
	    	CompetitionManager.persist(co1);
	        AthleteManager.deleteAthleteForCompetition(comptr1, "uighueeeee");
    }
    
    
   
     
    /**
     * test the athlete by id
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testFindByIdAthlete() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	Athlete a1 = new Athlete("lIU", "Jiahui",new MyCalendar(1995,8,9)); 
    	AthleteManager.persist(a1);
    	Athlete athlete =AthleteManager.findByAthleteId(a1.getId());
    	assertTrue(athlete.getFamilyName().equals(a1.getFamilyName()));
    	assertTrue(athlete.getBirthDate().equals(a1.getBirthDate()));	    
    }  
   
    
      
     
    /**
     * test findAthleteByNameBirthdate(String familyName,String firstName, String birthDate) 
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testFindAthleteByNameBirthdate() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	Athlete a1 = new Athlete("lIU", "Jiahui",new MyCalendar(1995,8,9)); 
    	AthleteManager.persist(a1);
    	System.out.println(a1.getId());
    	Athlete athlete =AthleteManager.findByAthleteByNameBirthdate("lIU", "Jiahui","9/8/1995" );
    	//Athlete athlete =AthleteManager.findByAthleteByNameBirthdate("lIU", "Jiahui",new MyCalendar(1995,8,9).toDatabaseString());
    	assertTrue(a1.equals(athlete));
    	 
    }  
    
    
    /**
     * test update an athlete
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testupdateAthlete() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
    	Athlete a2 = new Athlete("lIU", "Jiahui",new MyCalendar(1995,8,9)); 
    	AthleteManager.persist(a2);
    	Athlete a1 = new Athlete(a2.getId(),"liuiu", "Jiahui",new MyCalendar(1995,8,9));

    	AthleteManager.updateAthlete(a1);
    	Athlete athleteDatabase = AthleteManager.findByAthleteId(a1.getId());    

      
        assertTrue(athleteDatabase.getFirstName().equals(a1.getFirstName()));
        assertTrue(!athleteDatabase.getFamilyName().equals(a1.getFamilyName()));
        assertTrue(athleteDatabase.getBirthDate().equals(a1.getBirthDate()));
    }  
   
    
    /**
     * test find all athlete
     * @throws SQLException 
     * @throws ExistingCompetitorException 
     * @throws CompetitionException 
     */
    @Test
    public void testFindAllAthlete() throws BadParametersException, SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
        List<Athlete> allAthlete =AthleteManager.findAll();
        int count = 0;
        for (Athlete athlete :allAthlete){
        	System.out.print(athlete);
        	System.out.print("  ");
        	count++;
        }  
        System.out.println("  ");
        System.out.println(count);
    }      

}