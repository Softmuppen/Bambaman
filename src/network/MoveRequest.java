package network;

import character.Player;

public class MoveRequest {

	public Player player;
	public int dx;
	public int dy;
	
	public MoveRequest()
	{
		;
	}
	
	public MoveRequest(Player player, int dx, int dy)
	{
		this.player = player;
		this.dx = dx;
		this.dy = dy;
	}
}
