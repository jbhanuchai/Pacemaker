package comm.controller;

import java.util.List;

import comm.model.Alarm;
import comm.model.paceMakerEntity;
import comm.service.PacemakerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PacemakerController {

	private final PacemakerService pacemakerService;

	@PostMapping("/{patientId}/start")
	public ResponseEntity<String> startPacemakerForPatientId(@PathVariable Long patientId) {
		pacemakerService.startPacemakerForPatient(patientId);
		return ResponseEntity.ok("Pacemaker started for patient ID: " + patientId);
	}

	@PostMapping("/{patientId}/stop")
	public ResponseEntity<String> stopPacemakerForPatientId(@PathVariable Long patientId) {
		pacemakerService.stopPacemakerForPatient(patientId);
		return ResponseEntity.ok("Pacemaker stopped for patient ID: " + patientId);
	}

	@PostMapping("/{patientId}/adjust")
	public ResponseEntity<String> adjustHeartRateForPatientId(@PathVariable Long patientId,
															  @RequestParam("newHeartRate") int newHeartRate) {
		pacemakerService.adjustHeartRateForPatient(patientId, newHeartRate);
		return ResponseEntity.ok("Heart rate adjusted for patient ID " + patientId + " to " + newHeartRate);
	}

	@GetMapping("/{patientId}/status")
	public ResponseEntity<String> getPacemakerStatusForPatientId(@PathVariable Long patientId) {
		String status = pacemakerService.getPacemakerStatusForPatient(patientId);
		return ResponseEntity.ok(status);
	}

//	@GetMapping("/{patientId}/getCurrentHeartRate")
//	public ResponseEntity<String> simulateHeartRateChange(@PathVariable Long patientId) {
//		pacemakerService.getCurrentHeartRateForPatient(patientId);
//		return ResponseEntity.ok("Heart rate simulation started for patient ID: " + patientId);
//	}
	@GetMapping("/{patientId}/getCurrentHeartRate")
	public ResponseEntity<String> getCurrentHeartRate(@PathVariable Long patientId) {
		String heartRate = pacemakerService.getCurrentHeartRateForPatient(patientId);
		return ResponseEntity.ok("Current heart rate for patient ID " + patientId + ": " + heartRate);
	}

	@GetMapping("/{patientId}/alarms")
	public ResponseEntity<List<Alarm>> getAlarmsForPatientId(@PathVariable Long patientId) {
		List<Alarm> alarms = pacemakerService.getAlarmsForPatient(patientId);
		if (alarms != null) {
			return ResponseEntity.ok(alarms);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{patientId}/notify")
	public ResponseEntity<String> triggerNotification(@PathVariable Long patientId) {
		pacemakerService.triggerNotificationForPatient(patientId);
		return ResponseEntity.ok("Notification triggered for patient ID: " + patientId);
	}

	@PostMapping("/add-patient")
	public ResponseEntity<String> addNewPatient(@RequestBody paceMakerEntity newPatient) {
		if (newPatient.getPatientName() != null) {
			pacemakerService.addPatientData(newPatient);
			return ResponseEntity.ok("New patient added successfully");
		} else {
			return ResponseEntity.badRequest().body("Patient details cannot be null");
		}
	}


	@GetMapping("/{patientId}/details")
	public ResponseEntity<paceMakerEntity> getPatientDetailsById(@PathVariable Long patientId) {
		paceMakerEntity patientDetails = pacemakerService.getPatientDetailsById(patientId);
		if (patientDetails != null) {
			return ResponseEntity.ok(patientDetails);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}