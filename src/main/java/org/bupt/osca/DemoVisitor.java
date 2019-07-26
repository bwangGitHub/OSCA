package com.bupt.Neo4JTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
//import org.eclipse.jdt.internal.compiler.ast.Block;
//import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;

public class DemoVisitor extends ASTVisitor {
	private Neo4jCreate con;

	public DemoVisitor(Neo4jCreate con) {
		this.con = con;
	}
	

	public void preVisit(ASTNode node) {
		String[] temp = node.getClass().toString().replaceAll("\\s*", "").split("\\.");

		String thisnode;
		try {
			thisnode = addNode(node);
			String parentnode = getParName(node);
			if (parentnode != null)
				con.AddRelationship(parentnode, thisnode);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		System.out.println(node.getClass());
	}


	// 这里开始是想previsit调用的方法
	public String addNode(ASTNode node) throws SQLException {
		String[] temp = node.getClass().toString().replaceAll("\\s*", "").split("\\.");
		String nodename = temp[temp.length - 1] + String.valueOf(node.getStartPosition())
				+ String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("Name");
		proval.add(nodename);
		con.AddNode(nodename, temp[temp.length - 1], proname, proval);
		return nodename;
	}

	public String getParName(ASTNode node) {
		ASTNode par = node.getParent();
		if (par == null)
			return null;
		String[] temp = par.getClass().toString().replaceAll("\\s*", "").split("\\.");
		String parname = temp[temp.length - 1] + String.valueOf(par.getStartPosition())
				+ String.valueOf(par.getLength());
		return parname;
	}
}
