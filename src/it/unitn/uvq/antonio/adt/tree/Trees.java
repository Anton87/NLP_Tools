package it.unitn.uvq.antonio.adt.tree;


public class Trees<E> {
	
	public static <E> Tree<E> unmodifiableTree(final Tree<E> tree) { 
		TreeBuilder<E> copy = new TreeBuilder<E>(tree);
		return copy.build();
	}
	
	private Trees() { }

}
