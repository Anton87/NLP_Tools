package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.adt.tree.TreeBuilder;
import it.unitn.uvq.antonio.util.tuple.Pair;
import it.unitn.uvq.antonio.util.tuple.SimplePair;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;

/**
 * A Transformer class converting a stanford Tree (@see edu.stanford.nlp.trees.Tree) in 
 *  a generic Tree.
 *  
 * @author Antonio Uva 145683
 *
 */
public class CopyOfTreeTransformer {
	
	/**
	 * Transforms a stanford Tree (@see edu.stanford.nlp.trees.Tree) in 
	 *  a generic Tree.
	 * 
	 * @param tree A stanford parse Tree
	 * @return The new generic tree build out the specified stanford Tree
	 * @throws NullPointerExeption if (tree == null)
	 */
	static Tree<Node> transform(final edu.stanford.nlp.trees.Tree tree) {
		if (tree == null) throw new NullPointerException("tree: null");
		root = tree;
		Pair<Integer, Integer> span = getSpan(tree);
		Node node = new Node(tree.nodeNumber(root), tree.value(), span.first(), span.second(), 0);
		TreeBuilder<Node> tb = new TreeBuilder<Node>(node);
		List<Tree<Node>> children = transform(tree.getChildrenAsList(), tb);
		return tb.addChildren(children);
	}
	
	/**
	 * Transform a list of stanford Trees into their corresponding
	 *  generic trees.
	 *  
	 */
	private static List<Tree<Node>> transform(final List<edu.stanford.nlp.trees.Tree> trees, final Tree<Node> parent) {
		assert trees != null;
		assert parent != null;
		List<Tree<Node>> newTrees = new ArrayList<>();
		for (edu.stanford.nlp.trees.Tree tree : trees) {
			Pair<Integer, Integer> span = getSpan(tree);
			int parentNodeNum = parent.getElem().nodeNum();
			Node node = new Node(tree.nodeNumber(root), tree.value(), span.first(), span.second(), parentNodeNum);
			TreeBuilder<Node> newTree = new TreeBuilder<>(node);
			List<Tree<Node>> children =	transform(tree.getChildrenAsList(), (Tree<Node>) newTree);
			newTree.addChildren(children);
			newTrees.add(newTree);
		}
		return newTrees;
	}
	
	/**
	 * Get the span of a node in a tree. A span is composed by a pair of 
	 *  Ints representing the chars' beginPosition and 
	 *  endPosition of the node in the sentence.
	 */
	private static Pair<Integer, Integer> getSpan(final edu.stanford.nlp.trees.Tree tree) {
		assert tree != null;
		List<edu.stanford.nlp.trees.Tree> leaves = tree.getLeaves();
		int beginPos = ((CoreLabel) leaves.get(0).label()).beginPosition();
		int endPos = ((CoreLabel) leaves.get(leaves.size() - 1).label()).endPosition();
		return SimplePair.newInstance(beginPos, endPos);		
	}
	
	private static edu.stanford.nlp.trees.Tree root = null;

}
