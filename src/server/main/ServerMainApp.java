package server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class ServerMainApp{

	public static void main(String[] args) throws IOException {

		DataBaseManager.Init();


		Properties systemProps = System.getProperties();
		System.out.println("Target operting system:"+systemProps.getProperty("os.name"));
		switch(systemProps.getProperty("os.name")){
			case("Linux"):

			break;
			case("Windows"):

			break;


		}
		 ServerSocket server = new ServerSocket(8073);
         System.out.println("Сервер готов к работе");
         ServerMainThread mainThread = new ServerMainThread();
         mainThread.start();
         while (true) {
        	 System.out.println("Запуск прослушивающего потока...");
             Socket socket = server.accept();
             System.out.println( "Подключился: "+socket.getInetAddress().getHostName());
             ServerControlThread controlThread = new ServerControlThread(socket);
             //controlThread.setDaemon(true);
             controlThread.start();
         }

	}

}
