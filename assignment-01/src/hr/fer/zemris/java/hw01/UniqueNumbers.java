package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that retains unique appearances of integer numbers using an ordered binary tree as a helper structure.
 * Program terminates upon entering the keyword "end".
 *
 * @author jankovidakovic
 */
public class UniqueNumbers {
	
	/**
	 * A single node of the tree
	 */
	static class TreeNode {
		TreeNode left; //left child
		TreeNode right; //right child
		int value; //value that the node holds
	}
	
	/**
	 * Adds the given value as a node of the binary tree, if node with given value doesn't exist already.
	 *
	 * @param root root of the binary tree
	 * @param value value which is to be added to the tree
	 * @return root of the new tree
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if (!containsValue(root, value)) { //no such value is present within the tree
			if (root == null) { //tree is empty
				root = createNode(value);
			} else {
				TreeNode it = root; //tree iterator
				while (value < it.value && it.left != null //left subtree exists from current node
						|| value > it.value && it.right != null) { //right subtree exists
					
					if (value < it.value) { //left subtree represents the smaller values
						it = it.left; //move to the left subtree
					} else {
						it = it.right; //move to the right subtree
					}
				}
				
				if (value < it.value) { //smaller than the current node's value
					it.left = createNode(value); //create the left child
				} else { 
					it.right = createNode(value); //create the right child
				}
			}
		}
		return root;
	}
	
	/**
	 * Creates a new node with given value.
	 *
	 * @param value value of the new node.
	 * @return reference to the newly created node.
	 */
	public static TreeNode createNode(int value) {
		TreeNode newNode = new TreeNode();
		newNode.value = value;
		return newNode;
	}
	
	/**
	 * Calculates the size of the tree, defined as a number of nodes that the tree consists of.
	 *
	 * @param root root of the tree
	 * @return tree size
	 */
	public static int treeSize(TreeNode root) {
		if (root == null) { //empty tree
			return 0;
		} else {
			return treeSize(root.left) //size of the left subtree
					+ treeSize(root.right) //size of the right subtree
					+ 1; //current node
		}
	}
	
	/**
	 * Checks whether the tree contains the provided value.
	 *
	 * @param root root of the tree
	 * @param value value to be found
	 * @return {@code true} if the value is contained within the tree, {@code false} otherwise.
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if (root == null) { //empty tree
			return false; //surely doesn't contain the value
		} else if (root.value == value) { //current node's value corresponds to the wanted value
			return true;
		} else {
			return containsValue(root.left, value) //left subtree
					|| containsValue(root.right, value); //right subtree
		}
	}
	
	/**
	 * Prints the tree's values in ascending order.
	 *
	 * @param root root of the tree
	 */
	public static void printTreeAscending(TreeNode root) {
		if (root != null) {
			printTreeAscending(root.left); //print left subtree (smaller values)
			System.out.printf("%d ", root.value); //print current value
			printTreeAscending(root.right); //print right subtree (greater values)
		}
	}
	
	/**
	 * Prints the tree's values in descending order
	 *
	 * @param root root of the tree
	 */
	public static void printTreeDescending(TreeNode root) {
		if (root != null) {
			printTreeDescending(root.right); //print greater values
			System.out.printf("%d ", root.value); //print current value
			printTreeDescending(root.left); //print smaller values
		}
	}
	
	
	public static void main(String[] args) {

		TreeNode root = null; //create empty tree
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Enter a number >");
			String input = sc.nextLine();
			
			if (input.equals("end")) {
				System.out.print("Numbers in ascending order: ");
				printTreeAscending(root);
				System.out.println(); //newline

				System.out.print("Numbers in descending order: ");
				printTreeDescending(root);
				System.out.println();

				break; //program termination
			} else { //new number is inputted
				try {
					int value = Integer.parseInt(input.trim());
					
					int sizeBeforeAdding = treeSize(root); // tree size before adding
					root = addNode(root, value);
					if (treeSize(root) > sizeBeforeAdding) { // number was added
						System.out.println("Added.");
					} else { // size is the same, number was not added.
						System.out.println("Tree already contains the number, skipping.");
					}
				} catch(NumberFormatException ex) { //cannot be parsed as int
					System.out.printf("'%s' cannot be parsed as an integer.%n", input);
				}	
			}
		}
		sc.close();
	}
}
