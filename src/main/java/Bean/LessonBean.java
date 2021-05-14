package Bean;

import java.time.LocalDate;
import java.util.Date;

public class LessonBean {
	
	private int lekId;
	private Date datum;
	private int kursId;
	private int minuter;
	

	public int getMinuter() {
		return minuter;
	}

	public void setMinuter(int minuter) {
		this.minuter = minuter;
	}

	public int getLekId() {
		return lekId;
	}
	
	public void setLekId(int lekId) {
		this.lekId = lekId;
	}
	
	public Date getDatum() {
		return datum;
	}
	
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	
	public int getKursId() {
		return kursId;
	}
	
	public void setKursId(int kursId) {
		this.kursId = kursId;
	}

	@Override
	public String toString() {
		return "LektionBean [lekId=" + lekId + ", datum=" + datum + ", kursId=" + kursId + ", minuter=" + minuter + "]";
	}
}