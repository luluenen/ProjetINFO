package fr.uv1.bettingServices.bets;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.MyCalendar;

public class DrawBet extends Bet {
	private Competitor first;
	private Competitor second;
	
	
	/**
	 * DrawBet constructor.
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
	 *            one of the two competitors the gambler want to bet on.
	 * @param second
	 *            one of the two competitors the gambler want to bet on.
	 * @throws BadParametersException
	 */
	public DrawBet(Competition competition, Subscriber gambler, MyCalendar date, long coins, Competitor first, Competitor second) throws BadParametersException {
		super(competition, gambler, date, coins);
		if (first == null || second == null)
			throw new BadParametersException("Must provide competitors to create a bet");
		this.first = first;
		this.second = second;
	}
	
	/**
	 * DrawBet constructor from database.
	 * 
	 *  @param id
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
	 *            one of the two competitors the gambler want to bet on.
	 * @param second
	 *            one of the two competitors the gambler want to bet on.
	 * @throws BadParametersException 
	 */
	public DrawBet(Integer id, Competition competition, Subscriber gambler,
			MyCalendar date, int coins, Competitor first, Competitor second){
		
		super(id, competition, gambler, date, coins);
		this.first = first;
		this.second = second;
	}

	/**
	 * Test if the bet is won.
	 * @return True if the bet is won. False otherwise.
	 * @throws ExistingCompetitorException 
	 */
	public boolean isWon() throws ExistingCompetitorException {
		if (competition.getResultType() ==  1 && competition.getRankCompetitor(first) == 1 && competition.getRankCompetitor(second) == 1)
			return true;
		else
			return false;
		
	}
	
	public Competitor getFirst() {
		return first;
	}

	public Competitor getSecond() {
		return second;
	}

	public String toString(){
		if(first instanceof Athlete){
			return gambler.getUsername()+" made a draw bet of "+coins+" coins dated from "+date.toString()+" between "+((Athlete) first).getFirstName()+" "+((Athlete) first).getFamilyName()+
				" and "+((Athlete) second).getFirstName()+" "+((Athlete) second).getFamilyName()+" who participate to the competition "+competition.getName()+".";
		}
		if(first instanceof Team){
			return gambler.getUsername()+" made a draw bet of "+coins+" coins dated from "+date.toString()+" between "+((Team) first).getTeamName()+ " and "+((Team) second).getTeamName()+
					" who participate to the competition "+competition.getName()+".";
		}
		else return "Hello world!";
	}
}
