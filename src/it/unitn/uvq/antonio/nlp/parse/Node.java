package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.logging.Logger;

/**
 * A simple parse tree Node.
 * 
 * @author Antonio Uva 145683
 *
 */
public class Node implements Externalizable {
	
	public Node() {
		//System.out.println("Node Costructor");
	}
	
	/**
	 * Constructs a new parse tree Node.
	 * 
	 * @param nodeNum An integer holding the node number
	 * @param text A String holding the node text
	 * @param beginPos An Integer holding the text begin position
	 * @param endPos An Integer holding the text end position
	 * @param parendNodeNum An Integer holding the parent node number
	 * @throws NullPointerException if (text == null) 
	 * @throws IllegalArgumentException if (nodeNum < 0) || (parentNodeNum < 0) || (parentNodeNum < nodeNum) 
	 * @throws IllegalArgumentexception if (beginPos < 0) || (endPos <= 0) || (beginPos > endPos)
	 */
	public Node(int nodeNum, final String text, int beginPos, int endPos, int parentNodeNum) {
		if (nodeNum < 0) throw new IllegalArgumentException("nodeNum < 0");
		if (text == null) throw new NullPointerException("text: null");
		if (beginPos < 0) throw new IllegalArgumentException("beginPos < 0");
		if (endPos <= 0) throw new IllegalArgumentException("endPos <= 0");
		if (beginPos > endPos) throw new IllegalArgumentException("beginPos > endPos");
		if (parentNodeNum < 0) throw new IllegalArgumentException("parentNodeNum < 0");
		if (nodeNum < parentNodeNum) throw new IllegalArgumentException("nodeNum < parentNodeNum");
		this.nodeNum = nodeNum;
		this.text = text;
		this.beginPos = beginPos;
		this.endPos = endPos;
		this.parentNodeNum = parentNodeNum;
	}
	
	/**
	 * Returns the number of the node.
	 * 
	 * @return An Integer holding the number of the node 
	 */
	public int nodeNum() { return nodeNum; }
	
	/**
	 * Returns the text of the node.
	 * 
	 * @return A String holding the text of the node
	 */
	public String text() { return text; }
	
	/**
	 * Returns the begin position of the text.
	 * 
	 * @return An Integer holding the begin position of the text
	 */
	public int beginPos() { return beginPos; }
	
	/**
	 * Returns the end position of the text.
	 * 
	 * @return An Integer holding the end position of the text
	 */
	public int endPos() { return endPos; }
	
	/**
	 * Returns the node number of this parent node.
	 * 
	 * @return An Integer holding the node number of this parent node
	 */
	public int parentNodeNum() { return parentNodeNum; }
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		//System.out.println("Node.writeExternal");
		out.writeInt(nodeNum);
		out.writeObject(text);
		out.writeInt(beginPos);
		out.writeInt(endPos);
		out.writeInt(parentNodeNum);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		//System.out.println("Node.readExternal");
		nodeNum = in.readInt();
		text = (String) in.readObject();
		beginPos = in.readInt();
		endPos = in.readInt();
		parentNodeNum = in.readInt();
	}
	
	private int nodeNum;
	
	private String text;
	
	private int beginPos;
	
	private int endPos;
	
	private int parentNodeNum;
	
	@Override
	public String toString() {
		return "Node(nodeNum=" + nodeNum + ", text=" + text + ", beginPos=" + beginPos + ", endPos=" + endPos + ", parentNodeNum=" + parentNodeNum + ")";
	}
	
	public static void main(String[] args) { 
		String filepath = "/home/antonio/Scrivania/tree/71923275.out";
		Node node = null;
		ObjectInputStream in = null;
		try {	
			in = 
					new ObjectInputStream(
							new FileInputStream(filepath));
			node = (Node) in.readObject();
			
		} catch (IOException e) {
			Logger.getLogger(Node.class.getName()).warning("Error while reading node.");
		} catch (ClassNotFoundException e) {
			Logger.getLogger(Node.class.getName()).warning("Class " + Node.class.getName() + " not found.");
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				
			}
		}
		System.out.println(node);
		
		
		
		String str = "Silvio Berlusconi is an Italian politician and media tycoon.";
		Tree<Node> tree = Parser.parse(str); 
		
		System.out.println(tree);
		
		/*
		final String dirpath = "/home/antonio/Scrivania/tree";
		
		for (Tree<Node> treeNode : tree.getNodes()) {
			Node node = treeNode.getElem();
			String outFile = dirpath + File.separator + node.hashCode() + ".out";
			ObjectOutputStream out = null;
			try {
				out =
						new ObjectOutputStream(
								new FileOutputStream(outFile));
				out.writeObject(node);
			} catch (IOException e) {
				Logger.getLogger(Node.class.getName()).warning("Error while writing node: \"" + node.hashCode() + "\" on file.");
			} finally {
				try {
					out.close();
				} catch (IOException e) { 
					Logger.getLogger(Node.class.getName()).warning("Error while closing stream.");
				}
			}
		}
		*/
	}

}
