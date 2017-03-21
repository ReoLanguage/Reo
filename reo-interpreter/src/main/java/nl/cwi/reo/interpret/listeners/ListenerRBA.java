package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.interpret.ReoParser.Pr_paramContext;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.PrContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
import nl.cwi.reo.util.Monitor;

public class ListenerRBA extends Listener<PRAutomaton> {

	private ParseTreeProperty<PRAutomaton> primitives = new ParseTreeProperty<PRAutomaton>();
	private ParseTreeProperty<Value> params = new ParseTreeProperty<Value>();

	public ListenerRBA(Monitor m) {
		super(m);
	}

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, primitives.get(ctx.pr()));
	}

	public void enterPr(PrContext ctx) {

	}

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

	public void exitPr_param(Pr_paramContext ctx) {
		if(ctx.ID()!=null){
			params.put(ctx, new StringValue(ctx.ID().toString()));			
		}
		if(ctx.NAT()!=null){
			params.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().toString())));			
		}
		if(ctx.STRING()!=null){
			params.put(ctx, new StringValue(ctx.STRING().toString().replaceAll("\"", "")));			
		}
		
	}

}
