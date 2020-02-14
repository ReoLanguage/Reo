//package unfixedSwitch;

public class TransformerFunction {

	/*
	 * Add port name in message
	 */
	public static String AddPort(int portname, String data) {
		// String portname1 = (String)portname;

		if (data == null)
			throw new NullPointerException();

		else {

			String string = (String.valueOf(portname)) + "/" + ((String) data);
			return string;
		}
	}

	public static String AddPortA(String data) {
		// String portname1 = (String)portname;

		if (data == null)
			throw new NullPointerException();

		else {

			String string = "a" + "/" + ((String) data);
			return string;
		}
	}

	public static String Update(String str) {

		String[] arr = str.split("&&");
		String data = arr[0];
		String tree = arr[1];

		if (data == null || tree == null)
			throw new NullPointerException();
		else {

			BSTNode tree2 = BSTNode.deserialize(tree);
			BSTNode data2 = BSTNode.deserialize(data);

			BST.insert(data2, tree2);
			String tree3 = BSTNode.serialize(tree2);// change the (object)rule
													// to string

			return tree3;
		}

	}

	public static String Matching(String str) {

		String[] arr = str.split("&&");
		String data = arr[0];
		String tree = arr[1];

		if (data == null || tree == null)
			throw new NullPointerException();

		else {
			/*
			 * Envelope envelope = new Envelope(); String[] msarr =
			 * data.toString().split("\\/");
			 * 
			 * envelope.setInport(msarr[0]); envelope.setIpdst(msarr[1]);
			 * envelope.setIpsrc(msarr[2]);
			 */

			BSTNode tree2 = BSTNode.deserialize(tree);
			BSTNode data2 = BSTNode.deserialize(data);

			BSTNode tree3 = BST.find(data2, tree2);// find the rule with action

			String tail = tree3.action;

			String data3 = BSTNode.serializeMSGafter(data2) + "/" + tail;
			return data3;// the (String) data + (String) rule
		}
	}

	public static String PutOut(String data) {
		String[] arr = data.split("/");
		if (arr[5].contains("CHA")) {
			String changedpkt[] = arr[5].split("CHA[");
			String substr = changedpkt[1].substring(0,
					changedpkt[1].length() - 1);
			String separatedpkt[] = substr.split(";");
			for (int i = 0; i < separatedpkt.length; i++) {
				if (separatedpkt[i].contains("ipsrc")) {
					arr[2] = separatedpkt[i].split(":")[1];
				}
				if (separatedpkt[i].contains("ipdst")) {
					arr[1] = separatedpkt[i].split(":")[1];
				}
			}

		}
		data = arr[1] + "/" + arr[2] + "/" + arr[3] + "/" + arr[4];
		return data;
	}

}
