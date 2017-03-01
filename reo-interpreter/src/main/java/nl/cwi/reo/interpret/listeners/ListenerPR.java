package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.PrContext;
import nl.cwi.reo.interpret.ReoParser.Pr_portContext;
import nl.cwi.reo.interpret.ReoParser.Pr_stringContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.prautomata.PRAutomaton;

public class ListenerPR extends Listener<PRAutomaton> {

    private ParseTreeProperty<PRAutomaton> prAutomata =  new ParseTreeProperty<PRAutomaton>();
    private ParseTreeProperty<String> name =  new ParseTreeProperty<String>();
    private ParseTreeProperty<String> parameter =  new ParseTreeProperty<String>();
    private ParseTreeProperty<List<Port>> port =  new ParseTreeProperty<List<Port>>();
    Integer value;
    
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, prAutomata.get(ctx.pr()));
	}
    
	public void enterPr( PrContext ctx){
		
	}

	public void exitPr(PrContext ctx) {
		System.out.println(name.get(ctx.pr_string()));
		if(Objects.equals(name.get(ctx.pr_string()),"identity"))
			prAutomata.put(ctx, new PRAutomaton(name.get(ctx.pr_string()),new String(),new Integer(0),port.get(ctx.pr_port())));			
		else{
			if(ctx.param()!=null)
				parameter.put(ctx, ctx.param().getText());
			prAutomata.put(ctx, new PRAutomaton(name.get(ctx.pr_string()),parameter.get(ctx),value,port.get(ctx.pr_port())));
		}
	}

	public void enterPr_string(Pr_stringContext ctx) {
		
	}
	
	public void exitPr_string(Pr_stringContext ctx) {
		name.put(ctx, ctx.ID().getText());
	}

	public void exitPr_port(Pr_portContext ctx){
		List<Port> p = new ArrayList<Port>();
		for (TerminalNode id : ctx.ID())
			p.add(new Port(id.getText()));
		port.put(ctx, p);
	};

	public void enterPr_port(Pr_portContext ctx){
		
	};

}
