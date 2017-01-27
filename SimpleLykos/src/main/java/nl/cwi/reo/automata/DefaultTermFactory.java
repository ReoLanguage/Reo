package nl.cwi.reo.automata;

import nl.cwi.pr.misc.PortFactory;
import nl.cwi.reo.automata.TermSpec.DatumSpec;
import nl.cwi.reo.automata.TermSpec.FunctionSpec;
import nl.cwi.reo.automata.TermSpec.PortVariableSpec;
import nl.cwi.reo.automata.TermSpec.PostVariableSpec;
import nl.cwi.reo.automata.TermSpec.PreVariableSpec;
import nl.cwi.reo.automata.TermSpec.QuantifiedVariableSpec;

public class DefaultTermFactory extends TermFactory {

	//
	// CONSTRUCTORS
	//

	public DefaultTermFactory(PortFactory portFactory,
			MemoryCellFactory memoryCellFactory) {

		super(portFactory, memoryCellFactory);
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Term newTerm(int id, DatumSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Datum(id, spec);
	}

	@Override
	protected Term newTerm(int id, FunctionSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Function(id, spec);
	}

	@Override
	protected Term newTerm(int id, PortVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new PortVariable(id, spec);
	}

	@Override
	protected Term newTerm(int id, PostVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new PostVariable(id, spec);
	}

	@Override
	protected Term newTerm(int id, PreVariableSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new PreVariable(id, spec);
	}
	
	@Override
	protected Term newTerm(int id, QuantifiedVariableSpec spec) {
		if (spec==null)
			throw new NullPointerException();
		
		return new QuantifiedVariable(id, spec);
	}
}
