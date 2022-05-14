package com.hms.blockchain;

import java.io.Serializable;

public class PatientRecordUpdate implements Serializable {
    private String updatedByHash;
    private long timestamp;
    private String patientCondition;
    private boolean insuranceClaimed;

    public PatientRecordUpdate(String updatedByHash, long timestamp, String patientCondition, boolean insuranceClaimed) {
        this.updatedByHash = updatedByHash;
        this.timestamp = timestamp;
        this.patientCondition = patientCondition;
        this.insuranceClaimed = insuranceClaimed;
    }

    public String getUpdatedByHash() {
        return updatedByHash;
    }

    public void setUpdatedByHash(String updatedByHash) {
        this.updatedByHash = updatedByHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public boolean isInsuranceClaimed() {
        return insuranceClaimed;
    }

    public void setInsuranceClaimed(boolean insuranceClaimed) {
        this.insuranceClaimed = insuranceClaimed;
    }

    @Override
    public String toString() {
        return "PatientRecordUpdate{" +
                "updatedByHash='" + updatedByHash + '\'' +
                ", timestamp=" + timestamp +
                ", patientCondition='" + patientCondition + '\'' +
                ", insuranceClaimed=" + insuranceClaimed +
                '}';
    }
}
