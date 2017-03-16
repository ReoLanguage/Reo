package nl.cwi.reo.interpret.interpreters;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ReoFile;
import nl.cwi.reo.interpret.ReoLexer;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.ReoProgram;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.Component;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.listeners.Listener;
import nl.cwi.reo.interpret.listeners.ErrorListener;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

/**
 * Interpreter of Reo source files, for given generic semantics.
 * 
 * @param <T>
 *            Reo semantics type
 */
public class Interpreter<T extends Semantics<T>> {

	/**
	 * Type of semantics.
	 */
	private final SemanticsType semantics;

	/**
	 * ANTLR listener.
	 */
	private final Listener<T> listener;

	/**
	 * Component paths: base directories of component files.
	 */
	private final List<String> dirs;

	/**
	 * List of parameters to instantiate the main component.
	 */
	private final List<Term> values;

	/**
	 * Container for messages.
	 */
	private final Monitor m;

	/**
	 * Constructs a Reo interpreter.
	 * 
	 * @param dirs
	 *            list of directories of Reo components
	 */
	public Interpreter(SemanticsType semantics, Listener<T> listener, List<String> dirs, List<String> params,
			Monitor monitor) {
		if (semantics == null || listener == null || dirs == null || params == null)
			throw new NullPointerException();
		this.semantics = semantics;
		this.listener = listener;
		this.dirs = Collections.unmodifiableList(dirs);
		List<Term> values = new ArrayList<Term>();
		for (String s : params)
			values.add(new StringValue(s));
		this.values = Collections.unmodifiableList(values);
		this.m = monitor;
	}

	/**
	 * Interprets a list of Reo files (the first file is the main file) as a
	 * list of atomic components.
	 * 
	 * @param file
	 *            non-empty list of Reo file names.
	 * @return Reo connector interpretation of the main component in the first
	 *         file, or null, if the main component could not be interpreted.
	 */
	@Nullable
	public ReoProgram<T> interpret(String file) {

		// Set of parsed Reo source files.
		Map<String, ReoFile<T>> programs = new HashMap<String, ReoFile<T>>();
		
		// Dependency graph of parsed component definitions.
		Map<String, Set<String>> deps = new HashMap<String, Set<String>>();

		// Name of main component.
		String name = "";

		// List of unparsed imported component definitions.
		Queue<String> todo = new LinkedList<String>();

		// Parse the provided source file.
		ReoFile<T> mainFile = parse(file);
		if (mainFile != null) {
			programs.put(mainFile.getName(), mainFile);
			name = mainFile.getName();
			Set<String> imports = new HashSet<String>(mainFile.getImports()); 
			todo.addAll(imports);
			deps.putIfAbsent(mainFile.getName(), imports);
		}

		// Find and parse all imported component definitions.
		String component = null;
		while ((component = todo.poll()) != null) {
			if (!deps.containsKey(component)) {
				ReoFile<T> reoFile = findComponent(component);
				if (reoFile != null) {
					programs.put(reoFile.getName(), reoFile);
					Set<String> imports = new HashSet<String>(reoFile.getImports()); 
					todo.addAll(imports);
					deps.putIfAbsent(reoFile.getName(), imports);
				} else {
					m.add("Component " + component + " is not found.");
				}
			}
		}

		// Find the correct order to evaluate the parsed programs.
		List<ReoFile<T>> list = new ArrayList<ReoFile<T>>();
		while (!deps.isEmpty()) {
			String prog = null;
			for (Map.Entry<String, Set<String>> comp : deps.entrySet())
				if (comp.getValue().isEmpty())
					prog = comp.getKey();
			if (prog == null) {
				m.add("There is a cyclic dependency of imports among " + deps.keySet());
				return null;
			} 
			list.add(programs.get(prog));
			
			// Remove the program from the dependency graph.
			Iterator<Map.Entry<String, Set<String>>> iter = deps.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<String, Set<String>> entry = iter.next();
			    entry.getValue().remove(prog);
			    if (entry.getKey().equals(prog))
			    	iter.remove();
			}
		}
		
		// Evaluate all component expressions.
		Scope scope = new Scope();
		for (ReoFile<T> f : list)
			f.evaluate(scope, m);

		// Instantiate the main component
		Value main = scope.get(new Identifier(name));
		if (main instanceof Component<?>) {
			@SuppressWarnings("unchecked")
			Instance<T> i = ((Component<T>) main).instantiate(values, null, m);
			if (i != null)
				return new ReoProgram<T>(name, file, i.getConnector());
		}

		return null;
	}

	/**
	 * Locates the source file that contains the definition of a component.
	 * 
	 * @param component
	 *            fully qualified name of the requested component.
	 * @return path string of the file containing this components definition, or
	 *         null, if this path is not found.
	 */
	@Nullable
	private ReoFile<T> findComponent(String component) {

		ReoFile<T> prog = null;

		int k = component.lastIndexOf('.') + 1;
		String name = component.substring(k);
		String directory = component.substring(0, k).replace('.', File.separatorChar);
		String cp1 = directory + name + "." + semantics + ".treo";
		String cp2 = directory + name + ".treo";

		search: for (String dir : dirs) {

			// Check if atomic component exists in resources of this jar.
			InputStream in1 = getClass().getResourceAsStream(File.separator + cp1);
			if (in1 != null) {
				try {
					prog = parse(new ANTLRInputStream(in1), File.separator + cp1);
				} catch (IOException e1) {
					m.add("Cannot open " + cp1);
				}
			}

			// Check if composite component exists in resources of this jar.
			InputStream in2 = getClass().getResourceAsStream(File.separator + cp2);
			if (in2 != null) {
				try {
					prog = parse(new ANTLRInputStream(in2), File.separator + cp2);
				} catch (IOException e1) {
					m.add("Cannot open " + cp2);
				}
			}

			// Check if atomic component exists in file system.
			File f1 = new File(dir + File.separator + cp1);
			if (f1.exists() && !f1.isDirectory()) {
				try {
					prog = parse(new ANTLRFileStream(dir + File.separator + cp1), dir + File.separator + cp1);
				} catch (IOException e) {
					m.add("Cannot open " + f1.toString());
				}
				break search;
			}

			// Check if composite component exists in file system.
			File f2 = new File(dir + File.separator + cp2);
			if (f2.exists() && !f2.isDirectory()) {
				try {
					prog = parse(new ANTLRFileStream(dir + File.separator + cp2), dir + File.separator + cp2);
				} catch (IOException e) {
					m.add("Cannot open " + f2.toString());
				}
				break search;
			}

			// Check if this directory contains a .zip file.
			File folder = new File(dir);
			if (folder.exists() && folder.isDirectory()) {

				FilenameFilter archiveFilter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(".zip");
					}
				};

				File[] files = folder.listFiles(archiveFilter);
				if (files != null) {
					for (File file : files) {
						if (!file.isDirectory()) {
							ZipFile zipFile = null;
							try {
								zipFile = new ZipFile(file.getPath());
								ZipEntry entry1 = zipFile.getEntry(cp1);
								ZipEntry entry2 = zipFile.getEntry(cp2);
								if (entry1 != null) {
									InputStream input = zipFile.getInputStream(entry1);
									assert input != null : "@AssumeAssertion(nullness)";
									prog = parse(new ANTLRInputStream(input), cp1);
									break search;
								} else if (entry2 != null) {
									InputStream input = zipFile.getInputStream(entry2);
									assert input != null : "@AssumeAssertion(nullness)";
									prog = parse(new ANTLRInputStream(input), cp2);
									break search;
								}
							} catch (IOException e) {
								m.add("Cannot open " + file.toString());
							} finally {
								try {
									if (zipFile != null)
										zipFile.close();
								} catch (IOException e) {
								}
							}
						}
					}
				}
			}
		}

		return prog;
	}

	/**
	 * Parses a source file using ANTLR4, and walks over the parse tree to
	 * interpret this source file as a Java object.
	 * 
	 * @param is
	 *            input stream
	 * @param path
	 *            location of the file
	 * @return an interpreted source file, or null in case of an error.
	 */
	@Nullable
	private ReoFile<T> parse(InputStream is, String file) {
		try {
			return parse(new ANTLRInputStream(is), file);
		} catch (IOException e) {
			m.add("Cannot open " + file + ".");
		}
		return null;
	}

	/**
	 * Parses a source file using ANTLR4, and walks over the parse tree to
	 * interpret this source file as a Java object.
	 * 
	 * @param path
	 *            location of the file
	 * @return an interpreted source file, or null in case of an error.
	 */
	@Nullable
	private ReoFile<T> parse(String path) {
		try {
			return parse(new ANTLRFileStream(path), path);
		} catch (IOException e) {
			m.add("Cannot open " + path + ".");
		}
		return null;
	}

	/**
	 * Parses a source file using ANTLR4, and walks over the parse tree to
	 * interpret this source file as a Java object.
	 * 
	 * @param c
	 *            input character stream
	 * @param path
	 *            location of the file
	 * @return an interpreted source file, or null in case of an error.
	 */
	@Nullable
	private ReoFile<T> parse(CharStream c, String path) {
		ReoLexer lexer = new ReoLexer(c);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ReoParser parser = new ReoParser(tokens);

		ErrorListener errListener = new ErrorListener(m);
		parser.removeErrorListeners();
		parser.addErrorListener(errListener);
		ParseTree tree = parser.file();
		if (errListener.hasError)
			return null;
		ParseTreeWalker walker = new ParseTreeWalker();
		listener.setFileName(path);
		walker.walk(listener, tree);
		return listener.getMain();
	}

}