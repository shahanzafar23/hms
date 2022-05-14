package com.hms.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PatientMedicalRecord.
 */
@Document(collection = "patient_medical_record")
public class PatientMedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("patient_condition")
    private String patientCondition;

    @Field("insurance_claimed")
    private Boolean insuranceClaimed;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PatientMedicalRecord id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientCondition() {
        return this.patientCondition;
    }

    public PatientMedicalRecord patientCondition(String patientCondition) {
        this.setPatientCondition(patientCondition);
        return this;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public Boolean getInsuranceClaimed() {
        return this.insuranceClaimed;
    }

    public PatientMedicalRecord insuranceClaimed(Boolean insuranceClaimed) {
        this.setInsuranceClaimed(insuranceClaimed);
        return this;
    }

    public void setInsuranceClaimed(Boolean insuranceClaimed) {
        this.insuranceClaimed = insuranceClaimed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientMedicalRecord)) {
            return false;
        }
        return id != null && id.equals(((PatientMedicalRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientMedicalRecord{" +
            "id=" + getId() +
            ", patientCondition='" + getPatientCondition() + "'" +
            ", insuranceClaimed='" + getInsuranceClaimed() + "'" +
            "}";
    }
}
