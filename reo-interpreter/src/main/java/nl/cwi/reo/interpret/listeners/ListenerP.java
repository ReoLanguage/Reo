package nl.cwi.reo.interpret.listeners;

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
import nl.cwi.reo.interpret.ReoParser.AtomContext;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Predicate;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Universal;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

public class ListenerP extends Listener<Predicate> implements PListener {

	private ParseTreeProperty<Predicate> predicate = new ParseTreeProperty<Predicate>();
	private ParseTreeProperty<Formula> formulas = new ParseTreeProperty<Formula>();
	private ParseTreeProperty<Term> terms = new ParseTreeProperty<Term>();
	private ParseTreeProperty<List<Term>> arguments = new ParseTreeProperty<List<Term>>();
	private ParseTreeProperty<Variable> variables = new ParseTreeProperty<Variable>();

	public ListenerP(Monitor m) {
		super(m);
	}

	/**
	 * Predicates
	 */

	public void exitAtom(AtomContext ctx) {
		atoms.put(ctx, predicate.get(ctx.p()));
	}

	@Override
	public void enterP(PContext ctx) {
	}

	@Override
	public void exitP(PContext ctx) {
		predicate.put(ctx, new Predicate(formulas.get(ctx.p_form())));
	}

	/**
	 * Formulas
	 */

	@Override
	public void enterP_brackets(P_bracketsContext ctx) {
	}

	@Override
	public void exitP_brackets(P_bracketsContext ctx) {
		formulas.put(ctx, formulas.get(ctx.p_form()));
	}

	@Override
	public void enterP_exists(P_existsContext ctx) {
	}

	@Override
	public void exitP_exists(P_existsContext ctx) {
		formulas.put(ctx, new Existential(variables.get(ctx.p_var()), formulas.get(ctx.p_form())));
	}

	@Override
	public void enterP_forall(P_forallContext ctx) {
	}

	@Override
	public void exitP_forall(P_forallContext ctx) {
		formulas.put(ctx, new Universal(variables.get(ctx.p_var()), formulas.get(ctx.p_form())));
	}

	@Override
	public void enterP_not(P_notContext ctx) {
	}

	@Override
	public void exitP_not(P_notContext ctx) {
		formulas.put(ctx, new Negation(formulas.get(ctx.p_form())));
	}

	@Override
	public void enterP_and(P_andContext ctx) {
	}

	@Override
	public void exitP_and(P_andContext ctx) {
		List<Formula> list = new ArrayList<Formula>();
		for (P_formContext x : ctx.p_form())
			list.add(formulas.get(x));
		formulas.put(ctx, new Conjunction(list));
	}

	@Override
	public void enterP_or(P_orContext ctx) {
	}

	@Override
	public void exitP_or(P_orContext ctx) {
		List<Formula> list = new ArrayList<Formula>();
		for (P_formContext x : ctx.p_form())
			list.add(formulas.get(x));
		formulas.put(ctx, new Disjunction(list));
	}

	@Override
	public void enterP_eqs(P_eqsContext ctx) {
	}

	@Override
	public void exitP_eqs(P_eqsContext ctx) {
		List<Formula> list = new ArrayList<Formula>();
		for (int i = 0; i < ctx.p_term().size() - 1; i++)
			list.add(new Equality(terms.get(ctx.p_term(i)), terms.get(ctx.p_term(i + 1))));
		formulas.put(ctx, new Conjunction(list));
	}

	@Override
	public void enterP_neq(P_neqContext ctx) {
	}

	@Override
	public void exitP_neq(P_neqContext ctx) {
		formulas.put(ctx, new Negation(new Equality(terms.get(ctx.p_term(0)), terms.get(ctx.p_term(1)))));
	}

	@Override
	public void enterP_relation(P_relationContext ctx) {
	}

	@Override
	public void exitP_relation(P_relationContext ctx) {
		formulas.put(ctx, new Relation(ctx.getText(), arguments.get(ctx.p_args())));
	}

	@Override
	public void enterP_true(P_trueContext ctx) {
	}

	@Override
	public void exitP_true(P_trueContext ctx) {
		formulas.put(ctx, new Relation("truthvalue", new Boolean(true), new ArrayList<Term>()));
	}

	@Override
	public void enterP_false(P_falseContext ctx) {
	}

	@Override
	public void exitP_false(P_falseContext ctx) {
		formulas.put(ctx, new Relation("truthvalue", new Boolean(false), new ArrayList<Term>()));
	}

	/**
	 * Terms
	 */

	@Override
	public void enterP_variable(P_variableContext ctx) {
	}

	@Override
	public void exitP_variable(P_variableContext ctx) {
		terms.put(ctx, variables.get(ctx.p_var()));
	}

	@Override
	public void enterP_null(P_nullContext ctx) {
	}

	@Override
	public void exitP_null(P_nullContext ctx) {
		terms.put(ctx, new Function("constant", null, new ArrayList<Term>()));
	}

	@Override
	public void enterP_natural(P_naturalContext ctx) {
	}

	@Override
	public void exitP_natural(P_naturalContext ctx) {
		terms.put(ctx, new Function("constant", Integer.parseInt(ctx.getText()), new ArrayList<Term>()));
	}

	@Override
	public void enterP_boolean(P_booleanContext ctx) {
	}

	@Override
	public void exitP_boolean(P_booleanContext ctx) {
		terms.put(ctx, new Function("constant", Boolean.parseBoolean(ctx.getText()), new ArrayList<Term>()));
	}

	@Override
	public void enterP_string(P_stringContext ctx) {
	}

	@Override
	public void exitP_string(P_stringContext ctx) {
		terms.put(ctx, new Function("constant", ctx.getText(), new ArrayList<Term>()));
	}

	@Override
	public void enterP_decimal(P_decimalContext ctx) {
	}

	@Override
	public void exitP_decimal(P_decimalContext ctx) {
		terms.put(ctx, new Function("constant", Double.parseDouble(ctx.getText()), new ArrayList<Term>()));
	}

	@Override
	public void enterP_function(P_functionContext ctx) {
	}

	@Override
	public void exitP_function(P_functionContext ctx) {
		terms.put(ctx, new Function(ctx.getText(), arguments.get(ctx.p_args())));
	}

	/**
	 * Arguments
	 */

	@Override
	public void enterP_args(P_argsContext ctx) {
	}

	@Override
	public void exitP_args(P_argsContext ctx) {
		List<Term> list = new ArrayList<Term>();
		for (P_termContext x : ctx.p_term())
			list.add(terms.get(x));
		arguments.put(ctx, list);
	}

	/**
	 * Variables
	 */

	@Override
	public void enterP_var_port(P_var_portContext ctx) {
	}

	@Override
	public void exitP_var_port(P_var_portContext ctx) {
		variables.put(ctx, new Node(new Port(ctx.ID().getText())));
	}

	@Override
	public void enterP_var_curr(P_var_currContext ctx) {
	}

	@Override
	public void exitP_var_curr(P_var_currContext ctx) {
		variables.put(ctx, new MemCell(ctx.ID().getText(), false));
	}

	@Override
	public void enterP_var_next(P_var_nextContext ctx) {
	}

	@Override
	public void exitP_var_next(P_var_nextContext ctx) {
		variables.put(ctx, new MemCell(ctx.getText(), true));
	}

}
