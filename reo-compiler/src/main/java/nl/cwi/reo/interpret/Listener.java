package nl.cwi.reo.interpret;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Listener implements ParseTreeListener {

	public Component getMain() {
		return null;
	}

	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub
		
	}
}

//import java.util.List;
//import java.util.Map;
//
//import org.antlr.v4.runtime.tree.ParseTreeProperty;
//
///**
// * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
// * Returns a {@link nl.cwi.reo.semantics.Program}.
// */
//public class ListenerBase implements TreoListener {
//	
//	public Component main;
//		
//	/**
//	 * Local definitions.
//	 */
//	private ParseTreeProperty<Map<String, Object>> localdefinitions = 
//			new ParseTreeProperty<Map<String, Object>>();
//	
//	/**
//	 * Signatures of locally defined components.
//	 */
//	private ParseTreeProperty<Map<String, ParamType>> localsignatures = 
//			new ParseTreeProperty<Map<String, ParamType>>();
//
//	/**
//	 * Values.
//	 */
//	private ParseTreeProperty<Object> values = 
//			new ParseTreeProperty<Object>();
//
//	/**
//	 * Subcomponents.
//	 */
//	private ParseTreeProperty<Expression> subcomponents =
//			new ParseTreeProperty<Expression>();
//	
//	/**
//	 * Boolean expressions
//	 */
//	private ParseTreeProperty<BooleanExpression> booleanexpressions = 
//			new ParseTreeProperty<BooleanExpression>();
//	
//	/**
//	 * Signatures
//	 */
//	private ParseTreeProperty<ParamType> signatures = 
//			new ParseTreeProperty<ParamType>();
//	
//	/**
//	 * Variable lists
//	 */
//	private ParseTreeProperty<Variables> variables = 
//			new ParseTreeProperty<Variables>();	
//	
//	/**
//	 * Indices
//	 */
//	private ParseTreeProperty<List<List<IntegerExpression>>> indices = 
//			new ParseTreeProperty<List<List<IntegerExpression>>>();	
//	
//	/**
//	 * Integer expressions
//	 */
//	private ParseTreeProperty<IntegerExpression> integerexpressions = 
//			new ParseTreeProperty<IntegerExpression>();
//
//	/**
//	 * Atoms.
//	 */
//	private ParseTreeProperty<Atom> atoms = 
//			new ParseTreeProperty<Atom>();
//	
//
//	public Component getProgram() {
//		return null; // localdefinitions.get("main").instantiate(new HashMap<String,String>());
//	}
//}