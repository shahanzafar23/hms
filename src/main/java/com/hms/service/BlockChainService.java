package com.hms.service;

import com.hms.blockchain.*;
import com.hms.domain.PatientMedicalRecord;
import com.hms.domain.User;
import com.hms.domain.Wallet;
import com.hms.repository.UserRepository;
import com.hms.repository.WalletRepository;
import com.hms.security.SecurityUtils;
import com.hms.service.dto.PatientMedicalRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlockChainService {
    public static final String blockChainFileName = "blockchain.ser";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    public boolean mineNewBlock(PatientMedicalRecord patientMedicalRecord, Wallet patientWallet) {
        File blockChainFile = new File(blockChainFileName);
        BlockChain blockChain = null;
        if(!blockChainFile.exists()) {
            blockChain = new BlockChain();
            try(
                FileOutputStream fout = new FileOutputStream(blockChainFile);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
            ){
                oos.writeObject(blockChain);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try(
            FileInputStream fin = new FileInputStream(blockChainFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
        ){
            blockChain = (BlockChain) oin.readObject();
            if(blockChain == null) {
                blockChain = new BlockChain();
            }
            Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
            if(currentUserLogin.isPresent()) {
                Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin.get());
                if(oneByLogin.isPresent()){
                    Optional<Wallet> loginWallet = walletRepository.findOneByUserId(oneByLogin.get().getId());
                    Optional<User> patientUser = userRepository.findById(patientWallet.getUserId());
                    if(loginWallet.isPresent() && patientUser.isPresent()){

                        AddressInfo addressInfo = new AddressInfo(patientWallet.getStreet(), patientWallet.getCity(),
                            patientWallet.getState(), patientWallet.getCountry(), patientWallet.getPostCode());
                        PersonalInfo personalInfo = new PersonalInfo(patientUser.get().getFirstName(), patientUser.get().getLastName(),
                            patientWallet.getPhone(), patientUser.get().getEmail(), addressInfo);
                        PatientInfo patientInfo = new PatientInfo(personalInfo, patientUser.get().getId(), patientWallet.getAge() == null ? 0 : patientWallet.getAge());
                        PatientRecordUpdate patientRecordUpdate = new PatientRecordUpdate(loginWallet.get().getPublicKey(),
                            System.currentTimeMillis(), patientMedicalRecord.getPatientCondition(), patientMedicalRecord.getInsuranceClaimed());
                        List<PatientRecordUpdate> updateList = new ArrayList<>();
                        updateList.add(patientRecordUpdate);
                        Block block = new Block(blockChain.getLastHash(), patientWallet.getPublicKey(), patientInfo, updateList);
                        blockChain.addBlock(block);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try(
            FileOutputStream fout = new FileOutputStream(blockChainFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
        ){
            oos.writeObject(blockChain);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public List<PatientMedicalRecordDTO> getMedicalHistory(Wallet patientWallet){
        List<PatientMedicalRecordDTO> patientMedicalRecords = new ArrayList<>();
        File blockChainFile = new File(blockChainFileName);
        BlockChain blockChain = null;
        try(
            FileInputStream fin = new FileInputStream(blockChainFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
        ) {
            blockChain = (BlockChain) oin.readObject();
            if(blockChain == null) {
                return patientMedicalRecords;
            }
            else {
                List<Block> blocks = blockChain.blockExits(patientWallet.getUserId());
                if (!blocks.isEmpty()) {
                    for(Block block: blocks){
                        List<PatientRecordUpdate> recordUpdates = block.getRecordUpdates();
                        if(!recordUpdates.isEmpty()){
                            PatientMedicalRecordDTO patientMedicalRecord = new PatientMedicalRecordDTO();
                            patientMedicalRecord.setPatientCondition(recordUpdates.get(0).getPatientCondition());
                            patientMedicalRecord.setInsuranceClaimed(recordUpdates.get(0).isInsuranceClaimed() ? "YES" : "NO");
                            Date ts=new Timestamp(recordUpdates.get(0).getTimestamp());
                            patientMedicalRecord.setTimestamp(ts.toString());
                            Optional<Wallet> oneByPublicKey = walletRepository.findOneByPublicKey(recordUpdates.get(0).getUpdatedByHash());
                            patientMedicalRecord.setUpdatedBy(oneByPublicKey.get());
                            patientMedicalRecords.add(patientMedicalRecord);
                        }
                    }
                } else {
                    return patientMedicalRecords;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return patientMedicalRecords;
    }
}
