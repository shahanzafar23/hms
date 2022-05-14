package com.hms.service.dto;

import com.hms.domain.Wallet;

public class PatientMedicalRecordDTO {
    private String timestamp;
    private String patientCondition;
    private String insuranceClaimed;
    private Wallet updatedBy;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public String getInsuranceClaimed() {
        return insuranceClaimed;
    }

    public void setInsuranceClaimed(String insuranceClaimed) {
        this.insuranceClaimed = insuranceClaimed;
    }

    public Wallet getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Wallet updatedBy) {
        this.updatedBy = updatedBy;
    }
}
