package org.bupt.osca;

import java.awt.Dimension;
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
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
//import org.eclipse.jdt.internal.compiler.ast.Block;
//import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Expression;

public class DemoVisitor extends ASTVisitor {
	private Neo4jCreate con;

	public DemoVisitor(Neo4jCreate con) {
		this.con = con;
	}

	public void preVisit(ASTNode node) {
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
	}

//旧的
//	public String addNode(ASTNode node) throws SQLException {
//		String[] temp = node.getClass().toString().replaceAll("\\s*", "").split("\\.");
//		String nodename = temp[temp.length - 1] + String.valueOf(node.getStartPosition())
//				+ String.valueOf(node.getLength());
//		
//		
//		
//		ArrayList<String> proname = new ArrayList<String>();
//		ArrayList<String> proval = new ArrayList<String>();
//		proname.add("Name");
//		proval.add(nodename);
//		con.AddNode(nodename, temp[temp.length - 1], proname, proval);
//		return nodename;
//	}

	public String addNode(ASTNode node) throws SQLException {
		String[] temp = node.getClass().toString().replaceAll("\\s*", "").split("\\.");
		String nodename;
		System.out.println(temp[temp.length - 1]);
		switch (temp[temp.length - 1]) {
		case "CompilationUnit":
			nodename = addCompilationUnit((CompilationUnit) node);
			break;
		case "PackageDeclaration":
			nodename = addPackageDeclaration((PackageDeclaration) node);
			break;
		case "QualifiedName":
			nodename = addQualifiedName((QualifiedName) node);
			break;
		case "SimpleName":
			nodename = addSimpleName((SimpleName) node);
			break;
		case "SimpleType":
			nodename = addSimpleType((SimpleType) node);
			break;
		case "SingleVariableDeclaration":
			nodename = addSingleVariableDeclaration((SingleVariableDeclaration) node);
			break;
		case "TypeDeclaration":
			nodename = addTypeDeclaration((TypeDeclaration) node);
			break;
		case "Modifier":
			nodename = addModifier((Modifier) node);
			break;
		case "FieldDeclaration":
			nodename = addFieldDeclaration((FieldDeclaration) node);
			break;
		case "MethodDeclaration":
			nodename = addMethodDeclaration((MethodDeclaration) node);
			break;
		case "VariableDeclarationFragment":
			nodename = addVariableDeclarationFragment((VariableDeclarationFragment) node);
			break;
		case "StringLiteral":
			nodename = addStringLiteral((StringLiteral) node);
			break;
		case "NumberLiteral":
			nodename = addNumberLiteral((NumberLiteral) node);
			break;
		case "PrimitiveType":
			nodename = addPrimitiveType((PrimitiveType) node);
			break;
		case "Block":
			nodename = addBlock((Block) node);
			break;
		case "ExpressionStatement":
			nodename = addExpressionStatement((ExpressionStatement) node);
			break;
		case "WhileStatement":
			nodename = addWhileStatement((WhileStatement) node);
			break;
		case "InfixExpression":
			nodename = addInfixExpression((InfixExpression) node);
			break;
		case "Assignment":
			nodename = addAssignment((Assignment) node);
			break;
		case "ContinueStatement":
			nodename = addContinueStatement((ContinueStatement) node);
			break;
		// 写到这里了！！
		case "MethodInvocation":
			nodename = addMethodInvocation((MethodInvocation) node);
			break;
		case "ReturnStatement":
			nodename = addReturnStatement((ReturnStatement) node);
			break;
		default:
			nodename = addOtherNode(node);
			break;
		}
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

	public String addCompilationUnit(CompilationUnit node) throws SQLException {
		String label = "CompilationUnit";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);

//		proname.add("StatementsRecoveryData");
//		proval.add(node.getStatementsRecoveryData().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addPackageDeclaration(PackageDeclaration node) throws SQLException {
		String label = "PackageDeclaration";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addQualifiedName(QualifiedName node) throws SQLException {
		String label = "QualifiedName";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		proname.add("getQualifier");
		proval.add(node.getQualifier().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addSimpleName(SimpleName node) throws SQLException {
		String label = "SimpleName";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("FullyQualifiedName");
		proval.add(node.getFullyQualifiedName());
		proname.add("Identifier");
		proval.add(node.getIdentifier());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addSimpleType(SimpleType node) throws SQLException {
		String label = "SimpleType";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addSingleVariableDeclaration(SingleVariableDeclaration node) throws SQLException {
		String label = "SingleVariableDeclaration";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		// null pointer
//		proname.add("Initializer");
//		proval.add(node.getInitializer().toString());
//		proname.add("Type");
//		proval.add(node.getType().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addTypeDeclaration(TypeDeclaration node) throws SQLException {
		String label = "TypeDeclaration";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addFieldDeclaration(FieldDeclaration node) throws SQLException {
		String label = "FieldDeclaration";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Type");
		proval.add(node.getType().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addMethodDeclaration(MethodDeclaration node) throws SQLException {
		String label = "MethodDeclaration";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		// 空指针处理的问题
		if (node.getReceiverQualifier() != null) {
			proname.add("getReceiverQualifier");
			proval.add(node.getReceiverQualifier().toString());
		}
		if (node.getReceiverType() != null) {
			proname.add("getReceiverType");
			proval.add(node.getReceiverType().toString());
		}
		if (node.getReturnType2() != null) {
			proname.add("getgetReturnType2");
			proval.add(node.getReturnType2().toString());
		}

		con.AddNode(nodename, label, proname, proval);
		return nodename;

	}

	public String addModifier(Modifier node) throws SQLException {
		String label = "Modifier";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Keyword");
		proval.add(node.getKeyword().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addVariableDeclarationFragment(VariableDeclarationFragment node) throws SQLException {
		String label = "VariableDeclarationFragment";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		if (node.getInitializer() != null) {
			proname.add("Initializer");
			proval.add(node.getInitializer().toString());
		}
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addStringLiteral(StringLiteral node) throws SQLException {
		String label = "StringLiteral";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("EscapedValue");
		proval.add(node.getEscapedValue());
		proname.add("LiteralValue");
		proval.add(node.getLiteralValue().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addNumberLiteral(NumberLiteral node) throws SQLException {
		String label = "NumberLiteral";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Token");
		proval.add(node.getToken());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addPrimitiveType(PrimitiveType node) throws SQLException {
		String label = "PrimitiveType";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("PrimitiveTypeCode");
		proval.add(node.getPrimitiveTypeCode().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addBlock(Block node) throws SQLException {
		String label = "Block";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addExpressionStatement(ExpressionStatement node) throws SQLException {
		String label = "ExpressionStatement";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Expression");
		
		proval.add(node.getExpression().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addWhileStatement(WhileStatement node) throws SQLException {
		String label = "WhileStatement";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Expression");
		proval.add(node.getExpression().toString());
		proname.add("Body");
		proval.add(node.getBody().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addInfixExpression(InfixExpression node) throws SQLException {
		String label = "InfixExpression";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);

		proname.add("LeftOperand");
		proval.add(node.getLeftOperand().toString());
		proname.add("Operator");
		proval.add(node.getOperator().toString());
		proname.add("RightOperand");
		proval.add(node.getRightOperand().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addContinueStatement(ContinueStatement node) {
		String label = "ContinueStatement";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);

		return nodename;
	}

	public String addReturnStatement(ReturnStatement node) {
		String label = "ReturnStatement";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Expression");
		if (node.getExpression() != null)
			proval.add(node.getExpression().toString());
		return nodename;
	}

	public String addAssignment(Assignment node) throws SQLException {
		String label = "Assignment";
		String nodename = label + String.valueOf(node.getStartPosition()) + String.valueOf(node.getLength());
		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("LeftHandSide");
		proval.add(node.getLeftHandSide().toString());
		proname.add("Operator");
		proval.add(node.getOperator().toString());
		proname.add("RightHandSide");
		proval.add(node.getRightHandSide().toString());
		con.AddNode(nodename, label, proname, proval);
		return nodename;
	}

	public String addMethodInvocation(MethodInvocation node) throws SQLException {
		String[] temp = node.getClass().toString().replaceAll("\\s*", "").split("\\.");
		String nodename = temp[temp.length - 1] + String.valueOf(node.getStartPosition())
				+ String.valueOf(node.getLength());

		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		proname.add("Name");
		proval.add(node.getName().toString());
		if (node.getExpression() != null) {
			proname.add("Expression");
			proval.add(node.getExpression().toString());
		}
		con.AddNode(nodename, temp[temp.length - 1], proname, proval);
		return nodename;
	}

	public String addOtherNode(ASTNode node) throws SQLException {
		String[] temp = node.getClass().toString().replaceAll("\\s*", "").split("\\.");
		String nodename = temp[temp.length - 1] + String.valueOf(node.getStartPosition())
				+ String.valueOf(node.getLength());

		ArrayList<String> proname = new ArrayList<String>();
		ArrayList<String> proval = new ArrayList<String>();
		proname.add("NodeName");
		proval.add(nodename);
		con.AddNode(nodename, temp[temp.length - 1], proname, proval);
		return nodename;
	}
	
	public String[] fExpression(Expression ex)
	{
		String temp = ex.toString();
		
		if(temp.contains("="))
		{
			String exp[]=new String[2];
			int j = temp.indexOf("=");
			exp[0] = new String(temp.substring(0,j));
			exp[1] = new String(temp.substring(j+1,temp.length()));
			return exp;
		}
		else 
			return null;
			
	}

}
