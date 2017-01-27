package nl.cwi.pr.misc;


public class DefaultPortFactory extends PortFactory {

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Port newObject(int id, PortSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return new Port(id, spec);
	}
}
