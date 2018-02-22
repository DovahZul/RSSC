package server.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import server.model.CommandModel;

public abstract class DataBaseManager
{

	//private static String DBPath = System.getProperty("user.dir")+ "/data";
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/data";
	static final String USER = "postgres";
	static final String PASS = "1111";




	private static Connection connection = null;

	public static Connection getConnection(){

		return connection;
	}

	public static void Init()
	{
		try
		{
			//Class.forName("org.sqlite.JDBC");//set SQL driver
			Class.forName("org.postgresql.Driver");//set postgres driver
			System.out.println("Database connected successfully");
		}
		catch (ClassNotFoundException e1)
		{

			e1.printStackTrace();
			System.out.println("PostgreSQL JDBC Driver is not found.");
		}
/*
		try
		{

			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			System.out.println("Database connected successfully");

		} catch (SQLException e)
		{
			System.out.println("SQLException:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
*/
	    try {
	      connection = DriverManager.getConnection(DB_URL, USER, PASS);

	    } catch (SQLException e) {
	      System.out.println("Connection Failed");
	      e.printStackTrace();
	      return;
	    }

	}

	public List<CommandModel> getRegularCommads(long secs)
	{

		return null;


	}

	public static boolean executeSimpleQuery(String query)
		{
			try
			{
				Statement  statement  = connection.createStatement();
				statement.executeUpdate(query);
				statement.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}

			return true;
		}

	//Execute SQL query by use observer result(SELECT)
		public static boolean executeQueryResult(String query, SqlObserver sobs)
		{
			try
			{
				Statement  statement  = connection.createStatement();
				ResultSet  result     = statement.executeQuery(query);

				while (result.next())
				{
					sobs.reaction(result);
				}

				result.close();
				statement.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}

			return true;
		}


}
