package it.unitn.uvq.antonio.adt.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AbstractTree<E> implements Tree<E> {
	
	AbstractTree() { } /* Needed by Copy C-tor. */
	
	AbstractTree(final E elem, Tree<E> parent, final List<Tree<E>> children) {
		if (elem == null) throw new NullPointerException("elem: null");
		if (children == null) throw new NullPointerException("children: null");
		this.elem = elem;
		this.parent = parent;
		this.children = new ArrayList<>(children); /* Defensive programming */
	}
	
	AbstractTree(final E elem, final Tree<E> parent) {
		this(elem, parent, new ArrayList<Tree<E>>());
	}
	
	AbstractTree(final E elem) {
		this(elem, null);
	}

	@Override
	public Tree<E> getRoot() {
		Tree<E> tree = this;
		while (tree.getParent() != null) {
			tree = tree.getParent();
		}
		return tree;
	}

	@Override
	public Tree<E> getParent() {
		return parent;
	}

	@Override
	public E getElem() {
		return elem;
	}
	
	@Override
	public boolean isLeaf() { 
		return !hasChildren();
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public List<Tree<E>> getChildren() {
		return children;
	}

	@Override
	public Tree<E> getChild(int i) {
		return children.get(i);
	}
	
	@Override
	public List<Tree<E>> getNodes() {
		List<Tree<E>> nodes = new ArrayList<>();
		List<Tree<E>> queue = new ArrayList<>(Arrays.asList((Tree<E>) this));
		while (!queue.isEmpty()) {
			Tree<E> node = queue.remove(0);
			nodes.add(node);
			queue.addAll(node.getChildren());
		}
		return Collections.unmodifiableList(nodes);		
	}
	
	@Override
	public List<Tree<E>> getLeaves() {
		List<Tree<E>> leaves = new ArrayList<>();
		List<Tree<E>> queue = new ArrayList<>(Arrays.asList((Tree<E>) this));		
		while (!queue.isEmpty()) {
			Tree<E> node = queue.remove(0);
			if (!node.hasChildren()) {
				leaves.add(node);
			} else {
				queue.addAll(node.getChildren());
			} 
		}
		return Collections.unmodifiableList(leaves);		
	}
	
	/*
	@Override
	public String toString() {
		return treePrinter.print(this);
	}
	*/
	
	protected E elem;
	
	protected Tree<E> parent;
	
	protected List<Tree<E>> children;
	
	//private TreePrinter<E> treePrinter = new SimpleTreePrinter<>();

}
