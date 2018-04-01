package server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import server.controller.MainServerController;
import server.model.CommandModel;
import server.model.CommandProperty;

public class ServerControlThread extends Thread{
	private Socket socket;
	private InputStream is;
	private PrintStream os;
	private InetAddress addr;
	private static final String TERMINAL_MODIFIER="BASH-TERMINAL";

	public ServerControlThread(Socket s) throws IOException{
		socket = s;
		addr = s.getInetAddress();
		//is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		is = socket.getInputStream();
		os = new PrintStream(s.getOutputStream());

	}

	public void run(){
		System.out.println("starting control thread");
		String str = null;
		try {

			ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream deserializer = new ObjectInputStream(socket.getInputStream());
			//DataBaseManager.Init();





			while((str = (String)deserializer.readObject()) != null){
				System.out.println("[SERVER]thread-listener is running...");

				if(str.length()>13)
				{
					if(TERMINAL_MODIFIER.compareTo( str.substring(0, TERMINAL_MODIFIER.length())  ) == 0)
					{

					System.out.println("[SERVER] EXECUTING BASH-TERMINAL:"+str.substring(13, str.length() ));
					serializer.writeObject((String)MainServerController.executeBashCommand( str.substring(13, str.length() )) );
					//serializer.writeObject((String)"GREETING:"+MainServerController.getTerminalGreeting());
					socket.shutdownInput();
					socket.close();
					System.err.println("[SERVER] Connection closed");
					}
				}else
				if("SaveCommands".equals(str))
				{

				}else
				if("GetComm".equals(str))
				{
					System.out.println("{SERVER] SENDING COMMANDS FOR CLIENT...");
					List<CommandModel> l=DataBaseManager.getRegularCommads();
					//serializer.writeObject(l);

					for(CommandModel item : l)
					{
						serializer.writeObject(item);
						System.out.println(item.getCommand()+":"+item.getType()+":"+item.isActive());
						System.err.println("Properties count="+item.getProperties().length);
						for(CommandProperty p : item.getProperties())
						{
							System.out.println(p.getName()+":"+p.getValue());
						}
					}
					//serializer.flush();
					//deserializer.close();
					//serializer.close();
					serializer.writeObject("FINISH");
					socket.shutdownInput();
					socket.close();
					System.err.println("[SERVER] Connection closed");

					return;
				}else
				if("SendComm".equals(str))
				{
					System.out.println("[SERVER] receiving command list from client...");
					List<CommandModel> tempCommandList = new ArrayList<>();
					Object model = "";
					try
					{
						do
						{
							if(is.available() > 0)
							{
			    				model=deserializer.readObject();
			    				System.out.println("ПОПАЛИ В readObj");
			    				if (model.getClass().getSimpleName().compareTo("CommandModel") == 0)
			    					tempCommandList.add((CommandModel)model);
			    			}
			    				else System.out.println("DDeserializer NOT available");

							System.out.println("STRING SIMPLE NAME="+model.getClass().getSimpleName());
			    			if (model.getClass().getSimpleName().compareTo("String") == 0)
			    			{
			    				System.out.println("MOdel="+model);
			    				if (((String)model).compareTo("FINISH") == 0)
			    				{

			    					System.out.println("STOP STRING COMMAND");
			    					break;
			    				}
			    			}
			    		}
			    		while(socket.isConnected() && !socket.isInputShutdown());



					}
					catch(Exception e)
					{

					}

					System.out.println("[SERVER] got commands from client:");
		    		for(CommandModel item : tempCommandList)
					{
						System.out.println(item.getCommand()+":"+item.getType()+":"+item.isActive());
						System.out.println("Properties count="+item.getProperties().length);
						for(CommandProperty p : item.getProperties())
						{
							System.out.println(p.getName()+":"+p.getValue());
						}
					}

				}


				//Thread.sleep(10000);
				//return;

			}

		} catch (IOException e) {
			//System.out.println("[SERVER]");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


}
