package server.model;

public class CommandProperty {

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

	public int getId() {
		return commId;
	}

	public CommandProperty(int i, String n, int v)
	{
		this.commId=i;
		this.name=n;
		this.val=v;
	}


}
