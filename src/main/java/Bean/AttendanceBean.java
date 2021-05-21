package Bean;

public class AttendanceBean {
	
	private PersonBean person;
	private LessonBean lesson;
	private CourseBean course;
	private int attendance;
	
	public int getAndel() {
		return attendance;
	}

	public void setAndel(int andel) {
		this.attendance = andel;
	}

	public PersonBean getPerson() {
		return person;
	}
	
	public void setPerson(PersonBean person) {
		this.person = person;
	}
	
	public LessonBean getLektion() {
		return lesson;
	}
	
	public void setLektion(LessonBean lektion) {
		this.lesson = lektion;
	}

	public CourseBean getCourse() {
		return course;
	}

	public void setCourse(CourseBean course) {
		this.course = course;
	}
}