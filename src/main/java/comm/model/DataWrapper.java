package comm.model;

import java.util.List;

public class DataWrapper {
	private List<paceMakerEntity> patients;

	public List<paceMakerEntity> getPatients() {
		return patients;
	}

    public void setPatients(List<paceMakerEntity> patients) {
        this.patients = patients;
    }

}
