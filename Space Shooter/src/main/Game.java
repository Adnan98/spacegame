package main;

import javax.swing.JFrame;
public class Game {
	
	public static void main(String[] args){
		//This is the window itself in which the game is run
		JFrame window = new JFrame();		
		window.setContentPane( new Panel());//Sätter objektet Panel som innehållet i fönstret. 
											//På så sätt kan man justera fönstrets storlek i klassen Panel 
		window.setTitle("SPACE SHOOTER");
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);//This prevents showing of the exit and minimize buttons and the default OS topbar is hidden
		window.pack();//Size the window so that all its contents are at or above their preferred sizes. (necessary when not using setSize())
		window.setVisible(true); 
		window.setLocation(0,0);
	}

}
