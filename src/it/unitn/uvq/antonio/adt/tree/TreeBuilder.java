package it.unitn.uvq.antonio.adt.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilder<E> extends AbstractTree<E> {
	
	public TreeBuilder(final Tree<E> tree) {
		TreeBuilder<E> copy = (TreeBuilder<E>) clone(tree);
		this.elem = copy.getElem();
		this.children = copy.getChildren();
	}
	
	public TreeBuilder(final E elem) {
		super(elem);
	}
	
	public TreeBuilder(final E elem, final Tree<E> parent) {
		super(elem, parent);
	}
	
	public TreeBuilder(final E elem, final Tree<E> parent, final List<Tree<E>> children) {
		super(elem, parent, children);
	}
	
	public TreeBuilder<E> setElem(final E elem) {
		this.elem = elem;
		return this;
	}
	
	
	public Tree<E> addChild(final Tree<E> child) {
		if (child == null) throw new NullPointerException("child: null");
		return addChild(children.size(), child);
	}
	
	public Tree<E> addChild(int i, final Tree<E> child) {
		if (i < 0) throw new IllegalArgumentException("i < 0");
		if (i > children.size()) throw new IllegalArgumentException("i > children.size()");
		if (child == null) throw new NullPointerException("child: null");
		children.add(i, child);
		((TreeBuilder<E>) child).setParent(this);		
		return this;
	}
	
	public Tree<E> addChildren(final List<Tree<E>> children) {
		if (children == null) throw new NullPointerException("children: null"); 
		for (Tree<E> child : children) { this.addChild(child); }
		return this;
	}
	
	/**
	 * Remove the specified tree from the children list (if such a tree exists).
	 * 
	 * @param child The child to remove
	 * @return The new Tree with the specified child removed
	 * @throws NullPointerException if (child == null)
	 */
	public Tree<E> removeChild(final Tree<E> child) { 
		if (child == null) throw new NullPointerException("child: null");
		if (children.remove(child)) {
			((TreeBuilder<E>) child).setParent(null);
		}
		return this;
	}
	
	/**
	 * Remove the specified trees from the children list (if such tree exist). 
	 * 
	 * @param children The list of children to remove
	 * @return the new Tree with the specified child removed 
	 * @throws NullPointerException if (children == null)
	 */
	public Tree<E> removeChildren(final List<Tree<E>> children) {
		if (children == null) throw new NullPointerException("children: null");
		for (Tree<E> child : children) { 
			removeChild(child);
		}
		return this;
	}
	
	public Tree<E> setParent(final Tree<E> tree) {
		this.parent = tree;
		return this;
	}
	
	public Tree<E> build() { 
		return build(this);
	}
	
	private static <E> Tree<E> clone(final Tree<E> tree) {
		return TreeCopier.clone(tree);
	}
	
	private List<Tree<E>> build(final List<Tree<E>> children) {
		assert children != null;
		List<Tree<E>> nChildren = new ArrayList<>();
		for (Tree<E> child : children) {
			ImmutableTree<E> nChild = build(child);
			nChildren.add(nChild);
		}
		return nChildren;		
	}
	
	private ImmutableTree<E> build(final Tree<E> child) {
		assert child != null;
		return new ImmutableTree<E>(child.getElem(), child.getParent(), build(child.getChildren()));
	}
	
	public static void main(String[] args) {
		TreeBuilder<Integer> tb = new TreeBuilder<>(1);
		System.out.println(tb.getChildren());
	}

}
