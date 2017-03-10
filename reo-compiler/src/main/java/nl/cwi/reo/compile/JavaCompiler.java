package nl.cwi.reo.compile;

import java.util.SortedSet;
import java.util.TreeSet;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.automata.State;
import nl.cwi.reo.semantics.automata.Transition;
import nl.cwi.reo.semantics.portautomata.NullLabel;

public class JavaCompiler {
	
	public static void compile() {
		
		STGroup group = new STGroupFile("Automaton.stg");
        ST temp = group.getInstanceOf("transition");

        State q0 = new State("q0");
        State q1 = new State("q1");
        SortedSet<Port> N = new TreeSet<Port>();
        
        N.add(new Port("a", PortType.IN, PrioType.NONE, new TypeTag("Integer"), true));
        N.add(new Port("b", PortType.OUT, PrioType.NONE, new TypeTag("Boolean"), true));
        N.add(new Port("c", PortType.IN, PrioType.NONE, new TypeTag("Integer"), true));

        Transition<NullLabel> t = new Transition<NullLabel>(q0, q1, N, new NullLabel());
         
        temp.add("t", t);
        
        System.out.println(temp.render());
		//outputClass(name, st.render());		
	}
	
//	/**
//	 * Produces java source file containing the game graph;
//	 * @param name			name of the java class
//	 * @param code			implementation of the java class
//	 * @return <code>true</code> if the file is successfully written.
//	 */
//	private static boolean outputClass(String name, String code) {
//		try {
//			FileWriter out = new FileWriter(name + ".java");
//			out.write(code);
//			out.close();
//		} catch (IOException e) {
//			return false;
//		}
//		return true;
//	}
}
