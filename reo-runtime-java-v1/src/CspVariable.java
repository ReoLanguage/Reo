package nl.cwi.pr.runtime;

import java.util.ArrayList;
import java.util.List;

public class CspVariable {
	
	//
	// FIELDS
	//
	
	final List<CspLiteral> literals = new ArrayList<CspLiteral>();
	volatile Object value;

	//
	// METHODS
	//
	
	public boolean check() {
		for (final CspLiteral l : literals)
			if (l.check && l.isClosed() && !l.holds())
				return false;

		return true;
	}

	public Object getValue() {
		return value;
	}

	public void exportValue() {
	}

	public void importValue() {
		value = null;
	}

	public boolean setAndCheck(final Object newValue) {
		setValue(newValue);
		return check();
	}
	
	public void setValue(final Object newValue) {
		value = newValue;
	}
}