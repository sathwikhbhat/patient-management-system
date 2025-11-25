package com.sathwikhbhat.patientservice.service;

import com.sathwikhbhat.patientservice.dto.PatientResponseDTO;
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
}
