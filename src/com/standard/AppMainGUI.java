package com.standard;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*-----------------------------------------------------*/
//													   //
//				CREATED BY DOMINIK ZEDD				   //
//					Copyright © 2016				   //
//													   //
/*-----------------------------------------------------*/
public class AppMainGUI extends JFrame implements ActionListener {
	
	private JButton avserver, exit;
	//private JComboBox securityChooser;
	private BufferedImage img;
	private JLabel lname;
	private ServerGUI serv;
	/**
	 * 
	 */
	private static final long serialVersionUID = 188889L;
	

	public AppMainGUI() throws IOException {
		super("Secured Chat Server v0.3");
		//WINDOW INIT
		setSize(270,350);
		setName("Secured Chat Server v0.3");
		setLayout(null);
		setResizable(false);
		//BUTTONS INIT
		avserver = new JButton ("AV Server");
		exit = new JButton ("EXIT");
		avserver.setBounds(60, 250, 150, 20);
		exit.setBounds(60, 280, 150, 20);
		add(avserver);
		add(exit);
		avserver.addActionListener(this);
		exit.addActionListener(this);
		
		try{
		img = ImageIO.read(new File("img/1.jpg"));
		}
		
		catch(IOException e){
			e.printStackTrace();
		}
		
		//label
		lname = new JLabel("APP");
		lname.setBounds(120,10,150,20);
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
			
			if(o == avserver ){
			//String host = "localhost";
			//ClientGUI client = new ClientGUI(host ,1500);
			try {
				serv = new ServerGUI();
				serv.setVisible(true);
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
