package main;

import java.awt.Component;

import javax.swing.JFrame;
@SuppressWarnings("unused")
public class Game {
	
	public static void main(String[] args){
		//Denna klassen skapar sj�lva f�nstret d�r spelet k�rs i
		JFrame window = new JFrame();		
		window.setContentPane( new Panel());//S�tter objektet Panel som inneh�llet i f�nstret. 
											//P� s� s�tt kan man justera f�nstrets storlek i klassen Panel 
		window.setTitle("SPACE SHOOTER");
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);//Detta g�r att f�sntret visas i fullsk�rmsl�ge
		window.pack();
		window.setVisible(true); 
		window.setLocation(0,0);
	}

}
