package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import conDB.narvaroDAO;

/**
 * Servlet implementation class kurs
 */
@WebServlet("/Kurs")
public class Kurs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Kurs() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		
//		narvaroDAO naDao = new narvaroDAO();
//
//		ArrayList<Bean.kurs> kursList = naDao.kurs();
//		
//		request.setAttribute("kurs", kursList);
//		
//		request.getRequestDispatcher("StudentPage/index.jsp").forward(request, response);

	}

}