package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;

public interface TreePrinter {
	
	public String print(final Tree<Node> tree);

}
