package comm.repository;

import comm.model.paceMakerEntity;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacemakerRepository extends JpaRepository<paceMakerEntity, Long> {

    default void savePatient(paceMakerEntity patient, StringEncryptor encryptor) {
        String encryptedSSN = encryptor.encrypt(patient.getSsn());
        patient.setSsn(encryptedSSN);
        save(patient);
    }

    default paceMakerEntity findPatientById(Long patientId, StringEncryptor encryptor) {
        paceMakerEntity patient = findById(patientId).orElse(null);
        if (patient != null) {
            String decryptedSSN = encryptor.decrypt(patient.getSsn());
            patient.setSsn(decryptedSSN);
        }
        return patient;
    }

}
