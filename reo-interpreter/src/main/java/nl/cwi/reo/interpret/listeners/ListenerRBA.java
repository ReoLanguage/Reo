package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.interpret.ReoParser.Pr_paramContext;
import nl.cwi.reo.interpret.ReoParser.RbaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_dcContext;
import nl.cwi.reo.interpret.ReoParser.Rba_dc_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Rba_dc_equalityContext;
import nl.cwi.reo.interpret.ReoParser.Rba_defContext;
import nl.cwi.reo.interpret.ReoParser.Rba_dtContext;
import nl.cwi.reo.interpret.ReoParser.Rba_excluded_portContext;
import nl.cwi.reo.interpret.ReoParser.Rba_included_portContext;
import nl.cwi.reo.interpret.ReoParser.Rba_portContext;
import nl.cwi.reo.interpret.ReoParser.Rba_scContext;
import nl.cwi.reo.interpret.ReoParser.Rba_termContext;
import nl.cwi.reo.interpret.ReoParser.Rba_trContext;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.PrContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.semantics.rulebasedautomata.BooleanValue;
import nl.cwi.reo.semantics.rulebasedautomata.Conjunction;
import nl.cwi.reo.semantics.rulebasedautomata.Constant;
import nl.cwi.reo.semantics.rulebasedautomata.DataConstraint;
import nl.cwi.reo.semantics.rulebasedautomata.DataTerm;
import nl.cwi.reo.semantics.rulebasedautomata.Equality;
import nl.cwi.reo.semantics.rulebasedautomata.RuleBasedAutomaton;
import nl.cwi.reo.semantics.rulebasedautomata.SyncConstraint;
import nl.cwi.reo.semantics.rulebasedautomata.TransitionRule;
import nl.cwi.reo.util.Monitor;

public class ListenerRBA extends Listener<RuleBasedAutomaton> {

	private ParseTreeProperty<RuleBasedAutomaton> primitives = new ParseTreeProperty<RuleBasedAutomaton>();
	private ParseTreeProperty<Value> params = new ParseTreeProperty<Value>();
	private ParseTreeProperty<TransitionRule> transition = new ParseTreeProperty<TransitionRule>();
	private ParseTreeProperty<TermExpression> variable = new ParseTreeProperty<TermExpression>();
	private ParseTreeProperty<SyncConstraint> syncConstraint = new ParseTreeProperty<SyncConstraint>();
	private ParseTreeProperty<DataConstraint> dataConstraint = new ParseTreeProperty<DataConstraint>();	
	private ParseTreeProperty<Port> incPorts = new ParseTreeProperty<Port>();	
	private ParseTreeProperty<Port> excPorts = new ParseTreeProperty<Port>();
	private ParseTreeProperty<Set<Port>> incPortsList = new ParseTreeProperty<Set<Port>>();	
	private ParseTreeProperty<Set<Port>> excPortsList = new ParseTreeProperty<Set<Port>>();
	
	
	public ListenerRBA(Monitor m) {
		super(m);
	}

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, primitives.get(ctx.rba()));
	}


	public void exitRba(RbaContext ctx) {
		Set<TransitionRule> transitionList = new HashSet<TransitionRule>();
		for(Rba_trContext tr_ctx : ctx.rba_tr()){
			transitionList.add(transition.get(tr_ctx));
		}
		primitives.put(ctx, new RuleBasedAutomaton(null,transitionList,null));
	}

	public void exitRba_tr(Rba_trContext ctx) {
		List<DataTerm> dataTermInput = new ArrayList<DataTerm>();
//		for(Rba_atContext at_ctx : ctx.rba_at()){
//			if(at_ctx.NAT()!=null)
//				dataTermInput.add(new Constant(new IntegerValue(Integer.parseInt(at_ctx.NAT().toString()))));
//			else
//				dataTermInput.add(new Constant(new StringValue(at_ctx.ID().toString())));
//		}
//		List<DataTerm> dataTermOutput = new ArrayList<DataTerm>();
//		for(Rba_dtContext dt_ctx : ctx.rba_dt()){
//			if(dt_ctx.rba_at().ID()!=null)
//				dataTermOutput.add(new Constant(new StringValue(dt_ctx.rba_at().ID().toString())));
//			else
//				dataTermOutput.add(new Constant(new IntegerValue(Integer.parseInt(dt_ctx.rba_at().NAT().toString()))));
//		}
		
//		transition.put(ctx, new TransitionRule((DataTerm[]) dataTermInput.toArray() , (DataTerm[])dataTermOutput.toArray(),new SyncConstraint(excPortsList.get(ctx),incPortsList.get(ctx)),null));
		
	}
	
	public void exitRba_term(Rba_dcContext ctx){
//		dataConstraint.put(ctx, new Equality());
	}
	public void exitRba_def(){
		
	}
	
	public void exitRba_sc(Rba_scContext ctx){
		Set<Port> incPort = new HashSet<Port>();
		Set<Port> excPort = new HashSet<Port>();
		
		for(Rba_portContext ctx_port : ctx.rba_port()){
			if(incPorts.get(ctx_port)!=null){
				incPort.add(incPorts.get(ctx_port));
			}
			if(excPorts.get(ctx_port)!=null){
				excPort.add(excPorts.get(ctx_port));
			}
		}
		excPortsList.put(ctx, excPort);
		incPortsList.put(ctx, incPort);
	}
	
	public void exitRba_included_port(Rba_included_portContext ctx){
		incPorts.put(ctx, new Port(ctx.ID().toString(),PortType.NONE,PrioType.NONE,null,true));
	}
	public void exitRba_excluded_port(Rba_excluded_portContext ctx){
		excPorts.put(ctx, new Port(ctx.ID().toString(),PortType.NONE,PrioType.NONE,null,true));
	}
	
	public void exitRba_dc_equality(Rba_dc_equalityContext ctx){
		
		dataConstraint.put(ctx, new Equality(new Constant(ctx.ID(0).toString()),new Constant(ctx.ID(1).toString())));
	}
	
	public void exitRba_dc_conjunction(Rba_dc_conjunctionContext ctx){
		dataConstraint.put(ctx, new Conjunction(dataConstraint.get(ctx.rba_dc(0)),dataConstraint.get(ctx.rba_dc(1))));
	}
	

	
	public void exitRba_term(Rba_termContext ctx){
		dataConstraint.put(ctx, new BooleanValue(Boolean.parseBoolean(ctx.ID().toString())));
	}
	
	public void exitRba_def(Rba_defContext ctx){

	}
	
	
	
//	public void exitPr_param(Pr_paramContext ctx) {
//		if(ctx.ID()!=null){
//			params.put(ctx, new StringValue(ctx.ID().toString()));			
//		}
//		if(ctx.NAT()!=null){
//			params.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().toString())));			
//		}
//		if(ctx.STRING()!=null){
//			params.put(ctx, new StringValue(ctx.STRING().toString().replaceAll("\"", "")));			
//		}
//		
//	}

}
