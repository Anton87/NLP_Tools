package it.unitn.uvq.antonio.nlp.ner;

import edu.illinois.cs.cogcomp.entityComparison.core.EntityComparison;

/**
 * A simple Name class.
 * 
 * @author Antonio Uva 145683
 *
 */
public abstract class Name {
	
	/**
	 * Construct a new name.
	 * 
	 * @param name A string holding the name
	 * @throws NullPointerException if (name == null)
	 */
	public Name(String name) {
		if (name == null) throw new NullPointerException("name: null"); 
		this.text = name;
	}
	
	/**
	 * Checks whether two names are alike.
	 * 
	 * @param name The name to compare against this 
	 * @return true if the specified name is an alias of this one
	 * @throws NullPointerException if (name == null)
	 */
	public boolean isAlias(Name name) {
		if (name == null) throw new NullPointerException("name: null");
		return isAliasOf(name);
	}
	
	protected boolean isAliasOf(Name name) { 
		if (name == null) throw new NullPointerException("name: null");
		String name1 = type().abbr() + "#" + text;
		String name2 = name.type().abbr() + "#" + name.text;
		entityCmp.compareNames(name1, name2);
		return entityCmp.getScore() >= ACCEPTANCE_THRESHOLD;
	}
	
	public abstract NamedEntityType type();
	
	@Override
	public String toString() {
		return "Name(\"" + text + "\")";
	}
	
	protected final String text; 
	
	private static EntityComparison entityCmp = new EntityComparison();
	
	private final static double ACCEPTANCE_THRESHOLD = .82;
	
}
