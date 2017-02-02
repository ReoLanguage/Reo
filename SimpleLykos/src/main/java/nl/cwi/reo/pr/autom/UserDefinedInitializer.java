package nl.cwi.reo.pr.autom;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public abstract class UserDefinedInitializer {

	//
	// METHODS - PUBLIC
	//

	public abstract boolean canInitialize(UserDefinedAutomaton automaton);

	public abstract void initialize(UserDefinedAutomaton automaton);

	//
	// STATIC
	//

	private static Map<File, ClassLoader> automatonLoaders = new HashMap<File, ClassLoader>();

	//
	// STATIC - METHODS - PUBLIC
	//

	public static UserDefinedInitializer newInstance(String className,
			String rootDirectoryLocation) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, IOException {

		if (className == null)
			throw new NullPointerException();
		if (rootDirectoryLocation == null)
			throw new NullPointerException();

		/*
		 * Try to load the specified initializer class from the current class
		 * path
		 */

		ClassLoader loader = UserDefinedInitializer.class.getClassLoader();
		Class<?> initializerClass = null;
		try {
			initializerClass = loader.loadClass(className);
		} catch (ClassNotFoundException exception) {
		}

		/*
		 * Try to load the specified initializer class from the root location
		 */

		if (initializerClass == null) {
			File rootFile = new File(rootDirectoryLocation).getCanonicalFile();
			synchronized (automatonLoaders) {
				if (!automatonLoaders.containsKey(rootFile))
					automatonLoaders.put(rootFile, new URLClassLoader(
							new URL[] { rootFile.toURI().toURL() },
							UserDefinedAutomaton.class.getClassLoader()));

				loader = automatonLoaders.get(rootFile);
			}

			initializerClass = loader.loadClass(className);
		}

		return (UserDefinedInitializer) initializerClass.newInstance();
	}
}
