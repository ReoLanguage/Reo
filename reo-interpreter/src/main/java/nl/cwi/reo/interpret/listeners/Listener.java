package nl.cwi.reo.interpret.listeners;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.antlr.v4.runtime.tree.ParseTreeProperty;

import nl.cwi.reo.interpret.components.ComponentDefinition;
import nl.cwi.reo.interpret.components.ComponentAtomic;
import nl.cwi.reo.interpret.components.ComponentComposite;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.components.ComponentVariable;
import nl.cwi.reo.interpret.connectors.Component;
import nl.cwi.reo.interpret.connectors.Connector;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SourceCode;
import nl.cwi.reo.interpret.instances.InstanceAtomic;
import nl.cwi.reo.interpret.instances.InstanceComposite;
import nl.cwi.reo.interpret.instances.InstancesExpression;
import nl.cwi.reo.interpret.instances.Set;
import nl.cwi.reo.interpret.nodes.NodeExpression;
import nl.cwi.reo.interpret.parameters.ParameterExpression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortExpression;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.predicates.BooleanPredicate;
import nl.cwi.reo.interpret.predicates.ComponentdefPredicate;
import nl.cwi.reo.interpret.predicates.Conjunction;
import nl.cwi.reo.interpret.predicates.Disjunction;
import nl.cwi.reo.interpret.predicates.Membership;
import nl.cwi.reo.interpret.predicates.Negation;
import nl.cwi.reo.interpret.predicates.PredicateExpression;
import nl.cwi.reo.interpret.predicates.Relation;
import nl.cwi.reo.interpret.predicates.RelationSymbol;
import nl.cwi.reo.interpret.predicates.StructdefnPredicate;
import nl.cwi.reo.interpret.predicates.VariablePredicate;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.terms.Function;
import nl.cwi.reo.interpret.terms.FunctionSymbol;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermComponent;
import nl.cwi.reo.interpret.terms.TermInstance;
import nl.cwi.reo.interpret.terms.TermList;
import nl.cwi.reo.interpret.terms.TermVariable;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.VariableListExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;
import nl.cwi.reo.interpret.ReoBaseListener;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoParser.Component_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Component_compositeContext;
import nl.cwi.reo.interpret.ReoParser.Component_variableContext;
import nl.cwi.reo.interpret.ReoParser.FileContext;
import nl.cwi.reo.interpret.ReoParser.Formula_binaryrelationContext;
import nl.cwi.reo.interpret.ReoParser.Formula_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Formula_componentdefnContext;
import nl.cwi.reo.interpret.ReoParser.Formula_conjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Formula_disjunctionContext;
import nl.cwi.reo.interpret.ReoParser.Formula_existentialContext;
import nl.cwi.reo.interpret.ReoParser.Formula_membershipContext;
import nl.cwi.reo.interpret.ReoParser.Formula_negationContext;
import nl.cwi.reo.interpret.ReoParser.Formula_structdefnContext;
import nl.cwi.reo.interpret.ReoParser.Formula_universalContext;
import nl.cwi.reo.interpret.ReoParser.Formula_variableContext;
import nl.cwi.reo.interpret.ReoParser.ImpsContext;
import nl.cwi.reo.interpret.ReoParser.Instance_atomicContext;
import nl.cwi.reo.interpret.ReoParser.Instance_compositionContext;
import nl.cwi.reo.interpret.ReoParser.Instance_productContext;
import nl.cwi.reo.interpret.ReoParser.Instance_semicolonContext;
import nl.cwi.reo.interpret.ReoParser.Instance_sumContext;
import nl.cwi.reo.interpret.ReoParser.ListContext;
import nl.cwi.reo.interpret.ReoParser.MultisetContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_constraintContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_elseContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_setbuilderContext;
import nl.cwi.reo.interpret.ReoParser.Multiset_withoutContext;
import nl.cwi.reo.interpret.ReoParser.NodeContext;
import nl.cwi.reo.interpret.ReoParser.NodesContext;
import nl.cwi.reo.interpret.ReoParser.ParamContext;
import nl.cwi.reo.interpret.ReoParser.ParamsContext;
import nl.cwi.reo.interpret.ReoParser.PortContext;
import nl.cwi.reo.interpret.ReoParser.PortsContext;
import nl.cwi.reo.interpret.ReoParser.SecnContext;
import nl.cwi.reo.interpret.ReoParser.SignContext;
import nl.cwi.reo.interpret.ReoParser.TermContext;
import nl.cwi.reo.interpret.ReoParser.Term_booleanContext;
import nl.cwi.reo.interpret.ReoParser.Term_bracketsContext;
import nl.cwi.reo.interpret.ReoParser.Term_componentdefnContext;
import nl.cwi.reo.interpret.ReoParser.Term_decimalContext;
import nl.cwi.reo.interpret.ReoParser.Term_exponentContext;
import nl.cwi.reo.interpret.ReoParser.Term_instanceContext;
import nl.cwi.reo.interpret.ReoParser.Term_listContext;
import nl.cwi.reo.interpret.ReoParser.Term_naturalContext;
import nl.cwi.reo.interpret.ReoParser.Term_operationContext;
import nl.cwi.reo.interpret.ReoParser.Term_stringContext;
import nl.cwi.reo.interpret.ReoParser.Term_unaryminContext;
import nl.cwi.reo.interpret.ReoParser.Term_variableContext;
import nl.cwi.reo.interpret.ReoParser.TypeContext;
import nl.cwi.reo.interpret.ReoParser.VarContext;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
 * @param <O>
 */
public class Listener<T extends Semantics<T>> extends ReoBaseListener {
	
	// Symbol table
	private ParseTreeProperty<Map<String, String>> symbols = new ParseTreeProperty<Map<String, String>>();
	
	public boolean hasErrors = false;
	
	// File structure
	private ReoFile<T> program;
	private String section = "";	
	private List<String> imports = new ArrayList<String>();
	
	// Components
	private ParseTreeProperty<ComponentExpression<T>> components = new ParseTreeProperty<ComponentExpression<T>>();
	
	// Blocks
	
	// Formula
	private ParseTreeProperty<PredicateExpression> formula = new ParseTreeProperty<PredicateExpression>();
	
	// Instance
	private ParseTreeProperty<InstancesExpression<T>> instances = new ParseTreeProperty<InstancesExpression<T>>();

	// Multiset
	private ParseTreeProperty<InstancesExpression<T>> set = new ParseTreeProperty<InstancesExpression<T>>();	
	
	// Boolean expressions
//	private ParseTreeProperty<BooleanExpression> bools = new ParseTreeProperty<BooleanExpression>();
	
		
	// Signatures
	private ParseTreeProperty<SignatureExpression> signatureExpressions = new ParseTreeProperty<SignatureExpression>();
	private ParseTreeProperty<List<ParameterExpression>> parameterlists = new ParseTreeProperty<List<ParameterExpression>>();
	private ParseTreeProperty<ParameterExpression> parameters = new ParseTreeProperty<ParameterExpression>();
//	private ParseTreeProperty<ParameterType> parametertypes = new ParseTreeProperty<ParameterType>();
	private ParseTreeProperty<List<NodeExpression>> nodelists = new ParseTreeProperty<List<NodeExpression>>();
	private ParseTreeProperty<NodeExpression> nodes = new ParseTreeProperty<NodeExpression>();
	
	// Type tags for uninterpreted data
	private ParseTreeProperty<TypeTag> typetags = new ParseTreeProperty<TypeTag>();

	// Interface instantiation
	private ParseTreeProperty<PortExpression> portList = new ParseTreeProperty<PortExpression>();
	private ParseTreeProperty<List<PortExpression>> interfaces = new ParseTreeProperty<List<PortExpression>>();
	       
	// Variables
	private ParseTreeProperty<VariableExpression> variables = new ParseTreeProperty<VariableExpression>();	
	
	// Term Expression
	private ParseTreeProperty<TermsExpression> terms = new ParseTreeProperty<TermsExpression>();

	// Terms Expression list :
	private ParseTreeProperty<List<TermsExpression>> termsList = new ParseTreeProperty<List<TermsExpression>>();

	// Semantics
	protected ParseTreeProperty<T> atoms = new ParseTreeProperty<T>();
	
	/**
	 * Gets the program expression.
	 * @return program expression
	 */
	public ReoFile<T> getMain() {
		return program;
	}

	/**
	 * File structure
	 */
	
	@Override
	public void enterFile(FileContext ctx) {
		Map<String, String> s = new HashMap<String, String>();
		s.put(ctx.ID().getText(), "component");
		symbols.put(ctx.component(), s);
	}
	
	@Override
	public void exitFile(FileContext ctx) {
		program = new ReoFile<T>(section, imports, ctx.ID().getText(), components.get(ctx.component()), ctx.ID().getSymbol());
	}

	@Override
	public void exitSecn(SecnContext ctx) {
		section = ctx.name().getText();
	}

	@Override
	public void exitImps(ImpsContext ctx) {
		imports.add(ctx.name().getText());
	}

	/**
	 * Components
	 * 
	 */
	@Override
	public void exitComponent_variable(Component_variableContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		components.put(ctx, new ComponentVariable<T>(var));
	}

	@Override
	public void exitComponent_atomic(Component_atomicContext ctx) {
		T atom = atoms.get(ctx.atom());
		SourceCode s=null;

		if (atom == null) {
			hasErrors = true;
			System.err.println(new Message(MessageType.ERROR, "Undefined semantics."));
		} else {
			if((ctx.source()!=null)){
				ctx.source().LANG().toString().toUpperCase();
				s=new SourceCode(ctx.source().STRING().toString(),ctx.source().LANG().toString().toUpperCase());
				}
		}
		components.put(ctx, new ComponentAtomic<T>(signatureExpressions.get(ctx.sign()), atom, s));
	}

	@Override
	public void exitComponent_composite(Component_compositeContext ctx) {
		//TODO : check if cast works
		components.put(ctx, new ComponentComposite<T>(signatureExpressions.get(ctx.sign()), (Set<T>) instances.get(ctx.multiset())));		
	}
	
	/**
	 * Multiset
	 */ 
	
	@Override
	public void exitMultiset_constraint(Multiset_constraintContext ctx) {
		InstancesExpression<T> i = instances.get(ctx.instance());
		set.put(ctx, new Set<T>(null, Arrays.asList(i), null));
	}
	
	@Override
	public void exitMultiset_setbuilder(Multiset_setbuilderContext ctx) {
		
		List<InstancesExpression<T>> stmtlist = new ArrayList<InstancesExpression<T>>();
		for (MultisetContext stmt_ctx : ctx.multiset())
			stmtlist.add(set.get(stmt_ctx));
		
		set.put(ctx, new Set<T>( terms.get(ctx.term()),stmtlist,formula.get(ctx.formula())));
	}
	
	@Override
	public void exitMultiset_else(Multiset_elseContext ctx) {
		InstancesExpression<T> m1 = set.get(ctx.multiset(0));
		InstancesExpression<T> m2 = set.get(ctx.multiset(0));
		
		List<InstancesExpression<T>> l = Arrays.asList(m1,m2);
		
		set.put(ctx, new Set<T>(new StringValue("+"),l,null));
	}
	
	@Override
	public void exitMultiset_without(Multiset_withoutContext ctx) {
		InstancesExpression<T> m1 = set.get(ctx.multiset(0));
		InstancesExpression<T> m2 = set.get(ctx.multiset(0));
		
		List<InstancesExpression<T>> l = Arrays.asList(m1,m2);
		
		set.put(ctx, new Set<T>(new StringValue("-"),l,null));
	}

	/**
	 * Instance
	 * @param ctx
	 */
	@Override
	public void exitInstance_atomic(Instance_atomicContext ctx) {
		ComponentExpression<T> cexpr = components.get(ctx.component());
		List<TermsExpression> list = termsList.get(ctx.list());
		if (list == null) list = new ArrayList<TermsExpression>();
		
		List<VariableExpression> v = new ArrayList<VariableExpression>();
		
		for(PortExpression p : interfaces.get(ctx.ports())){
			v.add(p);
		}
		VariableListExpression var = new VariableListExpression(v);
		instances.put(ctx, new InstanceAtomic<T>(cexpr, new TermList(list),var));
	}
	
	@Override
	public void exitInstance_composition(Instance_compositionContext ctx) {
		InstancesExpression<T> i1 = instances.get(ctx.instance(0));
		InstancesExpression<T> i2 = instances.get(ctx.instance(1));
		TermsExpression term = terms.get(ctx.term());
		instances.put(ctx, new InstanceComposite<T>(term,i1,i2));
	}
	
	@Override
	public void exitInstance_product(Instance_productContext ctx) {
		InstancesExpression<T> i1 = instances.get(ctx.instance(0));
		InstancesExpression<T> i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue("*");
		instances.put(ctx, new InstanceComposite<T>(s,i1,i2));
	}

	@Override
	public void exitInstance_sum(Instance_sumContext ctx) {
		InstancesExpression<T> i1 = instances.get(ctx.instance(0));
		InstancesExpression<T> i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue("+");
		instances.put(ctx, new InstanceComposite<T>(s,i1,i2));
	}
	@Override
	public void exitInstance_semicolon(Instance_semicolonContext ctx) {
		InstancesExpression<T> i1 = instances.get(ctx.instance(0));
		InstancesExpression<T> i2 = instances.get(ctx.instance(1));
		StringValue s = new StringValue(";");
		instances.put(ctx, new InstanceComposite<T>(s,i1,i2));
	}
	
	/**
	 * Predicates
	 * @param ctx
	 */
	@Override
	public void exitFormula_boolean(Formula_booleanContext ctx) {
		BooleanPredicate p = new BooleanPredicate(Boolean.parseBoolean(ctx.BOOL().getText()));
		formula.put(ctx, p);
	}
	
	@Override
	public void exitFormula_componentdefn(Formula_componentdefnContext ctx) {
		ComponentdefPredicate<T> p = new ComponentdefPredicate<T>(variables.get(ctx.var()),components.get(ctx.component()));
		formula.put(ctx, p);
	}
	
	@Override
	public void exitFormula_structdefn(Formula_structdefnContext ctx) {
		List<ParameterExpression> param = new ArrayList<ParameterExpression>();
		for(ParamContext p : ctx.param()){
			param.add(parameters.get(p));
		}
		StructdefnPredicate p = new StructdefnPredicate(new Identifier(ctx.ID().toString()),param);
		formula.put(ctx, p);
	}
	
	@Override
	public void exitFormula_membership(Formula_membershipContext ctx) {
		Membership p = new Membership(new Identifier(ctx.ID().toString()),new TermList(termsList.get(ctx.list())));
		formula.put(ctx, p);
	}
	
	@Override
	public void exitFormula_variable(Formula_variableContext ctx) {
		VariablePredicate p = new VariablePredicate(variables.get(ctx.var()));
		formula.put(ctx, p);
	}
	
	@Override
	public void exitFormula_binaryrelation(Formula_binaryrelationContext ctx) {
		List<TermsExpression> l = Arrays.asList(terms.get(ctx.term(0)),terms.get(ctx.term(0)));
		switch(ctx.op.getType()){
		case ReoParser.LEQ:
			formula.put(ctx, new Relation(RelationSymbol.LEQ,l, new Location(ctx.start)));
			break;
		case ReoParser.LT:
			formula.put(ctx, new Relation(RelationSymbol.LT,l,new Location(ctx.start)));
			break;
		case ReoParser.GEQ:
			formula.put(ctx, new Relation(RelationSymbol.GEQ,l,new Location(ctx.start)));
			break;
		case ReoParser.GT:
			formula.put(ctx, new Relation(RelationSymbol.GT,l, new Location(ctx.start)));
			break;
		case ReoParser.EQ:
			formula.put(ctx, new Relation(RelationSymbol.EQ,l, new Location(ctx.start)));
			break;
		case ReoParser.NEQ:
			formula.put(ctx, new Relation(RelationSymbol.NEQ,l, new Location(ctx.start)));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void exitFormula_negation(Formula_negationContext ctx) {
		formula.put(ctx, new Negation(formula.get(ctx.formula())));
	}
//	@Override
//	public void exitFormula_existential(Formula_existentialContext ctx) {
//		List<PredicateExpression> l = Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
//		formula.put(ctx, new Conjunction(l));
//	}
//	@Override
//	public void exitFormula_universal(Formula_universalContext ctx) {
//		formula.put(ctx, new Conjunction(formula.get(ctx.formula())));
//	}
	
	
	@Override
	public void exitFormula_conjunction(Formula_conjunctionContext ctx) {
		List<PredicateExpression> l = Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
		formula.put(ctx, new Conjunction(l));
	}
	@Override
	public void exitFormula_disjunction(Formula_disjunctionContext ctx) {
		List<PredicateExpression> l = Arrays.asList(formula.get(ctx.formula(0)),formula.get(ctx.formula(0)));
		formula.put(ctx, new Disjunction(l));

	}
		

	
	/**
	 * Signatures
	 */

	@Override
	public void exitSign(SignContext ctx) {

		List<VariableExpression> nodes = new ArrayList<VariableExpression>();
		for(NodeExpression n : nodelists.get(ctx.nodes()))
			nodes.add(n);
		List<VariableExpression> parameters = new ArrayList<VariableExpression>();
		for(ParameterExpression p : parameterlists.get(ctx.params()))
			parameters.add(p);

		signatureExpressions.put(ctx, new SignatureExpression(new VariableListExpression(parameters), new VariableListExpression(nodes), new Location(ctx.start)));
	}

	@Override
	public void exitParams(ParamsContext ctx) {
		List<ParameterExpression> list = new ArrayList<ParameterExpression>();
		for (ParamContext param_ctx : ctx.param())
			list.add(parameters.get(param_ctx));
		parameterlists.put(ctx, list);
	}

	@Override
	public void exitParam(ParamContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		TypeTag type = typetags.get(ctx.type());
//		if (type == null) type = new TypeTag(Arrays.asList(""));
		if (type == null) type = new TypeTag("");
		parameters.put(ctx, new ParameterExpression(var, type));
		
		// TODO : add signature option
	}

	@Override
	public void exitNodes(NodesContext ctx) {
		List<NodeExpression> list = new ArrayList<NodeExpression>();
		for (NodeContext node_ctx : ctx.node())
			list.add(nodes.get(node_ctx));
		nodelists.put(ctx, list);
	}

	@Override
	public void exitNode(NodeContext ctx) {
		VariableExpression var = variables.get(ctx.var());
		if (var == null) var = new VariableExpression("",null, new Location(ctx.start));
		TypeTag tag = typetags.get(ctx.type());
		if (tag == null) tag = new TypeTag();
		PortType type = PortType.NONE;
		if (ctx.io != null) {
			switch (ctx.io.getType()) {
			case ReoParser.IN:
				type = PortType.IN;
				break;
			case ReoParser.OUT:
				type = PortType.OUT;
				break;
			case ReoParser.MIX:
				type = PortType.NONE;
				break;
			default:
				break;
			}
		}
		nodes.put(ctx, new NodeExpression(var, type, tag));
	}

	/**
	 * Type tags for uninterpreted data
	 */
	
	@Override
	public void exitType(TypeContext ctx) {
//		String tag = new ArrayList<String>();
//		if(ctx.ID() != null)
//			tag.add(ctx.ID().toString());
//		if(ctx.type()!=null)
//			for(TypeContext t : ctx.type())
//				tag.add(t.toString());
//		typetags.put(ctx, new TypeTag(tag));
		typetags.put(ctx, new TypeTag(ctx.getText()));
	}
	
	/**
	 * Interface instantiation
	 */

	@Override
	public void exitPorts(PortsContext ctx) {
		List<PortExpression> list = new ArrayList<PortExpression>();
		for (PortContext port : ctx.port())
			list.add(portList.get(port));
		interfaces.put(ctx,list);
	}

	@Override
	public void exitPort(PortContext ctx) {
		PrioType prio = PrioType.NONE;
		if (ctx.prio != null)
			prio = ctx.prio.getType() == ReoParser.AND ? PrioType.AMPERSANT : PrioType.PLUS ;
		portList.put(ctx, new PortExpression(prio, variables.get(ctx.var())));
	}

	/**
	 * Variables	
	 */

	@Override
	public void exitVar(VarContext ctx) {		
		String name = ctx.name().getText();	
		for (String imprt : imports) {
			if (imprt.endsWith(name)) {
				name = imprt;
				break;
			}
		}		
		List<TermsExpression> list = new ArrayList<TermsExpression>();		
		for (TermContext indices_ctx : ctx.term())
			list.add(terms.get(indices_ctx));
		if (list.isEmpty()) {
			variables.put(ctx, new VariableExpression(name, list, new Location(ctx.start)));
		} else { 
			// TODO : define the type of indices
			variables.put(ctx, new VariableExpression(name, list, new Location(ctx.start)));
		}
	}



	
	/**
	 * Term expressions
	 */
	@Override
	public void exitTerm_natural(Term_naturalContext ctx) {
		terms.put(ctx, new IntegerValue(Integer.parseInt(ctx.NAT().getText())));		
	}
	

	@Override
	public void exitTerm_boolean(Term_booleanContext ctx) {
		terms.put(ctx, new BooleanValue(Boolean.parseBoolean(ctx.BOOL().getText())));			
	}
	
	@Override
	public void exitTerm_string(Term_stringContext ctx) {
		terms.put(ctx, new StringValue((ctx.STRING().getText())));			
	}
	
	@Override
	public void exitTerm_decimal(Term_decimalContext ctx) {
		terms.put(ctx, new DecimalValue(Double.parseDouble(ctx.DEC().getText())));			
	}
	
	@Override
	public void exitTerm_componentdefn(Term_componentdefnContext ctx) {
		terms.put(ctx, new TermComponent<T>(components.get(ctx.component())));			
	}
	
	@Override
	public void exitTerm_variable(Term_variableContext ctx) {
		terms.put(ctx, new TermVariable(variables.get(ctx.var())));
	}
	
	@Override
	public void exitTerm_instance(Term_instanceContext ctx) {
		terms.put(ctx, new TermInstance<T>(instances.get(ctx.instance())));
	}

	@Override
	public void exitTerm_operation(Term_operationContext ctx) {
		TermsExpression e1 = terms.get(ctx.term(0));
		TermsExpression e2 = terms.get(ctx.term(1));		
		List<TermsExpression> l = Arrays.asList(e1,e2);
		switch(ctx.op.getType()){
		case ReoParser.DIV:
			terms.put(ctx, new Function(FunctionSymbol.DIV,l,new Location(ctx.start)));
		case ReoParser.MUL:
			terms.put(ctx, new Function(FunctionSymbol.MUL,l,new Location(ctx.start)));
		case ReoParser.MOD:
			terms.put(ctx, new Function(FunctionSymbol.MOD,l,new Location(ctx.start)));
		case ReoParser.ADD:
			terms.put(ctx, new Function(FunctionSymbol.ADD,l,new Location(ctx.start)));
		case ReoParser.MIN:
			terms.put(ctx, new Function(FunctionSymbol.MIN,l,new Location(ctx.start)));
		case ReoParser.LIST:
			terms.put(ctx, new Function(FunctionSymbol.LIST,l,new Location(ctx.start)));
		} 
	}
	
	@Override
	public void exitTerm_exponent(Term_exponentContext ctx) {
		TermsExpression e1 = terms.get(ctx.term(0));
		TermsExpression e2 = terms.get(ctx.term(1));
		List<TermsExpression> l = Arrays.asList(e1,e2);
		terms.put(ctx, new Function(FunctionSymbol.POW,l,new Location(ctx.start)));
	}

	@Override
	public void exitTerm_unarymin(Term_unaryminContext ctx) {
		TermsExpression e = terms.get(ctx.term());
		List<TermsExpression> l = Arrays.asList(e);
		terms.put(ctx, new Function(FunctionSymbol.MIN,l,new Location(ctx.start)));
	}

	@Override
	public void exitTerm_brackets(Term_bracketsContext ctx) {
		terms.put(ctx, terms.get(ctx.term()));
	}
	
	/**
	 * List of terms	
	 */

	@Override
	public void exitList(ListContext ctx) {
		List<TermsExpression> list = new ArrayList<TermsExpression>();
		for (TermContext expr_ctx : ctx.term())
			list.add(terms.get(expr_ctx));
		termsList.put(ctx, list);
	}
}