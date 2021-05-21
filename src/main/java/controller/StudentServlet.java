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
		} else if (request.getParameter("date") != null) {
			try {
				saveDateCookie(request, response);
			} catch (ServletException | IOException | ParseException e) {
				e.printStackTrace();
			}
		} else if (request.getParameter("courseId") != null) {
			saveCourseIdCookie(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		currentLesson();
		getCourseName(request, response);
		printCurrentLesson(request, response);
		courseAttendance(request, response);
		showLessonDate(request, response);
		lessonAttendance(request, response);
		getAttributeDateCookie(request, response);
		getCourseNameList(request, response);

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

		ArrayList<CourseBean> courseList = attendanceDaoInstance.courseInformation();

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

		double courseAttention = 0;

		double totalAttention = calcTotalParticipation(login.getUsers_id()) * 100;

		request.setAttribute("totalAttention", (int) totalAttention);

		if (changeCourseId(request, response) != null) {

			courseAttention = calcTotalParticipationCourse(login.getUsers_id(), changeCourseId(request, response))
					* 100;

		}
		request.setAttribute("courseAttention", (int) courseAttention);

	}

	public void printCurrentCourseId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer printCourseId = changeCourseId(request, response);

		request.setAttribute("currentCourse", printCourseId);
	}

	@SuppressWarnings("unused")
	public void getCourseName(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer currentCourseId = changeCourseId(request, response);

		AttendanceDAO attendanceDaoInstance = new AttendanceDAO();

		ArrayList<CourseBean> courseList = attendanceDaoInstance.courseInformation();

		if (currentCourseId != null) {
			for (CourseBean courseName : courseList) {
				if (currentCourseId == courseName.getKursId()) {
					request.setAttribute("courseName", courseName.getKursNamn());
				}
			}
		} else {
			String message = "Ingen aktiv kurs";
			request.setAttribute("courseName", message);
		}

	}

	public void getCourseNameList(HttpServletRequest request, HttpServletResponse response) {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<CourseBean> courseList = naDao.courseInformation();

		ArrayList<CourseBean> saveCourseList = new ArrayList<CourseBean>();

		for (CourseBean courseName : courseList) {

			saveCourseList.add(courseName);
		}

		request.setAttribute("courseNameList", saveCourseList);
	}

	public double calcTotalParticipationCourse(int userId, int courseId) throws ServletException, IOException {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> andel = naDao.personInformation();

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

	public Integer changeCourseId(HttpServletRequest request, HttpServletResponse response) {

		AttendanceDAO attendanceDaoInstance = new AttendanceDAO();

		ArrayList<CourseBean> courseList = attendanceDaoInstance.courseInformation();

		Date date = new Date();

		Integer courseId = null;

		for (CourseBean course : courseList) {

			if (getCourseIdCookie(request, response) != null) {

				if (course.getKursId() == getCourseIdCookie(request, response)) {
					courseId = course.getKursId();
				}
			}

			else if (course.getStartDatum().compareTo(date) < 0 && course.getSlutDatum().compareTo(date) > 0) {

				courseId = course.getKursId();
				break;
			}
		}
		return courseId;
	}

	// Lessons

	public Date currentLesson() throws ServletException, IOException {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<LessonBean> lessons = naDao.lessonInformation();

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
			String message = "Ingen lektion vald";
			request.setAttribute("lesson", message);
		}

	}

	public void lessonAttendance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		LoginBean login = (LoginBean) session.getAttribute("user");

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> andel = naDao.personInformation();

		ArrayList<Integer> filteredLesson = new ArrayList<Integer>();

		double lessonMinutes = 0;
		double attendanceMinutes = 0;

		for (AttendanceBean narvaroBean : andel) {

			try {

				if (narvaroBean.getPerson().getPersonId() == login.getUsers_id()
						&& narvaroBean.getLektion().getKursId() == changeCourseId(request, response)) {

					lessonMinutes = +narvaroBean.getLektion().getMinuter();
					attendanceMinutes = +narvaroBean.getAndel();
					double attendanceProcentage = attendanceMinutes / lessonMinutes * 100;

					int finalAttendancePercentage = (int) attendanceProcentage;

					filteredLesson.add(finalAttendancePercentage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("lessonAttendance", filteredLesson);

	}

	public Date showLessonDate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		LoginBean login = (LoginBean) session.getAttribute("user");

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> lessonDateList = naDao.personInformation();

		ArrayList<Date> filteredDate = new ArrayList<Date>();

		Date allDates = null;

		for (AttendanceBean lessonDate : lessonDateList) {

			try {
				if (lessonDate.getPerson().getPersonId() == login.getUsers_id()
						&& lessonDate.getLektion().getKursId() == changeCourseId(request, response)) {

					filteredDate.add(lessonDate.getLektion().getDatum());

					allDates = lessonDate.getLektion().getDatum();
				}
			} catch (Exception e) {
				e.printStackTrace();
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

		ArrayList<AttendanceBean> narvaroMethod = narvaroDaoInstance.personInformation();

		int attendance = 0;

		for (AttendanceBean lessonMinutes : narvaroMethod) {

			try {

				if (lessonMinutes.getLektion().getDatum().equals(getDateCookie(request, response))
						&& lessonMinutes.getPerson().getPersonId() == userId) {

					attendance = lessonMinutes.getLektion().getMinuter() * attentionP / 100;

					narvaroDaoInstance.updateAttendance(lessonMinutes.getPerson().getPersonId(),
							lessonMinutes.getLektion().getLekId(), attendance);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public int lessonId() {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<LessonBean> lessonBeanInstance = naDao.lessonInformation();

		Integer id = null;

		for (LessonBean lessonId : lessonBeanInstance) {
			id = lessonId.getLekId();

		}

		return id;
	}

	// Total attendance

	public double calcTotalParticipation(int userId) {

		AttendanceDAO naDao = new AttendanceDAO();

		ArrayList<AttendanceBean> andel = naDao.personInformation();

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

	public void saveDateCookie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {

		Cookie dateCookie = new Cookie("date", request.getParameter("date"));

		dateCookie.setMaxAge(1800);
		response.addCookie(dateCookie);
	}

	public Date getDateCookie(HttpServletRequest request, HttpServletResponse response) {

		Cookie dateCookie[] = request.getCookies();

		Date parseDate = null;

		for (Cookie cookie : dateCookie) {

			try {

				String cookieValue = cookie.getValue();

				parseDate = new SimpleDateFormat("yyyy-MM-dd").parse(cookieValue);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return parseDate;
	}

	public void getAttributeDateCookie(HttpServletRequest request, HttpServletResponse response) {

		Cookie ck[] = request.getCookies();

		for (Cookie cookie : ck) {
			if (cookie.getName().equals("date")) {
				request.setAttribute("cookieValue", cookie.getValue());
			} else {
				request.setAttribute("cookieValue", "Ingen aktiv lektion");
			}
		}
	}

	public void saveCourseIdCookie(HttpServletRequest request, HttpServletResponse response) {

		Cookie ck = new Cookie("courseIdCookie", request.getParameter("courseId"));

		ck.setMaxAge(1800);
		response.addCookie(ck);
	}

	public Integer getCourseIdCookie(HttpServletRequest request, HttpServletResponse response) {

		Cookie ck[] = request.getCookies();

		int courseId = 0;

		for (Cookie cookie : ck) {
			if (cookie.getName().equals("courseIdCookie")) {
				courseId = Integer.parseInt(cookie.getValue());
			}
		}

		return courseId;
	}

}