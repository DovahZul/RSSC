package server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

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
		try {
			while(true){
				System.out.println("listening thread is russing...");
				Thread.sleep(10000);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


}
