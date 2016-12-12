package nl.cwi.reo.interpret;

public interface IntegerExpression extends Evaluable<Integer> {
	/**
	 *  I need this empty interface to be able to distinguish
	 *  between Evaluable of type Boolean and Evaluable of type 
	 *  Integer, which is indistinguishable due to Java type erasure.
	 */
}
