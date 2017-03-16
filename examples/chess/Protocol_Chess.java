import java.util.*;
import java.util.concurrent.atomic.*;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Protocol_Chess {

	//
	// FIELDS
	//

	/*
	 * Automata
	 */

	final Protocol_Chess_Automaton4 automaton4;
	final Protocol_Chess_Automaton16 automaton16;
	final Protocol_Chess_Automaton20 automaton20;
	final Protocol_Chess_Automaton32 automaton32;
	final Protocol_Chess_Automaton43 automaton43;
	final Protocol_Chess_Automaton54 automaton54;
	final Protocol_Chess_Automaton57 automaton57;
	final Protocol_Chess_Automaton68 automaton68;

	/*
	 * Public ports
	 */

	final PublicPort _13;
	final PublicPort _16;
	final PublicPort _17;
	final PublicPort _18;
	final PublicPort _19;
	final PublicPort _3;

	/*
	 * Private ports
	 */

	final PrivatePort $in_10_1;
	final PrivatePort $in_15;
	final PrivatePort $in_2_0;
	final PrivatePort $in_20;
	final PrivatePort $out_10;
	final PrivatePort $out_14;
	final PrivatePort $out_2;
	final PrivatePort $out_7;

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

	public Protocol_Chess(
		Port _19,
		Port _3,
		Port _13,
		Port _16,
		Port _17,
		Port _18
	) {

		/*
		 * Set public ports
		 */

		this._13 = (PublicPort) _13;
		this._16 = (PublicPort) _16;
		this._17 = (PublicPort) _17;
		this._18 = (PublicPort) _18;
		this._19 = (PublicPort) _19;
		this._3 = (PublicPort) _3;

		/*
		 * Set private ports
		 */

		this.$in_10_1 = new PrivatePort();
		this.$in_15 = new PrivatePort();
		this.$in_2_0 = new PrivatePort();
		this.$in_20 = new PrivatePort();
		this.$out_10 = new PrivatePort();
		this.$out_14 = new PrivatePort();
		this.$out_2 = new PrivatePort();
		this.$out_7 = new PrivatePort();

		/*
		 * Set automata
		 */

		this.automaton4 = new Protocol_Chess_Automaton4();
		this.automaton16 = new Protocol_Chess_Automaton16();
		this.automaton20 = new Protocol_Chess_Automaton20();
		this.automaton32 = new Protocol_Chess_Automaton32();
		this.automaton43 = new Protocol_Chess_Automaton43();
		this.automaton54 = new Protocol_Chess_Automaton54();
		this.automaton57 = new Protocol_Chess_Automaton57();
		this.automaton68 = new Protocol_Chess_Automaton68();

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
		 * Initialize ports in automaton #43
		 */

		{
			this._18.handler = new HandlerFor_18(this);
			this.$in_10_1.masterHandler = new HandlerFor$in_10_1(this);
			this.$out_10.masterHandler = new HandlerFor$out_10(this);
		}


		/*
		 * Initialize ports in automaton #54
		 */

		{
			this._3.handler = new HandlerFor_3(this);
			this._16.handler = new HandlerFor_16(this);
			this.$in_15.masterHandler = new HandlerFor$in_15(this);
			this.$out_14.masterHandler = new HandlerFor$out_14(this);
		}


		/*
		 * Initialize ports in automaton #57
		 */

		{
			this._13.handler = new HandlerFor_13(this);
			this.$in_2_0.masterHandler = new HandlerFor$in_2_0(this);
			this.$out_2.masterHandler = new HandlerFor$out_2(this);
		}


		/*
		 * Initialize ports in automaton #68
		 */

		{
			this._19.handler = new HandlerFor_19(this);
			this._17.handler = new HandlerFor_17(this);
			this.$in_20.masterHandler = new HandlerFor$in_20(this);
			this.$out_7.masterHandler = new HandlerFor$out_7(this);
		}

		/*
		 * Initialize automata
		 */

		this.automaton4.initialize(this);
		this.automaton16.initialize(this);
		this.automaton20.initialize(this);
		this.automaton32.initialize(this);
		this.automaton43.initialize(this);
		this.automaton54.initialize(this);
		this.automaton57.initialize(this);
		this.automaton68.initialize(this);
	}

	public void start() {
		this.automaton4.start();
		this.automaton16.start();
		this.automaton20.start();
		this.automaton32.start();
		this.automaton43.start();
		this.automaton54.start();
		this.automaton57.start();
		this.automaton68.start();
	}

	//
	// MAIN
	//

	public static void main(String[] args) {
		OutputPort _19 = Ports.newOutputPort();
		OutputPort _3 = Ports.newOutputPort();
		InputPort _13 = Ports.newInputPort();
		InputPort _16 = Ports.newInputPort();
		InputPort _17 = Ports.newInputPort();
		InputPort _18 = Ports.newInputPort();

		new Protocol_Chess(
			_19,
			_3,
			_13,
			_16,
			_17,
			_18
		);

		Map<String, Port> inputPorts = new HashMap<>();
		inputPorts.put("_19", (Port) _19);
		inputPorts.put("_3", (Port) _3);

		Map<String, Port> outputPorts = new HashMap<>();
		outputPorts.put("_13", (Port) _13);
		outputPorts.put("_16", (Port) _16);
		outputPorts.put("_17", (Port) _17);
		outputPorts.put("_18", (Port) _18);

		new Thread(new Console(inputPorts, outputPorts)).start();
	}
}

class Protocol_Chess_Automaton4 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton4_State1 state1;
	final Protocol_Chess_Automaton4_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton4() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton4_State1();
		this.state2 = new Protocol_Chess_Automaton4_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton4_State1 extends State {

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

	PrivatePort $in_2_0;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton4.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton4.state2;
		/*
		 * Set ports 
		 */

		this.$in_2_0 = protocol.$in_2_0;

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

		$in_2_0.kickMaster();

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

class Protocol_Chess_Automaton4_State2 extends State {

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

	PrivatePort $out_7;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton4.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton4.state1;
		/*
		 * Set ports 
		 */

		this.$out_7 = protocol.$out_7;

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

		$out_7.kickMaster();

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

class Protocol_Chess_Automaton16 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton16_State1 state1;
	final Protocol_Chess_Automaton16_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton16() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton16_State1();
		this.state2 = new Protocol_Chess_Automaton16_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton16_State1 extends State {

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

	PrivatePort $in_20;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton16.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton16.state2;
		/*
		 * Set ports 
		 */

		this.$in_20 = protocol.$in_20;

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

		$in_20.kickMaster();

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

class Protocol_Chess_Automaton16_State2 extends State {

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

	PrivatePort $out_10;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton16.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton16.state1;
		/*
		 * Set ports 
		 */

		this.$out_10 = protocol.$out_10;

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

		$out_10.kickMaster();

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

class Protocol_Chess_Automaton20 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton20_State1 state1;
	final Protocol_Chess_Automaton20_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton20() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton20_State1();
		this.state2 = new Protocol_Chess_Automaton20_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton20_State1 extends State {

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

	PrivatePort $in_10_1;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton20.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton20.state2;
		/*
		 * Set ports 
		 */

		this.$in_10_1 = protocol.$in_10_1;

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

		$in_10_1.kickMaster();

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

class Protocol_Chess_Automaton20_State2 extends State {

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

	PrivatePort $out_14;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton20.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton20.state1;
		/*
		 * Set ports 
		 */

		this.$out_14 = protocol.$out_14;

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

		$out_14.kickMaster();

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

class Protocol_Chess_Automaton32 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton32_State1 state1;
	final Protocol_Chess_Automaton32_State2 state2;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton32() {
		super(0);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton32_State1();
		this.state2 = new Protocol_Chess_Automaton32_State2();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton32_State1 extends State {

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

	PrivatePort $in_15;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton32.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton32.state2;
		/*
		 * Set ports 
		 */

		this.$in_15 = protocol.$in_15;

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

		$in_15.kickMaster();

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

class Protocol_Chess_Automaton32_State2 extends State {

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

	PrivatePort $out_2;


	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set current state
		 */

		this.current = protocol.automaton32.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton32.state1;
		/*
		 * Set ports 
		 */

		this.$out_2 = protocol.$out_2;

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

		$out_2.kickMaster();

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

class Protocol_Chess_Automaton43 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton43_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton43() {
		super(1, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton43_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton43_State1 extends State {

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

	PublicPort _18;

	/*
	 * Private ports
	 */

	PrivatePort $in_10_1;
	PrivatePort $out_10;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton43_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

			

	/*
	 * Fairness indices for observable transitions
	 */

			
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton43_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_Chess_Automaton43_Transition2();

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

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton43.state1;
		/*
		 * Set ports 
		 */

		this._18 = protocol._18;
		this.$in_10_1 = protocol.$in_10_1;
		this.$out_10 = protocol.$out_10;

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

		_18.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$in_10_1.kickMaster();
		$out_10.kickMaster();

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

class Protocol_Chess_Automaton43_Transition2 extends Transition {

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

	PublicPort _18;

	/*
	 * Private ports
	 */

	PrivatePort $in_10_1;
	PrivatePort $out_10;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton43_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_Chess_Automaton16 automaton16;
	Protocol_Chess_Automaton20 automaton20;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton16.semaphore.acquireUninterruptibly();
		//automaton20.semaphore.acquireUninterruptibly();

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

			_18.status = IO.COMPLETED;
			_18.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton16.state1.reach();
			automaton20.state2.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton16.semaphore.release();
		//automaton20.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton43.context;

		/*
		 * Set ports 
		 */

		this._18 = protocol._18;
		this.$in_10_1 = protocol.$in_10_1;
		this.$out_10 = protocol.$out_10;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton43.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton16 = protocol.automaton16;
		this.automaton20 = protocol.automaton20;

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
			&& automaton16.isCurrentState2()			&& automaton20.isCurrentState1();
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

		final private CspPortVariable _18;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $in_10_1;
		final private CspPortVariable $out_10;

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

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this._18 = protocol.variablePool.newOrGetPortVariable(protocol._18);
			this.$in_10_1 = protocol.variablePool.newOrGetPortVariable(protocol.$in_10_1);
			this.$out_10 = protocol.variablePool.newOrGetPortVariable(protocol.$out_10);
			this.memoryCell4$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell4);
			this.memoryCell6$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell6);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$in_10_1.resetPortBuffer();
			$out_10.resetPortBuffer();
			memoryCell4$pre.importValue();
			$out_10.setValue(memoryCell4$pre.getValue());
			$in_10_1.setValue($out_10.getValue());
			_18.setValue($out_10.getValue());
			memoryCell6$post.setValue($in_10_1.getValue());
			memoryCell6$post.exportValue();
			_18.exportValue();
			return true;
		}
	}
}

class HandlerFor_18 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort _18;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton43_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerFor_18(Protocol_Chess protocol) {
		super(protocol.automaton43.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton43.context;
		this._18 = protocol._18;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton43.state1;

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
		if (_18.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		_18.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = _18.status;
		_18.status = null;
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

class HandlerFor$in_10_1 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton43_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$in_10_1(Protocol_Chess protocol) {
		super(protocol.automaton43.semaphore, protocol.automaton43.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton43.state1;

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

class HandlerFor$out_10 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton43_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$out_10(Protocol_Chess protocol) {
		super(protocol.automaton43.semaphore, protocol.automaton43.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton43.state1;

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

class Protocol_Chess_Automaton54 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton54_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton54() {
		super(2, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton54_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton54_State1 extends State {

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

	PublicPort _16;
	PublicPort _3;

	/*
	 * Private ports
	 */

	PrivatePort $in_15;
	PrivatePort $out_14;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton54_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

				

	/*
	 * Fairness indices for observable transitions
	 */

				
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton54_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_Chess_Automaton54_Transition2();

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

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton54.state1;
		/*
		 * Set ports 
		 */

		this._16 = protocol._16;
		this._3 = protocol._3;
		this.$in_15 = protocol.$in_15;
		this.$out_14 = protocol.$out_14;

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

		_16.semaphore.release();
		_3.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$in_15.kickMaster();
		$out_14.kickMaster();

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

class Protocol_Chess_Automaton54_Transition2 extends Transition {

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

	PublicPort _16;
	PublicPort _3;

	/*
	 * Private ports
	 */

	PrivatePort $in_15;
	PrivatePort $out_14;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton54_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_Chess_Automaton20 automaton20;
	Protocol_Chess_Automaton32 automaton32;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton20.semaphore.acquireUninterruptibly();
		//automaton32.semaphore.acquireUninterruptibly();

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

			_16.status = IO.COMPLETED;
			_16.semaphore.release();
			_3.status = IO.COMPLETED;
			_3.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton20.state1.reach();
			automaton32.state2.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton20.semaphore.release();
		//automaton32.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton54.context;

		/*
		 * Set ports 
		 */

		this._16 = protocol._16;
		this._3 = protocol._3;
		this.$in_15 = protocol.$in_15;
		this.$out_14 = protocol.$out_14;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton54.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton20 = protocol.automaton20;
		this.automaton32 = protocol.automaton32;

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
			&& automaton20.isCurrentState2()			&& automaton32.isCurrentState1();
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

		final private CspPortVariable _16;
		final private CspPortVariable _3;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $in_15;
		final private CspPortVariable $out_14;

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

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this._16 = protocol.variablePool.newOrGetPortVariable(protocol._16);
			this._3 = protocol.variablePool.newOrGetPortVariable(protocol._3);
			this.$in_15 = protocol.variablePool.newOrGetPortVariable(protocol.$in_15);
			this.$out_14 = protocol.variablePool.newOrGetPortVariable(protocol.$out_14);
			this.memoryCell6$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell6);
			this.memoryCell8$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell8);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$in_15.resetPortBuffer();
			$out_14.resetPortBuffer();
			_3.importValue();
			memoryCell6$pre.importValue();
			$out_14.setValue(memoryCell6$pre.getValue());
			_16.setValue(Functions.majority(Functions.parse(_3.getValue())));
			if (!Relations.Move(_16.getValue()))
				return false;
			$in_15.setValue(Functions.concatenate($out_14.getValue(), _16.getValue()));
			memoryCell8$post.setValue($in_15.getValue());
			_16.exportValue();
			memoryCell8$post.exportValue();
			return true;
		}
	}
}

class HandlerFor_16 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort _16;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton54_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerFor_16(Protocol_Chess protocol) {
		super(protocol.automaton54.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton54.context;
		this._16 = protocol._16;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton54.state1;

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
		if (_16.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		_16.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000010);
		IO status = _16.status;
		_16.status = null;
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

class HandlerFor_3 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort _3;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton54_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerFor_3(Protocol_Chess protocol) {
		super(protocol.automaton54.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton54.context;
		this._3 = protocol._3;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton54.state1;

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
		if (_3.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		_3.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = _3.status;
		_3.status = null;
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

class HandlerFor$in_15 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton54_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$in_15(Protocol_Chess protocol) {
		super(protocol.automaton54.semaphore, protocol.automaton54.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton54.state1;

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

class HandlerFor$out_14 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton54_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$out_14(Protocol_Chess protocol) {
		super(protocol.automaton54.semaphore, protocol.automaton54.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton54.state1;

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

class Protocol_Chess_Automaton57 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton57_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton57() {
		super(1, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton57_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton57_State1 extends State {

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

	PublicPort _13;

	/*
	 * Private ports
	 */

	PrivatePort $in_2_0;
	PrivatePort $out_2;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton57_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

			

	/*
	 * Fairness indices for observable transitions
	 */

			
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton57_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_Chess_Automaton57_Transition2();

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

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton57.state1;
		/*
		 * Set ports 
		 */

		this._13 = protocol._13;
		this.$in_2_0 = protocol.$in_2_0;
		this.$out_2 = protocol.$out_2;

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

		_13.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$in_2_0.kickMaster();
		$out_2.kickMaster();

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

class Protocol_Chess_Automaton57_Transition2 extends Transition {

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

	PublicPort _13;

	/*
	 * Private ports
	 */

	PrivatePort $in_2_0;
	PrivatePort $out_2;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton57_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_Chess_Automaton4 automaton4;
	Protocol_Chess_Automaton32 automaton32;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton4.semaphore.acquireUninterruptibly();
		//automaton32.semaphore.acquireUninterruptibly();

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

			_13.status = IO.COMPLETED;
			_13.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton4.state2.reach();
			automaton32.state1.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton4.semaphore.release();
		//automaton32.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton57.context;

		/*
		 * Set ports 
		 */

		this._13 = protocol._13;
		this.$in_2_0 = protocol.$in_2_0;
		this.$out_2 = protocol.$out_2;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton57.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton4 = protocol.automaton4;
		this.automaton32 = protocol.automaton32;

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
			&& automaton4.isCurrentState1()			&& automaton32.isCurrentState2();
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

		final private CspPortVariable _13;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $in_2_0;
		final private CspPortVariable $out_2;

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

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this._13 = protocol.variablePool.newOrGetPortVariable(protocol._13);
			this.$in_2_0 = protocol.variablePool.newOrGetPortVariable(protocol.$in_2_0);
			this.$out_2 = protocol.variablePool.newOrGetPortVariable(protocol.$out_2);
			this.memoryCell8$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell8);
			this.memoryCell2$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell2);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$in_2_0.resetPortBuffer();
			$out_2.resetPortBuffer();
			memoryCell8$pre.importValue();
			$out_2.setValue(memoryCell8$pre.getValue());
			$in_2_0.setValue($out_2.getValue());
			_13.setValue($out_2.getValue());
			memoryCell2$post.setValue($in_2_0.getValue());
			memoryCell2$post.exportValue();
			_13.exportValue();
			return true;
		}
	}
}

class HandlerFor_13 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort _13;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton57_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerFor_13(Protocol_Chess protocol) {
		super(protocol.automaton57.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton57.context;
		this._13 = protocol._13;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton57.state1;

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
		if (_13.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		_13.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = _13.status;
		_13.status = null;
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

class HandlerFor$in_2_0 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton57_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$in_2_0(Protocol_Chess protocol) {
		super(protocol.automaton57.semaphore, protocol.automaton57.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton57.state1;

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

class HandlerFor$out_2 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton57_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$out_2(Protocol_Chess protocol) {
		super(protocol.automaton57.semaphore, protocol.automaton57.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton57.state1;

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

class Protocol_Chess_Automaton68 extends AutomatonWithQhq {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton68_State1 state1;

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton68() {
		super(2, 2);

		/*
		 * Set states
		 */

		this.state1 = new Protocol_Chess_Automaton68_State1();

		/*
		 * Return
		 */

		return;
	}

	//
	// METHODS
	//

	public void initialize(Protocol_Chess protocol) {

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

class Protocol_Chess_Automaton68_State1 extends State {

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

	PublicPort _17;
	PublicPort _19;

	/*
	 * Private ports
	 */

	PrivatePort $in_20;
	PrivatePort $out_7;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton68_Transition2 transition2;

	/*
	 * Observable transitions per port
	 */

				

	/*
	 * Fairness indices for observable transitions
	 */

				
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton68_State1() {

		/*
		 * Set transitions
		 */

		this.transition2 = new Protocol_Chess_Automaton68_Transition2();

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

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton68.state1;
		/*
		 * Set ports 
		 */

		this._17 = protocol._17;
		this._19 = protocol._19;
		this.$in_20 = protocol.$in_20;
		this.$out_7 = protocol.$out_7;

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

		_17.semaphore.release();
		_19.semaphore.release();

		/*
		 * Notify masters of private ports
		 */

		$in_20.kickMaster();
		$out_7.kickMaster();

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

class Protocol_Chess_Automaton68_Transition2 extends Transition {

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

	PublicPort _17;
	PublicPort _19;

	/*
	 * Private ports
	 */

	PrivatePort $in_20;
	PrivatePort $out_7;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton68_State1 target;

	/*
	 * Predictable slaves
	 */

	Protocol_Chess_Automaton4 automaton4;
	Protocol_Chess_Automaton16 automaton16;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

		/*
		 * Lock slaves
		 */

		//automaton4.semaphore.acquireUninterruptibly();
		//automaton16.semaphore.acquireUninterruptibly();

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

			_17.status = IO.COMPLETED;
			_17.semaphore.release();
			_19.status = IO.COMPLETED;
			_19.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();

			/*
			 * Update current states in predictable slaves
			 */

			automaton4.state1.reach();
			automaton16.state2.reach();
		}

		/*
		 * Unlock slaves
		 */

		//automaton4.semaphore.release();
		//automaton16.semaphore.release();

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton68.context;

		/*
		 * Set ports 
		 */

		this._17 = protocol._17;
		this._19 = protocol._19;
		this.$in_20 = protocol.$in_20;
		this.$out_7 = protocol.$out_7;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton68.state1;

		/*
		 * Set predictable slaves
		 */

		this.automaton4 = protocol.automaton4;
		this.automaton16 = protocol.automaton16;

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
			&& automaton4.isCurrentState2()			&& automaton16.isCurrentState1();
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

		final private CspPortVariable _17;
		final private CspPortVariable _19;

		/*
		 * Private port variables
		 */

		final private CspPortVariable $in_20;
		final private CspPortVariable $out_7;

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

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this._17 = protocol.variablePool.newOrGetPortVariable(protocol._17);
			this._19 = protocol.variablePool.newOrGetPortVariable(protocol._19);
			this.$in_20 = protocol.variablePool.newOrGetPortVariable(protocol.$in_20);
			this.$out_7 = protocol.variablePool.newOrGetPortVariable(protocol.$out_7);
			this.memoryCell2$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell2);
			this.memoryCell4$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell4);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$in_20.resetPortBuffer();
			$out_7.resetPortBuffer();
			_19.importValue();
			memoryCell2$pre.importValue();
			$out_7.setValue(memoryCell2$pre.getValue());
			_17.setValue(Functions.majority(Functions.parse(_19.getValue())));
			if (!Relations.Move(_17.getValue()))
				return false;
			$in_20.setValue(Functions.concatenate($out_7.getValue(), _17.getValue()));
			memoryCell4$post.setValue($in_20.getValue());
			_17.exportValue();
			memoryCell4$post.exportValue();
			return true;
		}
	}
}

class HandlerFor_17 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort _17;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton68_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerFor_17(Protocol_Chess protocol) {
		super(protocol.automaton68.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton68.context;
		this._17 = protocol._17;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton68.state1;

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
		if (_17.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		_17.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000010);
		IO status = _17.status;
		_17.status = null;
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

class HandlerFor_19 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort _19;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton68_State1 state1;

	//
	// CONSTRUCTORS
	//

	public HandlerFor_19(Protocol_Chess protocol) {
		super(protocol.automaton68.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton68.context;
		this._19 = protocol._19;

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton68.state1;

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
		if (_19.status == IO.COMPLETED)
			return true;

		if (fireTransitionFromState1())
			return true;

		_19.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = _19.status;
		_19.status = null;
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

class HandlerFor$in_20 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton68_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$in_20(Protocol_Chess protocol) {
		super(protocol.automaton68.semaphore, protocol.automaton68.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton68.state1;

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

class HandlerFor$out_7 extends QueueableHandler {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton68_State1 state1;

	//
	// CONSTRUCTORS
	//

	HandlerFor$out_7(Protocol_Chess protocol) {
		super(protocol.automaton68.semaphore, protocol.automaton68.qhq);

		/*
		 * Set states
		 */

		this.state1 = protocol.automaton68.state1;

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
