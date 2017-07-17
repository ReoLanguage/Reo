package nl.cwi.reo.interpret.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.ReoParser.Rba_trContext;
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ReoParser.RbaContext;
import nl.cwi.reo.interpret.ReoParser.Rba_boolContext;
import nl.cwi.reo.interpret.ReoParser.Rba_trueContext;
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
import nl.cwi.reo.interpret.ReoParser.Rba_parameterContext;
import nl.cwi.reo.interpret.ReoParser.Rba_ruleContext;
import nl.cwi.reo.interpret.ReoParser.Rba_initialContext;
import nl.cwi.reo.interpret.ReoParser.Rba_stringContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncFireContext;
import nl.cwi.reo.interpret.ReoParser.Rba_termContext;
import nl.cwi.reo.interpret.ReoParser.Rba_syncBlockContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.hypergraphs.ConstraintHypergraph;
import nl.cwi.reo.semantics.hypergraphs.Rule;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.NullValue;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ListenerRBA.
 */
public class ListenerRBA extends Listener<ConstraintHypergraph> {

	/** The automaton. */
	private ParseTreeProperty<ConstraintHypergraph> automaton = new ParseTreeProperty<>();

	/** The rba formula. */
	private ParseTreeProperty<Formula> rba_formula = new ParseTreeProperty<>();

	/** The term. */
	private ParseTreeProperty<Term> term = new ParseTreeProperty<>();

	/** The rules. */
	private ParseTreeProperty<Rule> rules = new ParseTreeProperty<>();

	/** The map ports. */
	private Map<Port, Boolean> mapPorts = new HashMap<>();

	/** The initial. */
	private Map<MemoryVariable, Term> initial = new HashMap<>();

	/**
	 * Instantiates a new listener RBA.
	 *
	 * @param m
	 *            the m
	 */
	public ListenerRBA(Monitor m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */
	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, automaton.get(ctx.rba()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterAtom(nl.cwi.reo.interpret.
	 * ReoParser.AtomContext)
	 */
	public void enterAtom(AtomContext ctx) {
		initial = new HashMap<>();
		mapPorts = new HashMap<>();
	}

	/*
	 * Rule Based Automaton:
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba(nl.cwi.reo.interpret.
	 * ReoParser.RbaContext)
	 */
	public void exitRba(RbaContext ctx) {
		Set<Rule> s = new HashSet<Rule>();
		for (Rba_ruleContext rbaContext : ctx.rba_rule()) {
			s.add(rules.get(rbaContext));
		}
		automaton.put(ctx, new ConstraintHypergraph(s, initial));
	}

	/*
	 * State based rule :
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_tr(nl.cwi.reo.interpret.
	 * ReoParser.Rba_trContext)
	 */
	public void exitRba_tr(Rba_trContext ctx) {
		// Rule r = rules.get(ctx.rba_rule());
		// rules.put(ctx, new Rule(r.getSync(), new
		// Conjunction(r.getFormula(),new Equality(new
		// MemCell(ctx.ID(0).getText(),false),new
		// MemCell(ctx.ID(0).getText(),true)));
		// State q1 = new State(ctx.ID(0).getText());
		// State q2 = new State(ctx.ID(1).getText());
		// SortedSet<Port> sc = scs.get(ctx.pa_sc());
		// transitions.put(ctx, new Transition<NullLabel>(q1, q2, sc, new
		// NullLabel()));
	}

	/**
	 * Rules.
	 *
	 * @param ctx
	 *            the ctx
	 */

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
		rules.put(ctx, new Rule(mapPorts, rba_formula.get(ctx.rba_formula())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_initial(nl.cwi.reo.interpret
	 * .ReoParser.Rba_initialContext)
	 */
	public void exitRba_initial(Rba_initialContext ctx) {
		initial.put(new MemoryVariable(ctx.ID().getText(), false), term.get(ctx.rba_term()));
	}

	/**
	 * Synchronisation constraints.
	 *
	 * @param ctx
	 *            the ctx
	 */

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

	/**
	 * Data constraints.
	 *
	 * @param ctx
	 *            the ctx
	 */

	public void exitRba_conjunction(Rba_conjunctionContext ctx) {
		List<Formula> l = new ArrayList<Formula>();
		for (Rba_formulaContext f : ctx.rba_formula())
			l.add(rba_formula.get(f));
		rba_formula.put(ctx, new Conjunction(l));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_def(nl.cwi.reo.interpret.
	 * ReoParser.Rba_defContext)
	 */
	public void exitRba_def(Rba_defContext ctx) {
		rba_formula.put(ctx, rba_formula.get(ctx.rba_formula()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_equality(nl.cwi.reo.
	 * interpret.ReoParser.Rba_equalityContext)
	 */
	public void exitRba_equality(Rba_equalityContext ctx) {
		rba_formula.put(ctx, new Equality(term.get(ctx.rba_term(0)), term.get(ctx.rba_term(1))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_inequality(nl.cwi.reo.
	 * interpret.ReoParser.Rba_inequalityContext)
	 */
	public void exitRba_inequality(Rba_inequalityContext ctx) {
		rba_formula.put(ctx, new Negation(new Equality(term.get(ctx.rba_term(0)), term.get(ctx.rba_term(1)))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_true(nl.cwi.reo.interpret.
	 * ReoParser.Rba_trueContext)
	 */
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

	/**
	 * Terms.
	 *
	 * @param ctx
	 *            the ctx
	 */

	public void exitRba_nat(Rba_natContext ctx) {
		term.put(ctx, new Function(ctx.getText(), Integer.parseInt(ctx.getText()), null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_bool(nl.cwi.reo.interpret.
	 * ReoParser.Rba_boolContext)
	 */
	public void exitRba_bool(Rba_boolContext ctx) {
		term.put(ctx, new Function(ctx.getText(), Boolean.parseBoolean(ctx.getText()), null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_string(nl.cwi.reo.interpret.
	 * ReoParser.Rba_stringContext)
	 */
	public void exitRba_string(Rba_stringContext ctx) {
		term.put(ctx, new Function(ctx.getText(), ctx.getText(), null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_decimal(nl.cwi.reo.interpret
	 * .ReoParser.Rba_decimalContext)
	 */
	public void exitRba_decimal(Rba_decimalContext ctx) {
		term.put(ctx, new Function(ctx.getText(), Double.parseDouble(ctx.getText()), null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_function(nl.cwi.reo.
	 * interpret.ReoParser.Rba_functionContext)
	 */
	public void exitRba_function(Rba_functionContext ctx) {
		List<Term> args = new ArrayList<Term>();
		for (Rba_termContext arg : ctx.rba_term()) {
			args.add(term.get(arg));
		}
		term.put(ctx, new Function(ctx.ID().getText(), null, args));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitRba_parameter(nl.cwi.reo.
	 * interpret.ReoParser.Rba_parameterContext)
	 */
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
	public void exitRba_memorycellIn(Rba_memorycellInContext ctx) {
		term.put(ctx, new MemoryVariable(ctx.ID().getText(), false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_memorycellOut(nl.cwi.reo.
	 * interpret.ReoParser.Rba_memorycellOutContext)
	 */
	public void exitRba_memorycellOut(Rba_memorycellOutContext ctx) {
		term.put(ctx, new MemoryVariable(ctx.ID().getText(), true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitRba_null(nl.cwi.reo.interpret.
	 * ReoParser.Rba_nullContext)
	 */
	public void exitRba_null(Rba_nullContext ctx) {
		term.put(ctx, new NullValue());
	}

}
