package Networking;

import java.util.ArrayList;

import Character.Player;
import World.Map;

public class ServerPacket {
	public String message;
	public Map world;
	public ArrayList<Player> players;
	public Player clientPlayer;
}
