package it.unitn.uvq.antonio.adt.tree;

import java.util.List;

public interface Tree<E> {
	
	Tree<E> getRoot();
	
	Tree<E> getParent();
	
	E getElem();
	
	boolean hasChildren();
	
	boolean isLeaf();
	
	List<Tree<E>> getChildren();
	
	Tree<E> getChild(int i);
	
	List<Tree<E>> getNodes();
	
	List<Tree<E>> getLeaves();
	
}
