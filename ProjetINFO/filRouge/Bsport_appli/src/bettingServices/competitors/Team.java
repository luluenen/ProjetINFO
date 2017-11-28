package fr.uv1.bettingServices.competitors;
import java.util.ArrayList;

import fr.uv1.bettingServices.competitions.Competition;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.interfaces.Competitor;

	/**
	 * @author ZHAO LU and GUO JIAO
	 * 
	 * <br>
	 *  This class represents a team for a betting application. <br>
	 *
	 */
	public class Team implements Competitor{
		// Minimal size for a team's name.
		private static final int MIN_NAME = 4;
	   //Constraints for teamName
		private static final String REGEX_NAME = new String("[a-zA-Z][a-zA-Z\\-\\s]*");
		
		private String teamName;
		private ArrayList<Athlete> athletes = new ArrayList<Athlete>();
		private ArrayList<Competition> competitions;
		int id_team = 0;

		
		/**
		 * constructor for Team.
		 * 
		 * @param teamName
		 *            the name of the team.
		 * @param athletes
		 *            the list of the athletes of this team.
		 * @throws BadParametersException
		 * @throws CompetitionException
		 * 
		 */
		public Team(String teamName, ArrayList<Athlete> athletes )
				throws BadParametersException, CompetitionException {
			this.setName(teamName);
			this.setAthletes(athletes);
			this.competitions = new ArrayList<Competition>() ; 
		}
		
		
		/**
		 * constructor for Team if we have the id .
		 * 
		 * @param id
		 *            id of the competition.
		 * @param teamName
		 *            the name of the competition.
	     * @param athletes
		 *            the list of the athletes of this team.
		 * @throws CompetitionException 
		 * 
		 * @throws BadParametersException
		 */
		public Team(int id,String teamName, ArrayList<Athlete> athletes)
				throws BadParametersException, CompetitionException {
			this.id_team = id;
			this.setName(teamName);
			this.setAthletes(athletes);
			this.competitions = new ArrayList<Competition>() ; 
		}
		
		
		/**
		 * constructor for Team if we have the id and the teamName.
		 * 
		 * @param id
		 *            id of the competition.
		 * @param teamName
		 *            the name of the competition.
		 *            
		 * @throws CompetitionException 
		 * 
		 * @throws BadParametersException
		 */	
		public Team( int id,String teamName)
				throws BadParametersException, CompetitionException {
			this.id_team = id;
			this.setName(teamName);
			this.competitions = new ArrayList<Competition>() ;
			this.athletes = new ArrayList<Athlete>() ;

		}
		
		
		/**
		 * set value of name if name is valid
		 * 
		 * @param teamName
		 * @throws BadParametersException
		 */
		private void setName(String teamName) throws BadParametersException {
			if (teamName == null)
				throw new BadParametersException("name of competition is empty");
			checkStringName(teamName);
			this.teamName = teamName;
		}
		
		
		/**
		 *  set value of list of athlete.
		 * 
		 * @param  ArrayList<Athlete> athletes

		 */
		private void setAthletes(ArrayList<Athlete> athletes) {
			this.athletes =athletes;
		}
		

	    @Override
	    public void addMember(Competitor member)
	            throws ExistingCompetitorException, BadParametersException {   	
	    	
			if (athletes.contains(member)){
				throw new ExistingCompetitorException("competitor " + member.toString() +" is already in the competition.");
			}
			athletes.add((Athlete) member);
	    }

	    @Override
	    public void deleteMember(Competitor member) throws BadParametersException,
	            ExistingCompetitorException {
			if (! athletes.contains(member))
				throw new ExistingCompetitorException("competition doesn't exist");
			athletes.remove(member);
	    }
	    
	    @Override
		public boolean isInCompetition(Competition competition){
			return competitions.contains(competition);
		}
		
	    @Override
		public void addCompetition(Competition newCompetition){
			if ( competitions.contains(newCompetition))
				 throw new IllegalArgumentException("Competition already exist in the list of this Athlete");
			this.competitions.add(newCompetition);
			for (Athlete athlete:this.athletes)
				athlete.addCompetition(newCompetition);
			
		}
	    @Override
		public void deleteCompetition(Competition oldCompetition){
			if (! competitions.contains(oldCompetition))
				 throw new IllegalArgumentException("Competition doesn't exist in the list of this Athlete");
			this.competitions.remove(oldCompetition);
			for (Athlete athlete:this.athletes)
				athlete.addCompetition(oldCompetition);		
		}
	    

	   
		/**
		 * verify if the name of team is valid 
		 * 
		 * @param a_Name
		 * @throws BadParametersException
		 */	 
		private static void checkStringName(String a_Name)
				throws BadParametersException {
			if (a_Name == null)
				throw new BadParametersException("teamName not instantiated");

			if (a_Name.length() < MIN_NAME)
				throw new BadParametersException("teamName length should be more than "
						+ MIN_NAME + "characters");
			// Just letters and digits are allowed
			if (!a_Name.matches(REGEX_NAME))
				throw new BadParametersException("the teamName " + a_Name
						+ " does not verify constraints ");
		}
		
		
	    @Override
	    public boolean hasValidName(){
	    	if (this.teamName == null)
				return false;

			if (this.teamName.length() < MIN_NAME)
				return false;
			// Just letters and digits are allowed
			if (!this.teamName.matches(REGEX_NAME))
				return false;
			return true;
	    }
	    
	    
	    
		/**
		 * get name of team
		 * @return 
		 * 		String teamName
		 */
		public String getTeamName(){
			return this.teamName;
		}

		/**
		 * get list of athletes in the team
		 * @return 
		 * 		ArrayList<Athlete> the list of athletes
		 */
		public ArrayList<Athlete> getAthletes(){
			return this.athletes;
		}
		
		public String toString(){
			  
			return "Team：" + this.teamName ;  
				 
		}
		
		/**
		 * get id of the team
		 * @return 
		 * 		 id
		 */
		@Override
		public int getId(){
			return this.id_team;
		}
		
		/**
		 * set id of the team
		 */
		public void setId(int id){
			this.id_team = id;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Team) {
				return this.id_team == ((Team) obj).getId();
			}
			return false;
		}
}
	
