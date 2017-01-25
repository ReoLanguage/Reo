package nl.cwi.pr.tools.comp;

import java.util.Collections;
import java.util.Map;

import nl.cwi.pr.autom.AutomatonFactory;
import nl.cwi.pr.tools.InterpretedProtocol;

public class DefaultProtocolCompiler extends
		ProtocolCompiler<DefaultProgramCompiler> {

	//
	// CONSTRUCTORS
	//

	public DefaultProtocolCompiler(DefaultProgramCompiler parent,
			InterpretedProtocol protocol, AutomatonFactory automatonFactory) {
		super(parent, protocol, automatonFactory);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	protected Map<String, String> generateFiles() {
		return Collections.emptyMap();
	}
}
