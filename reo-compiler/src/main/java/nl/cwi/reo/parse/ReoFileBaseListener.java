package nl.cwi.reo.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl.cwi.reo.semantics.Program;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.semantics.Program}.
 */
public class ReoFileBaseListener extends ReoBaseListener implements ReoFileListener {
	
	/**
	 * Complete Reo program.
	 */
	private Program program = new Program();
	
	/**
	 * Component definitions.
	 */
	private HashMap<String, Definition> definitions = new HashMap<String, Definition>();
	
	/**
	 * Abstract components
	 */
	private ParseTreeProperty<Component> components = new ParseTreeProperty<Component>();
	
	/**
	 * Atomic components
	 */
	private ParseTreeProperty<Object> atoms = new ParseTreeProperty<Object>();
	
	/**
	 * Component interfaces
	 */
	private ParseTreeProperty<Interface> interfaces = new ParseTreeProperty<Interface>();
	
	/**
	 * Input ports
	 */
	private ParseTreeProperty<String> inputs = new ParseTreeProperty<String>();
	
	/**
	 * Output ports
	 */
	private ParseTreeProperty<String> outputs = new ParseTreeProperty<String>();
	
	/**
	 * Expressions
	 */
	private ParseTreeProperty<Expression> expressions = new ParseTreeProperty<Expression>();
	
	/**
	 * Gets the parsed Reo program.
	 * @return parsed Reo program
	 */
	public Program getProgram() {
		return this.program;
	}
	
	/**
	 * Annotates a atomic component to a parse tree node.
	 * @param node		parse tree node
	 * @param value 	atomic component
	 */
	public void setAtom(ParseTree node, Object value) {
		this.atoms.put(node, value);
	}
	
	/*
	 * 
	 * 
	 * FILE
	 * 
	 * 
	 */
	
	/**
	 * Gets all component instances in the file and combines them to a {@link nl.cwi.reo.semantics.Program}.
	 */
	@Override public void exitFile(@NotNull ReoParser.FileContext ctx) { 
		try {
			NodeGenerator gen = new NodeGenerator();
			for (ReoParser.CompContext comp_ctx : ctx.comp()) {
				Component C = components.get(comp_ctx);
				Program program = C.getProgram(new HashMap<String, String>(), gen);
				this.program.add(program);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * 
	 * 
	 * DEFINITIONS
	 * 
	 * 
	 */

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Definition} that models 
	 * an atomic definition, if the semantics of the atomic component is available.
	 * @throws NullPointerException if the parse tree is not labeled with an atomic component.
	 */
	@Override public void exitDefnAtomic(@NotNull ReoParser.DefnAtomicContext ctx) {
		String name = ctx.ID().getText();		
		List<String> params = new ArrayList<String>();
		if (ctx.params() != null) 
			for (TerminalNode id : ctx.params().ID())
				params.add(id.getText());
		List<String> intface = new ArrayList<String>();
		Set<String> input = new HashSet<String>();
		for (ReoParser.PortContext port_ctx : ctx.portset().port()) {
			String inputport;
			if ((inputport = inputs.get(port_ctx)) != null) {
				input.add(inputport);
				intface.add(inputport);
			}
			String outputport;
			if ((outputport = outputs.get(port_ctx)) != null) {
				intface.add(outputport);
			}
		}
		Object atom = atoms.get(ctx.atom());
		if (atom == null) throw new NullPointerException("Annotate the parse tree with an atomic component.");
		definitions.put(name, new DefinitionAtomic(name, params, intface, input, atom));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Definition} that models a composed definition.
	 */
	@Override public void exitDefnComposed(@NotNull ReoParser.DefnComposedContext ctx) {
		String name = ctx.ID().getText();
		List<String> params = new ArrayList<String>();
		if (ctx.params() != null)
			for (TerminalNode id : ctx.params().ID())
				params.add(id.getText());
		Interface intface = interfaces.get(ctx.nodeset());
		Set<Component> comps = new HashSet<Component>();
		for (ReoParser.CompContext comp_ctx : ctx.comp())
			comps.add(components.get(comp_ctx));
		definitions.put(name, new DefinitionComposite(params, intface, comps));
	}

	/*
	 * 
	 * 
	 * COMPONENTS
	 * 
	 * 
	 */

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Component} that models a reference.
	 */
	@Override public void exitCompReference(@NotNull ReoParser.CompReferenceContext ctx) {
		String name = ctx.ID().getText();
		List<String> parameters = new ArrayList<String>(); 
		if (ctx.assign() != null) 
			for (ReoParser.ValueContext value_ctx : ctx.assign().value())
				parameters.add(value_ctx.getText());
		Interface intface = interfaces.get(ctx.nodeset());
		Definition definition;
		if ((definition = definitions.get(name)) == null)
			System.err.println("[ERROR] component " + ctx.ID().getText() + " is not defined in any supported semantics.");
		else
			components.put(ctx, new ComponentReference(parameters, intface, definition));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Component} that models a for loop.
	 */
	@Override public void exitCompForLoop(@NotNull ReoParser.CompForLoopContext ctx) {
		String parameter = ctx.ID().getText();
		Expression lower = expressions.get(ctx.expr(0));
		Expression upper = expressions.get(ctx.expr(1));
		Set<Component> comps = new HashSet<Component>();
		for (ReoParser.CompContext comp_ctx : ctx.comp()) 
			comps.add(components.get(comp_ctx));
		components.put(ctx, new ComponentForLoop(parameter, lower, upper, comps));
	}
	
	/*
	 * 
	 * 
	 * INTERFACES
	 * 
	 * 
	 */
	
	/**
	 * Annotates the parse tree with an {@link java.lang.String} that models 
	 * an input port.
	 */
	@Override public void exitPortInput(@NotNull ReoParser.PortInputContext ctx) {
		inputs.put(ctx, ctx.ID().getText());
	}

	/**
	 * Annotates the parse tree with an {@link java.lang.String} that models 
	 * an output port.
	 */
	@Override public void exitPortOutput(@NotNull ReoParser.PortOutputContext ctx) {
		outputs.put(ctx, ctx.ID().getText());		
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Definition} that models 
	 * an composed definition.
	 */
	@Override public void exitNodesName(@NotNull ReoParser.NodesNameContext ctx) { 
		interfaces.put(ctx, new Interface(ctx.ID().getText()));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Interface} that models 
	 * an interface.
	 */
	@Override public void exitNodeset(@NotNull ReoParser.NodesetContext ctx) { 
		Interface intface = new Interface();
		for (ReoParser.NodesContext node_ctx : ctx.nodes())
			intface = Interface.join(intface, interfaces.get(node_ctx));
		interfaces.put(ctx, intface);
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Interface} that models 
	 * a singleton interface.
	 */
	@Override public void exitNodesIndex(@NotNull ReoParser.NodesIndexContext ctx) {
		String node = ctx.ID().getText();
		Expression expr = expressions.get(ctx.expr());
		interfaces.put(ctx, new Interface(node, expr));
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Interface} that models 
	 * a range interface.
	 */
	@Override public void exitNodesRange(@NotNull ReoParser.NodesRangeContext ctx) {
		String node = ctx.ID().getText();
		Expression e1 = expressions.get(ctx.expr(0));
		Expression e2 = expressions.get(ctx.expr(1));
		interfaces.put(ctx, new Interface(node, e1, e2)); 
	}
	
	/*
	 * 
	 * 
	 * EXPRESSIONS
	 * 
	 * 
	 */

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Expression} that models 
	 * a parameter.
	 */
	@Override public void exitExprParameter(@NotNull ReoParser.ExprParameterContext ctx) { 
		String param = ctx.ID().getText();
		Expression e = new Expression(param);
		expressions.put(ctx, e);
	}
	
	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Expression} that models INT.
	 */
	@Override public void exitExprInteger(@NotNull ReoParser.ExprIntegerContext ctx) { 
		int constant = Integer.parseInt(ctx.INT().getText());
		Expression e = new Expression(constant);
		expressions.put(ctx, e);
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Expression} that models 
	 * a scalar multiplication.
	 */
	@Override public void exitExprScalar(@NotNull ReoParser.ExprScalarContext ctx) { 
		int scalar = Integer.parseInt(ctx.INT().getText());
		String param = ctx.ID().getText();
		Expression e = Expression.multiply(new Expression(param), scalar);
		System.out.println("Expression c*e:" + e);
		expressions.put(ctx, e);
	}
	
	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Expression} that models 
	 * a unary minus operation.
	 */
	@Override public void exitExprUnaryMin(@NotNull ReoParser.ExprUnaryMinContext ctx) { 
		Expression e1 = expressions.get(ctx.expr());
		Expression e = Expression.multiply(e1, -1);
		System.out.println("Expression -e: " + e);
		expressions.put(ctx, e);		
	}
	
	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Expression} that models
	 * expression difference.
	 */
	@Override public void exitExprDifference(@NotNull ReoParser.ExprDifferenceContext ctx) {
		Expression e1 = expressions.get(ctx.expr(0));
		Expression e2 = expressions.get(ctx.expr(1));
		Expression e = Expression.add(e1, Expression.multiply(e2, -1));
		expressions.put(ctx, e);
	}

	/**
	 * Annotates the parse tree with an {@link nl.cwi.reo.parse.Expression} that models
	 * expression difference.
	 */
	@Override public void exitExprAddition(@NotNull ReoParser.ExprAdditionContext ctx) {
		Expression e1 = expressions.get(ctx.expr(0));
		Expression e2 = expressions.get(ctx.expr(1));
		Expression e = Expression.add(e1, e2);
		expressions.put(ctx, e);		
	}
}
