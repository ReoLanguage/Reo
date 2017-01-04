package nl.cwi.reo;
import java.util.List;

import nl.cwi.reo.interpret.Interpreter;
import nl.cwi.reo.workautomata.WorkAutomaton;

/**
 * Test class for debugging.
 */
public class Debug {

	public static void main(String[] args) {
		
		if (args.length > 0) {
			Interpreter<WorkAutomaton> interpreter = new Interpreter<WorkAutomaton>(new WorkAutomaton());
			
			List<WorkAutomaton> program = interpreter.getProgram(args[0]);
			
			System.out.println("------------------------List of workautomata-------------------");
			for (WorkAutomaton X : program) {
				System.out.println("\n" + X);				
			}
		}
	}
}

