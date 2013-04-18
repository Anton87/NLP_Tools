package it.unitn.uvq.antonio.nlp.ner;

import it.unitn.uvq.antonio.util.tuple.Quadruple;
import it.unitn.uvq.antonio.util.tuple.SimpleQuadruple;
import edu.stanford.nlp.util.Triple;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * This NER (Named Entity Recognizer) labels sequence of words in a text
 *  which are the name of things such as person, organization and location names. 
 *
 * @author Antonio Uva 145683
 *
 */
public class NER {
	
	/**
	 * Returns a list of triples holding the info about NEs found.
	 * 
	 * @param str A string holding the text to search for named entities
	 * @return The list of triples holding the NE's names and boundaries
	 * @throws NullPointerException if str is null
	 */
	public static List<Quadruple<String, String, Integer, Integer>> classify(final String str) {
		if (str == null) throw new NullPointerException("str: null");
		return classifyToCharacterOffsets(str);
	}
	
	private static List<Quadruple<String, String, Integer, Integer>> classifyToCharacterOffsets(final String str) { 
		assert str != null;
		List<Quadruple<String, String, Integer, Integer>> qples = new ArrayList<>();
		for (Triple<String, Integer, Integer> triple : classifier.classifyToCharacterOffsets(str)) { 
			String name = str.substring(triple.second(), triple.third());
			Quadruple<String, String, Integer, Integer> qple = new SimpleQuadruple<>(name, triple.first, triple.second, triple.third);
			qples.add(qple);			
		}
		return qples;
	}
	
	private final static String SEQUENCE_MODEL =  "/home/antonio/workspace/NLP_Tools/stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
	
	//private final static String SEQUENCE_MODEL = "stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
	
	@SuppressWarnings("unchecked")
	private static AbstractSequenceClassifier <CoreLabel> classifier = 
			CRFClassifier.getClassifierNoExceptions(SEQUENCE_MODEL);

}
