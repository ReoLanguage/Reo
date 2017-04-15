package nl.cwi.reo.interpret.typetags;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.variables.ParameterType;

public final class TypeTag implements ParameterType {

	private final String tag;

	public TypeTag() {
		this.tag = "";
	}

	public TypeTag(String tag) {
		if (tag == null)
			throw new NullPointerException();
		this.tag = tag;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equalType(ParameterType t) {
		if (!(t instanceof TypeTag))
			return false;
		TypeTag tag = (TypeTag) t;
		return this.tag.equals(tag.tag);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return tag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof TypeTag))
			return false;
		TypeTag p = (TypeTag) other;
		return Objects.equals(this.tag, p.tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.tag);
	}
}
