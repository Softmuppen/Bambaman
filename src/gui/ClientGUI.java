package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import network.NetworkCommands;

import client.MainClient;

import controller.KeyboardController;



@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements Runnable{

	private JTextField sendText;
	private JTextArea text;
	private JButton sendButton;
	private Gameboard gameboard;
	private MainClient client;
	
	public ClientGUI() 
	{
		String name = null;
		String ip = null;
		while(name == null && ip == null)
		{
			name = JOptionPane.showInputDialog("Enter username");
			ip = JOptionPane.showInputDialog("Enter IPAdress");
		}
		
		client = new MainClient(name, ip, this);
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt)
			{
				client.sendNetworkCommand(NetworkCommands.QUIT, client.getPlayer().getName());
				client.sendMessage("has left the game.");
				System.exit(0);
			}			
		});
		
		createCenterPanel();
		createSouthPanel();
		createMouseListener(this);
		
		setBounds(0, 0, 600, 600);
		setVisible(true);
		
		
	}
	
	private void createMouseListener(Component c)
	{
		c.setFocusable(true);
		c.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				arg0.getComponent().requestFocus();
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
		});
	}
	
	
	
	private void createCenterPanel()
	{
		JPanel panel = new JPanel();
		gameboard = new Gameboard(client.getPlayer(), client);
		gameboard.addKeyListener(new KeyboardController(client));
		createMouseListener(gameboard);
		panel.add(gameboard);
		add(panel, BorderLayout.CENTER);
	}
	
	private void createSouthPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridLayout(1,0));
		centerPanel.setSize(500, 500);
		panel.add(centerPanel, BorderLayout.CENTER);
		text = new JTextArea("", 5, 3);
		text.setEditable(false);
		JScrollPane scroll = new JScrollPane(text);
		scroll.setSize(500, 500);
		centerPanel.add(scroll);
		
		
		
		JPanel southPanel = new JPanel(new FlowLayout());
		panel.add(southPanel, BorderLayout.SOUTH);
		sendText = new JTextField("Enter data to send");
		sendText.setPreferredSize(new Dimension(200,30));
		southPanel.add(sendText);

		sendButton = new JButton("Send");		
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				client.sendMessage(sendText.getText());
				sendText.setText("");
			}
		});	
		southPanel.add(sendButton);
		
		add(panel, BorderLayout.SOUTH);
	}
	
	public void update(String msg)
	{
		text.append(msg + "\n");
	}
	
	public void run()
	{
		while(true)
		{
			if(client.isChanged())
			{
				client.setChanged(false);
				gameboard.repaint();
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

