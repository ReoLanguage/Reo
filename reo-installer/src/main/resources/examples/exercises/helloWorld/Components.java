import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

	public class Components {
		public static void cons(Input<String> p) {
			for (int i = 0; i < 12; i++)
				System.out.print(p.get());
			System.out.print("\n");
		}

		public static void prod_H(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("H");
		}
		public static void prod_E(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("E");
		}
		public static void prod_L(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("L");
		}
		public static void prod_O(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("O");
		}
		public static void prod_s(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put(" ");
		}
		public static void prod_W(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("W");
		}
		public static void prod_R(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("R");
		}
		public static void prod_D(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("D");
		}
		public static void prod_e(Output<String> p) {
			for (int i = 0; i < 15; i++)
				p.put("!");
		}
		

}
