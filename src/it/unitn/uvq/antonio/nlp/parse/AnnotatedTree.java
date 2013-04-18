package it.unitn.uvq.antonio.nlp.parse;

import java.util.List;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.adt.tree.TreeBuilder;

public class AnnotatedTree implements Tree<Node> {
	
	public AnnotatedTree(final Tree<Node> tree) {
		tb = new TreeBuilder<Node>(tree);
	}

	@Override
	public Tree<Node> getRoot() {
		return tb.getRoot();
	}

	@Override
	public Tree<Node> getParent() {
		return tb.getParent();
	}

	@Override
	public Node getElem() {
		return tb.getElem();
	}

	@Override
	public boolean hasChildren() {
		return tb.hasChildren();
	}

	@Override
	public boolean isLeaf() {
		return tb.isLeaf();
	}

	@Override
	public List<Tree<Node>> getChildren() {
		return tb.getChildren();
	}

	@Override
	public Tree<Node> getChild(int i) {
		return tb.getChild(i);
	}

	@Override
	public List<Tree<Node>> getNodes() {
		return tb.getNodes();
	}

	@Override
	public List<Tree<Node>> getLeaves() {
		return tb.getLeaves();
	}
	
	private final TreeBuilder<Node> tb;

}
