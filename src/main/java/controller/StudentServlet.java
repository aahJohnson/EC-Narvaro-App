package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.CourseBean;
import Bean.LessonBean;
import Bean.LoginBean;
import Bean.AttendanceBean;
import conDB.AttendanceDAO;
import conDB.UserDAO;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudentServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("attendingPercentage") != null) {
			try {
				lessonAttendancePercentage(request, response);
			} catch (ServletException | IOException | ParseException e) {
				e.printStackTrace();
			}
		} else if (request.getParameter("attendingPercentage") == null) {
			try {
				saveCookie(request, response);
			} catch (ServletException | IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		currentLesson();
		printCurrentLesson(request, response);
		courseAttendance(request, response);
		showLessonDate(request, response);
		lessonAttendance(request, response);
		getAttributeCookie(request, response);
		getCourseName(request, response);

		request.getRequestDispatcher("StudentPage/index.jsp").forward(request, response);

	}
	
	// User id
	
	public int getUserId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");

		String password = request.getParameter("password");

		UserDAO userDaoInstance = new UserDAO();

		LoginBean usersId = userDaoInstance.checkLogin(email, password);

		request.setAttribute("userId", usersId.getUsers_id());

		return usersId.getUsers_id();

	}

	// Courses
	
	public CourseBean getCurrentCourse() throws ServletException, IOException {

		CourseBean courseDate = null;

		AttendanceDAO attendanceDaoInstance = new AttendanceDAO();

		ArrayList<CourseBean> courseList = attendanceDaoInstance.kurs();

		Date date = new Date();

		for (CourseBean coursesInList : courseList) {

			if (coursesInList.getStartDatum().compareTo(date) < 0 && coursesInList.getSlutDatum().compareTo(date) > 0) {
				courseDate = coursesInList;
				break;
			}
		}

		return courseDate;
	}

	public void courseAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		LoginBean login = (LoginBean) session.getAttribute("user");

		double totalAttention = calcTotalParticipation(login.getUsers_id()) * 100;

		request.setAttribute("totalAttention", (int) totalAttention);

		CourseBean currentCourse = getCurrentCourse();

		if (currentCourse != null) {
			double courseAttention = calcTotalParticipationCourse(login.getUsers_id(), currentCourse.getKursId()) * 100;

			request.setAttribute("courseName", currentCourse.getKursNamn());
			request.setAttribute("courseAttention", (int) courseAttention);
		} else {
			String message = "Ingen aktiv kurs";
			request.setAttribute("courseName", message);
		}
	}
	
	public int currentCourseId() throws ServletException, IOException {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<CourseBean> courseList = naDao.kurs();

		Date date = new Date();
		CourseBean courseId = null;

		for (CourseBean kurs : courseList) {

			if (kurs.getStartDatum().compareTo(date) < 0 && kurs.getSlutDatum().compareTo(date) > 0) {
				courseId = kurs;
				break;
			}
		}

		return courseId.getKursId();
	}
	
	public void printCurrentCourseId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer printCourseId = currentCourseId();

		request.setAttribute("currentCourse", printCourseId);
	}
	
	public void getCourseName(HttpServletRequest request, HttpServletResponse response) {
		
		AttendanceDAO naDao = new AttendanceDAO();
		
		ArrayList<CourseBean> courseList = naDao.kurs();
		
		ArrayList<String> saveCourseList = new ArrayList<String>();
		
		for (CourseBean courseName : courseList) {
			
			saveCourseList.add(courseName.getKursNamn());
		}
		
		request.setAttribute("courseNameList", saveCourseList);
	}
	
	public double calcTotalParticipationCourse(int userId, int courseId) throws ServletException, IOException {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> andel = naDao.narvaroPerson();

		Date date = new Date();

		double totalParticipation = 0.0;
		double totalMinutes = 0.0;

		for (AttendanceBean narvaroBean : andel) {// totala Kurs närvaro

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
	
	// Lessons
	
	public Date currentLesson() throws ServletException, IOException {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<LessonBean> lessons = naDao.lektionBeans();

		Date currentDate = new Date();

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy");

		try {
			currentDate = dateFormatter.parse(dateFormatter.format(new Date()));
		} catch (ParseException e) {

			e.printStackTrace();
		}

		Date lessonDate = null;

		for (LessonBean lesson : lessons) {

			if (lesson.getDatum().equals(currentDate)) {

				lessonDate = lesson.getDatum();
				break;
			}

		}

		return lessonDate;

	}

	public void printCurrentLesson(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Date printLesson = currentLesson();

		if (printLesson != null) {
			request.setAttribute("lesson", printLesson);
		} else {
			String message = "Ingen aktiv lektion";
			request.setAttribute("lesson", message);
		}

	}
	
	public void lessonAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		LoginBean login = (LoginBean) session.getAttribute("user");

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> andel = naDao.narvaroPerson();

		ArrayList<Integer> filteredLesson = new ArrayList<Integer>();

		double lessonMinutes = 0;
		double attendanceMinutes = 0;

		for (AttendanceBean narvaroBean : andel) {

			if (narvaroBean.getPerson().getPersonId() == login.getUsers_id()
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
	
	public Date showLessonDate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		LoginBean login = (LoginBean) session.getAttribute("user");

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> lessonDateList = naDao.narvaroPerson();

		ArrayList<Date> filteredDate = new ArrayList<Date>();

		Date allDates = null;

		for (AttendanceBean lessonDate : lessonDateList) {

			if (lessonDate.getPerson().getPersonId() == login.getUsers_id()) {

				filteredDate.add(lessonDate.getLektion().getDatum());

				allDates = lessonDate.getLektion().getDatum();

			}
		}
		
		request.setAttribute("dateLesson", filteredDate);
		return allDates;
	}
	
	public void lessonAttendancePercentage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {

		String id = request.getParameter("userIdNumber");
		String attentionPercentage = request.getParameter("attendingPercentage");

		int userId = Integer.parseInt(id);
		int attentionP = Integer.parseInt(attentionPercentage);

		AttendanceDAO narvaroDaoInstance = new AttendanceDAO();

		ArrayList<AttendanceBean> narvaroMethod = narvaroDaoInstance.narvaroPerson();

		int attendance = 0;

		for (AttendanceBean lessonMinutes : narvaroMethod) {

			if (lessonMinutes.getLektion().getDatum().equals(getCookieDate(request, response))
					&& lessonMinutes.getPerson().getPersonId() == userId) {

				attendance = lessonMinutes.getLektion().getMinuter() * attentionP / 100;

				narvaroDaoInstance.updateAttendance(lessonMinutes.getPerson().getPersonId(),
						lessonMinutes.getLektion().getLekId(), attendance);
			}
		}

	}
	
	public int lessonId() {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<LessonBean> lessonBeanInstance = naDao.lektionBeans();

		Integer id = null;

		for (LessonBean lessonId : lessonBeanInstance) {
			id = lessonId.getLekId();

		}

		return id;
	}

	// Total attendance
	
	public double calcTotalParticipation(int userId) {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> andel = naDao.narvaroPerson();

		double totalParticipation = 0.0;
		double totalMinutes = 0.0;

		Date date = new Date();

		for (AttendanceBean narvaroBean : andel) {// totala närvaro

			if (narvaroBean.getPerson().getPersonId() == userId) {

				if (narvaroBean.getLektion().getDatum().compareTo(date) <= 0) {

					totalParticipation = totalParticipation + narvaroBean.getAndel();
					totalMinutes = totalMinutes + narvaroBean.getLektion().getMinuter();
				}
			}
		}

		return totalParticipation / totalMinutes;

	}

	// Cookies
	
	public void saveCookie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {

		Cookie ck = new Cookie("date", request.getParameter("date"));

		ck.setMaxAge(1800);
		response.addCookie(ck);
	}

	public Date getCookieDate(HttpServletRequest request, HttpServletResponse response) {

		Cookie ck[] = request.getCookies();

		Date date1 = null;

		for (Cookie cookie : ck) {
			try {
				String cookieValue = cookie.getValue();

				date1 = new SimpleDateFormat("yyyy-MM-dd").parse(cookieValue);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return date1;
	}

	public void getAttributeCookie(HttpServletRequest request, HttpServletResponse response) {

		Cookie ck[] = request.getCookies();

		for (Cookie cookie : ck) {
			if (cookie.getValue().length() > 15) {
				String message = "Ingen aktiv lektion";
				request.setAttribute("cookieValue", message);
			} else {
				request.setAttribute("cookieValue", cookie.getValue());
			}
		}
	}
	
}