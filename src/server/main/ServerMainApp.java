package server.main;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.Properties;

import com.sun.javafx.runtime.SystemProperties;

import javafx.application.Application;
import javafx.scene.*;


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
		 ServerSocket server = new ServerSocket(8072);
         System.out.println("Сервер готов к работе");
         ServerMainThread mainThread = new ServerMainThread();
         mainThread.run();
         while (true) {
             Socket socket = server.accept();
             System.out.println( "Подключился: "+socket.getInetAddress().getHostName());
             ServerControlThread controlThread = new ServerControlThread(socket);
             controlThread.setDaemon(true);
             controlThread.start();
         }

	}

}
