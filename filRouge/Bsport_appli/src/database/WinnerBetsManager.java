package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.bets.WinnerBet;
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
public class WinnerBetsManager {
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
	public static void persist(WinnerBet winnerBet) throws SQLException {
		// i) Stores the winnerBet as a simple bet.
		// ii) Retrieves the id under which the current winnerBet, previously considered as a simple bet, has been stored in the database.
		Connection c = DatabaseConnection.getConnection();
			
		int betId = BetsManager.persist((Bet) winnerBet);
		
		PreparedStatement psPersist = c.prepareStatement("INSERT INTO winner_bet(id_bet, winner)  VALUES (?, ?)");
					
			psPersist.setInt(1, betId);
			
			if(winnerBet.getWinner() instanceof Team){
				psPersist.setInt(2, ((Team) (winnerBet.getWinner())).getId());
			}
			else if(winnerBet.getWinner() instanceof Athlete){
				psPersist.setInt(2, ((Athlete) (winnerBet.getWinner())).getId());
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
	 * @throws BadParametersException 
	 * @throws CompetitionException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static WinnerBet findById(Integer id) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		// Creating a winnerBet knowing its id.
		// Initialising the different attributes necessary to create such object.
		WinnerBet winnerBet = null;
		
		Competition competition = null;
		Subscriber gambler = null;
		MyCalendar date = null;
		int coins = 0;
		Competitor winner = null;
		
		// Retrieving all the data needed to create the winner object namely name of the competition, name of the gambler.
		String competitionName = null;
		String gamblerName = null;
		String bet_date = null;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT * FROM bet WHERE id_bet= ?");
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
		
		// 
		
		competition = CompetitionManager.findByCompName(competitionName);		
		//Seeking the gambler because Tom is lazy.
		
				PreparedStatement psSelectSubscriber = c.prepareStatement("SELECT * FROM subscriber WHERE username='"+gamblerName+"'");
				
				ResultSet resultSetSubscriber = psSelectSubscriber.executeQuery();
				
				while(resultSetSubscriber.next()){
					gambler = new Subscriber(resultSetSubscriber.getString("LASTNAME"),resultSetSubscriber.getString("FIRSTNAME"),gamblerName,DateConvertor.String2MyCalendar(resultSetSubscriber.getString("BIRTHDATE")));
				}
				
				resultSetSubscriber.close();
				psSelectSubscriber.close();
				
				//
		date = DateConvertor.String2MyCalendar(bet_date);
		
		int winnerId = 0;
		
		PreparedStatement psSelect2 = c.prepareStatement("SELECT * FROM winner_bet WHERE id_bet=?");
		psSelect2.setInt(1, id);
		ResultSet resultSet2 = psSelect2.executeQuery();
		
		while(resultSet2.next()){
			winnerId = resultSet2.getInt("winner");
		}
		resultSet2.close();
		psSelect2.close();
		
		if(AthleteManager.findByAthleteId(winnerId)!=null){
			winner = AthleteManager.findByAthleteId(winnerId);
		}
		else{
			winner = TeamManager.findByTeamId(winnerId);
		}
		
		//
		c.close();

		winnerBet = new WinnerBet(id, competition, gambler, date, coins, winner);
		return winnerBet;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the winner bets for a specific subscriber in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static List<WinnerBet> findBySubscriber(String username)
			throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT id_bet FROM bet WHERE gambler=?");
		psSelect.setString(1, username);
		ResultSet resultSet = psSelect.executeQuery();
		
		List<WinnerBet> bets = new ArrayList<WinnerBet>();
		List<Integer> betsId = new ArrayList<Integer>();
		
		while(resultSet.next()){
			betsId.add(resultSet.getInt("id_bet"));
		}

		resultSet.close();
		psSelect.close();
		
		for(Integer i:betsId){
			Bet temporaryBet = null;
			temporaryBet = findById(i);
			((WinnerBet) temporaryBet).setId(i);
			bets.add((WinnerBet) temporaryBet);
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
	public static void delete(WinnerBet winnerBet) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		Statement deleteWinnerBetStatement = c.createStatement();
		
		if(winnerBet.getId()==0){
			int id = 0;
			
			PreparedStatement psSelectId = c.prepareStatement("SELECT b.id_bet FROM bet b JOIN winner_bet wn ON b.id_bet = wn.id_bet WHERE gambler=? AND competition=? AND tokens_number=?");
			psSelectId.setString(1, winnerBet.getGambler().getUsername());
			psSelectId.setString(2, winnerBet.getCompetition().getName());
			psSelectId.setLong(3,winnerBet.getCoins());

			ResultSet resultSetId = psSelectId.executeQuery();
			
			while(resultSetId.next()){
				id = resultSetId.getInt("id_bet");
			}
			
			resultSetId.close();
			psSelectId.close();
			
			winnerBet.setId(id);
		}
		
			String request = "DELETE FROM winner_bet WHERE id_bet="+winnerBet.getId();
		
			deleteWinnerBetStatement.executeUpdate(request);
			deleteWinnerBetStatement.close();
		
			BetsManager.delete((Bet) winnerBet);
		
			c.close();
	}
	
	public static void deleteAllOnCompetition(Competition deletedCompetition) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		List<Integer> betsIdToBeDeleted = new ArrayList<Integer>();
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT wb.id_bet FROM winner_bet wb JOIN bet b ON b.id_bet = wb.id_bet"+
				"                                        WHERE b.competition=?");
		psSelect.setString(1, deletedCompetition.getName());
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			betsIdToBeDeleted.add(resultSet.getInt("id_bet"));
		}
		
		for(Integer i:betsIdToBeDeleted){
			WinnerBetsManager.delete(findById(i));
		}
		
	}
	// -----------------------------------------------------------------------------
	
	public static boolean existsWinnerBetOnCompetition(String competitionName)throws SQLException{
		boolean betExists = false;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("SELECT bet.id_bet FROM bet JOIN winner_bet ON bet.id_bet = winner_bet.id_bet WHERE competition=?");
		psSelect.setString(1,competitionName);
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			betExists = true;
		}
		
		return betExists;
	}
	
}
