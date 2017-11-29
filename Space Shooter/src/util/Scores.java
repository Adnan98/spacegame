package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Scores {

	String file;
	Object[] scores;
/*
 * Denna klass anv�nds g�r att l�sa och mata in h�gsta po�ngen i filen highscores.txt
 */
	public Scores(){
		file = "scores.txt"; 
	}

	@SuppressWarnings("unchecked")
	public Object[] loadScores(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
				
		try { 
			//Denna metod l�ser in highscore fr�n filen och namnet och po�ngen som lokala variabler
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				String[] tokens = strLine.split(",");
				//puts the read name and the value in the hash Map
				map.put(tokens[0], Integer.parseInt(tokens[1]));
			} 

			br.close(); 
			
			//Now we need to sort the scores desc order
			Object[] obj = map.entrySet().toArray();
			
			Arrays.sort(obj, new Comparator() {
			    public int compare(Object o1, Object o2) {
			        return ((Map.Entry<String, Integer>) o2).getValue()
			                   .compareTo(((Map.Entry<String, Integer>) o1).getValue());
			    }
			});
			
			scores = obj;
			return obj;
		} 

		catch(FileNotFoundException e1) {  //undantag som kan intr�ffa vid l�sning 
			System.out.println("Filen hittades inte, var god f�rs�k igen"); 
		}

		catch(IOException e2) {   //undantag som kan intr�ffa vid l�sning 
			System.out.println(e2); 

		}
		return null; 
	}
	
	
	public void saveScore(String name, int score){

		try{ 
			//Mata in highscore i filen
			FileWriter w = new FileWriter(file, true); //true parameter indicates appending to file
			BufferedWriter bw = new BufferedWriter(w); 
			PrintWriter pw = new PrintWriter(bw); 
			pw.println(name+","+ score);//writes in the highcore in the file
			pw.close(); 
		} 

		catch(IOException e){ 
			System.out.println("Ett fel uppstod, var god och f�rs�k igen."); 
		} 
	}

}
