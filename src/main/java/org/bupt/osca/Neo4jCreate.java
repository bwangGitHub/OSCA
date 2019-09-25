package org.bupt.osca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Neo4jCreate {
	private Connection connect;
	private StringBuilder nodequery= new StringBuilder();
	private StringBuilder relaquery= new StringBuilder();
	public Neo4jCreate(String url,String username,String password) throws SQLException {
		//url=http://192.168.37.130:7474/browser/
		//username=neo4j
		//password=nnnnn123
		//Connection con = DriverManager.getConnection("jdbc:neo4j:http://192.168.37.130:7474/browser/", "neo4j", "nnnnn123"); 
		Connection con = DriverManager.getConnection("jdbc:neo4j:"+url,username,password);
		connect = con;
	}
	
	//return nodename
	public String AddNode(String nodename,String nodetype,ArrayList<String> proname,ArrayList<String> proval) throws SQLException {
//		try (Statement stmt = connect.createStatement()) {
			StringBuilder query = new StringBuilder();
			//String tname = nodename.replaceAll("\\s*","");
			//String ttype = nodetype.replaceAll("\\s*", "");
			String tname = RmSpace(nodename);
			String ttype = RmSpace(nodetype);
			query.append("CREATE ("+tname+":"+ttype+" {");
			for(int i=0;i<proname.size();i++)
			{
				query.append(proname.get(i)+":"+"\'"+proval.get(i)+"\'");
				if(i!=proname.size()-1)
					query.append(", ");
			}
			query.append("})\n");
			nodequery.append(query.toString());
		return  "ok";
	}
	
	public String RmSpace(String stmt)
	{
		String temp = stmt.replaceAll("\\s*", "");
		return temp;
	}
	
	public void AddRelationship(String parent,String son) throws SQLException
	{
		StringBuilder query = new StringBuilder();
		query.append("CREATE (");
		query.append(RmSpace(parent));
		query.append(")-[:include]->(");
		query.append(RmSpace(son));
		query.append(")\n");
		relaquery.append(query.toString());
	}
	
	public void executeQuery() throws SQLException, IOException
	{
		System.out.println(nodequery.toString());
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(".\\output.txt"));
		out.write(nodequery.toString());
//		System.out.println(relaquery.toString());
		//neo4j data push
		Statement stmt = connect.createStatement();
		System.out.println(stmt.execute(nodequery.toString()+relaquery.toString()));
	}
	
	public void AddRoot(String NodeName,String label) throws SQLException
	{
		StringBuilder query = new StringBuilder();
		query.append("CREATE ("+NodeName+":"+label+" {name: \'"+NodeName+"\'})\n");
		nodequery.append(query.toString());
	}
	
	//ÕÒµ½Ó²±àÂëº¯Êý
	public void findHrdCd(String lable,String var,String regrix) throws SQLException
	{
		Statement stmt = connect.createStatement();
		StringBuilder query = new StringBuilder();
		query.append("MATCH (temp:");
		query.append(lable);
		query.append(") WHERE temp.");
		query.append(var);
		query.append("=");
		query.append(regrix);
		query.append(" RETURN temp.");
		query.append(var);
		ResultSet rs = stmt.executeQuery(query.toString());
		System.out.println(query.toString());
		while(rs.next())  
        {  
			System.out.println(rs.getString("temp."+var));
        }  
	}
}
