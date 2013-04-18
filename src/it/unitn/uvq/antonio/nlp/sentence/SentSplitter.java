package it.unitn.uvq.antonio.nlp.sentence;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;

/**
 * Breaks a text into sentences.
 * 
 * @author Antonio Uva 145683
 *
 */

public class SentSplitter {
	
	/**
	 * Splits a string into its sentences.
	 * 
	 * @param str A string holding the text to split
	 * @return The list of split sentences
	 * @throws NullPointerException if the input string is null
	 */
	public static List<String> split(final String str) { 
		if (str == null) throw new NullPointerException("str: null");
		return ssplit(str);
	}
		
	private static List<String> ssplit(final String str) { 
		assert str != null;
		DocumentPreprocessor dp = 
				new DocumentPreprocessor(
						new StringReader(str));
		dp.setTokenizerFactory(PTBTokenizerFactory.newCoreLabelTokenizerFactory(""));
		List<String> sentences = new ArrayList<>();
		for (Iterator<List<HasWord>> it = dp.iterator(); it.hasNext(); ) {
			List<HasWord> words = it.next();
			CoreLabel first = (CoreLabel) words.get(0);			
			CoreLabel last =  (CoreLabel) words.get(words.size() - 1);
			String sent = str.substring(first.beginPosition(), last.endPosition());
			sentences.add(sent);
		}
		return sentences;
	}

}
