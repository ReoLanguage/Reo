package nl.cwi.reo.semantics.rbautomaton;

@Deprecated
public enum Role {
	BLOCK(0),
	FIRE(1),
	TRIGGER(2);
	
    private final int value;

    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
