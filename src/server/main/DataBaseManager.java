package server.main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server.model.CommandModel;
import server.model.CommandProperty;

public abstract class DataBaseManager
{	
	private static String DBPath = "./server-data/tasks";
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/data";
	
	@Deprecated
	static final String USER = "postgres";
	@Deprecated
	static final String PASS = "1111";

	private static Connection connection = null;
	
	public static Connection getConnection() {
		
		return connection;
	}

	public static void Init()
	{

		System.out.println("Database path: " + DBPath);
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
			boolean temp = executeSimpleQuery("CREATE DATABASE data");
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
			System.out.println("SQLException:" + e.getLocalizedMessage());
			e.printStackTrace();

		}

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
	public static List<CommandProperty> validateProperties(int idComm, List<CommandProperty> arr)
	{
		List<CommandProperty> temp=arr;
		boolean second=false;
		boolean minute=false;
		boolean hour=false;
		boolean day=false;
		boolean month=false;
		for(CommandProperty property : temp)
		{
			switch(property.getName())
			{
			case "second":
				second=true;
			break;
			case "minute":
				minute=true;
			break;
			case "hour":
				hour=true;
			break;
			case "day":
				day=true;
			break;
			case "month":
				month=true;
			break;
			}
		}
		if(!second)temp.add(new CommandProperty(idComm, "second",-1));
		if(!minute)temp.add(new CommandProperty(idComm, "minute",-1));
		if(!hour)temp.add(new CommandProperty(idComm, "hour",-1));
		if(!day)temp.add(new CommandProperty(idComm, "day",-1));
		if(!month)temp.add(new CommandProperty(idComm, "month",-1));
		return temp;
	}


	public static void savePropertiesForCommand(CommandModel command)
	{
		System.out.println("[SERVER] Saving property for command_id="+command.getId()+"...count = "+command.getProperties().length);



			//deleting old properties with current id_command


			SqlPrepared prepDelete = (stat) ->
			{
				try
				{
					stat.setInt(1, command.getId());
				}catch(SQLException e)
				{
					System.out.println("[SERVER] Failed while saving property to DB.(part of another save) "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("DELETE FROM properties WHERE id_command = ? ", prepDelete);




			//Filling with new values
		for(CommandProperty property : validateProperties(command.getId(), command.getPropertiesList()))
		{
			SqlPrepared prep = (stat) ->
			{
				try
				{
					//stat.setInt(1, command.getId());
					stat.setString(1, property.getName());
					stat.setInt(2, property.getValue());
					stat.setInt(3, command.getId());
				}catch(SQLException e)
				{
					System.out.println("[SERVER] Failed while saving property to DB.(part of another save) "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("INSERT INTO properties ("+
					"property,"										+
					"value,"										+
					"id_command"									+
					") VALUES "										+
					"(?,?,?)"
					//"WHERE id_command = ?"
					, prep);

				}
	}

	public static void deleteCommandsById(List<Integer> tempCommandList)
	{

		for(Integer id : tempCommandList)
		{
			SqlPrepared prep2 = (stat) ->
			{
				try
				{
					stat.setInt(1, id);
				}catch(SQLException e)
				{
						System.out.println("[SERVER] Failed while deleting old commands from DB. "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("DELETE FROM commands WHERE id_command = ?", prep2);
		}
	}

	public static void saveRegularCommands(List<CommandModel> list)
	{
		//DataBaseManager.executeSimpleQuery("DELETE * FROM properties");
		for(CommandModel command : list)
		{
			//down there
			//savePropertiesForCommand(command);

			SqlPrepared prep2 = (stat) ->
			{
				try
				{
					stat.setInt(1, command.getId());
					stat.setString(2, command.getCommand());
					stat.setInt(3, command.getType());
					stat.setBoolean(4, command.isActive());
				}catch(SQLException e)
				{
					System.out.println("[SERVER] Failed while saving to DB. "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("INSERT OR IGNORE INTO commands(  "+
			"id_command,"												+
			"command_context,"											+
			"command_type,"												+
			"active )"													+
			"VALUES (?,?,?,?)"
			, prep2);


			SqlPrepared prep = (stat) ->
			{

				try
				{
					//stat.setInt(1, command.getId());
					stat.setString(1, command.getCommand());
					stat.setInt(2, command.getType());
					stat.setBoolean(3, command.isActive());
					stat.setInt(4, command.getId());
				}catch(SQLException e)
				{
					System.out.println("[SERVER] Failed while saving to DB. "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("UPDATE commands SET "+
			//"id_command,"												+
			"command_context=?,"										+
			"command_type=?,"											+
			"active=?"													+
			"WHERE id_command=?"
			, prep);
			savePropertiesForCommand(command);
		}
	}

	public static boolean executeSimpleQuery(String query)
		{
			try
			{
				Statement  statement = connection.createStatement();
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
