package nl.cwi.pr.targ.java.comp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nl.cwi.pr.Platform;
import nl.cwi.pr.tools.InterpretedWorker;
import nl.cwi.pr.tools.comp.WorkerCompiler;
import nl.cwi.pr.tools.interpr.WorkerSignature;
import nl.cwi.pr.util.Timestamps;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;


public class JavaWorkerCompiler extends WorkerCompiler<JavaProgramCompiler> {

	//
	// CONSTRUCTORS
	//

	public JavaWorkerCompiler(JavaProgramCompiler parent,
			InterpretedWorker worker) {

		super(parent, worker);
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Map<String, String> generateFiles() {
		InterpretedWorker worker = getGeneratee();
		WorkerSignature signature = worker.getSignature();
		String[] identifiers = signature.getName().split("\\.");

		// String workerPackageName = getParent().getProgramPackageName() + "."
		// + identifiers[identifiers.length - 1].toLowerCase() + ".t"
		// + Timestamps.getNext();
		// String workerSimpleClassName = "Worker";

		String workerPackageName = getParent().getProgramPackageName();
		String workerSimpleClassName = identifiers[identifiers.length - 1];
		workerSimpleClassName = "Worker_" + Timestamps.getNext() + "_"
				+ workerSimpleClassName.substring(0, 1).toUpperCase()
				+ workerSimpleClassName.substring(1);

		String workerClassName = workerPackageName + "."
				+ workerSimpleClassName;

		worker.addAnnotation(JavaMainCompiler.ANNOTATION_CLASS_NAME,
				workerClassName);

		/*
		 * 
		 */
		
		STGroupFile templates = new STGroupFile(this.getSettings().getSTGlocation()+"java-worker.stg");

		ST headerTemplate;
		ST workerClassTemplate;

		String code = "";

		headerTemplate = templates.getInstanceOf("header");
		headerTemplate.add("packageName", workerPackageName);
		code += headerTemplate.render();

		workerClassTemplate = templates.getInstanceOf("workerClass");
		workerClassTemplate.add("simpleClassName", workerSimpleClassName);
		workerClassTemplate.add("signature", signature);
		code += "\n\n" + workerClassTemplate.render();

		Map<String, String> files = new HashMap<String, String>();
		files.put(workerClassName.replace(".", File.separator) + ".java", code);
		return files;
	}
}
