package nl.cwi.reo.interpret.systems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.oldstuff.Expression;
import nl.cwi.reo.interpret.oldstuff.Port;
import nl.cwi.reo.interpret.oldstuff.Semantics;
import nl.cwi.reo.interpret.oldstuff.SourceCode;
import nl.cwi.reo.interpret.semantics.Component;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.signatures.Node;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableNameList;

public final class ReoSystemValue<T extends Semantics<T>> implements ReoSystem<T> {
	
	/**
	 * Signature expression.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Program.
	 */
	private final Assembly<T> prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ReoSystemValue(SignatureExpression sign, Assembly<T> prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public SignatureExpression getSignature() {
		return sign;
	}
	
	public ComponentList<T> getInstances() {
		return prog.getInstances();
	}
	
	public  ComponentList<T>  getWorkers() {
		ComponentList<T> workers = new ComponentList<T>();
		Map<String, Expression> def= prog.getDefinitions();
		for(Map.Entry<String, Expression> e : def.entrySet()){
			e.getValue();
			if(e.getValue() instanceof ReoSystemValue<?>){
				ReoSystemValue<?> r = (ReoSystemValue<?>) e.getValue();
				for(Component<?>c : r.prog.getInstances()){
//					Port p = new Port(r.getSignature());
					if(c.getSourceCode()!=null) {
						Map<Port,Port> map = new HashMap<Port,Port>();
						for(Node n : r.getSignature().getNodeList()){
							map.put(new Port(n.getVariable().toString()),n.toPort());
						};
						workers.add(new Component<T>((T)c.getAtom(),map,c.getSourceCode()));
					}
				}
			}
		}
		return workers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoSystemValue<T> evaluate(Map<String, Expression> params) {
		return new ReoSystemValue<T>(sign, prog.evaluate(params));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Assembly<T> instantiate(ValueList values, VariableNameList iface) {
		SignatureConcrete links = sign.evaluate(values, iface);
		Assembly<T> _prog = prog.instantiate(links);
		return _prog;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ReoSystemValue<?>)) return false;
	    ReoSystemValue<?> p = (ReoSystemValue<?>)other;
	   	return Objects.equals(this.sign, p.sign) && 
	   			Objects.equals(this.prog, p.prog);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(this.sign, this.prog);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return sign + "{" + prog + "}";
	}
}