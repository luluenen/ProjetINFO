package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.bets.PodiumBet;
import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.DateConvertor;
import fr.uv1.utils.MyCalendar;

/**
 * DAO class (<i>Data Access Object</i>) for the {@link Bet} class. This class
 * provides the CRUD functionalities :<br>
 * <ul>
 * <li><b>C</b>: create a new bet in the database.
 * <li><b>R</b>: retrieve (or read) a (list of)bet(s) from the database.
 * <li><b>U</b>: update the values stored in the database for a bet.
 * <li><b>DumBet</b>: delete a bet in the database.
 * </ul>
 * 
 * @author William Chen
 */
public class PodiumBetsManager {
	// -----------------------------------------------------------------------------
	/**
	 * Store a bet in the database for a specific subscriber (the subscriber is
	 * included inside the Bet object). This bet is not stored yet, so his
	 * <code>id</code> value is <code>NULL</code>. Once the bet is stored, the
	 * method returns the bet with the <code>id</code> value setted.
	 * 
	 * @param bet
	 *            the bet to be stored.
	 * @return the bet with the updated value for the id.
	 * @throws SQLException
	 */
	public static void persist(PodiumBet podiumBet) throws SQLException {
		// i) Stores the podiumBet as a simple bet.
		// ii) Retrieves the id under which the current podiumBet, previously considered as a simple bet, has been stored in the database.
		Connection c = DatabaseConnection.getConnection();
			
		int betId = BetsManager.persist((Bet) podiumBet);
		
		PreparedStatement psPersist = c.prepareStatement("INSERT INTO podium_bet(id_bet, first, second, third)  VALUES (?, ?, ?, ?)");
					
			psPersist.setInt(1, betId);
			
			if(podiumBet.getFirst() instanceof Team){
				psPersist.setInt(2, ((Team) (podiumBet.getFirst())).getId());
			}
			else if(podiumBet.getFirst() instanceof Athlete){
				psPersist.setInt(2, ((Athlete) (podiumBet.getFirst())).getId());
			}
			
			if(podiumBet.getSecond() instanceof Team){
				psPersist.setInt(3, ((Team) (podiumBet.getSecond())).getId());
			}
			else if(podiumBet.getSecond() instanceof Athlete){
				psPersist.setInt(3, ((Athlete) (podiumBet.getSecond())).getId());
			}
			
			if(podiumBet.getThird() instanceof Team){
				psPersist.setInt(4, ((Team) (podiumBet.getThird())).getId());
			}
			else if(podiumBet.getThird() instanceof Athlete){
				psPersist.setInt(4, ((Athlete) (podiumBet.getThird())).getId());
			}

			psPersist.executeUpdate();
			psPersist.close();
    }

	// -----------------------------------------------------------------------------
	/**
	 * Find a bet by his id.
	 * 
	 * @param idSELECT bet_seq.nextval FROM bet_seq;
	 *            the id of the bet to retrieve.
	 * @return the bet or null if the id does not exist in the database.
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static PodiumBet findById(Integer id) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		// Creating a podiumBet knowing its id.
		// Initialising the different attributes necessary to create such object.
		
		PodiumBet podiumBet = null;
		
		Competition competition = null;
		Subscriber gambler = null;
		MyCalendar date = null;
		int coins = 0;
		Competitor first = null;
		Competitor second = null;
		Competitor third = null;
		
		// Retrieving all the data needed to create the podium object namely name of the competition, name of the gambler.
		String competitionName = null;
		String gamblerName = null;
		String bet_date = null;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT * FROM bet WHERE id_bet=?");
		psSelect.setInt(1, id);
		
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			competitionName = resultSet.getString("competition");
			gamblerName =resultSet.getString("gambler");
			bet_date = resultSet.getString("bet_date");
			coins = resultSet.getInt("tokens_number");
		}
		
		resultSet.close();
		psSelect.close();
		
		
		competition = CompetitionManager.findByCompName(competitionName);		
		
		PreparedStatement psSelectSubscriber = c.prepareStatement("SELECT * FROM subscriber WHERE username='"+gamblerName+"'");
		
		ResultSet resultSetSubscriber = psSelectSubscriber.executeQuery();
		
		while(resultSetSubscriber.next()){
			gambler = new Subscriber(resultSetSubscriber.getString("LASTNAME"),resultSetSubscriber.getString("FIRSTNAME"),gamblerName,DateConvertor.String2MyCalendar(resultSetSubscriber.getString("BIRTHDATE")));
		}
		
		resultSetSubscriber.close();
		psSelectSubscriber.close();
		
		date = DateConvertor.String2MyCalendar(bet_date);
		
		int firstId = 0;
		int secondId = 0;
		int thirdId = 0;
		
		PreparedStatement psSelect2 = c.prepareStatement("SELECT * FROM podium_bet WHERE id_bet=?");
		
		psSelect2.setInt(1, id);
		
		ResultSet resultSet2 = psSelect2.executeQuery();
		
		while(resultSet2.next()){
			firstId = resultSet2.getInt("first");
			secondId = resultSet2.getInt("second");
			thirdId = resultSet2.getInt("third");
		}
		resultSet2.close();
		psSelect2.close();
		
		if(AthleteManager.findByAthleteId(firstId)!=null){
			first = AthleteManager.findByAthleteId(firstId);
		}
		else{
			first = TeamManager.findByTeamId(firstId);
		}
		
		if(AthleteManager.findByAthleteId(secondId)!=null){
			second = AthleteManager.findByAthleteId(secondId);
		}
		else{
			second = TeamManager.findByTeamId(secondId);
		}
		
		if(AthleteManager.findByAthleteId(thirdId)!=null){
			third = AthleteManager.findByAthleteId(thirdId);
		}
		else{
			third = TeamManager.findByTeamId(thirdId);
		}
		//
		c.close();
		
		podiumBet = new PodiumBet(id, competition, gambler, date, coins, first, second, third);
		return podiumBet;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the podium bets for a specific subscriber in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static List<PodiumBet> findBySubscriber(String username)
			throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT id_bet FROM bet WHERE gambler=?");
		psSelect.setString(1, username);
		ResultSet resultSet = psSelect.executeQuery();
		
		List<PodiumBet> bets = new ArrayList<PodiumBet>();
		List<Integer> betsId = new ArrayList<Integer>();
		
		while(resultSet.next()){
			betsId.add(resultSet.getInt("id_bet"));
		}

		resultSet.close();
		psSelect.close();
		
		for(Integer i:betsId){
			Bet temporaryBet = null;
			temporaryBet = findById(i);
			((PodiumBet) temporaryBet).setId(i);
			bets.add((PodiumBet) temporaryBet);
		}
		
		return bets;
	}


	// -----------------------------------------------------------------------------
	/**
	 * Delete from the database a specific bet.
	 * 
	 * @param bet
	 *            the bet to be deleted.
	 * @throws SQLException
	 */
	public static void delete(PodiumBet podiumBet) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		
		if(podiumBet.getId()==0){
			int id = 0;
			
			PreparedStatement psSelectId = c.prepareStatement("SELECT b.id_bet FROM bet b JOIN podium_bet pb ON b.id_bet = pb.id_bet WHERE gambler=? AND competition=? AND tokens_number=?");
			psSelectId.setString(1, podiumBet.getGambler().getUsername());
			psSelectId.setString(2, podiumBet.getCompetition().getName());
			psSelectId.setLong(3,podiumBet.getCoins());

			ResultSet resultSetId = psSelectId.executeQuery();
			
			while(resultSetId.next()){
				id = resultSetId.getInt("id_bet");
			}
			
			resultSetId.close();
			psSelectId.close();
			
			podiumBet.setId(id);
		}
		
		PreparedStatement psUpdate = c.prepareStatement("delete from podium_bet where id_bet=?");
		psUpdate.setInt(1, podiumBet.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		
		PreparedStatement psUpdate2 = c.prepareStatement("delete from bet where id_bet=?");
		psUpdate2.setInt(1, podiumBet.getId());
		psUpdate2.executeUpdate();
		psUpdate2.close();
		
		c.close();
	}
	
	public static void deleteAllOnCompetition(Competition deletedCompetition) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		List<Integer> betsIdToBeDeleted = new ArrayList<Integer>();
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT pb.id_bet FROM podium_bet pb JOIN bet b ON b.id_bet = pb.id_bet"+
				"                                        WHERE b.competition=?");
		psSelect.setString(1, deletedCompetition.getName());
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			betsIdToBeDeleted.add(resultSet.getInt("id_bet"));
		}
		
		for(Integer i:betsIdToBeDeleted){
			delete(findById(i));
		}
		
	}
	// -----------------------------------------------------------------------------
	public static boolean existsPodiumBetOnCompetition(String competitionName)throws SQLException{
		boolean betExists = false;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("SELECT bet.id_bet FROM bet JOIN podium_bet ON bet.id_bet = podium_bet.id_bet WHERE competition=?");
		psSelect.setString(1,competitionName);
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			betExists = true;
		}
		
		return betExists;
	}
	
}
