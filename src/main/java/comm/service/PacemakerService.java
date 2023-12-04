package comm.service;

import comm.model.Alarm;
import comm.model.paceMakerEntity;
import comm.repository.PacemakerRepository;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class PacemakerService {

    private PacemakerRepository pacemakerRepository; // Assuming you have a repository to interact with the database
    private final JavaMailSender javaMailSender;
    private final StringEncryptor stringEncryptor;
    @Autowired
    public PacemakerService(PacemakerRepository pacemakerRepository, JavaMailSender javaMailSender, StringEncryptor stringEncryptor) {
        this.pacemakerRepository = pacemakerRepository;
        this.javaMailSender = javaMailSender;
        this.stringEncryptor = stringEncryptor;
    }
    public void startPacemakerForPatient(Long patientId) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);

        if (patient != null) {
            patient.setPacemakerStarted(true);
            pacemakerRepository.save(patient);
        } else {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
    }

    public void stopPacemakerForPatient(Long patientId) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);

        if (patient != null) {
            patient.setPacemakerStarted(false);
            pacemakerRepository.save(patient);
        } else {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
    }

    public void adjustHeartRateForPatient(Long patientId, int newHeartRate) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);

        if (patient != null) {
            patient.setCurrentHeartRate(String.valueOf(newHeartRate));
            pacemakerRepository.save(patient);
        } else {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
    }

    public String getPacemakerStatusForPatient(Long patientId) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);

        if (patient != null) {
            if (patient.isPacemakerStarted()) {
                return "Pacemaker is currently ON for patient ID: " + patientId;
            } else {
                return "Pacemaker is currently OFF for patient ID: " + patientId;
            }
        } else {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
    }

    // Method to simulate changing heart rate every 3 seconds
    public String getCurrentHeartRateForPatient(Long patientId) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);

        if (patient != null) {
            return patient.getCurrentHeartRate();
        } else {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
    }

    // Simulated methods to send email and SMS alerts
    private void sendEmailAlert(long patientId, String subject, String message) {
        // Simulated email sending logic - Print output for Postman
        System.out.println("Sending email alert to healthcare providers...");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("Email sent for patient ID: " + patientId);
    }

    private void sendSMSAlert(String phoneNumber, String subject, String message) {
        // Simulated SMS sending logic - Print output for Postman
        System.out.println("Sending SMS alert to emergency services...");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("SMS sent to " + phoneNumber);
    }
    public List<Alarm> getAlarmsForPatient(Long patientId) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);

        if (patient != null) {
            return patient.getAlarms();
        } else {
            return null;
        }
    }

    public void triggerNotificationForPatient(Long patientId) {
        paceMakerEntity patient = pacemakerRepository.findById(patientId).orElse(null);
        String currentHeartRate = getCurrentHeartRateForPatient(patientId);

        if (currentHeartRate != null && Integer.parseInt(currentHeartRate) > 100) {
            log.info("Critical event detected for patient ID: " + patientId);
            log.info("Sending notification to healthcare providers or emergency services...");

            sendEmailAlert(patientId, "Critical Event Detected",
                    "Heart rate exceeded 100 bpm for patient ID: " + patientId);
            sendSMSAlert("+1234567890", "Critical Event Detected",
                    "Heart rate exceeded 100 bpm for patient ID: " + patientId);
        } else if (currentHeartRate != null && Integer.parseInt(currentHeartRate) < 60) {
            log.info("Critical event detected for patient ID: " + patientId);
            log.info("Sending notification to healthcare providers or emergency services...");

            sendEmailAlert(patientId, "Critical Event Detected",
                    "Heart rate dropped below 60 bpm for patient ID: " + patientId);
            sendSMSAlert("+1234567890", "Critical Event Detected",
                    "Heart rate dropped below 60 bpm for patient ID: " + patientId);

        } else {
            log.info("No critical event detected for patient ID: " + patientId);
        }

        // Sending email notification to a recipient
        if (patient != null) {
            String recipientEmail = "recipient@example.com"; // Replace with the recipient's email
            String subject = "Notification for Patient ID: " + patientId;
            String body = "This is a notification for patient ID: " + patientId;

            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(recipientEmail);
                message.setSubject(subject);
                message.setText(body);

                javaMailSender.send(message);
            } catch (MailException e) {
                // Handle exceptions or log if email sending fails
                log.error("Failed to send email notification: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
    }

    public void addPatientData(paceMakerEntity newPatient) {
        if (newPatient != null) {
            pacemakerRepository.savePatient(newPatient, stringEncryptor);
            log.info("New patient added successfully: " + newPatient.getPatientName());
        } else {
            throw new IllegalArgumentException("Patient details cannot be null");
        }
    }

    public paceMakerEntity getPatientDetailsById(Long patientId) {
        paceMakerEntity patientDetails = pacemakerRepository.findById(patientId).orElse(null);

        if (patientDetails != null) {
            String formattedDateOfBirth = formatMonthAndDate(patientDetails.getDateOfBirth());
            patientDetails.setDateOfBirth(formattedDateOfBirth);

            // Decrypt SSN and mask it
            String decryptedSSN = null;
            try {
                decryptedSSN = stringEncryptor.decrypt(patientDetails.getSsn());
                String maskedSSN = maskSSN(decryptedSSN);
                patientDetails.setSsn(maskedSSN);
            } catch (Exception e) {
                log.error("Error decrypting SSN for patient ID " + patientId + ": " + e.getMessage());
                // Handle decryption errors, such as logging and setting SSN to a default value
                patientDetails.setSsn("SSN Decryption Error");
            }

            return patientDetails;
        } else {
            return null;
        }
    }


    private String maskSSN(String ssn) {
        // Check if SSN is valid
        if (ssn != null && ssn.length() >= 4) {
            String lastFourDigits = ssn.substring(ssn.length() - 4); // Extract last four digits
            String masked = ssn.substring(0, ssn.length() - 4).replaceAll("\\d", "*"); // Mask all but last four digits
            return masked + lastFourDigits; // Combine masked and unmasked parts
        } else {
            return null; // Return null for invalid SSN
        }
    }


    private String formatMonthAndDate(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        LocalDate dob = LocalDate.parse(dateOfBirth);
        return dob.format(formatter);
    }

}
