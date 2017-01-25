package pr.d20170125_t112821_802;

import java.util.*;
import java.util.concurrent.atomic.*;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Protocol_d20170125_t112821_985_Chess {

	//
	// FIELDS
	//

	/*
	 * Automata
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton5 automaton5;
	final Protocol_d20170125_t112821_985_Chess_Automaton26 automaton26;
	final Protocol_d20170125_t112821_985_Chess_Automaton31 automaton31;
	final Protocol_d20170125_t112821_985_Chess_Automaton46 automaton46;
	final Protocol_d20170125_t112821_985_Chess_Automaton62 automaton62;
	final Protocol_d20170125_t112821_985_Chess_Automaton65 automaton65;
	final Protocol_d20170125_t112821_985_Chess_Automaton76 automaton76;
	final Protocol_d20170125_t112821_985_Chess_Automaton80 automaton80;

	/*
	 * Public ports
	 */

	final PublicPort BlackCall$1;
	final PublicPort BlackMove$1;
	final PublicPort BlackReturn$1;
	final PublicPort WhiteCall$1$1;
	final PublicPort WhiteCall$1$2;
	final PublicPort WhiteCall$1$3;
	final PublicPort WhiteMove$1;
	final PublicPort WhiteReturn$1$1;
	final PublicPort WhiteReturn$1$2;
	final PublicPort WhiteReturn$1$3;

	/*
	 * Private ports
	 */

	final PrivatePort $inP1$12;
	final PrivatePort $inP1$36;
	final PrivatePort $inblack_out$11;
	final PrivatePort $inwhite_out$11;
	final PrivatePort $outP2$12;
	final PrivatePort $outP2$36;
	final PrivatePort $outblack_in$11;
	final PrivatePort $outwhite_in$11;

	/*
	 * Memory cells
	 */

	final MemoryCell memoryCell2 = new MemoryCell();
	final MemoryCell memoryCell4 = new MemoryCell();
	final MemoryCell memoryCell6 = new MemoryCell();
	final MemoryCell memoryCell8 = new MemoryCell("");

	/*
	 * Variable pool
	 */

	final CspVariablePool variablePool = new CspVariablePool();

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess(
		Port BlackReturn$1,
		Port WhiteReturn$1$1,
		Port WhiteReturn$1$2,
		Port WhiteReturn$1$3,
		Port BlackCall$1,
		Port BlackMove$1,
		Port WhiteCall$1$1,
		Port WhiteCall$1$2,
		Port WhiteCall$1$3,
		Port WhiteMove$1
	) {

		/*
		 * Set public ports
		 */

		this.BlackCall$1 = (PublicPort) BlackCall$1;
		this.BlackMove$1 = (PublicPort) BlackMove$1;
		this.BlackReturn$1 = (PublicPort) BlackReturn$1;
		this.WhiteCall$1$1 = (PublicPort) WhiteCall$1$1;
		this.WhiteCall$1$2 = (PublicPort) WhiteCall$1$2;
		this.WhiteCall$1$3 = (PublicPort) WhiteCall$1$3;
		this.WhiteMove$1 = (PublicPort) WhiteMove$1;
		this.WhiteReturn$1$1 = (PublicPort) WhiteReturn$1$1;
		this.WhiteReturn$1$2 = (PublicPort) WhiteReturn$1$2;
		this.WhiteReturn$1$3 = (PublicPort) WhiteReturn$1$3;

		/*
		 * Set private ports
		 */

		this.$inP1$12 = new PrivatePort();
		this.$inP1$36 = new PrivatePort();
		this.$inblack_out$11 = new PrivatePort();
		this.$inwhite_out$11 = new PrivatePort();
		this.$outP2$12 = new PrivatePort();
		this.$outP2$36 = new PrivatePort();
		this.$outblack_in$11 = new PrivatePort();
		this.$outwhite_in$11 = new PrivatePort();

		/*
		 * Set automata
		 */

		this.automaton5 = new Protocol_d20170125_t112821_985_Chess_Automaton5();
		this.automaton26 = new Protocol_d20170125_t112821_985_Chess_Automaton26();
		this.automaton31 = new Protocol_d20170125_t112821_985_Chess_Automaton31();
		this.automaton46 = new Protocol_d20170125_t112821_985_Chess_Automaton46();
		this.automaton62 = new Protocol_d20170125_t112821_985_Chess_Automaton62();
		this.automaton65 = new Protocol_d20170125_t112821_985_Chess_Automaton65();
		this.automaton76 = new Protocol_d20170125_t112821_985_Chess_Automaton76();
		this.automaton80 = new Protocol_d20170125_t112821_985_Chess_Automaton80();

		/*
		 * Initialize
		 */

		initialize();

		/*
		 * Start
		 */

		start();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize() {

		/*
		 * Initialize ports in automaton #62
		 */

		{
			this.WhiteReturn$1$1.handler = new HandlerForWhiteReturn$1$1(this);
			this.WhiteReturn$1$2.handler = new HandlerForWhiteReturn$1$2(this);
			this.WhiteReturn$1$3.handler = new HandlerForWhiteReturn$1$3(this);
			this.WhiteMove$1.handler = new HandlerForWhiteMove$1(this);
			this.$inwhite_out$11.masterHandler = new HandlerFor$inwhite_out$11(this);
			this.$outP2$12.masterHandler = new HandlerFor$outP2$12(this);
		}


		/*
		 * Initialize ports in automaton #65
		 */

		{
			this.BlackCall$1.handler = new HandlerForBlackCall$1(this);
			this.$inP1$36.masterHandler = new HandlerFor$inP1$36(this);
			this.$outblack_in$11.masterHandler = new HandlerFor$outblack_in$11(this);
		}


		/*
		 * Initialize ports in automaton #76
		 */

		{
			this.BlackReturn$1.handler = new HandlerForBlackReturn$1(this);
			this.BlackMove$1.handler = new HandlerForBlackMove$1(this);
			this.$inblack_out$11.masterHandler = new HandlerFor$inblack_out$11(this);
			this.$outP2$36.masterHandler = new HandlerFor$outP2$36(this);
		}


		/*
		 * Initialize ports in automaton #80
		 */

		{
			this.WhiteCall$1$1.handler = new HandlerForWhiteCall$1$1(this);
			this.WhiteCall$1$2.handler = new HandlerForWhiteCall$1$2(this);
			this.WhiteCall$1$3.handler = new HandlerForWhiteCall$1$3(this);
			this.$inP1$12.masterHandler = new HandlerFor$inP1$12(this);
			this.$outwhite_in$11.masterHandler = new HandlerFor$outwhite_in$11(this);
		}

		/*
		 * Initialize automata
		 */

		this.automaton5.initialize(this);
		this.automaton26.initialize(this);
		this.automaton31.initialize(this);
		this.automaton46.initialize(this);
		this.automaton62.initialize(this);
		this.automaton65.initialize(this);
		this.automaton76.initialize(this);
		this.automaton80.initialize(this);
	}

	public void start() {
		this.automaton5.start();
		this.automaton26.start();
		this.automaton31.start();
		this.automaton46.start();
		this.automaton62.start();
		this.automaton65.start();
		this.automaton76.start();
		this.automaton80.start();
	}

	//
	// MAIN
	//

	public static void main(String[] args) {
		OutputPort BlackReturn$1 = Ports.newOutputPort();
		OutputPort WhiteReturn$1$1 = Ports.newOutputPort();
		OutputPort WhiteReturn$1$2 = Ports.newOutputPort();
		OutputPort WhiteReturn$1$3 = Ports.newOutputPort();
		InputPort BlackCall$1 = Ports.newInputPort();
		InputPort BlackMove$1 = Ports.newInputPort();
		InputPort WhiteCall$1$1 = Ports.newInputPort();
		InputPort WhiteCall$1$2 = Ports.newInputPort();
		InputPort WhiteCall$1$3 = Ports.newInputPort();
		InputPort WhiteMove$1 = Ports.newInputPort();

		new Protocol_d20170125_t112821_985_Chess(
			BlackReturn$1,
			WhiteReturn$1$1,
			WhiteReturn$1$2,
			WhiteReturn$1$3,
			BlackCall$1,
			BlackMove$1,
			WhiteCall$1$1,
			WhiteCall$1$2,
			WhiteCall$1$3,
			WhiteMove$1
		);

		Map<String, Port> inputPorts = new HashMap<>();
		inputPorts.put("BlackReturn$1", (Port) BlackReturn$1);
		inputPorts.put("WhiteReturn$1$1", (Port) WhiteReturn$1$1);
		inputPorts.put("WhiteReturn$1$2", (Port) WhiteReturn$1$2);
		inputPorts.put("WhiteReturn$1$3", (Port) WhiteReturn$1$3);

		Map<String, Port> outputPorts = new HashMap<>();
		outputPorts.put("BlackCall$1", (Port) BlackCall$1);
		outputPorts.put("BlackMove$1", (Port) BlackMove$1);
		outputPorts.put("WhiteCall$1$1", (Port) WhiteCall$1$1);
		outputPorts.put("WhiteCall$1$2", (Port) WhiteCall$1$2);
		outputPorts.put("WhiteCall$1$3", (Port) WhiteCall$1$3);
		outputPorts.put("WhiteMove$1", (Port) WhiteMove$1);

		new Thread(new Console(inputPorts, outputPorts)).start();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton5 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton5_State1 state1;
	final Protocol_d20170125_t112821_985_Chess_Automaton5_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton5() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton5_State1();
		this.state2 = new Protocol_d20170125_t112821_985_Chess_Automaton5_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);
		this.state2.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}

	public boolean isCurrentState1() {
		return current.state == state1;
	}

	public boolean isCurrentState2() {
		return current.state == state2;
	}

	@Override
	public void reachSuccessor() {
		current.state.reachSuccessor();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton5_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $inP1$12;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton5.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton5.state2;
		/*
		 * Set ports 
		 */

		this.$inP1$12 = protocol.$inP1$12;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$inP1$12.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton5_State2 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $outP2$12;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton5.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton5.state1;
		/*
		 * Set ports 
		 */

		this.$outP2$12 = protocol.$outP2$12;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$outP2$12.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton26 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton26_State1 state1;
	final Protocol_d20170125_t112821_985_Chess_Automaton26_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton26() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton26_State1();
		this.state2 = new Protocol_d20170125_t112821_985_Chess_Automaton26_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);
		this.state2.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}

	public boolean isCurrentState1() {
		return current.state == state1;
	}

	public boolean isCurrentState2() {
		return current.state == state2;
	}

	@Override
	public void reachSuccessor() {
		current.state.reachSuccessor();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton26_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $inwhite_out$11;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton26.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton26.state2;
		/*
		 * Set ports 
		 */

		this.$inwhite_out$11 = protocol.$inwhite_out$11;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$inwhite_out$11.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton26_State2 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $outblack_in$11;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton26.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton26.state1;
		/*
		 * Set ports 
		 */

		this.$outblack_in$11 = protocol.$outblack_in$11;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$outblack_in$11.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton31 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton31_State1 state1;
	final Protocol_d20170125_t112821_985_Chess_Automaton31_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton31() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton31_State1();
		this.state2 = new Protocol_d20170125_t112821_985_Chess_Automaton31_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);
		this.state2.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}

	public boolean isCurrentState1() {
		return current.state == state1;
	}

	public boolean isCurrentState2() {
		return current.state == state2;
	}

	@Override
	public void reachSuccessor() {
		current.state.reachSuccessor();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton31_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $inP1$36;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton31.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton31.state2;
		/*
		 * Set ports 
		 */

		this.$inP1$36 = protocol.$inP1$36;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$inP1$36.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton31_State2 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $outP2$36;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton31.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton31.state1;
		/*
		 * Set ports 
		 */

		this.$outP2$36 = protocol.$outP2$36;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$outP2$36.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton46 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton46_State1 state1;
	final Protocol_d20170125_t112821_985_Chess_Automaton46_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton46() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton46_State1();
		this.state2 = new Protocol_d20170125_t112821_985_Chess_Automaton46_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);
		this.state2.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state2.reach();

		/*
		 * Return
		 */

		return;
	}

	public boolean isCurrentState1() {
		return current.state == state1;
	}

	public boolean isCurrentState2() {
		return current.state == state2;
	}

	@Override
	public void reachSuccessor() {
		current.state.reachSuccessor();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton46_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $inblack_out$11;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton46.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton46.state2;
		/*
		 * Set ports 
		 */

		this.$inblack_out$11 = protocol.$inblack_out$11;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$inblack_out$11.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton46_State2 extends State {

	//
	// FIELDS
	//

	/*
	 * Current state
	 */

	Current current;

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Private ports
	 */

	PrivatePort $outwhite_in$11;


	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton46.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton46.state1;
		/*
		 * Set ports 
		 */

		this.$outwhite_in$11 = protocol.$outwhite_in$11;

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Update current state
		 */

		current.state = this;

		/*
		 * Notify masters of private ports
		 */

		$outwhite_in$11.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton62 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton62() {
		super(4, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton62_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton62_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Public ports
	 */

	PublicPort WhiteMove$1;
	PublicPort WhiteReturn$1$1;
	PublicPort WhiteReturn$1$2;
	PublicPort WhiteReturn$1$3;

	/*
	 * Private ports
	 */

	PrivatePort $inwhite_out$11;
	PrivatePort $outP2$12;

	/*
	 * Transitions
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

						

	/*
	 * Fairness indices for observable transitions
	 */

						
	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton62_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_d20170125_t112821_985_Chess_Automaton62_Transition2();

		/*
		 * Set observable transitions per port
		 */

												
		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton62.state1;
		/*
		 * Set ports 
		 */

		this.$inwhite_out$11 = protocol.$inwhite_out$11;
		this.$outP2$12 = protocol.$outP2$12;
		this.WhiteMove$1 = protocol.WhiteMove$1;
		this.WhiteReturn$1$1 = protocol.WhiteReturn$1$1;
		this.WhiteReturn$1$2 = protocol.WhiteReturn$1$2;
		this.WhiteReturn$1$3 = protocol.WhiteReturn$1$3;

		/*
		 * Initialize transitions
		 */

		this.transition2.initialize(protocol);

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Unblock public ports
		 */

		WhiteMove$1.semaphore.release();
		WhiteReturn$1$1.semaphore.release();
		WhiteReturn$1$2.semaphore.release();
		WhiteReturn$1$3.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$inwhite_out$11.kickMaster();
		$outP2$12.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton62_Transition2 extends Transition {

	//
	// FIELDS
	//

	/*
	 * Context
	 */

	Context context;

	/*
	 * Public ports
	 */

	PublicPort WhiteMove$1;
	PublicPort WhiteReturn$1$1;
	PublicPort WhiteReturn$1$2;
	PublicPort WhiteReturn$1$3;

	/*
	 * Private ports
	 */

	PrivatePort $inwhite_out$11;
	PrivatePort $outP2$12;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton62_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton5 automaton5;
	Protocol_d20170125_t112821_985_Chess_Automaton26 automaton26;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton5.semaphore.acquireUninterruptibly();
		//automaton26.semaphore.acquireUninterruptibly();

		/*
		 * Check synchronization/data constraint
		 */

		boolean canFire = checkSynchronizationSet() && checkDataConstraint();

		/*
		 * Finalize transition
		 */

		if (canFire) {

			/*
			 * Update context
			 */

			context.remove(0, 0b00000000000000000000000000001111);

			/*
			 * Unblock ports
			 */

			WhiteMove$1.status = IO.COMPLETED;
			WhiteMove$1.semaphore.release();
			WhiteReturn$1$1.status = IO.COMPLETED;
			WhiteReturn$1$1.semaphore.release();
			WhiteReturn$1$2.status = IO.COMPLETED;
			WhiteReturn$1$2.semaphore.release();
			WhiteReturn$1$3.status = IO.COMPLETED;
			WhiteReturn$1$3.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton5.state1.reach();
			automaton26.state2.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton5.semaphore.release();
		//automaton26.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton62.context;

		/*
		 * Set ports 
		 */

		this.$inwhite_out$11 = protocol.$inwhite_out$11;
		this.$outP2$12 = protocol.$outP2$12;
		this.WhiteMove$1 = protocol.WhiteMove$1;
		this.WhiteReturn$1$1 = protocol.WhiteReturn$1$1;
		this.WhiteReturn$1$2 = protocol.WhiteReturn$1$2;
		this.WhiteReturn$1$3 = protocol.WhiteReturn$1$3;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton62.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton5 = protocol.automaton5;
		this.automaton26 = protocol.automaton26;

		/*
		 * Return
		 */

		 return;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected boolean checkDataConstraint() {
		return dataConstraint.solve();
	}

	protected boolean checkSynchronizationSet() {
		return true
			&& context.contains(0, 0b00000000000000000000000000001111)
			&& automaton5.isCurrentState2()			&& automaton26.isCurrentState1();
	}

	//
	// CLASSES
	//

	class DataConstraint {

		//
		// FIELDS
		//

		/*
		 * Public port variables
		 */

		final private CspPortVariable WhiteMove$1;
		final private CspPortVariable WhiteReturn$1$1;
		final private CspPortVariable WhiteReturn$1$2;
		final private CspPortVariable WhiteReturn$1$3;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $inwhite_out$11;
		final private CspPortVariable $outP2$12;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell2$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell4$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_d20170125_t112821_985_Chess protocol) {

			/*
			 * Set variables
			 */

			this.WhiteMove$1 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteMove$1);
			this.WhiteReturn$1$1 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteReturn$1$1);
			this.WhiteReturn$1$2 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteReturn$1$2);
			this.WhiteReturn$1$3 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteReturn$1$3);
			this.$inwhite_out$11 = protocol.variablePool.newOrGetPortVariable(protocol.$inwhite_out$11);
			this.$outP2$12 = protocol.variablePool.newOrGetPortVariable(protocol.$outP2$12);
			this.memoryCell2$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell2);
			this.memoryCell4$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell4);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$inwhite_out$11.resetPortBuffer();
			$outP2$12.resetPortBuffer();
			WhiteReturn$1$3.importValue();
			WhiteReturn$1$2.importValue();
			WhiteReturn$1$1.importValue();
			memoryCell2$pre.importValue();
			$outP2$12.setValue(memoryCell2$pre.getValue());
			WhiteMove$1.setValue(nl.cwi.pr.runtime.examples.thesis.chess.Functions.majority(nl.cwi.pr.runtime.examples.thesis.chess.Functions.concatenate(nl.cwi.pr.runtime.examples.thesis.chess.Functions.parse(WhiteReturn$1$3.getValue()), nl.cwi.pr.runtime.examples.thesis.chess.Functions.concatenate(nl.cwi.pr.runtime.examples.thesis.chess.Functions.parse(WhiteReturn$1$2.getValue()), nl.cwi.pr.runtime.examples.thesis.chess.Functions.parse(WhiteReturn$1$1.getValue())))));
			$inwhite_out$11.setValue(nl.cwi.pr.runtime.examples.thesis.chess.Functions.concatenate($outP2$12.getValue(), WhiteMove$1.getValue()));
			memoryCell4$post.setValue($inwhite_out$11.getValue());
			if (!nl.cwi.pr.runtime.examples.thesis.chess.Relations.Move(WhiteMove$1.getValue()))
				return false;
			if (!nl.cwi.pr.runtime.examples.thesis.basic.Relations.True($outP2$12.getValue(), WhiteMove$1.getValue()))
				return false;
			WhiteMove$1.exportValue();
			memoryCell4$post.exportValue();
			return true;
		}
	}
}

class HandlerForWhiteMove$1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteMove$1;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteMove$1(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton62.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton62.context;
		this.WhiteMove$1 = protocol.WhiteMove$1;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton62.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteMove$1.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteMove$1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000001000);
		IO status = WhiteMove$1.status;
		WhiteMove$1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000001000);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerForWhiteReturn$1$1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteReturn$1$1;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteReturn$1$1(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton62.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton62.context;
		this.WhiteReturn$1$1 = protocol.WhiteReturn$1$1;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton62.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteReturn$1$1.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteReturn$1$1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = WhiteReturn$1$1.status;
		WhiteReturn$1$1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000001);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerForWhiteReturn$1$2 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteReturn$1$2;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteReturn$1$2(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton62.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton62.context;
		this.WhiteReturn$1$2 = protocol.WhiteReturn$1$2;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton62.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteReturn$1$2.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteReturn$1$2.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000010);
		IO status = WhiteReturn$1$2.status;
		WhiteReturn$1$2.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000010);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerForWhiteReturn$1$3 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteReturn$1$3;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteReturn$1$3(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton62.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton62.context;
		this.WhiteReturn$1$3 = protocol.WhiteReturn$1$3;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton62.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteReturn$1$3.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteReturn$1$3.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000100);
		IO status = WhiteReturn$1$3.status;
		WhiteReturn$1$3.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000100);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$inwhite_out$11 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$inwhite_out$11(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton62.semaphore, protocol.automaton62.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton62.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$outP2$12 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton62_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$outP2$12(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton62.semaphore, protocol.automaton62.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton62.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton65 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton65_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton65() {
		super(1, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton65_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton65_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Public ports
	 */

	PublicPort BlackCall$1;

	/*
	 * Private ports
	 */

	PrivatePort $inP1$36;
	PrivatePort $outblack_in$11;

	/*
	 * Transitions
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton65_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

			

	/*
	 * Fairness indices for observable transitions
	 */

			
	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton65_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_d20170125_t112821_985_Chess_Automaton65_Transition2();

		/*
		 * Set observable transitions per port
		 */

						
		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton65.state1;
		/*
		 * Set ports 
		 */

		this.$inP1$36 = protocol.$inP1$36;
		this.$outblack_in$11 = protocol.$outblack_in$11;
		this.BlackCall$1 = protocol.BlackCall$1;

		/*
		 * Initialize transitions
		 */

		this.transition2.initialize(protocol);

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Unblock public ports
		 */

		BlackCall$1.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$inP1$36.kickMaster();
		$outblack_in$11.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton65_Transition2 extends Transition {

	//
	// FIELDS
	//

	/*
	 * Context
	 */

	Context context;

	/*
	 * Public ports
	 */

	PublicPort BlackCall$1;

	/*
	 * Private ports
	 */

	PrivatePort $inP1$36;
	PrivatePort $outblack_in$11;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton65_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton26 automaton26;
	Protocol_d20170125_t112821_985_Chess_Automaton31 automaton31;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton26.semaphore.acquireUninterruptibly();
		//automaton31.semaphore.acquireUninterruptibly();

		/*
		 * Check synchronization/data constraint
		 */

		boolean canFire = checkSynchronizationSet() && checkDataConstraint();

		/*
		 * Finalize transition
		 */

		if (canFire) {

			/*
			 * Update context
			 */

			context.remove(0, 0b00000000000000000000000000000001);

			/*
			 * Unblock ports
			 */

			BlackCall$1.status = IO.COMPLETED;
			BlackCall$1.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton26.state1.reach();
			automaton31.state2.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton26.semaphore.release();
		//automaton31.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton65.context;

		/*
		 * Set ports 
		 */

		this.$inP1$36 = protocol.$inP1$36;
		this.$outblack_in$11 = protocol.$outblack_in$11;
		this.BlackCall$1 = protocol.BlackCall$1;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton65.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton26 = protocol.automaton26;
		this.automaton31 = protocol.automaton31;

		/*
		 * Return
		 */

		 return;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected boolean checkDataConstraint() {
		return dataConstraint.solve();
	}

	protected boolean checkSynchronizationSet() {
		return true
			&& context.contains(0, 0b00000000000000000000000000000001)
			&& automaton26.isCurrentState2()			&& automaton31.isCurrentState1();
	}

	//
	// CLASSES
	//

	class DataConstraint {

		//
		// FIELDS
		//

		/*
		 * Public port variables
		 */

		final private CspPortVariable BlackCall$1;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $inP1$36;
		final private CspPortVariable $outblack_in$11;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell4$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell6$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_d20170125_t112821_985_Chess protocol) {

			/*
			 * Set variables
			 */

			this.BlackCall$1 = protocol.variablePool.newOrGetPortVariable(protocol.BlackCall$1);
			this.$inP1$36 = protocol.variablePool.newOrGetPortVariable(protocol.$inP1$36);
			this.$outblack_in$11 = protocol.variablePool.newOrGetPortVariable(protocol.$outblack_in$11);
			this.memoryCell4$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell4);
			this.memoryCell6$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell6);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$inP1$36.resetPortBuffer();
			$outblack_in$11.resetPortBuffer();
			memoryCell4$pre.importValue();
			$outblack_in$11.setValue(memoryCell4$pre.getValue());
			$inP1$36.setValue($outblack_in$11.getValue());
			BlackCall$1.setValue($outblack_in$11.getValue());
			memoryCell6$post.setValue($inP1$36.getValue());
			memoryCell6$post.exportValue();
			BlackCall$1.exportValue();
			return true;
		}
	}
}

class HandlerForBlackCall$1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort BlackCall$1;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton65_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForBlackCall$1(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton65.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton65.context;
		this.BlackCall$1 = protocol.BlackCall$1;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton65.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (BlackCall$1.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		BlackCall$1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = BlackCall$1.status;
		BlackCall$1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000001);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$inP1$36 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton65_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$inP1$36(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton65.semaphore, protocol.automaton65.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton65.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$outblack_in$11 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton65_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$outblack_in$11(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton65.semaphore, protocol.automaton65.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton65.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton76 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton76_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton76() {
		super(2, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton76_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton76_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Public ports
	 */

	PublicPort BlackMove$1;
	PublicPort BlackReturn$1;

	/*
	 * Private ports
	 */

	PrivatePort $inblack_out$11;
	PrivatePort $outP2$36;

	/*
	 * Transitions
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton76_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

				

	/*
	 * Fairness indices for observable transitions
	 */

				
	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton76_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_d20170125_t112821_985_Chess_Automaton76_Transition2();

		/*
		 * Set observable transitions per port
		 */

								
		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton76.state1;
		/*
		 * Set ports 
		 */

		this.$inblack_out$11 = protocol.$inblack_out$11;
		this.$outP2$36 = protocol.$outP2$36;
		this.BlackMove$1 = protocol.BlackMove$1;
		this.BlackReturn$1 = protocol.BlackReturn$1;

		/*
		 * Initialize transitions
		 */

		this.transition2.initialize(protocol);

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Unblock public ports
		 */

		BlackMove$1.semaphore.release();
		BlackReturn$1.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$inblack_out$11.kickMaster();
		$outP2$36.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton76_Transition2 extends Transition {

	//
	// FIELDS
	//

	/*
	 * Context
	 */

	Context context;

	/*
	 * Public ports
	 */

	PublicPort BlackMove$1;
	PublicPort BlackReturn$1;

	/*
	 * Private ports
	 */

	PrivatePort $inblack_out$11;
	PrivatePort $outP2$36;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton76_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton31 automaton31;
	Protocol_d20170125_t112821_985_Chess_Automaton46 automaton46;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton31.semaphore.acquireUninterruptibly();
		//automaton46.semaphore.acquireUninterruptibly();

		/*
		 * Check synchronization/data constraint
		 */

		boolean canFire = checkSynchronizationSet() && checkDataConstraint();

		/*
		 * Finalize transition
		 */

		if (canFire) {

			/*
			 * Update context
			 */

			context.remove(0, 0b00000000000000000000000000000011);

			/*
			 * Unblock ports
			 */

			BlackMove$1.status = IO.COMPLETED;
			BlackMove$1.semaphore.release();
			BlackReturn$1.status = IO.COMPLETED;
			BlackReturn$1.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton31.state1.reach();
			automaton46.state2.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton31.semaphore.release();
		//automaton46.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton76.context;

		/*
		 * Set ports 
		 */

		this.$inblack_out$11 = protocol.$inblack_out$11;
		this.$outP2$36 = protocol.$outP2$36;
		this.BlackMove$1 = protocol.BlackMove$1;
		this.BlackReturn$1 = protocol.BlackReturn$1;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton76.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton31 = protocol.automaton31;
		this.automaton46 = protocol.automaton46;

		/*
		 * Return
		 */

		 return;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected boolean checkDataConstraint() {
		return dataConstraint.solve();
	}

	protected boolean checkSynchronizationSet() {
		return true
			&& context.contains(0, 0b00000000000000000000000000000011)
			&& automaton31.isCurrentState2()			&& automaton46.isCurrentState1();
	}

	//
	// CLASSES
	//

	class DataConstraint {

		//
		// FIELDS
		//

		/*
		 * Public port variables
		 */

		final private CspPortVariable BlackMove$1;
		final private CspPortVariable BlackReturn$1;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $inblack_out$11;
		final private CspPortVariable $outP2$36;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell6$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell8$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_d20170125_t112821_985_Chess protocol) {

			/*
			 * Set variables
			 */

			this.BlackMove$1 = protocol.variablePool.newOrGetPortVariable(protocol.BlackMove$1);
			this.BlackReturn$1 = protocol.variablePool.newOrGetPortVariable(protocol.BlackReturn$1);
			this.$inblack_out$11 = protocol.variablePool.newOrGetPortVariable(protocol.$inblack_out$11);
			this.$outP2$36 = protocol.variablePool.newOrGetPortVariable(protocol.$outP2$36);
			this.memoryCell6$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell6);
			this.memoryCell8$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell8);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$inblack_out$11.resetPortBuffer();
			$outP2$36.resetPortBuffer();
			BlackReturn$1.importValue();
			memoryCell6$pre.importValue();
			$outP2$36.setValue(memoryCell6$pre.getValue());
			BlackMove$1.setValue(nl.cwi.pr.runtime.examples.thesis.chess.Functions.majority(nl.cwi.pr.runtime.examples.thesis.chess.Functions.parse(BlackReturn$1.getValue())));
			$inblack_out$11.setValue(nl.cwi.pr.runtime.examples.thesis.chess.Functions.concatenate($outP2$36.getValue(), BlackMove$1.getValue()));
			memoryCell8$post.setValue($inblack_out$11.getValue());
			if (!nl.cwi.pr.runtime.examples.thesis.chess.Relations.Move(BlackMove$1.getValue()))
				return false;
			if (!nl.cwi.pr.runtime.examples.thesis.basic.Relations.True($outP2$36.getValue(), BlackMove$1.getValue()))
				return false;
			BlackMove$1.exportValue();
			memoryCell8$post.exportValue();
			return true;
		}
	}
}

class HandlerForBlackMove$1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort BlackMove$1;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton76_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForBlackMove$1(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton76.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton76.context;
		this.BlackMove$1 = protocol.BlackMove$1;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton76.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (BlackMove$1.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		BlackMove$1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000010);
		IO status = BlackMove$1.status;
		BlackMove$1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000010);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerForBlackReturn$1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort BlackReturn$1;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton76_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForBlackReturn$1(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton76.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton76.context;
		this.BlackReturn$1 = protocol.BlackReturn$1;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton76.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (BlackReturn$1.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		BlackReturn$1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = BlackReturn$1.status;
		BlackReturn$1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000001);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$inblack_out$11 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton76_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$inblack_out$11(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton76.semaphore, protocol.automaton76.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton76.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$outP2$36 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton76_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$outP2$36(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton76.semaphore, protocol.automaton76.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton76.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton80 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton80() {
		super(3, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_d20170125_t112821_985_Chess_Automaton80_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Initialize states
		 */

		this.state1.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state1.reach();

		/*
		 * Return
		 */

		return;
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton80_State1 extends State {

	//
	// FIELDS
	//

	/*
	 * Successor
	 */

	State successor;

	/*
	 * Public ports
	 */

	PublicPort WhiteCall$1$1;
	PublicPort WhiteCall$1$2;
	PublicPort WhiteCall$1$3;

	/*
	 * Private ports
	 */

	PrivatePort $inP1$12;
	PrivatePort $outwhite_in$11;

	/*
	 * Transitions
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

					

	/*
	 * Fairness indices for observable transitions
	 */

					
	//
	// CONSTRUCTORS
	//

	public Protocol_d20170125_t112821_985_Chess_Automaton80_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_d20170125_t112821_985_Chess_Automaton80_Transition2();

		/*
		 * Set observable transitions per port
		 */

										
		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton80.state1;
		/*
		 * Set ports 
		 */

		this.$inP1$12 = protocol.$inP1$12;
		this.$outwhite_in$11 = protocol.$outwhite_in$11;
		this.WhiteCall$1$1 = protocol.WhiteCall$1$1;
		this.WhiteCall$1$2 = protocol.WhiteCall$1$2;
		this.WhiteCall$1$3 = protocol.WhiteCall$1$3;

		/*
		 * Initialize transitions
		 */

		this.transition2.initialize(protocol);

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reach() {

		/*
		 * Unblock public ports
		 */

		WhiteCall$1$1.semaphore.release();
		WhiteCall$1$2.semaphore.release();
		WhiteCall$1$3.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$inP1$12.kickMaster();
		$outwhite_in$11.kickMaster();

		/*
		 * Return
		 */

		return;
	}

	@Override
	public void reachSuccessor() {
		successor.reach();
	}
}

class Protocol_d20170125_t112821_985_Chess_Automaton80_Transition2 extends Transition {

	//
	// FIELDS
	//

	/*
	 * Context
	 */

	Context context;

	/*
	 * Public ports
	 */

	PublicPort WhiteCall$1$1;
	PublicPort WhiteCall$1$2;
	PublicPort WhiteCall$1$3;

	/*
	 * Private ports
	 */

	PrivatePort $inP1$12;
	PrivatePort $outwhite_in$11;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton80_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_d20170125_t112821_985_Chess_Automaton5 automaton5;
	Protocol_d20170125_t112821_985_Chess_Automaton46 automaton46;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton5.semaphore.acquireUninterruptibly();
		//automaton46.semaphore.acquireUninterruptibly();

		/*
		 * Check synchronization/data constraint
		 */

		boolean canFire = checkSynchronizationSet() && checkDataConstraint();

		/*
		 * Finalize transition
		 */

		if (canFire) {

			/*
			 * Update context
			 */

			context.remove(0, 0b00000000000000000000000000000111);

			/*
			 * Unblock ports
			 */

			WhiteCall$1$1.status = IO.COMPLETED;
			WhiteCall$1$1.semaphore.release();
			WhiteCall$1$2.status = IO.COMPLETED;
			WhiteCall$1$2.semaphore.release();
			WhiteCall$1$3.status = IO.COMPLETED;
			WhiteCall$1$3.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton5.state2.reach();
			automaton46.state1.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton5.semaphore.release();
		//automaton46.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_d20170125_t112821_985_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton80.context;

		/*
		 * Set ports 
		 */

		this.$inP1$12 = protocol.$inP1$12;
		this.$outwhite_in$11 = protocol.$outwhite_in$11;
		this.WhiteCall$1$1 = protocol.WhiteCall$1$1;
		this.WhiteCall$1$2 = protocol.WhiteCall$1$2;
		this.WhiteCall$1$3 = protocol.WhiteCall$1$3;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton80.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton5 = protocol.automaton5;
		this.automaton46 = protocol.automaton46;

		/*
		 * Return
		 */

		 return;
	}

	//
	// METHODS - PROTECTED
	//

	@Override
	protected boolean checkDataConstraint() {
		return dataConstraint.solve();
	}

	protected boolean checkSynchronizationSet() {
		return true
			&& context.contains(0, 0b00000000000000000000000000000111)
			&& automaton5.isCurrentState1()			&& automaton46.isCurrentState2();
	}

	//
	// CLASSES
	//

	class DataConstraint {

		//
		// FIELDS
		//

		/*
		 * Public port variables
		 */

		final private CspPortVariable WhiteCall$1$1;
		final private CspPortVariable WhiteCall$1$2;
		final private CspPortVariable WhiteCall$1$3;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $inP1$12;
		final private CspPortVariable $outwhite_in$11;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell8$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell2$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_d20170125_t112821_985_Chess protocol) {

			/*
			 * Set variables
			 */

			this.WhiteCall$1$1 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteCall$1$1);
			this.WhiteCall$1$2 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteCall$1$2);
			this.WhiteCall$1$3 = protocol.variablePool.newOrGetPortVariable(protocol.WhiteCall$1$3);
			this.$inP1$12 = protocol.variablePool.newOrGetPortVariable(protocol.$inP1$12);
			this.$outwhite_in$11 = protocol.variablePool.newOrGetPortVariable(protocol.$outwhite_in$11);
			this.memoryCell8$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell8);
			this.memoryCell2$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell2);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$inP1$12.resetPortBuffer();
			$outwhite_in$11.resetPortBuffer();
			memoryCell8$pre.importValue();
			$outwhite_in$11.setValue(memoryCell8$pre.getValue());
			$inP1$12.setValue($outwhite_in$11.getValue());
			WhiteCall$1$1.setValue($inP1$12.getValue());
			WhiteCall$1$2.setValue(WhiteCall$1$1.getValue());
			memoryCell2$post.setValue($inP1$12.getValue());
			WhiteCall$1$3.setValue(WhiteCall$1$2.getValue());
			memoryCell2$post.exportValue();
			WhiteCall$1$1.exportValue();
			WhiteCall$1$2.exportValue();
			WhiteCall$1$3.exportValue();
			return true;
		}
	}
}

class HandlerForWhiteCall$1$1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteCall$1$1;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteCall$1$1(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton80.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton80.context;
		this.WhiteCall$1$1 = protocol.WhiteCall$1$1;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton80.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteCall$1$1.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteCall$1$1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = WhiteCall$1$1.status;
		WhiteCall$1$1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000001);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerForWhiteCall$1$2 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteCall$1$2;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteCall$1$2(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton80.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton80.context;
		this.WhiteCall$1$2 = protocol.WhiteCall$1$2;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton80.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteCall$1$2.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteCall$1$2.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000010);
		IO status = WhiteCall$1$2.status;
		WhiteCall$1$2.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000010);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerForWhiteCall$1$3 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort WhiteCall$1$3;

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerForWhiteCall$1$3(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton80.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton80.context;
		this.WhiteCall$1$3 = protocol.WhiteCall$1$3;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton80.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (WhiteCall$1$3.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		WhiteCall$1$3.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000100);
		IO status = WhiteCall$1$3.status;
		WhiteCall$1$3.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000100);
	}




	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$inP1$12 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$inP1$12(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton80.semaphore, protocol.automaton80.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton80.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}

class HandlerFor$outwhite_in$11 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_d20170125_t112821_985_Chess_Automaton80_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$outwhite_in$11(Protocol_d20170125_t112821_985_Chess protocol) {
		super(protocol.automaton80.semaphore, protocol.automaton80.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton80.state1;

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	@Override
	public boolean call() {
		if (fireTransitionFromState1())
			return true;

		return false;
	}

	@Override
	public void flag() {
	}
	private boolean fireTransitionFromState1() {
		return state1.transition2.fire();
	}
}
