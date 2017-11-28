package fr.uv1.bettingServices.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.bets.PodiumBet;
import fr.uv1.bettingServices.bets.WinnerBet;
import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.AuthentificationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Betting;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.database.AthleteManager;
import fr.uv1.database.BetsManager;
import fr.uv1.database.CompetitionManager;
import fr.uv1.database.PodiumBetsManager;
import fr.uv1.database.SubscribersManager;
import fr.uv1.database.TeamManager;
import fr.uv1.database.WinnerBetsManager;
import fr.uv1.utils.DateConvertor;
import fr.uv1.utils.MyCalendar;

/**
 * 
 * @author sjerbi + tmarcuzz
 * <br>
 * <br>
 * 		This class implements the betting interface
 *
 *
 */

public class BSport implements Betting {
	
	private final String MANAGER_PASSWORD = "1L0v31nF0";
		
	
	/***********************************************************************
	 * MANAGER FONCTIONNALITIES
	 ***********************************************************************/

	/**
	 * Authentificate manager.
	 * 
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 */
	public void AuthentificateMngr(String managerPwd) throws AuthentificationException{
		if (!managerPwd.equals(MANAGER_PASSWORD))
			throw new AuthentificationException("The manager's password is incorrect.");
	}
	
	/**
	 * register a subscriber (person).
	 * 
	 * @param lastName
	 *            the last name of the subscriber.
	 * @param firstName
	 *            the first name of the subscriber.
	 * @param username
	 *            the username of the subscriber.
	 * @param borndate
	 *            the borndate of the subscriber.   
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 * @throws ExistingSubscriberException
	 *             raised if a subscriber exists with the same username.
	 * @throws SubscriberException
	 *             raised if subscriber is minor.
	 * @throws BadParametersException
	 *             raised if last name, first name, username or borndate are
	 *             invalid or not instantiated.
	 * 
	 * @return password for the new subscriber.
	 */
	public String subscribe(String lastName, String firstName, String username,
			String borndate, String managerPwd) throws AuthentificationException,
			ExistingSubscriberException, SubscriberException,
			BadParametersException {
		
		AuthentificateMngr(managerPwd);
		
		Subscriber subs = null;		
		
		try {
			
			subs = SubscribersManager.findByUsername(username);
			
		
			if (subs != null){
				throw new ExistingSubscriberException("The username already exists!");
			}
			
			Subscriber newSubs = new Subscriber(lastName, firstName, username, DateConvertor.String2MyCalendar(borndate));
		
			SubscribersManager.persist(newSubs);
			
			return newSubs.getPassword();
			
		} catch (SQLException | CompetitionException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * delete a subscriber. His currents bets are canceled. He looses betted
	 * tokens.
	 * 
	 * @param username
	 *            the username of the subscriber.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 * @throws ExistingSubscriberException
	 *             raised if username is not registered.
	 * 
	 * @return number of tokens remaining in the subscriber's account
	 */
	public long unsubscribe(String username, String managerPwd)
			throws AuthentificationException, ExistingSubscriberException {
		
		Subscriber subs = null;
		
		AuthentificateMngr(managerPwd);
		
		try {
			
			subs = SubscribersManager.findByUsername(username);
			
			ArrayList<Bet> bets = (ArrayList<Bet>) BetsManager.findAllBySubscriber(subs);
			
			for (Bet bet : bets){
				bet.cancelBet();
			}
			
			BetsManager.deleteAllBetsOfSubscriber(subs);
		
			SubscribersManager.delete(subs);
		
		} catch (SQLException | BadParametersException | CompetitionException | SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		} 
		
		return subs.getBalance();
	}

	/**
	 * list subscribers.
	 * 
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 * 
	 * @return a list of list of strings:
	 *         <ul>
	 *         <li>subscriber's lastname</li>
	 *         <li>subscriber's firstname</li>
	 *         <li>subscriber's born date</li>
	 *         <li>subscriber's username</li>
	 *         </ul>
	 */
	public List<List<String>> listSubscribers(String managerPwd)
			throws AuthentificationException {
		
		AuthentificateMngr(managerPwd);
		
		List<Subscriber> subscriberList = null;
		
		List<List<String>> outputList = new ArrayList<List<String>>();
				
		try {
			
			subscriberList = SubscribersManager.findAll();
			
		} catch (SQLException | BadParametersException | CompetitionException
				| SubscriberException | ExistingCompetitorException e) {
	
			e.printStackTrace();
		}
		
		for (Subscriber subs : subscriberList){
			
			List<String> subscriberOutput = new ArrayList<String>();
			
			subscriberOutput.add(subs.getFamilyName());
			subscriberOutput.add(subs.getFirstName());
			subscriberOutput.add(subs.getBirthDate().toDatabaseString());
			subscriberOutput.add(subs.getUsername());
			
			outputList.add(subscriberOutput);
			
		}
		
		return outputList;
	}

	/**
	 * add a competition.
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param closingDate
	 *            last date to bet.
	 * @param competitors
	 *            the collection of competitors for the competition.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if a competition with the same name already exists.
	 * @throws CompetitionException
	 *             raised if closing date is in the past (competition closed);
	 *             there are less than two competitors; two or more competitors
	 *             are the same (firstname, lastname, borndate).
	 * @throws BadParametersException
	 *             raised if name of competition is invalid; list of competitors
	 *             not instantiated; (firstname, lastname, borndate or name if a
	 *             team competitor) of one or more of the competitors is
	 *             invalid.
	 */
	public void addCompetition(String competition, Calendar closingDate,
			Collection<Competitor> competitors, String managerPwd)
			throws AuthentificationException, ExistingCompetitionException,
			CompetitionException, BadParametersException {
		
		AuthentificateMngr(managerPwd);
		
		try {
			
			Competition compOnDB = CompetitionManager.findByCompName(competition);
			if (compOnDB != null){
				throw new ExistingCompetitionException("A competition with this name already exists.");
			}
			
			Competition comp = new Competition(competition, (MyCalendar) closingDate, (ArrayList<Competitor>) competitors );
		
			CompetitionManager.persist(comp);
			
		} catch (SQLException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * cancel a competition.
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if the closing date is in the past (competition
	 *             closed).
	 */
	public void cancelCompetition(String competition, String managerPwd)
			throws AuthentificationException, ExistingCompetitionException,
			CompetitionException {
		
		AuthentificateMngr(managerPwd);
		
		Competition comp = null;
		
		try {
			comp = CompetitionManager.findByCompName(competition);

			if (comp==null){
				throw new ExistingCompetitionException("The competition does not exist");
			}
			
			if (comp.getClosingDate().before(MyCalendar.getDate())){
				throw new CompetitionException("The competition is closed");
			}
			
			ArrayList<Bet> bets = (ArrayList<Bet>) BetsManager.findAllByCompetition(competition);
			
			for (Bet bet : bets){
				bet.cancelBet();
				SubscribersManager.updateCoins(bet.getGambler());
			}

			BetsManager.deleleAllBetsOnCompetition(comp);
			
			CompetitionManager.delete(comp);
			
		} catch (SQLException | BadParametersException | SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		}	
		
		
	}

	/**
	 * delete a competition.
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if the closing date is in the futur (competition
	 *             opened).
	 */
	public void deleteCompetition(String competition, String managerPwd)
			throws AuthentificationException, ExistingCompetitionException,
			CompetitionException {
		
		AuthentificateMngr(managerPwd);
		
		Competition comp = null;
		
		try {
			comp = CompetitionManager.findByCompName(competition);
			
			
			if (comp==null){
				throw new ExistingCompetitionException("The competition does not exist");
			}
			
			if (comp.getClosingDate().after(MyCalendar.getDate())){
				throw new CompetitionException("The competition is not finished yet");
			}
		
			BetsManager.deleleAllBetsOnCompetition(comp);

			CompetitionManager.delete(comp);
			
		} catch (SQLException | BadParametersException | SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * add a competitor to a competition.
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param competitor
	 *            infos about the competitor.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if the closing date of the competition is in the past
	 *             (competition closed).
	 * @throws ExistingCompetitorException
	 *             raised if the competitor is already registered for the
	 *             competition.
	 * @throws BadParametersException
	 *             raised if the (firstname, lastname, borndate or name if team
	 *             competitors) of the competitor is invalid.
	 */
	public void addCompetitor(String competition, Competitor competitor,
			String managerPwd) throws AuthentificationException,
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException {
		
		Competition comp = null;
		Competitor comptr = null;
		
		AuthentificateMngr(managerPwd);
		
		if (!competitor.hasValidName()){
			throw new BadParametersException("Bad parameters about the competitor");
		}
		
		try {
			comp = CompetitionManager.findByCompName(competition);
		
			if (comp==null){
				throw new ExistingCompetitionException("The competition does not exist");
			}
				
			//Checks if the competitor already exists
			comptr = AthleteManager.findByAthleteId(competitor.getId());
		
			if (comptr == null){
				comptr = TeamManager.findByTeamId(competitor.getId());	
			}
			
			if (comptr.isInCompetition(comp)){
				throw new ExistingCompetitorException("The competitor is already registered for this competition");
			}
		
			CompetitionManager.addParticipation(comp, competitor);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * create a competitor (person) instance. If the competitor is already
	 * registered, the existing instance is returned. The instance is not
	 * persisted.
	 * 
	 * @param lastName
	 *            the last name of the competitor.
	 * @param firstName
	 *            the first name of the competitor.
	 * @param borndate
	 *            the borndate of the competitor.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 * @throws BadParametersException
	 *             raised if last name, first name or borndate are invalid.
	 * 
	 * @return Competitor instance.
	 */
	public Competitor createCompetitor(String lastName, String firstName,
		String borndate, String managerPwd) throws AuthentificationException, BadParametersException {
		
		AuthentificateMngr(managerPwd);
		
		Competitor comptr;	

		try{
			comptr = AthleteManager.findByAthleteByNameBirthdate(lastName, firstName, borndate);
			
			if (comptr != null){
				return comptr;
			} else {
				comptr = new Athlete(lastName, firstName, DateConvertor.String2MyCalendar(borndate));
				AthleteManager.persist((Athlete) comptr);
				return comptr;
			}

		} catch (SQLException | CompetitionException | ExistingCompetitorException e){
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * create competitor (team) instance. If the competitor is already
	 * registered, the existing instance is returned. The instance is not
	 * persisted.
	 * 
	 * @param name
	 *            the name of the team.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 * @throws BadParametersException
	 *             raised if name is invalid.
	 * 
	 * @return Competitor instance.
	 */
	public Competitor createCompetitor(String name, String managerPwd)
			throws AuthentificationException,
			BadParametersException {
		
		AuthentificateMngr(managerPwd);

		Competitor comptr;

		try{

			comptr = TeamManager.findByName(name);

			if (comptr != null){
				return comptr;
			} else {
				comptr = new Team(name, new ArrayList<Athlete>());
				TeamManager.persist((Team) comptr);
				return comptr;
			}		

		} catch (SQLException | CompetitionException | ExistingCompetitorException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * add an athlete to a team
	 * 
	 * @param team
	 *            the name of the team.
	 * @param competitor
	 * 			  infos about the athlete to add
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 * @throws BadParametersException
	 *             raised if name is invalid.
	 * 
	 *@throws ExistingCompetitorException 
	 *			   raised if the team does not exists
	 */
	public void addAhtleteToTeam(String team, Competitor competitor,
			String managerPwd) throws AuthentificationException,
			ExistingCompetitorException, BadParametersException {
		
		Team comptr = null;
		
		AuthentificateMngr(managerPwd);
		
		if (!competitor.hasValidName()){
			throw new BadParametersException("Bad parameters about the competitor");
		}
		
		try {
			comptr = TeamManager.findByName(team);
		
			if (comptr==null){
				throw new ExistingCompetitorException("The team does not exist");
			}
			
			if (comptr.getAthletes().contains((Athlete) competitor)){
				throw new ExistingCompetitorException("The athlete is already in the team");
			}
			
			TeamManager.addTeamMember(comptr.getId(), ((Athlete) competitor).getId());
			
		} catch (SQLException | CompetitionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * delete a competitor for a competition.
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param competitor
	 *            infos about the competitor.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if the closing date of the competition is in the past
	 *             (competition closed) ; the number of remaining competitors is
	 *             2 before deleting.
	 * @throws ExistingCompetitorException
	 *             raised if the competitor is not registered for the
	 *             competition.
	 */
	public void deleteCompetitor(String competition, Competitor competitor,
			String managerPwd) throws AuthentificationException,
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		
		AuthentificateMngr(managerPwd);

		try {

			Competition comp = CompetitionManager.findByCompName(competition);

			if (comp == null){
				throw new ExistingCompetitionException("The competition does not exist");
			}
			
			if (competitor.isInCompetition(comp)){
				throw new ExistingCompetitorException("The competitor is not registered for this competition");
			}

			if (comp.getCompetitors().size() == 2){
				throw new CompetitionException("There are only two competitors for this competition");
			}

			if (competitor instanceof Athlete){
				AthleteManager.deleteAthleteForCompetition((Athlete) competitor, competition);
				
			}else{
				TeamManager.deleteTeamForCompetition((Team) competitor, competition);
			}

		} catch (SQLException | BadParametersException e){

			e.printStackTrace();

		}

	}

	/**
	 * credit number of tokens of a subscriber.
	 * 
	 * @param username
	 *            subscriber's username.
	 * @param numberTokens
	 *            number of tokens to credit.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingSubscriberException
	 *             raised if the subscriber (username) is not registered.
	 * @throws BadParametersException
	 *             raised if number of tokens is less than (or equals to) 0.
	 */
	public void creditSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthentificationException, ExistingSubscriberException,
			BadParametersException {
		
		if (numberTokens<=0L)
			throw new BadParametersException("The number of tokens is less than or equals to 0!");
		if (username==null)
			throw new BadParametersException("The username is null!");
		
		AuthentificateMngr(managerPwd);
		
		Subscriber subs = null;
		
		try {
			
			subs = SubscribersManager.findByUsername(username);
					
			if (subs == null){
				throw new ExistingSubscriberException("The username doesn't exist!");
			}
			subs.addRemoveCoins(numberTokens);

			SubscribersManager.updateCoins(subs);		
			
		} catch (SQLException | CompetitionException | SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * debit a subscriber account with a number of tokens.
	 * 
	 * @param username
	 *            subscriber's username.
	 * @param numberTokens
	 *            number of tokens to debit.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingSubscriberException
	 *             raised if the subscriber (username) is not registered.
	 * @throws SubscriberException
	 *             raised if number of tokens not enough.
	 * @throws BadParametersException
	 *             raised if number of tokens is less than (or equals to) 0.
	 * 
	 */
	public void debitSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthentificationException, ExistingSubscriberException,
			SubscriberException, BadParametersException {
		
		
		if (numberTokens<=0L)
			throw new BadParametersException("The number of tokens is less than or equals to 0!");
		
		if (username==null)
			throw new BadParametersException("The username is null!");
		
		AuthentificateMngr(managerPwd);
		
		Subscriber subs = null;
		
		try {
			
			subs = SubscribersManager.findByUsername(username);
			
			if (subs == null){
				throw new ExistingSubscriberException("The username doesn't exist!");
			}
		
			subs.addRemoveCoins(-numberTokens);

			SubscribersManager.updateCoins(subs);	
			
		} catch (SQLException | CompetitionException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
	}

	/**
	 * settle bets on winner. <br>
	 * Each subscriber betting on this competition with winner a_winner is
	 * credited with a number of tokens equals to: <br>
	 * (number of tokens betted * total tokens betted for the competition) /
	 * total number of tokens betted for the winner <br>
	 * If no subscriber bets on the right competitor (the real winner), the
	 * tokens betted are credited to subscribers betting on the competition
	 * according to the number of tokens they betted. The competition is then
	 * deleted if no more bets exist for the competition.<br>
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param winner
	 *            competitor winner.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if there is no competitor a_winner for the
	 *             competition; competition still opened.
	 */
	 public void settleWinner(String competition, Competitor winner, String managerPwd)
			throws AuthentificationException, ExistingCompetitionException,
			CompetitionException {
		 
		 AuthentificateMngr(managerPwd);
		 
		 List<Bet> bets = new ArrayList<Bet>();
		 
		 try {
			 bets = BetsManager.findAllByCompetition(competition);
		 	
		 
			 Competition comp = CompetitionManager.findByCompName(competition);
		 
			 if (comp==null){
				 throw new ExistingCompetitionException("The competition does not exist");
			 }
			 
			 if (!(comp.isClosed()))
					throw new CompetitionException("The competition is still open!");
				
			 if (!(comp.getCompetitors().contains(winner)))
					throw new CompetitionException("The competitor isn't in the competition!");
			
			 long totalWinningTokens = 0;
			 long totalTokens = 0;
			 List<Bet> winningBets = new ArrayList<Bet>();
			 
			 for (int i=0; i<bets.size(); i++) {
				 Bet bet = bets.get(i);
				 if (bet instanceof WinnerBet && ((WinnerBet) bet).getWinner().equals(winner)){
					totalWinningTokens+=bet.getCoins();
					winningBets.add(bet);
				 }
				 totalTokens+=bet.getCoins();
			 }
			 
			 if (totalWinningTokens==0) {
				 for (int i=0; i<bets.size(); i++) {
					 Bet bet = bets.get(i);
					 creditSubscriber(bet.getGambler().getUsername(), bet.getCoins(), managerPwd);
				 }
				 
			 } else {
				 for (int i=0; i<winningBets.size(); i++) {
					 Bet bet = winningBets.get(i);
					 creditSubscriber(bet.getGambler().getUsername(), (long) (bet.getCoins()*totalTokens)/totalWinningTokens, managerPwd);
				 }
			 } 
			 WinnerBetsManager.deleteAllOnCompetition(comp);
			 CompetitionManager.delete(comp);
			 
		 } catch (SQLException | ExistingSubscriberException | BadParametersException | SubscriberException | ExistingCompetitorException e) {
			 e.printStackTrace();
			 
		 }
	}

	/**
	 * settle bets on podium. <br>
	 * Each subscriber betting on this competition with the right podium is
	 * credited with a number of tokens equals to: <br>
	 * (number of tokens betted * total tokens betted for the competition) /
	 * total number of tokens betted for the podium <br>
	 * If no subscriber bets on the right podium, the tokens betted are credited
	 * to subscribers betting on the competition according to the number of
	 * tokens they betted. The competition is then deleted if no more bets exist
	 * for the competition.<br>
	 * 
	 * @param competition
	 *            the name of the competition.
	 * @param winner
	 *            the winner.
	 * @param second
	 *            the second.
	 * @param third
	 *            the third.
	 * @param managerPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if the the manager's password is incorrect.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if two competitors in the podium are the same; no
	 *             competitor (firstname, lastname, borndate or name for teams)
	 *             a_winner, a_second or a_third for the competition;
	 *             competition still opened
	 */

	public void settlePodium(String competition, Competitor winner, Competitor second,
			Competitor third, String managerPwd)
			throws AuthentificationException, ExistingCompetitionException,
			CompetitionException {
		
		AuthentificateMngr(managerPwd);
		 
		 List<Bet> bets = new ArrayList<Bet>();
		 
		 try {
			 bets = BetsManager.findAllByCompetition(competition);
		 
			 Competition comp;
			
			 comp = CompetitionManager.findByCompName(competition);
			
			 if (comp==null){
				 throw new ExistingCompetitionException("The competition does not exist");
			 }
			 
			 if (!(comp.isClosed()))
					throw new CompetitionException("The competition is still open!");
				
			 if (!(comp.getCompetitors().contains(winner) || comp.getCompetitors().contains(second) || comp.getCompetitors().contains(third)))
					throw new CompetitionException("On of the competitors isn't in the competition!");
			
			 long totalWinningTokens = 0;
			 long totalTokens = 0;
			 List<Bet> winningBets = new ArrayList<Bet>();
			 
			 for (int i=0; i<bets.size(); i++) {
				 Bet bet = bets.get(i);
				 if (bet instanceof PodiumBet && ((PodiumBet) bet).getFirst().equals(winner) && ((PodiumBet) bet).getSecond().equals(second) && ((PodiumBet) bet).getThird().equals(third)){
					totalWinningTokens+=bet.getCoins();
					winningBets.add(bet);
				 }
				 totalTokens+=bet.getCoins();
			 }
			 
			 if (totalWinningTokens==0) {
				 for (int i=0; i<bets.size(); i++) {
					 Bet bet = bets.get(i);
					 creditSubscriber(bet.getGambler().getUsername(), bet.getCoins(), managerPwd);
				 }
				 
			 } else {
				 for (int i=0; i<winningBets.size(); i++) {
					 Bet bet = winningBets.get(i);
					 creditSubscriber(bet.getGambler().getUsername(), (long) (bet.getCoins()*totalTokens)/totalWinningTokens, managerPwd);
				 }
			 }
			 PodiumBetsManager.deleteAllOnCompetition(comp);
			 CompetitionManager.delete(comp);
			 
		 } catch (SQLException | ExistingSubscriberException | BadParametersException | SubscriberException | ExistingCompetitorException e){
			 e.printStackTrace();
		 }
	}
	

	/***********************************************************************
	 * SUBSCRIBERS FONCTIONNALITIES
	 ***********************************************************************/

	/**
	 * bet a winner for a competition <br>
	 * The number of tokens of the subscriber is debited.
	 * 
	 * @param numberTokens
	 *            number of tokens to bet.
	 * @param competition
	 *            name of the competition.
	 * @param winner
	 *            competitor to bet (winner).
	 * @param username
	 *            subscriber's username.
	 * @param pwdSubs
	 *            subscriber's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if (username, password) does not exist.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if there is no competitor a_winner for the
	 *             competition; competition is closed (closing date is in the
	 *             past); the player is a competitor of the competition.
	 * @throws SubscriberException
	 *             raised if subscriber has not enough tokens.
	 * @throws BadParametersException
	 *             raised if number of tokens less than 0.
	 * 
	 */
	public void betOnWinner(long numberTokens, String competition, Competitor winner,
			String username, String pwdSubs) throws AuthentificationException,
			CompetitionException, ExistingCompetitionException,
			SubscriberException, BadParametersException {
		
		Subscriber subs = null;
		
		try {
			subs = SubscribersManager.findByUsername(username);
	
			if (subs == null){
				throw new AuthentificationException("The username doesn't exist!");
			}
			
			subs.authentificateSubscriber(pwdSubs);
			
			Competition comp = null;
		
			comp = CompetitionManager.findByCompName(competition);

			if (comp == null){
				throw new ExistingCompetitionException("The competition doesn't exist!");
			}
			
			if (comp.isClosed())
				throw new CompetitionException("The competition is closed!");
			
			if (!(comp.getCompetitors().contains(winner)))
					throw new CompetitionException("The competitor isn't in the competition!");
			
			//Check if the subscriber is a competitor of the competition

			for (Competitor comptr : comp.getCompetitors()){
				if (comptr instanceof Team){
					for (Athlete athlete : ((Team) comptr).getAthletes()){
						
						if (athlete.getFamilyName().equals(subs.getFamilyName()) &&
								athlete.getFirstName().equals(subs.getFirstName()) &&
								athlete.getBirthDate().equals(subs.getBirthDate())){

							throw new CompetitionException("the subscribers is a competitor of the competition");
						}
					}
				} else {
					if (((Athlete) comptr).getFamilyName().equals(subs.getFamilyName()) &&
							((Athlete) comptr).getFirstName().equals(subs.getFirstName()) &&
							((Athlete) comptr).getBirthDate().equals(subs.getBirthDate())){
						throw new CompetitionException("the subscribers is a competitor of the competition");
					}
				}
			}

			ArrayList<Competitor> winners= new ArrayList<>();
			winners.add(winner);
			
			WinnerBet bet = (WinnerBet) subs.bet(MyCalendar.getDate(), comp, winners, pwdSubs, numberTokens);
			WinnerBetsManager.persist(bet);
			
			SubscribersManager.updateCoins(subs);
			
		} catch (SQLException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
	}

	/**
	 * bet on podium <br>
	 * The number of tokens of the subscriber is debited.
	 * 
	 * @param username
	 *            subscriber's username.
	 * @param pwdSubs
	 *            subscriber's password.
	 * @param numberTokens
	 *            number of tokens to bet.
	 * @param competition
	 *            the name of the competition.
	 * @param winner
	 *            winner to bet.
	 * @param second
	 *            second place.
	 * @param third
	 *            third place.
	 * 
	 * @throws AuthentificationException
	 *             raised if (username, password) does not exist.
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if there is no competitor with name a_winner, a_second
	 *             or a_third for the competition; competition is closed
	 *             (closing date is in the past); the player is a competitor of
	 *             the competition.
	 * @throws SubscriberException
	 *             raised if subscriber has not enough tokens.
	 * @throws BadParametersException
	 *             raised if number of tokens less than 0.
	 */
	public void betOnPodium(long numberTokens, String competition, Competitor winner,
			Competitor second, Competitor third, String username, String pwdSubs)
			throws AuthentificationException, CompetitionException,
			ExistingCompetitionException, SubscriberException,
			BadParametersException {
		
		Subscriber subs = null;
		
		try {
			subs = SubscribersManager.findByUsername(username);
		
			if (subs == null){
				throw new AuthentificationException("The username doesn't exist!");
			}
			
			subs.authentificateSubscriber(pwdSubs);
			
			Competition comp = null;

			comp = CompetitionManager.findByCompName(competition);
				
			if (comp == null){
				throw new ExistingCompetitionException("The competition doesn't exist!");
			}
			
			if (comp.isClosed())
				throw new CompetitionException("The competition is closed!");
			
			if (!(comp.getCompetitors().contains(winner)))
					throw new CompetitionException("The competitor 1 isn't in the competition!");
			if (!(comp.getCompetitors().contains(second)))
				throw new CompetitionException("The competitor 2 isn't in the competition!");
			if (!(comp.getCompetitors().contains(third)))
				throw new CompetitionException("The competitor 3 isn't in the competition!");
			
			//Check if the subscriber is a competitor of the competition

			for (Competitor comptr : comp.getCompetitors()){
				if (comptr instanceof Team){
					for (Athlete athlete : ((Team) comptr).getAthletes()){
						if (athlete.getFamilyName().equals(subs.getFamilyName()) &&
								athlete.getFirstName().equals(subs.getFirstName()) &&
								athlete.getBirthDate().equals(subs.getBirthDate())){
							throw new CompetitionException("the subscribers is a competitor of the competition");
						}
					}
				} else {
					if (((Athlete) comptr).getFamilyName().equals(subs.getFamilyName()) &&
							((Athlete) comptr).getFirstName().equals(subs.getFirstName()) &&
							((Athlete) comptr).getBirthDate().equals(subs.getBirthDate())){
						throw new CompetitionException("the subscribers is a competitor of the competition");
					}
				}
			}
			
			ArrayList<Competitor> winners= new ArrayList<>();
			winners.add(winner);
			winners.add(second);
			winners.add(third);

			PodiumBet bet = (PodiumBet) subs.bet(MyCalendar.getDate(), comp, winners, pwdSubs, numberTokens);
			PodiumBetsManager.persist(bet);
			
			SubscribersManager.updateCoins(subs);
			
		} catch (SQLException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
	}

	/**
	 * change subscriber's password.
	 * 
	 * @param username
	 *            username of the subscriber.
	 * @param newPwd
	 *            the new subscriber password.
	 * @param currentPwd
	 *            the manager's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if (username, password) does not exist.
	 * 
	 * @throws BadParametersException
	 *             raised if the new password is invalid.
	 */
	public void changeSubsPwd(String username, String newPwd, String currentPwd)
			throws AuthentificationException, BadParametersException {
		Subscriber subs = null;
		
		try {
			subs = SubscribersManager.findByUsername(username);
			
		} catch (SQLException | CompetitionException | SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		} 
		
		if (subs == null){
			throw new AuthentificationException("The username doesn't exist!");
		}
		
		subs.changeSubscriberPassword(newPwd, currentPwd);
	}

	/**
	 * consult informations about a subscriber
	 * 
	 * @param username
	 *            subscriber's username.
	 * @param pwdSubs
	 *            subscriber's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if (username, password) does not exist.
	 * 
	 * @return list of String with:
	 *         <ul>
	 *         <li>subscriber's lastname</li>
	 *         <li>subscriber's firstname</li>
	 *         <li>subscriber's borndate</li>
	 *         <li>subscriber's username</li>
	 *         <li>number of tokens</li>
	 *         <li>tokens betted</li>
	 *         <li>list of current bets</li>
	 *         </ul>
	 * <br>
	 *         All the current bets of the subscriber.
	 */
	public ArrayList<String> infosSubscriber(String username, String pwdSubs)
			throws AuthentificationException {
		Subscriber subs = null;
		ArrayList<String> infos = new ArrayList<String>();
		
	
		try {		
			subs = SubscribersManager.findByUsername(username);
	
			
			if (subs == null){
				throw new AuthentificationException("The username doesn't exist!");
			}
			
	
			subs.authentificateSubscriber(pwdSubs);
	
			
			
			infos.add(subs.getFamilyName());
			infos.add(subs.getFirstName());
			infos.add(subs.getBirthDate().toString());
			infos.add(username);
		
			infos.add(Long.toString(BetsManager.getTotalCoinsBet(subs)));
	
			
			List<Bet> listBets;
	
				listBets = BetsManager.findNotSoldBetsBySubscriber(subs);
	
			
			for (int i=0; i<listBets.size();i++) {
				Bet bet = listBets.get(i);
				infos.add(bet.toString());
			}
		
		}catch (SQLException | BadParametersException | CompetitionException
				| SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
			
		return infos;
	}

	/**
	 * delete all bets made by a subscriber on a competition.<br>
	 * subscriber's account is credited with a number of tokens corresponding to
	 * the bets made by the subscriber for the competition.
	 * 
	 * @param competition
	 *            competition's name.
	 * @param username
	 *            subscriber's username.
	 * @param pwdSubs
	 *            subscriber's password.
	 * 
	 * @throws AuthentificationException
	 *             raised if (username, password) does not exist.
	 * @throws CompetitionException
	 *             raised if closed competition (closing date is in the past).
	 * @throws ExistingCompetitionException
	 *             raised if there is no competition a_competition.
	 */
	public void deleteBetsCompetition(String competition, String username,
			String pwdSubs) throws AuthentificationException,
			CompetitionException, ExistingCompetitionException {
		Subscriber subs = null;
		
		try {
			subs = SubscribersManager.findByUsername(username);

			if (subs == null){
				throw new AuthentificationException("The username doesn't exist!");
			}
			
			subs.authentificateSubscriber(pwdSubs);
			
			List<Bet> betList = BetsManager.findAllBySubscriber(subs);
			
			Competition comp = null;

			comp = CompetitionManager.findByCompName(competition);
			
			if (comp == null){
				throw new ExistingCompetitionException("The competition doesn't exist!");
			}
			
			if (comp.isClosed())
				throw new CompetitionException("The competition is closed!");
			
			for (int i=0;i<betList.size();i++) {
				Bet bet = betList.get(i);
				if (bet.getCompetition().equals(comp))
					BetsManager.delete(bet);
			}
			
		} catch (SQLException | BadParametersException | SubscriberException | ExistingCompetitorException e ){
			e.printStackTrace();
		}
	}
		
	/***********************************************************************
	 * VISITORS FONCTIONNALITIES

	 ***********************************************************************/
	/**
	 * list competitions.

	 * 
	 * @return a collection of competitions represent a competition data:
	 *         <ul>
	 *         <li>competition's name</li>
	 *         <li>competition's closing date</li>
	 *         <li>competition's current bets</li>
	 *         <li>competition's competitors</li>
	 *         </ul>
	 */
	public List<List<String>> listCompetitions() {
		
		List<List<String>> collectionCompetition = new ArrayList<List<String>>();
		
		try {
			
			List<Competition> competitions = CompetitionManager.findAll();

			for (int i=0; i<competitions.size();i++){
				Competition comp = competitions.get(i);
				
				List<String> infoCompetition = new ArrayList<String>();
				
				infoCompetition.add(comp.getName());
				infoCompetition.add(comp.getClosingDate().toString());
								
				List<Bet> betsList = BetsManager.findAllByCompetition(comp.getName());
				for (Bet bet : betsList) {
					infoCompetition.add(bet.toString());
				}
								
				ArrayList<Competitor> competitors = comp.getCompetitors();
				for (Competitor comptr : competitors) {
					infoCompetition.add(comptr.toString());
				}
				
				collectionCompetition.add(infoCompetition);
			}
			
		} catch (SQLException | BadParametersException | CompetitionException | SubscriberException | ExistingCompetitorException e){
			e.printStackTrace();
		}
		
		return collectionCompetition;
	}

	/**
	 * list competitors.
	 * 
	 * @param competition
	 *            competition's name.
	 * 
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if competition closed.
	 * @return a collection of competitors for a competition. For each person
	 *         competitor
	 *         <ul>
	 *         <li>competitor's firstname</li>
	 *         <li>competitor's lastname</li>
	 *         <li>competitor's borndate</li>
	 *         </ul>
	 *         For each team competitor <li>competitor's name</li> </ul>
	 */
	public List<List<String>> listCompetitors(String competition)
			throws ExistingCompetitionException, CompetitionException {
		
		List<Competitor> competitors;
		List<List<String>> collectionCompetitor = new ArrayList<List<String>>();
		
		try {
			competitors = CompetitionManager.findCompetitors(competition);

			for (int i=0; i<competitors.size();i++){
				Competitor comp = competitors.get(i);
				
				List<String> infoCompetitor = new ArrayList<String>();
				
				if (comp instanceof Athlete) {
					infoCompetitor.add(((Athlete) comp).getFirstName());
					infoCompetitor.add(((Athlete) comp).getFamilyName());
					infoCompetitor.add(((Athlete) comp).getBirthDate().toString());
				} else if (comp instanceof Team) {
					infoCompetitor.add(((Team) comp).getTeamName());
				}
				
				collectionCompetitor.add(infoCompetitor);
			}
		
		} catch (SQLException | BadParametersException | ExistingCompetitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return collectionCompetitor;
	}

	/**
	 * list all competitors.
	 * 
	 * @throws ExistingCompetitionException
	 *             raised if the competition does not exist.
	 * @throws CompetitionException
	 *             raised if competition closed.
	 * @return a collection of competitors for a competition. For each person
	 *         competitor
	 *         <ul>
	 *         <li>competitor's firstname</li>
	 *         <li>competitor's lastname</li>
	 *         <li>competitor's borndate</li>
	 *         </ul>
	 *         For each team competitor <li>competitor's name</li> </ul>
	 */
	public List<List<String>> listAllCompetitors()
			throws ExistingCompetitionException, CompetitionException {
		
		List<Athlete> athleteCompetitors;
		List<Team> teamCompetitors;
		
		List<List<String>> collectionCompetitor = new ArrayList<List<String>>();
		
		try {
			athleteCompetitors = AthleteManager.findAll();
			teamCompetitors = TeamManager.findAll();

			for (Athlete a : athleteCompetitors){
	
				List<String> infoCompetitor = new ArrayList<String>();
				
				infoCompetitor.add(a.getFirstName());
				infoCompetitor.add(a.getFamilyName());
				infoCompetitor.add(a.getBirthDate().toString());
				
				collectionCompetitor.add(infoCompetitor);
				
			} 
			
			for (Team t : teamCompetitors){
				
				List<String> infoCompetitor = new ArrayList<String>();
	
				infoCompetitor.add(t.getTeamName());
				
				collectionCompetitor.add(infoCompetitor);
			}
		
		} catch (SQLException | BadParametersException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
		
		return collectionCompetitor;
	}
	
	/**
	 * consult bets on a competition.
	 * 
	 * @param competition
	 *            competition's name.
	 * 
	 * @throws ExistingCompetitionException
	 *             raised if it does not exist a competition of the name
	 *             a_competition.
	 * 
	 * @return a list of String containing the bets for the competition.
	 */
	public ArrayList<String> consultBetsCompetition(String competition)
			throws ExistingCompetitionException {
		
		List<Bet> listBets = null;
		try {
			listBets = BetsManager.findAllByCompetition(competition);
		} catch (SQLException | BadParametersException | CompetitionException | SubscriberException | ExistingCompetitorException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> listBetsString = new ArrayList<String>();
		
		for (int i=0;i<listBets.size();i++) {
			listBetsString.add(listBets.get(i).toString());
		}
		
		return listBetsString;
	}
	
	/**
	 * consult results of a closed competition.
	 * 
	 * @param competition
	 *            competition's name.
	 * 
	 * @throws ExistingCompetitionException
	 *             raised if it does not exist a competition of the name
	 *             a_competition.
	 *             
	 * @return the list of competitors that won the competition.
	 */
	public ArrayList<Competitor> consultResultsCompetition(String competition)
			throws ExistingCompetitionException {
		
		Competition comp = null;
		ArrayList<Competitor> winners = new ArrayList<Competitor>();
		
		try {
			comp = CompetitionManager.findByCompName(competition);
			
			if (comp == null){
				throw new ExistingCompetitionException("The competition doesn't exist!");
			}
			
			if (!comp.isClosed())
				throw new CompetitionException("The competition is still open!");
			
			ArrayList<Competitor> competitors = CompetitionManager.findCompetitors(competition);
			
			winners.add(null);
			winners.add(null);
			winners.add(null);
			
			for (int i=0; i<competitors.size();i++){
				Competitor competitor = competitors.get(i);
				int rank=0;
				try {
					rank=comp.getRankCompetitor(competitor);
				} catch (IllegalArgumentException | ExistingCompetitorException e) {}
				if (rank>0 && rank<4){
					winners.add(rank, competitor);
					if (winners.get(rank+1)==null)
						winners.remove(rank+1);
				}
			}
			
		} catch(SQLException | BadParametersException | CompetitionException | ExistingCompetitorException e){
			e.printStackTrace();
		}
		
		return winners;
	}
	
}

