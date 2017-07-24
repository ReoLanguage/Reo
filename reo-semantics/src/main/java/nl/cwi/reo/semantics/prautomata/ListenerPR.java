package nl.cwi.reo.semantics.prautomata;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.interpret.ReoParser.Pr_paramContext;
import nl.cwi.reo.interpret.listeners.Listener;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.PrContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ListenerPR.
 */
public class ListenerPR extends Listener<PRAutomaton> {

	/** The primitives. */
	private ParseTreeProperty<PRAutomaton> primitives = new ParseTreeProperty<PRAutomaton>();

	/** The params. */
	private ParseTreeProperty<Value> params = new ParseTreeProperty<Value>();

	/**
	 * Instantiates a new listener PR.
	 *
	 * @param m
	 *            the m
	 */
	public ListenerPR(Monitor m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, primitives.get(ctx.pr()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterPr(nl.cwi.reo.interpret.
	 * ReoParser.PrContext)
	 */
	public void enterPr(PrContext ctx) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitPr(nl.cwi.reo.interpret.
	 * ReoParser.PrContext)
	 */
	public void exitPr(PrContext ctx) {
		String name = ctx.pr_string().ID().getText();

		List<Port> ports = new ArrayList<Port>();
		for (TerminalNode id : ctx.pr_port().ID())
			ports.add(new Port(id.getText()));

		if (name.equals("identity")) {
			primitives.put(ctx, new PRAutomaton(name, null, ports));
		} else if (ctx.pr_param() != null) {
			primitives.put(ctx, new PRAutomaton(name, params.get(ctx.pr_param()), ports));
		} else {
			primitives.put(ctx, new PRAutomaton(name, null, ports));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitPr_param(nl.cwi.reo.interpret.
	 * ReoParser.Pr_paramContext)
	 */
	public void exitPr_param(Pr_paramContext ctx) {
		if (ctx.ID() != null) {
			params.put(ctx, new StringValue(ctx.ID().toString()));
		}
		if (ctx.NAT() != null) {
			params.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().toString())));
		}
		if (ctx.STRING() != null) {
			params.put(ctx, new StringValue(ctx.STRING().toString().replaceAll("\"", "")));
		}

	}

}