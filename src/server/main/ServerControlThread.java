package server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import server.controller.MainServerController;
import server.model.CommandModel;
import server.model.CommandProperty;

public class ServerControlThread extends Thread{
	private Socket socket;
	private BufferedReader is;
	private PrintStream os;
	private InetAddress addr;
	private static final String TERMINAL_MODIFIER="BASH-TERMINAL";

	public ServerControlThread(Socket s) throws IOException{
		socket = s;
		addr = s.getInetAddress();
		is = new BufferedReader(new InputStreamReader(s.getInputStream()));
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
