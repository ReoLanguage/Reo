package nl.cwi.reo.interpret.oldstuff;

public enum SemanticsType {
	PA, CAM, WA, SA, PR;

	@Override
	public String toString() {
		switch(this) {
		case PA: return "pa";
		case PR: return "pr";
		case CAM: return "cam";
		case WA: return "wa";
		case SA: return "sa";
		default: return null;
		}
	}

}
