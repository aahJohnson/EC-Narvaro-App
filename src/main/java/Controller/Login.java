package Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.CourseBean;
import Bean.LektionBean;
import Bean.LoginBean;
import Bean.NarvaroBean;
import Bean.PersonBean;
import ConDB.narvaroDAO;
import ConDB.userDAO;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	userDAO user = new userDAO();

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

		userDAO usDao = new userDAO();

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

	public static int getUserId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");

		String password = request.getParameter("password");

		userDAO useDao = new userDAO();

		LoginBean usersId = useDao.checkLogin(email, password);
		
		request.setAttribute("userId", usersId.getUsers_id());

		return usersId.getUsers_id();

	}
}