package server.main;

import java.nio.file.Path;

public class ServerMainThread extends Thread{

	private final String SCRIPTS_PATH= "";
	private final String DB_PATH="";
	private final String port = "";


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ServerMainThread(){

	}

	public void run() {
		System.out.println("starting main thread");
		try {
			while(true){
				System.out.println("main server thread is running...");
				Thread.sleep(10000);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
