package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;

import java.util.Iterator;
import java.util.List;

public class SimpleTreePrinter implements TreePrinter {
	
	public static String printTree(final Tree<Node> tree) { 
		return INSTANCE.print(tree);
	}

	@Override
	public String print(final Tree<Node> tree) {
		if (tree == null) throw new NullPointerException("tree: null");
		if (tree.isLeaf()) {
			return tree.getElem().text();
		} else {
			return "(" + tree.getElem().text() + " " + printChildren(tree.getChildren()) + ")";			
		}
	}
	
	private String printChildren(final List<Tree<Node>> children) {
		assert children != null;
		StringBuilder sb = new StringBuilder();
		for (Iterator<Tree<Node>> it = children.iterator(); it.hasNext(); ) {
			Tree<Node> child = it.next();
			sb.append(print(child));
			if (it.hasNext()) { sb.append(" "); }
		}
		return sb.toString();
	}
	
	private final static SimpleTreePrinter INSTANCE = new SimpleTreePrinter(); 

}
