package Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Buttons")
public class Buttons extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public Buttons() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		StudentServlet student = new StudentServlet();
		student.kurs(request, response);
		student.lektion(request, response);
		//student.lessonAttendance(request, response);
		
		//student.registerAttendance(request, response);
		
		request.getRequestDispatcher("StudentPage/index.jsp").forward(request, response);

	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}