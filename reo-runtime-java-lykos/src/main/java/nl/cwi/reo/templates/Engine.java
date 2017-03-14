package nl.cwi.reo.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Engine {
	private final InputPort inputPort;
	private final OutputPort outputPort;
	private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private final BufferedReader reader;
	private final BufferedWriter writer;
	private final int moveTime = new Random().nextInt(3000);

	public Engine(InputPort inputPort, OutputPort outputPort) {
		this.inputPort = inputPort;
		this.outputPort = outputPort;

		String os = System.getProperty("os.name").toLowerCase();
		final String stockfish;
		if (os.contains("win"))
			stockfish = "stockfish-6-win-32.exe";
		else if (os.contains("mac"))
			stockfish = "stockfish-6-osx-64";
		else
			stockfish = "stockfish-6-linux-64";

		String path = "src"
				+ File.separator
				+ getClass().getPackage().getName()
						.replace('.', File.separatorChar) + File.separator
				+ stockfish;

		Process process = null;
		try {
			process = Runtime.getRuntime().exec(path);
			System.out.println("Running " + path + "...");
		} catch (IOException e) {
			System.out.println("Failed to run " + path);
			e.printStackTrace();
			throw new Error();
		}

		this.reader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		this.writer = new BufferedWriter(new OutputStreamWriter(
				process.getOutputStream()));

		new Thread() {
			@Override
			public void run() {
				String line;
				StringBuilder builder = new StringBuilder();

				try {
					while ((line = reader.readLine()) != null) {
						builder.append(line + "\n");

						if (line.startsWith("uciok")
								|| line.startsWith("bestmove")) {

							queue.put(builder.toString());
							builder = new StringBuilder();
						}
					}
				}

				catch (IOException | InterruptedException e) {
					e.printStackTrace();
					throw new Error();
				}
			}
		}.start();

		try {
			writer.write("uci\n");
			writer.flush();
			queue.take();
		}

		catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
	}

	public void run() {
		while (true) {
			String moves = (String) inputPort.getUninterruptibly();
			String result = "";

			try {
				writer.write("ucinewgame\n");
				writer.write("position startpos moves " + moves + "\n");
				writer.write("go movetime " + moveTime + "\n");
				writer.flush();
				result = queue.take();
			}

			catch (IOException | InterruptedException e) {
				e.printStackTrace();
				throw new Error();
			}

			outputPort.putUninterruptibly(result);
		}

	}
}
