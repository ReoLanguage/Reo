//package unfixedSwitch;

public class Pair {
	
	
	public static String Merge(String data, String table){
        if (data == null)
            throw new NullPointerException();
        if (table == null)
        	throw new NullPointerException();
        if (!(data instanceof String))
	        throw new IllegalArgumentException();
	    if (!(table instanceof String))
            throw new IllegalArgumentException();
		
	    
	    String str = data + "&&" + table;// disabled by //
		return str;
	}
}
