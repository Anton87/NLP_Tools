package it.unitn.uvq.antonio.nlp.parse;

import java.util.Iterator;
import java.util.List;

import it.unitn.uvq.antonio.adt.tree.Tree;

public class TreeNodePrinter implements TreePrinter {

	@Override
	public String print(final Tree<Node> tree) {
		if (tree == null) throw new NullPointerException("tree: null");
		Node n = tree.getElem();
		if (tree.isLeaf()) { 
			return "{" + n.nodeNum() + ", " + n.parentNodeNum() + ", " + n.text() + "}";
		} else {
			return "({" + n.nodeNum() + ", " + n.parentNodeNum() + ", " + n.text() + "} " + printChildren(tree.getChildren()) + ")";
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
	

}
