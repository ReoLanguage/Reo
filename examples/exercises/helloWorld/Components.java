import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

	public class Components {
		public static void cons(InputPort p) {
			for (int i = 0; i < 15; i++)
			System.out.print(p.getUninterruptibly());
		}

		public static void prod_H(OutputPort p) {
			p.putUninterruptibly("H");
		}
		public static void prod_E(OutputPort p) {
			p.putUninterruptibly("E");
		}
		public static void prod_L(OutputPort p) {
			p.putUninterruptibly("L");
		}
		public static void prod_O(OutputPort p) {
			p.putUninterruptibly("O");
		}
		public static void prod_s(OutputPort p) {
			p.putUninterruptibly(" ");
		}
		public static void prod_W(OutputPort p) {
			p.putUninterruptibly("W");
		}
		public static void prod_R(OutputPort p) {
			p.putUninterruptibly("R");
		}
		public static void prod_D(OutputPort p) {
			p.putUninterruptibly("D");
		}
		public static void prod_e(OutputPort p) {
			p.putUninterruptibly("!");
		}
		

}
