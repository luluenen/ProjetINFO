
package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.utils.DateConvertor;
import fr.uv1.utils.MyCalendar;

	/**
	 * DAO class (<i>Data Access Object</i>) for the {@link Competition} class.
	 * This class provides the CRUD functionalities :<br>
	 * <ul>
	 *   <li><b>C</b>: create a new competition in the database.
	 *   <li><b>R</b>: retrieve (or read) a (list of)competition(s) in the database.
	 *   <li><b>U</b>: update the values stored in the database for a competition.
	 *   <li><b>D</b>: delete a competition in the database.
	 * </ul>
	 * 
	 * @author ZHAO Lu and GUO Jiao
	 * 
	 * Based on Philippe TANGUY's example.
	 */
/**
 * @author Lu et Jiao
 *
 */
public class CompetitionManager{
	
 
	/**
	 * Store a competition in the database.
	 * 
	 * @param competition
	 *            the competition to be stored.
	 * @return
	 * @throws SQLException
	 * @throws ExistingCompetitorException
	 */
	public static void persist(Competition competition) throws SQLException, ExistingCompetitorException{
		
		  Connection c = DatabaseConnection.getConnection();

	    try

	    {
	    	c.setAutoCommit(false);

		    PreparedStatement psPersist = c.prepareStatement("insert into competition(name, competition_date, is_sold)  values (?, ?, ?)");
		    psPersist.setString(1, competition.getName());
		    psPersist.setString(2, competition.getClosingDate().toDatabaseString());
		    psPersist.setInt(3, competition.getIsSold());

		    psPersist.executeUpdate();
		    psPersist.close();

		    // add a new element in table participation

		    for (Competitor competitor: competition.getCompetitors()){    	

		    	psPersist = c.prepareStatement("insert into participation(competitor, competition, ranking)  values (?, ?, ?)");
		    
		    	psPersist.setInt(1, competitor.getId());
		    	psPersist.setString(2, competition.getName());
		        psPersist.setInt(3, 1000);

		        psPersist.executeUpdate();

		        psPersist.close();

		    }

	    }
	    catch (SQLException e)
	    {
	      try
	      {
	        c.rollback();
	      }
	      catch (SQLException e1)
	      {
	        e1.printStackTrace();
	      }
	      c.setAutoCommit(true);
	      
	      
	      throw e;
	    }
	    
	    c.setAutoCommit(true);
	    
	    
	    c.close();
	    
		
	}

	
  //-----------------------------------------------------------------------------
	/**
	 * Find a competition by his name.
	 * 
	 * @param name 
	 * 			the name of the competition to retrieve.
	 * @return 
	 * 			the competition or null if the id does not exist in the database.
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws ExistingCompetitorException 
   */

 public static Competition findByCompName(String name) throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException{
	
	    Connection c = DatabaseConnection.getConnection();

	    PreparedStatement psSelect = c.prepareStatement("select * from competition where name=?");
	    psSelect.setString(1, name);

	    ResultSet resultSet = psSelect.executeQuery();
	    Competition competition = null;
	    
	    while(resultSet.next())
	    {	MyCalendar date = DateConvertor.String2MyCalendar(resultSet.getString("competition_date"));
	    	competition = new Competition(resultSet.getString("name"),
	    			date);
	    	
	    	competition.setCompetitors(CompetitionManager.findCompetitors(resultSet.getString("name")));
	                                  
	    }
	   
	    resultSet.close();
	    psSelect.close();
	    c.close();
	    
	    return competition;
	  
  }
 
  //-----------------------------------------------------------------------------
  	/**
  	 * Find all competitors in competition by name of competition. 
  	 * 
  	 * @param id the id of the subscriber to retrieve.
  	 * @return the subscriber or null if the id does not exist in the database.
  	 * @throws SQLException
  	 * @throws BadParametersException 
  	 * @throws CompetitionException 
  	 * @throws ExistingCompetitorException 
  	 */
  
  public static ArrayList<Competitor> findCompetitors (String nameCompetition) throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException{

	    Connection c = DatabaseConnection.getConnection();
	    PreparedStatement psSelect = c.prepareStatement("select competitor from participation where competition=?");

	    psSelect.setString(1, nameCompetition);

	    ResultSet resultList = psSelect.executeQuery();
	    ArrayList<Competitor> competitors = new ArrayList<Competitor>();
	    
	    while (resultList.next()) {
	    	  int id_competitor = resultList.getInt("competitor");
	    	  Athlete athlete = AthleteManager.findByAthleteId(id_competitor);
	    	  Team team = TeamManager.findByTeamId(id_competitor);
	    	  if (athlete != null)
	    		  competitors.add(athlete);
	    	  if (team != null)
	    		  competitors.add(team);
	    	}    
	    resultList.close();
	    psSelect.close();
	    c.close();
	    
	    return competitors;
	    
  }
  
 //-----------------------------------------------------------------------------

	/**
	 * verify the existence of a relation in table participation searching by name of competition and id of competitor.
	 * 
	 * @param nameCompetition
	 * @param idCompetitor
	 * @return
	 * 		a boolean
	 * @throws SQLException
	 */
	public static boolean existParticipationBetween( int idCompetitor,String nameCompetition ) throws SQLException{
	  
		Connection c = DatabaseConnection.getConnection();
	    PreparedStatement psSelect = c.prepareStatement("select * from participation where competitor=? and competition=?");

	    psSelect.setInt(1, idCompetitor);
	    psSelect.setString(2, nameCompetition);
	    
	    ResultSet resultSet = psSelect.executeQuery();
	    boolean result = false;
	    while (resultSet.next()) {
	    		result = true;		
	    	} 
	    resultSet.close();
	    psSelect.close();
	    c.close();
	    
	    return result;
	    
	  }
	
	
//-----------------------------------------------------------------------------
	/**
	 * verify the existence of a relation in table participation by name of competition.
	 * 
	 * @param nameCompetition
	 * @param idCompetitor
	 * @return
	 * @throws SQLException
	 */
	public static boolean exsitParticipationUseCompetition(String nameCompetition) throws SQLException
	  {
	    Connection c = DatabaseConnection.getConnection();

	    PreparedStatement psSelect = c.prepareStatement("select * from participation where competition=?");
	
	    psSelect.setString(1, nameCompetition);

	    ResultSet resultSet = psSelect.executeQuery();
	    
	    boolean result = false;
	    while (resultSet.next()) {
	    		result = true;		
	    	} 
	    resultSet.close();
	    psSelect.close();
	    c.close();
	    
	    return result;
	    
	  }
	  
//-----------------------------------------------------------------------------
		/**
		 * verify the existence of a relation in table participation by name of competition.
		 * 
		 * @param nameCompetition
		 * @param idCompetitor
		 * @return
		 * @throws SQLException
		 */
		public static boolean exsitParticipationUseCompetitor(int idCompetitor) throws SQLException
		  {
		    Connection c = DatabaseConnection.getConnection();

		    PreparedStatement psSelect = c.prepareStatement("select * from participation where competitor=?");
		
		    psSelect.setInt(1, idCompetitor);

		    ResultSet resultSet = psSelect.executeQuery();
		    
		    boolean result = false;
		    while (resultSet.next()) {
		    		result = true;		
		    	} 
		    resultSet.close();
		    psSelect.close();
		    c.close();
		    
		    return result;	    
		  }
		
		
 //-----------------------------------------------------------------------------
	  /**
	   * Find all the competition in the database.
	   * 
	   * @return
	   * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws ExistingCompetitorException 
	   */
  public static List<Competition> findAll() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException{
		
		Connection c = DatabaseConnection.getConnection();
		  PreparedStatement psSelect = c.prepareStatement("select * from competition");
		  ResultSet resultSet = psSelect.executeQuery();
		  List<Competition> competitions = new ArrayList<Competition>();
		  while(resultSet.next())
		  {
			MyCalendar date = DateConvertor.String2MyCalendar(resultSet.getString("competition_date"));
			Competition competition = new Competition(resultSet.getString("name"),
	    			date);
			
			competition.setCompetitors(CompetitionManager.findCompetitors(resultSet.getString("name")));
			
			competitions.add(competition);
		  }
		  resultSet.close();
		  psSelect.close();
		  c.close();
		    
		  return competitions;
	}

//-----------------------------------------------------------------------------
  /**
   * get on the database the value of ranking for a competitor.
   * 
   * @param competition the competition to be updated.
   * @throws SQLException
 * @throws ExistingCompetitorException 
   */
	public static int getRankingCompetitor( int idCompetitor, String nameCompetition ) throws SQLException{
		if (existParticipationBetween(idCompetitor,nameCompetition)){
			Connection c = DatabaseConnection.getConnection();
		    PreparedStatement psSelect = c.prepareStatement("select ranking from participation where competitor=? and competition=?");
	
		    psSelect.setInt(1, idCompetitor);
		    psSelect.setString(2, nameCompetition);
		    

		    ResultSet resultSet = psSelect.executeQuery();
	        int result = 0;
			while(resultSet.next())
			{
				result = resultSet.getInt("ranking");
			}
		    resultSet.close();
		    psSelect.close();
		    c.close();
		    return result;
		}
	    
	    
	    return 0;
	    
	  }
	
	
//-----------------------------------------------------------------------------
	  /**
	   * Update on the database the values from a competition.
	   * 
	   * @param competition the competition to be updated.
	   * @throws SQLException
	 * @throws ExistingCompetitorException 
	   */
  public static void update(Competition competition) throws SQLException, ExistingCompetitorException{
		
		Connection c = DatabaseConnection.getConnection();
		
		Statement updateDateStmt = c.createStatement();
		
		String request = "update competition set is_sold="+competition.getIsSold()+" where name='"+competition.getName()+"'";
		
		updateDateStmt.executeUpdate(request);
		
		updateDateStmt.close();
		  
		c.close();

	}
  

  

	
//-----------------------------------------------------------------------------
  /**
   * Update on the database the values from a competition.
   * 
   * @param competition the competition to be updated.
   * @throws SQLException
 * @throws ExistingCompetitorException 
   */
  public static void setRank(Competition competition) throws SQLException, ExistingCompetitorException{
		
		Connection c = DatabaseConnection.getConnection();	    

		for (Competitor competitor : competition.getCompetitors()){
			
			Statement updateDateStmt = c.createStatement();
			String request = "update subscriber set ranking "+competition.getRankCompetitor(competitor)+" where competitor = "+
					competitor.getId() + " and competition = '"+competition.getName()+"'";
			updateDateStmt.executeUpdate(request);
			updateDateStmt.close();			
		}
		c.close();
	}
  
 
  
  
//-----------------------------------------------------------------------------
  /**
   * Delete from the database a specific competition.<br>
   * <i>Be careful: the delete functionality does not operate a delete
   * cascade on bets belonging to the competition.</i>
   * 
   * @param competition the competition to be deleted.
   * @throws CompetitionException 
   * @throws BadParametersException 
   * @throws SQLException
 * @throws SubscriberException 
   * @throws ExistingCompetitorException 
   * */
  
	  public static void delete(Competition competition) throws SQLException, BadParametersException, CompetitionException, SubscriberException
	  {
		  
	    Connection c = DatabaseConnection.getConnection();
	    // delete participations belong to the competition 
	    if (CompetitionManager.exsitParticipationUseCompetition(competition.getName())){
	    	Statement psDeleteParticipation = c.createStatement();
	    	String request =  "delete from participation where competition='"+competition.getName()+"'";
	    	psDeleteParticipation.executeUpdate(request);
	    	psDeleteParticipation.close();	    
	    }
	    
		// delete the competition 
	    PreparedStatement psDeleteCompetition = c.prepareStatement("delete from competition where name=?");
	    psDeleteCompetition.setString(1, competition.getName());
	    psDeleteCompetition.executeUpdate();
	    psDeleteCompetition.close();
	    
	    c.close();
	  }
	  
	//-----------------------------------------------------------------------------



	  	/**
	  	 *Delete from the database a specific participation by competition and competitor.<br>
	  	 * 
	  	 * @param competition
	  	 * 			the competition in this relation
	  	 * @param competitor
	  	 * 			the competitor in this relation
	  	 * @throws SQLException
	  	 */
	 public static void deleteParticipation(Competition competition, Competitor competitor) throws SQLException
	 {
	  		  
	  	  Connection c = DatabaseConnection.getConnection();
	  	  int id_competitor = competitor.getId();
	  	  String comptitionName = competition.getName();
	  	  if (CompetitionManager.existParticipationBetween(id_competitor, comptitionName)){
		    	Statement psDeleteParticipation = c.createStatement();
		    	String request =  "delete from participation where competition='" +competition.getName()+ "' and competitor = '"+competitor.getId()+"'";
		    	psDeleteParticipation.executeUpdate(request);
		    	psDeleteParticipation.close();	
			    c.close();
		
	  	  }

	  }
	 
	  	/**
	  	 *add from the database a specific participation by competition and competitor.<br>
	  	 * 
	  	 * @param competition
	  	 * 			the competition in this relation
	  	 * @param competitor
	  	 * 			the competitor in this relation
	  	 * @throws SQLException
	  	 * @throws ExistingCompetitorException 
	  	 */
	 public static void addParticipation(Competition competition, Competitor competitor) throws SQLException, ExistingCompetitorException
	 {
	  		  
	  	  Connection c = DatabaseConnection.getConnection();
	  	  int id_competitor = competitor.getId();
	  	  String comptitionName = competition.getName();
	  	  if (CompetitionManager.existParticipationBetween(id_competitor, comptitionName)){
	  		  String request = "insert into participation(competitor, competition, ranking) values (" +id_competitor+ ", '" + comptitionName+ "' ,"+ competition.getRankCompetitor(competitor) +")" ;	    
		      Statement psDeleteParticipation = c.createStatement();
		      psDeleteParticipation.executeUpdate(request);
		      psDeleteParticipation.close();
		      c.close();
	  	  }

	  }
	 

	  
}
