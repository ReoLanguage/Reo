package nl.cwi.reo.pr.autom;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.pr.autom.MemoryCellFactory.MemoryCell;
import nl.cwi.reo.pr.autom.MemoryCellFactory.MemoryCellSet;
import nl.cwi.reo.pr.misc.IdObjectFactory;

public abstract class MemoryCellFactory extends
		IdObjectFactory<MemoryCell, MemoryCellSet, MemoryCellSpec> {

	//
	// METHODS - PUBLIC
	//

	@Override
	public MemoryCellSet newSet() {
		return new MemoryCellSet();
	}

	//
	// CLASSES - PUBLIC
	//

	public class MemoryCell extends
			IdObjectFactory<MemoryCell, MemoryCellSet, MemoryCellSpec>.IdObject {

		//
		// CONSTRUCTORS
		//

		protected MemoryCell(int id, MemoryCellSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public Map<String, Object> getProperties() {
			Map<String, Object> properties = new HashMap<>();
			properties.put("HAS_TERM", hasTerm());
			return properties;
		}

		public Object getTerm() {
			if (!hasTerm())
				throw new IllegalStateException();

			return getSpec().getTerm();
		}

		public boolean hasTerm() {
			return getSpec().hasTerm();
		}

		@Override
		public String toString() {
			return Integer.toString(getId());
		}
	}

	public class MemoryCellSet
			extends
			IdObjectFactory<MemoryCell, MemoryCellSet, MemoryCellSpec>.IdObjectSet {
	}
}
