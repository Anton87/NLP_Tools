package it.unitn.uvq.antonio.adt.tree;


import java.util.Collections;
import java.util.List;

public class ImmutableTree<E> extends AbstractTree<E> {

	ImmutableTree(final E elem) {
		super(elem);
	}
	
	ImmutableTree(final E elem, final Tree<E> parent) {
		super(elem, parent);
	}
	
	ImmutableTree(final E elem, final Tree<E> parent, final List<Tree<E>> children) {
		super(elem, parent, children);
	}
	
	@Override
	public List<Tree<E>> getLeaves() {
		List<Tree<E>> leaves = super.getLeaves();
		return Collections.unmodifiableList(leaves);			
	}
	
	@Override
	public List<Tree<E>> getChildren() {
		List<Tree<E>> children = super.getChildren();
		return Collections.unmodifiableList(children);
	}	

}
