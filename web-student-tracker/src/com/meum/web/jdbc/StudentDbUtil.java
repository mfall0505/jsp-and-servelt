package com.meum.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource thedataSource){
		dataSource=thedataSource;
		
	}
	public List<Student> getStudents()throws Exception
	{
		List<Student> students=new ArrayList<>();
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		 //get Connection
		try{
		myConn=dataSource.getConnection();
		
		//create sql statement
		String sql="Select * from student order by last_name";
		myStmt=myConn.createStatement();
		
		//execute the query
		myRs=myStmt.executeQuery(sql);
		
		//return the result
		while (myRs.next()){
			//retrieve the row
			int id=myRs.getInt("id");
			String firstName=myRs.getString("first_name");
			String lastName=myRs.getString("last_name");
			String email=myRs.getString("email");
			
			//create a  new student object
			Student tempStudent=new Student(id,firstName,lastName,email);			
			
			//add it to our list of student
			students.add(tempStudent);
		}
		return students;
		}
		finally{
			
			//close the jdbc objetc
			close(myConn,myStmt,myRs);
			
		}
	}
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try{
			if(myConn!=null)
				myConn.close();
			if (myStmt!=null)
				myStmt.close();
			if (myRs!=null)
				myRs.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	public  void addStudent(Student theStudent)throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt=null;
		
		try{
			//get connection
			myConn=dataSource.getConnection();
			
			//create a sql statement
			String  sql="Insert into student"
					+"(first_name, last_name, email) "
					+"values (?,?,?)";
			myStmt=myConn.prepareStatement(sql);
			
			//set the parm for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			//execute the query
			myStmt.executeUpdate();
			
			}
		finally{
			close(myConn,myStmt,null);
		}
		
		
	}
	public Student getStudents (String theStudentId)throws Exception {
		Student theStudent=null;
		Connection myConn = null;
		PreparedStatement myStmt=null;
		ResultSet myRs=null;
		int studentId;
		
		try{
			//convert student id to int
			studentId=Integer.parseInt(theStudentId);
			//get connection to the database
			myConn=dataSource.getConnection();
			//get the sql statement
			String sql="Select * from student where id=?";
			//create preparedStatement
			myStmt=myConn.prepareStatement(sql);
			//set params
			myStmt.setInt(1, studentId);
			//execute statement
			myRs=myStmt.executeQuery();
			//retrieve data
			if(myRs.next()){
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				theStudent=new Student(studentId,firstName, lastName, email);
			}
			else{
				throw new Exception("couldn't find student id:"+studentId);
			}
			
			
				return theStudent;
		}
		finally{
			close(myConn,myStmt,myRs);
			
		}
	}
	public  void updateStudent(Student theStudent)
	throws Exception{
		
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		try{
		//db connection
		myConn=dataSource.getConnection();
		
		//create the query
		String sql="update student "
				+"set first_name=?, last_name=?, email=? "
				+"where id=?";
		//prepared Statement
		myStmt=myConn.prepareStatement(sql);
		
		//set the parm for the student
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		//execute
		myStmt.execute();
		}
		finally
		{
			//clean up code
			close(myConn,myStmt,null);
		}
	}
	public void deleteStudent(String theStudentId) throws Exception {
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try{
			//convert studentid to int
			int studentId=Integer.parseInt(theStudentId);			
			//connection to the database
			myConn=dataSource.getConnection();
			
			//Create the query to delete students
			String sql="delete from student where id=?";
			//prepared statement
			myStmt=myConn.prepareStatement(sql);
			
			//set
			myStmt.setInt(1, studentId);
			//execute the query
			myStmt.execute();
			
		}
		finally{
			//clean up jdbc
			close(myConn,myStmt,null);
			
		}
		
	}

}
