package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.adt.tree.TreeBuilder;
import it.unitn.uvq.antonio.nlp.parse.exception.TreeUnmarshalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUnmarshaller {
	
	public static Tree<Node> umarshal(final String inFile) throws FileNotFoundException, TreeUnmarshalException {
		if (inFile == null) throw new NullPointerException("inFile: null");
		if (!isDirectory(inFile)) throw new FileNotFoundException("File not found: \"" + inFile + "\".");
		List<Node> nodes = readNodes(inFile);
		Collections.sort(nodes, nodeNumCmp);
		
		Node node = null;
		TreeBuilder<Node> tree = null;
		Map<Integer, TreeBuilder<Node>> map = new HashMap<>();
		
		
		for (int i = 0; i < nodes.size(); i++) {
			node = nodes.get(i);
			tree = new TreeBuilder<>(node);

			TreeBuilder<Node> parent = map.get(node.parentNodeNum());
			if (parent != null) parent.addChild(tree);
			map.put(tree.getElem().nodeNum(), tree);
		}
		tree = map.get(1);
		return tree.build();
	}
	
	private static List<Node> readNodes(final String inFile) throws TreeUnmarshalException { 
		assert inFile != null;
		List<Node> nodes = new ArrayList<>();
		File in = new File(inFile);
		for (File file : in.listFiles()) { 
			String filepath = file.getPath();
			Node node = readNode(filepath);
			nodes.add(node);
		}
		return nodes;
	}
	
	private static Node readNode(final String inFile) throws TreeUnmarshalException {
		Node node = null;
		assert inFile != null;
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(
					new FileInputStream(inFile));
			node = (Node) in.readObject();
		} catch (FileNotFoundException e) {
			throw new TreeUnmarshalException("File not found: \"" + inFile + "\"", e);
		} catch (ClassNotFoundException e) {
			throw new TreeUnmarshalException("Class not found: \"" + TreeUnmarshaller.class.getName() + "\"", e); 
		} catch (IOException e) {
			throw new TreeUnmarshalException("File reading error: \"" + inFile + "\"", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {  }
		}
		return node;
	}
	
	private static boolean isDirectory(final String filepath) { 
		return new File(filepath).isDirectory();
	}
	
	private final static Comparator<Node> nodeNumCmp = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.nodeNum() - o2.nodeNum();
		}
	};
	
	public static void main(String[] args) {
		String inFile = "/home/antonio/Scrivania/test/tree";
		
		Tree<Node> tree = null;
		try {
			tree = TreeUnmarshaller.umarshal(inFile);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + inFile + ".");
		}	
		catch (TreeUnmarshalException e) {
			System.err.println("Tree unmarshalling error.");
		}
		System.out.println(tree);
		
	}

}
