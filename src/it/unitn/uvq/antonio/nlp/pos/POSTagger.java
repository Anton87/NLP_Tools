package it.unitn.uvq.antonio.nlp.pos;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Reads a sentence and assigns part-of-speech to each word and
 *  other tokens.
 *  
 * @author Antonio Uva 145683
 *
 */
public class POSTagger {
	
	/**
	 * Breaks the text into a list of TaggedWord tokens (i.e. the words with their tags).  
	 * 
	 * @param str A string holding the text to break into tokens
	 * @return The list of TaggedWord appearing in the text
	 * @throws NullPointerException if str is null
	 */
	public static List<TaggedWord> tag(final String str) {
		if (str == null) throw new NullPointerException("str: null");
		List<HasWord> hasWords = getHasWords(str);
		List<TaggedWord> taggedWords = new ArrayList<>();		
		for (edu.stanford.nlp.ling.TaggedWord tw : tagger.tagSentence(hasWords)) {
			TaggedWord taggedWord = new TaggedWord(tw.word(), tw.tag(), tw.beginPosition(), tw.endPosition());
			taggedWords.add(taggedWord);
		}
		return taggedWords;
	}
	
	private static List<HasWord> getHasWords(final String str) {
		assert str != null;
		List<HasWord> hasWords = new ArrayList<>();
		DocumentPreprocessor dp =
				new DocumentPreprocessor(
						new StringReader(str));
		for (Iterator<List<HasWord>> it = dp.iterator(); it.hasNext(); ) {
			hasWords.addAll(it.next());
		}
		return hasWords;
	}
				
	private final static String POSTAG_MODEL = "/home/antonio/workspace/NLP_Tools/stanford-postagger/models/english-bidirectional-distsim.tagger";
	
	private static Logger logger = Logger.getLogger(POSTagger.class.getName());
	
	private static MaxentTagger tagger;
	
	static {
		try {
			tagger = new MaxentTagger(POSTAG_MODEL);
			assert tagger != null;
		} catch (ClassNotFoundException e) {
			logger.warning("Class MaxentTagger not found.");
		} catch (IOException e) {
			logger.warning("Error while loading Model \"" + POSTAG_MODEL + "\".");
		}
	}

}
