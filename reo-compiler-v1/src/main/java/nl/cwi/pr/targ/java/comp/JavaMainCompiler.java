package nl.cwi.pr.targ.java.comp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nl.cwi.pr.Platform;
import nl.cwi.pr.misc.MemberSignature;
import nl.cwi.pr.tools.InterpretedMain;
import nl.cwi.pr.tools.InterpretedProtocol;
import nl.cwi.pr.tools.InterpretedWorker;
import nl.cwi.pr.tools.comp.MainCompiler;
import nl.cwi.pr.tools.interpr.MainSignature;
import nl.cwi.pr.tools.interpr.WorkerSignature;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;


public class JavaMainCompiler extends MainCompiler<JavaProgramCompiler> {

	//
	// CONSTRUCTORS
	//

	public JavaMainCompiler(JavaProgramCompiler parent, InterpretedMain main) {
		super(parent, main);
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected Map<String, String> generateFiles() {
		InterpretedMain main = getGeneratee();
		MainSignature signature = main.getSignature();

		Map<String, MemberSignature> protocolSignatures = new HashMap<>();
		Map<String, WorkerSignature> workerSignatures = new HashMap<>();

		for (InterpretedProtocol pr : main.getProtocols())
			protocolSignatures.put(
					(String) pr.getAnnotation(ANNOTATION_CLASS_NAME),
					pr.getSignature());

		for (InterpretedWorker w : main.getWorkers())
			workerSignatures.put(
					(String) w.getAnnotation(ANNOTATION_CLASS_NAME),
					w.getSignature());

		// if (workerSignatures.isEmpty())
		// return new HashMap<>();

		String mainPackageName = getParent().getProgramPackageName();
		String mainClassName = mainPackageName + ".Main";

		
		STGroupFile templates = new STGroupFile(this.getSettings().getSTGlocation()+"java-main.stg");

		ST headerTemplate;
		ST mainClassTemplate;

		String code = "";

		headerTemplate = templates.getInstanceOf("header");
		headerTemplate.add("packageName", mainPackageName);
		code += headerTemplate.render();

		mainClassTemplate = templates.getInstanceOf("mainClass");
		mainClassTemplate.add("signature", signature);
		mainClassTemplate.add("protocolSignatures", protocolSignatures);
		mainClassTemplate.add("workerSignatures", workerSignatures);
		code += "\n\n" + mainClassTemplate.render();

		Map<String, String> files = new HashMap<String, String>();
		files.put(mainClassName.replace(".", File.separator) + ".java", code);
		return files;
	}

	//
	// STATIC
	//

	public static final String ANNOTATION_CLASS_NAME = "className";

	public static final String ANNOTATION_PARAMETERS = "parameters";
}
