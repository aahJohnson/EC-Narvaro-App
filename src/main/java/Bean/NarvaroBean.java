package Bean;

public class NarvaroBean {
	
	private PersonBean person;
	private LektionBean lektion;
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
	
	public LektionBean getLektion() {
		return lektion;
	}
	
	public void setLektion(LektionBean lektion) {
		this.lektion = lektion;
	}

	public CourseBean getCourse() {
		return course;
	}

	public void setCourse(CourseBean course) {
		this.course = course;
	}
}