package Bean;

public class AttendanceBean {
	
	private PersonBean person;
	private LessonBean lektion;
	private CourseBean course;
	private int andel;
	
	public int getAndel() {
		return andel;
	}

	public void setAndel(int andel) {
		this.andel = andel;
	}

	public PersonBean getPerson() {
		return person;
	}
	
	public void setPerson(PersonBean person) {
		this.person = person;
	}
	
	public LessonBean getLektion() {
		return lektion;
	}
	
	public void setLektion(LessonBean lektion) {
		this.lektion = lektion;
	}

	public CourseBean getCourse() {
		return course;
	}

	public void setCourse(CourseBean course) {
		this.course = course;
	}
}