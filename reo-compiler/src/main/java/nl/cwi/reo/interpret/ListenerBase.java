// Generated from nl/cwi/reo/Treoparse/Treo.g4 by ANTLR 4.3
package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.semantics.Program}.
 */
public class ListenerBase implements TreoListener {
	
	public Component main;
		
	/**
	 * Local definitions.
	 */
	private ParseTreeProperty<Map<String, Object>> localdefinitions = new ParseTreeProperty<Map<String, Object>>();
	
	/**
	 * Signatures of locally defined components.
	 */
	private ParseTreeProperty<Map<String, Object>> localsignatures = new ParseTreeProperty<Map<String, Object>>();

	/**
	 * Values.
	 */
	private ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();

	/**
	 * Subcomponents.
	 */
	private ParseTreeProperty<Evaluable> subcomponents = new ParseTreeProperty<Evaluable>();
	
	/**
	 * Boolean expressions
	 */
	private ParseTreeProperty<BooleanExpression> booleanexpressions = new ParseTreeProperty<BooleanExpression>();
	
	/**
	 * Variable lists.
	 */
	private ParseTreeProperty<Variables> variables = new ParseTreeProperty<Variables>();	
	
	/**
	 * Indices.
	 */
	private ParseTreeProperty<List<IntegerExpression>> indices = new ParseTreeProperty<List<IntegerExpression>>();	
	
	/**
	 * Integer expressions
	 */
	private ParseTreeProperty<IntegerExpression> integerexpressions = new ParseTreeProperty<IntegerExpression>();

	/**
	 * Atoms.
	 */
	private ParseTreeProperty<Object> atoms = new ParseTreeProperty<Object>();
	

	public Component getProgram() {
		return null; // localdefinitions.get("main").instantiate(new HashMap<String,String>());
	}
}