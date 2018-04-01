package server.model;

import java.sql.Date;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import server.controller.MainServerController;

public class CommandModel implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String command = new String();
	private int id;
	private boolean active;
	private int type;

	private  List<Integer> second=new ArrayList<Integer>();
	private  List<Integer> minute=new ArrayList<Integer>();
	private  List<Integer> hour=new ArrayList<Integer>();
	private  List<Integer> day=new ArrayList<Integer>();
	private  List<Integer> month=new ArrayList<Integer>();

	List<CommandProperty> command_properties = new ArrayList<CommandProperty>();



	///GET///

	public CommandProperty[] getProperties() {
		CommandProperty[] array = new CommandProperty[command_properties.size()];
		return command_properties.toArray(array);
	};
	public List<CommandProperty> getPropertiesList() {
		return command_properties;
	};


	public List<Integer> getSeconds()
	{
		return this.second;
	}
	public List<Integer> getMinutes()
	{
		return this.minute;
	}
	public List<Integer> getHours()
	{
		return this.hour;
	}
	public List<Integer> getMonths()
	{
		return this.month;
	}


	public String getCommand() {
		return command;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return this.type;
	}

	public boolean isActive() {
		return this.active;
	}

	///ADD///

	public void addProperty(CommandProperty prop) {
		command_properties.add(prop);
	}

	///SET///

	public void setCommandContext(String context)
	{
		this.command=context;
	}
	public void setActive(boolean active)
	{
		this.active=active;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public void setProperties(List<CommandProperty> props)
	{
		this.command_properties=props;
	}

	public void replaceCommandById(int id, CommandModel command)
	{

	}

	public CommandModel(String context, boolean active, List<CommandProperty> prop) {
		//this.id = id;
		this.command = context;
		this.active=active;
		for(CommandProperty item : prop)
		{
			switch(item.getName())
			{
			case "second":
				this.second.add(item.getValue());
				break;
			case "minute":
				this.minute.add(item.getValue());
				break;
			case "day":
				this.day.add(item.getValue());
				break;
			case "month":
				this.month.add(item.getValue());
				break;
			case "year":
				//this.year.add(item.getValue());
				break;
			}
		}
	}

	public CommandModel(int id, String context,  boolean active, int type) {
		this.id = id;
		this.command = context;
		this.active=active;
		this.type=type;
	}



	public boolean haveToExecute()
	{
		//System.out.println("command_properties:"+command_properties);
		//MainController.executeBashCommand("notify-send \"My name is bash and I rock da hous\"");
		//if(command_properties.size()<=0)return true;

		for(CommandProperty item : command_properties)
		{
		//	if (!this.active)return false;

			//System.out.println("ITEM:"+item.getName()+":"+item.getValue());
			//System.out.println("properties count:"+command_properties.size());
			int mydata = -1;

			switch(item.getName()){
				case "second":
					//second.add(item.getValue());
					mydata = LocalDateTime.now().getSecond();
					if(item.getValue()==-1) second.add( (LocalDateTime.now().getSecond() ) );
					else
						second.add(item.getValue());
					break;
				case "minute":
					//minute = item.getValue();

					//mydata = LocalDateTime.now().getMinute();
					if(item.getValue()==-1)
						minute.add( LocalDateTime.now().getMinute() );
					else
						minute.add(item.getValue());
					break;

				case "hour":
					//hour = item.getValue();
					//hour.add(item.getValue());
					//mydata = LocalDateTime.now().getHour();
					//if(item.getValue()==-1 && LocalDateTime.now().getMinute()==0 && LocalDateTime.now().getSecond()==0)return true;
					//if(item.getValue()==-1) hour.add( (LocalDateTime.now().getHour() ) );
					if(item.getValue()==-1)
						hour.add( LocalDateTime.now().getHour() );
					else
						hour.add(item.getValue());
					break;
				case "day":
					//day = item.getValue();
					//day.add(item.getValue());
					//mydata = LocalDateTime.now().getDayOfWeek().ordinal();
					//if(item.getValue()==-1 &&  LocalDateTime.now().getHour()==0 && LocalDateTime.now().getMinute()==0 && LocalDateTime.now().getSecond()==0)return true;
					//if(item.getValue()==-1) day.add( (LocalDateTime.now().getDayOfMonth() ) );
					if(item.getValue()==-1)
						day.add( LocalDateTime.now().getDayOfMonth() );
					else
						day.add(item.getValue());
					break;

				case "week":
					//mydata = LocalDateTime.now().getDayOfMonth();
					break;
				case "month":
					//month = item.getValue();
					//month.add(item.getValue());
					//mydata = LocalDateTime.now().getMonthValue();
					//if(item.getValue()==-1) month.add( (LocalDateTime.now().getMonthValue() ) );
					if(item.getValue()==-1)
						month.add( LocalDateTime.now().getMonthValue() );
					else
						month.add(item.getValue());
					break;
			}

			//if(item.getValue()==second || item.getValue()==minute)
			//if(item.getValue()==-1)return true;
			//if (mydata != item.getValue())
			//{
				//return false;
			//}


			if(second.contains(LocalDateTime.now().getSecond()) &&  minute.contains(LocalDateTime.now().getMinute())
				&& hour.contains(LocalDateTime.now().getHour()) && day.contains(LocalDateTime.now().getDayOfMonth())
				&& month.contains(LocalDateTime.now().getMonthValue()) )
			{
				//System.out.println("PROPS");
				System.out.println(LocalDateTime.now().toLocalDate()+" "+LocalDateTime.now().toLocalTime());
				//System.out.println("sec list:"+second);
				//System.out.println("min list:"+minute);
				//System.out.println("hour list:"+hour);
				//System.out.println("day list:"+day);
				//System.out.println("months list:"+month);
				return true;
			}
		//System.out.println("PROPS");
		//System.out.println("SEC:"+second);
		//System.out.println("min:"+minute);
		//System.out.println("h:"+hour);
		//System.out.println("d:"+day);
		//System.out.println("m:"+month);
		}
		//System.out.println(LocalDateTime.now().toLocalDate()+" "+LocalDateTime.now().toLocalTime());
		//System.out.println("sec list:"+second);
		//System.out.println("min list:"+minute);
		//System.out.println("hour list:"+hour);
		//System.out.println("day list:"+day);
		//System.out.println("months list:"+month);
		second.clear();
		minute.clear();
		hour.clear();
		day.clear();
		month.clear();

		/*STUB!*/return false;

	}

	public void Decrease() {
		// TODO Auto-generated method stub
	//	this.count--;

	}
	public void setId(int id) {
		this.id=id;

	}

//	public int getCount() {
		// TODO Auto-generated method stub
	//	return this.count;
	//}
}
