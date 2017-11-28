package fr.uv1.bettingServices.bets;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.MyCalendar;

public class WinnerBet extends Bet {
	private Competitor winner;

	/**
	 * WinnerBet constructor.
	 * 
	 * @param competition
	 *            the competition on which the bet is.
	 * @param gambler
	 *            the subscriber who have bet.
	 * @param date
	 *            the date when the bet was made.
	 * @param coins
	 *            the number of coins on the bet.
	 * @param first
	 *            the competitor that the gambler except to arrived in 1st position.
	 * @throws BadParametersException
	 */
	public WinnerBet(Competition competition, Subscriber gambler, MyCalendar date, long coins, Competitor winner) throws BadParametersException {
		super(competition, gambler, date, coins);
		if (winner == null)
			throw new BadParametersException("Must provide a winner to create a bet");
		this.winner = winner;
	}
	
	/**
	 * WinnerBet constructor from database.
	 * 
	 * @param id
	 * 			the id of the bet
	 * @param competition
	 *            the competition on which the bet is.
	 * @param gambler
	 *            the subscriber who have bet.
	 * @param date
	 *            the date when the bet was made.
	 * @param coins
	 *            the number of coins on the bet.
	 * @param first
	 *            the competitor that the gambler except to arrived in 1st position.
	 * @throws BadParametersException
	 */
	public WinnerBet(int id, Competition competition, Subscriber gambler, MyCalendar date, long coins, Competitor winner){
		super(id, competition, gambler, date, coins);
		this.winner = winner;
	}
	
	/**
	 * Test if the bet is won.
	 * @return True if the bet is won. False otherwise.
	 * @throws ExistingCompetitorException 
	 */
	public boolean isWon() throws ExistingCompetitorException {
		if (competition.getResultType() == 2 && competition.getRankCompetitor(winner) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public Competitor getWinner() {
		return winner;
	}

	public String toString(){
		if(winner instanceof Athlete){
			return gambler.getUsername()+" made a winner bet of "+coins+" coins dated from "+date.toString()+" over "+((Athlete) winner).getFirstName()+" "+((Athlete) winner).getFamilyName()+
					" who participates to the competition "+competition.getName()+".";
		}
		if(winner instanceof Team){
			return gambler.getUsername()+" made a winner bet of "+coins+" coins dated from "+date.toString()+" over "+((Team) winner).getTeamName()+
					" who participates to the competition "+competition.getName()+".";
		}
		else return "Hello world!";
	}
}