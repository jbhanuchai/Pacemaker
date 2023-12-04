package comm.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@Table(name = "pace_m")
public class paceMakerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long patientId;
	private String patientName;
	private String currentHeartRate;
	private boolean pacemakerStarted;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "pacemaker_entity_id")
	private List<Alarm> alarms = new ArrayList<>();
	private String ssn;
	private String dateOfBirth;

	public paceMakerEntity() {
	}

	public paceMakerEntity(long patientId, String patientName, String currentHeartRate, boolean pacemakerStarted
			, String ssn,String dateOfBirth) {
		super();
		this.patientId = patientId;
		this.patientName = patientName;
		this.currentHeartRate = currentHeartRate;
		this.pacemakerStarted = pacemakerStarted;
		this.ssn = ssn;
		this.dateOfBirth=dateOfBirth;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;

	}

	public String getCurrentHeartRate() {
		return currentHeartRate;
	}

	public void setCurrentHeartRate(String currentHeartRate) {
		this.currentHeartRate = currentHeartRate;
	}

	public boolean isPacemakerStarted() {
		return pacemakerStarted;
	}

	public void setPacemakerStarted(boolean pacemakerStarted) {
		this.pacemakerStarted = pacemakerStarted;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}
	// Method to set the date of birth
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "paceMakerEntity{" + "id=" + patientId + ", name='" + patientName + '\'' + ", heartrate='"
				+ currentHeartRate + '\'' + ", pacemakerStarted='" + pacemakerStarted + '\'' + "," + " alarms=" + alarms
				+ ", ssn='" + ssn + '\'' +'}';
	}
}
