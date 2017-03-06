package nl.cwi.pr.runtime.examples.thesis.npb.lu;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Master(int N, String CLASS, String version,
			OutputPort initAdderOutputPort, OutputPort initLowerJacOutputPort,
			OutputPort initRHSComputeOutputPort,
			OutputPort initScaleOutputPort, OutputPort initUpperJacOutputPort,

			OutputPort doAdderOutputPort, OutputPort doLowerJacOutputPort,
			OutputPort doRHSComputeOutputPort, OutputPort doScaleOutputPort,
			OutputPort doUpperJacOutputPort,

			InputPort doneAdderInputPort, InputPort doneLowerJacInputPort,
			InputPort doneRHSComputeInputPort, InputPort doneScaleInputPort,
			InputPort doneUpperJacInputPort) {

		LU.main(new String[] { "NP=" + N, "CLASS=" + CLASS }, version,
				initAdderOutputPort, initLowerJacOutputPort,
				initRHSComputeOutputPort, initScaleOutputPort,
				initUpperJacOutputPort, doAdderOutputPort,
				doLowerJacOutputPort, doRHSComputeOutputPort,
				doScaleOutputPort, doUpperJacOutputPort, doneAdderInputPort,
				doneLowerJacInputPort, doneRHSComputeInputPort,
				doneScaleInputPort, doneUpperJacInputPort);
	}

	public static void Slave1(String type, Integer id, InputPort initInputPort,
			InputPort doInputPort, OutputPort doneOutputPort) {

		switch (type) {
		case "Adder":
			new Adder(id, initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "RHSCompute":
			new RHSCompute(id, initInputPort, doInputPort, doneOutputPort)
					.run();
			break;
		case "Scale":
			new Scale(id, initInputPort, doInputPort, doneOutputPort).run();
			break;
		default:
			throw new Error("Unknown worker type");
		}
	}

	public static void Slave2(String type, Integer id, InputPort initInputPort,
			InputPort doInputPort, OutputPort doneOutputPort,
			InputPort acqInputPort, InputPort relInputPort) {

		switch (type) {
		case "LowerJac":
			new LowerJac(id, initInputPort, doInputPort, doneOutputPort,
					acqInputPort, relInputPort).run();
			break;
		case "UpperJac":
			new UpperJac(id, initInputPort, doInputPort, doneOutputPort,
					acqInputPort, relInputPort).run();
			break;
		default:
			throw new Error("Unknown worker type");
		}
	}
}
