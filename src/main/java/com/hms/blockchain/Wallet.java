package com.hms.blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;

public class Wallet {
    private String publicKey;
    private String privateKey;
    private PersonalInfo personalInfo;
    private UserType userType;
    private BlockChain blockChain;

    public Wallet(PersonalInfo personalInfo, UserType userType, BlockChain blockChain) {
        this.personalInfo = personalInfo;
        this.userType = userType;
        generateKeys();
        this.blockChain = blockChain;
    }

    private void generateKeys() {
        try {
            KeyPair keyPair;
            String algorithm = "RSA"; //DSA DH etc
            keyPair = KeyPairGenerator.getInstance(algorithm).
                    generateKeyPair();
            privateKey = keyPair.getPrivate().getEncoded().toString();
            publicKey = keyPair.getPublic().getEncoded().toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePatientRecords(String patientNumber, List<PatientRecordUpdate> patientRecordUpdate) {
        if (!checkIfPatientIsUpdatingTheRecord()) {
            for (Block block : this.blockChain.getBlocks()) {
                if (block.getPatientInfo().getPatientNumber().equalsIgnoreCase(patientNumber)) {
                    block.getRecordUpdates().clear();
                    block.setRecordUpdates(patientRecordUpdate);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfPatientIsUpdatingTheRecord() {
        for (Block block : this.blockChain.getBlocks()) {
            if (block.getPatientHash().equalsIgnoreCase(this.publicKey)) {
                return true;
            }
        }
        return false;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public BlockChain getBlockChain() {
        return blockChain;
    }

    public void setBlockChain(BlockChain blockChain) {
        this.blockChain = blockChain;
    }
}
