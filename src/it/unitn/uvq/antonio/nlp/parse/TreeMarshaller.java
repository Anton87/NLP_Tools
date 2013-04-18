package it.unitn.uvq.antonio.nlp.parse;

import it.unitn.uvq.antonio.adt.tree.Tree;
import it.unitn.uvq.antonio.nlp.parse.exception.TreeMarshalException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TreeMarshaller {
	
	public static Tree<Node> buildAndMarshalTree(final String str, final String outFile) throws TreeMarshalException {
		assert str != null;
		assert outFile != null;
		Tree<Node> tree = Parser.parse(str);
		marshal(tree, outFile);
		return tree;
	}
	
	public static void marshal(final Tree<Node> tree, final String outFile) throws TreeMarshalException {
		if (tree == null) throw new NullPointerException("tree: null");
		if (outFile == null) throw new NullPointerException("outFile: null");
		List<Node> nodes = getNodes(tree);
		writeNodes(nodes, outFile);
	}
	
	private static List<Node> getNodes(final Tree<Node> tree) { 
		assert tree != null;
		List<Node> nodes = new ArrayList<>();
		for (Tree<Node> subTree : tree.getNodes()) { 
			Node node = subTree.getElem();
			nodes.add(node);
		}
		return nodes;
	}
	
	private static void writeNodes(final List<Node> nodes, final String outFile) throws TreeMarshalException {
		assert nodes != null;
		assert outFile != null;

		deleteDirectoryIfExists(outFile);
		mkdirs(outFile);
		for (Node node : nodes) {
			String filepath =  new File(outFile, node.hashCode() + ".out").toString();
			ObjectOutput out = null;
			try {
				out = new ObjectOutputStream(
						new FileOutputStream(filepath));
				out.writeObject(node);
			} catch (IOException e) {
				throw new TreeMarshalException("Writing file error: \"" + outFile +  "\"", e);
			} finally { 
				try {
					out.close();
				} catch (IOException e) { }
			}
		}
	}
	
	private static void deleteDirectoryIfExists(final String pathname) {
		assert pathname != null;
		if (existsDirectory(pathname)) { 
			deleteDirectory(pathname);
		}				
	}
	
	private static boolean existsDirectory(final String pathname) {
		assert pathname != null;
		return new File(pathname).exists();
	}
	
	private static void deleteDirectory(final String dirpath) {
		assert dirpath != null;
		deleteFiles(dirpath);
	}
	
	private static void deleteFiles(final String dirpath) { 
		assert dirpath != null;
		File dir = new File(dirpath);
		for (File file : dir.listFiles()) { 
			file.delete();
		}
	}
	
	private static boolean mkdirs(final String pathname) {
		assert pathname != null;
		return new File(pathname).mkdirs();
	}
	
	public static void main(String[] args) {
		String str = args[0];		
		String outFile = "/home/antonio/Scrivania/test/tree";
		
		try {
			TreeMarshaller.buildAndMarshalTree(str, outFile);
		} catch (TreeMarshalException e) {
			System.err.println("Tree Marshalling error.");
		}		
	}

}
