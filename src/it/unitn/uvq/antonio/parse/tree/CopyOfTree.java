package it.unitn.uvq.antonio.parse.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CopyOfTree {
	
	/*
	
	Tree(String text, int nodeNum) {
		this(text, nodeNum, Collections.<Tree>emptyList());
	}
	
	Tree(String text, int nodeNum, List<Tree> children) {
		this(text, nodeNum, null, children);
	}
	
	Tree(String text, int nodeNum, Tree parent, List<Tree> children) {
		if (text == null) throw new NullPointerException("tree: null");
		if (nodeNum < 0) throw new IllegalArgumentException("nodeNum < 0");
		if (children == null) throw new NullPointerException("children: null");		
		this.text = text;
		this.nodeNum = nodeNum;
		this.parent = parent;
		this.children = new ArrayList<>(children);
	}
	
	public Tree getRoot() { 
		Tree tree = this;
		while (tree.getParent() != null) { 
			tree = tree.getParent();
		}
		return tree;
	}
	
	public Tree getParent() { 
		return parent;
	}
	
	public int getNodeNum() { return nodeNum; }
	
	public String getText() { return text; }
	
	public boolean isLeaf() { return !hasChildren(); }
	
	public boolean hasChildren() { return !children.isEmpty(); }
	
	public List<Tree> getChildren() { return children; }
	
	public Tree getChild(int i) { return children.get(i); }
	
	public List<Tree> getNodes() {
		List<Tree> nodes = new ArrayList<>();
		List<Tree> queue = new ArrayList<>();
		queue.add(this);
		
		while (!queue.isEmpty()) { 
			Tree tree = queue.remove(0);
			nodes.add(tree);
			queue.addAll(tree.getChildren());
		}
		return Collections.unmodifiableList(nodes);
	}
	
	public List<Tree> getLeaves() { 
		List<Tree> leaves = new ArrayList<>();
		for (Tree tree : getNodes()) { 
			if (tree.isLeaf()) { 
				leaves.add(tree);
			}
		}
		return Collections.unmodifiableList(leaves);
	}	
	
	private final String text;
	
	private final int nodeNum;
	
	private final Tree parent;
		
	private final List<Tree> children;

	*/
	
	
}
