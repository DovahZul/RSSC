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

import server.model.CommandModel;
import server.model.CommandProperty;

public class ServerControlThread extends Thread{
	private Socket socket;
	private BufferedReader is;
	private PrintStream os;
	private InetAddress addr;

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
				System.out.println("thread-listener is running...");

				/*if("BASH-TERMINAL".equals(str.substring(0, "BASH-TERMINAL".length())))
				{
					try {

						Runtime rt = Runtime.getRuntime();
				    	Process proc = rt.exec(str.substring(13, str.length()));
						proc.waitFor();

						BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						String line;

						String result = "";
						while ((line = is.readLine()) != null) {
							result += line + "\n";
						}
						System.err.print("TERMINAL OUTPUT:"+result);
						serializer.writeObject(result);

					}
						//SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText(result);

						catch(Exception e)
						{

						}
					}*/

				if("SaveCommands".equals(str))
				{

				}else
				if("GetComm".equals(str))
				{
					List<CommandModel> l=DataBaseManager.getRegularCommads();
					//serializer.writeObject(l);
					System.out.println("Send commands for client:");
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
					System.err.println("Server connection closed");

					return;
				}


				//Thread.sleep(10000);
				//return;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


}
