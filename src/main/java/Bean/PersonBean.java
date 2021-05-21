package Bean;

public class PersonBean {
	
	private int personId;
	private String firstName;
	private String lastName;
	private String role;
	private int locationId;
	
	
	public int getPersonId() {
		return personId;
	}
	
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public int getOrtId() {
		return locationId;
	}
	
	public void setOrtId(int ortId) {
		this.locationId = ortId;
	}
}