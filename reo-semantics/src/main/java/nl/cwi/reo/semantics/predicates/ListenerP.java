package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.PListener;
import nl.cwi.reo.interpret.PParser.PContext;
import nl.cwi.reo.interpret.PParser.P_andContext;
import nl.cwi.reo.interpret.PParser.P_argsContext;
import nl.cwi.reo.interpret.PParser.P_booleanContext;
import nl.cwi.reo.interpret.PParser.P_bracketsContext;
import nl.cwi.reo.interpret.PParser.P_decimalContext;
import nl.cwi.reo.interpret.PParser.P_eqsContext;
import nl.cwi.reo.interpret.PParser.P_existsContext;
import nl.cwi.reo.interpret.PParser.P_falseContext;
import nl.cwi.reo.interpret.PParser.P_forallContext;
import nl.cwi.reo.interpret.PParser.P_formContext;
import nl.cwi.reo.interpret.PParser.P_functionContext;
import nl.cwi.reo.interpret.PParser.P_naturalContext;
import nl.cwi.reo.interpret.PParser.P_neqContext;
import nl.cwi.reo.interpret.PParser.P_notContext;
import nl.cwi.reo.interpret.PParser.P_nullContext;
import nl.cwi.reo.interpret.PParser.P_orContext;
import nl.cwi.reo.interpret.PParser.P_relationContext;
import nl.cwi.reo.interpret.PParser.P_stringContext;
import nl.cwi.reo.interpret.PParser.P_termContext;
import nl.cwi.reo.interpret.PParser.P_trueContext;
import nl.cwi.reo.interpret.PParser.P_var_currContext;
import nl.cwi.reo.interpret.PParser.P_var_nextContext;
import nl.cwi.reo.interpret.PParser.P_var_portContext;
import nl.cwi.reo.interpret.PParser.P_variableContext;
import nl.cwi.reo.interpret.listeners.BaseListener;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ListenerP.
 */
public class ListenerP extends BaseListener implements PListener {

	/** The formulas. */
	private ParseTreeProperty<Formula> formulas = new ParseTreeProperty<>();

	/** The terms. */
	private ParseTreeProperty<Term> terms = new ParseTreeProperty<>();

	/** The arguments. */
	private ParseTreeProperty<List<Term>> arguments = new ParseTreeProperty<>();

	/** The variables. */
	private ParseTreeProperty<Variable> variables = new ParseTreeProperty<>();

	/**
	 * Instantiates a new listener P.
	 *
	 * @param m
	 *            the m
	 */
	public ListenerP(Monitor m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterP(nl.cwi.reo.interpret.
	 * ReoParser.PContext)
	 */
	@Override
	public void enterP(PContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP(nl.cwi.reo.interpret.ReoParser
	 * .PContext)
	 */
	@Override
	public void exitP(PContext ctx) {
		atoms.put(ctx, new Predicate(formulas.get(ctx.p_form())));
	}

	/**
	 * Formulas.
	 *
	 * @param ctx
	 *            the ctx
	 */

	@Override
	public void enterP_brackets(P_bracketsContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_brackets(nl.cwi.reo.interpret.
	 * ReoParser.P_bracketsContext)
	 */
	@Override
	public void exitP_brackets(P_bracketsContext ctx) {
		formulas.put(ctx, formulas.get(ctx.p_form()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_exists(nl.cwi.reo.interpret.
	 * ReoParser.P_existsContext)
	 */
	@Override
	public void enterP_exists(P_existsContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_exists(nl.cwi.reo.interpret.
	 * ReoParser.P_existsContext)
	 */
	@Override
	public void exitP_exists(P_existsContext ctx) {
		formulas.put(ctx, new Existential(variables.get(ctx.p_var()), formulas.get(ctx.p_form())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_forall(nl.cwi.reo.interpret.
	 * ReoParser.P_forallContext)
	 */
	@Override
	public void enterP_forall(P_forallContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_forall(nl.cwi.reo.interpret.
	 * ReoParser.P_forallContext)
	 */
	@Override
	public void exitP_forall(P_forallContext ctx) {
		formulas.put(ctx, new Universal(variables.get(ctx.p_var()), formulas.get(ctx.p_form())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_not(nl.cwi.reo.interpret.
	 * ReoParser.P_notContext)
	 */
	@Override
	public void enterP_not(P_notContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitP_not(nl.cwi.reo.interpret.
	 * ReoParser.P_notContext)
	 */
	@Override
	public void exitP_not(P_notContext ctx) {
		formulas.put(ctx, new Negation(formulas.get(ctx.p_form())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_and(nl.cwi.reo.interpret.
	 * ReoParser.P_andContext)
	 */
	@Override
	public void enterP_and(P_andContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitP_and(nl.cwi.reo.interpret.
	 * ReoParser.P_andContext)
	 */
	@Override
	public void exitP_and(P_andContext ctx) {
		List<Formula> list = new ArrayList<Formula>();
		for (P_formContext x : ctx.p_form())
			list.add(formulas.get(x));
		formulas.put(ctx, Formulas.conjunction(list));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#enterP_or(nl.cwi.reo.interpret.
	 * ReoParser.P_orContext)
	 */
	@Override
	public void enterP_or(P_orContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitP_or(nl.cwi.reo.interpret.
	 * ReoParser.P_orContext)
	 */
	@Override
	public void exitP_or(P_orContext ctx) {
		List<Formula> list = new ArrayList<Formula>();
		for (P_formContext x : ctx.p_form())
			list.add(formulas.get(x));
		formulas.put(ctx, new Disjunction(list));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_eqs(nl.cwi.reo.interpret.
	 * ReoParser.P_eqsContext)
	 */
	@Override
	public void enterP_eqs(P_eqsContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitP_eqs(nl.cwi.reo.interpret.
	 * ReoParser.P_eqsContext)
	 */
	@Override
	public void exitP_eqs(P_eqsContext ctx) {
		List<Formula> list = new ArrayList<Formula>();
		for (int i = 0; i < ctx.p_term().size() - 1; i++)
			list.add(new Equality(terms.get(ctx.p_term(i)), terms.get(ctx.p_term(i + 1))));
		formulas.put(ctx, Formulas.conjunction(list));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_neq(nl.cwi.reo.interpret.
	 * ReoParser.P_neqContext)
	 */
	@Override
	public void enterP_neq(P_neqContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.ReoBaseListener#exitP_neq(nl.cwi.reo.interpret.
	 * ReoParser.P_neqContext)
	 */
	@Override
	public void exitP_neq(P_neqContext ctx) {
		formulas.put(ctx, new Negation(new Equality(terms.get(ctx.p_term(0)), terms.get(ctx.p_term(1)))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_relation(nl.cwi.reo.interpret
	 * .ReoParser.P_relationContext)
	 */
	@Override
	public void enterP_relation(P_relationContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_relation(nl.cwi.reo.interpret.
	 * ReoParser.P_relationContext)
	 */
	@Override
	public void exitP_relation(P_relationContext ctx) {
		formulas.put(ctx, new Relation(ctx.getText(), arguments.get(ctx.p_args()), false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_true(nl.cwi.reo.interpret.
	 * ReoParser.P_trueContext)
	 */
	@Override
	public void enterP_true(P_trueContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_true(nl.cwi.reo.interpret.
	 * ReoParser.P_trueContext)
	 */
	@Override
	public void exitP_true(P_trueContext ctx) {
		formulas.put(ctx, new TruthValue(true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_false(nl.cwi.reo.interpret.
	 * ReoParser.P_falseContext)
	 */
	@Override
	public void enterP_false(P_falseContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_false(nl.cwi.reo.interpret.
	 * ReoParser.P_falseContext)
	 */
	@Override
	public void exitP_false(P_falseContext ctx) {
		formulas.put(ctx, new TruthValue(false));
	}

	/**
	 * Terms.
	 *
	 * @param ctx
	 *            the ctx
	 */

	@Override
	public void enterP_variable(P_variableContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_variable(nl.cwi.reo.interpret.
	 * ReoParser.P_variableContext)
	 */
	@Override
	public void exitP_variable(P_variableContext ctx) {
		terms.put(ctx, variables.get(ctx.p_var()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_null(nl.cwi.reo.interpret.
	 * ReoParser.P_nullContext)
	 */
	@Override
	public void enterP_null(P_nullContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_null(nl.cwi.reo.interpret.
	 * ReoParser.P_nullContext)
	 */
	@Override
	public void exitP_null(P_nullContext ctx) {
		terms.put(ctx, Terms.Null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_natural(nl.cwi.reo.interpret.
	 * ReoParser.P_naturalContext)
	 */
	@Override
	public void enterP_natural(P_naturalContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_natural(nl.cwi.reo.interpret.
	 * ReoParser.P_naturalContext)
	 */
	@Override
	public void exitP_natural(P_naturalContext ctx) {
		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Integer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_boolean(nl.cwi.reo.interpret.
	 * ReoParser.P_booleanContext)
	 */
	@Override
	public void enterP_boolean(P_booleanContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_boolean(nl.cwi.reo.interpret.
	 * ReoParser.P_booleanContext)
	 */
	@Override
	public void exitP_boolean(P_booleanContext ctx) {
		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Boolean));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_string(nl.cwi.reo.interpret.
	 * ReoParser.P_stringContext)
	 */
	@Override
	public void enterP_string(P_stringContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_string(nl.cwi.reo.interpret.
	 * ReoParser.P_stringContext)
	 */
	@Override
	public void exitP_string(P_stringContext ctx) {
		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.String));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_decimal(nl.cwi.reo.interpret.
	 * ReoParser.P_decimalContext)
	 */
	@Override
	public void enterP_decimal(P_decimalContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_decimal(nl.cwi.reo.interpret.
	 * ReoParser.P_decimalContext)
	 */
	@Override
	public void exitP_decimal(P_decimalContext ctx) {
		terms.put(ctx, new Function(ctx.getText(), new ArrayList<>(), false, TypeTags.Decimal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_function(nl.cwi.reo.interpret
	 * .ReoParser.P_functionContext)
	 */
	@Override
	public void enterP_function(P_functionContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_function(nl.cwi.reo.interpret.
	 * ReoParser.P_functionContext)
	 */
	@Override
	public void exitP_function(P_functionContext ctx) {
		List<Term> args = arguments.get(ctx.p_args());
		TypeTag tag = args.get(0).getTypeTag();
		terms.put(ctx, new Function(ctx.getText(), args, false, tag));
	}

	/**
	 * Arguments.
	 *
	 * @param ctx
	 *            the ctx
	 */

	@Override
	public void enterP_args(P_argsContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_args(nl.cwi.reo.interpret.
	 * ReoParser.P_argsContext)
	 */
	@Override
	public void exitP_args(P_argsContext ctx) {
		List<Term> list = new ArrayList<Term>();
		for (P_termContext x : ctx.p_term())
			list.add(terms.get(x));
		arguments.put(ctx, list);
	}

	/**
	 * Variables.
	 *
	 * @param ctx
	 *            the ctx
	 */

	@Override
	public void enterP_var_port(P_var_portContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_var_port(nl.cwi.reo.interpret.
	 * ReoParser.P_var_portContext)
	 */
	@Override
	public void exitP_var_port(P_var_portContext ctx) {
		variables.put(ctx, new PortVariable(new Port(ctx.ID().getText())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_var_curr(nl.cwi.reo.interpret
	 * .ReoParser.P_var_currContext)
	 */
	@Override
	public void enterP_var_curr(P_var_currContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_var_curr(nl.cwi.reo.interpret.
	 * ReoParser.P_var_currContext)
	 */
	@Override
	public void exitP_var_curr(P_var_currContext ctx) {
		variables.put(ctx, new MemoryVariable(ctx.ID().getText(), false, TypeTags.Object));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#enterP_var_next(nl.cwi.reo.interpret
	 * .ReoParser.P_var_nextContext)
	 */
	@Override
	public void enterP_var_next(P_var_nextContext ctx) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.interpret.ReoBaseListener#exitP_var_next(nl.cwi.reo.interpret.
	 * ReoParser.P_var_nextContext)
	 */
	@Override
	public void exitP_var_next(P_var_nextContext ctx) {
		variables.put(ctx, new MemoryVariable(ctx.getText(), true, TypeTags.Object));
	}

}
