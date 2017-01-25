package nl.cwi.pr.runtime.examples.thesis.npb.is;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Master(int N, String CLASS, String version,
			OutputPort initOutputPort, OutputPort doOutputPort,
			InputPort doneInputPort) {

		IS.main(new String[] { "NP=" + N, "CLASS=" + CLASS }, version,
				initOutputPort, doOutputPort, doneInputPort);
	}

	public static void Slave(InputPort initInputPort, InputPort doInputPort,
			OutputPort doneOutputPort) {

		new RankThread(initInputPort, doInputPort, doneOutputPort).run();
	}
}
