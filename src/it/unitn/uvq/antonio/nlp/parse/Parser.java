package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.nlp.parse.tree.Tree;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeBuilder;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeTransformer;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;

/**
 * Parse s
 * @author antonio
 *
 */
public class Parser {
	
	/**
	 * Return a String representing the parse tree of the sentence as
	 *  a bracketed list.
	 *  
	 * @param str A 
	 * @return
	 */
	public static Tree parse(final String str) {
		if (str == null) throw new NullPointerException("str: null");
		List<CoreLabel> tokens = tokenize(str);
		edu.stanford.nlp.trees.Tree tree = parser.apply(tokens);
		return TreeTransformer.transform(tree).build();
				
	}
	
	private static List<CoreLabel> tokenize(final String str) {
		assert str != null;
		Tokenizer<CoreLabel> tokenizer =
				tokenizerFactory.getTokenizer(
						new StringReader(str));
		return tokenizer.tokenize();
	}
	
	private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	
	private static TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
	
	private static LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
	
	public static void main(String[] args) { 
		String str = "Andy Warhol (August 6, 1928 – February 22, 1987) was an American artist who was a leading figure in the visual art movement known as pop art.";
		
		Tree tree = Parser.parse(str);
		TreeBuilder tb = new TreeBuilder(tree);
		System.out.println(tb);
		
		tb.save("/home/antonio/Scrivania/test/tree");
		TreeBuilder newTree = new TreeBuilder();
		newTree.load("/home/antonio/Scrivania/test/tree");
		
		System.out.println(newTree);
	}

}
