package it.unitn.uvq.antonio.nlp.chunking;

/**
 * A simple class storing a chunk of text, type, start and end position.
 * 
 * @author Antonio Uva 145693
 *
 */
public class Span {
	
	/**
	 * Constructs a new Span instance.
	 * 
	 * @param text A string holding the span's text
	 * @param type A string holding the span's type
	 * @param beginPos An integer holding the start position of the span
	 * @param endPos An integer holding the end position of the span
	 * @throws NullPointerException if (text == null || type == null)
	 * @throws IllegalArgumentException if (beginPos < 0 || endPos < 0) || (beginPos > endPos)
	 */
	public Span(final String text, final String type, int beginPos, int endPos) {
		if (text == null) throw new NullPointerException("text: null");
		if (type == null) throw new NullPointerException("type: null");
		if (beginPos < 0) throw new IllegalArgumentException("beginPos < 0");
		if (endPos < 0) throw new IllegalArgumentException("endPos < 0"); 
		if (beginPos > endPos) throw new IllegalArgumentException("beginPos > endPos");
		this.text = text;
		this.type = type;
		this.beginPos = beginPos;
		this.endPos = endPos;
	}
	
	public String text() { return text; }
	
	public String type() { return type; }
	
	public int beginPos() { return beginPos; }
	
	public int endPos() { return endPos; }
	
	private final String text;
	
	private final String type;
	
	private final int beginPos;
	
	private final int endPos;
	
	@Override
	public String toString() {
		return "Span(" + text + ", " + type + ", " + beginPos + ", " + endPos + ")";
	}

}
