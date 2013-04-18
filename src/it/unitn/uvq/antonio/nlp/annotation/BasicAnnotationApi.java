package it.unitn.uvq.antonio.nlp.annotation;

import java.util.ArrayList;
import java.util.List;

import it.unitn.uvq.antonio.parse.tree.TreeBuilder;
import it.unitn.uvq.antonio.util.IntRange;
import it.unitn.uvq.antonio.util.tuple.Pair;
import it.unitn.uvq.antonio.util.tuple.SimplePair;

public class BasicAnnotationApi implements AnnotationApi {

	@Override
	public boolean isAnnotable(TextAnnotationI a, TreeBuilder tree) {
		TreeBuilder subTree = findBestMatchingSubTree(a, tree);
		if (subTree == null) { return false; }
		return subTreeMatch(a, subTree) ||
			   subTreeChidldrenMatchAnnotation(a, subTree);
	}

	@Override
	public TreeBuilder annotate(TextAnnotationI a, TreeBuilder tree) {
		if (isAnnotable(a, tree)) { return false; }
		TreeBuilder subTree = findBestMatchingSubTree(a, tree);
		annotateSubTree(a, subTree);
		return tree;
	}
	
	private TreeBuilder findBestMatchingSubTree(TextAnnotationI a, TreeBuilder tree) { 
		Pair<TreeBuilder, IntRange> bestMatch = null;
		for (TreeBuilder subTree : tree.getNodes()) { 
			if (!subTree.isLeaf()) { 
				IntRange sTreeSpan = subTree.getSpan();
				if (sTreeSpan.contains(a.span())  &&
				   (bestMatch == null) || sTreeSpan.compareTo(bestMatch.second()) < 0) { 
						bestMatch = new SimplePair<>(subTree, sTreeSpan);
				}
			}
		}
		return bestMatch != null ? bestMatch.first() : null;
	}
	
	private TreeBuilder annotateSubTree(final TextAnnotationI a, final TreeBuilder subTree) { 
		assert a != null;
		assert subTree != null;
		
		TreeBuilder root = subTree.getRoot();
		if (subTreeMatch(subTree.getSpan(), a.span())) {
			root = tagSubTree(a, subTree);
		} else if (subTreeChildrenMatchAnnotation(a, subTree)) {
			root = tagSubTreePortion(a, subTree).getRoot();
			// root = adjustNodeNumbers(root);
			// root = adjustParentNodeNumbers(root);
		}
		return root;
	}
	
	private TreeBuilder tagSubTree(TextAnnotationI a, TreeBuilder subTree) {
		assert a != null;
		assert subTree != null;
		TreeBuilder parent = subTree.getParent();
		int pos = parent.getChildren().indexOf(subTree);
		parent.removeChild(subTree);
		TreeBuilder aTree = tagTree(a, subTree);
		parent.addChild(pos, aTree);
		return aTree;
	}
	
	private TreeBuilder tagSubTreePortion(TextAnnotationI a, TreeBuilder subTree) { 
		assert a != null;
		assert subTree != null;
		List<TreeBuilder> subTreeChildren = getOverlappingChildren(a, subTree);
		int pos = subTree.getChildren().indexOf(subTreeChildren.get(0));
		subTree.removeAll(subTreeChildren);
		TreeBuilder newSubTree = buildNewTree(a, subTreeChildren);
		subTree.addChild(pos, newSubTree);
		return subTree;
	}
	
	private TreeBuilder buildNewTree(TextAnnotationI a, List<TreeBuilder> children) { 
		assert a != null;
		assert children != null;
		IntRange cSpan = getSpan(children);
		
		TreeBuilder newSubTree = new TreeBuilder(text, 0, cSpan.start(), cSpan.end());
		newSubTree.addChildren(children);
		
		return newSubTree;
	}
	
	private TreeBuilder tagSubTree(TextAnnotationI a, TreeBuilder subTree) {
		assert a != null;
		assert subTree != null;
		subTree.setText(a.text());
		return subTree;
	}
	
	private boolean subTreeMatch(TextAnnotationI a, TreeBuilder subTree) {
		assert a != null;
		assert subTree != null;
		return subTree.getSpan().equals(a.span());
	}
	
	private boolean subTreeChildrenMatchAnnotation(TextAnnotationI a, TreeBuilder subTree) {
		assert a != null;
		assert tree != null;
		List<TreeBuilder> subTreeChildren = getOverlappingChildren(a, subTree);
		IntRange span = getSpan(subTreeChildren);
		return span.equals(a.span());
	}
	
	private List<TreeBuilder> getOverlappingChildren(TextAnnotationI a, TreeBuilder tree) {
		assert a != null;
		assert tree != null;
		List<TreeBuilder> subTrees = new ArrayList<>();
		for (TreeBuilder child : tree.getChildren()) { 
			if (a.span().contains(child.getSpan())) {
				subTrees.add(child);
			}
		}
		return subTrees;
	}
	
	private IntRange getSpan(List<TreeBuilder> trees) {
		assert trees != null;
		int start = trees.get(0).start();
		int end = trees.get(trees.size() - 1).end();
		return new IntRange(start, end);
	}
 
}
