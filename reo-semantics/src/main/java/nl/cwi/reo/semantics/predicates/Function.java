package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * A function applied to a list of terms.
 */
public final class Function implements Term {

	/**
	 * Flag for string template.
	 */
	public static final boolean function = true;

	/**
	 * Name of this function.
	 */
	private final String name;

	/**
	 * List of arguments of this function.
	 */
	private final List<Term> args;

	/**
	 * Defines whether the function symbol can be used with infix notation. For
	 * example, infix can be a+b, while prefix can be +(a,b).
	 */
	private final boolean infix;

	/**
	 * Type of returned data.
	 */
	private final TypeTag tag;

	/**
	 * Free variables of this term.
	 */
	private final Set<Variable> vars;

	/**
	 * Constructs a new function from a name, a value, and a list of arguments.
	 *
	 * @param name
	 *            name of the function
	 * @param args
	 *            list of arguments
	 * @param infix
	 *            infix notation
	 * @param tag
	 *            type tag
	 */
	public Function(String name, List<Term> args, boolean infix, TypeTag tag) {
		this.name = name;
		this.args = args;
		this.infix = infix;
		this.tag = tag;
		Set<Variable> vars = new HashSet<Variable>();
		for (Term t : args)
			vars.addAll(t.getFreeVariables());
		this.vars = vars;
	}

	/**
	 * Gets the name of this function.
	 * 
	 * @return name of this function.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name for string template
	 * @return
	 */
	public String getSTGName() {
		if(name.substring(0,1).contentEquals("\""))
			return name.substring(1,name.length()-1);			
		return name;
	}
	
	/**
	 * Gets the list of arguments of this function.
	 * 
	 * @return list of arguments of this function.
	 */
	@Nullable
	public List<Term> getArgs() {
		return args;
	}

	/**
	 * Gets whether this function is written as infix.
	 * 
	 * @return true, is the function is written as infix.
	 */
	@Nullable
	public boolean getInfix() {
		return infix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		List<Term> list = new ArrayList<Term>();
		for (Term s : args)
			list.add(s.rename(links));
		return new Function(name, list, infix, tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		if (!vars.contains(x))
			return this;
		List<Term> _args = new ArrayList<>();
		for (Term u : args)
			_args.add(u.substitute(t, x));
		return new Function(name, _args, infix, tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term evaluate(Scope s, Monitor m) {


		// Evaluate the symbol
		String _name = name;
		Value v = s.get(new Identifier(name));
		if (v != null)
			_name = v.toString();

		// Evaluate the arguments
		List<Term> _args = new ArrayList<>();
		for (Term t : args) {
			Term u = t.evaluate(s, m);
			if (u == null)
				return null;
			_args.add(u);
		}

		return new Function(_name, _args, infix, tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeTag getTypeTag() {
		return tag;
	}
	
	public Function setTag(TypeTag t) {
		return new Function(getName(), getArgs(), getInfix(), t);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST(
				"<if(s.infix)>(<s.args; separator=s.name>)<else><s.name><if(s.args)>(<s.args; separator=\", \">)<endif><endif>");
		st.add("s", this);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Function))
			return false;
		Function func = (Function) other;
		return Objects.equals(name, func.name) && Objects.equals(args, func.args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, this.args);
	}

	@Override
	public Term setTypeTag(TypeTag t) {
		if(getTypeTag()!=null && getTypeTag()!=TypeTags.Object){
			if(getTypeTag()!=t){
				try {
					throw new Exception("type mismatch");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return this;
		}
		else
			return setTag(getTypeTag());
	}

}

