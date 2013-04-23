package it.unitn.uvq.antonio.nlp.ner;

import it.unitn.uvq.antonio.nlp.annotation.NeAnnotation;
import it.unitn.uvq.antonio.nlp.annotation.NeAnnotationI;
import it.unitn.uvq.antonio.util.IntRange;
import edu.stanford.nlp.util.Triple;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Labels the sequence of words in a text which are the name of things such 
 *  as person, organization and location.
 * Additionally, it split the list of Named Entity found in
 *   Primary Entities (NE)  and Additional Entities (AE).
 *
 *  @author Antonio Uva 145683
 *
 */
public class AdvancedNER {
	
	/**
	 * Returns a list of Named Entity annotations found in this sentence. 
	 * 
	 * @param namesList The list of names of the main entity
	 * @param namesType The type of the main entity (i.e. {@link NamedEntityType})
	 * @param str The sentence to search for Named Entity annotations
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
		for (Triple<String, Integer, Integer> entityInfo : classifier.classifyToCharacterOffsets(str)) {			
			NeAnnotationI a  = getNeAnnotation(names,  entityInfo, str); 
			annotations.add(a);
		}
		return annotations;
	}
	
	private static NeAnnotationI getNeAnnotation(Names names, Triple<String, Integer, Integer> entityInfo, String str) {
		assert names != null;
		assert entityInfo != null;
		assert str != null;
		
		String entTypeName = (String) entityInfo.first;
		NamedEntityType entType = NamedEntityType.valueOf(entTypeName);
		IntRange entSpan = new IntRange((Integer) entityInfo.second, (Integer) entityInfo.third);
		String entText = str.substring(entSpan.start(), entSpan.end());
		Name entName = newName(entText, entType);
		String entPri = names.isAlias(entName) ? "NE" : "AE";
		NeAnnotationI a = new NeAnnotation(entPri, entType, entSpan);
		return a;		
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
		return retName;
	}	
	
	private final static String SEQUENCE_MODEL = "/home/antonio/workspace/NLP_Tools/stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
	
	
	@SuppressWarnings("unchecked")
	private static AbstractSequenceClassifier<CoreLabel> classifier =
			CRFClassifier.getClassifierNoExceptions(SEQUENCE_MODEL);

}

