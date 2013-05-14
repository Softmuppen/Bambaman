package network;

public enum NetworkCommands {

	ACCEPTED(0), QUIT(1);
	
	int code;
	
	private NetworkCommands(int c)
	{
		code = c;
	}
	
	
}
