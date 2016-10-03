package nl.cwi.reo.parse;

/**
 * A fresh node name generator, that provides fresh names for internal nodes.
 */
public class NodeGenerator {
	
	/**
	 * Initial index.
	 */
	private int index = 0;
	
	/**
	 * Gets a fresh node name.
	 * @return fresh node name
	 */
	public String getNode() {
		return "_a" + index++;
	}

}
