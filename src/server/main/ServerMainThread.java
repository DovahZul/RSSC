package server.main;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import server.controller.MainServerController;
import server.model.CommandModel;
import server.model.CommandProperty;

public class ServerMainThread extends Thread{

	private final String SCRIPTS_PATH= "";
	private final String DB_PATH="";
	private final String port = "8073";


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ServerMainThread(){

	}

	public void run() {
		System.out.println("starting main thread");
		try {
			List<CommandModel> ls = DataBaseManager.getRegularCommads();

			System.out.println("main server thread is running...");
				while(true)
				{
					//System.out.println("main server thread is running...");
					//System.out.println(Calendar.getInstance().getTimeInMillis());
					Thread.sleep(1000);

					for(CommandModel item : ls)
					{
						CommandProperty[] mass = item.getProperties();

						/*
						System.err.println("Command: "+item.getCommand());
						System.err.println("Type: "+item.getType());
						System.err.println("Active: "+item.isActive());

						for (int i = 0; i < mass.length; i++)
						{
							System.out.println(":" + mass[i].getName() + " = " + mass[i].getValue());
							//System.out.print(":"+item.getCount());
						}
												*/
						if(item.haveToExecute())
						{
							MainServerController.executeBashCommand(item.getCommand());
							//item.Decrease();
						}
					//MainController.executeBashCommand("notify-send \"My name is bash and I rock da hous\"");
				}

				}
			}
		catch (InterruptedException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

}
}

