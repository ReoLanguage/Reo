package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

//import static org.junit.Assert.*;
import org.junit.Test;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Automaton;
import nl.cwi.reo.semantics.automata.State;
import nl.cwi.reo.semantics.automata.Transition;
import nl.cwi.reo.semantics.portautomata.NullLabel;

public class AutomatonTest {

	@Test
	public void compose() {
		
		SortedSet<State> Q = new TreeSet<State>();
		SortedSet<Port> P = new TreeSet<Port>();
		P.add(new Port("a"));
		P.add(new Port("b"));
		Map<State, Set<Transition<NullLabel>>> T = new HashMap<State, Set<Transition<NullLabel>>>();
		State q0 = new State("q0");
		State q1 = new State("q1");
		Q.add(q0);
		Q.add(q1);
		Set<Transition<NullLabel>> out0 = new HashSet<Transition<NullLabel>>();
		Set<Transition<NullLabel>> out1 = new HashSet<Transition<NullLabel>>();
		TreeSet<Port> N0 = new TreeSet<Port>();
		TreeSet<Port> N1 = new TreeSet<Port>();
		N0.add(new Port("a"));
		N1.add(new Port("b"));
		Transition<NullLabel> t0 = new Transition<NullLabel>(q0, q1, N0, new NullLabel());
		Transition<NullLabel> t1 = new Transition<NullLabel>(q1, q0, N1, new NullLabel());
		out0.add(t0);
		out1.add(t1);
		T.put(q0, out0);
		T.put(q1, out1);
		
		Automaton<NullLabel> A = new Automaton<NullLabel>(Q, P, T, q0, new NullLabel());
		
		Map<Port, Port> links = new HashMap<Port, Port>();
		links.put(new Port("a"), new Port("c"));
		links.put(new Port("b"), new Port("d"));
		Automaton<NullLabel> B = A.rename(links);

//		new HashSet();
//		assertTrue(B.out.get(q0).iterator().next().getSyncConstraint(), );
		
		List<Automaton<NullLabel>> lst = new ArrayList<Automaton<NullLabel>>();
		lst.add(B);
		
		System.out.println(A.compose(lst));
		
		//System.out.println(A.compose(A));
		
		// Automaton.outputDOT(A, "A") 
		// to render the file, run:
		// dot -Tps A.dot -o A.ps
	}
	

//	public void GameGraph() {
//		
//		WorkAutomaton A = new WorkAutomaton();
//		A.addTransition("0; 1; ; x==2");
//		A.addTransition("1; 2; ; x==2");
//		A.addTransition("1; 0; a; x<=2");
//		A.addTransition("2; 1; a; x<=2");
//		WorkAutomaton B = new WorkAutomaton();
//		B.addTransition("0; 1; a; y==1 & z==0");
//		B.addTransition("1; 0; ; y==0 & z==3");
//
//		//WorkAutomaton P = WorkAutomaton.product(A, B);
//		//A.outputDOT("A");
//		//B.outputDOT("B");
//		//P.outputDOT("P");
//
//		WorkAutomaton C = new WorkAutomaton();
//		C.addTransition("0; 0; ; x==1&y<=1");
//		C.addTransition("0; 0; ; x==5&y==0");
//		C.addTransition("0; 0; a; x==0&y==1");
//		WorkAutomaton.outputDOT(C, "C");
//
//		GameGraph G = new GameGraph(C, "a");
//		G.synthesize();
//
//		GameGraph.outputDOT(G, "schedulinggame");
//
//		// presentation
//		WorkAutomaton X = new WorkAutomaton();
//		X.addTransition("0; 0; a; x==5");
//		X.addTransition("0; 0; ; x==1");
//		WorkAutomaton Y = new WorkAutomaton();
//		Y.addTransition("0; 1; a; true");
//		Y.addTransition("1; 0; b; true");
//		WorkAutomaton Z = new WorkAutomaton();
//		Z.addTransition("0; 1; b; y==2");
//		Z.addTransition("1; 0; ; y==7");
//
//		WorkAutomaton product = WorkAutomaton.product(X, Y, Z);
//		WorkAutomaton.outputDOT(product, "product");
//	}

}
