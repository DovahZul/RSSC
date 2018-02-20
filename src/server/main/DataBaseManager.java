package server.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataBaseManager
{

	private static String DBPath = System.getProperty("user.dir")+ "/ScheduleDB";



	private static Connection connection = null;

	public static Connection getConnection(){

		return connection;
	}

	public static void Init()
	{

		try
		{
			Class.forName("org.sqlite.JDBC"); //set SQL driver
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			System.out.println("Database connected successfully");

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println("DataBaseManager exceprion:"+e.getLocalizedMessage());
			e.printStackTrace();
		}


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


}
