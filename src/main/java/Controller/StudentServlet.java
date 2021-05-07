package Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Bean.CourseBean;
import Bean.LektionBean;
import Bean.LoginBean;
import Bean.NarvaroBean;
import ConDB.narvaroDAO;
import ConDB.userDAO;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudentServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		kurs(request, response);
		lektion(request, response);
		narvaroPerson(request, response);
		showLessonDate(request, response);
		lessonAttendance(request, response);
		getUserId(request, response);
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		kurs(request, response);
		lektion(request, response);
		narvaroPerson(request, response);
		showLessonDate(request, response);
		lessonAttendance(request, response);

		request.getRequestDispatcher("StudentPage/index.jsp").forward(request, response);
	}

	public void kurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<CourseBean> kursList = naDao.kurs();

		Date date = new Date();
		CourseBean aktuell = null;

		for (CourseBean kurs : kursList) {

			if (kurs.getStartDatum().compareTo(date) < 0 && kurs.getSlutDatum().compareTo(date) > 0) {
				aktuell = kurs;
				break;
			}
		}

		if (aktuell != null) {
			request.setAttribute("kurs", aktuell.getKursNamn());
		} else {
			String message = "Ingen aktiv kurs";
			request.setAttribute("kurs", message);
		}

	}

	public static Date lektion(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<LektionBean> lessons = naDao.lektionBeans();

		Date currentDate = new Date();

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

		try {
			currentDate = dateFormatter.parse(dateFormatter.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		LektionBean aktuell = null;

		for (LektionBean lesson : lessons) {

			if (lesson.getDatum().equals(currentDate)) {

				aktuell = lesson;
				request.setAttribute("lektion", aktuell.getDatum());
				break;
			}
		}

		if (aktuell == null) {
			String message = "ingen aktiv lektion";
			request.setAttribute("lektion", message);
		}
		return currentDate;
	}

	public void narvaroPerson(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> andel = naDao.narvaroPerson();

		double totalParticipation = 0;
		double totalMinutes = 0;
		double courseParticipation = 0;
		double courseMinutes = 0;

		Date date = new Date();

		for (NarvaroBean narvaroBean : andel) { // Total närvaro

			if (narvaroBean.getPerson().getPersonId() == getUserId(request, response)
					&& narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

				totalParticipation =+ narvaroBean.getAndel();
				totalMinutes =+ narvaroBean.getLektion().getMinuter();

			}
		}

		double totalAttention = totalParticipation / totalMinutes * 100;
		int totalAttentionPercentage = (int) totalAttention; // <-- Convert double to int
		request.setAttribute("narvaro", totalAttentionPercentage);

		for (NarvaroBean narvaroBean : andel) { // Närvaro för kurs

			if (narvaroBean.getPerson().getPersonId() == getUserId(request, response)
					&& narvaroBean.getLektion().getKursId() == currentCourse(request, response)
					&& narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

				courseParticipation = courseParticipation + narvaroBean.getAndel();
				courseMinutes = courseMinutes + narvaroBean.getLektion().getMinuter();

			}
		}

		double courseAttention = courseParticipation / courseMinutes * 100;
		int totalCoursePercentage = (int) courseAttention; // <-- Convert double to int

		request.setAttribute("course", totalCoursePercentage);

	}

	public void lessonAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> andel = naDao.narvaroPerson();

		double lessonMinutes = 0;
		double attendanceMinutes = 0;

		ArrayList<Integer> filteredLesson = new ArrayList<Integer>();

		for (NarvaroBean narvaroBean : andel) { // Närvaro per lektion

			if (narvaroBean.getPerson().getPersonId() == getUserId(request, response)
					&& narvaroBean.getLektion().getKursId() == currentCourse(request, response)) {

				lessonMinutes = narvaroBean.getLektion().getMinuter();
				attendanceMinutes = narvaroBean.getAndel();
				double attendancePercentage = attendanceMinutes / lessonMinutes * 100;
				int finalAttendancePercentage = (int) attendancePercentage;
				filteredLesson.add(finalAttendancePercentage);
			}
		}
		request.setAttribute("lessonAttendance", filteredLesson);
	}

	public void showLessonDate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> lessonDateList = naDao.narvaroPerson();

		ArrayList<Date> filteredDate = new ArrayList<Date>();

		for (NarvaroBean lessonDate : lessonDateList) {

			if (lessonDate.getPerson().getPersonId() == getUserId(request, response)) {

				filteredDate.add(lessonDate.getLektion().getDatum());
			}
		}
		request.setAttribute("dateLesson", filteredDate);
		// System.out.println(filteredDate);
	}

	public static int getUserId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		userDAO useDao = new userDAO();
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		LoginBean usersId = useDao.checkLogin(email, password);

		return usersId.getUsers_id();
	}

	public int currentCourse(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		narvaroDAO naDao = new narvaroDAO();

		ArrayList<CourseBean> kursList = naDao.kurs();

		Date date = new Date();
		CourseBean aktuell = null;

		for (CourseBean kurs : kursList) {

			if (kurs.getStartDatum().compareTo(date) < 0 && kurs.getSlutDatum().compareTo(date) > 0) {
				aktuell = kurs;
				request.setAttribute("currentCourse", aktuell.getKursId());
				break;
			}
		}

		if (aktuell == null) {
			String message = "Ingen aktiv kurs";
			request.setAttribute("kurs", message);
		}
		return aktuell.getKursId();
	}

	public static void registerAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO narvaroDaoInstance = new narvaroDAO();

		ArrayList<NarvaroBean> narvaroMethod = narvaroDaoInstance.narvaroPerson();

		for (NarvaroBean lessonMinutes : narvaroMethod) {

			if (lessonMinutes.getLektion().getDatum().equals(lektion(request, response))
					&& lessonMinutes.getPerson().getPersonId() == 2) {

				int attendingMinutes = lessonMinutes.getLektion().getMinuter();

				System.out.println(attendingMinutes);
				break;
			}
		}
	}
}