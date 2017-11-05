package main;

import java.awt.Component;

import javax.swing.JFrame;
@SuppressWarnings("unused")
public class Game {
	
	public static void main(String[] args){
		//Denna klassen skapar själva fönstret där spelet körs i
		JFrame window = new JFrame();		
		window.setContentPane( new Panel());//Sätter objektet Panel som innehållet i fönstret. 
											//På så sätt kan man justera fönstrets storlek i klassen Panel 
		window.setTitle("SPACE SHOOTER");
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);//Detta gör att fösntret visas i fullskärmsläge
		window.pack();
		window.setVisible(true); 
		window.setLocation(0,0);
	}

}
