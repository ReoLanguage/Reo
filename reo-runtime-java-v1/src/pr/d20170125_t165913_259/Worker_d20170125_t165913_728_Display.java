package pr.d20170125_t165913_259;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Worker_d20170125_t165913_728_Display extends Thread {
	private final InputPort BlackMove$1;
	private final InputPort WhiteMove$1;

	public Worker_d20170125_t165913_728_Display(
		InputPort BlackMove$1,
		InputPort WhiteMove$1
	) {
		this.BlackMove$1 = BlackMove$1;
		this.WhiteMove$1 = WhiteMove$1;
	}

	@Override
	public void run() {
		List<Throwable> exceptions = new ArrayList<>();

		/* 
		 * Treat "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display" as a class of type Runnable
		 */

		Runnable runnable = null;
		try {
			Class<?> workerClass = Class.forName("nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display");
			Object workerInstance = null;
			for (Constructor<?> constr : workerClass.getConstructors())
				try {
					constr.setAccessible(true);
					workerInstance = constr.newInstance(
						WhiteMove$1,
						BlackMove$1
					);
				} catch (Throwable throwable) {
					exceptions.add(throwable);
				}

			if (workerInstance == null)
				throw new Error(
						"Class \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display\" has no constructor for the provided arguments");

			if (!(workerInstance instanceof Runnable))
				throw new Error(
						"Class \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display\" does not implement interface Runnable");

			runnable = (Runnable) workerInstance;
		}

		catch (Throwable throwable) {
			exceptions.add(throwable);
		}

		if (runnable != null) {
			runnable.run();
			return;
		}

		/*
		 * Treat "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display" as a static method
		 */

		int lastDotIndex = "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display".lastIndexOf('.');
		if ("nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display".startsWith(".") || "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display".endsWith("."))
			throw new Error("Failed to load worker \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display\"");
		if (lastDotIndex == -1)
			throw new Error("Failed to load worker \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display\"");

		String className = "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display".substring(0, lastDotIndex);
		String methodName = "nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display".substring(lastDotIndex + 1);

		boolean hasMethod = false;

		try {
			Class<?> workerClass = Class.forName(className);
			for (Method meth : workerClass.getMethods())
				if (meth.getName().equals(methodName)) {
					meth.setAccessible(true);
					meth.invoke(
						null, 
						WhiteMove$1,
						BlackMove$1
					);
					hasMethod = true;
				}
		}

		catch (Throwable throwable) {
			exceptions.add(throwable);
		}

		if (!hasMethod) {
			for (Throwable thr : exceptions)
				thr.printStackTrace();

			throw new Error("Failed to load worker \"nl.cwi.pr.runtime.examples.thesis.chess.Workers.Display\" because of the following exceptions: " + exceptions.toString());
		}
	}
}
