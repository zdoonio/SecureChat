package com.standard;



import com.db.DbAddUser;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*-----------------------------------------------------*/
//													   //
//				CREATED BY DOMINIK ZEDD				   //
//					Copyright © 2016				   //
//													   //
/*-----------------------------------------------------*/

public class UserRegGUI extends JFrame implements ActionListener {
	
    private JButton badd, bclear;
    private JLabel lnameapp, lname, lpw, lip, lport;
    private JTextField tname, tip, tport;
    private JPasswordField tpw;
    /**
     * 
     */
    private static final long serialVersionUID = 188889L;


    public UserRegGUI() throws IOException {
            
            //WINDOW INIT
            setSize(500,250);
            setName("USER REGISTRATION");
            setLayout(null);
            setResizable(false);
            
            //BUTTONS INIT
            badd = new JButton ("ADD");
            bclear = new JButton ("CLEAR");
            badd.setBounds(60, 180, 150, 20);
            bclear.setBounds(240, 180, 150, 20);
            add(badd);
            add(bclear);
            badd.addActionListener(this);
            bclear.addActionListener(this);

            //TEXTFIELD INIT
            tname = new JTextField("name");
            tname.setBounds(190, 60, 150, 20);
            add(tname);
            tpw = new JPasswordField("password");
            tpw.setBounds(190, 90, 150, 20);
            add(tpw);
            tip = new JTextField("ip");
            tip.setBounds(190, 120, 150, 20);
            //add(tip);
            tport = new JTextField("port");
            tport.setBounds(190, 150, 150, 20);
            //add(tport);



            //label
            lnameapp = new JLabel("User Registration");
            lnameapp.setBounds(190,10,150,20);
            add(lnameapp);
            lname = new JLabel("User Name");
            lname.setBounds(100,60,150,20);
            add(lname);
            lpw = new JLabel("Password");
            lpw.setBounds(100,90,150,20);
            add(lpw);
            lip = new JLabel("Ip Address");
            lip.setBounds(100,120,150,20);
           // add(lip);
            lport = new JLabel("Port");
            lport.setBounds(100,150,150,20);
           // add(lport);



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
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        if(o == badd){
            addUser();
        }

        if(o == bclear){
            clear();
        }

    }

    private void clear() {
        tpw.setText("");
        tname.setText("");
        tip.setText("");
        tport.setText("");
    }


    /**
     * 
     */
    private void addUser() {
        try {
            String name = tname.getText();
            char[] password = tpw.getPassword();
            if(name.equals("") | password.length <= 0)
                JOptionPane.showMessageDialog(this, "Password or username is empty");
            else {
                UserReg user = new UserReg(name, password);
                String[] args = new String[]{user.getUsername(), user.getSalt(), user.getUserkey()};
                DbAddUser.main(args);
                JOptionPane.showMessageDialog(this, "User added!");
                clear();
            }
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(UserRegGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "User already exists, choose different name!");    
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserRegGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Something went wrong, try again!");
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserRegGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Something went wrong, try again!");
        }
    }

	
}

