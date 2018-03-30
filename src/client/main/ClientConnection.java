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
        		//while(deserializer.readObject()!=null)
        		//while(deserializer.available()!=0)

        		/*
        		CommandModel model;

        		if(is.available()>0) System.out.println("deserializer available");
        		else System.out.println("deserializer NOT availavle");

        		while((model = (CommandModel)deserializer.readObject()) != null)
        		{
        			l.add(model);
        		}
        		*/

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
    			if (model.getClass().getSimpleName().compareTo("String") == 0) {
    				System.out.println("MOdel="+model);
    				if (((String)model).compareTo("FINISH") == 0)
    				{

    					System.out.println("STOP STRING COMMAND");
    					break;
    				}
    			}
    			//Thread.sleep(1000);
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
    		//serializer.flush();
    		//deserializer.close();
    		//serializer.close();
    		//cok.close();

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

    public static String SendCommandString(String script) throws IOException
    {
    	String str;

    	serializer.writeObject("BASH-TERMINAL"+script);
		System.out.println("Sending bash command from client:"+script);
		serializer.flush();
		try
		{
		str= (String)deserializer.readObject();
		return str;
		}catch(IOException e)
		{
			System.out.println(e+ " IO error while getting terminal callback, return null!");
		}
		catch (ClassNotFoundException e) {
			System.out.println(e+ " Class not found error while getting terminal callback, return null! ");
		}

		return null;


    }

}
