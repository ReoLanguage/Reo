package nl.cwi.pr.misc;

import java.util.ArrayList;
import java.util.List;

public class Member {

	//
	// FIELDS
	//

	private MemberSignature signature;

	//
	// METHODS
	//

	public List<Primitive> getPrimitives() {
		List<Primitive> primitives = new ArrayList<>();

		if (this instanceof Primitive)
			primitives.add((Primitive) this);

		else if (this instanceof Composite)
			for (Member m : ((Composite) this).getChildren())
				primitives.addAll(m.getPrimitives());

		else
			throw new Error();

		return primitives;
	}

	public MemberSignature getSignature() {
		return signature;
	}

	public boolean hasSignature() {
		return signature != null;
	}

	public void setSignature(MemberSignature newSignature) {
		if (newSignature == null)
			throw new NullPointerException();
		if (hasSignature())
			throw new IllegalStateException();

		this.signature = newSignature;
	}

	@Override
	public String toString() {
		if (hasSignature())
			return signature.toString();
		else
			return "-";
	}

	//
	// STATIC - CLASSES
	//

	public static class Primitive extends Member {

		//
		// FIELDS
		//

		private final String className;
		private final String rootLocation;

		//
		// CONSTRUCTORS
		//

		public Primitive(String className, String rootLocation) {
			if (className == null)
				throw new NullPointerException();
			if (rootLocation == null)
				throw new NullPointerException();
			
			this.className = className;
			this.rootLocation = rootLocation;
		}

		//
		// METHODS
		//

		public String getClassName() {
			return className;
		}

		public String getRootLocation() {
			return rootLocation;
		}
	}

	public static class Composite extends Member {

		//
		// FIELDS
		//

		private final List<Member> children = new ArrayList<>();

		//
		// CONSTRUCTORS
		//

		//
		// METHODS
		//

		public void addChild(Member child) {
			if (child == null)
				throw new NullPointerException();

			children.add(child);
		}

		public List<Member> getChildren() {
			return children;
		}

		public List<Member> getManyChildren(boolean isAncestor) {
			List<Member> manyChildren = new ArrayList<>();

			if (isAncestor || !hasSignature())
				for (Member m : children)
					if (m instanceof Primitive)
						manyChildren.add(m);
					else
						manyChildren.addAll(((Composite) m)
								.getManyChildren(false));
			else
				manyChildren.add(this);

			return manyChildren;
		}
	}
}
