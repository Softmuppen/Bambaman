package Character;

import Utility.Entity;

public class Player extends Entity {

	private static final long serialVersionUID = 8L;
	private String name;
	private String direction;
	private int health;
	private int dx;
	private int dy;

	public Player()
	{
		super();
		;
	}

	public Player(String name, int width, int height)
	{
		super(0, 50, 50, width, height);  
		this.name = name;
		health = 100;
		direction = "right";
		dx = dy = 0;
	}

	public void setHealth(int health){
		this.health = health;
	}
	public int getHealth(){
		return health;
	}
	public void move()
	{
		// Move character
		setY(getY()+getDy());
		setX(getX()+getDx());

		// Set the current direction
		if(getDx() > 0){
			setDirection("right");
		}else if(getDx() < 0) {
			setDirection("left");
		}

		if(getDy() > 0){
			setDirection("down");
		}else if(getDy() < 0){
			setDirection("up");
		}
	}
	/**
	 * Moves the player and updates its attacking status.
	 */
	public void update(){
		move();
	}
	/**
	 * Returns the latest direction of this character.
	 * @return direction The current direction this Character is facing.
	 */
	public String getDirection()
	{
		return direction;
	}
	/**
	 * Sets the current direction of this character
	 * @param arg The new value for direction.
	 */
	public void setDirection(String arg)
	{
		direction = arg;
	} 
	public void setDx(int dx)
	{
		this.dx = dx;
	}
	public void setDy(int dy)
	{
		this.dy = dy;
	}
	/**
	 * Returns the current argument for dx.
	 * @return Argument for dx.
	 */
	public int getDx()
	{
		return dx;
	}
	/**
	 * Returns the current argument for dy.
	 * @return Argument for dy.
	 */
	public int getDy()
	{
		return dy;
	}
	/**
	 * Reset direction of this Character.
	 */
	public void resetDirection()
	{
		dx = 0;
		dy = 0;
	}
	/**
	 * Equals-method used to compare two characters.
	 * @return Boolean showing if the two characters were the same or not.
	 */
	@Override
	public boolean equals(Object obj)
	{
		/*if( this.getY() == ((Character) obj).getY() && this.getX() == ((Character) obj).getX() ){
			   return(true);
		   }*/

		if( this.getName() == ((Player)obj).getName()){
			return true;
		}

		return(false);
	}
	/**
	 * Returns the name of this Character.
	 * @return The name of this Character.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Sets the character name
	 */
	public void setName(String name){
		this.name = name;
	}
	@Override public String toString() {
		return name;
	}
}
