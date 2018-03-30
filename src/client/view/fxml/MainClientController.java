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
import javafx.scene.input.KeyCode;
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
	public static CommandModel observableCommand;
	public static List<CommandProperty> loadedProperties;
	public static CommandProperty observableProperty;

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

		if(inputTerminal.getText().isEmpty())
			{
			System.out.println("Input-terminal field is empty! Aborting.");
			return false;
			}
		try
		{
			outputTerminal.setText(ClientConnection.SendCommandString(inputTerminal.getText()));
			//outputTerminal.selectPositionCaret(outputTerminal.getLength());
			//outputTerminal.deselect(); //removes the highlighting
			outputTerminal.appendText("");

			inputTerminal.clear();

			return true;
		}catch(IOException e)
		{
			System.out.println("ClientlOntroller: ExecuteCommandFailed:"+e);
			return false;
		}
	}

	@FXML boolean descibeCommand()
	{
		try
		{
			int selectedIndex = this.tableCommands.getSelectionModel().getSelectedIndex();
			if(selectedIndex < 0)
			{
				return false;
			}else
			{
				observableCommand = (CommandModel) this.tableCommands.getItems().get(selectedIndex);
				loadedProperties = observableCommand.getPropertiesList();
				setStatusLabetText("Selected item:" + observableCommand.getCommand() + "ID:"+observableCommand.getId());
				if(observableCommand.getProperties().length > 0)
				{
					final ObservableList<CommandProperty> properties = FXCollections.observableArrayList();
					for(CommandProperty property : loadedProperties)
				    {
						properties.add(property);
						System.err.println("Property:" + property.getName());
						System.err.println("Value:" + property.getValue());

				    }
					tableProperties.setItems(properties);
				}

				this.fieldCommandContext.setText(observableCommand.getCommand());
				//(m.isActive())?this.checkBoxEnabled.setSelected(true);:this.checkBoxEnabled.setSelected(false);
				if(observableCommand.isActive())
					this.checkBoxEnabled.setSelected(true);
				else
					this.checkBoxEnabled.setSelected(false);

				rewriteObservableCommand();


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
			observableProperty=(CommandProperty) this.tableProperties.getItems().get(selectedIndex);
			setStatusLabetText("Selected property:"+observableProperty.getName()+"Value:"+observableProperty.getValue()+":"+selectedIndex);

			this.fieldPropertyName.setText(observableProperty.getName());
			this.fieldPropertyValue.setText(String.valueOf(observableProperty.getValue()));



		}

		return true;

	}

	@FXML
	public void rewriteObservableCommand()
	{
		//CommandModel tempCommand = new CommandModel(this.fieldCommandContext.getText(), t, null);
		setStatusLabetText(String.valueOf(observableCommand.getId()));
		if(this.fieldCommandContext.getText().compareTo(MainClientController.observableCommand.getCommand())!=0)
		{
			loadedCommands.get(observableCommand.getId()-1) .setCommandContext((this.fieldCommandContext.getText()));
		}
		if(this.checkBoxEnabled.isSelected() && observableCommand.isActive())
		{
			loadedCommands.get(observableCommand.getId()-1) .setActive(checkBoxEnabled.isSelected());
		}

		refreshCommandView();

	}

	@FXML
	public void rewriteObservableProperty()
	{

			List<CommandProperty>temp = loadedProperties;
			for(CommandProperty pr: temp)
			{

				if(pr.getName().compareTo(fieldPropertyName.getText())==0)
					if(pr.getValue()!=Integer.parseInt(fieldPropertyValue.getText()))
					{
						//pr.setValue(Integer.parseInt(fieldPropertyValue.getText()));
						//return;
						temp.add(new CommandProperty(observableCommand.getId(),
								fieldPropertyName.getText(),
								Integer.parseInt(fieldPropertyValue.getText())));
						return;

					}
				loadedProperties=temp;

			}
		}



	//}

	public void refreshCommandView()
	{
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
    	setStatusLabetText("UPDATED");


        tableCommands.refresh();
    	tableProperties.refresh();

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

		inputTerminal.setOnKeyPressed(e -> {
			 if (e.isAltDown()  &&  e.getCode()==KeyCode.ENTER)

			   {
				 	ExecuteTerminalCommand();
			   }
		}
		);


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
