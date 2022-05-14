package com.hms.repository;

import com.hms.domain.PatientMedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PatientMedicalRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientMedicalRecordRepository extends MongoRepository<PatientMedicalRecord, String> {}
