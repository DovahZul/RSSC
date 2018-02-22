package server.main;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import server.model.CommandModel;

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

			final List<CommandModel> list = new ArrayList<CommandModel>();
			SqlObserver sobs = (s) -> {

				int id = s.getInt("id");
				//...
				CommandModel tmp = new CommandModel();
						list.add(tmp);
			};
			DataBaseManager.executeQueryResult("Select * from data", sobs);

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
