package nl.cwi.pr.misc;

import nl.cwi.pr.misc.MainArgumentFactory.MainArgument;
import nl.cwi.pr.misc.PortFactory.Port;

public class Factories {

	//
	// FIELDS
	//

	private final MainArgumentFactory mainArgumentFactory;
	private final PortFactory portFactory;

	//
	// CONSTRUCTORS
	//

	public Factories(MainArgumentFactory mainArgumentFactory,
			PortFactory portFactory) {
		if (mainArgumentFactory == null)
			throw new NullPointerException();
		if (portFactory == null)
			throw new NullPointerException();

		this.mainArgumentFactory = mainArgumentFactory;
		this.portFactory = portFactory;
	}

	//
	// METHODS
	//

	public boolean constructedMainArgument(String name) {
		if (name == null)
			throw new NullPointerException();

		return mainArgumentFactory.constructed(new MainArgumentSpec(name));
	}

	public boolean constructedPort(String name) {
		if (name == null)
			throw new NullPointerException();

		return portFactory.constructed(new PortSpec(name));
	}

	public MainArgument newOrGetMainArgument(String name) {
		if (name == null)
			throw new NullPointerException();

		return mainArgumentFactory.newOrGet(new MainArgumentSpec(name));
	}

	public Port newOrGetPort(String name) {
		if (name == null)
			throw new NullPointerException();

		return portFactory.newOrGet(new PortSpec(name));
	}

	public MainArgument getMainArgument(String name) {
		if (name == null)
			throw new NullPointerException();
		if (!constructedMainArgument(name))
			throw new IllegalStateException();

		return mainArgumentFactory.get(new MainArgumentSpec(name));
	}

	public MainArgumentFactory getMainArgumentFactory() {
		return mainArgumentFactory;
	}

	public Port getPort(String name) {
		if (name == null)
			throw new NullPointerException();
		if (!constructedPort(name))
			throw new IllegalStateException();

		return portFactory.get(new PortSpec(name));
	}

	public PortFactory getPortFactory() {
		return portFactory;
	}
}
