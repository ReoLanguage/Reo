package nl.cwi.pr.runtime.examples.thesis.npb.mg;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Master(int N, String CLASS, String version,
			OutputPort initResidOutputPort, OutputPort initPsinvOutputPort,
			OutputPort initInterpOutputPort, OutputPort initRprjOutputPort,

			OutputPort doResidOutputPort, OutputPort doPsinvOutputPort,
			OutputPort doInterpOutputPort, OutputPort doRprjOutputPort,

			InputPort doneResidInputPort, InputPort donePsinvInputPort,
			InputPort doneInterpInputPort, InputPort doneRprjInputPort) {

		MG.main(new String[] { "NP=" + N, "CLASS=" + CLASS }, version,
				initResidOutputPort, initPsinvOutputPort, initInterpOutputPort,
				initRprjOutputPort, doResidOutputPort, doPsinvOutputPort,
				doInterpOutputPort, doRprjOutputPort, doneResidInputPort,
				donePsinvInputPort, doneInterpInputPort, doneRprjInputPort);
	}

	public static void Slave(String type, InputPort initInputPort,
			InputPort doInputPort, OutputPort doneOutputPort) {

		switch (type) {
		case "Interp":
			new Interp(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "Psinv":
			new Psinv(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "Resid":
			new Resid(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "Rprj":
			new Rprj(initInputPort, doInputPort, doneOutputPort).run();
			break;
		default:
			throw new Error("Unknown worker type");
		}
	}
}
