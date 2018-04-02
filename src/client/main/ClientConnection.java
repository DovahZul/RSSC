package client.main;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import server.model.CommandModel;
import server.model.CommandProperty;

/**
 * Created by user on 02.12.2016.
 */
public class ClientConnection {
    public static Socket cok = null;
    public static int port = 8073;
    public static PrintStream ps;
    public static BufferedReader br;
    public static ObjectInputStream deserializer;
    public static ObjectOutputStream serializer;
    public static InputStream is;
    public static InputStream terminalInputStream;
    public static OutputStream terminalOutputStream;
    public static void connection() throws UnknownHostException,IOException {

    	System.out.println("Connecting to server["+InetAddress.getLocalHost()+":"+port+"]... ");
        cok = new Socket(InetAddress.getLocalHost(), port);

      //  ps = new PrintStream(cok.getOutputStream());

       // br = new BufferedReader(new InputStreamReader(cok.getInputStream()));

        serializer = new ObjectOutputStream(cok.getOutputStream());

        is = cok.getInputStream();
        deserializer = new ObjectInputStream(is);
        System.out.println("Connection established!");
    }

    public static List<CommandModel> getCommands() throws SocketException, InterruptedException
    {
    	//cok.setSoTimeout(2000);
    	List<CommandModel> l = new ArrayList<>();
    		try
        	{
        		serializer.writeObject("GetComm");
        		System.out.println("Loading commands from database...");

    		Object model = "";
    		do
    		{

    			System.out.println("ЗАШЛИ В ЦИКЛ");
    			System.out.println("");
    			System.out.println(is.available());
    			if(is.available() > 0){
    				model=deserializer.readObject();
    				System.out.println("ПОПАЛИ В readObj");
    				if (model.getClass().getSimpleName().compareTo("CommandModel") == 0)
        			l.add((CommandModel)model);
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
    		while(cok.isConnected() && !cok.isInputShutdown());


    		System.out.println("got commands:");
    		for(CommandModel item : l)
			{
				System.out.println(item.getCommand()+":"+item.getType()+":"+item.isActive());
				System.out.println("Properties count="+item.getProperties().length);
				for(CommandProperty p : item.getProperties())
				{
					System.out.println(p.getName()+":"+p.getValue());
				}
			}

    	}
    	catch(IOException ex)
    	{
    		System.out.println(ex.getLocalizedMessage()+ " while getting commands");
    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;

    }

    public static boolean sendCommands(List<CommandModel> loadedCommands) throws UnknownHostException, IOException {

    	System.out.println("[CLIENT] final commandList is:");
    	for(CommandModel m : loadedCommands)
    	{
    		System.out.println("ID:"+m.getId());
    		System.out.println("Command:"+m.getCommand());
    		System.out.println("Type:"+m.getType());
    		System.out.println("Active:"+m.isActive());
    	}


    	connection();
    	System.out.println("[CLIENT] Sending commands list to server...");
    	try
    	{
    		serializer.writeObject("SendComm");
    		for(CommandModel m: loadedCommands)
    		{
    			serializer.writeObject(m);
    		}
    		serializer.writeObject("FINISH");
			cok.shutdownInput();
			cok.close();
			System.err.println("[CLIENT] Connection closed, command list sent.");
			return true;
    	}
    	catch(Exception e)
    	{
    		System.err.println("[CLIENT] Failed to save command list. Aboring");
    	}
    	return false;



	}


    public static String SendCommandString(String script) throws IOException
    {
    	connection();
    	String str;

    	System.out.println("SendCommandStrng script="+script);

    	if(script==null)
    	{
    		System.out.println("SendCommandStrng got null! Aborting.");
    		return null;
    	}

		System.out.println("Sending bash command from client:"+script);
		serializer.writeObject("BASH-TERMINAL"+script);

		//serializer.flush();
		try
		{
		str= (String)deserializer.readObject();
		return str;
		}catch(IOException e)
		{
			System.out.println(e+ "[CLIENT] IO error while getting terminal callback! Aborting.");
		}
		catch (ClassNotFoundException e) {
			System.out.println(e+ " Class not found error while getting terminal callback, return null! ");
		}

		return "null pointer";


    }

	public static boolean sendDeletedId(List<Integer> deletedCommands) throws UnknownHostException, IOException
	{
		System.out.println("[CLIENT]Deleted command ID's is:");
    	for(Integer m : deletedCommands)
    	{
    		System.out.println("ID:"+m);
    	}
    	connection();
    	System.out.println("[CLIENT] Sending commands list to server...");
    	try
    	{
    		serializer.writeObject("SendDelComm");
    		for(Integer index: deletedCommands)
    		{
    			serializer.writeObject(index);
    		}
    		serializer.writeObject("FINISH");
			cok.shutdownInput();
			cok.close();
			System.err.println("[CLIENT] Connection closed, deleted ID's list sent.");
			return true;
    	}
    	catch(Exception e)
    	{
    		System.err.println("[CLIENT] Failed to send deleted ID's list. Aboring");
    	}
    	return false;

	}



}
