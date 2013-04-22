package it.unitn.uvq.antonio.nlp.ner;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import edu.illinois.cs.cogcomp.entityComparison.core.EntityComparison;

public class PersonName extends Name {

	public PersonName(String name) {
		super(name);
	}
	
	@Override
	public boolean isAlias(Name name) {
		return isAliasOf(name);
	}
	
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
		
	private List<String> tokenize(String str) {
		List<String> parts = new ArrayList<>();
		for (StringTokenizer st = new StringTokenizer(str);
			 st.hasMoreElements();
	    ) { 
			String part = st.nextToken();
			parts.add(part);
		}
		return parts;
	}
	
	/*
	private <E> List<E> intersection(List<E> l, List<E> m) { 
		List<E> lDup = new ArrayList<>(l);
		lDup.retainAll(m);
		return lDup;
	}	
	
	private final List<String> parts; 	
	*/
	
	private final static NamedEntityType TYPE = NamedEntityType.PERSON;
	
	private final static double ACCEPTANCE_THRESHOLD = .82;
	
	private static EntityComparison entityCmp  = new EntityComparison(); 

}
