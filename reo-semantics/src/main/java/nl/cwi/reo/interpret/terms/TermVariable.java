package nl.cwi.reo.interpret.terms;

import java.util.Arrays;


import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

public class TermVariable implements TermsExpression {

		private VariableExpression variable;
		
		public TermVariable(VariableExpression variable){
			this.variable=variable;
		}
		
		@Override
		public Terms evaluate(Scope s, Monitor m) {
			Terms terms = new Terms(Arrays.asList(this.variable.evaluate(s, m)));
			return terms;
		}
	}
