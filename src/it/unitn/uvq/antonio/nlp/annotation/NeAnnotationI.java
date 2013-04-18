package it.unitn.uvq.antonio.nlp.annotation;

public interface NeAnnotationI extends TextAnnotationI {
	
	/**
	 * Returns the name of the Named Entity.
	 * 
	 * @return A string holding the name of the Named Entity
	 */
	String name();
	
	/**
	 * Returns the type of the Named Entity (e.g. PERSON, LOCATION, ORGANIZATION).
	 * 
	 * @return A string holding the type of the Named Entity
	 */
	String type();

}
