package fr.uv1.tests.databaseTests;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import fr.uv1.bettingServices.exceptions.AuthentificationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.subscribers.Subscriber;
import fr.uv1.database.SubscribersManager;
import fr.uv1.utils.MyCalendar;

public class TestDatabaseSubscriber {
	
	private	Subscriber subs;
	private Subscriber subsOnDB;
	
	@Test
    public void testPersistSubscriber() throws BadParametersException,SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
	
        subs = new Subscriber("Marcuzzi","Tom-Tom","totomat1",new MyCalendar(1995,8,9));
        
        SubscribersManager.persist(subs);
        
        subsOnDB = SubscribersManager.findByUsername("totomat1");
        
        assertTrue(subsOnDB.getFirstName().equals(subs.getFirstName()));
        assertTrue(subsOnDB.getFamilyName().equals(subs.getFamilyName()));
        assertTrue(subsOnDB.getUsername().equals(subs.getUsername()));
        assertTrue(subsOnDB.getPassword().equals(subs.getPassword()));
        assertTrue(subsOnDB.getBirthDate().equals(subs.getBirthDate()));
	}
	
	@Test
    public void testDeleteSubscriber() throws BadParametersException,SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
	
		subs = new Subscriber("Marcuzzi","Tom-Tom","totomat1",new MyCalendar(1995,8,9));   
		
	    SubscribersManager.persist(subs); 
        SubscribersManager.delete(subs);
        
        assertTrue(SubscribersManager.findByUsername(subs.getUsername())==null);

	}
	
	@Test
    public void testDeleteSubscriberByUsername() throws BadParametersException,SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
	
		 subs = new Subscriber("Marcuzzi","Tom-Tom","totomat30",new MyCalendar(1995,8,9));        
	     SubscribersManager.persist(subs); 
	     
         SubscribersManager.deleteByUsername("totomat30");
         
         assertTrue(SubscribersManager.findByUsername("totomat30")==null);

	}
	
	@Test
    public void testUpdateBalance() throws BadParametersException,SubscriberException, SQLException, CompetitionException, ExistingCompetitorException {
	
		 subs = new Subscriber("Marcuzzi","Tom-Tom","totomat3",new MyCalendar(1995,8,9));        
	     SubscribersManager.persist(subs); 
	     
	     subs.addRemoveCoins(1000);
	     
	     long balance = subs.getBalance();
	     
         SubscribersManager.updateCoins(subs);
         
         subsOnDB = (SubscribersManager.findByUsername(subs.getUsername()));
         
         SubscribersManager.delete(subs);
         
         assertTrue(subsOnDB.getBalance()==balance);

	}
	
	@Test
    public void testUpdatePassword() throws BadParametersException,SubscriberException, SQLException, CompetitionException, AuthentificationException, ExistingCompetitorException {
	
		 subs = new Subscriber("Marcuzzi","Tom-Tom","totomat6",new MyCalendar(1995,8,9));        
	     SubscribersManager.persist(subs); 
	     
	     String newPassword = "PA45d1r7";
	     
	     subs.changeSubscriberPassword("PA45d1r7", subs.getPassword());
	     
         SubscribersManager.updatePassword(subs);
         
         subsOnDB = (SubscribersManager.findByUsername(subs.getUsername()));
         
         assertTrue(subsOnDB.getPassword().equals(newPassword));
         
         SubscribersManager.delete(subs);

	}
	

}
