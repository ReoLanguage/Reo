package nl.cwi.reo.parse;

import java.util.List;
import java.util.Map;

/** 
 * A parameterized definition of a component. A parameterized definition is either an atomic component 
 * {@link nl.cwi.reo.parse.DefinitionAtomic} defined in an abstract semantics of Reo, or a composite 
 * definition {@link nl.cwi.reo.parse.DefinitionComposite} of a set 
 * {@link java.util.Set}&lt;{@link nl.cwi.reo.parse.Component}&gt; of parameterized components. 
 */
public interface Definition extends Component {
	
	/**
	 * Gets the parameters.
	 * @return list of parameter names
	 */
	public List<String> getParameters();
	
	/**
	 * Gets the concrete component interface.
	 * @param parameters	parameter assignment
	 * @return list of node names
	 * @throws Exception if not all required parameters are assigned.
	 */
	public List<String> getInterface(Map<String, String> parameters) throws Exception;

}
