package nl.cwi.reo.semantics;

public enum SemanticsType {
	PA, CAM, WA, SA;

	@Override
	public String toString() {
		switch(this) {
		case PA: return "pa";
		case CAM: return "cam";
		case WA: return "wa";
		case SA: return "sa";
		default: return null;
		}
	}

}
