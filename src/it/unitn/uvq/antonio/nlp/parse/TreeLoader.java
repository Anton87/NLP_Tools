package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.adt.tree.Trees;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeTransformer;

/**
 * Load a Tree from its parse. The returned tree is unmodifiable.
 *  
 * @author Antonio Uva 145683
 *
 */
class TreeLoader {
	
	/**
	 * Returns an ImmodifiableTree from its parse.
	 * 
	 * @param parse A String holding the tree as a bracketed list
	 * @return The ImmodifiableTree  corresponding to the specified parse
	 * @throws NullPointerException if (parse == null)
	 */
	public static Tree<Node> load(final String parse) {
		if (parse == null) throw new NullPointerException("parse: null");
		edu.stanford.nlp.trees.Tree sTree = edu.stanford.nlp.trees.Tree.valueOf(parse);
		Tree<Node> tree = TreeTransformer.transform(sTree);
		return Trees.unmodifiableTree(tree);
	}

}
