package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
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
import nl.cwi.reo.semantics.predicates.MemCell;
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
	private final Term asterix = new Function("*", null, new ArrayList<Term>());
	
	public ListenerRBA(Monitor m) {
		super(m);
	}

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, automaton.get(ctx.rba()));
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
//		rba_formula.put(ctx, new Relation(ctx.rba_boolean().getText(),Arrays.asList(new Function("constant", Boolean.parseBoolean(ctx.rba_boolean().getText()), new ArrayList<Term>()))));	
		String b = ctx.rba_boolean().getText();
		rba_formula.put(ctx, new Relation(b, b, null));
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
		term.put(ctx, new MemCell(ctx.ID().getText(), false));
	}
	
	public void exitRba_memorycellOut(Rba_memorycellOutContext ctx){
		term.put(ctx, new MemCell(ctx.ID().getText(), true));
	}
	
	public void exitRba_null(Rba_nullContext ctx){
		term.put(ctx, asterix);
	}

}
