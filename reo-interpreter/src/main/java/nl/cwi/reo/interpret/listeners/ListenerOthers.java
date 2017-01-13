package nl.cwi.reo.interpret.listeners;

import nl.cwi.reo.interpret.TreoParser.Atom_constraintautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_portautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_seepageautomataContext;
import nl.cwi.reo.interpret.TreoParser.Atom_sourcecodeContext;
import nl.cwi.reo.interpret.TreoParser.Atom_workautomataContext;
import nl.cwi.reo.interpret.TreoParser.CamContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_addsubContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_andContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_existentialContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_exponentContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_ineqContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_multdivremContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_neqContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_orContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_termContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dc_universalContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_bracketsContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_dataContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_functionContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_nextContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_notContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_unaryMinContext;
import nl.cwi.reo.interpret.TreoParser.Cam_dt_variableContext;
import nl.cwi.reo.interpret.TreoParser.Cam_trContext;
import nl.cwi.reo.interpret.TreoParser.GplContext;
import nl.cwi.reo.interpret.TreoParser.PaContext;
import nl.cwi.reo.interpret.TreoParser.Pa_trContext;
import nl.cwi.reo.interpret.TreoParser.SaContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_andContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_boolContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_orContext;
import nl.cwi.reo.interpret.TreoParser.Sa_pbe_variableContext;
import nl.cwi.reo.interpret.TreoParser.Sa_seepagefunctionContext;
import nl.cwi.reo.interpret.TreoParser.Sa_transitionContext;
import nl.cwi.reo.semantics.Semantics;

/**
 * Listens to events triggered by a {@link org.antlr.v4.runtime.tree.ParseTreeWalker}.
 * Returns a {@link nl.cwi.reo.interpret.p}.
 */
public class ListenerOthers<T extends Semantics<T>> extends Listener<T> {		

	/**
	 * Atoms
	 */

	@Override
	public void enterAtom_sourcecode(Atom_sourcecodeContext ctx) { }

	@Override
	public void exitAtom_sourcecode(Atom_sourcecodeContext ctx) {
	}
	
	@Override
	public void enterAtom_workautomata(Atom_workautomataContext ctx) { }

	@Override
	public void exitAtom_workautomata(Atom_workautomataContext ctx) {
	}

	@Override
	public void enterAtom_constraintautomata(Atom_constraintautomataContext ctx) { }

	@Override
	public void exitAtom_constraintautomata(Atom_constraintautomataContext ctx) {
	}

	@Override
	public void enterAtom_seepageautomata(Atom_seepageautomataContext ctx) { }

	@Override
	public void exitAtom_seepageautomata(Atom_seepageautomataContext ctx) {
	}

	@Override
	public void enterAtom_portautomata(Atom_portautomataContext ctx) { }

	@Override
	public void exitAtom_portautomata(Atom_portautomataContext ctx) {
	}
	
	/**
	 * Semantics - GPL Source code
	 */

	@Override
	public void enterGpl(GplContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGpl(GplContext ctx) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Semantics - Constraint automata with memory
	 */
	
	@Override
	public void enterCam_dc_exponent(Cam_dc_exponentContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_exponent(Cam_dc_exponentContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_next(Cam_dt_nextContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_next(Cam_dt_nextContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_tr(Cam_trContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_tr(Cam_trContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_multdivrem(Cam_dc_multdivremContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_multdivrem(Cam_dc_multdivremContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_ineq(Cam_dc_ineqContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_ineq(Cam_dc_ineqContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_neq(Cam_dc_neqContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_neq(Cam_dc_neqContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterCam_dt_brackets(Cam_dt_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_brackets(Cam_dt_bracketsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_existential(Cam_dc_existentialContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_existential(Cam_dc_existentialContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_or(Cam_dc_orContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_or(Cam_dc_orContext ctx) {
		// TODO Auto-generated method stub
	}

	@Override
	public void enterCam_dc_universal(Cam_dc_universalContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_universal(Cam_dc_universalContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_data(Cam_dt_dataContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_data(Cam_dt_dataContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_not(Cam_dt_notContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_not(Cam_dt_notContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam(CamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam(CamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_function(Cam_dt_functionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_function(Cam_dt_functionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_addsub(Cam_dc_addsubContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_addsub(Cam_dc_addsubContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterCam_dc_and(Cam_dc_andContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_and(Cam_dc_andContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dc_term(Cam_dc_termContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dc_term(Cam_dc_termContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_variable(Cam_dt_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitCam_dt_variable(Cam_dt_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCam_dt_unaryMin(Cam_dt_unaryMinContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCam_dt_unaryMin(Cam_dt_unaryMinContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Semantics - Seepage Automata
	 */

	@Override
	public void enterSa(SaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa(SaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterSa_pbe_bool(Sa_pbe_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_pbe_bool(Sa_pbe_boolContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSa_transition(Sa_transitionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_transition(Sa_transitionContext ctx) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void enterSa_pbe_or(Sa_pbe_orContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_pbe_or(Sa_pbe_orContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterSa_pbe_variable(Sa_pbe_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_pbe_variable(Sa_pbe_variableContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterSa_seepagefunction(Sa_seepagefunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSa_seepagefunction(Sa_seepagefunctionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSa_pbe_and(Sa_pbe_andContext ctx) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exitSa_pbe_and(Sa_pbe_andContext ctx) {
		// TODO Auto-generated method stub
		
	}	

	/**
	 * Semantics - Port Automata
	 */

	@Override
	public void enterPa(PaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPa(PaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPa_tr(Pa_trContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitPa_tr(Pa_trContext ctx) {
		// TODO Auto-generated method stub
		
	}
}