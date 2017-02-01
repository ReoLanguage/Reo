package nl.cwi.reo.pr.comp;

import nl.cwi.reo.pr.misc.Member;
import nl.cwi.reo.pr.misc.MemberSignature;

public class InterpretedProtocol extends InterpretedGeneratee {

	//
	// FIELDS
	//

	private final Member member;

	//
	// CONSTRUCTORS
	//

	public InterpretedProtocol(Member member) {
		if (member == null)
			throw new NullPointerException();

		this.member = member;
	}

	//
	// METHODS
	//

	public Member getMember() {
		return member;
	}

	public MemberSignature getSignature() {
		return member.getSignature();
	}

	@Override
	public String toString() {
		return getSignature().toString();
	}
}
