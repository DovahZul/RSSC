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
    public static PrintStream ps;
    public static BufferedReader br;
    public static ObjectInputStream deserializer;
    public static ObjectOutputStream serializer;
    public static InputStream terminalInputStream;
    public static OutputStream terminalOutputStream;
    public static void connection() throws UnknownHostException,IOException {
        cok = new Socket(InetAddress.getLocalHost(), 8073);

        ps = new PrintStream(cok.getOutputStream());

        br = new BufferedReader(new InputStreamReader(cok.getInputStream()));

        serializer = new ObjectOutputStream(cok.getOutputStream());

        deserializer = new ObjectInputStream(cok.getInputStream());
        System.out.println("Client Connection");
    }

    public static List<CommandModel> getCommands() throws SocketException
    {
    	cok.setSoTimeout(200);
    	List<CommandModel> l = new ArrayList<>();
    	try
    	{
    		serializer.writeObject("GetComm");
    		System.out.println("getting commands...");
    		//while(deserializer.readObject()!=null)
    		//while(deserializer.available()!=0)
    		while(true)
    		{
    			try{
    				l.add( (CommandModel) deserializer.readObject());


    			//l.add((CommandModel) deserializer.readObject());
    			//break;
    			}
    			catch (ClassNotFoundException e) {
					break;
				}


    		}
    		//l = (List<CommandModel>) deserializer.readObject();
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
    		serializer.flush();
    		deserializer.close();
    		serializer.close();

    	}
    	catch(IOException ex)
    	{
    		System.out.println(ex+ " while getting commands");
    	}
		return l;

    }
    public static boolean SendCommandString(String script) throws IOException
    {
    	serializer.writeObject("BASH-TERMINAL"+script);
		System.out.println("Sending bash command from client:"+script);
		serializer.flush();
		return false;


    }

}
