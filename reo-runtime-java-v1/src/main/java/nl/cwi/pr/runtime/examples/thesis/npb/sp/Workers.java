package nl.cwi.pr.runtime.examples.thesis.npb.sp;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Master(int N, String CLASS, String version,
			OutputPort initRHSAdderOutputPort,
			OutputPort initRHSComputeOutputPort,
			OutputPort initTXInverseOutputPort,
			OutputPort initXSolverOutputPort, OutputPort initYSolverOutputPort,
			OutputPort initZSolverOutputPort,

			OutputPort doRHSAdderOutputPort, OutputPort doRHSComputeOutputPort,
			OutputPort doTXInverseOutputPort, OutputPort doXSolverOutputPort,
			OutputPort doYSolverOutputPort, OutputPort doZSolverOutputPort,

			InputPort doneRHSAdderInputPort, InputPort doneRHSComputeInputPort,
			InputPort doneTXInverseInputPort, InputPort doneXSolverInputPort,
			InputPort doneYSolverInputPort, InputPort doneZSolverInputPort) {

		SP.main(new String[] { "NP=" + N, "CLASS=" + CLASS }, version,
				initRHSAdderOutputPort, initRHSComputeOutputPort,
				initTXInverseOutputPort, initXSolverOutputPort,
				initYSolverOutputPort, initZSolverOutputPort,
				doRHSAdderOutputPort, doRHSComputeOutputPort,
				doTXInverseOutputPort, doXSolverOutputPort,
				doYSolverOutputPort, doZSolverOutputPort,
				doneRHSAdderInputPort, doneRHSComputeInputPort,
				doneTXInverseInputPort, doneXSolverInputPort,
				doneYSolverInputPort, doneZSolverInputPort);
	}

	public static void Slave(String type, InputPort initInputPort,
			InputPort doInputPort, OutputPort doneOutputPort) {

		switch (type) {
		case "RHSAdder":
			new RHSAdder(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "RHSCompute":
			new RHSCompute(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "TXInverse":
			new TXInverse(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "XSolver":
			new XSolver(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "YSolver":
			new YSolver(initInputPort, doInputPort, doneOutputPort).run();
			break;
		case "ZSolver":
			new ZSolver(initInputPort, doInputPort, doneOutputPort).run();
			break;
		default:
			throw new Error("Unknown worker type");
		}
	}
}
