package it.unitn.uvq.antonio.adt.tree;


import java.util.ArrayList;
import java.util.List;

public class TreeCopier {
	
	static <E> Tree<E> clone(final Tree<E> tree) {
		if (tree == null) throw new NullPointerException("tree: null"); 
		TreeBuilder<E> copy = new TreeBuilder<E>(tree.getElem());
		return copy.addChildren(clone(tree.getChildren(), copy));
	}
	
	private static <E> List<Tree<E>> clone(final List<Tree<E>> trees, final Tree<E> parent) {
		assert trees != null;
		assert parent != null;
		List<Tree<E>> copies = new ArrayList<>();
		for (Tree<E> tree : trees) {
			TreeBuilder<E> copy = new TreeBuilder<E>(tree.getElem(), parent);
			copy.addChildren(clone(tree.getChildren(), copy));
			copies.add(copy);
		}
		return copies;
	}

}
