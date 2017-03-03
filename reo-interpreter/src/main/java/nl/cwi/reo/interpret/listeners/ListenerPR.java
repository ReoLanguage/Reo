package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.interpret.ReoParser.Pr_paramContext;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.PrContext;
import nl.cwi.reo.interpret.ReoParser.Pr_portContext;
import nl.cwi.reo.interpret.ReoParser.Pr_stringContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;
import nl.cwi.reo.util.Monitor;

public class ListenerPR extends Listener<PRAutomaton> {

	private ParseTreeProperty<PRAutomaton> prAutomata = new ParseTreeProperty<PRAutomaton>();
	private ParseTreeProperty<String> name = new ParseTreeProperty<String>();
	private ParseTreeProperty<Value> parameter = new ParseTreeProperty<Value>();
	private ParseTreeProperty<List<Port>> port = new ParseTreeProperty<List<Port>>();

	public ListenerPR(Monitor m) {
		super(m);
	}

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, prAutomata.get(ctx.pr()));
	}

	public void enterPr(PrContext ctx) {

	}

	public void exitPr(PrContext ctx) {
		// If you encounter an error, put a message in the monitor:
		// m.add("test error message");
		System.out.println(name.get(ctx.pr_string()));

		if(Objects.equals(name.get(ctx.pr_string()),"identity"))
			prAutomata.put(ctx, new PRAutomaton(name.get(ctx.pr_string()),null,port.get(ctx.pr_port())));			
		else{
			if(ctx.pr_param()!=null)
				prAutomata.put(ctx, new PRAutomaton(name.get(ctx.pr_string()),parameter.get(ctx.pr_param()),port.get(ctx.pr_port())));
			else
				prAutomata.put(ctx, new PRAutomaton(name.get(ctx.pr_string()),null,port.get(ctx.pr_port())));
			
		}
	}

	public void exitPr_param(Pr_paramContext ctx) {
		if(ctx.ID()!=null){
			parameter.put(ctx, new StringValue(ctx.ID().toString()));			
		}
		if(ctx.NAT()!=null){
			parameter.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().toString())));			
		}
		
	}

	public void exitPr_string(Pr_stringContext ctx) {
		name.put(ctx, ctx.ID().getText());
	}

	public void exitPr_port(Pr_portContext ctx) {
		List<Port> p = new ArrayList<Port>();
		for (TerminalNode id : ctx.ID())
			p.add(new Port(id.getText()));
		port.put(ctx, p);
	};

	public void enterPr_port(Pr_portContext ctx) {

	};

}
