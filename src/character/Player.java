package character;

import java.io.Serializable;

public class Player{

	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private String name;
	
	public Player()
	{
		x = 50;
		y = 50;
		name = "UNKOWN";
	}
	
	public Player(String name)
	{
		x = 50;
		y = 50;
		this.name = name;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public String getName()
	{
		return name;
	}
}
