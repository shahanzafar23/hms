package com.hms.blockchain;

import java.io.Serializable;

public class PatientInfo implements Serializable {
    private PersonalInfo personalInfo;
    private String patientNumber;
    private double age;

    public PatientInfo(PersonalInfo personalInfo, String patientNumber, double age) {
        this.personalInfo = personalInfo;
        this.patientNumber = patientNumber;
        this.age = age;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "PatientInfo{" +
                "personalInfo=" + personalInfo.toString() +
                ", patientNumber='" + patientNumber + '\'' +
                ", age=" + age +
                '}';
    }
}
