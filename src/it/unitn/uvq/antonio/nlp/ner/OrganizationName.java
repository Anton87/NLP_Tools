package it.unitn.uvq.antonio.nlp.ner;

public class OrganizationName extends Name {

	public OrganizationName(String name) {
		super(name);
	}

	@Override
	public NamedEntityType type() {
		return TYPE;
	}
	
	private final static NamedEntityType TYPE = NamedEntityType.ORGANIZATION;

}
