package com.bupt.Neo4JTest;

import java.sql.Connection;
import java.sql.DriverManager;
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
	
	public void executeQuery() throws SQLException
	{
		System.out.println(nodequery.toString());
		System.out.println(relaquery.toString());
		//写进数据库
		Statement stmt = connect.createStatement();
		System.out.println(stmt.execute(nodequery.toString()+relaquery.toString()));
	}
	
	public void AddRoot(String NodeName,String label) throws SQLException
	{
		StringBuilder query = new StringBuilder();
		query.append("CREATE ("+NodeName+":"+label+" {name: \'"+NodeName+"\'})\n");
		nodequery.append(query.toString());
	}
}
