package it.unitn.uvq.antonio.nlp.pos;

/**
 * A TaggedWord object containing a token's name, tag, begin and end positions.
 * 
 * @author Antonio Uva 145683
 *
 */
public class TaggedWord {

	/**
	 * Constructs a new TaggedWord object.
	 * 
	 * @param word A string holding the word value of the tagged word
	 * @param tag A string holding the tag value of the tagged word
	 * @param beginPos An Integer holding begin position of the tagged word
	 * @param endPos An Integer holding the end position of the tagged word
	 * @throws NullPointerException if (word == null || tag == null)
	 * @throws IllegalArgumentException if (beginPos < 0 || endPos < 0) || beginPos > endPos 
	 */
	public TaggedWord(final String word, final String tag, int beginPos, int endPos) {
		if (word == null) throw new NullPointerException("word: null");
		if (tag == null) throw new NullPointerException("tag: null");
		if (beginPos < 0) throw new IllegalArgumentException("beginPos < 0");
		if (endPos < 0) throw new IllegalArgumentException("endPos < 0");
		if (beginPos > endPos) throw new IllegalArgumentException("beginPos > endPos");
		this.word = word;
		this.tag = tag;
		this.beginPos = beginPos;
		this.endPos = endPos;
	}
	
	/**
	 * Return the word value of the taggedWord.
	 * 
	 * @return A string holding the word value of the tagged word.
	 */
	public String word() { return word; }
	
	/**
	 * Return the tag value of the taggedWord.
	 * 
	 * @return A string holding the tag value of the tagged word.
	 */
	public String tag() { return tag; }
	
	/**
	 * Return the begin position of the taggedWord.
	 * 
	 * @return An integer holding the begin position of the tagged word
	 */
	public int beginPos() { return beginPos; }
	
	/**
	 * Return the end position of the tagged word.
	 * 
	 * @return An integer holding the end position of the tagged word
	 */
	public int endPos() { return endPos; }
	
	public final String word;
	
	public final String tag;
	
	public final int beginPos;
	
	public final int endPos;
	
	@Override
	public String toString() {
		return "TaggedWord(" + word  + ", " + tag + ", " + beginPos  + ", " + endPos + ")";
	}

}
