package it.unitn.uvq.antonio.nlp.parse.tree;

import it.unitn.uvq.antonio.util.IntRange;

import java.util.ArrayList;
import java.util.List;

public final class Tree {
	
	Tree(TreeBuilder tb) { 
		this.tb = tb;
	}
	
	public Tree getRoot() { 
		return new Tree(tb.getRoot());
	}
	
	public Tree getParent() { 
		return new Tree(tb.getParent());
	}
	
	public int getNodeNum() { return tb.getNodeNum(); }
	
	public IntRange getSpan() { return tb.getSpan(); }
	
	public String getText() { return tb.getText(); }
	
	public boolean isLeaf() { return tb.isLeaf(); }
	
	public boolean hasChildren() { return tb.hasChildren(); }
	
	public List<Tree> getChildren() {
		List<Tree> children = new ArrayList<>();
		for (TreeBuilder child : tb.getChildren()) { 
			children.add(new Tree(child));
		}
		return children;
	}
	
	public Tree getChild(int i) { 
		return new Tree(tb.getChild(i));
	}
	
	public List<Tree> getNodes() {
		return toTrees(tb.getNodes());
	}
	
	public List<Tree> getLeaves() { 
		return toTrees(tb.getLeaves());
	}	
	
	private List<Tree> toTrees(List<TreeBuilder> tbs) { 
		List<Tree> trees = new ArrayList<>();
		for (TreeBuilder tb : tbs) { 
			Tree tree = new Tree(tb);
			trees.add(tree);
		}
		return trees;
	}
	
	@Override
	public String toString() {
		return SimpleTreePrinter.print(tb);
	}
	
	TreeBuilder builder() {
		TreeBuilder copy = null;
		try {
			copy = (TreeBuilder) tb.clone();
		} catch (CloneNotSupportedException e) {
			/* Clone operation is supported. */
		}
		return copy;
	}
	
	private TreeBuilder tb;
	
}
