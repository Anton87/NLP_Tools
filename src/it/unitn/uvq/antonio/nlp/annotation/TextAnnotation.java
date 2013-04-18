package it.unitn.uvq.antonio.nlp.annotation;

import it.unitn.uvq.antonio.util.IntRange;

public class TextAnnotation implements TextAnnotationI {
	
	public TextAnnotation(String text, IntRange span) {
		if (text == null) throw new NullPointerException("text: null");
		this.text = text;
		a = new AbstractAnnotation(span);
	}
	
	public TextAnnotation(String text, int start, int end) {
		this(text, new IntRange(start, end));
	}

	public String text() { return text; }
	
	@Override
	public int start() { return a.start(); }
	
	public int end() { return a.end(); }
	
	@Override
	public IntRange span() { return a.span(); }
	
	private final String text;
	
	@Override
	public String toString() {
		return "TextAnnotation(text=\"" + text + "\", start=" + a.start() + ", end=" + a.end() + ")";
	}
	
	private final AnnotationI a;

}
