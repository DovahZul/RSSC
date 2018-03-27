package client.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import client.view.fxml.MainClientController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import server.model.CommandModel;

public class ClientMainApp extends Application{
	private static Stage primaryStage = null;
	private static MainClientController mainController;

	public static void main(String[] args)
	{
		launch();
	}

	public static Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public static MainClientController getMainContoller() {
		return mainController;
	}

	@Override
	public void start(Stage ps) throws Exception {
		// TODO Auto-generated method stub
		ClientMainApp.primaryStage=ps;

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/client/view/fxml/MainView.fxml"));
		BorderPane root = loader.load();
		mainController=loader.getController();

		Scene scene = new Scene(root, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		Socket socket = null;
        BufferedReader br = null;

        /*
        try {
        	System.out.println("Starting terminal client...");
            //MainClientController controller = new MainClientController();
            //ClientMainApp a=new ClientMainApp();
            //a.init();
            //controller.startApplication();
          //  ClientConnection.connection();
          //  List<CommandModel> l =ClientConnection.getCommands();
           // CommandModel m=new CommandModel(l);
          //  final ObservableList<CommandModel> data = FXCollections.observableArrayList();
            //for(CommandModel m : l)
            //	data.add(m);





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
