package nl.cwi.pr.misc;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;

public abstract class PortFactory extends
		IdObjectFactory<Port, PortSet, PortSpec> {

	//
	// METHODS - PUBLIC
	//

	@Override
	public PortSet newSet() {
		return new PortSet();
	}

	//
	// CLASSES - PUBLIC
	//

	public class Port extends IdObjectFactory<Port, PortSet, PortSpec>.IdObject
			implements Variable, PortOrArray {

		//
		// FIELDS
		//

		final Map<String, Object> annotations = new HashMap<String, Object>();

		//
		// CONSTRUCTORS
		//

		public Port(int id, PortSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public void addAnnotation(String name, Object annotation) {
			if (name == null)
				throw new NullPointerException();
			if (annotation == null)
				throw new NullPointerException();
			if (hasAnnotation(name))
				throw new IllegalStateException();

			annotations.put(name, annotation);
		}

		@Override
		public int compareTo(Port o) {
			if (o == null)
				throw new NullPointerException();

			return getName().compareTo(o.getName());
		}

		public Object getAnnotation(String name) {
			if (name == null)
				throw new NullPointerException();

			return annotations.get(name);
		}

		@SuppressWarnings("unchecked")
		public <O> O getAnnotation(String name, Class<O> type) {
			if (name == null)
				throw new NullPointerException();
			if (type == null)
				throw new NullPointerException();
			if (!hasAnnotation(name, type))
				throw new IllegalStateException();

			return (O) annotations.get(name);
		}

		public String getName() {
			return getSpec().getName();
		}

		public boolean hasAnnotation(String name) {
			if (name == null)
				throw new NullPointerException();

			return annotations.containsKey(name);
		}

		@SuppressWarnings("unchecked")
		public <O> boolean hasAnnotation(String name, Class<O> type) {
			if (name == null)
				throw new NullPointerException();
			if (type == null)
				throw new NullPointerException();

			try {
				return annotations.containsKey(name)
						&& (O) annotations.get(name) != null;
			}

			catch (ClassCastException exception) {
				return false;
			}
		}

		@Override
		public String toString() {
			return getName();
		}

		//
		// STATIC
		//

		public static final String ANNOTATION_PORT_TYPE = "portType";
	}

	public class PortSet extends
			IdObjectFactory<Port, PortSet, PortSpec>.IdObjectSet {

		//
		// METHODS - PUBLIC
		//

		@Override
		public String toString() {
			return super.getSorted().toString();
		}
	}

	//
	// STATIC
	//

	public static enum PortType {
		INPUT, OUTPUT
	}
}
