package controller;

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
import Bean.LessonBean;
import Bean.LoginBean;
import Bean.NarvaroBean;
import conDB.narvaroDAO;
import conDB.userDAO;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudentServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("attentionPercentage") != null) {
			setAttendance(request, response);
		} else if (request.getParameter("activeLessonId") != null) {
			printCurrentLesson(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// currentLesson();
		//printCurrentLesson(request, response);
		printCurrentCourseId(request, response);
		courseAttendance(request, response);
		showLessonDate(request, response);
		lessonAttendance(request, response);
		// changeLessonDate(request, response);

		request.getRequestDispatcher("StudentPage/index.jsp").forward(request, response);

	}

	// Course
	
	public CourseBean getCurrentCourse() throws ServletException, IOException {

		CourseBean course = null;

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<CourseBean> kursList = naDao.courses();

		Date date = new Date();

		for (CourseBean kurs : kursList) {

			if (kurs.getStartDatum().compareTo(date) < 0 && kurs.getSlutDatum().compareTo(date) > 0) {
				course = kurs;
				break;
			}
		}

		return course;
	}

	public void courseAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		double totalAttention = calcTotalParticipation(getUserId(request, response)) * 100;

		request.setAttribute("narvaro", (int) totalAttention);

		CourseBean currentCourse = getCurrentCourse();

		if (currentCourse != null) {
			double courseAttention = calcTotalParticipationCourse(getUserId(request, response),
					currentCourse.getKursId()) * 100;

			request.setAttribute("courseName", currentCourse.getKursNamn());
			request.setAttribute("course", (int) courseAttention);
		} else {
			String message = "Ingen aktiv kurs";
			request.setAttribute("courseName", message);
		}
	}
	
	public double calcTotalParticipationCourse(int userId, int courseId) throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> andel = naDao.narvaroPerson();

		Date date = new Date();

		double totalParticipation = 0.0;
		double totalMinutes = 0.0;

		for (NarvaroBean narvaroBean : andel) {// totala Kurs närvaro

			if (narvaroBean.getPerson().getPersonId() == userId) {

				if (narvaroBean.getLektion().getKursId() == courseId) {

					if (narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

						totalParticipation = totalParticipation + narvaroBean.getAndel();
						totalMinutes = totalMinutes + narvaroBean.getLektion().getMinuter();

					}
				}

			}

		}
		return totalParticipation / totalMinutes;

	}
	
	public int currentCourseId() throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<CourseBean> kursList = naDao.courses();

		Date date = new Date();
		CourseBean aktuell = null;

		for (CourseBean kurs : kursList) {

			if (kurs.getStartDatum().compareTo(date) < 0 && kurs.getSlutDatum().compareTo(date) > 0) {
				aktuell = kurs;
				break;
			}
		}

		return aktuell.getKursId();

	}

	public void printCurrentCourseId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer printCourseId = currentCourseId();

		if (printCourseId != null) {
			request.setAttribute("currentCourse", printCourseId);
		} else {
			String message = "Ingen aktiv kurs";
			request.setAttribute("currentCourse", message);
		}

	}
	
	// Lesson
	
	public Date currentLesson() throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<LessonBean> lessons = naDao.lessions();

		Date currentDate = new Date();

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy");

		try {
			currentDate = dateFormatter.parse(dateFormatter.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date aktuell = null;

		for (LessonBean lesson : lessons) {

			if (lesson.getDatum().equals(currentDate)) {

				aktuell = lesson.getDatum();
				break;
			}

		}

		return aktuell;

	}

	public void printCurrentLesson(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("activeLessonId") != null) {
			Date printLesson = changeLessonDate(request, response);
			System.out.println("lesson id is " + printLesson);
			
			request.setAttribute("lessonDate", printLesson);

//			if (printLesson != null) {
//				request.setAttribute("lektion", "abcde");
//			} else {
//				String message = "Ingen aktiv lektion";
//				request.setAttribute("lektion", message);
//			}
		}
	}

	public void lessonAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> andel = naDao.narvaroPerson();

		ArrayList<Integer> filteredLesson = new ArrayList<Integer>();

		double lessonMinutes = 0;
		double attendanceMinutes = 0;

		for (NarvaroBean narvaroBean : andel) {

			if (narvaroBean.getPerson().getPersonId() == getUserId(request, response)
					&& narvaroBean.getLektion().getKursId() == currentCourseId()) {

				lessonMinutes = +narvaroBean.getLektion().getMinuter();
				attendanceMinutes = +narvaroBean.getAndel();
				double attendanceProcentage = attendanceMinutes / lessonMinutes * 100;

				int finalAttendancePercentage = (int) attendanceProcentage;

				filteredLesson.add(finalAttendancePercentage);

			}

		}

		request.setAttribute("lessonAttendance", filteredLesson);

	}
	
	public void showLessonDate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> lessonDateList = naDao.narvaroPerson();

		ArrayList<NarvaroBean> filteredDate = new ArrayList<NarvaroBean>();

		for (NarvaroBean lessonDate : lessonDateList) {

			if (lessonDate.getPerson().getPersonId() == getUserId(request, response)) {

				filteredDate.add(lessonDate);

			}

		}

		request.setAttribute("dateLesson", filteredDate);
	}
	
	public Date changeLessonDate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy");
//
//		try {
//			lessonDate = dateFormatter.parse(dateFormatter.format(new Date()));
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//		}

		Date lessonDate = new Date();

		String idActive = request.getParameter("activeLessonId");

		narvaroDAO narvaroDaoInstance = new narvaroDAO();

		ArrayList<NarvaroBean> allLessons = narvaroDaoInstance.narvaroPerson();

		for (NarvaroBean lesson : allLessons) {

			if (lesson.getLektion().getLekId() == Integer.parseInt(idActive)) {

				lessonDate = lesson.getLektion().getDatum();
				System.out.println("lessonDate: " + lessonDate);
				break;
			}
		}

		return lessonDate;
	}
	
	// Total attendance
	
	public double calcTotalParticipation(int userId) {

		narvaroDAO naDao = new narvaroDAO();

		ArrayList<NarvaroBean> andel = naDao.narvaroPerson();

		double totalParticipation = 0.0;
		double totalMinutes = 0.0;

		Date date = new Date();

		for (NarvaroBean narvaroBean : andel) {// totala närvaro

			if (narvaroBean.getPerson().getPersonId() == userId) {

				if (narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

					totalParticipation = totalParticipation + narvaroBean.getAndel();
					totalMinutes = totalMinutes + narvaroBean.getLektion().getMinuter();
				}
			}
		}

		return totalParticipation / totalMinutes;

	}

	public int getUserId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");

		String password = request.getParameter("password");

		userDAO useDao = new userDAO();

		LoginBean usersId = useDao.checkLogin(email, password);

		request.setAttribute("userId", usersId.getUsers_id());

		return usersId.getUsers_id();

	}

	


	public void setAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("userIdNumber");
		String attentionPercentage = request.getParameter("attentionPercentage");

		int attentionP = Integer.parseInt(attentionPercentage);
		int userId = Integer.parseInt(id);

		narvaroDAO narvaroDaoInstance = new narvaroDAO();

		ArrayList<NarvaroBean> narvaroMethod = narvaroDaoInstance.narvaroPerson();

		int attendance;

		for (NarvaroBean lessonMinutes : narvaroMethod) {

			if (lessonMinutes.getLektion().getDatum().equals(currentLesson())) {

				if (lessonMinutes.getPerson().getPersonId() == userId) {

					attendance = lessonMinutes.getLektion().getMinuter() * attentionP / 100;

					narvaroDaoInstance.updateAttendance(lessonMinutes.getPerson().getPersonId(),
							lessonMinutes.getLektion().getLekId(), attendance);
				}
			}
		}
	}
}