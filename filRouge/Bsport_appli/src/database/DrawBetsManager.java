package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.bets.DrawBet;
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


public class DrawBetsManager {
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
	public static void persist(DrawBet drawBet) throws SQLException {
		// i) Stores the drawBet as a simple bet.
		// ii) Retrieves the id under which the current drawBet, previously considered as a simple bet, has been stored in the database.
		Connection c = DatabaseConnection.getConnection();
			
		int betId = BetsManager.persist((Bet) drawBet);
		
		PreparedStatement psPersist = c.prepareStatement("INSERT INTO draw_bet(id_bet, competitor1, competitor2)  VALUES (?, ?, ?)");
					
			psPersist.setInt(1, betId);
			
			if(drawBet.getFirst() instanceof Team){
				psPersist.setInt(2, ((Team) (drawBet.getFirst())).getId());
			}
			else if(drawBet.getFirst() instanceof Athlete){
				psPersist.setInt(2, ((Athlete) (drawBet.getFirst())).getId());
			}
			
			if(drawBet.getSecond() instanceof Team){
				psPersist.setInt(3, ((Team) (drawBet.getSecond())).getId());
			}
			else if(drawBet.getSecond() instanceof Athlete){
				psPersist.setInt(3, ((Athlete) (drawBet.getSecond())).getId());
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
	public static DrawBet findById(Integer id) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		// Creating a drawBet knowing its id.
		// Initialising the different attributes necessary to create such object.
		
		DrawBet drawBet = null;
		
		Competition competition = null;
		Subscriber gambler = null;
		MyCalendar date = null;
		int coins = 0;
		Competitor first = null;
		Competitor second = null;
		
		// Retrieving all the data needed to create the draw object namely name of the competition, name of the gambler.
		String competitionName = null;
		String gamblerName = null;
		String bet_date = null;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT * FROM bet WHERE id_bet=?");
		psSelect.setInt(1,  id);
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
		
		int firstId = 0;
		int secondId = 0;
		
		PreparedStatement psSelect2 = c.prepareStatement("SELECT * FROM draw_bet WHERE id_bet=?");
		psSelect2.setInt(1,  id);
		ResultSet resultSet2 = psSelect2.executeQuery();
		
		while(resultSet2.next()){
			firstId = resultSet2.getInt("competitor1");
			secondId = resultSet2.getInt("competitor2");
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
		
		//
		c.close();

		drawBet = new DrawBet(id, competition, gambler, date, coins, first, second);
		return drawBet;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the draw bets for a specific subscriber in the database.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws CompetitionException 
	 * @throws BadParametersException 
	 * @throws SubscriberException 
	 * @throws ExistingCompetitorException 
	 */
	public static List<DrawBet> findBySubscriber(String username)
			throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException {
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT id_bet FROM bet WHERE gambler=?");
		psSelect.setString(1,username);
		ResultSet resultSet = psSelect.executeQuery();
		
		List<DrawBet> bets = new ArrayList<DrawBet>();
		List<Integer> betsId = new ArrayList<Integer>();
		
		while(resultSet.next()){
			betsId.add(resultSet.getInt("id_bet"));
		}

		resultSet.close();
		psSelect.close();
		
		for(Integer i:betsId){
			bets.add(findById(i));
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
	public static void delete(DrawBet drawBet) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		
		if(drawBet.getId()==0){
			int id = 0;
			
			PreparedStatement psSelectId = c.prepareStatement("SELECT b.id_bet FROM bet b JOIN draw_bet db ON b.id_bet = db.id_bet WHERE gambler=? AND competition=? AND tokens_number=?");
			psSelectId.setString(1, drawBet.getGambler().getUsername());
			psSelectId.setString(2, drawBet.getCompetition().getName());
			psSelectId.setLong(3,drawBet.getCoins());

			ResultSet resultSetId = psSelectId.executeQuery();
			
			while(resultSetId.next()){
				id = resultSetId.getInt("id_bet");
			}
			
			resultSetId.close();
			psSelectId.close();
			
			drawBet.setId(id);
		}
		
		PreparedStatement psUpdate = c.prepareStatement("delete from draw_bet where id_bet=?");
		psUpdate.setInt(1, drawBet.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		
		BetsManager.delete((Bet) drawBet);
		
		c.close();
	}
	
	public static void deleteAllOnCompetition(Competition deletedCompetition) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		List<Integer> betsIdToBeDeleted = new ArrayList<Integer>();
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c.prepareStatement("SELECT db.id_bet FROM draw_bet db JOIN bet b ON b.id_bet = db.id_bet"+
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

	
	// ---------------------------------------------------------------------------
	public static boolean existsDrawBetOnCompetition(String competitionName)throws SQLException{
		boolean betExists = false;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("SELECT bet.id_bet FROM bet JOIN draw_bet ON bet.id_bet = draw_bet.id_bet WHERE competition=?");
		psSelect.setString(1,competitionName);
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			betExists = true;
		}
		
		return betExists;
	}
	
}
