package com.meum.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDbUtil studentDbUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		//create our student db util
		try{
			studentDbUtil=new StudentDbUtil(dataSource);
		}
		catch(Exception exc)
		{
			throw new ServletException(exc);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//read the command
			String theCommand=request.getParameter("command");
			//if the command is missing list default student
			if(theCommand==null){
				theCommand="LIST";
			}
			
			//reroute to the right code
			switch(theCommand){
			case "LIST":
				listStudents(request,response);
				break;
				
			case "ADD":
				addStudent(request,response);
				break;
			
			case "LOAD":
				loadStudent(request,response);
				break;
			
			case "DELETE":
				deleteStudent(request,response);
				break;
			
			case"UPDATE":
				updateStudent(request,response);
				break;
				
			default:
				listStudents(request,response);
			}
			listStudents(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
	throws Exception{
		 //read the student id from the form
		String theStudentId=request.getParameter("studentId");
		
		//delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		//send back to page
		listStudents(request,response);
		
		
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response)
	 throws Exception {
		//read student info from form
		int id=Integer.parseInt(request.getParameter("studentId"));
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		//create a new student object
		Student theStudent=new Student(id,firstName,lastName,email);
		studentDbUtil.updateStudent(theStudent);
		//perform update on database
		listStudents(request,response);
		
		//send it back to student list
		
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) 
	 throws Exception{
		//read the student id from form
		String theStudentId=request.getParameter("studentId");
		//get student from dbutil
		Student theStudent=studentDbUtil.getStudents(theStudentId);
		//place student in request attribute
		request.setAttribute("THE_STUDENT",theStudent);
		//send to jsp page
		RequestDispatcher dispatcher=
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read the student from the form
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		//create a new student object
		Student theStudent=new Student(firstName,lastName,email);
		//add student to database
		studentDbUtil.addStudent(theStudent);
		//back to main page
		listStudents(request,response);
		
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		//get students from db util
		List<Student> students=studentDbUtil.getStudents();
		
		//add the students to the request
		request.setAttribute("STUDENT_LIST", students);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}
