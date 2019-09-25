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
		String temp = readFileToString(".\\src\\main\\java\\org\\bupt\\osca\\UsageStatisticsSenderImpl.java");
		astParser.setSource(temp.toCharArray());
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		astParser.setCompilerOptions(options);
//        astParser.setResolveBindings(true);
//        astParser.setEnvironment(null,null,null,true);
//        astParser.setUnitName("example.java");

		// 写入的时候l30 l32 l33 l36都不可以注释！！
//		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
//
//		DemoVisitor visitor = new DemoVisitor(con);
//		result.accept(visitor);
//
//		// 将节点和关系写入
//		con.executeQuery();

		// 只查询不写入
		System.out.println("test");
//		con.findHrdCd("Assignment", "RightHandSide",
//				"~ '(?i)\"*(http.*)|((((1[0-9][0-9].)|(2[0-4][0-9].)|(25[0-5].)|([1-9][0-9].)|([0-9]).)){3})'");
		con.findHrdCd("StringLiteral", "LiteralValue", "~ '(?i).*http.*|(((1[0-9][0-9].)|(2[0-4][0-9].)|(25[0-5].)|([1-9][0-9].)|([0-9]).){4})'");
		System.out.println("over");
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

	public static void main(String args[]) throws SQLException {
		Neo4jCreate con = new Neo4jCreate("http://192.168.37.133:7474/browser/", "neo4j", "nnnnn123");

		try {
			DemoVisitorTest so = new DemoVisitorTest(con);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
