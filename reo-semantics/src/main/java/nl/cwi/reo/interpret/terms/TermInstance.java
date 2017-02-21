package nl.cwi.reo.interpret.terms;

import java.util.Arrays;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.instances.InstancesExpression;
import nl.cwi.reo.util.Monitor;

public class TermInstance<T extends Semantics<T>> implements TermsExpression {

		private InstancesExpression<T> instance;
		
		public TermInstance(InstancesExpression<T> instance){
			this.instance=instance;
		}
		
		@Override
		public Terms evaluate(Scope s, Monitor m) {
			Terms terms = new Terms(Arrays.asList(this.instance.evaluate(s, m)));
			return terms;
		}
}
