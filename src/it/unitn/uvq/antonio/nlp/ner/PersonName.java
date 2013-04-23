package it.unitn.uvq.antonio.nlp.ner;

/**
 * A simple Person Name class.
 * 
 * @author Antonio Uva 45683
 *
 */
public class PersonName extends Name {

	public PersonName(String name) {
		super(name);
	}
	
	/*
	@Override
	public boolean isAlias(Name name) {
		return isAliasOf(name);
	}
	*/
	
	@Override
	public boolean isAliasOf(Name name) { 
		if (!TYPE.equals(name.type())) { return false; }
		return super.isAliasOf(name);
	}
	
	@Override
	public NamedEntityType type() {
		return TYPE;
	}
	
	@Override
	public String toString() {
		return "PerName(\"" + text + "\")";
	}
	
	private final static NamedEntityType TYPE = NamedEntityType.PERSON;

}
