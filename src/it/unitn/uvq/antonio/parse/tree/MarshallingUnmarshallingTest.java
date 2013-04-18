package it.unitn.uvq.antonio.parse.tree;

import java.io.FileNotFoundException;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.nlp.parse.Node;
import it.unitn.uvq.antonio.nlp.parse.TreeMarshaller;
import it.unitn.uvq.antonio.nlp.parse.TreeUnmarshaller;
import it.unitn.uvq.antonio.nlp.parse.exception.TreeMarshalException;
import it.unitn.uvq.antonio.nlp.parse.exception.TreeUnmarshalException;

public class MarshallingUnmarshallingTest {
	
	public static void main(String[] args) { 
		String str = args[0];
		String pathname = "/home/antonio/Scrivania/test/tree";
		
		Tree<Node> mschlledTree = null;
		Tree<Node> unmschlldTree = null;
		
		
		try {
			mschlledTree = TreeMarshaller.buildAndMarshalTree(str, pathname);
			System.out.println(mschlledTree);
		}catch (TreeMarshalException e) {
			System.err.println("Tree Marshalling error.");
		}
		
		try {
			unmschlldTree = TreeUnmarshaller.umarshal(pathname);
			System.out.println(unmschlldTree);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: \"" + pathname + "\"");
		} catch (TreeUnmarshalException e) {
			System.err.println("Tree Unmarshalling error.");
			e.printStackTrace();
		}
		
		
	}

}
