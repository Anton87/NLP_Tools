package it.unitn.uvq.antonio.nlp.er;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unitn.uvq.antonio.nlp.annotation.Annotation;
import it.unitn.uvq.antonio.nlp.annotation.AnnotationI;
import it.unitn.uvq.antonio.nlp.annotation.Annotator;
import it.unitn.uvq.antonio.nlp.parse.Parser;
import it.unitn.uvq.antonio.nlp.parse.tree.Tree;
import it.unitn.uvq.antonio.util.tuple.SimpleTriple;
import it.unitn.uvq.antonio.util.tuple.Triple;

/**
 * Returns sequence of words in the sentence which
 *   are name of things.
 *  
 * @author Antonio Uva 145683
 *
 */
public class ER implements Annotator {
	
	/**
	 * Builds a new Entity Recognizer.
	 * 
	 * @param treeRegexs The list of regexs used to extract the names of 
	 *  entities from the text
	 * @throws NullPointerException if (treeRegexs == null)
	 */
	public ER(List<String> treeRegexs) {
		if (treeRegexs == null) throw new NullPointerException("treeRegexs: null");
		
		this.treePatterns = initPatterns(treeRegexs);
	}
	
	@Override
	public List<AnnotationI> annotate(String str) { 
		if (str == null) throw new NullPointerException("str: null");
		
		List<AnnotationI> annotations = new ArrayList<>();
		List<Triple<String, Integer, Integer>> entities = classify(str);
		for (Triple<String, Integer, Integer> entity : entities) {
			AnnotationI a = new Annotation(entity.second(), entity.third());
			annotations.add(a);					
		}
		return annotations;
	}
	
	/**
	 * Returns the list of entity names in this sentence.
	 * 
	 * @param str The string to search for entity names
	 * @return The list of entity names found in the sentence
	 * @throws NullPointerException if (str == null)
	 */
	public List<Triple<String, Integer, Integer>> classify(String str) {
		if (str == null) throw new NullPointerException("str: null");
		
		Tree parse = Parser.parse(str);
		return classify(str, parse);		
	}
	
	/**
	 * Return the list of entity names in the tree NPs node.
	 * 
	 * @param str The string to search for entity names
	 * @param tree The pare tree build form this sentence
	 * @return The list of entity names found in this sentence
	 * @throws NullPointerException if (str == null) || (tree == null)
	 */
	public List<Triple<String, Integer, Integer>> classify(String str, Tree tree) {
		if (str == null) throw new NullPointerException("str: null");
		if (tree == null) throw new NullPointerException("tree: null");
		
		List<Triple<String, Integer, Integer>> entities = new ArrayList<>(); 
		for (Tree node : tree.getNodes()) {
			List<Tree> children = node.getChildren();
			if (hasTag(node, "NP") && 
				tags(children).matches(".*NNPS?.*")) {
				List<Triple<String, Integer, Integer>> matches = match(str, node);
				entities.addAll(matches);
			}
		}
		return entities;
	}
	
	private List<Triple<String, Integer, Integer>> match(String str, Tree tree) {
		assert str != null;
		assert tree != null;		
		
		List<Triple<String, Integer, Integer>> matches = new ArrayList<>();
		for (Pattern pattern : treePatterns) {
			List<Triple<String, Integer, Integer>> pMatches = match(str, tree, pattern);
			matches.addAll(pMatches);
		}
		return matches;		
	}
	
	private List<Tree> parentsOf(List<Tree> trees) { 
		assert trees != null;
		
		List<Tree> parents = new ArrayList<>();
		for (Tree tree : trees) { 
			parents.add(tree.getParent());
		}
		return parents;		
	}
	
	private List<Triple<String, Integer, Integer>> match(String str, Tree tree, Pattern pattern) {
		assert tree != null;
		assert pattern != null;
		
		List<Triple<String, Integer, Integer>> matches = new ArrayList<>();
		List<Tree> leaves = tree.getLeaves();
		
		String tags  = tags(parentsOf(leaves));
		Matcher m = pattern.matcher(tags);
		
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			int startTree = count(tags.substring(0, start), ' ');
			int endTree = count(tags.substring(0, end), ' ');			
			int startCh = leaves.get(startTree).getSpan().start();
			int endCh = leaves.get(endTree).getSpan().end();
			
			String text = str.substring(startCh, endCh);
			while (startTree > 0 && 
					Character.isUpperCase(leaves.get(startTree - 1).getText().charAt(0))) {
				startTree -= 1;
				startCh = leaves.get(startTree).getSpan().start();
				text = str.substring(startCh, endCh);
			}
			Triple<String, Integer, Integer> match = 
					new SimpleTriple<>(text, startCh, endCh);
			matches.add(match);
		}
		return matches;		
	}
	
	private int count(String str, char c) {
		assert str != null;
		
		int count = 0;
		for (int i = 0; i < str.length(); i++) { 
			if (str.charAt(i) == c) count++; 
		}
		return count;
	}
	
	private boolean hasTag(Tree tree, String text) { 
		assert tree != null;
		assert text != null;
		
		return tree.getText().equals(text);
	}
	
	private String tags(List<Tree> trees) { 
		assert trees != null;
		
		StringBuilder sb = new StringBuilder();
		for (Tree tree : trees) { 
			sb.append(tree.getText() + " ");
		}
		String tags = sb.toString();
		return tags.isEmpty() ? "" : tags.substring(0, tags.length() - 1);
	}
	
	private List<Pattern> initPatterns(List<String> treeRegexs) { 
		List<Pattern> patterns = new ArrayList<>();
		for (String regex : treeRegexs) { 
			Pattern p = Pattern.compile(regex);
			patterns.add(p);
		}
		return patterns;
	}
	
	private final List<Pattern> treePatterns;
	
	public static void main(String[] args) { 
		String str = "Paul Robin Krugman (born February 28, 1953) is an American economist, Professor of Economics and International Affairs at the Woodrow Wilson School of Public and International Affairs at Princeton University, Centenary Professor at the London School of Economics, and an op-ed columnist for The New York Times.";
		//String str = "Del Rey initially began performing in clubs in New York City at age 18 and signed her first recording contract when she was 20 years old with 5 Points Records, releasing her first digital album Lana Del Ray a.k.a. Lizzy Grant in January 2010.";
		
		
		List<String> treeRegexs = Arrays.asList(("(?:CD )*NNPS? .* NNPS?"));
		ER er = new ER(treeRegexs);
		
		List<AnnotationI> annotations = er.annotate(str);
		for (AnnotationI a : annotations) { 
			System.out.println("(\"" + str.substring(a.start(), a.end()) +  "\", [" + a.start() + ", " + a.end() + "])");
		}
		
	}
}
