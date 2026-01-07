package com.sathwikhbhat.patientservice.service;

import com.sathwikhbhat.patientservice.dto.PatientRequestDTO;
import com.sathwikhbhat.patientservice.dto.PatientResponseDTO;
import com.sathwikhbhat.patientservice.exception.EmailAlreadyExistsException;
import com.sathwikhbhat.patientservice.exception.PatientNotFoundException;
import com.sathwikhbhat.patientservice.grpc.BillingServiceGrpcClient;
import com.sathwikhbhat.patientservice.kafka.KafkaProducer;
import com.sathwikhbhat.patientservice.mapper.PatientMapper;
import com.sathwikhbhat.patientservice.model.Patient;
import com.sathwikhbhat.patientservice.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository,
                          BillingServiceGrpcClient billingServiceGrpcClient,
                          KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
        log.info("Fetching all patients");
        List<Patient> patientList = patientRepository.findAll();
        log.info("Found {} patients", patientList.size());
        return patientList.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " +
                    patientRequestDTO.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        log.info("Created patient with ID: {}", patient.getId());

        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());

        kafkaProducer.sendEvent(patient);

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " +
                    patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        log.info("Updated patient with ID: {}", id);
        return PatientMapper.toDTO(updatedPatient);
    }

    @Transactional
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
        log.info("Deleted patient with ID: {}", id);
    }
}
