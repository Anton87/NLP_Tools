package it.unitn.uvq.antonio.nlp.parse;

import java.util.List;

import it.unitn.uvq.antonio.nlp.parse.tree.Tree;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeBuilder;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeBuilderFactory;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeNodePrinter;
import it.unitn.uvq.antonio.nlp.pos.POSTagger;
import it.unitn.uvq.antonio.nlp.pos.TaggedWord;
import it.unitn.uvq.antonio.util.IntRange;

public class VectorParser {
	
	public static Tree parse(String str) { 
		List<TaggedWord> taggedWords = POSTagger.tag(str);
		
		int start = taggedWords.get(0).beginPos;
		int end = taggedWords.get(taggedWords.size() - 1).endPos;
		int i = 1;
		TreeBuilder root = TreeBuilderFactory.newInstance("ROOT", 1, new IntRange(start, end));
		TreeBuilder s = TreeBuilderFactory.newInstance("S", 2, new IntRange(start, end));
		root.addChild(s);
		
		i = 3;
		for (TaggedWord tw : taggedWords) { 
			IntRange span = new IntRange(tw.beginPos, tw.endPos);			
			TreeBuilder tag = TreeBuilderFactory.newInstance(tw.tag, i, span);
			TreeBuilder word = TreeBuilderFactory.newInstance(tw.word, i + taggedWords.size(), span);
			s.addChild(tag);
			tag.addChild(word);			
		}
		return root.build();
	}
	
	public static void main(String[] args) { 
		String str = "Silvio Berlusconi is an Italian politician and media tycoon.";
		Tree vec = VectorParser.parse(str);
		
		//System.out.println(vec);
		System.out.println(TreeNodePrinter.print(vec));	
	}

}
