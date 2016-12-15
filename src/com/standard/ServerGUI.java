package com.standard;


import javax.imageio.ImageIO;
import javax.swing.*;

import com.db.DbAddTable;
import com.db.DbConnection;
import com.db.DbCreate;
import com.db.DbDrop;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;


public class ServerGUI extends JFrame implements ActionListener {
	
	/*-----------------------------------------------------*/
	//													   //
	//				CREATED BY DOMINIK ZEDD				   //
	//					Copyright © 2016				   //
	//													   //
	/*-----------------------------------------------------*/
	
	private JButton register, exit, serverstatus, serverclose;
	//private JComboBox securityChooser;
	private BufferedImage img;
	private JLabel lname,lserverstatus,lstatus;
	private UserRegGUI usergui;
	private static final long serialVersionUID = 1L;
	

	public ServerGUI() throws IOException {
		super("Secured Chat Server v0.3");
		//WINDOW INIT
		setSize(450,400);
		setName("Secured Chat Server v0.3");
		setLayout(null);
		setResizable(false);
		//BUTTONS INIT
		register = new JButton ("User Registration");
		exit = new JButton ("EXIT");
		serverstatus = new JButton ("Start the Server");
		serverclose = new JButton("Stop the Server");
		register.setBounds(50, 270, 170, 20);
		serverclose.setBounds(50, 330, 170, 20);
		exit.setBounds(50, 360, 170, 20);
		serverstatus.setBounds(50, 300, 170, 20);
		add(register);
		add(exit);
		add(serverstatus);
		add(serverclose);
		register.addActionListener(this);
		exit.addActionListener(this);
		serverstatus.addActionListener(this);
		serverclose.addActionListener(this);
		
		try{
		img = ImageIO.read(new File("img/2.jpg"));
		}
		
		catch(IOException e){
			e.printStackTrace();
		}
		
		//label
		lname = new JLabel("Authentication Server");
		lname.setBounds(140,10,300,20);
		add(lname);
		lserverstatus = new JLabel("Server Status:");
		lserverstatus.setBounds(270,300,300,20);
		add(lserverstatus);
		lstatus = new JLabel("OFF");
		lstatus.setForeground(Color.red);
		lstatus.setBounds(400,300,300,20);
		add(lstatus);
		
		
		/*
		try{
			
		}
		
		catch (IOException e){
			e.printStackTrace();
		} */
		
		/*//CHOOSER INIT
		securityChooser = new JComboBox();
		securityChooser.setBounds(20, 80, 150, 20);
		securityChooser.addItem("RSA");
		securityChooser.addItem("Diffie-Helman");
		securityChooser.addItem("Merkle Puzzel's");
		securityChooser.addItem("TTP");
		securityChooser.addItem("PreDistributed");
		add(securityChooser);
		securityChooser.addActionListener(this);*/
	}
	
	public static void main(String[] args) throws IOException {
		//WINDOW OPEN
		AppMainGUI mainWin  = new AppMainGUI();
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.setVisible(true);
		
	}
	
	public void CloseFrame(){
	    super.dispose();
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(img, 0, 50, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(new Date());
			Object o = e.getSource();
			
			if(o == register ){
			//String host = "localhost";
			//ClientGUI client = new ClientGUI(host ,1500);
				try {
					usergui = new UserRegGUI();
					usergui.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			return;
			}
			
			if(o == serverstatus){
				lstatus.setForeground(Color.green);
				lstatus.setText("ON");
				DbCreate.main(null);
                                DbConnection.main(null);
				DbAddTable table = new DbAddTable();
				table.main(null);
				try {
					Server.main(null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			if(o == serverclose){
				lstatus.setForeground(Color.red);
				lstatus.setText("OFF");
                                DbConnection.main(null);
				DbDrop.main(null);
				
				Server.setLog(Server.out);
				PrintWriter serverLog = null;
				try {
					serverLog = new PrintWriter("log/server.log");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        serverLog.println(Server.out);
		        serverLog.close();
			}
			
			if(o == exit){
				System.exit(0);
				CloseFrame();
				
			//ServerGUI server = new ServerGUI(1500);
			return;
			}
		
		
	}

	
}
