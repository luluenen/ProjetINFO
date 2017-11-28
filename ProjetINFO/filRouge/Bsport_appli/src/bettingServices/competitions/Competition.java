package fr.uv1.bettingServices.competitions;


import java.util.ArrayList;

import fr.uv1.bettingServices.exceptions.AuthentificationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.utils.MyCalendar;
/**
 * 
 * @author GUO Jiao + ZHAO Lu <br>
 * <br>
 *         This class declares all methods for competition. <br>
 * 
 */
public class Competition {
	
	//Minimal size for a subscriber's name.
	private static final int MIN_NAME =4;
	//Constraints for name
	private static final String REGEX_NAME = new String("[a-zA-Z0-9\\-\\.\\_]*");
    private String name;
    MyCalendar closingDate;
	private ArrayList<Competitor> competitors;
	// list of competitors ordered by their rank 
	private ArrayList<Competitor> rankingList;
	// state of isSold of the competition, 0 (is not sold) 1 (is sold)
	private int isSold = 0;
	//resultType of competition, resultType=1 (Draw) and 2 (notDraw) 
	private int resultType;

	/**
	 * constructor for Competition.
	 * 
	 * @param name
	 *            the name of the competition.
	 * @param closingDate
	 *            the time when competition starts, we can no longer bet for this competition.
	 * @param competitors
	 *            list of competitors in the competition. size of list > =2.
	 * @throws BadParametersException
	 * 			  raised id the parameters are illegal
	 * @throws CompetitionException
	 */
	public Competition(String a_name, MyCalendar closingDate, ArrayList<Competitor> competitors )
			throws BadParametersException, CompetitionException {
		this.setName(a_name);
		this.setCompetitors(competitors);
		this.setClosingDate(closingDate);
		this.rankingList = new ArrayList<Competitor>() ;
		this.resultType = 0;
		this.isSold = 0;
	}
	
	public Competition(String a_name, MyCalendar closingDate, ArrayList<Competitor> competitors , int isSold )
			throws BadParametersException, CompetitionException {
		this.setName(a_name);
		this.setCompetitors(competitors);
		this.setClosingDate(closingDate);
		this.rankingList = new ArrayList<Competitor>() ;
		this.resultType = 0;
		this.isSold = isSold;
	}

	/**
	 * constructor for Competition from database.
	 * 
	 * @param name
	 *            the name of the competition.
	 * @param closingDate
	 *            the time when competition starts, we can no longer bet for this competition.
	 * @param competitors
	 *            list of competitors in the competition. size of list > =2.
	 * @throws BadParametersException
	 * 			  raised id the parameters are illegal
	 * @throws CompetitionException
	 */
	public Competition(String a_name, MyCalendar closingDate)
			throws BadParametersException, CompetitionException {
		this.setName(a_name);
		this.closingDate = closingDate;
		this.rankingList = new ArrayList<Competitor>() ;
		this.resultType = 0;
		this.isSold = 0;
	}
	
	
	/**
	 * set value of name
	 * 
	 * @param competitionName
	 * @throws BadParametersException
	 */
	private void setName(String competitionName) throws BadParametersException {
		if (competitionName == null)
			throw new BadParametersException("name of competition is empty");
		checkStringName(competitionName);
		this.name = competitionName;
	}
	
	
	/**
	 * set value of closingDate.
	 * 
	 * @param closingDate
	 * @throws BadParametersException
	 * @throws CompetitionException
	 */
	private void setClosingDate(MyCalendar closingDate) throws BadParametersException, CompetitionException {
		if (closingDate == null)
			throw new BadParametersException("date of competition is empty");
		else if (closingDate.before(MyCalendar.getDate())) {
	        throw new CompetitionException("date of competition is not valid");
	    }
		this.closingDate = closingDate;
	}
	
	
	/**
	 * set value of competitors.
	 * set save this competition for every competitor in its list of competitions
	 * 
	 * @param competitors
	 * @throws BadParametersException
	 * @throws CompetitionException 
	 */
	public void setCompetitors(ArrayList<Competitor> competitors) throws BadParametersException, CompetitionException {
		if (competitors == null)
			throw new BadParametersException("list of competitors is empty");
        if (competitors.size() < 2) {
            throw new CompetitionException("size of list of competitors should be longer than 1");
        }		
		this.competitors =competitors;
		for (Competitor competitor:competitors){
			competitor.addCompetition(this);
		}
	}
	
	
	 /**
	 * set the type of this competition: 1 (Draw) and 2 (notDraw)
	 * when the competition close
	 * @param resultType
	 *             the resultType.
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 */
	public void setResultType(int resultType ){

		if ((resultType ==1)||(resultType ==2))
			this.resultType =resultType;
		else
			throw new IllegalArgumentException("the value of parametre resultType is 1 (Draw) or 2 (notDraw) ");
	}
	
	
	 /**
	 * set the rankingList for this competition, the index of competitor is his rank in the competition.  
	 * 
	 * @param rankingList
	 *             the list of ranking,have to include at least 3 the competitors:the first,the second,the third
	 *             
	 * @throws IllegalArgumentException
	 *             raised if the competitor doesn't in the list of competitors.
	 */
	public void setRankingList(ArrayList<Competitor> rankingList){		
		for( Competitor competitor:rankingList){
			if (!competitors.contains(competitor))
				throw new IllegalArgumentException(competitor.toString()+"doesn't exist in the list of competitors");
		}
		
		if (rankingList.size()< 3)
			throw new IllegalArgumentException("you have to mention at least 3 the competitors:the first,the second,the third");
		this.rankingList = rankingList;	
	}
	
	
	/**
	 * set the attribute isSold=1 if the competition is sold. 
	 * 
	 */
	public void setIsSold(){
		isSold = 1;
	}
	
	
	
	/**
	 * verify if the name of competition is valid
	 * which we use in the function setName()
	 * 
	 * @param a_Name
	 * @throws BadParametersException
	 */
	private static void checkStringName(String a_Name)
			throws BadParametersException {
		if (a_Name == null)
			throw new BadParametersException("name not instantiated");

		if (a_Name.length() < MIN_NAME)
			throw new BadParametersException("name length more than "
					+ MIN_NAME + "characters");
		// Just letters and digits are allowed
		if (!a_Name.matches(REGEX_NAME))
			throw new BadParametersException("the name " + a_Name
					+ " does not verify constraints ");
	}
	
	
	
	/**
	 * verify if the name exists in database
	 * 
	 * @param a_Name
	 * @throws BadParametersException
	 * @throws CompetitionException 
	 * @throws SQLException 
	 */
	/**
	private static boolean checkIsExistedeName(String a_Name)
			throws BadParametersException, SQLException, CompetitionException {
		if (CompetitionManager.findByCompName(a_Name) != null)
			return true;
		else return false;				
	}
	 */
	

	/**
	 * add competitor in the list of competitors
	 * 
	 * @param competitor
	 *            the competitor which we want to add in the list of competitors .
	 * @throws ExistingCompetitorException 
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 */
	public void addCompetitor(Competitor competitor) throws ExistingCompetitorException{

		if (competitors.contains(competitor))
			throw new ExistingCompetitorException("competitor is already in the competition");
		competitors.add(competitor);
		competitor.addCompetition(this);
	}
	
	
	
	/**
	 * delete competitor in the list of competitors
	 * at the same time, delete this competition for the competitor in his list of competitions
	 * 
	 * @param competitor
	 *            the competitor which we want to delete in the list of competitors .
	 *            
	 * @throws ExistingCompetitorException 
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 */
	public void deleteCompetitor(Competitor competitor) throws ExistingCompetitorException{

		if (! competitors.contains(competitor))
			throw new ExistingCompetitorException("competition doesn't exist");

		competitors.remove(competitor);
		competitor.deleteCompetition(this);
	}
	
	
	 /**
	 * verify if the competition is closed for betting.
	 * 
	 */
	public boolean isClosed(){
		if (closingDate.after(MyCalendar.getDate())) 
			return false;
		else return true;
	}
	
	 /**
	 * two Competition are equal if they have the same name.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Competition) {
			return this.name == ((Competition) obj).getName();
		}
		return false;
	}
	
	
	 /**
	 * get the rank of certain competitor.
	 * 
	 * @param c
	 *            the competitor who we demand the rank .
	 * @return 
	 * 		   a integer is representative of rank 
	 * 			for a draw game,the two winners get the same rank;
	 * 			for a game who has normal result,the three winners get 1,2,3 respectively
	 * @throws ExistingCompetitorException 
	 * 
	 * @throws AuthentificationException
	 *             raised if the manager's password is incorrect.
	 */
	public int getRankCompetitor(Competitor c) throws ExistingCompetitorException{	
		if (competitors.contains(c)){
		    if (resultType == 1){
		    	if ((rankingList.indexOf(c)==0)||(rankingList.indexOf(c)==1))
		    		return 1;
		    	else
		    		return -1;
		    }    	
		    else if (resultType == 2){
		    	if ((rankingList.indexOf(c)==0)||(rankingList.indexOf(c)==1)||(rankingList.indexOf(c)==2))
		    		return rankingList.indexOf(c)+1;
		    	else
		    		return -1;
		    }   
		    else 
		    	throw new IllegalArgumentException("we don't have the result of this competition yet");
		}
		else 
			throw new IllegalArgumentException("we don't have this competitor in the competition");
	}

	 /**
	 * get the name of the competition
	 * 
	 * @return 
	 * 		String who is the name of this competition            
	 */
	
	public String getName(){
		return name;
	}
	
	
	 /**
	 * get the situation if the competition is sold or not
	 * 
	 * @return 
	 * 		0 "is not sold", 1 "is sold"            
	 */
	
	public int getIsSold(){
		return isSold;
	}
	
	 /**
	 * get the closingDate.  
	 *             
	 * @return 
	 * 		Type date of the closing date of this competition
	 */
	public MyCalendar getClosingDate(){
		return closingDate;
	}
	
	
	 /**
	 * get the list of competitors.  
	 *             
	 * @return 
	 * 		ArrayList<Competitor> which is the list of the competitors of this competition
	 */
	public ArrayList<Competitor> getCompetitors(){

		return competitors;
	}
	
	
	 /**
	 * get the rankingList.  
	 *             
	 * @return ArrayList<Competitor> 
	 * 		ArrayList<Competitor> which is the list of the ranking ordered by the rank.
	 */
	public ArrayList<Competitor> getRankingList(){
		return rankingList;
	}
	
	
	 /**
	 * get the resultType.  
	 *             
	 * @return 
	 * 		resultType=1 (Draw) and 2 (notDraw) 
	 */
	public int getResultType(){
			return resultType;
	}
	
	public String toString(){
		
		return "Competition：" + this.name ;  
			 
	}
	

}

