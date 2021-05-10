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

		registerAttendance(request, response);
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

				request.setAttribute("kurs", aktuell.getKursNamn());
				break;
			}
		}

		if (aktuell == null) {
			String message = "Ingen aktiv kurs";
			request.setAttribute("kurs", message);
		}

	}

	public Date lektion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<LektionBean> lessons = naDao.lektionBeans();

		Date currentDate = new Date();

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy");

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

		Date date = new Date();

		for (NarvaroBean narvaroBean : andel) {// totala närvaro

			if (narvaroBean.getPerson().getPersonId() == Login.getUserId(request, response)) {

				if (narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

					totalParticipation = totalParticipation + narvaroBean.getAndel();
					totalMinutes = totalMinutes + narvaroBean.getLektion().getMinuter();
				}
			}
		}

		double totalAttention = totalParticipation / totalMinutes * 100;
		int totalAttentionProcentage = (int) totalAttention;

		request.setAttribute("narvaro", totalAttentionProcentage);

		for (NarvaroBean narvaroBean : andel) {// totala Kurs närvaro

			if (narvaroBean.getPerson().getPersonId() == Login.getUserId(request, response)) {

				if (narvaroBean.getLektion().getKursId() == currentCourse(request, response)) {

					if (narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

						totalParticipation = totalParticipation + narvaroBean.getAndel();
						totalMinutes = totalMinutes + narvaroBean.getLektion().getMinuter();
					}
				}
			}
		}

		double courseAttention = totalParticipation / totalMinutes * 100;
		int totalCourseProcentage = (int) courseAttention;

		request.setAttribute("course", totalCourseProcentage);
	}

	public void lessonAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> andel = naDao.narvaroPerson();

		ArrayList<Integer> filteredLesson = new ArrayList<Integer>();

		double lessonMinutes = 0;
		double attendanceMinutes = 0;

		for (NarvaroBean narvaroBean : andel) {

			if (narvaroBean.getPerson().getPersonId() == Login.getUserId(request, response)
					&& narvaroBean.getLektion().getKursId() == currentCourse(request, response)) {

				lessonMinutes = +narvaroBean.getLektion().getMinuter();
				attendanceMinutes = +narvaroBean.getAndel();
				double attendanceProcentage = attendanceMinutes / lessonMinutes * 100;

				int finalAttendancePercentage = (int) attendanceProcentage;

				filteredLesson.add(finalAttendancePercentage);
			}
		}

		request.setAttribute("lessonAttendance", filteredLesson);
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

	public void showLessonDate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> lessonDateList = naDao.narvaroPerson();

		ArrayList<NarvaroBean> filteredDate = new ArrayList<NarvaroBean>();

		for (NarvaroBean lessonDate : lessonDateList) {

			if (lessonDate.getPerson().getPersonId() == Login.getUserId(request, response)) {

				filteredDate.add(lessonDate);
			}
		}

		request.setAttribute("dateLesson", filteredDate);
	}

	public void registerAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String attendingValue = request.getParameter("attendingValue");

		narvaroDAO narvaroDaoInstance = new narvaroDAO();

		ArrayList<NarvaroBean> narvaroMethod = narvaroDaoInstance.narvaroPerson();
		int attendance = 0;

		System.out.println("registerAttendance");
		System.out.println(attendingValue);
		
		for (NarvaroBean lessonMinutes : narvaroMethod) {

			if (lessonMinutes.getLektion().getDatum().equals(lektion(request, response))) {
				if (lessonMinutes.getPerson().getPersonId() == Login.getUserId(request, response)) {

					System.out.println("loop 1");
					
					if (attendingValue == "1") {

						attendance = lessonMinutes.getLektion().getMinuter();

					} else if (attendingValue == "2") {
						attendance = 0;
					}
				}
			}
			narvaroDaoInstance.updateAttendance(lessonMinutes.getPerson().getPersonId(),
					lessonMinutes.getLektion().getLekId(), attendance);
		}
	}
}