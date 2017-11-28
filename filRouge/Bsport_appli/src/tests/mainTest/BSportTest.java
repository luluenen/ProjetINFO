package fr.uv1.tests.mainTest;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.uv1.bettingServices.competitors.Athlete;
import fr.uv1.bettingServices.competitors.Team;
import fr.uv1.bettingServices.exceptions.AuthentificationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.interfaces.Competitor;
import fr.uv1.bettingServices.main.BSport;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.database.AthleteManager;
import fr.uv1.database.DatabaseConnection;
import fr.uv1.database.SubscribersManager;
import fr.uv1.database.TeamManager;
import fr.uv1.utils.MyCalendar;


public class BSportTest {
	
	private static String MANAGER_PASSWORD = "1L0v31nF0";	
	private static BSport bs = new BSport(); 
	

	public static void databaseClean() throws SQLException{
		
		Connection c = DatabaseConnection.getConnection();

		String request1 = "delete from participation" ;
		
		
		
		String request2 = "delete from member";
		
		String request3 = "delete from draw_bet" ;
		String request4 = "delete from winner_bet" ;
		String request5 = "delete from podium_bet" ;
		String request6 = "delete from bet";
		
		String request7 = "delete from competition" ;
		
		String request8 = "delete from athlete"; 
		String request9 = "delete from team" ;
		String request10 = "delete from competitor" ;
	
		String request11 = "delete from subscriber";
		
		ArrayList<String> requests = new ArrayList<String>(Arrays.asList(request1, request2, request3, 
				request4, request5, request6, request7, request8, request9, request10, request11));
		
		for (String request : requests){
			
			Statement cleanStmt = c.createStatement();
			cleanStmt.executeUpdate(request);
			cleanStmt.close();
		}
		
		
		c.close();
		
		
	}
	
	
	public static void main(String[] args) throws AuthentificationException,
					ExistingSubscriberException, SubscriberException, 
					BadParametersException, SQLException, ExistingCompetitorException, CompetitionException, ExistingCompetitionException{
		
		
		databaseClean();

		System.out.println("Setting date 29/05/2016");
		MyCalendar.setDate(2016,5,29);
		
		//Subscribers
		System.out.print("New subscriptions...");
		
		bs.subscribe("Cruise", "Tom", "impossibleMan", "03/07/1962", MANAGER_PASSWORD);
		System.out.print(".");
		
		bs.subscribe("Chan", "Jackie", "woooooooTchah", "07/04/1954", MANAGER_PASSWORD);
		System.out.print(".");

		bs.subscribe("Federer", "Roger", "tennisforLife", "8/8/1981", MANAGER_PASSWORD);
		System.out.print(".");
		
		bs.subscribe("Smith", "Will", "iD0ntLikeR0b0ts", "25/09/1968", MANAGER_PASSWORD);
		System.out.print(".");
		
		bs.subscribe("Pitt", "Brad", "IloveANGIE", "18/12/1963", MANAGER_PASSWORD);
		System.out.print(".");
		
		bs.subscribe("Jolie", "Angelina", "IloveBrady", "04/06/1975", MANAGER_PASSWORD);
		System.out.print(".");
		
		bs.subscribe("Ridley", "Daisy", "jediMaster", "10/04/1992", MANAGER_PASSWORD);
		System.out.print(". ");
		
		System.out.println("COMPLETE\n");
		
		
		System.out.println("New subscribers:");
		for (List<String> s : bs.listSubscribers(MANAGER_PASSWORD)){
			System.out.println(s);
		}
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//impossibleMan unsubscription
		System.out.print("impossibleMan unsubscribing... ");
		bs.unsubscribe("impossibleMan", MANAGER_PASSWORD);
		System.out.println("COMPLETE\n");
		
		System.out.println("Subscribers:");
		for (List<String> s : bs.listSubscribers(MANAGER_PASSWORD)){
			System.out.println(s);
		}
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//credit
		System.out.print("WooooooooTchah balance is: ");
		System.out.println((SubscribersManager.findByUsername("woooooooTchah")).getBalance());
	
		System.out.print("Crediting wooooooooTchah... ");
		bs.creditSubscriber("woooooooTchah", 1000, MANAGER_PASSWORD);
		System.out.println("COMPLETE\n");

		System.out.print("WooooooooTchah balance is now: ");
		System.out.println((SubscribersManager.findByUsername("woooooooTchah")).getBalance());
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//debit
		System.out.print("WooooooooTchah balance is: ");
		System.out.println((SubscribersManager.findByUsername("woooooooTchah")).getBalance());
		
		System.out.print("Debiting wooooooooTchah... ");
		bs.debitSubscriber("woooooooTchah", 500, MANAGER_PASSWORD);
		System.out.println("COMPLETE\n");

		System.out.print("WooooooooTchah balance is now: ");
		System.out.println((SubscribersManager.findByUsername("woooooooTchah")).getBalance());
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//Competitors
		
		//Creating athletes
		System.out.print("Creating athletes...");
		
		bs.createCompetitor("Cruise", "Tom", "03/07/1962", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Chan", "Jackie","07/04/1954", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Nadal", "Rafael","3/06/1986" ,MANAGER_PASSWORD );
		System.out.print(".");
		bs.createCompetitor("Federer", "Roger", "8/8/1981", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Tsonga","Jo-Wilfried", "17/04/1985", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Noah", "Yannick", "18/05/1960", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Hollande","Francois","12/08/1954",MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Sarkozy", "Nicolas", "28/01/1955", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("Joly", "Eva", "5/12/1943", MANAGER_PASSWORD);
		System.out.print(". ");
		
		System.out.println("COMPLETE\n");
		
		//Creating teams
		System.out.print("Creating teams...");
		
		bs.createCompetitor("TeamTennisI", MANAGER_PASSWORD);
		System.out.print(".");
		bs.createCompetitor("TeamTennisII", MANAGER_PASSWORD);
		System.out.print(". ");
		
		System.out.println("COMPLETE\n");
		
		System.out.println("New competitors:");
		for (List<String> s : bs.listAllCompetitors()){
			System.out.println(s);

		}
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//Adding athletes to team
		System.out.print("Adding athletes to teams...");
		bs.addAhtleteToTeam("TeamTennisI", AthleteManager.findByAthleteByNameBirthdate("Tsonga","Jo-Wilfried", "17/4/1985"), MANAGER_PASSWORD);
		System.out.print(".");
		bs.addAhtleteToTeam("TeamTennisI", AthleteManager.findByAthleteByNameBirthdate("Noah", "Yannick", "18/5/1960"), MANAGER_PASSWORD);
		System.out.print(".");
		bs.addAhtleteToTeam("TeamTennisII", AthleteManager.findByAthleteByNameBirthdate("Federer", "Roger", "8/8/1981"), MANAGER_PASSWORD);
		System.out.print(". ");
		bs.addAhtleteToTeam("TeamTennisII", AthleteManager.findByAthleteByNameBirthdate("Nadal", "Rafael","3/6/1986"), MANAGER_PASSWORD);
		System.out.println("COMPLETE\n");
	
		System.out.println("\n-----------------------------------------------------\n");
	
		System.out.println("Teams:\n");
		for (Team t : TeamManager.findAll()){
			System.out.println("-----"+t.getTeamName()+"-----");
			
			for (Athlete athlete : t.getAthletes()){
				System.out.println(athlete);			
			}
			System.out.println("\n");	
		}
	
		System.out.println("\n-----------------------------------------------------\n");
		
		//Adding competition
		System.out.print("Adding competitions...");
		
		ArrayList<Competitor> competitors = new ArrayList<Competitor>(Arrays.asList(
				(Competitor)TeamManager.findByName("TeamTennisI"),
				(Competitor)TeamManager.findByName("TeamTennisII")));
		
		bs.addCompetition("RolandGarros", new MyCalendar(2016,6,5), competitors, MANAGER_PASSWORD);
		System.out.print(". ");
		
		competitors = new ArrayList<Competitor>(Arrays.asList(
				(Competitor) AthleteManager.findByAthleteByNameBirthdate("Hollande","Francois","12/8/1954"),
				(Competitor) AthleteManager.findByAthleteByNameBirthdate("Sarkozy", "Nicolas", "28/1/1955"),
				(Competitor) AthleteManager.findByAthleteByNameBirthdate("Joly", "Eva", "5/12/1943")));
		
		bs.addCompetition("Presidentielle2017", new MyCalendar(2017,6,5), competitors, MANAGER_PASSWORD);
		System.out.print("COMPLETE\n\n");
		
		System.out.println("Competitions:\n");
		
		for (List<String> comp : bs.listCompetitions()){
			
			System.out.print("#");
			
			for (String s : comp){
				System.out.println(s);
			}
			System.out.println("\n");
		}
			
		System.out.println("\n-----------------------------------------------------\n");
		
		//---------------Betting on winner--------------------------------------------
		System.out.println("Betting on winner for RolandGarros\n");
		
		//-----------------------------------------------------------
		System.out.println("Betting from woooooooTchah");
		
		Subscriber subs = SubscribersManager.findByUsername("woooooooTchah");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnWinner(500, "RolandGarros", TeamManager.findByName("TeamTennisII"), subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("woooooooTchah");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
				
		//-----------------------------------------------------------
		System.out.println("\nBetting from IloveANGIE");
		
		subs = SubscribersManager.findByUsername("IloveANGIE");		
		bs.creditSubscriber( subs.getUsername(),250000, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("IloveANGIE");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnWinner(500, "RolandGarros", TeamManager.findByName("TeamTennisII"), subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("IloveANGIE");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
		
		//-----------------------------------------------------------
		System.out.println("\nBetting from iD0ntLikeR0b0ts");
		
		subs = SubscribersManager.findByUsername("iD0ntLikeR0b0ts");
		bs.creditSubscriber( subs.getUsername(),500, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("iD0ntLikeR0b0ts");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnWinner(500, "RolandGarros", TeamManager.findByName("TeamTennisI"), subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("iD0ntLikeR0b0ts");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
		
		//-----------------------------------------------------------
		System.out.println("\nBetting from jediMaster");
		
		subs = SubscribersManager.findByUsername("jediMaster");
		bs.creditSubscriber( subs.getUsername(),10000, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("jediMaster");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnWinner(5000, "RolandGarros", TeamManager.findByName("TeamTennisII"), subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("jediMaster");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
		
		System.out.println("\n\nBets for RolandGarros:\n");
		
		for (String bet : bs.consultBetsCompetition("RolandGarros")){
			System.out.print("- ");
			System.out.println(bet);
		}
		
		System.out.println("\n-----------------------------------------------------\n");
		
		System.out.println("Setting date 6/6/2016");
		MyCalendar.setDate(2016,6,6);
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//Setting a winner for the competition
		
		System.out.println("Before setting the winner...\n");
				
		for (Subscriber sub : SubscribersManager.findAll()){
			System.out.println(sub.getUsername()+" had "+sub.getBalance()+" coins.");
		}
		
		System.out.println("\nSetting TeamTennisI winner for RolandGarros...");
		Competitor winner = TeamManager.findByName("TeamTennisI");
			
		bs.settleWinner("RolandGarros", winner, MANAGER_PASSWORD);
		System.out.println("COMPLETE\n");
		
		for (Subscriber sub : SubscribersManager.findAll()){
			System.out.println(sub.getUsername()+" has now "+sub.getBalance()+" coins.");
		}
		
		System.out.println("\n-----------------------------------------------------\n");

		System.out.println("Setting initial date 29/05/2016");
		MyCalendar.setDate(2016,5,29);
		
		System.out.println("\n-----------------------------------------------------\n");
		
		
		//---------------Betting on podium--------------------------------------------
		System.out.println("Betting on podium for Presidentielle2017\n");
		
		Competitor eva = AthleteManager.findByAthleteByNameBirthdate("Joly", "Eva", "5/12/1943");
		Competitor nicolas = AthleteManager.findByAthleteByNameBirthdate("Sarkozy", "Nicolas", "28/1/1955");
		Competitor francois = AthleteManager.findByAthleteByNameBirthdate("Hollande","Francois","12/8/1954");		
		
		//-----------------------------------------------------------
		System.out.println("Betting from woooooooTchah");
		
		subs = SubscribersManager.findByUsername("woooooooTchah");
		bs.creditSubscriber( subs.getUsername(),1000, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("woooooooTchah");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
				
		bs.betOnPodium(500, "Presidentielle2017", eva, nicolas, francois , subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("woooooooTchah");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
				
		//-----------------------------------------------------------
		System.out.println("\nBetting from IloveANGIE");
		
		subs = SubscribersManager.findByUsername("IloveANGIE");		
		bs.creditSubscriber( subs.getUsername(),1000, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("IloveANGIE");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnPodium(500, "Presidentielle2017", eva, nicolas, francois , subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("IloveANGIE");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
		
		//-----------------------------------------------------------
		System.out.println("\nBetting from iD0ntLikeR0b0ts");
		
		subs = SubscribersManager.findByUsername("iD0ntLikeR0b0ts");
		bs.creditSubscriber( subs.getUsername(),1000, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("iD0ntLikeR0b0ts");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnPodium(500, "Presidentielle2017", eva, nicolas, francois , subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("iD0ntLikeR0b0ts");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
		
		//-----------------------------------------------------------
		System.out.println("\nBetting from jediMaster");
		
		subs = SubscribersManager.findByUsername("jediMaster");
		bs.creditSubscriber( subs.getUsername(),1000, MANAGER_PASSWORD);
		
		subs = SubscribersManager.findByUsername("jediMaster");
		System.out.println(subs.getUsername() +" has "+subs.getBalance()+" coins.");
		
		bs.betOnPodium(500, "Presidentielle2017", francois, eva, nicolas , subs.getUsername(), subs.getPassword());
		
		subs = SubscribersManager.findByUsername("jediMaster");
		System.out.println(subs.getUsername() +" has now "+subs.getBalance()+" coins.");
		
		System.out.println("\n\nBets for Presidentielle2017:\n");
		
		for (String bet : bs.consultBetsCompetition("Presidentielle2017")){
			System.out.print("- ");
			System.out.println(bet);
		}
		
		System.out.println("\n-----------------------------------------------------\n");
		
		System.out.println("Setting date 6/6/2017");
		MyCalendar.setDate(2017,6,6);
		
		System.out.println("\n-----------------------------------------------------\n");
		
		//Setting a podium for the competition
		
		System.out.println("Before setting the podium...\n");
				
		for (Subscriber sub : SubscribersManager.findAll()){
			System.out.println(sub.getUsername()+" had "+sub.getBalance()+" coins.");
		}
		
		System.out.println("\nSetting podium Eva/Nicolas/Francois for Presidentielle2017...");
					
		bs.settlePodium("Presidentielle2017", eva, nicolas, francois, MANAGER_PASSWORD);
		System.out.println("COMPLETE\n");
		
		for (Subscriber sub : SubscribersManager.findAll()){
			System.out.println(sub.getUsername()+" has now "+sub.getBalance()+" coins.");
		}
		
		System.out.println("\n-----------------------------------------------------\n");
		
		
		
	}
	

}
