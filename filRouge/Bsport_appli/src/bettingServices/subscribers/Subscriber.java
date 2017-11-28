package fr.uv1.bettingServices.subscribers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import fr.uv1.bettingServices.bets.Bet;
import fr.uv1.bettingServices.bets.DrawBet;
import fr.uv1.bettingServices.bets.PodiumBet;
import fr.uv1.bettingServices.bets.WinnerBet;
import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.exceptions.AuthentificationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.utils.MyCalendar;


/**
 * 
 * @author sjerbi + tmarcuzz <br>
 * <br>
 *         This class declares all methods for a subscriber. <br>
 * 
 */

public class Subscriber {
	private String familyName;
	private String firstName;
	private String username;
	private MyCalendar birthDate;
	private String password;
	private ArrayList<Bet> bets = new ArrayList<>();
	private long coins;
	
	
	private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int PWD_LENGTH = 8;
	
	private static final String REGEX_NAME = "[a-zA-Z][a-zA-Z\\-\\ ]+";
	private static final String REGEX_USERNAME = "[a-zA-Z0-9]{4,}";
	
	
	/**
     * Constructor used for creation from a manager
     */
	public Subscriber(String familyName, String firstName, String username, MyCalendar birthDate) 
			throws BadParametersException, SubscriberException{
				
		this.familyName = familyName;
		this.firstName = firstName;
		this.username = username;
		this.birthDate = birthDate;
		
		hasValidIdentifiers();
		checkDate(birthDate);
		
		this.coins = 0;
		String pwd = this.generatePassword();
		this.password = pwd;
		
	}
	
	/**
     * Constructor used for creation from a database
     */
	public Subscriber(String familyName, String firstName, String username, MyCalendar birthDate, long coins, String password){
		
		this.familyName = familyName;
		this.firstName = firstName;
		this.username = username;
		this.birthDate = birthDate;
		this.coins = coins;
		this.password = password;
		
	}
	
	/**
     * Setter for the list of bets
     * 
     * @param bets
     * 			an ArrayList of bets
     */
	public void setBets(ArrayList<Bet> bets){
		
		this.bets = bets;
	}
	
	/**
     * Checks the birthDate
     * 
     * @param birthDate
     * @throws BadParametersException
     *             if the date is not instantiated
     * @throws SubscriberException
     *             if the subscriber is minor
     */
	private void checkDate(MyCalendar birthDate) throws SubscriberException, BadParametersException {
		
		if (birthDate == null)
            throw new BadParametersException("The date is not instantiated");

		MyCalendar today = MyCalendar.getDate();
		
		if (new MyCalendar(today.get(Calendar.YEAR)-18,today.get(Calendar.MONTH)+1,today.get(Calendar.DAY_OF_MONTH)).before(birthDate))
			throw new SubscriberException("The person is underage");
					
		}		

	/**
     * Checks the family name
     * 
     * @param name
     * @throws BadParametersException
     *             if name is not instantiated or if it does not respect the format
     */
	private void checkFamilyName(String name) throws BadParametersException{
		if (name==null) 
			throw new BadParametersException("The family name is not instantiated");
		if (!name.matches(REGEX_NAME)) 
			throw new BadParametersException("The family name does not respect the format");
		}
	
	/**
     * Checks the name
     * 
     * @param name
     * @throws BadParametersException
     *             if name is not instantiated or if it does not respect the format
     */
	private void checkFirstName(String name) throws BadParametersException{
		if (name==null) 
			throw new BadParametersException("firstname not instantiated");
		if (!name.matches(REGEX_NAME)) 
			throw new BadParametersException("it does not respect the format");
		}
	
	/**
     * Checks the username
     * 
     * @param username
     * @throws BadParametersException
     *             if username is not instantiated or if it does not respect the format
     */
	private void checkUsername(String username) throws BadParametersException{
		if (username==null) 
			throw new BadParametersException("username is null");
		if (!username.matches(REGEX_USERNAME)) 
			throw new BadParametersException("Username format is invalid");
		}

	/**
     * Generates the password
     * 
     *@return password 
     *			the password of the subscriber
     */
	public String generatePassword(){
		Random r = new Random();
		String password = new String();
		for (int i = 0; i < PWD_LENGTH; i++) {
	        password = password + ALPHABET.charAt(r.nextInt(ALPHABET.length()));
		}
	    return(password);
	}
	
	/**
     * Getter for the password
     * 
     *@return password 
     *			the password of the subscriber
     */
	public String getPassword(){
	    return(password);
	}
	
	/**
     * Getter for the firstName
     * 
     *@return firstName 
     *			the firstName of the subscriber
     */
	public String getFirstName() {
		return firstName;
	}
	
	/**
     * Getter for the familyName
     * 
     *@return familyName 
     *			the familyName of the subscriber
     */
	public String getFamilyName() {
		return familyName;
	}
	
	/**
     * Getter for the username
     * 
     *@return username 
     *			the username of the subscriber
     */
	public String getUsername() {
		return username;
	}
	
	/**
     * Getter for the balance
     * 
     *@return username 
     *			the username of the subscriber
     */
	public long getBalance() {
		return coins;
	}

	/**
     * Getter for the birthDate
     * 
     *@return birthDate 
     *			the birthDate of the subscriber
     */
	public MyCalendar getBirthDate() {
		return birthDate;
	}
	
	/**
     * Authentificates the subscriber
     *
     * @param subscriberPassword
     * 
     * @throws BadParametersException
     *             if subscriberPassword is null
     * @throws SubscriberException
     *             if subscriberPassword is wrong
     */
	public void authentificateSubscriber(String subscriberPassword) throws BadParametersException, AuthentificationException{
		if (subscriberPassword==null)
			throw new BadParametersException("The entered password is null");
		if (!password.equals(subscriberPassword))
			throw new AuthentificationException("The entered password is wrong");
	}
	
	/**
     * Changes the subscriber's password
     *
     * @param newSubscriberPassword
     * 
     * @param oldSubscriberPassword
     * 
     * @throws BadParametersException
     *             if subscriberPassword is null
     * @throws SubscriberException
     *             if subscriberPassword is wrong
     */
	public void changeSubscriberPassword(String newSubscriberPassword, String oldSubscriberPassword) 
			throws BadParametersException, AuthentificationException{
		
		authentificateSubscriber(oldSubscriberPassword);
		
		if (newSubscriberPassword==null)
			throw new BadParametersException("The entered password is null");
		
		if (newSubscriberPassword.length()<PWD_LENGTH)
			throw new BadParametersException("The entered password is too short");

		if (!correctPasswordFormat(newSubscriberPassword))
			throw new BadParametersException("The entered password is invalid");
			
		password = newSubscriberPassword;
	}
	
	/**
     * Check if the new password respects the format
     *
     * @param pwd
     * 
     * @return boolean
     * 			return if the new password respect the format or not
     * 
     */
	public boolean correctPasswordFormat(String pwd){
		for (int i = 0; i < pwd.length(); i++) {
	        if (ALPHABET.indexOf(pwd.charAt(i))==-1)  	
	        	return(false);
		}
		return(true);
	}
	
	/**
     * Changes the subscriber's balance
     *
     * @param numberCoins
     * 
     * @return String
     * 			confirmation message
     * 
     */
	public String addRemoveCoins(long numberCoins) throws SubscriberException{
		if (coins+numberCoins<0)
			throw new SubscriberException("Not enough coins !");				
		coins = coins+numberCoins;		
		return("Balance updated");
	}
	
	/**
     * Checks the identifiers of the subscriber
     * 
     * @throws BadParametersException
     *             if username is not instantiated or if it does not respect the format
     */
	public boolean hasValidIdentifiers() throws BadParametersException{
		
		checkFamilyName(familyName);
		checkFirstName(firstName);
		checkUsername(username);
		
		return true;
	}
	
	/**
     * Create a new Bet
     * 
     * @return the Bet
     * 
     * @throws BadParametersException
     *             if the parameters in the constructor are incorrect
	 * @throws AuthentificationException 
	 * 			   if the password is incorrect
	 * @throws SubscriberException 
	 * 			   if the subscriber does not have enough coins
	 * @throws SQLException	
	 * 			   if there is an error when the bet is made persistent
     */
	public Bet bet(MyCalendar betDate, Competition chosenCompetition, ArrayList<Competitor> prospectiveWinners, String passwordSubs, long coins)
			throws BadParametersException, AuthentificationException, SubscriberException{
		
		authentificateSubscriber(passwordSubs);
		
		Bet newBet = null;
		
		if (prospectiveWinners.size()==2){
			newBet = new DrawBet(chosenCompetition, this, betDate, coins,
					(Competitor)(prospectiveWinners.get(0)), (Competitor)(prospectiveWinners.get(1)));			
			
		} else if (prospectiveWinners.size()==3){
			newBet = new PodiumBet(chosenCompetition, this, betDate, coins,
					(Competitor)(prospectiveWinners.get(0)), (Competitor)(prospectiveWinners.get(1)), (Competitor)(prospectiveWinners.get(2)));
					
		} else if (prospectiveWinners.size()==1){
			newBet = new WinnerBet(chosenCompetition, this, betDate, coins,
					(Competitor)(prospectiveWinners.get(0)));
		}
		
		addRemoveCoins(-coins);
				
		bets.add(newBet);
		
		return newBet;

	}
	
	/**
     * List the bets
     * 
     * @return bets
     * 			an ArrayList of bets
     */
	public ArrayList<Bet> listBets(){
		return  bets;
		
	}
	
		
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false; 
        if (getClass() != o.getClass())
            return false;
        
        return username.equals(((Subscriber)o).username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return familyName + " " + firstName + " " + username;
    }


    
    
}
