package server.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.model.CommandModel;
import server.model.CommandProperty;

public abstract class DataBaseManager
{

	private static String DBPath = System.getProperty("user.dir")+ "/data";
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
			Class.forName("org.sqlite.JDBC");//set SQL driver
			//Class.forName("org.postgresql.Driver");//set postgres driver
			System.out.println("JDBC SqlLite driver is ok");
		}
		catch (ClassNotFoundException e1)
		{

			e1.printStackTrace();
			//System.out.println("PostgreSQL JDBC Driver is not found.");
			System.out.println("Sqlite JDBC Driver is not found.");
		}

		try
		{
			boolean temp=executeSimpleQuery("CREATE DATABASE data");
		}
		catch(Exception e)
		{

		}

		try
		{

			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			System.out.println("Database connected successfully");

		} catch (SQLException e)
		{
			System.out.println("SQLException:"+e.getLocalizedMessage());
			e.printStackTrace();

		}
/*
	    try {
	      connection = DriverManager.getConnection(DB_URL, USER, PASS);

	    } catch (SQLException e) {
	      System.out.println("Connection Failed");
	      e.printStackTrace();
	      return;
	    }
*/
	}

	public static List<CommandModel> getRegularCommads()
	{
		final int id_command=-1;
		//List<CommandModel> list = new ArrayList<CommandModel>();
		List<CommandProperty> list = new ArrayList<CommandProperty>();
		Map<Integer, CommandModel> commands = new HashMap<Integer, CommandModel>();
		SqlObserver sobs = (s) ->
		{
			try
			{
					CommandProperty item=new CommandProperty(s.getInt("id_command"),s.getString("property"),s.getInt("Value"));
				if (commands.get(item.getId()) == null) {
					commands.put(item.getId(), new CommandModel(item.getId(), s.getString("command_context"), s.getBoolean("active"),s.getInt("command_type")));
				}
				list.add(item);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		};
		DataBaseManager.executeQueryResult("SELECT commands.id_command,command_context,commands.active,commands.command_type,property,value FROM commands INNER JOIN properties on commands.id_command=properties.id_command", sobs);


		for (CommandProperty item : list) {
			CommandModel cm = commands.get(item.getId());
			cm.addProperty(item);
		}
		return new ArrayList<>(commands.values());

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

		public static boolean executePreparedQuery(String string, SqlPrepared prep) {

			try
			{
				PreparedStatement statement = connection.prepareStatement(string);
				prep.insert(statement);
				statement.executeUpdate();
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
