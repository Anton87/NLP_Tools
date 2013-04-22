package it.unitn.uvq.antonio.nlp.annotation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import it.unitn.uvq.antonio.util.IntRange;

public class AbstractAnnotation implements AnnotationI, Externalizable {
	
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
	
	private IntRange span;
	
	@Override
	public String toString() {
		return "AbstractAnnotation(start=" + span.start() + ", end=" + span.end() + ")";
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(span);	
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		span = (IntRange) in.readObject();		
	}

}
