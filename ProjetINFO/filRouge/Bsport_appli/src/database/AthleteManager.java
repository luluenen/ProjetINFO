package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.utils.DateConvertor;

/**
 * DAO class (<i>Data Access Object</i>) for the {@link Bet} class. This class
 * provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new bet in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)bet(s) from the database.
 * <li><b>U</b>: update the values stored in the database for a bet.
 * <li><b>D</b>: delete a bet in the database.
 * </ul>
 *
 * @author GUO Jiao and ZHAO Lu
 */

public class AthleteManager {

        /**
         * Store a athlete in the database .
         * This athlete is not stored yet, so his <code>id</code> value is <code>NULL</code>. 
         * Once athlete is stored, the method returns the Athlete with the <code>id</code> value settled. 
         * Its id depend on the sequence of table competitor.
         * set id for this athlete
         *
         * @param athlete
         *            the athlete to be stored.
         * @throws SQLException
         */
        public static void persist(Athlete athlete) throws SQLException {
                Integer id = null;
                Connection c = DatabaseConnection.getConnection();
                try {
                        c.setAutoCommit(false);
                        
                        PreparedStatement psIdValue = c.prepareStatement("SELECT competitor_seq.nextval as value_id FROM dual");
                        ResultSet resultSet = psIdValue.executeQuery();
                        
                        while (resultSet.next()){id = resultSet.getInt("value_id");}
                            resultSet.close();
                            psIdValue.close();
                            c.commit();
                    }
                    catch (SQLException e) {
                        try {
                                c.rollback();
                        } catch (SQLException e1) {
                                e1.printStackTrace();
                        }
                        c.setAutoCommit(true);
                        throw e;
                }
                c.setAutoCommit(true);
                

                //we insert this id into the table of competitor
                PreparedStatement psPersist2 = c.prepareStatement("INSERT INTO competitor(id_competitor) VALUES (?)");
                psPersist2.setInt(1, id);
                psPersist2.executeUpdate();
                psPersist2.close();
                
                PreparedStatement psPersist1 = c.prepareStatement("INSERT INTO athlete(id_athlete, lastname, firstname, birthDate)  VALUES (?, ?, ?, ?)");
                psPersist1.setInt(1, id);
                psPersist1.setString(2, athlete.getFamilyName());
                psPersist1.setString(3, athlete.getFirstName());
                psPersist1.setString(4, athlete.getBirthDate().toDatabaseString());                
                psPersist1.executeUpdate();
                psPersist1.close();
                

                c.close();
                athlete.setId(id);
        }
        
        
        
        /**
    	 * Find a athlete by his id.
    	 * 
    	 * @param athleteId;
    	 *            the athleteId of the subscriber to retrieve.
    	 * @return the athlete or null if the athleteId does not exist in the database.
    	 * @throws SQLException
         * @throws BadParametersException 
         * @throws ExistingCompetitorException 
         * @throws CompetitionException 
    	 */
    	public static Athlete findByAthleteId(int athleteId) throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
    		
    		Connection c = DatabaseConnection.getConnection();
    		PreparedStatement psSelect = c
    				.prepareStatement("select * from athlete where id_athlete = ?");
    		psSelect.setInt(1,athleteId );
    		ResultSet resultSet = psSelect.executeQuery();
    		
    		Athlete athleteFound = null;
    		
    		while (resultSet.next()) {
    			athleteFound = new Athlete(resultSet.getInt("id_athlete"),
    					resultSet.getString("lastname"),
    					resultSet.getString("firstname"),
    					DateConvertor.String2MyCalendar(resultSet.getString("birthDate")));
    		}		
    		resultSet.close();
    		psSelect.close();
    		
//    		//add the list of competitions
//    		PreparedStatement psSelect2 = c
//    				.prepareStatement("select competition from participation where competitor = ?");
//    		psSelect2.setInt(1, athleteFound.getId() );
//    		ResultSet resultSet2 = psSelect2.executeQuery();
//    		
//    		
//    		while (resultSet2.next()) {
//    			 		
//    			Competition competition = CompetitionManager.findByCompName(resultSet2.getString("competition"));
//    			athleteFound.addCompetition(competition);
//    			
//    		}		
//    		resultSet2.close();
//    		psSelect2.close();
//    		
    		c.close();
    		
    		return athleteFound;
    	} 
       
    	
    	/**
    	 * find a athlete by familyName,firstName and birthDate
    	 * 
    	 * @param familyName 
    	 * @param firstName
    	 * @param birthDate;
    	 *
    	 * @return the athlete or null if the parameter do not exist in the database.
    	 * @throws SQLException
         * @throws BadParametersException 
    	 * @throws ExistingCompetitorException 
    	 * @throws CompetitionException 
    	 */	
		public static Athlete findByAthleteByNameBirthdate(String familyName,String firstName, String birthDate) 
				throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
    		
    		Connection c = DatabaseConnection.getConnection();	
    		PreparedStatement psSelect = c
    				.prepareStatement("select * from athlete where lastname = ? and firstname = ? and birthdate = ?" );
    		
    		psSelect.setString(1,familyName);
    		psSelect.setString(2,firstName);
    		psSelect.setString(3,birthDate);
    		
    		ResultSet resultSet = psSelect.executeQuery();
    		
    		Athlete athleteFound = null;
    		
    		while (resultSet.next()) {
    			
    			athleteFound = new Athlete(resultSet.getInt("id_athlete"),
    					resultSet.getString("lastname"),
    					resultSet.getString("firstname"),
    					DateConvertor.String2MyCalendar(resultSet.getString("birthDate")));
    		}		
   
    		resultSet.close();
    		psSelect.close();
    		
//    		//add the list of competitions
//    		PreparedStatement psSelect2 = c
//    				.prepareStatement("select competition from participation where competitor = ?");
//    		psSelect2.setInt(1, athleteFound.getId() );
//    		ResultSet resultSet2 = psSelect2.executeQuery();
//    		
//    		
//    		while (resultSet2.next()) {
//    			 
//    			
//    			Competition competition = CompetitionManager.findByCompName(resultSet2.getString("competition"));
//    			athleteFound.addCompetition(competition);
//    			
//    		}		
//    		resultSet2.close();
//    		psSelect2.close();
    		
    		c.close();

    		
    		return athleteFound;
    	} 
   
		
    	
    	/**
    	 * Find all athletes or a athlete by a familyName and a firstName.
    	 * 
    	 * @param athleteId;
    	 *            the athleteId of the subscriber to retrieve.
    	 * @return the athlete or null if the athleteId does not exist in the database.
    	 * @throws SQLException
         * @throws BadParametersException 
    	 * @throws ExistingCompetitorException 
    	 * @throws CompetitionException 
    	 */
		public static ArrayList<Athlete> findByAthleteName(String familyName,String firstName) throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
    		
    		Connection c = DatabaseConnection.getConnection();
    		PreparedStatement psSelect = c
    				.prepareStatement("select * from athlete where lastname = ? and firstname = ?");
    		psSelect.setString(1,familyName);
    		psSelect.setString(2,firstName);
    		ResultSet resultSet = psSelect.executeQuery();
    		
    		ArrayList<Athlete> athletesFound = new ArrayList<Athlete>();
    		
    		while (resultSet.next()) {
    			
    			Athlete athleteFound = new Athlete(resultSet.getInt("id_athlete"),
    					resultSet.getString("lastname"),
    					resultSet.getString("firstname"),
    					DateConvertor.String2MyCalendar(resultSet.getString("birthDate")));
    			
    			
//    			//add the list of competitions
//        		PreparedStatement psSelect2 = c
//        				.prepareStatement("select competition from participation where competitor = ?");
//        		psSelect2.setInt(1, athleteFound.getId() );
//        		ResultSet resultSet2 = psSelect2.executeQuery();
//        		
//        		
//        		while (resultSet2.next()) {
//        			 
//        			
//        			Competition competition = CompetitionManager.findByCompName(resultSet2.getString("competition"));
//        			athleteFound.addCompetition(competition);
//        			
//        		}		
//        		resultSet2.close();
//        		psSelect2.close();
        		
        		athletesFound.add(athleteFound);
        		
    		}		
    		resultSet.close();
    		psSelect.close();
    		
    		
    		c.close();
    		
    		return athletesFound;
    	} 
   
    	
    	
    	/**
    	 * Find all the athletes in the database.
    	 * 
    	 * @return list of athletes
    	 * 			a list of all the athletes
    	 * @throws SQLException
    	 * @throws BadParametersException 
    	 * @throws ExistingCompetitorException 
    	 * @throws CompetitionException 
    	 */
		
    	public static List<Athlete> findAll() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
    		Connection c = DatabaseConnection.getConnection();
    		PreparedStatement psSelect = c
    				.prepareStatement("select * from athlete order by firstname");
    		ResultSet resultSet = psSelect.executeQuery();
    		List<Athlete> athletes= new ArrayList<Athlete>();
    		
    		while (resultSet.next()) {
  		
    			Athlete athlete = new Athlete(resultSet.getInt("id_athlete"),
    					resultSet.getString("lastname"),
    					resultSet.getString("firstname"),
    					DateConvertor.String2MyCalendar(resultSet.getString("birthDate")));
    			   			
    			
//    			//add the list of competitions
//        		PreparedStatement psSelect2 = c
//        				.prepareStatement("select competition from participation where competitor = ?");
//        		psSelect2.setInt(1, athlete.getId() );
//        		ResultSet resultSet2 = psSelect2.executeQuery();
//        		
//        		
//        		while (resultSet2.next()) {
//        			 
//        			
//        			Competition competition = CompetitionManager.findByCompName(resultSet2.getString("competition"));
//        			athlete.addCompetition(competition);
//        			
//        		}		
//        		resultSet2.close();
//        		psSelect2.close();
        		        		
    			athletes.add(athlete);
    			
    		}
    		resultSet.close();
    		psSelect.close();
    		c.close();

    		return athletes;
    	} 
    	

    	
        /**
    	 * Update the data of a athlete.
    	 * 
    	 * @param athlete;
    	 *            the object athlete of the athlete to update its data.
    	 * @throws SQLException
         * @throws BadParametersException 
    	 */
    	public static void updateAthlete( Athlete athlete) throws SQLException {

    		  Connection c = DatabaseConnection.getConnection();
    	        PreparedStatement psUpdate = c
    	                .prepareStatement("update athlete set lastname=?, firstname=?, birthdate= ? where id_athlete=?");

    	        psUpdate.setString(1, athlete.getFamilyName());
    	   
    	        psUpdate.setString(2, athlete.getFirstName());
    	        psUpdate.setString(4, athlete.getBirthDate().toDatabaseString()); 
    	        psUpdate.setInt(4, athlete.getId());
    	    }

    	
    	/**
    	 * Delete athletes who participate a certain competition.
    	 * 
    	 * @param athlete
    	 *            the athlete to be deleted.
    	 * @param nameCompetition
    	 *            the name of competition  
    	 * @throws SQLException
    	 * @throws BadParametersException 
    	 */
    	public static void deleteAthleteForCompetition(Athlete athlete,String nameCompetition) throws SQLException, BadParametersException {
    		  
    	    Connection c = DatabaseConnection.getConnection();
    	    // delete participations belong to the competition 
    	    if (CompetitionManager.exsitParticipationUseCompetition(nameCompetition)){
    	    	Statement psDeleteParticipation = c.createStatement();
    	    	String request =  "delete from participation where competition='"+nameCompetition+"'";
    	    	psDeleteParticipation.executeUpdate(request);
    	    	psDeleteParticipation.close();	    
    	    }
    	    else 
    	    	throw new BadParametersException("This competition does not have the competitiors"); 
    	}
        
    	
    	/**
    	 * Delete from the database a specific athlete.
    	 * 
    	 * @param athlete
    	 *            the athlete to be deleted.
    	 * @throws SQLException
    	 */
    	public static void delete(Athlete athlete) throws SQLException {
    		Connection c = DatabaseConnection.getConnection();
    		// delete data of relations in member table belong to this athlete 
    		if ( TeamManager.existMemberUseIdAthlete(athlete.getId())){
    			PreparedStatement psDelete4 = c.prepareStatement("delete from member where competitor=?");
          		psDelete4.setInt(1, athlete.getId());
                psDelete4.executeUpdate();
                psDelete4.close();
    		}
            
    		 // delete all relations in participation table belong to this athlete
    		if ( CompetitionManager.exsitParticipationUseCompetitor(athlete.getId())){
	            PreparedStatement psDelete2 = c.prepareStatement("delete from participation where competitor=? ");
	    		psDelete2.setInt(1, athlete.getId());
	            psDelete2.executeUpdate();
	            psDelete2.close();
    		}		
            
            // delete data of this athlete in Athlete table
            PreparedStatement psDelete1 = c.prepareStatement("delete from athlete where id_athlete=? ");
            psDelete1.setInt(1, athlete.getId());
            psDelete1.executeUpdate();
            psDelete1.close();
            
            // delete data in competitor table belong to this athlete
            PreparedStatement psDelete3 = c.prepareStatement("delete from competitor where id_competitor=? ");
    		psDelete3.setInt(1, athlete.getId());
            psDelete3.executeUpdate();
            psDelete3.close();

                        
            c.close();   		

    	}

}