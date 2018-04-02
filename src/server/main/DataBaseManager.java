package server.main;

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
	public static void savePropertiesForCommand(CommandModel command)
	{
		for(CommandProperty property : command.getProperties())
		{
			SqlPrepared prep = (stat) ->
			{
				try
				{
					//stat.setInt(1, command.getId());
					stat.setInt(1, property.getValue());
					stat.setInt(2, command.getId());
					stat.setString(3, property.getName());
				}catch(SQLException e)
				{
					System.out.println("[SERVER] Failed while saving property to DB.(part of another save) "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("UPDATE properties SET "+
					//"property=?,"										+
					"value=?"											+
					"WHERE id_command=? AND property=?"
					, prep);
				}
	}

	public static <T> List<T> subtract(List<T> list1, List<T> list2)
	{
		List<T> result = new ArrayList<T>();
		Set<T> set2 = new HashSet<T>(list2);
		for (T t1 : list1) {
			if( !set2.contains(t1) ) {
				result.add(t1);
			}
		}
		return result;
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
		//List<CommandModel> original=DataBaseManager.getRegularCommads();
		//DataBaseManager.executeSimpleQuery("DELETE from commands");
			//System.out.println("[DB MANAGER] COMMANDS FOR REMOVING:");

		//List<CommandModel> delete = null;

		//System.out.println("[DB MANAGER] COMMANDS FOR REMOVING:"+list.size()+":"+original.size());



		for(CommandModel command : list)
		{
			/*
			boolean flag=true;

			if(list!=original)System.out.println("[DB MANAGER] original difference with new list!");
			else System.out.println("[DB MANAGER] no difference between original and new list!");
			if(list.size() >= original.size())
			{
				System.out.println("[DB MANAGER] original <= new list, flag=false");
				//flag=false;
			}else
			{
			for(CommandModel rem : original)
			{

				//System.out.println("flag ID:"+flag);

					//flag=true;
				if(command.getId()==rem.getId())
				{
					System.out.println("exists, not deleting:");
					System.out.println("ID:"+rem.getId());
					System.out.println("command:"+rem.getCommand());

					flag=false;
					//delete.add(rem);
					//break;

				}
			}
			System.out.println("[DB MANAGER]flag= "+ flag);
			if(flag)
			{
				System.out.println("[DB MANAGER] DELETING COMMAND: "+ command.getId());
				SqlPrepared prep3 = (stat) ->
				{
					try
					{
						stat.setInt(1, command.getId());

					}catch(SQLException e)
					{
						System.out.println("[SERVER] Failed while deleting from DB. "+ e);
					}
				};
				DataBaseManager.executePreparedQuery("DELETE FROM commands "+
				"WHERE id_command = ?", prep3);

			}

			}
			*/

			//savePropertiesForCommand(command);
			/*
			for(CommandModel rem : original)
			{
				if(!list.contains(rem))
			SqlPrepared prep3 = (stat) ->
			{
				try
				{
					stat.setInt(1, rem.getId());

				}catch(SQLException e)
				{
					System.out.println("[SERVER] Failed while deleting to DB. "+ e);
				}
			};
			DataBaseManager.executePreparedQuery("DELETE FROM commands "+
			"WHERE id_command = ?", prep3);

			}
			*/

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
