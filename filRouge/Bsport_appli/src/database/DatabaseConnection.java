package fr.uv1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing the connection to the database.
 * 
 * Based on Philippe TANGUY's example.
 */
public class DatabaseConnection
{
	private static String username        = "wchen01";
	  private static String password        = "ORA2528";
	  private static String host            = "@oracle-tp.svc.enst-bretagne.fr";
	  private static String numPort         = "1521";
	  private static String base            = "enseig";
	  private static String connectString   = "jdbc:oracle:thin:" + host + ":" + numPort + ":" + base;
	  static
	  {
	    try
	    {
	    	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	    }
	  }

  /**
   * Obtaining a connection to the database.
   *  
   * @return an instance of the Connection class.
   * @throws SQLException
   */
  public static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(connectString,username,password);
  }
}
