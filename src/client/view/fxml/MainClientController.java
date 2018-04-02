package client.view.fxml;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.main.ClientConnection;
import client.model.Property;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.Duration;
import server.model.CommandModel;
import server.model.CommandProperty;

public class MainClientController implements Initializable {

	@FXML TabPane mainTabPane;

	@FXML TableView tableCommands;
	@FXML TableColumn<CommandModel, String> columnId;
	@FXML TableColumn<CommandModel, String> columnCommand;
	@FXML TableColumn<CommandModel, String> columnType;
	@FXML TableColumn<CommandModel, String> columnEnabled;
	@FXML TableColumn<CommandModel, String> columnProperties;

	@FXML TableColumn<CommandModel, String> columnSecondsValue;
	@FXML TableColumn<CommandModel, String> columnMinutesValue;
	@FXML TableColumn<CommandModel, String> columnHoursValue;
	@FXML TableColumn<CommandModel, String> columnDaysValue;
	@FXML TableColumn<CommandModel, String> columnMonthsValue;


	@FXML TableView tableProperties;
	@FXML TableColumn<CommandProperty, String> columnPropertyName;
	@FXML TableColumn<CommandProperty, String> columnPropertyValue;


	@FXML Button buttonSave;
	@FXML Button buttonTerminalInput;

	@FXML TextArea inputTerminal;
	@FXML TextArea outputTerminal;
	@FXML TextArea infoOutputArea;

	@FXML TextField fieldPropertyName;
	@FXML TextField fieldPropertyValue;
	@FXML TextField fieldCommandContext;

	@FXML CheckBox checkBoxEnabled;

	@FXML Label dateTimeLabel;


//	@FXML Label statusLabel;

	public static List<CommandModel> loadedCommands;
	public static CommandModel observableCommand;

	public static List<CommandProperty> loadedProperties;
	public static CommandProperty observableProperty;

	public static List<Integer> deletedCommands =  new ArrayList<Integer>();
	private static ObservableList<CommandModel> commandList = FXCollections.observableArrayList();
	private static ObservableList<CommandProperty> propertiesList = FXCollections.observableArrayList();

	public static int observableCommandIndex=0;
	public static int observablePropertyIndex=0;

	private String getStringProperty(CommandProperty[] p, String propertyName) {
		if(p.length<=0) return "";
		String temp = "";
		for(CommandProperty item: p)
		{
			if(item!=null && item.getName().compareTo(propertyName)==0)
			{
				temp+=item.getValue()+",";
			}

		}
		return temp;
	}

	public void fxVerbose(String message)
	{
		this.infoOutputArea.appendText(message+"\n");
		//this.infoOutputArea.appendText("TEST");
	}


	@FXML
	void loadCommands() throws UnknownHostException, IOException
	{
		ClientConnection.connection();
		try
		{
			loadedCommands = ClientConnection.getCommands();
			if(deletedCommands!=null)
				{
					deletedCommands.clear();
				}
			RefreshDisplayValues();
			fxVerbose("Commands loaded.");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			fxVerbose("Failed to load commands.");
		}

	}
	@FXML
	void saveCommands() throws UnknownHostException, IOException
	{
		if(ClientConnection.sendCommands(loadedCommands) && ClientConnection.sendDeletedId(deletedCommands))
		{
			fxVerbose("Commands saved to server.");
		}else
		{
			fxVerbose("Failed to save commamds.");
		}

	}

	public void RefreshDisplayValues()
	{

		commandList.clear();
		//properties.clear();
		//loadedCommands.clear();
		 for(CommandModel model : loadedCommands)
         {
       	  commandList.add(model);
         	//System.err.println("Command:"+model.getCommand());
         //	System.err.println("Type:"+model.getType());
         	if(model.getProperties().length<=0)System.err.println("ZERO PROP LENGTH");

         	for(CommandProperty p : model.getProperties())
         	{
         		//System.err.println(p.getName()+":"+p.getValue());

         	}
         }
		 //Updating property for selected command

		 try{
			 System.out.println("UPDATING PROPERTY:trying...");
					if(loadedProperties.size() > 0)
					{
						propertiesList.clear();
						for(CommandProperty property : loadedProperties)
						{
							propertiesList.add(property);
							System.out.println("UPDATING PROPERTY");
						}
						tableProperties.setItems(propertiesList);
					}else
					{
						tableProperties.setItems(null);
						System.out.println("UPDATING PROPERTY: property=0");
					}
		 }
					catch(NullPointerException e)
					{
						System.out.println("UPDATING PROPERTY NULL POINTER EXCEPTION");
					}


		 if(commandList.size() <=0 )
		 {
			tableCommands.setItems(null);
		 }
		 else
		 {
			 tableCommands.setItems(commandList);
			 //statusLabel.setText("commandList:"+commandList.size()+"loadedCommands:"+loadedCommands.size());
			 //fxVerbose("commandList:"+commandList.size()+"loadedCommands:"+loadedCommands.size());
		 }




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
			observableCommandIndex = this.tableCommands.getSelectionModel().getSelectedIndex();
			if(observableCommandIndex < 0)
			{
				return false;
			}else
			{
				observableCommand = (CommandModel) this.tableCommands.getItems().get(observableCommandIndex);
				loadedProperties = observableCommand.getPropertiesList();
				//fxVerbose("Selected item:" + observableCommand.getCommand() + "ID:"+observableCommand.getId());
				if(observableCommand.getProperties().length > 0)
				{
					propertiesList.clear();
					//final ObservableList<CommandProperty> properties = FXCollections.observableArrayList();
					for(CommandProperty property : loadedProperties)
				    {
						propertiesList.add(property);
					//	System.err.println("Property:" + property.getName());
						//System.err.println("Value:" + property.getValue());

				    }
					tableProperties.setItems(propertiesList);
				}else
					tableProperties.setItems(null);


				this.fieldCommandContext.setText(observableCommand.getCommand());
				//(m.isActive())?this.checkBoxEnabled.setSelected(true);:this.checkBoxEnabled.setSelected(false);
				if(observableCommand.isActive())
					this.checkBoxEnabled.setSelected(true);
				else
					this.checkBoxEnabled.setSelected(false);

				//RefreshDisplayValues();


				return true;
			}
		}catch(IndexOutOfBoundsException e){
			return false;
		}
	}

	@FXML boolean describeProperty()
	{
		observablePropertyIndex = this.tableProperties.getSelectionModel().getSelectedIndex();
		if(observablePropertyIndex<0)
		{
			return false;
		}else
		{
			observableProperty=(CommandProperty) this.tableProperties.getItems().get(observablePropertyIndex);
			//fxVerbose("Selected property:"+observableProperty.getName()+"Value:"+observableProperty.getValue()+":"+observablePropertyIndex);

			this.fieldPropertyName.setText(observableProperty.getName());
			this.fieldPropertyValue.setText(String.valueOf(observableProperty.getValue()));

		}

		return true;

	}

	@FXML
	public void rewriteObservableCommand()
	{
		if(observableCommand == null || observableCommand.getId() < 0)
			{
			fxVerbose("Aborting rewriteOvservableCommand!");
			return;
			}
		//CommandModel tempCommand = new CommandModel(this.fieldCommandContext.getText(), t, null);
		//fxVerbose(String.valueOf(observableCommand.getId()));
		if(this.fieldCommandContext.getText().compareTo(observableCommand.getCommand())!=0
				|| this.checkBoxEnabled.isSelected()!=observableCommand.isActive())
		{
			for(CommandModel foredit : loadedCommands)
			{
				if(foredit.getId()==observableCommand.getId())
				{
					foredit.setActive(this.checkBoxEnabled.isSelected());
					foredit.setCommandContext(this.fieldCommandContext.getText());
					//descibeCommand();

					//foredit.setType();
				}

			}
			//fxVerbose("Observable command active:"+observableCommand.isActive());

		}

		RefreshDisplayValues();
		if(observableCommandIndex > loadedCommands.size())
		{
			tableCommands.getSelectionModel().selectLast();
		}else
		{
			tableCommands.getSelectionModel().select(observableCommandIndex);
		}

	}

	@FXML
	public void rewriteObservableProperty()
	{

			List<CommandProperty>temp = loadedProperties;
			CommandProperty tempProperty = new CommandProperty(
					observableCommand.getId(),
					fieldPropertyName.getText(),
					Integer.parseInt(fieldPropertyValue.getText()
							));

			for(CommandProperty pr: temp)
			{

				/*adding or rewriting existing property*/
				if(pr.getName().compareTo(tempProperty.getName())==0 && pr.getValue()==tempProperty.getValue())
				{
					//if(pr.getValue()==tempProperty.getValue())
					//{
						System.out.println("[CLIENT] Such property already exists. Aborting");
						return;
					//}
				}

				//Adding new property
				switch(tempProperty.getName())
				{
					case "second":
						System.out.println("TempPropertyValue=" + tempProperty.getValue());
						if(tempProperty.getValue() >=- 1 && tempProperty.getValue() <= 60)
						{
							tempProperty.setValue(Integer.parseInt( fieldPropertyValue.getText() ));
							System.out.println("[CLIENT] added new property:"+observableCommand.getId()+":"+fieldPropertyName.getText()+
									":"+fieldPropertyValue.getText());
						}else
						{
							System.out.println("[CLIENT] Wrong second value! Aborting.");
							return;
						}
							break;


					case "minute":
						System.out.println("TempPropertyValue=" + tempProperty.getValue());
						if(tempProperty.getValue() >=- 1 && tempProperty.getValue() <= 60)
						{

							tempProperty.setValue(Integer.parseInt( fieldPropertyValue.getText() ));
							System.out.println("[CLIENT] added new property:"+observableCommand.getId()+":"+fieldPropertyName.getText()+
									":"+fieldPropertyValue.getText());
						}else
						{
							System.out.println("[CLIENT] Wrong minute value! Aborting.");
							return;
						}
							break;

					case "hour":
						System.out.println("TempPropertyValue=" + tempProperty.getValue());
						if(tempProperty.getValue() >=- 1 && tempProperty.getValue() <= 60)
						{

							tempProperty.setValue(Integer.parseInt( fieldPropertyValue.getText() ));
							System.out.println("[CLIENT] added new property:"+observableCommand.getId()+":"+fieldPropertyName.getText()+
									":"+fieldPropertyValue.getText());
						}else
						{
							System.out.println("[CLIENT] Wrong hour value! Aborting.");
							return;
						}
							break;

					case "day":
						System.out.println("TempPropertyValue=" + tempProperty.getValue());
						if(tempProperty.getValue() >=- 1 && tempProperty.getValue() <= 31)
						{

							tempProperty.setValue(Integer.parseInt( fieldPropertyValue.getText() ));
							System.out.println("[CLIENT] added new property:"+observableCommand.getId()+":"+fieldPropertyName.getText()+
									":"+fieldPropertyValue.getText());
						}else
						{
							System.out.println("[CLIENT] Wrong day value! Aborting.");
							return;
						}
							break;

					case "month":
						System.out.println("TempPropertyValue=" + tempProperty.getValue());
						if(tempProperty.getValue() >=- 1 && tempProperty.getValue() <= 12)
						{

							tempProperty.setValue(Integer.parseInt( fieldPropertyValue.getText() ));
							System.out.println("[CLIENT] added new property:"+observableCommand.getId()+":"+fieldPropertyName.getText()+
									":"+fieldPropertyValue.getText());
						}else
						{
							System.out.println("[CLIENT] Wrong month value! Aborting.");
							return;
						}
							break;
					default:
						System.out.println("[CLIENT] Wrond property name "+ fieldPropertyName.getText() + ". Aborting. " );
						return;
				}



			}
			if(tempProperty!=null)
			{
				temp.add(tempProperty);
				loadedProperties=temp;
				System.out.println("[CLIENT] Property added.");
				RefreshDisplayValues();
				if(observablePropertyIndex < temp.size())
				{
					tableProperties.getSelectionModel().selectLast();
					//describeProperty();
				}else
				{
					//tableProperties.getSelectionModel().select(observablePropertyIndex);
					//describeProperty();
				}



			}

		}

	@FXML
	public void deleteProperty()
	{

		List<CommandProperty> temp = loadedProperties;
		CommandProperty tempProperty = new CommandProperty(
				observableCommand.getId(),
				fieldPropertyName.getText(),
				Integer.parseInt(fieldPropertyValue.getText()
						));

		for(CommandProperty pr: temp)
		{

			/*deleting existing property*/
			if(pr.getName().compareTo(tempProperty.getName())==0 && pr.getValue()==tempProperty.getValue())
			{
					temp.remove(pr);
					fxVerbose("Property deleted.");
					//System.out.println("[CLIENT] Property removed." + tempProperty.getName()+":" + tempProperty.getValue());
					break;
			}
		}
			loadedProperties = temp;
			RefreshDisplayValues();
			fxVerbose("observablePropertyIndex=" + observablePropertyIndex);
		//	tableCommands.getSelectionModel().selectLast();
			if(temp.size() > 0 && observablePropertyIndex < 0 )
			{
				tableProperties.getSelectionModel().selectLast();

			}else
			{
				tableProperties.getSelectionModel().select(observablePropertyIndex);

			}
			describeProperty();

	}

	@FXML public void deleteCommand()
	{
		List<CommandModel> temp = loadedCommands;
		//tableCommands.getSelectionModel().getSelectedItem();
		for(CommandModel m : temp)
		{
			if(m.getId()==observableCommand.getId())
			{
				deletedCommands.add(observableCommand.getId());
				temp.remove(m);
				fxVerbose("Command deleted.");
				break;
			}

		}
		loadedCommands = temp;
		RefreshDisplayValues();
		fxVerbose("observableCommandIndex=" + observableCommandIndex);
	//	tableCommands.getSelectionModel().selectLast();
		if(temp.size() > 0 && observableCommandIndex < 0 )
		{
			tableCommands.getSelectionModel().selectLast();

		}else
		{
			tableCommands.getSelectionModel().select(observableCommandIndex);

		}
		descibeCommand();

	}

	@FXML public void addCommand()
	{
		int maxId=0;
		for(CommandModel m : loadedCommands)
		{
			if(m.getId()>maxId)maxId=m.getId();
		}
		CommandModel tempCommand = new CommandModel(maxId+1, "New command", false, 1);

		loadedCommands.add(tempCommand);
		fxVerbose("Added comand with id "+tempCommand.getId()+", total="+loadedCommands.size());
		RefreshDisplayValues();
		tableCommands.getSelectionModel().selectLast();
		descibeCommand();

	}

	int i = 0;
	@FXML void testButtonAction()
	{
		fxVerbose("test button action...");
		DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
		final Timeline timeline = new Timeline(
		    new KeyFrame(
		        Duration.millis( 1000 ),
		        event -> {
		            dateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		        }
		    )
		);
		timeline.setCycleCount( Animation.INDEFINITE );
		timeline.play();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tableProperties.setPlaceholder(new Label("Empty"));
		tableCommands.setPlaceholder(new Label("Empty"));

		inputTerminal.setOnKeyPressed(e -> {
			 if (e.isAltDown()  &&  e.getCode()==KeyCode.ENTER)

			   {
				 	ExecuteTerminalCommand();
			   }
		}
		);

		DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
		final Timeline timeline = new Timeline(
		    new KeyFrame(
		        Duration.millis( 1000 ),
		        event -> {
		            dateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		        }
		    )
		);
		timeline.setCycleCount( Animation.INDEFINITE );
		timeline.play();


		columnId.setCellValueFactory( val -> new SimpleStringProperty(String.valueOf(val.getValue().getId())));




        columnCommand.setCellValueFactory(
      		    new PropertyValueFactory<CommandModel,String>("command")
      		);

        columnType.setCellValueFactory(
    		    new PropertyValueFactory<CommandModel,String>("type")
    		);

        columnEnabled.setCellValueFactory(
      		    new PropertyValueFactory<CommandModel,String>("active")
      		);

        columnPropertyName.setCellValueFactory(
    		    new PropertyValueFactory<CommandProperty,String>("name")
    		);
      columnPropertyValue.setCellValueFactory(
  		    new PropertyValueFactory<CommandProperty,String>("value")
  		);

       // columnProperties.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties())));

  	columnSecondsValue.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties(),"second")));
  	columnMinutesValue.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties(),"minute")));
  	columnHoursValue.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties(),"hour")));
  	columnDaysValue.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties(),"day")));
  	columnMonthsValue.setCellValueFactory(val -> new SimpleStringProperty(getStringProperty(val.getValue().getProperties(),"month")));

  	///Stubs. not working from FX builder...

  	//tableCommands.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);



  	columnSecondsValue.setResizable(true);
  	columnMinutesValue.setResizable(true);
  	columnHoursValue.setResizable(true);
  	columnDaysValue.setResizable(true);
  	columnMonthsValue.setResizable(true);

	//columnCommand.setEditable(true);
	columnCommand.setResizable(true);


	}




}
