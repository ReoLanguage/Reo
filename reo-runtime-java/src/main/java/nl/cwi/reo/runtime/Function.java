package nl.cwi.reo.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import nl.cwi.reo.runtime.Input;

public class Function {

	
	public static String filterSymbol(String a) {
		if(a == null)
			return null;
		if(a.contains("X"))
			return "X";
		else if(a.contains("O"))
			return "O";
		else
			return " ";
	}
	
	public static boolean containscross(String a) {
		if(a != null)
			return a.contains("X");
		return false;
	}
	public static boolean containscircle(String a) {
		if(a != null)
			return a.contains("O");
		else
			return false;
	}
	
	public static String getwinner(String a) {
		if(a == null)
			return null;
		String [] x = a.split(":");
		if(x.length!=9)
			return null;
		for(int i=0; i<2;i++){
			if(x[i] == x[i+1] && x[i+1] == x[i+2] && !x[0].equals(" "))
				return x[0] + " won the game.";
			if(x[i] == x[i+3] && x[i+3] == x[i+6] && !x[i].equals(" "))
				return x[0] + " won the game.";
		}
		if(x[0] == x[4] && x[4] == x[9] && !x[0].equals(" "))
			return x[0] + " won the game.";
		if(x[2] == x[4] && x[4] == x[6] && !x[2].equals(" "))
			return x[0] + " won the game.";
		else
			return null;
	}	
	
	public static boolean isSymbolAllowed(String a, int i) {
		String[] x = new String[] { "a", "b", "c"};
		String[] y = new String[] { "1", "2", "3"};
		int num = (i-1)/3>0?(i-1)/3:0;
		String prefixe = x[num]+y[(i+2)%3];
		if(a == null)
			return false;
		if(a.length()!= 3 )
			return false;
		if(a.substring(0,2).equals(prefixe))
			return true;
		else
			return false;
	}
	
	public static String concatenate(Object object1, Object object2) {
		if (object1 == null)
			throw new NullPointerException();
		if (object2 == null)
			throw new NullPointerException();


		String string = (String)object1 + (String) object2;
		return string;
	}
	
	public static String formatting(Object object1, Object object2) {
		if (object1 == null)
			throw new NullPointerException();
		if (object2 == null)
			throw new NullPointerException();


		String string = (String)object1 + ":" + (String) object2;
		return string;
	}

}
