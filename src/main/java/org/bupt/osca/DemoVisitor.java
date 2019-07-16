package org.bupt.osca;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
//import org.eclipse.jdt.internal.compiler.ast.Block;
//import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.Statement;
 
public class DemoVisitor extends ASTVisitor {
	
//	@Override
//	public void preVisit(ASTNode node) {
//		System.out.println("---");
//		System.out.println("aaa: "+node);
//		System.out.println("bbb: "+node.getParent());
//		System.out.println("---");
//	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		
		System.out.println("--------");
		
		for (Object obj: node.fragments()) {
			VariableDeclarationFragment v = (VariableDeclarationFragment)obj;
			System.out.println("Parent: "+v.getParent());
			System.out.println("Field:\t" + v.getName());
//			System.out.println("Field fragment:"+v.fragments());
//			System.out.println("Field type:"+v.getType());
		}
		
		for (Iterator iter = node.fragments().iterator(); iter.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();

			IVariableBinding binding = fragment.resolveBinding();
			
//			System.out.println("---");
//			System.out.println("Key: "+binding.getKey());
//			System.out.println("ConstantValue: "+binding.getConstantValue());
//			System.out.println("VariableDeclaration: "+binding.getVariableDeclaration());
//			System.out.println("Name: "+binding.getName());
//			System.out.println("Type: "+binding.getType());
//			System.out.println(binding.toString());
//			System.out.println("---");
//			VariableBindingManager manager = new VariableBindingManager(fragment);
//			localVariableManagers.put(binding, manager);
		}
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
//		node.
		System.out.println("Method:\t" + node.getName());
			//get method parameters
		System.out.println("Mparent : "+node.getParent());
		System.out.println("method parameters:"+node.parameters());
		System.out.println("node name property: "+ node.getProperty("temp"));
		
//		System.out.println("aa: "+ node.getStructuralProperty());
			//get method return type
		System.out.println("method return type:"+node.getReturnType2());
		Block methodBody = node.getBody();
//		System.out.println("method body: "+methodBody);
//        List<Statement> statementList = methodBody.statements();
//        if(methodBody.statements()!=null) {
//		System.out.println(methodBody.statements());}
//        System.out.println(statementList.toString());

//        statementList.get(0);

//        ExpressionStatement ifs = (ExpressionStatement) node.getBody().statements().get(1);
//        Assignment expression = (Assignment) ifs.getExpression();
//        Expression exp = expression.getRightHandSide();
//        List<Statement> statementList = methodBody.statements();

//        System.out.println(statementList.toString());
		
		return true;
	}
 
	@Override
	public boolean visit(TypeDeclaration node) {
		
		System.out.println("Class:\t" + node.getName());
		return true;
	}
	
	@Override
	public boolean visit(CompilationUnit node) {
		System.out.println("CompilationUnit:\t "+node.getAST());
		return true;
	}
}
