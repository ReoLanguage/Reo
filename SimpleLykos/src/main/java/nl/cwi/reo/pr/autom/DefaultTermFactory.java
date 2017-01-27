package nl.cwi.reo.pr.autom;

import nl.cwi.reo.pr.autom.TermSpec.DatumSpec;
import nl.cwi.reo.pr.autom.TermSpec.FunctionSpec;
import nl.cwi.reo.pr.autom.TermSpec.PortVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.PostVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.PreVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.QuantifiedVariableSpec;
import nl.cwi.reo.pr.misc.PortFactory;

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
