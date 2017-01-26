package nl.cwi.reo.interpret.listeners;

import nl.cwi.reo.prautomata.PRAutomaton;
import nl.cwi.reo.semantics.Port;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.PrContext;
import nl.cwi.reo.interpret.ReoParser.Pr_portContext;
import nl.cwi.reo.interpret.ReoParser.Pr_stringContext;
import nl.cwi.reo.interpret.listeners.Listener;

public class ListenerPR extends Listener<PRAutomaton> {

    private ParseTreeProperty<PRAutomaton> prAutomata =  new ParseTreeProperty<PRAutomaton>();
    private ParseTreeProperty<String> name =  new ParseTreeProperty<String>();
    private ParseTreeProperty<String> parameter =  new ParseTreeProperty<String>();
    private ParseTreeProperty<List<Port>> port =  new ParseTreeProperty<List<Port>>();
    Integer value;
    
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, prAutomata.get(ctx.pa()));
	}
    
	public void enterPr( PrContext ctx){
		
	}

	public void exitPr(PrContext ctx) {
		parameter.put(ctx, ctx.ID().getText());
		
		prAutomata.put(ctx, new PRAutomaton(name.get(ctx.pr_string()),parameter.get(ctx),value,port.get(ctx.pr_port())));
	}

	public void enterPr_string(Pr_stringContext ctx) {
		
	}
	
	public void exitPr_string(Pr_stringContext ctx) {
		name.put(ctx, ctx.ID().getText());
	}

	public void exitPr_port(Pr_portContext ctx){
		List<Port> p = new ArrayList<Port>();
		p.add(new Port(ctx.ID(0).getText()));
		p.add(new Port(ctx.ID(1).getText()));
		
		port.put(ctx, p);
	};

	public void enterPr_port(Pr_portContext ctx){
		
	};

}
