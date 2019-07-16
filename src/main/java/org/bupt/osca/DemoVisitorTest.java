package org.bupt.osca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
 

 
public class DemoVisitorTest {
	
	public DemoVisitorTest() throws IOException {
		ASTParser astParser = ASTParser.newParser(AST.JLS4);
		String temp=readFileToString("./src/main/java/org/bupt/osca/ClassDemo.java");
        astParser.setSource(temp.toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setEnvironment(null,null,null,true);
        astParser.setUnitName("example.java");
        
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        
//        System.out.println(result);

		DemoVisitor visitor = new DemoVisitor();
		result.accept(visitor);
	}
	
	
	public String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();
		return fileData.toString();
	}
	
	public static void main(String args[])
	{
		try {
			DemoVisitorTest so = new DemoVisitorTest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
