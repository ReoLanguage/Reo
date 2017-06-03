import java.io.IOException;
import java.io.PrintWriter;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class WorkersLykos {
	
	static int N=1000;
	static int k=4;

	public static void producer(OutputPort port) {
		Long t1 = System.nanoTime();
		while((System.nanoTime()-t1)/1000000000 < 30){
			port.putUninterruptibly("d");
		}

		for (int i = 0; i < 2*N; i++)
			port.putUninterruptibly("d" + i);
	}

	public static void consumer(InputPort port) {
                Long t1 = System.nanoTime();
                while((System.nanoTime()-t1)/1000000000 < 30){
			port.getUninterruptibly();
                }

                Long t2 = System.nanoTime();
                for (int i = 0; i < k*N; i++){
			port.getUninterruptibly();
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

