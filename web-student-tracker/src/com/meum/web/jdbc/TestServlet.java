package com.meum.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//define Data ressource/connection pool for Ressource Injection
	
	@Resource(name="jdbc/mydatabase")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//set up printwriter
		PrintWriter out= response.getWriter();
		response.setContentType("text/plain");		
		//Get a connection to the database
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			Class.forName("com.mysql.jdbc.Driver"); 
			myConn=dataSource.getConnection();
			
		//create a SQL statement
			String sql="Select * from student";
			myStmt=myConn.createStatement();
		//Execute the statement
		myRs=myStmt.executeQuery(sql);
		//process the result
		while(myRs.next()){
			String email=myRs.getString("email");
			out.println(email);
		}
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
	}

}
