package nl.cwi.reo.semantics.hypergraphs;

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
import nl.cwi.reo.interpret.ReoParser.Rba_operationContext;
import nl.cwi.reo.interpret.ReoParser.Rba_parameterContext;
import nl.cwi.reo.interpret.ReoParser.Rba_ruleContext;
import nl.cwi.reo.interpret.ReoParser.Rba_initialContext;
import nl.cwi.reo.interpret.ReoParser.Rba_stringContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncFireContext;
import nl.cwi.reo.interpret.ReoParser.Rba_termContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncBlockContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Terms;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.semantics.rulebasedautomata.Rule;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ListenerRBA.
 */
public class ListenerCH extends BaseListener {

	/** The automaton. */
	protected ParseTreeProperty<ConstraintHypergraph> automaton = new ParseTreeProperty<>();

	/** The rba formula. */
	protected ParseTreeProperty<Formula> rba_formula = new ParseTreeProperty<>();

	/** The term. */
	protected ParseTreeProperty<Term> term = new ParseTreeProperty<>();

	/** The rules. */
	protected ParseTreeProperty<Rule> rules = new ParseTreeProperty<>();

	/** The map ports. */
	protected Map<Port, Boolean> mapPorts = new HashMap<>();

	/** The initial. */
	protected Map<MemoryVariable, Term> initial = new HashMap<>();

	/**
	 * Instantiates a new listener RBA.
	 *
	 * @param m
	 *            the m
	 */
	public ListenerCH(Monitor m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */
	@Override
	public void enterAtom(AtomContext ctx) {
		initial = new HashMap<>();
		mapPorts = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba(nl.cwi.reo.interpret.
	 * ReoParser.RbaContext)
	 */
	@Override
	public void exitRba(RbaContext ctx) {
		Set<Rule> s = new HashSet<Rule>();
		for (Rba_ruleContext rbaContext : ctx.rba_rule()) {
			s.add(rules.get(rbaContext));
		}
		atoms.put(ctx, new ConstraintHypergraph(s, initial));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterRba_rule(nl.cwi.reo.interpret.
	 * ReoParser.Rba_ruleContext)
	 */
	@Override
	public void enterRba_rule(Rba_ruleContext ctx) {
		mapPorts = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_rule(nl.cwi.reo.interpret.
	 * ReoParser.Rba_ruleContext)
	 */
	public void exitRba_rule(Rba_ruleContext ctx) {
		Formula f = rba_formula.get(ctx.rba_formula());
		if (f != null)
			rules.put(ctx, new Rule(mapPorts, f));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_initial(nl.cwi.reo.interpret
	 * .ReoParser.Rba_initialContext)
	 */
	public void exitRba_initial(Rba_initialContext ctx) {
		initial.put(new MemoryVariable(ctx.ID().getText(), false, TypeTags.Object), term.get(ctx.rba_term()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_syncFire(nl.cwi.reo.
	 * interpret.ReoParser.Rba_syncFireContext)
	 */
	@Override
	public void exitRba_syncFire(Rba_syncFireContext ctx) {
		mapPorts.put(new Port(ctx.ID().getText()), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_syncBlock(nl.cwi.reo.
	 * interpret.ReoParser.Rba_syncBlockContext)
	 */
	public void exitRba_syncBlock(Rba_syncBlockContext ctx) {
		mapPorts.put(new Port(ctx.ID().getText()), false);
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
		for (Rba_formulaContext f : ctx.rba_formula())
			l.add(rba_formula.get(f));
		rba_formula.put(ctx, Formulas.conjunction(l));
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
		rba_formula.put(ctx, rba_formula.get(ctx.rba_formula()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_equality(nl.cwi.reo.
	 * interpret.ReoParser.Rba_equalityContext)
	 */
	@Override
	public void exitRba_equality(Rba_equalityContext ctx) {
		Term t0 = term.get(ctx.rba_term(0));
		Term t1 = term.get(ctx.rba_term(1));
		if (t0 != null && t1 != null)
			rba_formula.put(ctx, new Equality(t0, t1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_inequality(nl.cwi.reo.
	 * interpret.ReoParser.Rba_inequalityContext)
	 */
	@Override
	public void exitRba_inequality(Rba_inequalityContext ctx) {
		Term t0 = term.get(ctx.rba_term(0));
		Term t1 = term.get(ctx.rba_term(1));
		if (t0 != null && t1 != null)
			rba_formula.put(ctx, new Negation(new Equality(t0, t1)));
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
		rba_formula.put(ctx, new TruthValue(true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_false(nl.cwi.reo.interpret.
	 * ReoParser.Rba_falseContext)
	 */
	public void exitRba_false(Rba_falseContext ctx) {
		rba_formula.put(ctx, new TruthValue(false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_nat(nl.cwi.reo.interpret.
	 * ReoParser.Rba_natContext)
	 */
	@Override
	public void exitRba_nat(Rba_natContext ctx) {
		term.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Integer));
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
		term.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Boolean));
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
		term.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.String));
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
		term.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Decimal));
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
			Term t = term.get(arg);
			args.add(t);
			tag = t.getTypeTag();
		}
		term.put(ctx, new Function(ctx.ID().getText(), args, false, tag));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_parameter(nl.cwi.reo.
	 * interpret.ReoParser.Rba_parameterContext)
	 */
	@Override
	public void exitRba_parameter(Rba_parameterContext ctx) {
		term.put(ctx, new PortVariable(new Port(ctx.ID().getText())));
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
		term.put(ctx, new MemoryVariable(ctx.ID().getText(), false, TypeTags.Object));
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
		term.put(ctx, new MemoryVariable(ctx.ID().getText(), true, TypeTags.Object));
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
		term.put(ctx, Terms.Null);
	}
	
	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_unarymin(nl.cwi.reo.interpret.ReoParser.Rba_unaryminContext)
	 */
	@Override 
	public void exitRba_unarymin(Rba_unaryminContext ctx) { 
		term.put(ctx, new Function("-", Arrays.asList(term.get(ctx.rba_term())), false, new TypeTag("int")));		
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_operation(nl.cwi.reo.interpret.ReoParser.Rba_operationContext)
	 */
	@Override 
	public void exitRba_operation(Rba_operationContext ctx) { 
		TypeTag tag = term.get(ctx.rba_term(0)).getTypeTag();
		term.put(ctx, new Function(ctx.op.getText(), Arrays.asList(term.get(ctx.rba_term(0)), term.get(ctx.rba_term(1))), true, tag));		
	}

}
