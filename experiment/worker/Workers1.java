import java.io.IOException;
import java.io.PrintWriter;

import nl.cwi.reo.runtime.java.*;

public class Workers {

	static int N=1000;
	static int k=4;
	
	public static void producer(Output<String> a) {
		Long t1 = System.nanoTime();
		while((System.nanoTime()-t1)/1000000000 < 10){
			for (int i = 0; i < N; i++)
				a.put("d" + i);
		}

		for (int i = 0; i < N; i++)
			a.put("d" + i);
	}
	
	public static void consumer(Input<String> a) {
		Long t1 = System.nanoTime();
		while((System.nanoTime()-t1)/1000000000 < 10){
			for (int i = 0; i < k*N; i++){
				a.get();
			}
		}

		Long t2 = System.nanoTime();
		for (int i = 0; i < k*N; i++){
			a.get();
		}
		Long t3 = System.nanoTime();
                Long t = (t3-t2) ;
		try{
    			PrintWriter writer = new PrintWriter(
							"result_execution.txt"
								, "UTF-8");
    			writer.println("time for N put and k*N get :"+t);
    			writer.close();
		} catch (IOException e) { // do something  
		}
		System.out.println(" done ");
		System.exit(0);
		
	}
}
