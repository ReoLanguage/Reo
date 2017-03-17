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

	final Protocol_Chess_Automaton69 automaton69;

	/*
	 * Public ports
	 */

	final PublicPort $_1;
	final PublicPort $_13;
	final PublicPort $_14;
	final PublicPort $_17;
	final PublicPort $_18;
	final PublicPort $_27;
	final PublicPort $_28;
	final PublicPort $_29;
	final PublicPort $_5;
	final PublicPort $_9;

	/*
	 * Memory cells
	 */

	final MemoryCell memoryCell1 = new MemoryCell();
	final MemoryCell memoryCell2 = new MemoryCell();
	final MemoryCell memoryCell3 = new MemoryCell();
	final MemoryCell memoryCell4 = new MemoryCell("");

	/*
	 * Variable pool
	 */

	final CspVariablePool variablePool = new CspVariablePool();

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess(
		Port $_1,
		Port $_18,
		Port $_28,
		Port $_29,
		Port $_13,
		Port $_14,
		Port $_17,
		Port $_27,
		Port $_5,
		Port $_9
	) {

		/*
		 * Set public ports
		 */

		this.$_1 = (PublicPort) $_1;
		this.$_13 = (PublicPort) $_13;
		this.$_14 = (PublicPort) $_14;
		this.$_17 = (PublicPort) $_17;
		this.$_18 = (PublicPort) $_18;
		this.$_27 = (PublicPort) $_27;
		this.$_28 = (PublicPort) $_28;
		this.$_29 = (PublicPort) $_29;
		this.$_5 = (PublicPort) $_5;
		this.$_9 = (PublicPort) $_9;

		/*
		 * Set automata
		 */

		this.automaton69 = new Protocol_Chess_Automaton69();

		/*
		 * Initialize
		 */

		initialize();

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
		 * Initialize ports in automaton #69
		 */

		{
			this.$_1.handler = new HandlerFor$_1(this);
			this.$_18.handler = new HandlerFor$_18(this);
			this.$_28.handler = new HandlerFor$_28(this);
			this.$_29.handler = new HandlerFor$_29(this);
			this.$_13.handler = new HandlerFor$_13(this);
			this.$_14.handler = new HandlerFor$_14(this);
			this.$_17.handler = new HandlerFor$_17(this);
			this.$_27.handler = new HandlerFor$_27(this);
			this.$_5.handler = new HandlerFor$_5(this);
			this.$_9.handler = new HandlerFor$_9(this);
		}

		/*
		 * Initialize automata
		 */

		this.automaton69.initialize(this);
	}

	//
	// MAIN
	//

	public static void main(String[] args) {
		OutputPort $_1 = Ports.newOutputPort();
		OutputPort $_18 = Ports.newOutputPort();
		OutputPort $_28 = Ports.newOutputPort();
		OutputPort $_29 = Ports.newOutputPort();
		InputPort $_13 = Ports.newInputPort();
		InputPort $_14 = Ports.newInputPort();
		InputPort $_17 = Ports.newInputPort();
		InputPort $_27 = Ports.newInputPort();
		InputPort $_5 = Ports.newInputPort();
		InputPort $_9 = Ports.newInputPort();

		new Protocol_Chess(
			$_1,
			$_18,
			$_28,
			$_29,
			$_13,
			$_14,
			$_17,
			$_27,
			$_5,
			$_9
		);

		Map<String, Port> inputPorts = new HashMap<>();
		inputPorts.put("$_1", (Port) $_1);
		inputPorts.put("$_18", (Port) $_18);
		inputPorts.put("$_28", (Port) $_28);
		inputPorts.put("$_29", (Port) $_29);

		Map<String, Port> outputPorts = new HashMap<>();
		outputPorts.put("$_13", (Port) $_13);
		outputPorts.put("$_14", (Port) $_14);
		outputPorts.put("$_17", (Port) $_17);
		outputPorts.put("$_27", (Port) $_27);
		outputPorts.put("$_5", (Port) $_5);
		outputPorts.put("$_9", (Port) $_9);

		new Thread(new Console(inputPorts, outputPorts)).start();
	}
}

class Protocol_Chess_Automaton69 extends Automaton {

	//
	// FIELDS
	//

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State2 state2;
	final Protocol_Chess_Automaton69_State3 state3;
	final Protocol_Chess_Automaton69_State5 state5;
	final Protocol_Chess_Automaton69_State9 state9;

	/*
	 * Current state
	 */

	final Current current = new Current();

	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton69() {
		super(10);

		/*
		 * Set states
		 */

		this.state2 = new Protocol_Chess_Automaton69_State2();
		this.state3 = new Protocol_Chess_Automaton69_State3();
		this.state5 = new Protocol_Chess_Automaton69_State5();
		this.state9 = new Protocol_Chess_Automaton69_State9();

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

		this.state2.initialize(protocol);
		this.state3.initialize(protocol);
		this.state5.initialize(protocol);
		this.state9.initialize(protocol);

		/*
		 * Reach initial state
		 */

		this.state2.reach();

		/*
		 * Return
		 */

		return;
	}
}

class Protocol_Chess_Automaton69_State2 extends State {

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
	 * Public ports
	 */

	PublicPort $_27;
	PublicPort $_5;
	PublicPort $_9;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton69_Transition19 transition19;

	/*
	 * Observable transitions per port
	 */

			

	/*
	 * Fairness indices for observable transitions
	 */

			
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton69_State2() {

		/*
		 * Set transitions
		 */

		this.transition19 = new Protocol_Chess_Automaton69_Transition19();

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
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton69.state9;
		/*
		 * Set ports 
		 */

		this.$_27 = protocol.$_27;
		this.$_5 = protocol.$_5;
		this.$_9 = protocol.$_9;

		/*
		 * Initialize transitions
		 */

		this.transition19.initialize(protocol);

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
		 * Unblock public ports
		 */

		$_27.semaphore.release();
		$_5.semaphore.release();
		$_9.semaphore.release();

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

class Protocol_Chess_Automaton69_State3 extends State {

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
	 * Public ports
	 */

	PublicPort $_1;
	PublicPort $_13;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton69_Transition20 transition20;

	/*
	 * Observable transitions per port
	 */

		

	/*
	 * Fairness indices for observable transitions
	 */

		
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton69_State3() {

		/*
		 * Set transitions
		 */

		this.transition20 = new Protocol_Chess_Automaton69_Transition20();

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
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton69.state2;
		/*
		 * Set ports 
		 */

		this.$_1 = protocol.$_1;
		this.$_13 = protocol.$_13;

		/*
		 * Initialize transitions
		 */

		this.transition20.initialize(protocol);

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
		 * Unblock public ports
		 */

		$_1.semaphore.release();
		$_13.semaphore.release();

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

class Protocol_Chess_Automaton69_State5 extends State {

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
	 * Public ports
	 */

	PublicPort $_17;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton69_Transition21 transition21;

	/*
	 * Observable transitions per port
	 */

	

	/*
	 * Fairness indices for observable transitions
	 */

	
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton69_State5() {

		/*
		 * Set transitions
		 */

		this.transition21 = new Protocol_Chess_Automaton69_Transition21();

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
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton69.state3;
		/*
		 * Set ports 
		 */

		this.$_17 = protocol.$_17;

		/*
		 * Initialize transitions
		 */

		this.transition21.initialize(protocol);

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
		 * Unblock public ports
		 */

		$_17.semaphore.release();

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

class Protocol_Chess_Automaton69_State9 extends State {

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
	 * Public ports
	 */

	PublicPort $_14;
	PublicPort $_18;
	PublicPort $_28;
	PublicPort $_29;

	/*
	 * Transitions
	 */

	final Protocol_Chess_Automaton69_Transition22 transition22;

	/*
	 * Observable transitions per port
	 */

				

	/*
	 * Fairness indices for observable transitions
	 */

				
	//
	// CONSTRUCTORS
	//

	public Protocol_Chess_Automaton69_State9() {

		/*
		 * Set transitions
		 */

		this.transition22 = new Protocol_Chess_Automaton69_Transition22();

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
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set successor
		 */

		this.successor = protocol.automaton69.state5;
		/*
		 * Set ports 
		 */

		this.$_14 = protocol.$_14;
		this.$_18 = protocol.$_18;
		this.$_28 = protocol.$_28;
		this.$_29 = protocol.$_29;

		/*
		 * Initialize transitions
		 */

		this.transition22.initialize(protocol);

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
		 * Unblock public ports
		 */

		$_14.semaphore.release();
		$_18.semaphore.release();
		$_28.semaphore.release();
		$_29.semaphore.release();

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

class Protocol_Chess_Automaton69_Transition19 extends Transition {

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

	PublicPort $_27;
	PublicPort $_5;
	PublicPort $_9;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton69_State9 target;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

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

			context.remove(0, 0b00000000000000000000001110000000);

			/*
			 * Unblock ports
			 */

			$_27.status = IO.COMPLETED;
			$_27.semaphore.release();
			$_5.status = IO.COMPLETED;
			$_5.semaphore.release();
			$_9.status = IO.COMPLETED;
			$_9.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();
		}

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton69.context;

		/*
		 * Set ports 
		 */

		this.$_27 = protocol.$_27;
		this.$_5 = protocol.$_5;
		this.$_9 = protocol.$_9;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton69.state9;

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
			&& context.contains(0, 0b00000000000000000000001110000000)
;
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

		final private CspPortVariable $_27;
		final private CspPortVariable $_5;
		final private CspPortVariable $_9;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell2$pre;
		final private CspPreVariable memoryCell3$pre;
		final private CspPreVariable memoryCell4$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell1$post;
		final private CspPostVariable memoryCell2$post;
		final private CspPostVariable memoryCell3$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this.$_27 = protocol.variablePool.newOrGetPortVariable(protocol.$_27);
			this.$_5 = protocol.variablePool.newOrGetPortVariable(protocol.$_5);
			this.$_9 = protocol.variablePool.newOrGetPortVariable(protocol.$_9);
			this.memoryCell2$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell2);
			this.memoryCell3$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell3);
			this.memoryCell4$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell4);
			this.memoryCell1$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell1);
			this.memoryCell2$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell2);
			this.memoryCell3$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell3);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			memoryCell4$pre.importValue();
			memoryCell3$pre.importValue();
			memoryCell2$pre.importValue();
			memoryCell2$post.setValue(memoryCell2$pre.getValue());
			memoryCell3$post.setValue(memoryCell3$pre.getValue());
			memoryCell1$post.setValue(memoryCell4$pre.getValue());
			$_9.setValue(memoryCell4$pre.getValue());
			$_27.setValue(memoryCell4$pre.getValue());
			$_5.setValue(memoryCell4$pre.getValue());
			memoryCell1$post.exportValue();
			$_9.exportValue();
			$_27.exportValue();
			$_5.exportValue();
			memoryCell2$post.exportValue();
			memoryCell3$post.exportValue();
			return true;
		}
	}
}

class Protocol_Chess_Automaton69_Transition20 extends Transition {

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

	PublicPort $_1;
	PublicPort $_13;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton69_State2 target;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

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

			context.remove(0, 0b00000000000000000000000000010001);

			/*
			 * Unblock ports
			 */

			$_1.status = IO.COMPLETED;
			$_1.semaphore.release();
			$_13.status = IO.COMPLETED;
			$_13.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();
		}

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton69.context;

		/*
		 * Set ports 
		 */

		this.$_1 = protocol.$_1;
		this.$_13 = protocol.$_13;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton69.state2;

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
			&& context.contains(0, 0b00000000000000000000000000010001)
;
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

		final private CspPortVariable $_1;
		final private CspPortVariable $_13;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell1$pre;
		final private CspPreVariable memoryCell2$pre;
		final private CspPreVariable memoryCell3$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell1$post;
		final private CspPostVariable memoryCell2$post;
		final private CspPostVariable memoryCell4$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this.$_1 = protocol.variablePool.newOrGetPortVariable(protocol.$_1);
			this.$_13 = protocol.variablePool.newOrGetPortVariable(protocol.$_13);
			this.memoryCell1$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell1);
			this.memoryCell2$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell2);
			this.memoryCell3$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell3);
			this.memoryCell1$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell1);
			this.memoryCell2$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell2);
			this.memoryCell4$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell4);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			$_1.importValue();
			memoryCell3$pre.importValue();
			memoryCell2$pre.importValue();
			memoryCell1$pre.importValue();
			memoryCell1$post.setValue(memoryCell1$pre.getValue());
			memoryCell2$post.setValue(memoryCell2$pre.getValue());
			$_13.setValue(runtime.chess.Functions.majority(runtime.chess.Functions.parse($_1.getValue())));
			if (!nl.cwi.pr.runtime.Relations.True($_13.getValue(), memoryCell3$pre.getValue()))
				return false;
			memoryCell4$post.setValue(runtime.chess.Functions.concatenate(memoryCell3$pre.getValue(), $_13.getValue()));
			if (!runtime.chess.Relations.Move($_13.getValue()))
				return false;
			memoryCell1$post.exportValue();
			memoryCell2$post.exportValue();
			$_13.exportValue();
			memoryCell4$post.exportValue();
			return true;
		}
	}
}

class Protocol_Chess_Automaton69_Transition21 extends Transition {

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

	PublicPort $_17;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton69_State3 target;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

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

			context.remove(0, 0b00000000000000000000000001000000);

			/*
			 * Unblock ports
			 */

			$_17.status = IO.COMPLETED;
			$_17.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();
		}

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton69.context;

		/*
		 * Set ports 
		 */

		this.$_17 = protocol.$_17;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton69.state3;

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
			&& context.contains(0, 0b00000000000000000000000001000000)
;
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

		final private CspPortVariable $_17;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell1$pre;
		final private CspPreVariable memoryCell2$pre;
		final private CspPreVariable memoryCell4$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell1$post;
		final private CspPostVariable memoryCell3$post;
		final private CspPostVariable memoryCell4$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this.$_17 = protocol.variablePool.newOrGetPortVariable(protocol.$_17);
			this.memoryCell1$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell1);
			this.memoryCell2$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell2);
			this.memoryCell4$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell4);
			this.memoryCell1$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell1);
			this.memoryCell3$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell3);
			this.memoryCell4$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell4);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			memoryCell4$pre.importValue();
			memoryCell2$pre.importValue();
			memoryCell1$pre.importValue();
			memoryCell1$post.setValue(memoryCell1$pre.getValue());
			memoryCell4$post.setValue(memoryCell4$pre.getValue());
			memoryCell3$post.setValue(memoryCell2$pre.getValue());
			$_17.setValue(memoryCell2$pre.getValue());
			memoryCell1$post.exportValue();
			memoryCell3$post.exportValue();
			$_17.exportValue();
			memoryCell4$post.exportValue();
			return true;
		}
	}
}

class Protocol_Chess_Automaton69_Transition22 extends Transition {

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

	PublicPort $_14;
	PublicPort $_18;
	PublicPort $_28;
	PublicPort $_29;

	/*
	 * Data constraint
	 */

	DataConstraint dataConstraint;

	/*
	 * Target
	 */

	Protocol_Chess_Automaton69_State5 target;

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean fire() {

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

			context.remove(0, 0b00000000000000000000000000101110);

			/*
			 * Unblock ports
			 */

			$_14.status = IO.COMPLETED;
			$_14.semaphore.release();
			$_18.status = IO.COMPLETED;
			$_18.semaphore.release();
			$_28.status = IO.COMPLETED;
			$_28.semaphore.release();
			$_29.status = IO.COMPLETED;
			$_29.semaphore.release();

			/*
			 * Update current state
			 */

			target.reach();
		}

		/*
		 * Return
		 */

		return canFire;
	}

	public void initialize(Protocol_Chess protocol) {

		/*
		 * Set context
		 */

		this.context = protocol.automaton69.context;

		/*
		 * Set ports 
		 */

		this.$_14 = protocol.$_14;
		this.$_18 = protocol.$_18;
		this.$_28 = protocol.$_28;
		this.$_29 = protocol.$_29;

		/*
		 * Set data constraint and target
		 */

		this.dataConstraint = new DataConstraint(protocol);

		/*
		 * Set target
		 */

		this.target = protocol.automaton69.state5;

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
			&& context.contains(0, 0b00000000000000000000000000101110)
;
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

		final private CspPortVariable $_14;
		final private CspPortVariable $_18;
		final private CspPortVariable $_28;
		final private CspPortVariable $_29;

		/*
		 * Pre variables
		 */

		final private CspPreVariable memoryCell1$pre;
		final private CspPreVariable memoryCell3$pre;
		final private CspPreVariable memoryCell4$pre;

		/*
		 * Post variables
		 */

		final private CspPostVariable memoryCell2$post;
		final private CspPostVariable memoryCell3$post;
		final private CspPostVariable memoryCell4$post;

		//
		// CONSTRUCTORS
		//

		public DataConstraint(Protocol_Chess protocol) {

			/*
			 * Set variables
			 */

			this.$_14 = protocol.variablePool.newOrGetPortVariable(protocol.$_14);
			this.$_18 = protocol.variablePool.newOrGetPortVariable(protocol.$_18);
			this.$_28 = protocol.variablePool.newOrGetPortVariable(protocol.$_28);
			this.$_29 = protocol.variablePool.newOrGetPortVariable(protocol.$_29);
			this.memoryCell1$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell1);
			this.memoryCell3$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell3);
			this.memoryCell4$pre = protocol.variablePool.newOrGetPreVariable(protocol.memoryCell4);
			this.memoryCell2$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell2);
			this.memoryCell3$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell3);
			this.memoryCell4$post = protocol.variablePool.newOrGetPostVariable(protocol.memoryCell4);

			/*
			 * Return
			 */

			return;
		}

		public boolean solve() {
			memoryCell4$pre.importValue();
			memoryCell3$pre.importValue();
			$_28.importValue();
			$_18.importValue();
			$_29.importValue();
			memoryCell1$pre.importValue();
			memoryCell3$post.setValue(memoryCell3$pre.getValue());
			memoryCell4$post.setValue(memoryCell4$pre.getValue());
			$_14.setValue(runtime.chess.Functions.majority(runtime.chess.Functions.concatenate(runtime.chess.Functions.parse($_28.getValue()), runtime.chess.Functions.concatenate(runtime.chess.Functions.parse($_18.getValue()), runtime.chess.Functions.parse($_29.getValue())))));
			if (!nl.cwi.pr.runtime.Relations.True($_14.getValue(), memoryCell1$pre.getValue()))
				return false;
			memoryCell2$post.setValue(runtime.chess.Functions.concatenate(memoryCell1$pre.getValue(), $_14.getValue()));
			if (!runtime.chess.Relations.Move($_14.getValue()))
				return false;
			$_14.exportValue();
			memoryCell2$post.exportValue();
			memoryCell3$post.exportValue();
			memoryCell4$post.exportValue();
			return true;
		}
	}
}

class HandlerFor$_1 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_1;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State3 state3;



	//
	// CONSTRUCTORS
	//

	public HandlerFor$_1(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_1 = protocol.$_1;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state3 = protocol.automaton69.state3;



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
		if ($_1.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_1)
		 */

		/* 
		 * Fire a transition from state #3 
		 */

		if (current.state == state3 && state3.transition20.fire())
			return true;

		/*
		 * Ignore state #5 (which has no transitions involving port $_1)
		 */

		/*
		 * Ignore state #9 (which has no transitions involving port $_1)
		 */


		$_1.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000001);
		IO status = $_1.status;
		$_1.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000001);
	}

}

class HandlerFor$_13 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_13;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State3 state3;



	//
	// CONSTRUCTORS
	//

	public HandlerFor$_13(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_13 = protocol.$_13;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state3 = protocol.automaton69.state3;



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
		if ($_13.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_13)
		 */

		/* 
		 * Fire a transition from state #3 
		 */

		if (current.state == state3 && state3.transition20.fire())
			return true;

		/*
		 * Ignore state #5 (which has no transitions involving port $_13)
		 */

		/*
		 * Ignore state #9 (which has no transitions involving port $_13)
		 */


		$_13.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000010000);
		IO status = $_13.status;
		$_13.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000010000);
	}

}

class HandlerFor$_14 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_14;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State9 state9;

	//
	// CONSTRUCTORS
	//

	public HandlerFor$_14(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_14 = protocol.$_14;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state9 = protocol.automaton69.state9;

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
		if ($_14.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_14)
		 */

		/*
		 * Ignore state #3 (which has no transitions involving port $_14)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_14)
		 */

		/* 
		 * Fire a transition from state #9 
		 */

		if (current.state == state9 && state9.transition22.fire())
			return true;


		$_14.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000100000);
		IO status = $_14.status;
		$_14.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000100000);
	}

}

class HandlerFor$_17 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_17;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State5 state5;


	//
	// CONSTRUCTORS
	//

	public HandlerFor$_17(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_17 = protocol.$_17;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state5 = protocol.automaton69.state5;


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
		if ($_17.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_17)
		 */

		/*
		 * Ignore state #3 (which has no transitions involving port $_17)
		 */

		/* 
		 * Fire a transition from state #5 
		 */

		if (current.state == state5 && state5.transition21.fire())
			return true;

		/*
		 * Ignore state #9 (which has no transitions involving port $_17)
		 */


		$_17.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000001000000);
		IO status = $_17.status;
		$_17.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000001000000);
	}

}

class HandlerFor$_18 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_18;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State9 state9;

	//
	// CONSTRUCTORS
	//

	public HandlerFor$_18(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_18 = protocol.$_18;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state9 = protocol.automaton69.state9;

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
		if ($_18.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_18)
		 */

		/*
		 * Ignore state #3 (which has no transitions involving port $_18)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_18)
		 */

		/* 
		 * Fire a transition from state #9 
		 */

		if (current.state == state9 && state9.transition22.fire())
			return true;


		$_18.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000010);
		IO status = $_18.status;
		$_18.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000010);
	}

}

class HandlerFor$_27 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_27;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State2 state2;




	//
	// CONSTRUCTORS
	//

	public HandlerFor$_27(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_27 = protocol.$_27;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state2 = protocol.automaton69.state2;




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
		if ($_27.status == IO.COMPLETED)
			return true;

		/* 
		 * Fire a transition from state #2 
		 */

		if (current.state == state2 && state2.transition19.fire())
			return true;

		/*
		 * Ignore state #3 (which has no transitions involving port $_27)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_27)
		 */

		/*
		 * Ignore state #9 (which has no transitions involving port $_27)
		 */


		$_27.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000010000000);
		IO status = $_27.status;
		$_27.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000010000000);
	}

}

class HandlerFor$_28 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_28;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State9 state9;

	//
	// CONSTRUCTORS
	//

	public HandlerFor$_28(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_28 = protocol.$_28;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state9 = protocol.automaton69.state9;

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
		if ($_28.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_28)
		 */

		/*
		 * Ignore state #3 (which has no transitions involving port $_28)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_28)
		 */

		/* 
		 * Fire a transition from state #9 
		 */

		if (current.state == state9 && state9.transition22.fire())
			return true;


		$_28.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000000100);
		IO status = $_28.status;
		$_28.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000000100);
	}

}

class HandlerFor$_29 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_29;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State9 state9;

	//
	// CONSTRUCTORS
	//

	public HandlerFor$_29(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_29 = protocol.$_29;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state9 = protocol.automaton69.state9;

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
		if ($_29.status == IO.COMPLETED)
			return true;

		/*
		 * Ignore state #2 (which has no transitions involving port $_29)
		 */

		/*
		 * Ignore state #3 (which has no transitions involving port $_29)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_29)
		 */

		/* 
		 * Fire a transition from state #9 
		 */

		if (current.state == state9 && state9.transition22.fire())
			return true;


		$_29.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000000001000);
		IO status = $_29.status;
		$_29.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000000001000);
	}

}

class HandlerFor$_5 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_5;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State2 state2;




	//
	// CONSTRUCTORS
	//

	public HandlerFor$_5(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_5 = protocol.$_5;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state2 = protocol.automaton69.state2;




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
		if ($_5.status == IO.COMPLETED)
			return true;

		/* 
		 * Fire a transition from state #2 
		 */

		if (current.state == state2 && state2.transition19.fire())
			return true;

		/*
		 * Ignore state #3 (which has no transitions involving port $_5)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_5)
		 */

		/*
		 * Ignore state #9 (which has no transitions involving port $_5)
		 */


		$_5.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000000100000000);
		IO status = $_5.status;
		$_5.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000000100000000);
	}

}

class HandlerFor$_9 extends Handler {

	//
	// FIELDS
	//

	/*
	 * Context and port
	 */

	final Context context;
	final PublicPort $_9;

	/*
	 * Current state
	 */

	final Current current;

	/*
	 * States
	 */

	final Protocol_Chess_Automaton69_State2 state2;




	//
	// CONSTRUCTORS
	//

	public HandlerFor$_9(Protocol_Chess protocol) {
		super(protocol.automaton69.semaphore);

		/*
		 * Set context and port
		 */

		this.context = protocol.automaton69.context;
		this.$_9 = protocol.$_9;

		/*
		 * Set current state
		 */

		this.current = protocol.automaton69.current;

		/*
		 * Set states
		 */

		this.state2 = protocol.automaton69.state2;




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
		if ($_9.status == IO.COMPLETED)
			return true;

		/* 
		 * Fire a transition from state #2 
		 */

		if (current.state == state2 && state2.transition19.fire())
			return true;

		/*
		 * Ignore state #3 (which has no transitions involving port $_9)
		 */

		/*
		 * Ignore state #5 (which has no transitions involving port $_9)
		 */

		/*
		 * Ignore state #9 (which has no transitions involving port $_9)
		 */


		$_9.semaphore.drainPermits();
		return false;
	}

	@Override
	public boolean cancel() {
		context.remove(0, 0b00000000000000000000001000000000);
		IO status = $_9.status;
		$_9.status = null;
		return status == IO.COMPLETED;
	}

	@Override
	public void flag() {
		context.add(0, 0b00000000000000000000001000000000);
	}

}
