package nl.cwi.pr.tools;


import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


public class Compiler {
	
	private Composite composite;
	
	
	/**
	 * List of provided Reo source files.
	 */
	@Parameter(names = {"-s", "--source"},description = ".pr files")
	private String sourceFileLocation;
	
	@Parameter(names = {"-g", "--stglocation"},description = "string template generator location")
	private String STGlocation;
	
	@Parameter(names = {"-r", "--runtimelocation"},description = "run time location")
	private String runTimeLocation;
	
	@Parameter(names = {"-t", "--targetLanguage"},description = "target language")
	private Language targetLanguage;
	
	
	
	public Compiler(){
		this.targetLanguage=Language.JAVA;
		this.composite=new Composite();
		this.sourceFileLocation=null;
	}
	
	public static void main(String[] args) {
		Compiler compiler = new Compiler();
		
		compiler.composite.setTargetNewProject(false);
		compiler.composite.setTargetSameProject(true);
		compiler.composite.setTargetFileProject(false);
		compiler.composite.setProject("Compiler");
		
		new JCommander(compiler, args);
        compiler.run();					
	}
	
	public void run() {

		try {
			
			String project = null;
			
			/*
			 * Get sourceFileLocation
			 */
			
			String sourceFileLocation = this.sourceFileLocation;

			String targetGenerateesDirectoryLocation = null;
			String targetRunTimeDirectoryLocation = null;

			if (composite.isTargetNewProject()
					|| composite.isTargetSameProject()) {

				String sourceDirectoryName = null;
				File projectDirectory = null;

				/*
				 * Create a new project  ..
				 */
				
				if (composite.isTargetNewProject()) {

					while ((projectDirectory = new File("pr/project-"
									+ new SimpleDateFormat("yyyyMMddHHmmss")
											.format(System.currentTimeMillis()))
							).exists())
						;

					projectDirectory.mkdirs();
					sourceDirectoryName = "src";

					project = projectDirectory.getName();
				}
				
				/*
				 * .. or take an existing one 
				 */
				
				if (composite.isTargetSameProject()) {
					if (project == null) {

						/*
						 * Prompt project
						 */

						if (composite.getProject() == null)
							throw new NullPointerException("Unkown project");
						else
							project = composite.getProject();
					}

					projectDirectory = new File("../reo-runtime-java-v1/");

					switch (targetLanguage) {
					case JAVA:
						sourceDirectoryName = "src/main/java";
						break;

					case C11:
						
					default:
						throw new Exception("Unsupported target language");
					}
				}

				String projectDirectoryLocation = projectDirectory
						.getAbsolutePath();

				targetGenerateesDirectoryLocation = Paths.get(
						projectDirectoryLocation, sourceDirectoryName)
						.toString();
				targetRunTimeDirectoryLocation = Paths.get(
						projectDirectoryLocation, sourceDirectoryName)
						.toString();
			}

			else if (composite.isTargetFileSystem()) {
				targetGenerateesDirectoryLocation = composite
						.getTargetGenerateesDirectoryLocation();
				targetRunTimeDirectoryLocation = composite
						.getTargetRunTimeDirectoryLocation();
			}

			else
				throw new IllegalStateException();


			/*
			 * Compile
			 */


			CompilerSettings compilerSettings = new CompilerSettings(runTimeLocation,STGlocation,sourceFileLocation, targetLanguage,false);

			
			compilerSettings.ignoreInput(false);
			compilerSettings.ignoreData(false);
			compilerSettings.partition(true);
			compilerSettings.subtractSyntactically(true);
			compilerSettings.commandify(true);
			compilerSettings.inferQueues(true);

			compilerSettings.put("COUNT_PORTS", false);



			ToolResult<CompiledProgram> result = Tools.compile(
					compilerSettings);

			if (result.hasErrors() || !result.has()) {
				String exceptionMessage = "";
				for (ToolError err : result.getErrors())
					exceptionMessage += "\n\n" + err.toString();

				throw new Exception(exceptionMessage.substring(1));
			}

			/*
			 * Write
			 */

			Files.createDirectories(Paths
					.get(targetGenerateesDirectoryLocation));
			Files.createDirectories(Paths.get(targetRunTimeDirectoryLocation));

			CompiledProgram compiledProgram = result.get();
			compiledProgram.writeGeneratees(targetGenerateesDirectoryLocation);
			
//			compiledProgram.writeRunTime(targetRunTimeDirectoryLocation);
		}


		catch (Cancellation cancellation) {
			if (cancellation.getMessage() != null)
				new Exception("error");
		}

		catch (Throwable throwable) {
			throwable.printStackTrace();
			new Exception("Unanticipated failure at "
							+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System
									.currentTimeMillis()), throwable);
		}

		finally {

		}
	}
}

