//package unfixedSwitch;

import java.util.LinkedList;

public class BST {

	// public static BSTNode root;

	/*
	 * public BST() { BST.root.priority = 100; BST.root.inport = "*";
	 * BST.root.type = "RULE"; BST.root.ipdst = "*"; BST.root.ipsrc = "*";
	 * BST.root.action = "f[q0]"; BST.root.left = null; BST.root.right = null; }
	 */

	public static BSTNode find(BSTNode data, BSTNode tree) {

		// BSTNode current = tree;
		// String[] msarr = data.toString().split("\\/");
		if (tree != null) {
			// current = current.left.clone();
			// find(data,current.left

			find(data, tree.left);

			if (match(tree.inport, data.inport)
					&& match(tree.ipdst, data.ipdst)
					&& match(tree.ipsrc, data.ipsrc))
				return tree;

			find(data, tree.right);
		}
		return null;
	}

	/**
	 * 通配符算法。 可以匹配"*"和"?" 如a*b?d可以匹配aAAAbcd
	 * 
	 * @param pattern
	 *            匹配表达式
	 * @param str
	 *            匹配的字符串
	 * @return
	 */
	public static boolean match(String pattern, String str) {
		if (pattern == null || str == null)
			return false;

		boolean result = false;
		char c; // 当前要匹配的字符串
		boolean beforeStar = false; // 是否遇到通配符*
		int back_i = 0;// 回溯,当遇到通配符时,匹配不成功则回溯
		int back_j = 0;
		int i, j;
		for (i = 0, j = 0; i < str.length();) {
			if (pattern.length() <= j) {
				if (back_i != 0) {// 有通配符,但是匹配未成功,回溯
					beforeStar = true;
					i = back_i;
					j = back_j;
					back_i = 0;
					back_j = 0;
					continue;
				}
				break;
			}

			if ((c = pattern.charAt(j)) == '*') {
				if (j == pattern.length() - 1) {// 通配符已经在末尾,返回true
					result = true;
					break;
				}
				beforeStar = true;
				j++;
				continue;
			}

			if (beforeStar) {
				if (str.charAt(i) == c) {
					beforeStar = false;
					back_i = i + 1;
					back_j = j;
					j++;
				}
			} else {
				if (c != '?' && c != str.charAt(i)) {
					result = false;
					if (back_i != 0) {// 有通配符,但是匹配未成功,回溯
						beforeStar = true;
						i = back_i;
						j = back_j;
						back_i = 0;
						back_j = 0;
						continue;
					}
					break;
				}
				j++;
			}
			i++;
		}

		if (i == str.length() && j == pattern.length())// 全部遍历完毕
			result = true;
		return result;
	}

	public static BSTNode delete(BSTNode deletenode, BSTNode tree) {

		BSTNode parent = tree;
		BSTNode current = tree;
		boolean isLeftChild = false;

		do {
			find(current.left, tree);

			if (match(current.inport, deletenode.inport)
					&& match(current.ipdst, deletenode.ipdst)
					&& match(current.ipsrc, deletenode.ipsrc)) {

				// if i am here that means we have found the node
				// Case 1: if node to be deleted has no children
				if (current.left == null && current.right == null) {
					if (current == tree) {
						tree = null;
					}
					if (isLeftChild == true) {
						parent.left = null;
					} else {
						parent.right = null;
					}
				}
				// Case 2 : if node to be deleted has only one child
				else if (current.right == null) {
					if (current == tree) {
						tree = current.left;
					} else if (isLeftChild) {
						parent.left = current.left;
					} else {
						parent.right = current.left;
					}
				} else if (current.left == null) {
					if (current == tree) {
						tree = current.right;
					} else if (isLeftChild) {
						parent.left = current.right;
					} else {
						parent.right = current.right;
					}
				} else if (current.left != null && current.right != null) {

					// now we have found the minimum element in the right sub
					// tree
					BSTNode successor = getSuccessor(current);
					if (current == tree) {
						tree = successor;
					} else if (isLeftChild) {
						parent.left = successor;
					} else {
						parent.right = successor;
					}
					successor.left = current.left;
				}

			}

			find(current.right, tree);
		} while (!(match(current.inport, deletenode.inport)
				&& match(current.ipdst, deletenode.ipdst) && match(
					current.ipsrc, deletenode.ipsrc)));
		return tree;

	}

	public static BSTNode getSuccessor(BSTNode deleleNode) {
		BSTNode successsor = null;
		BSTNode successsorParent = null;
		BSTNode current = deleleNode.right;
		while (current != null) {
			successsorParent = successsor;
			successsor = current;
			current = current.left;
		}
		// check if successor has the right child, it cannot have left child for
		// sure
		// if it does have the right child, add it to the left of
		// successorParent.
		// successsorParent
		if (successsor != deleleNode.right) {
			successsorParent.left = successsor.right;
			successsor.right = deleleNode.right;
		}
		return successsor;
	}

	public static BSTNode insert(BSTNode data, BSTNode tree) {
		BSTNode current = tree;
		BSTNode parent = tree;
		// BSTNode newNode;
		if (tree == null) {
			tree = data;
			return tree;
		} else {
			while (true) {
				parent = current;
				if (data.priority < current.priority) {
					current = current.left;
					if (current == null) {
						parent.left = data;
						return tree;
					}
				} else {
					current = current.right;
					if (current == null) {
						parent.right = data;
						return tree;
					}
				}
			}
		}
	}

	public void display(BSTNode root) {
		if (root != null) {
			display(root.left);
			System.out.print("inorder tree traveral is:" + root.priority + "/"
					+ root.inport + "/" + root.type + "/" + root.ipdst + "/"
					+ root.ipsrc + "/" + root.action);
			display(root.right);
		}
	}

	/*
	 * public static void main(int priority, String ipdst, String ipsrc, String
	 * inport, String action, BSTNode left, BSTNode right) { BST b = new BST();
	 * 
	 * 
	 * b.insert(1, "12", "13", "a", "forwordc", null, null);
	 * 
	 * b.insert(8); b.insert(1); b.insert(4); b.insert(6); b.insert(2);
	 * b.insert(10); b.insert(9); b.insert(20); b.insert(25); b.insert(15);
	 * b.insert(16); System.out.println("Original Tree : "); b.display(b.root);
	 * System.out.println("");
	 * System.out.println("Check whether Node with value 4 exists : " +
	 * b.find(4)); System.out.println("Delete Node with no children (2) : " +
	 * b.delete(2)); b.display(root); System.out
	 * .println("\n Delete Node with one child (4) : " + b.delete(4));
	 * b.display(root);
	 * System.out.println("\n Delete Node with Two children (10) : " +
	 * b.delete(10)); b.display(root); }
	 */

}
