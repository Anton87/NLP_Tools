package it.unitn.uvq.antonio.nlp.annotation;

import it.unitn.uvq.antonio.util.IntRange;

public class NeAnnotation implements NeAnnotationI {
	
	public NeAnnotation(String text, String name, String type, IntRange span) {
		if (name == null) throw new NullPointerException("name: null");
		if (type == null) throw new NullPointerException("type: null");
		this.name = name;
		this.type = type;
		a = new TextAnnotation(text, span);		
	}
	
	public NeAnnotation(String text, String name, String type, int start, int end) {
		this(text, name, type, new IntRange(start, end));
	}

	@Override
	public String text() { return a.text(); }

	@Override
	public IntRange span() { return a.span(); }

	@Override
	public int start() { return a.start(); }

	@Override
	public int end() { return a.end(); }

	@Override
	public String name() { return name; }

	@Override
	public String type() { return type; }
	
	@Override
	public String toString() {
		return "NeAnnotation(text=\"" + a.text() + "\", name=\"" + name() + "\", type=\"" + type() + "\", start=" + a.start() + ", end=" + a.end() + ")";
	}
	
	private final String name;
	
	private final String type;
	
	private final TextAnnotation a;

}
