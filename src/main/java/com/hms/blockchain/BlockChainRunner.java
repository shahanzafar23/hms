package com.hms.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlockChainRunner {

    public static void main(String[] args) {
        System.out.println("*************************** Welcome to BlockChain Hospital Management System ***************************");
        BlockChain blockChain = new BlockChain();
        Wallet employeeWallet = createEmployeeWallet(blockChain);
        System.out.println("You're logged in with the Wallet : " + employeeWallet.getPublicKey());

        int choice = 0;
        do {
            choice = getChoice();
            switch (choice) {
                case 1:
                    PatientInfo patientInfo = getPatientInformation();
                    Wallet patientWallet = new Wallet(patientInfo.getPersonalInfo(), UserType.PATIENT, blockChain);
                    System.out.println("Patient is created with the Wallet Id: " + patientWallet.getPublicKey());
                    PatientRecordUpdate patientMedicalRecord = getPatientMedicalRecord(employeeWallet);
                    List<PatientRecordUpdate> updateList = new ArrayList<>();
                    updateList.add(patientMedicalRecord);
                    Block block = new Block(blockChain.getLastHash(), patientWallet.getPublicKey(), patientInfo, updateList);
                    blockChain.addBlock(block);
                    break;
                case 2:
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Please Enter Patient number: ");
                    String patientNumber = scanner.nextLine();
                    List<Block> blocks = blockChain.blockExits(patientNumber);
                    if (!blocks.isEmpty()) {
                        PatientRecordUpdate medicalRecord = getPatientMedicalRecord(employeeWallet);
                        blocks.get(0).addPatientRecord(medicalRecord);
                        blockChain.setLastHash(blocks.get(0).getCurrentHash());
                    } else {
                        System.err.println("Patient does not exits!");
                    }
                    break;
                case 3:
                    scanner = new Scanner(System.in);
                    System.out.println("Please Enter Patient number: ");
                    patientNumber = scanner.nextLine();
                    List<Block> blocks1 = blockChain.blockExits(patientNumber);
                    if (!blocks1.isEmpty()) {
                        System.out.println("Patient name: " + blocks1.get(0).getPatientInfo().getPersonalInfo().getFirstName() + " " + blocks1.get(0).getPatientInfo().getPersonalInfo().getLastName());
                        for (Block block1 : blocks1) {
                            for (PatientRecordUpdate patientRecordUpdate : block1.getRecordUpdates()) {
                                System.out.println("********** Patient Medical History ************");
                                System.out.println("Timestamp: " + patientRecordUpdate.getTimestamp());
                                System.out.println("Updated By: " + patientRecordUpdate.getUpdatedByHash());
                                System.out.println("Patient Condition: " + patientRecordUpdate.getPatientCondition());
                                System.out.println("Insurance Claimed: " + (patientRecordUpdate.isInsuranceClaimed() ? "Yes" : "No"));
                            }
                        }
                    } else {
                        System.err.println("Patient does not exits!");
                    }
                    break;
                case 4:
                    for (Block block1 : blockChain.getBlocks()) {
                        System.out.println(block1.toString());
                    }
                    break;
                case 5:
                    System.out.println("Block Chain is " + (blockChain.isChainValid() ? "Valid" : "Invalid"));
                    break;
            }
        } while (choice >= 1 && choice <= 5);
    }

    private static PatientRecordUpdate getPatientMedicalRecord(Wallet employeeWallet) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***** Patient Medical Information *****");
        System.out.println("Please Enter Patient Condition i.e. Normal, Critical : ");
        String condition = scanner.nextLine();
        System.out.println("Please Enter If Insurance Claimed (yes/no): ");
        String insuranceClaimed = scanner.nextLine();

        return new PatientRecordUpdate(employeeWallet.getPublicKey(), System.currentTimeMillis(), condition, insuranceClaimed.equalsIgnoreCase("yes"));
    }

    private static int getChoice() {
        System.out.println("Please choose the following actions: ");
        System.out.println("1 - Add Patient Record");
        System.out.println("2 - Update Patient Record");
        System.out.println("3 - View patient Record History");
        System.out.println("4 - View All Patients (BlockChain)");
        System.out.println("5 - Check if BlockChain is valid");
        System.out.println("6 - To Exit");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static Wallet createEmployeeWallet(BlockChain blockChain) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("********** Creating Employee Wallet *************");
        System.out.println("***** Personal Information *****");
        System.out.println("Please Enter FirstName: ");
        String firstName = scanner.nextLine();
        System.out.println("Please Enter LastName: ");
        String lastName = scanner.nextLine();
        System.out.println("Please Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.println("Please Enter Email: ");
        String email = scanner.nextLine();

        System.out.println("***** Address Information *****");
        System.out.println("Please Enter Street: ");
        String street = scanner.nextLine();
        System.out.println("Please Enter Postcode: ");
        String postcode = scanner.nextLine();
        System.out.println("Please Enter City: ");
        String city = scanner.nextLine();
        System.out.println("Please Enter State: ");
        String state = scanner.nextLine();
        System.out.println("Please Enter Country: ");
        String country = scanner.nextLine();

        AddressInfo addressInfo = new AddressInfo(street, city, state, country, postcode);
        PersonalInfo personalInfo = new PersonalInfo(firstName, lastName, phone, email, addressInfo);
        return new Wallet(personalInfo, UserType.EMPLOYEE, blockChain);
    }

    private static PatientInfo getPatientInformation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Patient information to record in block chain:");
        System.out.println("***** Address Information *****");
        System.out.println("Please Enter Street: ");
        String street = scanner.nextLine();
        System.out.println("Please Enter Postcode: ");
        String postcode = scanner.nextLine();
        System.out.println("Please Enter City: ");
        String city = scanner.nextLine();
        System.out.println("Please Enter State: ");
        String state = scanner.nextLine();
        System.out.println("Please Enter Country: ");
        String country = scanner.nextLine();

        System.out.println("***** Personal Information *****");
        System.out.println("Please Enter FirstName: ");
        String firstName = scanner.nextLine();
        System.out.println("Please Enter LastName: ");
        String lastName = scanner.nextLine();
        System.out.println("Please Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.println("Please Enter Email: ");
        String email = scanner.nextLine();

        System.out.println("***** Patient Information *****");
        System.out.println("Please Enter Age: ");
        String ageStr = scanner.nextLine();
        System.out.println("Please Enter Patient Number: ");
        String patientNumber = scanner.nextLine();

        double age = Double.valueOf(ageStr);

        AddressInfo addressInfo = new AddressInfo(street, city, state, country, postcode);
        PersonalInfo personalInfo = new PersonalInfo(firstName, lastName, phone, email, addressInfo);
        PatientInfo patientInfo = new PatientInfo(personalInfo, patientNumber, age);

        return patientInfo;

    }
}
