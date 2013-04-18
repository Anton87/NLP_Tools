package it.unitn.uvq.antonio.nlp.chunking;

import it.unitn.uvq.antonio.nlp.pos.TaggedWord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import it.unitn.uvq.antonio.nlp.chunking.Span;

/**
 * Divides a text in syntactically correlated parts of words, 
 *  likes noun groups, verbs groups, etc...
 *  
 * @author Antonio Uva 145683
 *
 */
public class Chunker {
	
	/**
	 * Chunks a set of TaggedWord objects into spans.
	 * 
	 * @param taggedWords The list of Taggedwords objects to chunk
	 * @return The list of spans holding the chunked words 
	 * @throws NullPointerException if taggedWords is null
	 */
	public static List<Span> chunk(final List<TaggedWord> taggedWords) {
		if (taggedWords == null) throw new NullPointerException("taggedWords: null");
		List<Span> chunks = new ArrayList<>();
		String[] toks = getToks(taggedWords);
		String[] tags = getTags(taggedWords);
		for (opennlp.tools.util.Span s : chunker.chunkAsSpans(toks, tags)) { 
			List<TaggedWord> span = taggedWords.subList(s.getStart(), s.getEnd());
			String text = getText(span);
			Span chunk = new Span(text, s.getType(), span.get(0).beginPos, span.get(span.size() - 1).endPos);
			chunks.add(chunk);
		}
		return chunks;
	}
	
	private static String getText(final List<TaggedWord> tws) {
		assert tws != null;
		StringBuilder builder = new StringBuilder();
		
		TaggedWord tw = null, nextTw;
		int i = 0;
		for ( ; i < tws.size() - 1; i++) {
			tw = tws.get(i);
			nextTw = tws.get(i + 1);
			builder.append(
					tws.get(i).word());
			builder.append(
					repeat(' ', nextTw.beginPos() - tw.endPos()));			
		}
		tw = tws.get(i);
		builder.append(tw.word());
		return builder.toString();		
	}
	
	private static String repeat(char c, int n) {
		assert n >= 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) { sb.append(c); }
		return sb.toString();
	}
	
	private static String[] getToks(final List<TaggedWord> taggedWords) {
		assert taggedWords != null;
		String[] toks = new String[taggedWords.size()];
		for (int i = 0; i < toks.length; i++) { 
			toks[i] = taggedWords.get(i).word();
		}
		return toks;
	}
	
	private static String[] getTags(final List<TaggedWord> taggedWords) {
		assert taggedWords != null;
		String[] pos = new String[taggedWords.size()];
		for (int i = 0; i < taggedWords.size(); i++) { 
			pos[i] = taggedWords.get(i).tag(); 
		}
		return pos;
	}
	
	private static ChunkerModel readModel(final String modelPath) {
		assert modelPath != null;
		System.out.println("modelPath: " + modelPath);
		InputStream modelIn = null;
		ChunkerModel model = null;	
		try {
			modelIn = new FileInputStream(modelPath);
			model = new ChunkerModel(modelIn);
		} catch (FileNotFoundException e) {
			logger.warning("File not found: \"" + modelPath + "\".");
		} catch (IOException e) {
			logger.warning("Error while reading model: \"" + CHUNKER_MODEL + "\".");
			//e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {				
					modelIn.close();
				} catch (IOException e) { 
					logger.warning("I/O error.");
				}
			}
		}		
		return model;
	}
	
	private final static String CHUNKER_MODEL = "/home/antonio/workspace/NLP_Tools/apache-opennlp/models/en-chunker.bin";
	
	static {
		ChunkerModel model = readModel(CHUNKER_MODEL);
		if (model != null) {
			chunker = new ChunkerME(model);
		}
		
	}

	private static ChunkerME chunker;
	
	private static Logger logger = Logger.getLogger(Chunker.class.getName());
	
	public static void main(String[] args) {
		System.out.println("modelPath: " + CHUNKER_MODEL);
		//ChunkerModel model = Chunker.readModel(CHUNKER_MODEL);
	}

}
