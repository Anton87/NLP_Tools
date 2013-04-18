package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.TreeBuilder;
import it.unitn.uvq.antonio.nlp.annotation.TextAnnotationI;


/**
 * Annotates a Tree with one or more annotation objects.
 * 
 * @author Antonio Uva 145683
 *
 */
public interface AnnotationAPI {
	
	/**
	 * Returns a boolean indicating whether the given tree is annoatable by
	 *  the specified annotation.
	 *  
	 * @param a The Annotation 
	 * @param tree The Tree to tag with he specified annotation
	 * @return true if the tree is annotable, false otherwise
	 * @throws NullPointerException if (data == null) || (tree == null)
	 */
	boolean isAnnotable(final TextAnnotationI a, final TreeBuilder<Node> tree);
	
	/**
	 * Returns the tree annotated with the specified annotation.
	 * 
	 * @param data A tuple holding to the data to tag within the tree
	 * @param tree The Tree to tag with the supplied data
	 * @return The Tree annotated with the specified data
	 * @throws NullPointerException if (data == null) || (tree == null)
	 */
	TreeBuilder<Node> annotate(final TextAnnotationI a, final TreeBuilder<Node> tree);

}
