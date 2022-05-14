package com.hms.blockchain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {
    private long timestamp;
    private String currentHash;
    private String previousHash;
    private String patientHash;
    private PatientInfo patientInfo;
    //Medical history of a patient
    private List<PatientRecordUpdate> recordUpdates = new ArrayList<>();
    private int nonce;

    public Block(String previousHash, String patientHash, PatientInfo patientInfo, List<PatientRecordUpdate> recordUpdates) {
        this.timestamp = System.currentTimeMillis();
        this.previousHash = previousHash;
        this.recordUpdates = recordUpdates;
        this.patientHash = patientHash;
        this.patientInfo = patientInfo;
        nonce = 0;
        currentHash = calculateHash();
    }

    private String calculateHash() {
        try {
            String data = "";
            for (int index = 0; index < recordUpdates.size(); index++) {
                data += recordUpdates.get(index).toString();
            }
            String input = timestamp + previousHash + data + nonce + patientHash;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addPatientRecord(PatientRecordUpdate patientRecordUpdate) {
        this.recordUpdates.add(patientRecordUpdate);
        this.currentHash = calculateHash();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public void setCurrentHash(String currentHash) {
        this.currentHash = currentHash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public List<PatientRecordUpdate> getRecordUpdates() {
        return recordUpdates;
    }

    public void setRecordUpdates(List<PatientRecordUpdate> recordUpdates) {
        this.recordUpdates = recordUpdates;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getPatientHash() {
        return patientHash;
    }

    public void setPatientHash(String patientHash) {
        this.patientHash = patientHash;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public void mineBlock(int difficulty) {
        nonce = 0;
        String target = new String(new char[difficulty]).replace('\0',
                '0');
        while (!currentHash.substring(0, difficulty).equals(target)) {
            nonce++;
            currentHash = calculateHash();
        }
    }

    @Override
    public String toString() {
        return "Block{" +
                "timestamp=" + timestamp +
                ", currentHash='" + currentHash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", patientHash='" + patientHash + '\'' +
                ", patientInfo=" + patientInfo.toString() +
                ", recordUpdates=" + recordUpdates +
                ", nonce=" + nonce +
                '}';
    }
}
