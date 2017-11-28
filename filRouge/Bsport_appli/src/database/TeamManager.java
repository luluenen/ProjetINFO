package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;

public class TeamManager {
	/**
     * Store a team in the database for a specific team.This team is not 
     * stored yet, so his <code>id</code> value is <code>NULL</code>. Once team is stored, the
     * method returns the team  with the <code>id</code> value settled. Its id depend on the sequence
     * of table competitor.
     *
     * @param team 
     *            the team  to be stored.
     * @return the team  with the updated value for the id.
     * @throws SQLException
     */
	
    public static void persist(Team team) throws SQLException {
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
            
            
            PreparedStatement psPersist1 = c.prepareStatement("INSERT INTO team(id_team, name)  VALUES (?, ?)");
            psPersist1.setInt(1, id);
            psPersist1.setString(2, team.getTeamName());
            psPersist1.executeUpdate();

            psPersist1.close();
                    
            
            c.close();
            
            team.setId(id);
            
            for (Athlete athlete : team.getAthletes()){                	
            	addTeamMember(team.getId(), athlete.getId());
            }

    }
    
    
    /**
	 * Find a team by his id.
	 * 
	 * @param teamId;
	 *            the teamId of the team to retrieve.
	 * @return the team or null if the teamId does not exist in the database.
	 * @throws SQLException
     * @throws BadParametersException 
     * @throws CompetitionException 
     * @throws ExistingCompetitorException 
	 */
	public static Team findByTeamId(int teamId) throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect1 = c
				.prepareStatement("select * from team where id_team = ?");
		psSelect1.setInt(1, teamId );
		ResultSet resultSet1 = psSelect1.executeQuery();
		
		Team teamFound = null;
		
		while (resultSet1.next()) {
			
			teamFound = new Team(resultSet1.getInt("id_team"),
					resultSet1.getString("name"));		
		}		
		resultSet1.close();
		psSelect1.close();
		
	
		
		if (teamFound != null){
			//add the list of athletes	
			PreparedStatement psSelect2 = c.prepareStatement("select athlete from member where team = ?");
			psSelect2.setInt(1, teamFound.getId() );
			ResultSet resultSet2 = psSelect2.executeQuery();
			
			
			while (resultSet2.next()) {
				 
				Athlete athlete = AthleteManager.findByAthleteId(resultSet2.getInt("athlete"));
				if (!teamFound.getAthletes().contains(athlete)){
					teamFound.addMember(athlete);
				}
			}		
			resultSet2.close();
			psSelect2.close();
				
		}
		
		
//		//add the list of competitions
//		PreparedStatement psSelect3 = c
//				.prepareStatement("select competition from participation where competitor = ?");
//		psSelect3.setInt(1, teamId );
//		ResultSet resultSet3 = psSelect3.executeQuery();
//		
//		
//		while (resultSet3.next()) {
//			 
//			
//			Competition competition = CompetitionManager.findByCompName(resultSet3.getString("competition"));
//			teamFound.addCompetition(competition);
//		}		
//		resultSet3.close();
//		psSelect3.close();
		
		c.close();
		
		return teamFound;
	} 

	
	/**
	 * Add a member of team in the database.
	 *
	 * @throws SQLException
	 */
	
	public static void addTeamMember(int id_team,int id_athlete) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement psAddMember = connection
                    .prepareStatement("INSERT INTO member(athlete, team)  values (?, ?)");
            
            psAddMember.setInt(1, id_athlete);
            psAddMember.setInt(2, id_team);
            psAddMember.executeUpdate();

            psAddMember.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            connection.setAutoCommit(true);
            throw e;
        }
    }
	
	
	/**
	 * Find all the teams in the database.
	 * 
	 * @return list of teams
	 * 			
	 * @throws SQLException
	 * @throws BadParametersException 
	 * @throws CompetitionException 
	 * @throws ExistingCompetitorException 
	 */
	public static List<Team> findAll() throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from team order by name");
		ResultSet resultSet = psSelect.executeQuery();
		List<Team> teams= new ArrayList<Team>();
		while (resultSet.next()) {
						
			Team team= null;
			
			team = new Team(resultSet.getInt("id_team"),
					resultSet.getString("name"));
			
			if (team != null){
				//add the list of athletes	
				PreparedStatement psSelect2 = c.prepareStatement("select athlete from member where team = ?");
				psSelect2.setInt(1, team.getId() );
				ResultSet resultSet2 = psSelect2.executeQuery();
				
				
				while (resultSet2.next()) {
					 
					Athlete athlete = AthleteManager.findByAthleteId(resultSet2.getInt("athlete"));
					if (!team.getAthletes().contains(athlete)){
						team.addMember(athlete);
					}
				}		
				resultSet2.close();
				psSelect2.close();
					
			}
			
			teams.add(team);
			
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return teams;
	} 
	
	
	

	/**
	 * find a team by name
	 * 
	 * @param ;
	 *            the athleteId of the subscriber to retrieve.
	 * @return the team or null if the athleteId does not exist in the database.
	 * @throws SQLException
     * @throws BadParametersException 
	 * @throws CompetitionException 
	 * @throws ExistingCompetitorException 
	 */
	
	public static Team findByName(String name) 
			throws SQLException, BadParametersException, CompetitionException, ExistingCompetitorException {
		
		Connection c = DatabaseConnection.getConnection();	
		PreparedStatement psSelect = c
				.prepareStatement("select * from team where name = ?" );
		
		psSelect.setString(1, name);
		
		ResultSet resultSet = psSelect.executeQuery();
		
		Team teamFound = null;
		
		while (resultSet.next()) {
			
			teamFound = new Team(resultSet.getInt("id_team"),
					resultSet.getString("name"));
		}		

		resultSet.close();
		psSelect.close();

		if (teamFound != null){
			//add the list of athletes	
			PreparedStatement psSelect2 = c.prepareStatement("select athlete from member where team = ?");
			psSelect2.setInt(1, teamFound.getId() );
			ResultSet resultSet2 = psSelect2.executeQuery();
			
			
			while (resultSet2.next()) {
				 
				Athlete athlete = AthleteManager.findByAthleteId(resultSet2.getInt("athlete"));
				
				if (!teamFound.getAthletes().contains(athlete)){
					teamFound.addMember(athlete);
				}
			}		
			resultSet2.close();
			psSelect2.close();
				
		}
		
		c.close();
		
		return teamFound;
	} 
	
	
	
    /**
	 * Update the data of a team.
	 * 
	 * @param team;
	 *            the object team of the team to update its data.
	 * @throws SQLException
     * @throws BadParametersException 
	 */
	public static void updateTeam( Team team) throws SQLException {

		  Connection c = DatabaseConnection.getConnection();
	        PreparedStatement psUpdate = c
	                .prepareStatement("update team set name=? where id_team=?");

	        psUpdate.setString(1, team.getTeamName());
	        psUpdate.setInt(2, team.getId());
	    }
	
	
	
		/**
		 * verify the existence of a relation in table member searching by id of team and id of competitor.
		 * 
		 * @param idTeam
		 * @param idCompetitor
		 * @return
		 * 		a boolean
		 * @throws SQLException
		 */
		public static boolean existMemberBetween( int idAthlete,int idTeam ) throws SQLException{
		  
			Connection c = DatabaseConnection.getConnection();
		    PreparedStatement psSelect = c.prepareStatement("select * from member where athlete=? and team=?");

		    psSelect.setInt(1, idAthlete);
		    psSelect.setInt(2, idTeam);
		    
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
		
		
		
		/**
		 * verify the existence of a relation in table member searching by id_team.
		 * 
		 * @param idTeam
		 * @return
		 * 		a boolean
		 * @throws SQLException
		 */
		public static boolean existMemberUseIdTeam( int idTeam ) throws SQLException{
		  
			Connection c = DatabaseConnection.getConnection();
		    PreparedStatement psSelect = c.prepareStatement("select * from member where team=?");


		    psSelect.setInt(1, idTeam);
		    
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
		/**
		 * verify the existence of a relation in table member searching by id_competitor.
		 * 
		 * @param idCompetitor
		 * @return
		 * 		a boolean
		 * @throws SQLException
		 */
		public static boolean existMemberUseIdAthlete( int idAthlete ) throws SQLException{
		  
			Connection c = DatabaseConnection.getConnection();
		    PreparedStatement psSelect = c.prepareStatement("select * from member where athlete=?");


		    psSelect.setInt(1, idAthlete);
		    
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
		    
		  

	/**
	 * Delete from the database a specific team.
	 * 
	 * @param competitor
	 *            the team to be deleted.
	 * @throws SQLException
	 */
	public static void delete(Team competitor) throws SQLException {
		Connection c = DatabaseConnection.getConnection();	
		
		if (existMemberUseIdTeam(competitor.getId())){
			// delete data of relations in member table belong to this team 
	        PreparedStatement psDelete4 = c.prepareStatement("delete from member where team=?");
	  		psDelete4.setInt(4, competitor.getId());  
	        psDelete4.executeUpdate();
	        psDelete4.close();
		}
		if (CompetitionManager.exsitParticipationUseCompetitor(competitor.getId())){
			// delete data of relations in participation table belong to this team 
	        PreparedStatement psDelete2 = c.prepareStatement("delete from participation where competitor=? ");
			psDelete2.setInt(2, competitor.getId());
	        psDelete2.executeUpdate();
	        psDelete2.close();
		}
		
        // delete data of this team in team table
        PreparedStatement psDelete1 = c.prepareStatement("delete from team where id_team=? ");
        psDelete1.setInt(1, competitor.getId());
        psDelete1.executeUpdate();
        psDelete1.close();

		// delete data of relations in competitor table belong to this team 
        PreparedStatement psDelete3 = c.prepareStatement("delete from competitor where id_competitor=? ");
		psDelete3.setInt(3, competitor.getId());
        psDelete3.executeUpdate();
        psDelete3.close();

        
        c.close();   		

	}

	/**
	 * Delete from the database a specific team for a specific competition.
	 * 
	 * @param competitor
	 *            the team to be deleted.
	 * @throws SQLException
	 */
	public static void deleteTeamForCompetition(Team competitor,
			String competition) throws SQLException {
		
		Connection c = DatabaseConnection.getConnection();	
		
		if (CompetitionManager.exsitParticipationUseCompetitor(competitor.getId())){
			System.out.println("in boucle");
			// delete data of relations in participation table belong to this team 
	        PreparedStatement psDelete2 = c.prepareStatement("delete from participation where competitor=? ");
			psDelete2.setInt(1, competitor.getId());
			
	        psDelete2.executeUpdate();
	        psDelete2.close();
		}
		
		c.close();
		
	}
    
	
	

}