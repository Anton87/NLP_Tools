package it.unitn.uvq.antonio.nlp.annotation;

import it.unitn.uvq.antonio.util.IntRange;

public class AbstractAnnotation implements AnnotationI {
	
	AbstractAnnotation(IntRange span) {
		if (span == null) throw new NullPointerException("span: null");
		this.span = span;
	}
	
	AbstractAnnotation(int start, int end) {
		this(new IntRange(start, end));
	}

	@Override
	public IntRange span() { return span; }

	@Override
	public int start() { return span.start(); }

	@Override
	public int end() { return span.end(); }
	
	private final IntRange span;
	
	@Override
	public String toString() {
		return "AbstractAnnotation(start=" + span.start() + ", end=" + span.end() + ")";
	}

}
