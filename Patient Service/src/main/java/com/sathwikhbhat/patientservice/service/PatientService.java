package com.sathwikhbhat.patientservice.service;

import com.sathwikhbhat.patientservice.dto.PatientRequestDTO;
import com.sathwikhbhat.patientservice.dto.PatientResponseDTO;
import com.sathwikhbhat.patientservice.exception.EmailAlreadyExistsException;
import com.sathwikhbhat.patientservice.mapper.PatientMapper;
import com.sathwikhbhat.patientservice.model.Patient;
import com.sathwikhbhat.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patientList = patientRepository.findAll();
        return patientList.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists" +
                    patientRequestDTO.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(patient);
    }
}
