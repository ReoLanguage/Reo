package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.ReoParser.Rba_negationContext;
import nl.cwi.reo.interpret.ReoParser.RbaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_boolContext;
import nl.cwi.reo.interpret.ReoParser.Rba_trueContext;
import nl.cwi.reo.interpret.ReoParser.Rba_unaryminContext;
import nl.cwi.reo.interpret.listeners.BaseListener;
import nl.cwi.reo.interpret.ReoParser.Rba_falseContext;
import nl.cwi.reo.interpret.ReoParser.Rba_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Rba_decimalContext;
import nl.cwi.reo.interpret.ReoParser.Rba_defContext;
import nl.cwi.reo.interpret.ReoParser.Rba_equalityContext;
import nl.cwi.reo.interpret.ReoParser.Rba_formulaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_inequalityContext;
import nl.cwi.reo.interpret.ReoParser.Rba_functionContext;
import nl.cwi.reo.interpret.ReoParser.Rba_memorycellInContext;
import nl.cwi.reo.interpret.ReoParser.Rba_memorycellOutContext;
import nl.cwi.reo.interpret.ReoParser.Rba_natContext;
import nl.cwi.reo.interpret.ReoParser.Rba_nullContext;
import nl.cwi.reo.interpret.ReoParser.Rba_null_ctxtContext;
import nl.cwi.reo.interpret.ReoParser.Rba_operationContext;
import nl.cwi.reo.interpret.ReoParser.Rba_parameterContext;
import nl.cwi.reo.interpret.ReoParser.Rba_portContext;
import nl.cwi.reo.interpret.ReoParser.Rba_relationContext;
import nl.cwi.reo.interpret.ReoParser.Rba_ruleContext;
import nl.cwi.reo.interpret.ReoParser.Rba_initialContext;
import nl.cwi.reo.interpret.ReoParser.Rba_stringContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncFireContext;
import nl.cwi.reo.interpret.ReoParser.Rba_termContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncBlockContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.semantics.predicates.Constant;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Terms;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.util.Monitor;

/**
 * The Class ListenerRBA.
 */
public class ListenerRBA extends BaseListener {

	/** The transitions. */
	protected ParseTreeProperty<Rule> transitions = new ParseTreeProperty<>();

	/** The formulas. */
	protected ParseTreeProperty<Formula> formulas = new ParseTreeProperty<>();

	/** The terms. */
	protected ParseTreeProperty<Term> terms = new ParseTreeProperty<>();

	/** The synchronization constraints. */
	protected ParseTreeProperty<Map<Port, Boolean>> syncs = new ParseTreeProperty<>();

	/** The initial value of memory. */
	protected ParseTreeProperty<Map<MemoryVariable, Term>> initials = new ParseTreeProperty<>();
	
	
	/**
	 * Instantiates a new listener RBA.
	 *
	 * @param m
	 *            the monitor
	 */
	public ListenerRBA(Monitor m) {
		super(m);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba(nl.cwi.reo.interpret.
	 * ReoParser.RbaContext)
	 */
	@Override
	public void exitRba(RbaContext ctx) {
		Set<Rule> s = new HashSet<>();
		for (Rba_ruleContext rule_ctx : ctx.rba_rule())
			s.add(transitions.get(rule_ctx));
		Set<Set<Rule>> rules = new HashSet<>();
		rules.add(s);

		Map<MemoryVariable, Term> initial = new HashMap<>();
		for (Rba_initialContext initial_ctx : ctx.rba_initial()) {
			Map<MemoryVariable, Term> init = initials.get(initial_ctx);
			if (init != null)
				initial.putAll(init);
		}
		atoms.put(ctx, new RuleBasedAutomaton(rules, initial));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_rule(nl.cwi.reo.interpret.
	 * ReoParser.Rba_ruleContext)
	 */
	public void exitRba_rule(Rba_ruleContext ctx) {
		Map<Port, Boolean> sync = new HashMap<>();
		for (Rba_portContext port_ctx : ctx.rba_port()) {
			Map<Port, Boolean> syncp = syncs.get(port_ctx);
			if (syncp != null)
				sync.putAll(syncp);
		}

		List<Formula> clauses = new ArrayList<>();
		clauses.add(formulas.get(ctx.rba_formula()));
//
//		for (Map.Entry<Port, Boolean> entry : sync.entrySet()) {
//			Formula eq = new Equality(new PortVariable(entry.getKey()), Terms.Null);
//			if (entry.getValue())
//				clauses.add(new Negation(eq));
//			else
//				clauses.add(eq);
//		}

		Formula f = Formulas.conjunction(clauses);
		transitions.put(ctx, new Rule(sync, f));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_initial(nl.cwi.reo.interpret
	 * .ReoParser.Rba_initialContext)
	 */
	public void exitRba_initial(Rba_initialContext ctx) {
		Map<MemoryVariable, Term> initial = new HashMap<>();
		initial.put(new MemoryVariable(ctx.ID().getText(), false, TypeTags.Object), terms.get(ctx.rba_term()));
		initials.put(ctx, initial);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_syncFire(nl.cwi.reo.
	 * interpret.ReoParser.Rba_syncFireContext)
	 */
	@Override
	public void exitRba_syncFire(Rba_syncFireContext ctx) {
		Map<Port, Boolean> syncp = new HashMap<>();
		syncp.put(new Port(ctx.ID().getText()), true);
		syncs.put(ctx, syncp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_syncBlock(nl.cwi.reo.
	 * interpret.ReoParser.Rba_syncBlockContext)
	 */
	public void exitRba_syncBlock(Rba_syncBlockContext ctx) {
		Map<Port, Boolean> syncp = new HashMap<>();
		syncp.put(new Port(ctx.ID().getText()), false);
		syncs.put(ctx, syncp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_conjunction(nl.cwi.reo.
	 * interpret.ReoParser.Rba_conjunctionContext)
	 */
	@Override
	public void exitRba_conjunction(Rba_conjunctionContext ctx) {
		List<Formula> l = new ArrayList<Formula>();
		for (Rba_formulaContext f : ctx.rba_formula()) {
			Formula g = formulas.get(f);
			if (g != null)
				l.add(g);
		}
		formulas.put(ctx, Formulas.conjunction(l));
	}


	public void exitRba_negation(Rba_negationContext ctx) {
		formulas.put(ctx, new Negation(formulas.get(ctx.rba_formula())));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_def(nl.cwi.reo.interpret.
	 * ReoParser.Rba_defContext)
	 */
	@Override
	public void exitRba_def(Rba_defContext ctx) {
		formulas.put(ctx, formulas.get(ctx.rba_formula()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_equality(nl.cwi.reo.
	 * interpret.ReoParser.Rba_equalityContext)
	 */
	@Override
	public void exitRba_equality(Rba_equalityContext ctx) {
		Term t0 = terms.get(ctx.rba_term(0));
		Term t1 = terms.get(ctx.rba_term(1));		
		if (t0 != null && t1 != null)
			formulas.put(ctx, new Equality(t0, t1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_inequality(nl.cwi.reo.
	 * interpret.ReoParser.Rba_inequalityContext)
	 */
	@Override
	public void exitRba_inequality(Rba_inequalityContext ctx) {
		Term t0 = terms.get(ctx.rba_term(0));
		Term t1 = terms.get(ctx.rba_term(1));
		if (t0 != null && t1 != null)
			formulas.put(ctx, new Negation(new Equality(t0, t1)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_true(nl.cwi.reo.interpret.
	 * ReoParser.Rba_trueContext)
	 */
	@Override
	public void exitRba_true(Rba_trueContext ctx) {
		formulas.put(ctx, new TruthValue(true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_false(nl.cwi.reo.interpret.
	 * ReoParser.Rba_falseContext)
	 */
	public void exitRba_false(Rba_falseContext ctx) {
		formulas.put(ctx, new TruthValue(false));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_relation(nl.cwi.reo.interpret.
	 * ReoParser.Rba_relationContext)
	 */
	public void exitRba_relation(Rba_relationContext ctx) {
		List<Term> args = new ArrayList<Term>();
		for (Rba_termContext arg : ctx.rba_term()) {
			Term t = terms.get(arg);
			args.add(t);
		}
		formulas.put(ctx, new Relation(ctx.ID().getText(),args,false));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_notrelation(nl.cwi.reo.interpret.
	 * ReoParser.Rba_notrelationContext)
	 */
//	public void exitRba_notrelation(Rba_notrelationContext ctx) {
//		List<Term> args = new ArrayList<Term>();
//		for (Rba_termContext arg : ctx.rba_term()) {
//			Term t = terms.get(arg);
//			args.add(t);
//		}
//		formulas.put(ctx, new Negation(new Relation(ctx.ID().getText(),args,false)));
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_nat(nl.cwi.reo.interpret.
	 * ReoParser.Rba_natContext)
	 */
	@Override
	public void exitRba_nat(Rba_natContext ctx) {
//		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Integer));
		terms.put(ctx,  new Constant(new IntegerValue(Integer.valueOf(ctx.getText()))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_bool(nl.cwi.reo.interpret.
	 * ReoParser.Rba_boolContext)
	 */
	@Override
	public void exitRba_bool(Rba_boolContext ctx) {	
//		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Boolean));
		if(ctx.getText() == "true")
			terms.put(ctx, new Constant(new BooleanValue(true)));
		else
			terms.put(ctx, new Constant(new BooleanValue(false)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_string(nl.cwi.reo.interpret.
	 * ReoParser.Rba_stringContext)
	 */
	@Override
	public void exitRba_string(Rba_stringContext ctx) {
//		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.String));
		terms.put(ctx, new Constant(new StringValue(ctx.getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_decimal(nl.cwi.reo.interpret
	 * .ReoParser.Rba_decimalContext)
	 */
	@Override
	public void exitRba_decimal(Rba_decimalContext ctx) {
//		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Decimal));
		terms.put(ctx,  new Constant(new DecimalValue(Double.valueOf(ctx.getText()))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_function(nl.cwi.reo.
	 * interpret.ReoParser.Rba_functionContext)
	 */
	@Override
	public void exitRba_function(Rba_functionContext ctx) {
		List<Term> args = new ArrayList<Term>();
		TypeTag tag = null;
		for (Rba_termContext arg : ctx.rba_term()) {
			Term t = terms.get(arg);
			args.add(t);
			tag = t.getTypeTag();
		}
		terms.put(ctx, new Function(ctx.ID().getText(), args, false, tag));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_parameter(nl.cwi.reo.
	 * interpret.ReoParser.Rba_parameterContext)
	 */
	@Override
	public void exitRba_parameter(Rba_parameterContext ctx) {
		terms.put(ctx, new PortVariable(new Port(ctx.ID().getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_memorycellIn(nl.cwi.reo.
	 * interpret.ReoParser.Rba_memorycellInContext)
	 */
	@Override
	public void exitRba_memorycellIn(Rba_memorycellInContext ctx) {
		terms.put(ctx, new MemoryVariable(ctx.ID().getText(), false, TypeTags.Object));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_memorycellOut(nl.cwi.reo.
	 * interpret.ReoParser.Rba_memorycellOutContext)
	 */
	@Override
	public void exitRba_memorycellOut(Rba_memorycellOutContext ctx) {
		terms.put(ctx, new MemoryVariable(ctx.ID().getText(), true, TypeTags.Object));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_null(nl.cwi.reo.interpret.
	 * ReoParser.Rba_nullContext)
	 */
	@Override
	public void exitRba_null(Rba_nullContext ctx) {
		terms.put(ctx, Terms.Null);
	}
	
	@Override
	public void exitRba_null_ctxt(Rba_null_ctxtContext ctx) {
		terms.put(ctx, Terms.Null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_unarymin(nl.cwi.reo.
	 * interpret.ReoParser.Rba_unaryminContext)
	 */
	@Override
	public void exitRba_unarymin(Rba_unaryminContext ctx) {
		terms.put(ctx, new Function("-", Arrays.asList(terms.get(ctx.rba_term())), false, new TypeTag("int")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_operation(nl.cwi.reo.
	 * interpret.ReoParser.Rba_operationContext)
	 */
	@Override
	public void exitRba_operation(Rba_operationContext ctx) {
		TypeTag tag = terms.get(ctx.rba_term(0)).getTypeTag();
		terms.put(ctx, new Function(ctx.op.getText(),
				Arrays.asList(terms.get(ctx.rba_term(0)), terms.get(ctx.rba_term(1))), true, tag));
	}

}
