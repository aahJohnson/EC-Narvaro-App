package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.LoginBean;
import conDB.UserDAO;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO user = new UserDAO();

	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserDAO usDao = new UserDAO();

		LoginBean login = usDao.checkLogin(email, password);

		String destPage = "login.jsp";

		if (login != null) {
			if (login.getUserType().equals("Student")) {
				HttpSession session = request.getSession();
				session.setAttribute("user", login);
				

				destPage = "/StudentServlet";
			} else if (login.getUserType().equals("Utbildningsledare")) {
				HttpSession session = request.getSession();
				session.setAttribute("user", login);
				
				destPage = "AdminPage/index.jsp";
			} else if (login.getUserType().equals("Lärare")) {
				HttpSession session = request.getSession();
				session.setAttribute("user", login);
				destPage = "AdminPage/index.jsp";
			}

		} else {
			String message = "Invalid email/password";
			request.setAttribute("message", message);
		}

		request.getRequestDispatcher(destPage).forward(request, response);

	}
}