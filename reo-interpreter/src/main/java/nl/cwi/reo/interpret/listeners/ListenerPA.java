package nl.cwi.reo.interpret.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.interpret.PAParser.PaContext;
import nl.cwi.reo.interpret.PAParser.Pa_initContext;
import nl.cwi.reo.interpret.PAParser.Pa_scContext;
import nl.cwi.reo.interpret.PAParser.Pa_trContext;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.State;
import nl.cwi.reo.semantics.automata.Transition;
import nl.cwi.reo.semantics.portautomata.NullLabel;
import nl.cwi.reo.semantics.portautomata.PortAutomaton;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Listens to events triggered by a
 * {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 */
public class ListenerPA extends Listener<PortAutomaton> {

	/** The automata. */
	private ParseTreeProperty<PortAutomaton> automata = new ParseTreeProperty<PortAutomaton>();

	/** The scs. */
	private ParseTreeProperty<SortedSet<Port>> scs = new ParseTreeProperty<SortedSet<Port>>();

	/** The transitions. */
	private ParseTreeProperty<Transition<NullLabel>> transitions = new ParseTreeProperty<Transition<NullLabel>>();

	/** The initial. */
	private ParseTreeProperty<State> initial = new ParseTreeProperty<State>();

	/**
	 * Instantiates a new listener PA.
	 *
	 * @param m
	 *            the m
	 */
	public ListenerPA(Monitor m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, automata.get(ctx.pa()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitPa(nl.cwi.reo.interpret.
	 * ReoParser.PaContext)
	 */
	public void exitPa(PaContext ctx) {
		SortedSet<State> Q = new TreeSet<State>();
		SortedSet<Port> P = new TreeSet<Port>();
		Map<State, Set<Transition<NullLabel>>> T = new HashMap<State, Set<Transition<NullLabel>>>();
		State q0 = initial.get(ctx.pa_init());
		for (Pa_trContext pa_tr_ctx : ctx.pa_tr()) {
			Transition<NullLabel> t = transitions.get(pa_tr_ctx);
			if (q0 == null)
				q0 = t.getSource();
			Q.add(t.getSource());
			Q.add(t.getTarget());
			P.addAll(t.getSyncConstraint());
			Set<Transition<NullLabel>> outs = T.get(t.getSource());
			if (outs == null)
				outs = new HashSet<Transition<NullLabel>>();
			outs.add(t);
			T.put(t.getSource(), outs);
			T.putIfAbsent(t.getTarget(), new HashSet<Transition<NullLabel>>());
		}
		automata.put(ctx, new PortAutomaton(Q, P, T, q0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitPa_sc(nl.cwi.reo.interpret.
	 * ReoParser.Pa_scContext)
	 */
	public void exitPa_sc(Pa_scContext ctx) {
		SortedSet<Port> sc = new TreeSet<Port>();
		for (TerminalNode id : ctx.ID())
			sc.add(new Port(id.getText()));
		scs.put(ctx, sc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitPa_tr(nl.cwi.reo.interpret.
	 * ReoParser.Pa_trContext)
	 */
	public void exitPa_tr(Pa_trContext ctx) {
		State q1 = new State(ctx.ID(0).getText());
		State q2 = new State(ctx.ID(1).getText());
		SortedSet<Port> sc = scs.get(ctx.pa_sc());
		transitions.put(ctx, new Transition<NullLabel>(q1, q2, sc, new NullLabel()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitPa_init(nl.cwi.reo.interpret.
	 * ReoParser.Pa_initContext)
	 */
	public void exitPa_init(Pa_initContext ctx) {
		initial.put(ctx, new State(ctx.ID().getText()));
	}
}