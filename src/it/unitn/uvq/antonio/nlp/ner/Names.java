package it.unitn.uvq.antonio.nlp.ner;

import java.util.ArrayList;
import java.util.List;

public class Names {
	
	private Names(List<Name> names) { 
		assert names != null;
		this.names = new ArrayList<>(names);
	}
	
	public static Names of(List<Name> names) {
		if (names == null) throw new NullPointerException("names: null");
		return new Names(names);
	}
		
	boolean isAlias(Name name) {
		if (name == null) throw new NullPointerException("name: null");
		for (Name thisName : names) {
			if (thisName.isAlias(name)) return true;			
		}
		return false;
	}
	
	private List<Name> names; 

}
