package org.bupt.osca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
 
 
public class DemoVisitorTest {
	
	public DemoVisitorTest(Neo4jCreate con) throws IOException, SQLException {
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		String temp=readFileToString(".\\src\\main\\java\\org\\bupt\\osca\\ClassDemo.java");
        astParser.setSource(temp.toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);
//        astParser.setResolveBindings(true);
//        astParser.setEnvironment(null,null,null,true);
//        astParser.setUnitName("example.java");
        
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        
       	DemoVisitor visitor = new DemoVisitor(con);
		result.accept(visitor);
		
		con.executeQuery();
		
		
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
	
	public static void main(String args[]) throws SQLException
	{
		Neo4jCreate con = new Neo4jCreate("http://192.168.37.131:7474/browser/", "neo4j", "nnnnn123");

		try {
			DemoVisitorTest so = new DemoVisitorTest(con);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
