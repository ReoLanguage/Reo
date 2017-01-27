package nl.cwi.pr.misc;

public class TypedName {

	//
	// FIELDS
	//

	private final String name;
	private final Type type;

	//
	// CONSTRUCTORS
	//

	public TypedName(String name, Type type) {
		if (name == null)
			throw new NullPointerException();
		if (type == null)
			throw new NullPointerException();

		this.name = name;
		this.type = type;
	}

	//
	// METHODS
	//

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypedName && equals((TypedName) obj);
	}

	public boolean equals(TypedName name) {
		return this.name.equals(name.name) && this.type.equals(name.type);
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name + ":" + type;
	}

	//
	// STATIC - ENUM
	//

	public static enum Type {
		ARRAY, EXTRALOGICAL, FAMILY, INTEGER, MAIN_ARGUMENT, PORT, WORKER_NAME;

		@Override
		public String toString() {
			switch (this) {
			case ARRAY:
				return "array";
			case EXTRALOGICAL:
				return "extralogical";
			case FAMILY:
				return "family";
			case INTEGER:
				return "integer";
			case MAIN_ARGUMENT:
				return "mainArgument";
			case PORT:
				return "port";
			case WORKER_NAME:
				return "workerName";
			default:
				throw new Error();
			}
		}
	}
}
