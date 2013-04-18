package it.unitn.uvq.antonio.nlp.parse;

import java.util.ArrayList;
import java.util.List;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.adt.tree.TreeBuilder;
import it.unitn.uvq.antonio.nlp.annotation.AnnotationI;
import it.unitn.uvq.antonio.nlp.annotation.TextAnnotationI;
import it.unitn.uvq.antonio.util.IntRange;
import it.unitn.uvq.antonio.util.tuple.Pair;
import it.unitn.uvq.antonio.util.tuple.SimplePair;

/**
 * A basic Annotation API implementation 
 * It annotates only:
 *   1. Nodes exactly matching the annotation boundaries;
 *       e.g.: "(NP (NNP Silvio) (NNP Berlusconi)) ..." exactly matches the annotation Annotation(text="NE", beginPos=0, endPos=17).
 *             tag(a, str) => "(NE (NNP Silvio) (NNP Berlusconi)) ..."
 *   
 *   2. Nodes having a sub-list of children which match the annotation boundaries. 
 *       In this case a new subTree is built by matching only the partly overlapping Tree struct. 
 *       e.g.: "(NP (NNP Silvio) (NNP Berlusconi) ('s POS)) ..." partly matches the annotation Annotation(text="NE", beginPos=0, endPos=17).
 *             tag(a, str) => "(NE (NE (NNP Silvio) (NNP Berlusconi)) ('s POS) ..."
 *       
 *   3. It does not match trees spanning  over too many not groupable trees.
 *       e.g.: "(NP (NP (NNP b) (NNP c)) (NP (NNP d) (NNP e))) ..." cannot be matched by the annotation Annotation(text="NE", beginPos=2, endPos=5).
 * 
 * @author Antonio Uva 145683
 *
 */
public class BasicAnnotationAPI implements AnnotationAPI {
	
	@Override
	public TreeBuilder<Node> annotate(final TextAnnotationI a, TreeBuilder<Node> tree) {
		System.out.println(a + " is " + (isAnnotable(a, tree) ? "" : "not ") + "annotable"); // to sgancel
		if (!isAnnotable(a, tree)) return tree; 
		TreeBuilder<Node> bestTree = findBestMatchingSubTree(a, tree);
		TreeBuilder<Node> aTree = annotateSubTree(a, bestTree);
		return (TreeBuilder<Node>) aTree.getRoot();
	}
	
	@Override
	public boolean isAnnotable(TextAnnotationI a, TreeBuilder<Node> tree) {
		Tree<Node> subTree = findBestMatchingSubTree(a, tree);
		if (subTree == null) { return false; }
		return isPerfectMatch(a.span(), getSpan(subTree)) ||
			   subTreeChildrenMatchAnnotation(a, subTree);
	}
	
	/**
	 * Returns the sub-tree which best matches the annotation span.
	 * 
	 */
	private TreeBuilder<Node> findBestMatchingSubTree(final TextAnnotationI a, TreeBuilder<Node> tree) {
		Pair<Tree<Node>, IntRange> bestMatch = null;
		for (Tree<Node> subTree : tree.getNodes()) {
			if (!subTree.isLeaf()) { 
				IntRange stSpan = getSpan(subTree);
				if (stSpan.contains(a.span()) &&
				   (bestMatch == null || stSpan.compareTo(bestMatch.second()) < 0)) {
						bestMatch = SimplePair.newInstance(subTree, stSpan);
				}
			}
		}
		return bestMatch != null ? ((TreeBuilder<Node>) bestMatch.first()) : null;
	}
	
	/*
	private TreeBuilder<Node> annotateSubTree(final TextAnnotationI a, final TreeBuilder<Node> subTree) { 
		assert a != null;
		assert subTree != null;
		IntRange stSpan = getSpan(subTree);
		
		// if (subTree.matches(a))
		if (isPerfectMatch(stSpan, a.span())) { 
			tagPerfectMatch(a, subTree);
		} 
		// if (subTree.contains(a))
		else if (subTreeChildrenMatchAnnotation(a, subTree)) {
			tagSubTreePortion(a, subTree);
			adjustNodeNumbers((TreeBuilder<Node>) subTree.getRoot());
			adjustParentNodeNumbers((TreeBuilder<Node>) subTree.getRoot());			
		}
		return subTree;		
	}
	*/
	
	
	private TreeBuilder<Node> annotateSubTree(final TextAnnotationI a, final TreeBuilder<Node> tree) {
		assert a != null;
		assert tree != null;
		IntRange tSpan = getSpan(tree); 
		TreeBuilder<Node> root = (TreeBuilder<Node>) tree.getRoot();
		
		if (isPerfectMatch(tSpan, a.span())) {
			root = (TreeBuilder<Node>) tagPerfectMatch(a, tree)
					.getRoot();
		} else if (subTreeChildrenMatchAnnotation(a, tree)) {
			root = (TreeBuilder<Node>) tagSubTreePortion(a, tree).getRoot();
			root = adjustNodeNumbers(root);
			root = adjustParentNodeNumbers(root);
		}
		return root;
	}
	
	
	private TreeBuilder<Node> tagPerfectMatch(final TextAnnotationI a, final TreeBuilder<Node> tree) {
		assert a != null;
		assert tree != null;
		TreeBuilder<Node> parent = (TreeBuilder<Node>) tree.getParent();
		int pos = parent.getChildren().indexOf(tree);
		parent.removeChild(tree);
		TreeBuilder<Node> aTree = tagTree(a, tree);
		parent.addChild(pos, aTree);
		return aTree;
	}
	
	private TreeBuilder<Node> tagSubTreePortion(final TextAnnotationI a, final TreeBuilder<Node> tree) {
		assert a != null;
		assert tree  != null;
		List<Tree<Node>> subTrees = getOverlappingChildren(a, tree);
		int pos = tree.getChildren().indexOf(subTrees.get(0));
		tree.removeChildren(subTrees);
		TreeBuilder<Node> newSubTree = buildNewTree(a, subTrees);
		tree.addChild(pos,  newSubTree);
		return tree;
	}
	
	private TreeBuilder<Node> buildNewTree(final TextAnnotationI a, final List<Tree<Node>> children) {
		assert a != null;
		assert children != null;
		IntRange childrenSpan = getSpan(children);
		Node n = new Node(0, a.text(), childrenSpan.start(), childrenSpan.end(), 0);
		TreeBuilder<Node> tree = new TreeBuilder<Node>(n);
		for (Tree<Node> child : children) {
			TreeBuilder<Node> subTree = new TreeBuilder<>(child);
			tree.addChild(subTree);
		}
		return tree;
	}
	
	private TreeBuilder<Node> tagTree(final TextAnnotationI a, Tree<Node> tree) {
		assert a != null;
		assert tree != null;
		Node o = tree.getElem();
		Node n = new Node(o.nodeNum(), a.text(), o.beginPos(), o.endPos(), o.parentNodeNum());
		TreeBuilder<Node> newTree = new TreeBuilder<Node>(n);
		
		for (Tree<Node> node : tree.getChildren()) {
			Tree<Node> newChild = new TreeBuilder<Node>(node);
			newTree.addChild(newChild);			
		}		 
		return newTree;
	}
	
	private boolean isPerfectMatch(final IntRange r1, final IntRange r2) { 
		assert r1 != null;
		assert r2 != null;
		return r1.equals(r2);
	}
	
	private boolean subTreeChildrenMatchAnnotation(final AnnotationI a, Tree<Node> tree) {
		assert a != null;
		assert tree != null;
		List<Tree<Node>> subTrees = getOverlappingChildren(a, tree);
		IntRange subTreesSpan = getSpan(subTrees);
		return subTreesSpan.equals(a.span());
	}
	
	private List<Tree<Node>> getOverlappingChildren(final AnnotationI a, final Tree<Node> tree) {
		assert a != null;
		assert tree != null;
		List<Tree<Node>> subTrees = new ArrayList<>();
		for (Tree<Node> child : tree.getChildren()) { 
			IntRange childSpan = getSpan(child);
			if (a.span().contains(childSpan)) {
				subTrees.add(child);
			}
		}
		return subTrees;
	}
	
	private TreeBuilder<Node> adjustNodeNumbers(final TreeBuilder<Node> tree) { 
		List<TreeBuilder<Node>> queue = new ArrayList<>();
		queue.add(tree);
		for (int i = 1; !queue.isEmpty(); i++) {
			TreeBuilder<Node> t = queue.remove(0);
			Node o = t.getElem();			
			Node n = new Node(i, o.text(), o.beginPos(), o.endPos(), 0);
			t.setElem(n);
			for (Tree<Node> child : t.getChildren()) { 
				queue.add((TreeBuilder<Node>) child); 
			} 
		}
		return tree;
	}
	
	private TreeBuilder<Node> adjustParentNodeNumbers(final TreeBuilder<Node> tree) {
		for (Tree<Node> t : tree.getNodes()) {
			List<Tree<Node>> children = t.getChildren();
			int nodeNum = t.getElem().nodeNum();
			for (Tree<Node> child : children) { 
				Node o = child.getElem();
				Node n = new Node(o.nodeNum(), o.text(), o.beginPos(), o.endPos(), nodeNum);
				((TreeBuilder<Node>) child).setElem(n);
			}
		}
		return tree;		
	}
	
	private IntRange getSpan(final Tree<Node> tree) {
		assert tree != null;
		return getSpan(tree.getChildren());
	}
	
	private IntRange getSpan(final List<Tree<Node>> trees) { 
		assert trees != null;
		int start = trees.get(0).getElem().beginPos();
		int end = trees.get(trees.size() - 1).getElem().endPos();
		return new IntRange(start, end);
	}

}
