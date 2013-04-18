package it.unitn.uvq.antonio.parse.tree;

import java.util.ArrayList;
import java.util.List;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.adt.tree.TreeBuilder;
import it.unitn.uvq.antonio.nlp.annotation.NeAnnotation;
import it.unitn.uvq.antonio.nlp.annotation.NeAnnotationI;
import it.unitn.uvq.antonio.nlp.annotation.TextAnnotationI;
import it.unitn.uvq.antonio.nlp.ner.NER;
import it.unitn.uvq.antonio.nlp.parse.AnnotationAPI;
import it.unitn.uvq.antonio.nlp.parse.BasicAnnotationAPI;
import it.unitn.uvq.antonio.nlp.parse.Node;
import it.unitn.uvq.antonio.nlp.parse.Parser;
import it.unitn.uvq.antonio.nlp.parse.SimpleTreePrinter;
import it.unitn.uvq.antonio.nlp.parse.TreePrinter;
import it.unitn.uvq.antonio.util.tuple.Quadruple;


public class BasicAnnotationAPITest {
	
	private final static List<TextAnnotationI> getAnnotations(final String str) {
		List<TextAnnotationI> aList = new ArrayList<>();
		List<Quadruple<String, String, Integer, Integer>> nes = NER.classify(str);
		
		for (Quadruple<String, String, Integer, Integer> ne : nes) { 
			NeAnnotationI a = new NeAnnotation("NE", ne.first(), ne.second(), ne.third(), ne.fourth());
			aList.add(a);
		}
		return aList;
	}
	
	private static String flatten(final Tree<Node> tree) {
		TreePrinter printer = new SimpleTreePrinter();
		return printer.print(tree);
	}
	
	public static Tree<Node> parseAndAnnotate(final String str) {
		AnnotationAPI annotationAPI = new BasicAnnotationAPI();
		Tree<Node> tree = Parser.parse(str);
		List<TextAnnotationI> aList = getAnnotations(str);
		
		TreeBuilder<Node> tb = new TreeBuilder<>(tree);
		for (TextAnnotationI a : aList) {
			tb = annotationAPI.annotate(a, tb);
		}
		return tb.build();
	}
	
	public final static void main(String[] args) {
		String[] sents = {
			"Silvio Berlusconi is an Italian politician and media tycoon.",
			"Paul Robin Krugman (born February 28, 1953) is an American economist, Professor of Economics and International Affairs at the Woodrow Wilson School of Public and International Affairs at Princeton University.",
			"In 2008, Krugman won the Nobel Memorial Prize for his contributions to New Trade Theory and New Economic Geography.",
			"In 2000, Stiglitz founded the Initiative for Policy Dialogue (IPD), a think tank on international development based at Columbia University."
		};
		
		for (String str : sents) { 
			Tree<Node> tree = parseAndAnnotate(str);
			System.out.println("aTree: " + flatten(tree));
		}
	}

}
