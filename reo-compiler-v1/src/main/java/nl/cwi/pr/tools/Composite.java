package nl.cwi.pr.tools;


public class Composite {
	private String projectName;

	private boolean TargetFileSystem;
	private boolean TargetNewProject;
	private boolean TargetSameProject;

	private String TargetGenerateesDirectoryLocation;
	private String TargetRunTimeDirectoryLocation;
	
	public Composite(){
		
	}

	public boolean isTargetNewProject() {
		return TargetNewProject;
	}
	public void setTargetNewProject(boolean TargetNewProject) {
		this.TargetNewProject = TargetNewProject;
	}
	
	
	
	public boolean isTargetSameProject() {
		return TargetSameProject;
	}
	public void setTargetSameProject(boolean TargetSameProject) {
		this.TargetSameProject = TargetSameProject;
	}
	
	
	
	public boolean isTargetFileSystem() {
		return TargetFileSystem;
	}
	public void setTargetFileProject(boolean TargetFileSystem) {
		this.TargetFileSystem = TargetFileSystem;
	}
	
	
	
	
	public String getTargetGenerateesDirectoryLocation(){
		return TargetGenerateesDirectoryLocation;
	}
	public String getTargetRunTimeDirectoryLocation(){
		return TargetRunTimeDirectoryLocation;
	}
	public String getProject() {
		return projectName;
	}

	

	public void setProject(String projectName) {
		this.projectName = projectName;
	}
	public void setTargetGenerateesDirectoryLocation(String TargetGenerateesDirectoryLocation) {
		this.TargetGenerateesDirectoryLocation = TargetGenerateesDirectoryLocation;
	}


}
