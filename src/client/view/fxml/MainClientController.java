package client.view.fxml;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

import client.main.ClientConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import server.model.CommandModel;
import server.model.CommandProperty;

public class MainClientController implements Initializable {



	@FXML TableView tableCommands;
	@FXML TableColumn<CommandModel, String> columnCommand;
	@FXML TableColumn<CommandModel, String> columnType;
	@FXML TableColumn<CommandModel, String> columnEnabled;
	@FXML TableColumn<CommandModel, String> columnProperties;

	@FXML TableView tableProperties;
	@FXML TableColumn<CommandProperty, String> columnPropertyName;
	@FXML TableColumn<CommandProperty, String> columnPropertyValue;


	@FXML Button buttonSave;
	@FXML Button buttonTerminalInput;

	@FXML TextArea inputTerminal;
	@FXML TextArea outputTerminal;

	@FXML TextField fieldPropertyName;
	@FXML TextField fieldPropertyValue;
	@FXML TextField fieldCommandContext;

	@FXML CheckBox checkBoxEnabled;


	@FXML Label statusLabel;

	public static List<CommandModel> loadedCommands;

	private String getStringProperty(CommandProperty[] p) {
		String seconds = "seconds:";
		String minutes = "minutes:";
		String hours = "hours:";
		String days = "days:";
		String months = "months:";
		for(CommandProperty item: p)
		{
			switch(item.getName())
			{
			case "second":
				seconds+=item.getValue()+",";
				break;
			case "minute":
				minutes+=item.getValue()+",";
				break;
			case "hour":
				hours+=item.getValue()+",";
				break;
			case "day":
				days+=item.getValue()+",";
				break;
			case "month":
				months+=item.getValue()+",";
				break;

			}
			if(item.getName()=="second")
			//for(String name : item.getName())
			seconds+=item.getValue()+",";
		}
		return seconds+minutes+hours+days+months;
	}

	@FXML
	void loadCommands() throws UnknownHostException, IOException
	{


		ClientConnection.connection();
		//ClientConnection.SendCommandString("notify-send \"ALALA\"");
		//ClientConnection.SendCommandString("ls /etc");
        //List<CommandModel> list=null;
		try {
			loadedCommands = ClientConnection.getCommands();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


          columnPropertyName.setCellValueFactory(
        		    new PropertyValueFactory<CommandProperty,String>("name")
        		);
          columnPropertyValue.setCellValueFactory(
      		    new PropertyValueFactory<CommandProperty,String>("value")
      		);


          final ObservableList<CommandModel> commanList = FXCollections.observableArrayList();

          for(CommandModel model : loadedCommands)
          {
        	  commanList.add(model);
          	System.err.println("Command:"+model.getCommand());
          	System.err.println("Type:"+model.getType());
          	if(model.getProperties().length<=0)System.err.println("ZERO PROP LENGTH");
          	for(CommandProperty p : model.getProperties())
          	{
          		System.err.println(p.getName()+":"+p.getValue());
          	}
          }
          tableCommands.setItems(commanList);

	}

	@FXML boolean ExecuteTerminalCommand()
	{
		if(inputTerminal.getText()!=null)return false;
		try
		{
			outputTerminal.setText(ClientConnection.SendCommandString(inputTerminal.getText()));
			return true;
		}catch(IOException e)
		{
			System.out.println("CLientCOntroller: ExecuteCommandFailed:"+e);
			return false;
		}
	}

	@FXML boolean descibeCommand()
	{
		try
		{
			int selectedIndex = this.tableCommands.getSelectionModel().getSelectedIndex();
			if(selectedIndex<0)
			{
				return false;
			}else
			{
				CommandModel m=(CommandModel) this.tableCommands.getItems().get(selectedIndex);
				setStatusLabetText("Selected item:"+m.getCommand()+"ID:"+m.getId());
				if(m.getProperties().length>0)
				{
					final ObservableList<CommandProperty> properties = FXCollections.observableArrayList();
					for(CommandProperty property : m.getProperties())
				    {
						properties.add(property);
						System.err.println("Property:"+property.getName());
						System.err.println("Value:"+property.getValue());

				    }
					tableProperties.setItems(properties);
				}

				this.fieldCommandContext.setText(m.getCommand());
				//(m.isActive())?this.checkBoxEnabled.setSelected(true);:this.checkBoxEnabled.setSelected(false);
				if(m.isActive())
					this.checkBoxEnabled.setSelected(true);
				else
					this.checkBoxEnabled.setSelected(false);

				return true;
			}
		}catch(IndexOutOfBoundsException e){
			return false;
		}
	}

	@FXML boolean describeProperty()
	{
		int selectedIndex = this.tableProperties.getSelectionModel().getSelectedIndex();
		if(selectedIndex<0)
		{
			return false;
		}else
		{
			CommandProperty p=(CommandProperty) this.tableProperties.getItems().get(selectedIndex);
			setStatusLabetText("Selected property:"+p.getName()+"Value:"+p.getValue());

			this.fieldPropertyName.setText(p.getName());
			this.fieldPropertyValue.setText(String.valueOf(p.getValue()));



		}

		return true;

	}

	@FXML public void setStatusLabetText(String message)
	{
		this.statusLabel.setText(message);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.columnCommand.setEditable(true);
		this.columnCommand.setResizable(true);


        columnCommand.setCellValueFactory(
      		    new PropertyValueFactory<CommandModel,String>("command")
      		);

        columnType.setCellValueFactory(
    		    new PropertyValueFactory<CommandModel,String>("type")
    		);

        columnEnabled.setCellValueFactory(
      		    new PropertyValueFactory<CommandModel,String>("active")
      		);

      /*  columnProperties.setCellValueFactory(
      		    new PropertyValueFactory<CommandModel,String>("properties")
      		);
		*/
        columnProperties.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties())));


	}




}
