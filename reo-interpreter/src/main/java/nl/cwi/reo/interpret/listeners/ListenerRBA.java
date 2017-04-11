package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.RbaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_boolContext;
import nl.cwi.reo.interpret.ReoParser.Rba_boolean_formulaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Rba_decimalContext;
import nl.cwi.reo.interpret.ReoParser.Rba_defContext;
import nl.cwi.reo.interpret.ReoParser.Rba_equalityContext;
import nl.cwi.reo.interpret.ReoParser.Rba_formulaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_inequalityContext;
import nl.cwi.reo.interpret.ReoParser.Rba_memorycellInContext;
import nl.cwi.reo.interpret.ReoParser.Rba_memorycellOutContext;
import nl.cwi.reo.interpret.ReoParser.Rba_natContext;
import nl.cwi.reo.interpret.ReoParser.Rba_nullContext;
import nl.cwi.reo.interpret.ReoParser.Rba_parameterContext;
import nl.cwi.reo.interpret.ReoParser.Rba_ruleContext;
import nl.cwi.reo.interpret.ReoParser.Rba_stringContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncAtomContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncBlockContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncTrigerContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryCell;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.rbautomaton.Role;
import nl.cwi.reo.semantics.rbautomaton.Rule;
import nl.cwi.reo.semantics.rbautomaton.RulesBasedAutomaton;
import nl.cwi.reo.util.Monitor;

public class ListenerRBA extends Listener<RulesBasedAutomaton> {

	private ParseTreeProperty<RulesBasedAutomaton> automaton = new ParseTreeProperty<RulesBasedAutomaton>();
	private ParseTreeProperty<Formula> rba_formula = new ParseTreeProperty<Formula>();
	private ParseTreeProperty<Term> term = new ParseTreeProperty<Term>();	
	private ParseTreeProperty<Rule> rules = new ParseTreeProperty<Rule>();	

	private Map<Port,Role> mapPorts = new HashMap<Port,Role>();
	private final Term asterix = new Function(null, new ArrayList<Term>());
	
	public ListenerRBA(Monitor m) {
		super(m);
	}

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, automaton.get(ctx.rba()));
	}

	public void enterAtom(AtomContext ctx) {
		System.out.println(ctx.getText());
	}

	
	/*
	 * Rule Based Automaton:
	 */

	public void exitRba(RbaContext ctx) {
		Set<Rule> s = new HashSet<Rule>();
		for(Rba_ruleContext rbaContext : ctx.rba_rule()){
			s.add(rules.get(rbaContext));
		}
		automaton.put(ctx, new RulesBasedAutomaton(s));
	}
	
	/*
	 * Rules :
	 */

	public void enterRba_rule(Rba_ruleContext ctx) {
		mapPorts = new HashMap<Port,Role>(); 
	}
	
	public void exitRba_rule(Rba_ruleContext ctx) {
		rules.put(ctx, new Rule(mapPorts,rba_formula.get(ctx.rba_formula())));
	}
	
	/*
	 * Synchronisation :
	 */
	
	public void exitRba_syncAtom(Rba_syncAtomContext ctx){
		mapPorts.put(new Port(ctx.ID().getText()),Role.FIRE);
	}
	public void exitRba_syncTriger(Rba_syncTrigerContext ctx){
		mapPorts.put(new Port(ctx.ID().getText()),Role.TRIGGER);			
	}
	public void exitRba_syncBlock(Rba_syncBlockContext ctx){
		mapPorts.put(new Port(ctx.ID().getText()),Role.BLOCK);
	}
	
	/*
	 * Formula :
	 */
	
	public void exitRba_conjunction(Rba_conjunctionContext ctx){
		List<Formula> l = new ArrayList<Formula>();
		for(Rba_formulaContext f : ctx.rba_formula()){
			l.add(rba_formula.get(f));
		}
		rba_formula.put(ctx, new Conjunction(l));
	}
	public void exitRba_def(Rba_defContext ctx){
		rba_formula.put(ctx, rba_formula.get(ctx.rba_formula()));
	}
	public void exitRba_equality(Rba_equalityContext ctx){
		rba_formula.put(ctx, new Equality(term.get(ctx.rba_term(0)),term.get(ctx.rba_term(1))));
	}
	public void exitRba_inequality(Rba_inequalityContext ctx){
		rba_formula.put(ctx, new Negation(new Equality(term.get(ctx.rba_term(0)),term.get(ctx.rba_term(1)))));		
	}	
	public void exitRba_boolean_formula(Rba_boolean_formulaContext ctx){
		rba_formula.put(ctx, new Relation(ctx.rba_boolean().getText(),Arrays.asList(new Function("constant", Boolean.parseBoolean(ctx.rba_boolean().getText()), new ArrayList<Term>()))));		
	}	

	
	/*
	 * Terms:
	 */
	public void exitRba_nat(Rba_natContext ctx){
		term.put(ctx, new Function("constant", Integer.parseInt(ctx.NAT().getText()), new ArrayList<Term>()));
	}
	
	public void exitRba_bool(Rba_boolContext ctx){
		term.put(ctx, new Function("constant", Boolean.parseBoolean(ctx.BOOL().getText()), new ArrayList<Term>()));		
	}

	public void exitRba_string(Rba_stringContext ctx){
		term.put(ctx, new Function(ctx.STRING().toString(), new ArrayList<Term>()));		
	}

	public void exitRba_decimal(Rba_decimalContext ctx){
		term.put(ctx, new Function("constant", Double.parseDouble(ctx.DEC().getText()), new ArrayList<Term>()));
	}
	
	public void exitRba_parameter(Rba_parameterContext ctx){
		term.put(ctx, new Node(new Port(ctx.ID().getText(),PortType.NONE,PrioType.NONE,new TypeTag("Integer"),true)));
	}
	
	public void exitRba_memorycellIn(Rba_memorycellInContext ctx){
		term.put(ctx, new MemoryCell(Integer.parseInt(ctx.NAT().getText()),false));
	}
	
	public void exitRba_memorycellOut(Rba_memorycellOutContext ctx){
		term.put(ctx, new MemoryCell(Integer.parseInt(ctx.NAT().getText()),true));		
	}
	
	public void exitRba_null(Rba_nullContext ctx){
		term.put(ctx, asterix);
	}
	
////	public void exitSba_dt_function(Sba_dt_functionContext ctx){
////		List<Term> termList = new ArrayList<Term>();
////		for(Sba_dtContext Sbacontext : ctx.Sba_dt()){
////			termList.add(term.get(Sbacontext));
////		}
////		dataterm.put(ctx, new Function(ctx.ID().toString(),transitions));		
////	}
//	
//	
//	/*
//	 * Synchronisation constraints:
//	 */
//	
//	public void exitSba_sc(Sba_scContext ctx){
//		
//		List<Formula> formulaList = new ArrayList<Formula>();
//		
//		for(Sba_portContext ctx_port : ctx.sba_port()){
//			if(incPorts.get(ctx_port)!=null){
//				formulaList.add(new Negation(new Equality(new Node(incPorts.get(ctx_port)), asterix)));
//			}
//			if(excPorts.get(ctx_port)!=null){
//				formulaList.add(new Equality(new Node(excPorts.get(ctx_port)), asterix));
//			}
//		}
//		sba_formula.put(ctx, new Conjunction(formulaList));
////		syncConstraint.put(ctx, new SyncConstraint(incPort,excPort));
//	}
//	
//	/*
//	 * Ports :
//	 */
//	
//	public void exitSba_included_port(Sba_included_portContext ctx){
//		incPorts.put(ctx, new Port(ctx.ID().toString(),PortType.NONE,PrioType.NONE,new TypeTag("Integer"),true));
//	}
//	public void exitSba_excluded_port(Sba_excluded_portContext ctx){
//		excPorts.put(ctx, new Port(ctx.ID().toString(),PortType.NONE,PrioType.NONE,new TypeTag("Integer"),true));
//	}
//	
//	
//	/*
//	 * Data Constraint :
//	 */
//	public void exitSba_term(Sba_termContext ctx){
//		sba_formula.put(ctx, new Equality(term.get(ctx.sba_dt()), new Function("constant", true, new ArrayList<Term>())));
//
//	}
//	public void exitSba_def(Sba_defContext ctx){
////		if(dataConstraint.get(ctx) instanceof Conjunction)
////			dataConstraint.put(ctx, new Conjunction(dataConstraint.get(ctx)));
////		DataConstraint d = dataConstraint.get(ctx.Sba_dc());
//		sba_formula.put(ctx, sba_formula.get(ctx.sba_dc()));
//	}
//	
//	public void exitSba_dc_equality(Sba_dc_equalityContext ctx){
//		sba_formula.put(ctx, new Equality(term.get(ctx.sba_dt(0)),term.get(ctx.sba_dt(1))));
//	}
//	
//	public void exitSba_dc_inequality(Sba_dc_inequalityContext ctx){
//		sba_formula.put(ctx, new Negation(new Equality(term.get(ctx.sba_dt(0)),term.get(ctx.sba_dt(1)))));
//	}
//	
//	public void exitSba_dc_conjunction(Sba_dc_conjunctionContext ctx){
//		sba_formula.put(ctx, new Conjunction(Arrays.asList(sba_formula.get(ctx.sba_dc(0)),sba_formula.get(ctx.sba_dc(1)))));
//	}
//		
	
	
	
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
