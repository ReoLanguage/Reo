package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.SbaContext;
import nl.cwi.reo.interpret.ReoParser.Sba_boolContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dc_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dc_equalityContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dc_inequalityContext;
import nl.cwi.reo.interpret.ReoParser.Sba_decimalContext;
import nl.cwi.reo.interpret.ReoParser.Sba_defContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dt_memorycellInContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dt_memorycellOutContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dt_nullContext;
import nl.cwi.reo.interpret.ReoParser.Sba_dt_parameterContext;
import nl.cwi.reo.interpret.ReoParser.Sba_excluded_portContext;
import nl.cwi.reo.interpret.ReoParser.Sba_included_portContext;
import nl.cwi.reo.interpret.ReoParser.Sba_natContext;
import nl.cwi.reo.interpret.ReoParser.Sba_portContext;
import nl.cwi.reo.interpret.ReoParser.Sba_scContext;
import nl.cwi.reo.interpret.ReoParser.Sba_stringContext;
import nl.cwi.reo.interpret.ReoParser.Sba_termContext;
import nl.cwi.reo.interpret.ReoParser.Sba_trContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryCell;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Predicate;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.util.Monitor;

public class ListenerSBA extends Listener<Predicate> {

	private ParseTreeProperty<Predicate> automaton = new ParseTreeProperty<Predicate>();
	private ParseTreeProperty<Formula> sba_formula = new ParseTreeProperty<Formula>();
	private ParseTreeProperty<Term> term = new ParseTreeProperty<Term>();
//	private ParseTreeProperty<> syncConstraint = new ParseTreeProperty<SyncConstraint>();
//	private ParseTreeProperty<DataConstraint> dataConstraint = new ParseTreeProperty<DataConstraint>();	
	private ParseTreeProperty<Port> incPorts = new ParseTreeProperty<Port>();	
	private ParseTreeProperty<Port> excPorts = new ParseTreeProperty<Port>();	
	
	private final Term asterix = new Function(null, new ArrayList<Term>());
	
	public ListenerSBA(Monitor m) {
		super(m);
	}

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, automaton.get(ctx.sba()));
	}

	/*
	 * Rule Based Automaton:
	 */

	public void exitSba(SbaContext ctx) {
		List<Formula> DNF = new ArrayList<Formula>();
		for(Sba_trContext tr_ctx : ctx.sba_tr()){
			DNF.add(sba_formula.get(tr_ctx));
		}
		automaton.put(ctx, new Predicate(new Disjunction(DNF)));
	}
	
	/*
	 * Transition formula :
	 */

	public void exitSba_tr(Sba_trContext ctx) {
		sba_formula.put(ctx, new Conjunction(Arrays.asList(sba_formula.get(ctx.sba_sc()),sba_formula.get(ctx.sba_dc()))));
		
	}

	/*
	 * Data Terms:
	 */
	public void exitSba_nat(Sba_natContext ctx){
		term.put(ctx, new Function("constant", Integer.parseInt(ctx.NAT().getText()), new ArrayList<Term>()));
	}
	
	public void exitSba_bool(Sba_boolContext ctx){
		term.put(ctx, new Function("constant", Boolean.parseBoolean(ctx.sba_boolean().getText()), new ArrayList<Term>()));		
	}

	public void exitSba_string(Sba_stringContext ctx){
		term.put(ctx, new Function(ctx.STRING().toString(), new ArrayList<Term>()));		
	}

	public void exitSba_decimal(Sba_decimalContext ctx){
		term.put(ctx, new Function("constant", Double.parseDouble(ctx.DEC().getText()), new ArrayList<Term>()));
	}
	
	public void exitSba_dt_parameter(Sba_dt_parameterContext ctx){
		term.put(ctx, new Node(new Port(ctx.ID().getText(),PortType.NONE,PrioType.NONE,new TypeTag("Integer"),true)));
	}
	
	public void exitSba_dt_memorycellIn(Sba_dt_memorycellInContext ctx){
		term.put(ctx, new MemoryCell(Integer.parseInt(ctx.NAT().getText()),false));
	}
	
	public void exitSba_dt_memorycellOut(Sba_dt_memorycellOutContext ctx){
		term.put(ctx, new MemoryCell(Integer.parseInt(ctx.NAT().getText()),true));		
	}
	
//	public void exitSba_dt_function(Sba_dt_functionContext ctx){
//		List<Term> termList = new ArrayList<Term>();
//		for(Sba_dtContext Sbacontext : ctx.Sba_dt()){
//			termList.add(term.get(Sbacontext));
//		}
//		dataterm.put(ctx, new Function(ctx.ID().toString(),transitions));		
//	}
	
	public void exitSba_dt_null(Sba_dt_nullContext ctx){
		term.put(ctx, asterix);
	}
	
	/*
	 * Synchronisation constraints:
	 */
	
	public void exitSba_sc(Sba_scContext ctx){
		
		List<Formula> formulaList = new ArrayList<Formula>();
		
		for(Sba_portContext ctx_port : ctx.sba_port()){
			if(incPorts.get(ctx_port)!=null){
				formulaList.add(new Negation(new Equality(new Node(incPorts.get(ctx_port)), asterix)));
			}
			if(excPorts.get(ctx_port)!=null){
				formulaList.add(new Equality(new Node(excPorts.get(ctx_port)), asterix));
			}
		}
		sba_formula.put(ctx, new Conjunction(formulaList));
//		syncConstraint.put(ctx, new SyncConstraint(incPort,excPort));
	}
	
	/*
	 * Ports :
	 */
	
	public void exitSba_included_port(Sba_included_portContext ctx){
		incPorts.put(ctx, new Port(ctx.ID().toString(),PortType.NONE,PrioType.NONE,new TypeTag("Integer"),true));
	}
	public void exitSba_excluded_port(Sba_excluded_portContext ctx){
		excPorts.put(ctx, new Port(ctx.ID().toString(),PortType.NONE,PrioType.NONE,new TypeTag("Integer"),true));
	}
	
	
	/*
	 * Data Constraint :
	 */
	public void exitSba_term(Sba_termContext ctx){
		sba_formula.put(ctx, new Equality(term.get(ctx.sba_dt()), new Function("constant", true, new ArrayList<Term>())));

	}
	public void exitSba_def(Sba_defContext ctx){
//		if(dataConstraint.get(ctx) instanceof Conjunction)
//			dataConstraint.put(ctx, new Conjunction(dataConstraint.get(ctx)));
//		DataConstraint d = dataConstraint.get(ctx.Sba_dc());
		sba_formula.put(ctx, sba_formula.get(ctx.sba_dc()));
	}
	
	public void exitSba_dc_equality(Sba_dc_equalityContext ctx){
		sba_formula.put(ctx, new Equality(term.get(ctx.sba_dt(0)),term.get(ctx.sba_dt(1))));
	}
	
	public void exitSba_dc_inequality(Sba_dc_inequalityContext ctx){
		sba_formula.put(ctx, new Negation(new Equality(term.get(ctx.sba_dt(0)),term.get(ctx.sba_dt(1)))));
	}
	
	public void exitSba_dc_conjunction(Sba_dc_conjunctionContext ctx){
		sba_formula.put(ctx, new Conjunction(Arrays.asList(sba_formula.get(ctx.sba_dc(0)),sba_formula.get(ctx.sba_dc(1)))));
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
