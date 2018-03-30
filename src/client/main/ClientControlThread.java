package client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientControlThread implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket socket = null;
        BufferedReader br = null;
        /*
        try {
        	System.out.println("Starting terminal client...");
            //MainClientController controller = new MainClientController();
            //ClientMainApp a=new ClientMainApp();
            //a.init();
            //controller.startApplication();
           // ClientConnection.connection();
            //ClientConnection.getCommands();

        } catch (UnknownHostException e) {
            System.err.println("адрес недоступен" + e);
        } catch (IOException e) {
            System.err.println("ошибка I/О потока" + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket!= null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
*/

	}

}
