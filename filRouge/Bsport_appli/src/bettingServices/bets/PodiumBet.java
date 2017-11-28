package fr.uv1.bettingServices.bets;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.utils.MyCalendar;

public class PodiumBet extends Bet {
	private Competitor first;
	private Competitor second;
	private Competitor third;
	
	/**
	 * PodiumBet constructor.
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
	 * @param second
	 *            the competitor that the gambler except to arrived in 2nd position.
	 * @param third
	 *            the competitor that the gambler except to arrived in 3rd position.
	 * @throws BadParametersException
	 */
	public PodiumBet(Competition competition, Subscriber gambler, MyCalendar date, long coins, Competitor first, Competitor second, Competitor third) throws BadParametersException {
		super(competition, gambler, date, coins);
		if (first == null || second == null || third == null)
			throw new BadParametersException("Must provide competitors to create a bet");
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	/**
	 * PodiumBet constructorfrom database.
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
	 * @param second
	 *            the competitor that the gambler except to arrived in 2nd position.
	 * @param third
	 *            the competitor that the gambler except to arrived in 3rd position.
	 */
	public PodiumBet(int id, Competition competition, Subscriber gambler, MyCalendar date, long coins, Competitor first, Competitor second, Competitor third){
		super(id ,competition, gambler, date, coins);
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	/**
	 * Test if the bet is won.
	 * @return True if the bet is won. False otherwise.
	 * @throws ExistingCompetitorException 
	 */
	public boolean isWon() throws ExistingCompetitorException {
		if (competition.getResultType() ==  2 && competition.getRankCompetitor(first) == 1 && competition.getRankCompetitor(second) == 2 && competition.getRankCompetitor(third) == 3)
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

	public Competitor getThird() {
		return third;
	}
	
	public String toString(){
		if(first instanceof Athlete){
			return gambler.getUsername()+" made a podium bet of "+coins+" coins dated from "+date.toString()+", the podium being \n First place: "+((Athlete) first).getFirstName()+" "+((Athlete) first).getFamilyName()+
				"\n Second place: "+((Athlete) second).getFirstName()+" "+((Athlete) second).getFamilyName()+"\n Third place: "+((Athlete) third).getFirstName()+" "+((Athlete) first).getFamilyName()+"\n who participate to the competition "+competition.getName()+".";
		}
		if(first instanceof Team){
			return gambler.getUsername()+" made a podium bet of "+coins+" coins dated from "+date.toString()+", the podium being \n First place: "+((Team) first).getTeamName()+ "\n Second place: "+((Team) second).getTeamName()+"\n Third place: "+((Team) third).getTeamName()
					+"\n who participate to the competition "+competition.getName()+".";
		}
		else return "Hello world!";
	}
}
