package fr.uv1.bettingServices.competitors;
import java.util.ArrayList;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.utils.MyCalendar;


/**
 * @author ZHAO LU and GUO JIAO
 *  <br>
 *  This class represents an athlete for a betting application. <br>
 *
 */

public class Athlete implements Competitor{
	
    //Constraints for familyName and firstName
	private static final String REGEX_NAME = new String("[a-zA-Z][a-zA-Z\\-\\s]*");
	private static final int MIN_NAME = 1;
	
	private String familyName;
	private String firstName;
	int id_athlete = 0;
	private MyCalendar birthDate;
	//list of competition that the competitor participates.
	private ArrayList<Competition> competitions;
	
	
	/**
	 * Athlete constructor.
	 * 
	 * @param familyName
	 *            the familyName of the athlete.
	 * @param firstName
	 *           the firstName of the athlete.
	 * @param birthDate
	 *           type MyCalendar, the birthDate of the athlete.
	 *           
	 * @throws BadParametersException
	 */
	public Athlete(String familyName, String firstName, MyCalendar birthDate)
            throws BadParametersException {
		checkStringName(familyName);
		checkStringName(firstName);
		checkDate(birthDate);
		
		if (birthDate == null)
			throw new BadParametersException("you have to add the birthdate of "
					+ firstName+"  "+ familyName);
        this.familyName = familyName;
		this.firstName = firstName;
		this.birthDate = birthDate;
		this.competitions = new ArrayList<Competition>(); 
    }
	
	


	/**
	 * Athlete constructor for set the id.
	 * 
	 * @param familyName
	 *            the familyName of the athlete.
	 * @param firstName
	 *           the firstName of the athlete.
	 * @param birthDate
	 *           type MyCalendar, the birthDate of the athlete.
	 * @throws BadParametersException
	 */
	
	public Athlete( int id,String familyName, String firstName, MyCalendar birthDate )
            throws BadParametersException 
            {
		checkStringName(familyName);
		checkStringName(firstName);
		checkDate(birthDate);
		
		this.competitions = new ArrayList<Competition>(); 
		this.familyName = familyName;
		this.firstName = firstName;
		this.id_athlete = id;
		this.birthDate = birthDate;
    }
	
	
	/**
	 * verify if the name of athlete is valid
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
		if (!a_Name.matches(REGEX_NAME))
			throw new BadParametersException("the name " + a_Name
					+ " does not verify constraints ");
	}
	
	
	/**
     * Checks the birthDate
     * 
     * @param birthDate
     * @throws BadParametersException
     *             if the date is not instantiated
     */
	private static void checkDate(MyCalendar birthDate) throws BadParametersException {
		
		if (birthDate == null)
            throw new BadParametersException("The date is not instantiated");
		
		}
	

    @Override
    public boolean hasValidName(){
    	if ((this.familyName == null)||(this.firstName == null))
			return false;

		if ((this.familyName.length() < MIN_NAME)||(this.firstName.length() < MIN_NAME))
			return false;
		// Just letters and digits are allowed
		if ((!this.familyName.matches(REGEX_NAME))||(!this.firstName.matches(REGEX_NAME)))
			return false;
		return true;
    }
    
  
    @Override
	public boolean isInCompetition(Competition competition){
		return this.competitions.contains(competition);
	}
	
    
    @Override
	public void addCompetition(Competition newCompetition){
		if ( competitions.contains(newCompetition))
			 throw new IllegalArgumentException("This Competition "+newCompetition.toString()+"already exist for Athlete"+this.familyName+this.firstName+
					 "\n you need not to add him youself \n or the same athlete can not participer two team in the same game");
	
		this.competitions.add(newCompetition);
	}
    
    
    @Override
	public void deleteCompetition(Competition oldCompetition){
		if (! competitions.contains(oldCompetition))
			 throw new IllegalArgumentException("Competition doesn't exist in the list of this Athlete");
		this.competitions.remove(oldCompetition);
	}
    
    
	 /**
	 * get the familyName of the athlete
	 * 
	 * @return 
	 * 		String familyName            
	 */
	public String getFamilyName(){
		return this.familyName;
	}
	
	 /**
	 * get the firstName of the athlete
	 * 
	 * @return 
	 * 		String firstName            
	 */
	public String getFirstName(){
		return this.firstName;
	}
	
	@Override
	public int getId(){
		return this.id_athlete;
	}
	
	
	 /**
	 * get the list of competitions in which the athlete participate
	 * 
	 * @return 
	 * 		ArrayList<Competition>           
	 */
	public ArrayList<Competition> getCompetitons(){
		return this.competitions;
	}
	

	 /**
	 * get the birthDate of the athlete
	 * 
	 * @return 
	 * 		MyCalendar birthDate         
	 */	
	public MyCalendar getBirthDate(){
		return this.birthDate;
	}

	 /**
	 * set id         
	 */
	public void setId(int id){
		this.id_athlete = id;
	}
	
	
	 /**
	 * set birthDate       
	 */
	public void setBirthDate(MyCalendar birthDate){ 
		this.birthDate=birthDate;
	}
	
	
	public String toString(){
	  
		return "Athlete：" + this.familyName + " " + this.firstName +"  "+this.birthDate;  
			 
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Athlete) {
			return this.id_athlete == ((Athlete) obj).getId();
		}	
		return false;
	}
	
	
    @Override
    public void addMember(Competitor member)
            throws ExistingCompetitorException, BadParametersException {
    }

    @Override
    public void deleteMember(Competitor member) throws BadParametersException,
            ExistingCompetitorException {
    }
	
}





