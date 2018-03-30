package server.model;

import java.io.Serializable;

public class CommandProperty implements Serializable{

	private int commId;
	private String name;
	private int val;

	public String getName()
	{
		return this.name;
	}
	public int getValue()
	{
		return this.val;
	}

	///SET///

	public void setName(String name)
	{
		this.name=name;
	}
	public void setValue(int value)
	{
		this.val=value;
	}

	public int getId() {
		return commId;
	}

	public CommandProperty(int i, String n, int v)
	{
		this.commId=i;
		this.name=n;
		this.val=v;
	}
	public String toString()
	{
		return "name:"+this.getName()+"; val:"+this.getValue();
	}


}
