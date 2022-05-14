import { IWallet, Wallet } from "../wallet/wallet.model";

export interface IPatientMedicalHistory {
    timestamp: string;
    patientCondition: string;
    insuranceClaimed: string;
    updatedBy: IWallet;
  }
  
  export class PatientMedicalHistory implements IPatientMedicalHistory {
    constructor(public timestamp: string, public patientCondition: string, public insuranceClaimed: string, public updatedBy: Wallet) {}
  }
  