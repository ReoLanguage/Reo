//package unfixedSwitch;

import java.util.LinkedList;

public class BSTNode implements Cloneable {
	int priority;
	String inport;
	String type;
	String ipdst;
	String ipsrc;
	String action;
	String content;

	BSTNode left;
	BSTNode right;
	BSTNode parent;

	/*
	 * public BSTNode(int priority, String inport, String ipdst, String ipsrc,
	 * String action, BSTNode left, BSTNode right, BSTNode parent) {
	 * 
	 * this.priority = priority; this.inport = inport; this.ipdst = ipdst;
	 * this.ipsrc = ipsrc;
	 * 
	 * this.action = action; this.left = left; this.right = right; this.parent =
	 * parent; }
	 */

	public BSTNode(String inport, String type, int priority, String ipdst,
			String ipsrc, String action) {
		this.inport = inport;
		this.priority = priority;
		this.type = type;
		this.ipdst = ipdst;
		this.ipsrc = ipsrc;
		this.action = action;
	}

	public BSTNode(String inport, String type, String ipdst, String ipsrc,
			String content) {
		this.inport = inport;
		this.type = type;
		this.ipdst = ipdst;
		this.ipsrc = ipsrc;
		this.content = content;
	}

	/*
	 * public BSTNode(String inport, String type, String ipdst, String ipsrc) {
	 * 
	 * this.inport = inport; this.type = type; this.ipdst = ipdst; this.ipsrc =
	 * ipsrc;
	 * 
	 * }
	 */

	public BSTNode(String inport, int priority, String type, String ipdst,
			String ipsrc, String action, BSTNode left, BSTNode right) {

		this.priority = priority;
		this.inport = inport;
		this.type = type;
		this.ipdst = ipdst;
		this.ipsrc = ipsrc;

		this.action = action;
		this.left = left;
		this.right = right;
	}

	// Encodes a tree to a single string.
	public static String serialize(BSTNode root) {
		if (root == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		LinkedList<BSTNode> queue = new LinkedList<BSTNode>();

		queue.add(root);
		while (!queue.isEmpty()) {
			BSTNode t = queue.poll();
			if (t != null) {
				sb.append((t.inport) + "/" + (t.type) + "/"
						+ String.valueOf(t.priority) + "/" + (t.ipdst) + "/"
						+ (t.ipsrc) + "/" + (t.action) + "|");
				queue.add(t.left);
				queue.add(t.right);
			} else {
				sb.append("#|");
			}
		}

		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb.toString());
		return sb.toString();
	}

	public static String serializeMSGbefore(BSTNode data) {
		if (data == null) {
			return "";
		}
		String str = (data.inport) + "/" + (data.type) + "/" + (data.ipdst)
				+ "/" + (data.ipsrc);

		return str;

	}

	public static String serializeMSGafter(BSTNode data) {
		if (data == null) {
			return "";
		}
		String str = (data.inport) + "/" + (data.type) + "/" + (data.ipdst)
				+ "/" + (data.ipsrc) + "/" + (data.content) + "/"
				+ (data.action);

		return str;

	}

	public static String serializeRULE(BSTNode data) {
		if (data == null) {
			return "";
		}
		String str = (data.inport) + "/" + (data.type) + "/"
				+ String.valueOf(data.priority) + "/" + (data.ipdst) + "/"
				+ (data.ipsrc) + "/" + (data.action);

		return str;

	}

	// Decodes your encoded data to tree.
	public static BSTNode deserialize(String data) {
		if (data == null || data.length() == 0)
			return null;
		// if there comes tables<fifth> more than one rule:
		if (data.contains("|")) {
			String[] node = data.split("\\|");
			String[] arr = node[0].split("/");
			BSTNode root = new BSTNode(arr[0], arr[1],
					Integer.parseInt(arr[2]), arr[3], arr[4], arr[5]);

			LinkedList<BSTNode> queue = new LinkedList<BSTNode>();
			queue.add(root);

			int i = 1;
			while (!queue.isEmpty()) {
				BSTNode t = queue.poll();

				if (t == null)
					continue;

				if (!node[i].equals("#")) {
					String[] leftdata = node[i].split("/");
					t.left = new BSTNode(leftdata[0], leftdata[1],
							Integer.parseInt(leftdata[2]), leftdata[3],
							leftdata[4], leftdata[5]);
					queue.offer(t.left);

				} else {
					t.left = null;
					queue.offer(null);
				}
				i++;

				if (!node[i].equals("#")) {
					String[] rightdata = node[i].split("/");
					t.right = new BSTNode(rightdata[0], rightdata[1],
							Integer.parseInt(rightdata[2]), rightdata[3],
							rightdata[4], rightdata[5]);
					queue.offer(t.right);

				} else {
					t.right = null;
					queue.offer(null);
				}
				i++;
			}

			return root;
		} else {
			String[] msg = data.split("/");
			// if only one table(the fifth one) need to be deserialized
			if (msg.length == 6) {
				BSTNode root = new BSTNode(msg[0], msg[1],
						Integer.parseInt(msg[2]), msg[3], msg[4], msg[5]);
				return root;
			} else {
				// if only one data(the second one) need to be deserialized
				BSTNode root = new BSTNode(msg[0], msg[1], msg[2], msg[3],
						msg[4]);
				return root;
			}
		}

	}

	@Override
	public BSTNode clone() {
		BSTNode ob = null;
		try {
			ob = (BSTNode) super.clone();
			ob.priority = this.priority;
			ob.inport = this.inport;
			ob.type = this.type;
			ob.ipdst = this.ipdst;
			ob.ipsrc = this.ipsrc;
			ob.action = this.action;

			if (this.left != null) {
				ob.left = (BSTNode) this.left;
			}
			if (this.right != null) {
				ob.right = (BSTNode) this.right;
			}
			if (this.parent != null) {
				ob.parent = (BSTNode) this.parent;
			}
		}

		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
