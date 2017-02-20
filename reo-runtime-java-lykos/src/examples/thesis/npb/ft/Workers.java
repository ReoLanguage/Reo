package nl.cwi.pr.runtime.examples.thesis.npb.ft;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Master(int N, String CLASS, String version,
			OutputPort initEvolveOutputPort, OutputPort initFFTOutputPort,
			OutputPort doEvolveOutputPort, OutputPort doFFTOutputPort,
			InputPort doneEvolveInputPort, InputPort doneFFTInputPort) {

		FT.main(new String[] { "NP=" + N, "CLASS=" + CLASS }, version,
				initEvolveOutputPort, initFFTOutputPort, doEvolveOutputPort,
				doFFTOutputPort, doneEvolveInputPort, doneFFTInputPort);
	}

	public static void Slave(String type, InputPort initInputPort,
			InputPort doInputPort, OutputPort doneOutputPort) {

		switch (type) {
		case "Evolve":
			new EvolveThread(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "FFT":
			new FFTThread(initInputPort, doInputPort, doneOutputPort).run();
			break;
		default:
			throw new Error("Unknown worker type");
		}
	}
}
