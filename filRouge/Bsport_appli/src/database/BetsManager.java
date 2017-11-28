package fr.uv1.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.bets.DrawBet;
import fr.uv1.bettingServices.bets.PodiumBet;
import fr.uv1.bettingServices.bets.WinnerBet;
import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.subscribers.Subscriber;

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
 * @author BON Jerome & William CHEN
 */
public class BetsManager {
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
	public static int persist(Bet bet) throws SQLException {
		Integer id = null;
		Connection c = DatabaseConnection.getConnection();
		try {
			c.setAutoCommit(false);
			
			PreparedStatement psIdValue = c.prepareStatement("SELECT bet_seq.nextval as value_id FROM dual");
			ResultSet resultSet = psIdValue.executeQuery();
			
			while(resultSet.next()){id = resultSet.getInt("value_id");}
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
			PreparedStatement psPersist = c.prepareStatement("INSERT INTO bet(id_bet, gambler, competition, bet_date, tokens_number)  VALUES (?, ?, ?, ?, ?)");
					
			psPersist.setInt(1, id);
			psPersist.setString(2, bet.getGambler().getUsername());
			psPersist.setString(3, bet.getCompetition().getName());
			psPersist.setString(4, bet.getDate().toDatabaseString());
			psPersist.setLong(5, bet.getCoins());

			psPersist.executeUpdate();

			psPersist.close();
		c.close();

		return id;
	}
	// -----------------------------------------------------------------------------
	
	public static List<Bet> findAllByCompetition(String competitionName) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		List<Bet> bets = new ArrayList<Bet>();
		List<Integer> winnerBetsIds = new ArrayList<Integer>();
		List<Integer> drawBetsIds = new ArrayList<Integer>();
		List<Integer> podiumBetsIds = new ArrayList<Integer>();
		Connection c = DatabaseConnection.getConnection();
		
		PreparedStatement psSelectWinnerBet = c
				.prepareStatement("SELECT b.id_bet FROM bet b JOIN winner_bet wb ON wb.id_bet=b.id_bet WHERE competition=?");
		psSelectWinnerBet.setString(1,competitionName);
		ResultSet resultSetWinnerBet = psSelectWinnerBet.executeQuery();
		
		while(resultSetWinnerBet.next()){
			winnerBetsIds.add(resultSetWinnerBet.getInt("id_bet"));
		}
		
		resultSetWinnerBet.close();
		psSelectWinnerBet.close();
		
		
		
		PreparedStatement psSelectDrawBet = c
				.prepareStatement("SELECT b.id_bet FROM bet b JOIN draw_bet db ON db.id_bet=b.id_bet WHERE competition=?");
		psSelectDrawBet.setString(1,competitionName);
		ResultSet resultSetDrawBet = psSelectDrawBet.executeQuery();
		
		while(resultSetDrawBet.next()){
			drawBetsIds.add(resultSetDrawBet.getInt("id_bet"));
		}
		
		resultSetDrawBet.close();
		psSelectDrawBet.close();
		
		
		
		PreparedStatement psSelectPodiumBet = c
				.prepareStatement("SELECT b.id_bet FROM bet b JOIN podium_bet pb ON pb.id_bet=b.id_bet WHERE competition=?");
		psSelectPodiumBet.setString(1,competitionName);
		ResultSet resultSetPodiumBet = psSelectPodiumBet.executeQuery();
		
		while(resultSetPodiumBet.next()){
			podiumBetsIds.add(resultSetPodiumBet.getInt("id_bet"));
		}
		
		resultSetPodiumBet.close();
		psSelectPodiumBet.close();
		
		c.close();
		
		for(int id :winnerBetsIds){
			bets.add(WinnerBetsManager.findById(id));
		}
		for(int id :drawBetsIds){
			bets.add(DrawBetsManager.findById(id));
		}
		for(int id :podiumBetsIds){
			bets.add(PodiumBetsManager.findById(id));
		}
		
		return bets;
	}
	
	
	public static List<Bet> findAllBySubscriber(Subscriber gambler) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		List<Bet> bets = new ArrayList<Bet>();
		List<Integer> winnerBetsIds = new ArrayList<Integer>();
		List<Integer> drawBetsIds = new ArrayList<Integer>();
		List<Integer> podiumBetsIds = new ArrayList<Integer>();
		String username = gambler.getUsername();
		Connection c = DatabaseConnection.getConnection();
		
		PreparedStatement psSelectWinnerBet = c
				.prepareStatement("SELECT b.id_bet FROM bet b JOIN winner_bet wb ON wb.id_bet=b.id_bet WHERE gambler=?");
		psSelectWinnerBet.setString(1,username);
		ResultSet resultSetWinnerBet = psSelectWinnerBet.executeQuery();
		
		while(resultSetWinnerBet.next()){
			winnerBetsIds.add(resultSetWinnerBet.getInt("id_bet"));
		}
		
		resultSetWinnerBet.close();
		psSelectWinnerBet.close();
		
		
		
		PreparedStatement psSelectDrawBet = c
				.prepareStatement("SELECT b.id_bet FROM bet b JOIN draw_bet db ON db.id_bet=b.id_bet WHERE gambler=?");
		psSelectDrawBet.setString(1,username);
		ResultSet resultSetDrawBet = psSelectDrawBet.executeQuery();
		
		while(resultSetDrawBet.next()){
			drawBetsIds.add(resultSetDrawBet.getInt("id_bet"));
		}
		
		resultSetDrawBet.close();
		psSelectDrawBet.close();
		
		
		
		PreparedStatement psSelectPodiumBet = c
				.prepareStatement("SELECT b.id_bet FROM bet b JOIN podium_bet pb ON pb.id_bet=b.id_bet WHERE gambler=?");
		psSelectPodiumBet.setString(1,username);
		ResultSet resultSetPodiumBet = psSelectPodiumBet.executeQuery();
		
		while(resultSetPodiumBet.next()){
			podiumBetsIds.add(resultSetPodiumBet.getInt("id_bet"));
		}
		
		resultSetPodiumBet.close();
		psSelectPodiumBet.close();
		
		c.close();
		
		for(int id :winnerBetsIds){
			bets.add(WinnerBetsManager.findById(id));
		}
		for(int id :drawBetsIds){
			bets.add(DrawBetsManager.findById(id));
		}
		for(int id :podiumBetsIds){
			bets.add(PodiumBetsManager.findById(id));
		}
		
		return bets;
	}
	
	
	
	
	
	public static List<Bet> findAllByCompetitor(int CompetitorId) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		List<Bet> bets = new ArrayList<Bet>();
		
		Connection c = DatabaseConnection.getConnection();
		
		PreparedStatement psSelectPodium = c.prepareStatement(
				"SELECT b.id_bet FROM bet b JOIN podium_bet pb ON pb.id_bet = b.id_bet " +
				"WHERE pb.third = ? OR pb.second = ? OR pb.first = ?");
		
		psSelectPodium.setInt(1, CompetitorId);
		psSelectPodium.setInt(2, CompetitorId);
		psSelectPodium.setInt(3, CompetitorId);
		
		ResultSet resultSetPodium = psSelectPodium.executeQuery();
		
		while(resultSetPodium.next()){
			bets.add(PodiumBetsManager.findById(resultSetPodium.getInt("id_bet")));
		}
		
		resultSetPodium.close();
		psSelectPodium.close();
		
		PreparedStatement psSelectDraw = c.prepareStatement(
				"SELECT b.id_bet FROM bet b JOIN draw_bet db ON db.id_bet=b.id_bet WHERE db.competitor1 = ? OR db.competitor2 = ?");
		
		psSelectDraw.setInt(1, CompetitorId);
		psSelectDraw.setInt(2, CompetitorId);
		
		ResultSet resultSetDraw = psSelectDraw.executeQuery();
		
		while(resultSetDraw.next()){
			bets.add(DrawBetsManager.findById(resultSetDraw.getInt("id_bet")));
		}
		
		resultSetDraw.close();
		psSelectDraw.close();
		
		PreparedStatement psSelectWinner = c.prepareStatement(
				"SELECT b.id_bet FROM bet b JOIN winner_bet wn ON wn.id_bet=b.id_bet WHERE wn.winner = ?");
		
		psSelectWinner.setInt(1, CompetitorId);
		
		ResultSet resultSetWinner = psSelectWinner.executeQuery();
		
		while(resultSetWinner.next()){
			bets.add(WinnerBetsManager.findById(resultSetWinner.getInt("id_bet")));
		}
		
		
		resultSetWinner.close();
		psSelectWinner.close();
		
		c.close();
		
		return bets;
	}
	
	/**
	 * Delete from the database a specific bet.
	 * 
	 * @param bet
	 *            the bet to be deleted.
	 * @throws SQLException
	 */
	public static void delete(Bet bet) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		Statement deleteBetStatement = c.createStatement();
		String request = "DELETE bet WHERE id_bet="+bet.getId();
		
		deleteBetStatement.executeUpdate(request);
		deleteBetStatement.close();

		c.close();
	}
	
	/**
	 * Delete from the database a specific bet specified by its id.
	 * 
	 * @param id
	 *            the id of the bet to be deleted.
	 * @throws SQLException
	 */
	public static void deleteById(int id) throws SQLException {
		Connection c = DatabaseConnection.getConnection();
		Statement deleteBetStatement = c.createStatement();
		
		String request = "DELETE bet WHERE id_bet="+id;
		
		deleteBetStatement.executeUpdate(request);
		deleteBetStatement.close();

		c.close();
	}
	
	public static boolean existsBetOnCompetition(String competitionName)throws SQLException{
		boolean betExists = false;
		
		Connection c = DatabaseConnection.getConnection();
		PreparedStatement psSelect = c
				.prepareStatement("SELECT * FROM bet WHERE competition=?");
		psSelect.setString(1,competitionName);
		ResultSet resultSet = psSelect.executeQuery();
		
		while(resultSet.next()){
			betExists = true;
		}
		
		return betExists;
	}
	public static void deleleAllBetsOnCompetition(Competition competition) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		WinnerBetsManager.deleteAllOnCompetition(competition);
		DrawBetsManager.deleteAllOnCompetition(competition);
		PodiumBetsManager.deleteAllOnCompetition(competition);
	}
	
	public static void deleteAllBetsOfSubscriber(Subscriber gambler) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{
		for(WinnerBet winnerBet: WinnerBetsManager.findBySubscriber(gambler.getUsername())){
			WinnerBetsManager.delete(winnerBet);
		}
		for(DrawBet drawBet: DrawBetsManager.findBySubscriber(gambler.getUsername())){
			DrawBetsManager.delete(drawBet);
		}
		for(PodiumBet podiumBet: PodiumBetsManager.findBySubscriber(gambler.getUsername())){
			PodiumBetsManager.delete(podiumBet);
		}
		
	}
	
	public static List<Bet> findNotSoldBetsBySubscriber(Subscriber gambler) throws SQLException, BadParametersException, CompetitionException, SubscriberException, ExistingCompetitorException{

        List<Bet> bets = new ArrayList<Bet>();
        
        Connection c = DatabaseConnection.getConnection();
        
        
        PreparedStatement psSelectPodiumBet = c.prepareStatement("SELECT b.id_bet FROM bet b JOIN competition c ON b.competition = c.name "+
                                            "JOIN podium_bet pb ON pb.id_bet=b.id_bet WHERE c.is_sold = 0 AND b.gambler =?");
        
        psSelectPodiumBet.setString(1,gambler.getUsername());
        ResultSet resultSetPodiumBet = psSelectPodiumBet.executeQuery();
        
        while(resultSetPodiumBet.next()){
            bets.add(PodiumBetsManager.findById(resultSetPodiumBet.getInt("id_bet")));
        }
        
        resultSetPodiumBet.close();
        psSelectPodiumBet.close();
        
        
        PreparedStatement psSelectDrawBet = c.prepareStatement("SELECT b.id_bet FROM bet b JOIN competition c ON b.competition = c.name "+
                                            "JOIN draw_bet db ON db.id_bet=b.id_bet WHERE c.is_sold = 0 AND b.gambler =?");
        
        psSelectDrawBet.setString(1,gambler.getUsername());
        ResultSet resultSetDrawBet = psSelectDrawBet.executeQuery();
        
        while(resultSetDrawBet.next()){
            bets.add(DrawBetsManager.findById(resultSetDrawBet.getInt("id_bet")));
        }
        
        resultSetDrawBet.close();
        psSelectDrawBet.close();
        
        PreparedStatement psSelectWinnerBet = c.prepareStatement("SELECT b.id_bet FROM bet b JOIN competition c ON b.competition = c.name "+
                                            "JOIN winner_bet wb ON wb.id_bet=b.id_bet WHERE c.is_sold = 0 AND b.gambler =?");
        
        psSelectWinnerBet.setString(1,gambler.getUsername());
        ResultSet resultSetWinnerBet = psSelectWinnerBet.executeQuery();
        
        while(resultSetWinnerBet.next()){
            bets.add(WinnerBetsManager.findById(resultSetWinnerBet.getInt("id_bet")));
        }
        
        resultSetWinnerBet.close();
        psSelectWinnerBet.close();
        
        c.close();
        return bets;
	 }
	 
	 
	 public static long getTotalCoinsBet(Subscriber gambler) throws SQLException{

        long coinsAmount = 0;
        
        Connection c = DatabaseConnection.getConnection();
        
        
        PreparedStatement psSelect = c.prepareStatement("SELECT SUM(b.tokens_number) FROM bet b " +
                                                        "JOIN competition c ON b.COMPETITION=c.NAME " +
                                                        "WHERE b.GAMBLER = ?");
        psSelect.setString(1,gambler.getUsername());
        ResultSet resultSet = psSelect.executeQuery();
        
        while(resultSet.next()){
            coinsAmount = resultSet.getInt("SUM(b.tokens_number)");
        }
        
        resultSet.close();
        psSelect.close();
        
        c.close();
        return coinsAmount;
	 }
	

	// -----------------------------------------------------------------------------
}
