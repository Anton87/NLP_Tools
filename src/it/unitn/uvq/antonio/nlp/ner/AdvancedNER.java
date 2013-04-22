package it.unitn.uvq.antonio.nlp.ner;

import it.unitn.uvq.antonio.nlp.annotation.AnnotationApi;
import it.unitn.uvq.antonio.nlp.annotation.BasicAnnoationApiTest;
import it.unitn.uvq.antonio.nlp.annotation.BasicAnnotationApi;
import it.unitn.uvq.antonio.nlp.annotation.NeAnnotation;
import it.unitn.uvq.antonio.nlp.annotation.NeAnnotationI;
import it.unitn.uvq.antonio.nlp.annotation.TextAnnotationI;
import it.unitn.uvq.antonio.nlp.parse.AnnotationAPI;
import it.unitn.uvq.antonio.nlp.parse.Parser;
import it.unitn.uvq.antonio.nlp.parse.tree.Tree;
import it.unitn.uvq.antonio.nlp.parse.tree.TreeBuilder;
import it.unitn.uvq.antonio.util.IntRange;
import edu.stanford.nlp.util.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Labels the sequence of words in a text which are the name of things such 
 *  as person, organization and location.
 * Additionally, it divides the list of Named Entity found within the sentence in
 *   Primary Entities (with text NE) and Additional Entities (with text AE).
 *
 *  @author Antonio Uva 145683
 *
 */
public class AdvancedNER {
	
	/**
	 * Returns a list of NeAnnotations about the NEs found in the text.
	 *  The returned entities are divided in Primary and Additional entities. 
	 * 
	 * @param namesList The list of the name of the Primary Entity
	 * @param namesType The type of the Primary entity
	 * @param str The string to search for Named Entities
	 * @return The list of Named Entity annotations found in the text
	 * @throws NullPointerException if (names == null) || (namesType == null) || (str == null)
	 */
	public static List<NeAnnotationI> classify(List<String> names, NamedEntityType namesType, String str) {
		if (names == null) throw new NullPointerException("names: null");
		if (namesType == null) throw new NullPointerException("namesType: null");
		if (str == null) throw new NullPointerException("str: null");
		return classifyToCharacterOffsets(names, namesType, str);
	}
	
	private static List<NeAnnotationI> classifyToCharacterOffsets(List<String> namesList, NamedEntityType namesType, String str) {
		assert namesList != null;
		assert namesType != null;
		assert str != null;
		
		List<NeAnnotationI> annotations = new ArrayList<>();
		Names names = newNames(namesList, namesType);
		for (Triple<String, Integer, Integer> triple : classifier.classifyToCharacterOffsets(str)) {			
			String eTypeName = triple.first;
			IntRange eSpan = new IntRange(triple.second, triple.third);
			String eNameText = str.substring(eSpan.start(), eSpan.end());			
			NamedEntityType eType = NamedEntityType.valueOf(eTypeName);
			Name eName = newName(eNameText, eType);			
			String aText = names.isAlias(eName) ? "NE" : "AE";			
			NeAnnotationI a = new NeAnnotation(aText, eType, eSpan);
			annotations.add(a);
		}
		return annotations;
	}
	
	private static Names newNames(List<String> namesList, NamedEntityType type) {
		assert namesList != null;
		assert type != null;
		
		List<Name> retNames= new ArrayList<>();		
		for (String name : namesList) { 
			Name retName = newName(name, type);
			retNames.add(retName);
		}
		Names names = Names.of(retNames);
		return names;
	}
	
	private final static Name newName(String name, NamedEntityType type) {
		assert name != null;
		assert type != null;
		
		Name retName = null;
		switch (type) {
			case PERSON:
				retName = new PersonName(name); 
				break;
			case LOCATION:
				retName = new LocationName(name);
				break;
			case ORGANIZATION:
				retName = new OrganizationName(name);
				break;
			default:
				retName = null;
				break;
		}
		// System.out.println(name + " has class " + retName.getClass().getSimpleName());
		return retName;
	}	
	
	private final static String SEQUENCE_MODEL = "/home/antonio/workspace/NLP_Tools/stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
	
	
	@SuppressWarnings("unchecked")
	private static AbstractSequenceClassifier<CoreLabel> classifier =
			CRFClassifier.getClassifierNoExceptions(SEQUENCE_MODEL);
	
	public static void main(String[] args) {
		String str = "He is nicknamed Il Cavaliere (The Knight) for his Order of Merit for Labour.[2] Berlusconi is the longest-serving post-war Prime Minister of Italy, and third longest-serving since the Unification of Italy, after Benito Mussolini and Giovanni Giolitti, holding three separate terms.";
		Tree tree = Parser.parse(str);
		TreeBuilder tb = new TreeBuilder(tree);
		AnnotationApi api = new BasicAnnotationApi();
		String[] names = { "Silvio Berlusconi", "The Knight", "Berlusca", "Nano", "Unto dal Signore", "Il Cavaliere", "Sua Emittenza", "Psiconano", "Il Caimano", "Papi" };
		List<String> namesList = Arrays.asList(names);
		
		List<NeAnnotationI> aList = classify(namesList, NamedEntityType.PERSON, str);
		System.out.print(tree);
		for (NeAnnotationI a : aList) { 
			String text = str.substring(a.start(), a.end()); 
			api.annotate(a, tb);
			System.out.println("Entity(\"" + text + "\", " + a.text() + ", " + a.type() + ", " + a.start() + ", " + a.end() + ")");
		}
		System.out.println(tb);
	}

}

