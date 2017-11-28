package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.DateConvertor;

/**
 * DAO class (<i>Data Access Object</i>) for the {@link Subscriber} class. This class
 * provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new subscriber in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)subscriber(s) from the database.
 * <li><b>U</b>: update the values stored in the database for a subscriber.
 * <li><b>D</b>: delete a subscriber in the database.
 * </ul>
 * 
 * @author tmarcuzzi 
 * 
 * Based on Philippe TANGUY's example.
 */
public class SubscribersManager {
	// -----------------------------------------------------------------------------
	/**
	 * Store a subscriber in the database.
	 * 
	 * @param subscriber
	 *            the subscriber to be stored.
	 * @return 
	 * @throws SQLException
	 */
	public static void persist(Subscriber subs) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			
			PreparedStatement psIdValue = c.prepareStatement("SELECT subscriber_seq.nextval as value_id FROM dual");
			
			ResultSet resultSet = psIdValue.executeQuery();
			
			while (resultSet.next()){}
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
		
		PreparedStatement psPersist = c.prepareStatement("INSERT INTO subscriber(username," +
				" lastname, firstname, birthDate, tokens," +
				" password)  VALUES (?, ?, ?, ?, ?, ?)");
				
		psPersist.setString(1, subs.getUsername());
		psPersist.setString(2, subs.getFamilyName());
		psPersist.setString(3, subs.getFirstName());
		psPersist.setString(4, (subs.getBirthDate()).toDatabaseString());
		psPersist.setLong(5, subs.getBalance());
		psPersist.setString(6, subs.getPassword());

		psPersist.executeUpdate();

		psPersist.close();
		
		c.close();

	}

	// -----------------------------------------------------------------------------
	/**
	 * Find a subscriber by his username.
	 * 
	 * @param username;
	 *            the username of the subscriber to retrieve.
	 * @return the subscriber or null if the username does not exist in the database.
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static Subscriber findByUsername(String username) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from subscriber where username=?");
		psSelect.setString(1, username);
		ResultSet resultSet = psSelect.executeQuery();
		
		Subscriber subs = null;
		
		while (resultSet.next()) {
			
			subs = new Subscriber(resultSet.getString("lastname"),
					resultSet.getString("firstname"),
					resultSet.getString("username"),
					DateConvertor.String2MyCalendar(resultSet.getString("birthDate")),
					resultSet.getInt("tokens"),
					resultSet.getString("password"));
					
			
			subs.setBets((ArrayList<Bet>) BetsManager.findAllBySubscriber(subs));	
			
					
		}
		
		resultSet.close();
		psSelect.close();
		c.close();

		return subs;
	} 
	
	// -----------------------------------------------------------------------------
	/**
	 * Find all the subscribers in the database.
	 * 
	 * @return subscribers
	 * 			a list of all the subscribers
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static List<Subscriber> findAll() throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from subscriber order by username");
		ResultSet resultSet = psSelect.executeQuery();
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		while (resultSet.next()) {
			
						
			Subscriber subs = null;
			
			subs = new Subscriber(resultSet.getString("lastname"),
					resultSet.getString("firstname"),
					resultSet.getString("username"),
					DateConvertor.String2MyCalendar(resultSet.getString("birthDate")),
					resultSet.getInt("tokens"),
					resultSet.getString("password"));
							
			subs.setBets((ArrayList<Bet>) BetsManager.findAllBySubscriber(subs));
			
			subscribers.add(subs);
			
		}
		resultSet.close();
		psSelect.close();
		c.close();

		return subscribers;
	} 
	
	// -----------------------------------------------------------------------------
	/**
	 * Update on the database the balance.
	 * 
	 * @param subscriber
	 *            the subscriber to be updated.
	 * @throws SQLException
	 */
	public static void updateCoins(Subscriber subs) throws SQLException {
		
	    Connection c = DatabaseConnection.getConnection();
	    
	    Statement updateCoinsStmt = c.createStatement();
		
		String request = "update subscriber set tokens="+subs.getBalance()+"where username='"+subs.getUsername()+"'";
		
		updateCoinsStmt.executeUpdate(request);
		
		updateCoinsStmt.close();
	    
	    c.close();
	    
	}
	
	// -----------------------------------------------------------------------------
		/**
		 * Update on the database the values for a password.
		 * 
		 * @param subscriber
		 *            the subscriber to be updated.
		 * @throws SQLException
		 */
		public static void updatePassword(Subscriber subs) throws SQLException {
			
			Connection c = DatabaseConnection.getConnection();
			
			Statement updatePasswordStmt = c.createStatement();
			
			String request = "update subscriber set password='"+subs.getPassword()+"' where username='"+subs.getUsername()+"'";
			
			updatePasswordStmt.executeUpdate(request);
			
			updatePasswordStmt.close();
					
			c.close();
		}

	// -----------------------------------------------------------------------------
	/**
	 * Delete from the database a specific subscriber.
	 * 
	 * @param subs
	 *            the subscriber to be deleted.
	 * @throws SQLException
	 */
	public static void delete(Subscriber subs) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		
		Statement deleteStmt = c.createStatement();
	
		String request = "delete subscriber where username='"+subs.getUsername()+"'";
		
		deleteStmt.executeUpdate(request);
		
		deleteStmt.close();
		c.close();
	}
	// -----------------------------------------------------------------------------
	
	/**
	 * Delete from the database a specific subscriber.
	 * 
	 * @param username
	 *            the username of the subscriber to be deleted.
	 * @throws SQLException
	 */
	public static void deleteByUsername(String username) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		
		Statement deleteStmt = c.createStatement();
	
		String request = "delete subscriber where username='"+username+"'";
		
		deleteStmt.executeUpdate(request);
		
		deleteStmt.close();
		c.close();
	}
	// -----------------------------------------------------------------------------
	
    
}


