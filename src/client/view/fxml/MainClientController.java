package client.view.fxml;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

import client.main.ClientConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import server.model.CommandModel;
import server.model.CommandProperty;

public class MainClientController implements Initializable {


	@FXML TableView tableCommands;
	@FXML TableColumn<CommandModel, String> columnCommand;
	@FXML TableColumn<CommandModel, String> columnType;
	@FXML TableColumn<CommandModel, String> columnEnabled;
	@FXML TableColumn<CommandModel, CommandProperty> columnProperties;

	@FXML Button buttonSave;

	@FXML TextArea inputTerminal;
	@FXML TextArea outputTerminal;


	@FXML
	void loadCommands() throws UnknownHostException, IOException
	{


		ClientConnection.connection();
		//ClientConnection.SendCommandString("notify-send \"ALALA\"");
		ClientConnection.SendCommandString("ls /etc");
        List<CommandModel> list =ClientConnection.getCommands();
         // CommandModel m=new CommandModel(l);
          final ObservableList<CommandModel> data = FXCollections.observableArrayList();

          columnCommand.setCellValueFactory(
        		    new PropertyValueFactory<CommandModel,String>("Command")
        		);
          columnType.setCellValueFactory(
      		    new PropertyValueFactory<CommandModel,String>("Type")
      		);
          columnEnabled.setCellValueFactory(
        		    new PropertyValueFactory<CommandModel,String>("Active")
        		);
          columnProperties.setCellValueFactory(
        		    new PropertyValueFactory<CommandModel,CommandProperty>("Properties")
        		);



          for(CommandModel model : list)
          {
          	data.add(model);
          	System.err.println("Command:"+model.getCommand());
          	System.err.println("Type:"+model.getType());
          	if(model.getProperties().length<=0)System.err.println("ZERO PROP LENGTH");
          	for(CommandProperty p : model.getProperties())
          	{
          		System.err.println(p.getName()+":"+p.getValue());
          	}
          }
          tableCommands.setItems(data);

	}
	/*
	@XFML void saveCommands()
	{

	}
*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}




}
