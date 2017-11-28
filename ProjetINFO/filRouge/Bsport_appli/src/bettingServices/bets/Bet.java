package fr.uv1.bettingServices.bets;

import java.sql.SQLException;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.database.DrawBetsManager;
import fr.uv1.database.PodiumBetsManager;
import fr.uv1.database.WinnerBetsManager;
import fr.uv1.utils.MyCalendar;

public abstract class Bet {
	protected long coins;
	protected MyCalendar date;
	protected Competition competition;
	protected Subscriber gambler;
	protected int id;
	
	/**
	 * Bet constructor.
	 * 
	 * @param competition
	 *            the competition on which the bet is.
	 * @param gambler
	 *            the subscriber who have bet.
	 * @param date
	 *            the date when the bet was made.
	 * @param coins
	 *            the number of coins on the bet.
	 * @throws BadParametersException
	 */
	public Bet(Competition competition, Subscriber gambler, MyCalendar date, long coins) throws BadParametersException {
		if (competition == null)
			throw new BadParametersException("Must provide a competition to create a bet");
		else if (gambler == null)
			throw new BadParametersException("Must provide a gambler to create a bet");
		else if (date == null)
			throw new BadParametersException("Must provide a date to create a bet");
		else if (coins <= 0)
			throw new BadParametersException("Must provide a strictly positive amount of coins");
		if (competition.isClosed())
			throw new BadParametersException("Must provide an ongoing competition");
		this.competition = competition;
		this.gambler = gambler;
		this.date = date;
		this.coins = coins;
		this.id = 0;
	}
	
	/**
	 * Bet constructor from the database.
	 * 
	 * @param id
	 *            the id of the bet.
	 * @param competition
	 *            the competition on which the bet is.
	 * @param gambler
	 *            the subscriber who have bet.
	 * @param date
	 *            the date when the bet was made.
	 * @param coins
	 *            the number of coins on the bet.
	 */
	public Bet(int id, Competition competition, Subscriber gambler, MyCalendar date, long coins){
		this.competition = competition;
		this.gambler = gambler;
		this.date = date;
		this.coins = coins;
		this.id = id;
	}
	
	/**
	 * Test if the bet is won.
	 * @return True if the bet is won. False otherwise.
	 * @throws ExistingCompetitorException 
	 */
	public abstract boolean isWon() throws ExistingCompetitorException;
	
	/**
	 * Cancel a bet by giving back his coins to the gambler.
	 * @throws SubscriberException 
	 * @throws SQLException 
	 */
	public void cancelBet() throws SubscriberException, SQLException {
		gambler.addRemoveCoins(this.coins);
		this.coins = 0;
		if(this instanceof WinnerBet){
			WinnerBetsManager.delete((WinnerBet) this);
		}
		else if(this instanceof DrawBet){
			DrawBetsManager.delete((DrawBet) this);
		}
		else if(this instanceof PodiumBet){
			PodiumBetsManager.delete((PodiumBet) this);
		}
	}

	public long getCoins() {
		return coins;
	}

	public MyCalendar getDate() {
		return date;
	}

	public Competition getCompetition() {
		return competition;
	}

	public Subscriber getGambler() {
		return gambler;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int i){
		this.id = i;
	}
}
