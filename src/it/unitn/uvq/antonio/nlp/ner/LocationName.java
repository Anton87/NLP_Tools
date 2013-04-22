package it.unitn.uvq.antonio.nlp.ner;

public class LocationName extends Name {

	public LocationName(String name) {
		super(name);
	}

	@Override
	public NamedEntityType type() {
		return NamedEntityType.LOCATION;
	}	

}
