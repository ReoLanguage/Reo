package nl.cwi.pr.tools;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

public class CompilerProgressMonitor extends SubProgressMonitor {
	private long beginTime;
	private long timeout = -1;
	private AtomicBoolean isCanceled = new AtomicBoolean();

	//
	// CONSTRUCTORS
	//

	public CompilerProgressMonitor(IProgressMonitor monitor, int ticks) {
		super(monitor, ticks, PREPEND_MAIN_LABEL_TO_SUBTASK);

		if (monitor instanceof CompilerProgressMonitor) {
			CompilerProgressMonitor compilerProgressMonitor = (CompilerProgressMonitor) monitor;

			this.beginTime = compilerProgressMonitor.beginTime;
			this.timeout = compilerProgressMonitor.timeout;
			this.isCanceled = compilerProgressMonitor.isCanceled;
		}

	}

	public CompilerProgressMonitor(IProgressMonitor monitor, int ticks,
			long timeout) {

		super(monitor, ticks, PREPEND_MAIN_LABEL_TO_SUBTASK);

		if (timeout < 0)
			throw new IllegalArgumentException();

		this.timeout = timeout * 1000 * 1000 * 1000;
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public void beginTask(String name, int totalWork) {
		if (super.getWrappedProgressMonitor() instanceof CompilerProgressMonitor)
			name = "| " + name;

		super.beginTask(name, totalWork);
		beginTime = System.nanoTime();
		checkTimeout();
	}

	public void checkTimeout() {
		if (timeout != -1 && beginTime + timeout < System.nanoTime())
			throw new Cancellation("Compilation timeout!");
		if (isCanceled.get())
			throw new Cancellation("Compilation canceled");
	}

	@Override
	public void done() {
		super.done();
		if (isCanceled.get())
			throw new Cancellation("Canceled");
	}

	@Override
	public void setCanceled(boolean b) {
		super.setCanceled(b);
		isCanceled.set(b);
	}

	@Override
	public void subTask(String name) {
		super.subTask(name);
		checkTimeout();
	}

	@Override
	public void worked(int work) {
		super.worked(work);
		checkTimeout();
	}
}