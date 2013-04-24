package it.unitn.uvq.antonio.nlp.annotation;

import java.util.List;

public interface Annotator {
	
	List<AnnotationI> annotate(String str);

}
