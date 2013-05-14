package gui;



import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import client.MainClient;

import character.Player;


@SuppressWarnings("serial")
public class Gameboard extends JPanel{
	
	private int width = 400, height = 400;
	private int tileSize = 80;
	private Player player;
	private MainClient client;

	public Gameboard(Player player, MainClient client)
	{
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		setVisible(true);
		this.player = player;
		this.client = client;
		
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		java.net.URL imageURL = getClass().getResource("/images/Tiles.jpg");
		if(imageURL != null)
		{
			Image img = new ImageIcon(imageURL).getImage();
			for(int x = 0; x < width; x+= tileSize)
			{
				for(int y = 0; y < height; y += tileSize)
				{
					g2d.drawImage(img, x, y, this);
				}
			}
		}
		
		java.net.URL playerURL = getClass().getResource("/images/Char.gif");
		if(imageURL != null)
		{
			Image img = new ImageIcon(playerURL).getImage();
			for(Player p : client.getPlayers())
			{
				g2d.drawImage(img, p.getX(), p.getY(), this);
			}
		}
	}
	
	
}

