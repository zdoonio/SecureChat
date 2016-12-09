package com.standard;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ServerGUI extends JFrame implements ActionListener {
	
	private JButton register, exit, keygen;
	//private JComboBox securityChooser;
	private BufferedImage img;
	private JLabel lname;
	private UserRegGUI usergui;

	/**
	 * 
	 */
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
		keygen = new JButton ("Key Generation");
		register.setBounds(140, 250, 170, 20);
		exit.setBounds(140, 310, 170, 20);
		keygen.setBounds(140, 280, 170, 20);
		add(register);
		add(exit);
		add(keygen);
		register.addActionListener(this);
		exit.addActionListener(this);
		keygen.addActionListener(this);
		
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
			
			if(o == exit){
				System.exit(0);
				CloseFrame();
				
			//ServerGUI server = new ServerGUI(1500);
			return;
			}
		
		
	}

	
}
