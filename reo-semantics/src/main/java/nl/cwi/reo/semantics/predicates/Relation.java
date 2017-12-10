package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Relation of a list of terms.
 */
public final class Relation implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean relation = true;

	/**
	 * Name of this relation.
	 */
	private final String name;

	/**
	 * List of arguments of this relation.
	 */
	private final List<Term> args;

	/**
	 * Defines whether the function symbol can be used with infix notation. For
	 * example, infix can be a+b, while prefix can be +(a,b).
	 */
	private final boolean infix;
	
	/**
	 * Free variables of this term.
	 */
	private final Set<Variable> vars;

	/**
	 * Constructs a new relation with a given name and a given list of
	 * arguments.
	 * 
	 * @param name
	 *            name of the relation
	 * @param args
	 *            list of arguments
	 * @param infix
	 *            infix notation
	 */
	public Relation(String name, Collection<Term> args, boolean infix) {
		this.name = name;
		this.args = Collections.unmodifiableList(new ArrayList<>(args));
		this.infix = infix;
		Set<Variable> vars = new HashSet<Variable>();
		for (Term t : args)
			vars.addAll(t.getFreeVariables());
		this.vars = Collections.unmodifiableSet(vars);
	}

	/**
	 * Gets the name of this relation.
	 * 
	 * @return name of this relation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name for string template
	 * @return
	 */
	public String getSTGName() {
		return name.substring(1,name.length()-1);
	}
	
	/**
	 * Gets the list of arguments of this relation.
	 * 
	 * @return list of arguments of this relation.
	 */
	@Nullable
	public List<Term> getArgs() {
		return args;
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
	public Set<Port> getPorts() {
		return new HashSet<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQuantifierFree() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Formula evaluate(Scope s, Monitor m) {

		// Evaluate the symbol
		String _name = name;
		Value v = s.get(new Identifier(name));
		if (v != null)
			_name = v.toString();

		// Evaluate the arguments
		List<Term> _args = null;
		if (args != null) {
			_args = new ArrayList<>();
			for (Term t : args) {
				Term u = t.evaluate(s, m);
				if (u == null)
					return null;
				_args.add(u);
			}
		}

		return new Relation(_name, _args, infix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		List<Term> list = new ArrayList<Term>();
		for (Term s : args)
			list.add(s.rename(links));
		return new Relation(name, list, infix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula DNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		if (!vars.contains(x))
			return this;
		List<Term> _args = new ArrayList<>();
		for (Term u : args)
			_args.add(u.substitute(t, x));
		return new Relation(name, _args, infix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		return new HashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "";
		if (infix) {
			Iterator<Term> iter = args.iterator();
			while (iter.hasNext()) {
				s += iter.next().toString();
				if (iter.hasNext())
					s += name;
			}
		} else {
			if (args != null && !args.isEmpty()) {
				s += name + "(";
				Iterator<Term> iter = args.iterator();
				while (iter.hasNext()) {
					s += iter.next().toString();
					if (iter.hasNext())
						s += ", ";
				}
				s += ")";
			}
		}
		return s;
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
		if (!(other instanceof Relation))
			return false;
		Relation p = (Relation) other;
		return Objects.equals(this.name, p.name) && Objects.equals(this.args, p.args);
	}

	@Override
	public Set<Set<Term>> inferTermType(Set<Set<Term>> termTypeSet) {
		Set<Set<Term>> _termTypeSet = new HashSet<>(termTypeSet);
		findset:
		for(Term t : args){
			if(!t.getTypeTag().isDefaultType()){
				TypeTag tt = t.getTypeTag();
				for(Set<Term> terms : termTypeSet){
					/*
					 * Find if 't' has already a type assigned. 
					 * Loop over all set of termTypeSet
					 */
					Set<Term> _terms = new HashSet<>();
					if(terms.contains(t)){
						for(Term term : terms){
							/*
							 * Either propagate type in this set, or throw exception if type mismatch
							 */
							if(term.getTypeTag().isDefaultType()){
								_terms.add(term.setTypeTag(tt));
							}
							else if(term.getTypeTag().equals(tt)){
								continue findset;
							}
							else if(!term.getTypeTag().isDefaultType()&& term.getTypeTag()!=tt){
								try {
									throw new Exception("Type mismatch");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						_termTypeSet.remove(terms);
						_termTypeSet.add(_terms);
					}
				}
			}
		}
		return _termTypeSet;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.args);
	}

	@Override
	public Formula getTypedFormula(Map<Term, TypeTag> typeMap) {
		List<Term> _args = new ArrayList<>();
		
		for(Term t : args){
			if(!t.getTypeTag().isDefaultType() && t.getTypeTag()!=typeMap.get(t)){
				try {
					throw new Exception("type mismatch");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				_args.add(t.setTypeTag(typeMap.get(t)));
		}
		
		return null;
	}

}
